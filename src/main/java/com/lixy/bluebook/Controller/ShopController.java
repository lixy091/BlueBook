package com.lixy.bluebook.Controller;

import com.lixy.bluebook.Service.ShopService;
import com.lixy.bluebook.Utils.ResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author lixy
 */
@Api(tags = "商品相关接口")
@RestController
@RequestMapping("/shop")
public class ShopController {
    @Resource
    private ShopService shopService;

    @ApiOperation("根据id查询商铺")
    @GetMapping("getShop/{id}")
    public ResponseData getShopById(
            @ApiParam(value = "商店id", name = "id" , required = true)
            @PathVariable("id") Long id
    ){
        return shopService.getShopById(id);
    }
}
