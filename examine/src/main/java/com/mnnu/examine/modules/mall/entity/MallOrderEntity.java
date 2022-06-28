package com.mnnu.examine.modules.mall.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author 自动生成
 * @email generat
 * @date 2021-12-05 22:38:40
 */
@Data
@TableName("gwy_mall_order")
public class MallOrderEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId
    private Long id;
    /**
     * 快递上填的名字
     */
    private String realName;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 快递上的手机号
     */
    private String phone;
    /**
     * 快递地址
     */
    private String deliveryAddress;
    /**
     * 订单id
     */
    private String orderId;
    /**
     * 快递单号
     */
    private String expressNum;
    /**
     * 逻辑删除位
     */
    private Integer showStatus;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 更新时间
     */
    private String updateTime;
    /**
     * 商品的id
     */
    private Long productId;
    /**
     * 商品名
     */
    private String productName;
    /**
     * 扣除的积分
     */
    private BigDecimal point;
    /**
     * 地区
     */
    private String area;
}
