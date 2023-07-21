package com.lixy.bluebook.Dao;

import com.lixy.bluebook.Entity.VoucherOrder;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author lixy
 */
@Mapper
public interface OrderMapper {
    long createVoucherOrder(VoucherOrder order);

    long isBought(long id);
}
