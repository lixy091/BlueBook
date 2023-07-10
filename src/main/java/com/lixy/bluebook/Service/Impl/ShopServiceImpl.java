package com.lixy.bluebook.Service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.lixy.bluebook.Dao.ShopMapper;
import com.lixy.bluebook.Entity.Shop;
import com.lixy.bluebook.Service.ShopService;
import com.lixy.bluebook.Utils.ExceptionEnums;
import com.lixy.bluebook.Utils.ProjectConstant;
import com.lixy.bluebook.Utils.RedisUtils;
import com.lixy.bluebook.Utils.ResponseData;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.concurrent.TimeUnit;

import static com.lixy.bluebook.Utils.ProjectConstant.CACHE_SHOP;

/**
 * @author lixy
 */
@Service
public class ShopServiceImpl implements ShopService {
    @Resource
    private ShopMapper shopMapper;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private RedisUtils redisUtils;
    @Override
    public ResponseData getShopById(Long id) {
        ResponseData data;
        //从Redis中查询商铺信息
        String shopJson = stringRedisTemplate.opsForValue().get(CACHE_SHOP+id);
        //若查到直接返回
        if (!StrUtil.isBlank(shopJson)){
            Shop shop = BeanUtil.toBean(shopJson, Shop.class);
            data = ResponseData.getInstance(ExceptionEnums.SUCCESSFUL.getCode(), ExceptionEnums.SUCCESSFUL.getMessage());
            data.setData(shop);
        }
        //未查到则查询数据库
        Shop shop = shopMapper.getShopById(id);
        //数据库中也不存在则返回查询失败并将null存入Redis中
        if (shop == null){
            redisUtils.set(CACHE_SHOP+id,"", 2L, TimeUnit.MINUTES);
            data = ResponseData.getInstance(ExceptionEnums.FAILURE.getCode(), ExceptionEnums.FAILURE.getMessage()+"商铺不存在");
            return data;
        }
        //查到则将查询到的商铺信息保存至redis
        redisUtils.set(CACHE_SHOP+id,shop,30L,TimeUnit.MINUTES);
        //将商铺信息返回
        data = ResponseData.getInstance(ExceptionEnums.SUCCESSFUL.getCode(), ExceptionEnums.SUCCESSFUL.getMessage());
        data.setData(shop);
        return data;
    }
}
