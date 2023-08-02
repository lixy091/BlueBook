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
import org.apache.ibatis.session.RowBounds;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.domain.geo.GeoReference;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
        Shop shop = redisUtils.getBeanFromRedis(CACHE_SHOP+id,id, Shop.class,shopMapper::getShopById);
//        Shop shop = redisUtils.getLogicBeanFromRedis(CACHE_LOGIC+id,id, Shop.class,shopMapper::getShopById);
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

    @Override
    public ResponseData getShopByName(String name, int currentPage, int pageSize) {
        List<Shop> shops = shopMapper.getShopListByName(name , new RowBounds((currentPage-1)*pageSize , pageSize));
        if (shops == null){
            shops = Collections.emptyList();
        }
        return ResponseData.getInstance(ExceptionEnums.SUCCESSFUL.getCode(), ExceptionEnums.SUCCESSFUL.getMessage()).setData(shops);
    }

    @Override
    public ResponseData getGeoShopByType(int typeId, int current, String sortBy, Double x, Double y) {
        List<Shop> shops;
        if ("comments".equals(sortBy)){
            return ResponseData.getInstance(ExceptionEnums.SUCCESSFUL.getCode(), ExceptionEnums.SUCCESSFUL.getMessage());
        }else if ("score".equals(sortBy)){
            return ResponseData.getInstance(ExceptionEnums.SUCCESSFUL.getCode(), ExceptionEnums.SUCCESSFUL.getMessage());
        }else {
            int from = (current-1)*PAGE_SIZE;
            int end = current*PAGE_SIZE;
            // GEOSEARCH key BYLONLAT x y BYRADIUS 5000 WITHDISTANCE
            GeoResults<RedisGeoCommands.GeoLocation<String>> searchedShop = stringRedisTemplate.opsForGeo().search(SHOP_GEO + typeId
                    , GeoReference.fromCoordinate(x, y)
                    , new Distance(5000)
                    , RedisGeoCommands.GeoSearchCommandArgs.newGeoSearchArgs().includeDistance().limit(end));
            if (searchedShop == null){
                return ResponseData.getInstance(ExceptionEnums.SUCCESSFUL.getCode(), ExceptionEnums.SUCCESSFUL.getMessage()).setData(Collections.emptyList());
            }
            List<GeoResult<RedisGeoCommands.GeoLocation<String>>> content = searchedShop.getContent();
            if (content.isEmpty() || content.size() < from){
                return ResponseData.getInstance(ExceptionEnums.SUCCESSFUL.getCode(), ExceptionEnums.SUCCESSFUL.getMessage()).setData(Collections.emptyList());
            }
            shops = content.stream().skip(from).map(gs -> {
                Shop shop = shopMapper.getShopById(Long.parseLong(gs.getContent().getName()));
                shop.setDistance(gs.getDistance().getValue());
                return shop;
            }).collect(Collectors.toList());
        }
        return ResponseData.getInstance(ExceptionEnums.SUCCESSFUL.getCode(), ExceptionEnums.SUCCESSFUL.getMessage()).setData(shops);
    }
}
