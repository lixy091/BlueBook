package com.lixy.bluebook.Controller;

import com.lixy.bluebook.Entity.Shop;
import com.lixy.bluebook.Service.ShopService;
import com.lixy.bluebook.Utils.ResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation("修改商铺信息")
    @PutMapping("updateShop")
    public ResponseData updateShop(
            @ApiParam(value = "商铺详情" , name = "shop", required = true)
            @RequestBody Shop shop
    ){
        return shopService.updateShop(shop);
    }

    @ApiOperation("添加热点数据至Redis")
    @PostMapping("addHot")
    public ResponseData addHotShop(
            @ApiParam(value = "商店id", name = "id" , required = true)
            @RequestParam("id") Long id
    ){
        return shopService.addHotShop(id);
    }

}
