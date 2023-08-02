package com.lixy.bluebook;

import com.lixy.bluebook.Dao.ShopMapper;
import com.lixy.bluebook.Entity.Shop;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootTest
class BlueBookApplicationTests {

    @Resource
    private ShopMapper shopMapper;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Test
    void contextLoads() {
        Map<Long, List<Shop>> listMap = shopMapper.getShops().stream().collect(Collectors.groupingBy(Shop::getTypeId));
        for (Map.Entry<Long, List<Shop>> entry : listMap.entrySet()){
            List<RedisGeoCommands.GeoLocation<String>> geoList = entry.getValue()
                    .stream()
                    .map(shop -> new RedisGeoCommands.GeoLocation<>(shop.getId().toString(), new Point(shop.getX(), shop.getY())))
                    .collect(Collectors.toList());
            stringRedisTemplate.opsForGeo().add("shop:geo:"+entry.getKey() , geoList);
        }
    }

}
