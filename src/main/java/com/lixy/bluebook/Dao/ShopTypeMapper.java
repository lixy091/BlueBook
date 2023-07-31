package com.lixy.bluebook.Dao;

import com.lixy.bluebook.Entity.ShopType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author lixy
 */
@Mapper
public interface ShopTypeMapper {

    List<ShopType> getShopTypeList();
}
