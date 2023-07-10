package com.lixy.bluebook.Utils;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.lixy.bluebook.DTO.UserDTO;
import com.lixy.bluebook.Entity.User;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

import static com.lixy.bluebook.Utils.ProjectConstant.USER_INFO;

/**
 * @author lixy
 */
@Component
public class RedisUtils {

    private final StringRedisTemplate stringRedisTemplate;

    public RedisUtils(StringRedisTemplate stringRedisTemplate){this.stringRedisTemplate = stringRedisTemplate;}

    //将用户信息写入Redis并获取一个token
    public String getTokenByUser(User user){
        String token = RandomUtil.randomString(30);
        UserDTO userDTO = new UserDTO(user);
        stringRedisTemplate.opsForHash().putAll(USER_INFO+token,userDTO.transMap());
        stringRedisTemplate.expire(USER_INFO+token,30, TimeUnit.MINUTES);
        return token;
    }

    //将实体类写入Redis
    public void set(String key , Object value , Long timeout , TimeUnit timeUnit){
        String valueJson = JSONUtil.toJsonStr(value);
        stringRedisTemplate.opsForValue().set(key,valueJson,timeout,timeUnit);
    }

    public void set(String key , Object value){
        String valueJson = JSONUtil.toJsonStr(value);
        stringRedisTemplate.opsForValue().set(key,valueJson);
    }
}
