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

    @ApiOperation("根据name获取商铺分页信息")
    @GetMapping("/name")
    public ResponseData getShopByName(
            @ApiParam(value = "name",name = "商铺名称")
            @RequestParam(value = "name" , required = false) String name,
            @ApiParam(value = "currentPage" , name = "当前页码")
            @RequestParam(value = "currentPage" , defaultValue = "1") int currentPage,
            @ApiParam(value = "pageSize" , name = "每页条数")
            @RequestParam(value = "pageSize" , defaultValue = "10") int pageSize
    ){
        return shopService.getShopByName(name , currentPage , pageSize);
    }

    @ApiOperation("根据分类获取商铺分页及地理信息")
    @GetMapping("/ofType")
    public ResponseData getGeoShopByType(
            @ApiParam(value = "" , name = "" , required = true)
            @RequestParam("typeId") int typeId,
            @ApiParam(value = "" , name = "" , required = true)
            @RequestParam("current") int current,
            @ApiParam(value = "" , name = "" )
            @RequestParam( value = "sortBy" , required = false , defaultValue = "") String sortBy,
            @ApiParam(value = "" , name = "" )
            @RequestParam( value = "x" , required = false) Double x,
            @ApiParam(value = "" , name = "" )
            @RequestParam( value = "y" , required = false) Double y
    ){
        return shopService.getGeoShopByType(typeId, current, sortBy, x, y);
    }

}
