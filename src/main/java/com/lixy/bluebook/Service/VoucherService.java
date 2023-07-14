package com.lixy.bluebook.Service;

import com.lixy.bluebook.Entity.Voucher;
import com.lixy.bluebook.Utils.ResponseData;

/**
 * @author lixy
 */
public interface VoucherService {

    ResponseData addVoucher(Voucher voucher);

    ResponseData addSecKillVoucher(Voucher voucher);

    ResponseData buyVoucher(Long id);

    ResponseData buySecKillVoucher(Long id);
}
