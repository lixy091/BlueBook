package com.lixy.bluebook.Service.Impl;

import com.lixy.bluebook.Dao.ShopTypeMapper;
import com.lixy.bluebook.Service.ShopTypeService;
import com.lixy.bluebook.Utils.ExceptionEnums;
import com.lixy.bluebook.Utils.ResponseData;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author lixy
 */
@Service
public class ShopTypeServiceImpl implements ShopTypeService {
    @Resource
    private ShopTypeMapper shopTypeMapper;

    @Override
    public ResponseData getShopTypeList() {
        return ResponseData.getInstance(ExceptionEnums.SUCCESSFUL.getCode(), ExceptionEnums.SUCCESSFUL.getMessage()).setData(shopTypeMapper.getShopTypeList());
    }
}
