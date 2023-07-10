package com.lixy.bluebook.Dao;

import com.lixy.bluebook.Entity.Shop;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author lixy
 */
@Mapper
public interface ShopMapper {
    Shop getShopById(Long id);

    long updateShop(Shop shop);
}
