package com.mnnu.examine.modules.mall.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.mnnu.examine.common.utils.GwyUtils;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@TableName("gwy_delivery_address")
public class DeliveryAddressVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    private Long id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 地址信息
     */
    @NotBlank
    private String deliveryAddress;
    /**
     * 是否是默认地址0不是，1是
     */
    @NotNull
    private Boolean isDefault;

    /**
     * 区域
     */
    @NotBlank
    private String area;
    /**
     * 的真实姓名
     */
    @NotBlank
    private String realName;
    /**
     * 电话
     */
    @NotNull
    @Pattern(regexp = GwyUtils.REG_PHONE
            , message = "输入正确手机号")
    private String phone;

}
