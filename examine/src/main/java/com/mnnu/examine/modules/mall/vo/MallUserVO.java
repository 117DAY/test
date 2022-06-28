package com.mnnu.examine.modules.mall.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 用户在积分商城兑换商品时需要填写的个人信息
 *
 * @author qiaoh
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MallUserVO {
    /**
     * 真实姓名
     */
    @NotNull
    private String realName;
    /**
     * 电话
     */
    @Pattern(regexp = "\\d{11}")
    private String phone;
    /**
     * 区域
     */
    @NotNull
    private String area;

    /**
     * 交货地址
     */
    @NotNull
    private String deliveryAddress;

}
