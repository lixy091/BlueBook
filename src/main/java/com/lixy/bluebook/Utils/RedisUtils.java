package com.lixy.bluebook.Utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.lixy.bluebook.DTO.UserDTO;
import com.lixy.bluebook.Entity.LogicEntity;
import com.lixy.bluebook.Entity.User;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static com.lixy.bluebook.Utils.ProjectConstant.*;

/**
 * @author lixy
 */
@Component
public class RedisUtils {

    private final StringRedisTemplate stringRedisTemplate;

    private ThreadPoolExecutor threadPool = new ThreadPoolExecutor(8,10,50,TimeUnit.MILLISECONDS,new ArrayBlockingQueue<>(3),new ThreadPoolExecutor.DiscardOldestPolicy());

    public RedisUtils(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    private Boolean getLock(String key) {
        return stringRedisTemplate.opsForValue().setIfAbsent(key, "L", 5, TimeUnit.SECONDS);
    }

    private void unlock(String key) {
        stringRedisTemplate.delete(key);
    }

    //将用户信息写入Redis并获取一个token
    public String getTokenByUser(User user) {
        String token = RandomUtil.randomString(30);
        UserDTO userDTO = new UserDTO(user);
        stringRedisTemplate.opsForHash().putAll(USER_INFO + token, userDTO.transMap());
        stringRedisTemplate.expire(USER_INFO + token, 30, TimeUnit.MINUTES);
        return token;
    }

    //将实体类写入Redis
    public void set(String key, Object value, Long timeout, TimeUnit timeUnit) {
        String valueJson = JSONUtil.toJsonStr(value);
        stringRedisTemplate.opsForValue().set(key, valueJson, timeout, timeUnit);
    }

    public void set(String key, Object value) {
        String valueJson = JSONUtil.toJsonStr(value);
        stringRedisTemplate.opsForValue().set(key, valueJson);
    }

    //将实体类写入Redis且设置逻辑过期时间
    public void setByLogic(String key, Object value, Long timeout, TimeUnit timeUnit) {
        LogicEntity entity = new LogicEntity(value, LocalDateTime.now().plusSeconds(timeUnit.toSeconds(timeout)));
        set(key, entity);
    }

    //查询Redis并完成缓存穿透以及击穿问题
    public <T, QF> T getBeanFromRedis(String key, QF queryFactor, Class<T> type, Function<QF, T> queryDb) {
        T resBean = null;
        //查询key是否存在
        if (!Objects.requireNonNull(stringRedisTemplate.keys(key)).isEmpty()) {
            //查询Redis
            String beanJson = stringRedisTemplate.opsForValue().get(key);
            //查询到直接返回
            if (!StrUtil.isBlank(beanJson)) {
                return JSONUtil.toBean(beanJson, type);
            }
            return null;
        }
        try {
            //未查询到获取共享锁
            //获取锁失败则休眠一段时间
            if (!getLock(LOCK + key)) {
                Thread.sleep(10);
                getBeanFromRedis(key, queryFactor, type, queryDb);
            }
            //获取锁成功查询数据库
            resBean = queryDb.apply(queryFactor);
            //数据库也未查询到则将null存入Redis并释放锁
            if (resBean == null) {
                set(key, "", 2L, TimeUnit.MINUTES);
                return null;
            }
            //查询到则将数据存入Redis并释放锁
            set(key, resBean, 30L, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            unlock(LOCK + key);
        }
        //返回数据
        return resBean;
    }


    public <T, QF> T getLogicBeanFromRedis(String key, QF queryFactor, Class<T> type, Function<QF, T> queryDb) {
        T resBean;
        //查询Redis
        String beanJson = stringRedisTemplate.opsForValue().get(key);
        //查询到判断是否为空
        if (StrUtil.isNotBlank(beanJson)) {
            LogicEntity logicEntity = JSONUtil.toBean(beanJson, LogicEntity.class);
            resBean = JSONUtil.toBean((JSONObject) logicEntity.getData(), type);
            //判断是否过期
            if (logicEntity.getExpireTime().isAfter(LocalDateTime.now())) {
                return resBean;
            }
            //过期则去获取锁
            if (getLock(LOCK+key)){
                //获取锁成功则开启新线程执行数据库查询并加入Redis
                threadPool.submit(()->{
                    T t = queryDb.apply(queryFactor);
                    setByLogic(key,t,30L,TimeUnit.MINUTES);
                    unlock(LOCK+key);
                });
            }
            //获取锁失败返回旧数据
            return resBean;
        }
        //未命中返回null
        return null;
    }
}
