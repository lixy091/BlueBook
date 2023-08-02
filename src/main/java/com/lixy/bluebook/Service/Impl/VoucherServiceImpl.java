package com.lixy.bluebook.Service.Impl;

import cn.hutool.core.bean.BeanUtil;
import com.lixy.bluebook.Dao.OrderMapper;
import com.lixy.bluebook.Dao.VoucherMapper;
import com.lixy.bluebook.Entity.Voucher;
import com.lixy.bluebook.Entity.VoucherOrder;
import com.lixy.bluebook.Service.VoucherService;
import com.lixy.bluebook.Utils.ExceptionEnums;
import com.lixy.bluebook.Utils.IncrIdGenerator;
import com.lixy.bluebook.Utils.ResponseData;
import com.lixy.bluebook.Utils.UserLocal;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.lixy.bluebook.Utils.ProjectConstant.*;

/**
 * @author lixy
 */
@Service
@Slf4j
public class VoucherServiceImpl implements VoucherService {

    @Resource
    private VoucherMapper voucherMapper;
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private IncrIdGenerator idGenerator;
    @Resource
    private RedissonClient redissonClient;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private static final DefaultRedisScript<Long> SEC_KILL_SCRIPT;
    static {
        SEC_KILL_SCRIPT = new DefaultRedisScript<>();
        SEC_KILL_SCRIPT.setLocation(new ClassPathResource("static/SecKill.lua"));
        SEC_KILL_SCRIPT.setResultType(Long.class);
    }

    private final BlockingQueue<VoucherOrder> secKillQueue = new ArrayBlockingQueue<>(1024*1024);

    private static final ThreadPoolExecutor BUY_THREAD_POOL = new ThreadPoolExecutor(1,2,10, TimeUnit.MILLISECONDS,new ArrayBlockingQueue<>(1024));

    @PostConstruct
    @Transactional(rollbackFor = Throwable.class)
    public void executeKill(){
        BUY_THREAD_POOL.submit(() -> {
            String streamName = "stream.orders";
            while (true){
                try {

                    List<MapRecord<String, Object, Object>> messageList = stringRedisTemplate.opsForStream().read(
                            Consumer.from("g1", "c1")
                            , StreamReadOptions.empty().count(1).block(Duration.ofSeconds(5))
                            , StreamOffset.create(streamName, ReadOffset.lastConsumed()));
                    if (messageList == null || messageList.size() == 0){
                        Thread.sleep(10000);
                        continue;
                    }
                    MapRecord<String, Object, Object> record = messageList.get(0);
                    VoucherOrder order = BeanUtil.fillBeanWithMap(record.getValue(), new VoucherOrder(), true);
                    voucherMapper.updateVoucherStockById(order.getVoucherId());
                    orderMapper.createVoucherOrder(order);
                    stringRedisTemplate.opsForStream().acknowledge(streamName , "g1" ,record.getId());
                }catch (Exception e){
                    try {
                        log.error("执行错误",e);
                        List<MapRecord<String, Object, Object>> messageList = stringRedisTemplate.opsForStream().read(
                                Consumer.from("g1", "c1")
                                , StreamReadOptions.empty().count(1).block(Duration.ofSeconds(5))
                                , StreamOffset.create(streamName, ReadOffset.from("0")));
                        if (messageList == null || messageList.size() == 0){
                            break;
                        }
                        MapRecord<String, Object, Object> record = messageList.get(0);
                        VoucherOrder order = BeanUtil.fillBeanWithMap(record.getValue(), new VoucherOrder(), true);
                        voucherMapper.updateVoucherStockById(order.getVoucherId());
                        orderMapper.createVoucherOrder(order);
                        stringRedisTemplate.opsForStream().acknowledge(streamName , "g1" ,record.getId());
                    }catch (Exception exception){
                        log.error("pending-list执行异常" , exception);
                    }

                }
            }
        });
    }
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
        stringRedisTemplate.opsForValue().set(KILL_STOCK+voucher.getId(),voucher.getStock().toString());
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
        Long userId = UserLocal.getUserDTO().getId();
        long orderId = idGenerator.generateIncrId(ORDER+id);
        Long res = stringRedisTemplate.execute(SEC_KILL_SCRIPT, Collections.emptyList(), id.toString(), userId.toString() , String.valueOf(orderId));
        if (res != 0L){
            return ResponseData.getInstance(ExceptionEnums.FAILURE.getCode(), ExceptionEnums.FAILURE.getMessage()+ (res == 1L ? "库存不足" : "不可重复下单"));
        }

        return ResponseData.getInstance(ExceptionEnums.SUCCESSFUL.getCode(), ExceptionEnums.SUCCESSFUL.getMessage()).setData(id);
    }
}
