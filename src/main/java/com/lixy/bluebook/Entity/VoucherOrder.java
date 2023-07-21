package com.lixy.bluebook.Entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author lixy
 */
@Data
@NoArgsConstructor
public class VoucherOrder {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 下单的用户id
     */
    private Long userId;

    /**
     * 购买的代金券id
     */
    private Long voucherId;

    /**
     * 支付方式 1：余额支付；2：支付宝；3：微信
     */
    private Integer payType;

    /**
     * 订单状态，1：未支付；2：已支付；3：已核销；4：已取消；5：退款中；6：已退款
     */
    private Integer status;

    /**
     * 下单时间
     */
    private LocalDateTime createTime;

    /**
     * 支付时间
     */
    private LocalDateTime payTime;

    /**
     * 核销时间
     */
    private LocalDateTime useTime;

    /**
     * 退款时间
     */
    private LocalDateTime refundTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    {
        createTime = LocalDateTime.now();
        updateTime = createTime;
        payTime = createTime;
    }

    public VoucherOrder(Long id, Long userId, Long voucherId, Integer payType, Integer status) {
        this.id = id;
        this.userId = userId;
        this.voucherId = voucherId;
        this.payType = payType;
        this.status = status;
    }
}
