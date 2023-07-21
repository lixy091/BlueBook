package com.lixy.bluebook.Service.Impl;

import com.lixy.bluebook.Dao.OrderMapper;
import com.lixy.bluebook.Dao.VoucherMapper;
import com.lixy.bluebook.Entity.Voucher;
import com.lixy.bluebook.Entity.VoucherOrder;
import com.lixy.bluebook.Service.VoucherService;
import com.lixy.bluebook.Utils.ExceptionEnums;
import com.lixy.bluebook.Utils.IncrIdGenerator;
import com.lixy.bluebook.Utils.ResponseData;
import com.lixy.bluebook.Utils.UserLocal;
import org.redisson.api.RLock;
import org.redisson.api.RateLimiterConfig;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.time.LocalDateTime;

import static com.lixy.bluebook.Utils.ProjectConstant.*;

/**
 * @author lixy
 */
@Service
public class VoucherServiceImpl implements VoucherService {

    @Resource
    private VoucherMapper voucherMapper;
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private IncrIdGenerator idGenerator;
    @Resource
    private RedissonClient redissonClient;
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
        //创建订单
        long orderId = idGenerator.generateIncrId(ORDER+id);
        VoucherOrder order = new VoucherOrder(orderId , UserLocal.getUserDTO().getId(), id , 1 , 2);
        return orderMapper.createVoucherOrder(order) == 0
                ? ResponseData.getInstance(ExceptionEnums.FAILURE.getCode(), ExceptionEnums.FAILURE.getMessage()+"购买失败")
                : ResponseData.getInstance(ExceptionEnums.SUCCESSFUL.getCode(), ExceptionEnums.SUCCESSFUL.getMessage()).setData(orderId);
    }

    @Override
    @Transactional
    public ResponseData buySecKillVoucher(Long id) {
        Voucher voucher = voucherMapper.getVoucherById(id);
        if (voucher == null){
            return ResponseData.getInstance(ExceptionEnums.FAILURE.getCode(), ExceptionEnums.FAILURE.getMessage()+"未找到该优惠券");
        }
        if (LocalDateTime.now().isAfter(voucher.getEndTime()) || LocalDateTime.now().isBefore(voucher.getBeginTime())){
            return ResponseData.getInstance(ExceptionEnums.FAILURE.getCode(), ExceptionEnums.FAILURE.getMessage()+"不在活动时间范围");
        }
        RLock lock = redissonClient.getLock(LOCK+ORDER+UserLocal.getUserDTO().getId());
        boolean isLock = lock.tryLock();
        if (!isLock){
            return ResponseData.getInstance(ExceptionEnums.FAILURE.getCode(), ExceptionEnums.FAILURE.getMessage()+"获取锁失败");
        }
        try {
            if (orderMapper.isBought(UserLocal.getUserDTO().getId()) != 0){
                return ResponseData.getInstance(ExceptionEnums.FAILURE.getCode(), ExceptionEnums.FAILURE.getMessage()+"不可重复购买");
            }
            if (voucher.getStock() <= 0 || voucherMapper.updateVoucherStockById(id) == 0){
                return ResponseData.getInstance(ExceptionEnums.FAILURE.getCode(), ExceptionEnums.FAILURE.getMessage()+"该优惠券已售罄");
            }
            buyVoucher(id);
            return ResponseData.getInstance(ExceptionEnums.SUCCESSFUL.getCode(), ExceptionEnums.SUCCESSFUL.getMessage()).setData("购买成功");
        }finally {
            lock.unlock();
        }
    }
}
