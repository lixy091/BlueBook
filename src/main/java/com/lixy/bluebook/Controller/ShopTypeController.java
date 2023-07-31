package com.lixy.bluebook.Controller;

import com.lixy.bluebook.Service.ShopTypeService;
import com.lixy.bluebook.Utils.ResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author lixy
 */
@RestController
@Api(tags = "商品类别接口")
@RequestMapping("/shop-type")
public class ShopTypeController {
    @Resource
    private ShopTypeService shopTypeService;

    @ApiOperation("获取商店类别列表")
    @GetMapping("/list")
    public ResponseData getShopTypeList(){
        return shopTypeService.getShopTypeList();
    }
}
