package com.lixy.bluebook.Dao;

import com.lixy.bluebook.Entity.Voucher;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author lixy
 */
@Mapper
public interface VoucherMapper {

    long addVoucher(Voucher voucher);

    long addSecKillVoucher(Voucher voucher);

    Voucher getVoucherById(Long id);

    long updateVoucherStockById(Long id);

}
