package io.renren.modules.mall.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 自动生成
 * @email generat
 * @date 2021-12-05 21:42:48
 */
@Data
@TableName("gwy_delivery_address")
public class DeliveryAddressEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId
    private Long id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 地区信息
     */
    private String area;
    /**
     * 是否是默认地址0不是，1是
     */
    private Integer isDefault;
    /**
     * 的真实姓名
     */
    private String realName;
    /**
     * 电话
     */
    private String phone;
    /**
     * 交货地址
     */
    private String deliveryAddress;
}
