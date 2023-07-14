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

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import static com.lixy.bluebook.Utils.ProjectConstant.*;

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
//        Shop shop = redisUtils.getBeanFromRedis(CACHE_SHOP+id,id, Shop.class,shopMapper::getShopById);
        Shop shop = redisUtils.getLogicBeanFromRedis(CACHE_LOGIC+id,id, Shop.class,shopMapper::getShopById);
        if (shop == null){
            data = ResponseData.getInstance(ExceptionEnums.FAILURE.getCode(), ExceptionEnums.FAILURE.getMessage()+"商铺不存在");
            return data;
        }
        //将商铺信息返回
        data = ResponseData.getInstance(ExceptionEnums.SUCCESSFUL.getCode(), ExceptionEnums.SUCCESSFUL.getMessage());
        data.setData(shop);
        return data;
    }

    @Override
    public ResponseData updateShop(Shop shop) {
        ResponseData data;
        shop.setUpdateTime(LocalDateTime.now());
        //更新数据库
        if (shopMapper.updateShop(shop) == 0){
            //商铺不存在返回错误信息
            data = ResponseData.getInstance(ExceptionEnums.FAILURE.getCode(), ExceptionEnums.FAILURE.getMessage()+"商铺不存在");
            return data;
        }
        //将Redis缓存删除
        stringRedisTemplate.delete(CACHE_SHOP+shop.getId());
        //返回信息
        data = ResponseData.getInstance(ExceptionEnums.SUCCESSFUL.getCode(), ExceptionEnums.SUCCESSFUL.getMessage());
        data.setData(1);
        return data;
    }

    @Override
    public ResponseData addHotShop(Long id) {
        ResponseData data;
        Shop shop = shopMapper.getShopById(id);
        if (shop == null){
            data = ResponseData.getInstance(ExceptionEnums.FAILURE.getCode(), ExceptionEnums.FAILURE.getMessage()+"商铺不存在");
            return data;
        }
        redisUtils.setByLogic(CACHE_LOGIC+id,shop,2L,TimeUnit.HOURS);
        data = ResponseData.getInstance(ExceptionEnums.SUCCESSFUL.getCode(), ExceptionEnums.SUCCESSFUL.getMessage());
        return data;
    }
}
