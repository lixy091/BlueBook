package com.lixy.bluebook.Dao;

import com.lixy.bluebook.Entity.Shop;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * @author lixy
 */
@Mapper
public interface ShopMapper {
    Shop getShopById(Long id);

    long updateShop(Shop shop);

    List<Shop> getShopListByName(@Param("name") String name , RowBounds rowBounds);

    List<Shop> getShops();
}
