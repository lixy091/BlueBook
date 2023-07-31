package com.lixy.bluebook.Controller;

import com.lixy.bluebook.Entity.Voucher;
import com.lixy.bluebook.Service.VoucherService;
import com.lixy.bluebook.Utils.ResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author lixy
 */
@RestController
@RequestMapping("voucher")
@Api(tags ="优惠券相关接口")
public class VoucherController {

    @Resource
    private VoucherService voucherService;

    @ApiOperation("新增普通优惠券")
    @PostMapping("addVou")
    public ResponseData addVoucher(
            @ApiParam(value = "优惠券" ,name = "voucher" ,required = true)
            @RequestBody Voucher voucher
    ){
        return voucherService.addVoucher(voucher);
    }

    @ApiOperation("新增秒杀优惠券")
    @PostMapping("addSec")
    public ResponseData addSecKillVoucher(
            @ApiParam(value = "优惠券" ,name = "voucher" ,required = true)
            @RequestBody Voucher voucher
    ){
        return voucherService.addSecKillVoucher(voucher);
    }

    @ApiOperation("购买普通优惠券")
    @PostMapping("buyVou")
    public ResponseData buyVoucher(
            @ApiParam(value = "优惠券id",name = "id",required = true)
            @RequestParam("id") Long id
    ){
        return voucherService.buyVoucher(id);
    }
    //购买限量优惠券
    @ApiOperation("购买限量优惠券")
    @PostMapping("buySecVou")
    public ResponseData buySecKillVoucher(
            @ApiParam(value = "优惠券id",name = "id",required = true)
            @RequestParam("id") Long id
    ){
        return voucherService.buySecKillVoucher(id);
    }
}
