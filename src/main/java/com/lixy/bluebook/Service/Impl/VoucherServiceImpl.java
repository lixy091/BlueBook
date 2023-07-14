package com.lixy.bluebook.Service.Impl;

import com.lixy.bluebook.Dao.VoucherMapper;
import com.lixy.bluebook.Entity.Voucher;
import com.lixy.bluebook.Service.VoucherService;
import com.lixy.bluebook.Utils.ExceptionEnums;
import com.lixy.bluebook.Utils.ResponseData;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author lixy
 */
@Service
public class VoucherServiceImpl implements VoucherService {

    @Resource
    private VoucherMapper voucherMapper;
    @Override
    public ResponseData addVoucher(Voucher voucher) {
        ResponseData data;
        if ( voucherMapper.addVoucher(voucher) == 0){
            data = ResponseData.getInstance(ExceptionEnums.FAILURE.getCode(), ExceptionEnums.FAILURE.getMessage()+"添加失败");
            return data;
        }
        data = ResponseData.getInstance(ExceptionEnums.SUCCESSFUL.getCode(), ExceptionEnums.SUCCESSFUL.getMessage());
        data.setData("购买成功!");
        return data;
    }

    @Override
    public ResponseData addSecKillVoucher(Voucher voucher) {
        ResponseData data;
        if ( voucherMapper.addSecKillVoucher(voucher) == 0){
            data = ResponseData.getInstance(ExceptionEnums.FAILURE.getCode(), ExceptionEnums.FAILURE.getMessage()+"添加失败");
            return data;
        }
        data = ResponseData.getInstance(ExceptionEnums.SUCCESSFUL.getCode(), ExceptionEnums.SUCCESSFUL.getMessage());
        data.setData("购买成功!");
        return data;
    }

    @Override
    public ResponseData buyVoucher(Long id) {
        return null;
    }

    @Override
    public ResponseData buySecKillVoucher(Long id) {
        return null;
    }
}
