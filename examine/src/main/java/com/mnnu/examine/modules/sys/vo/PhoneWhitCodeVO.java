package com.mnnu.examine.modules.sys.vo;

import com.mnnu.examine.common.utils.GwyUtils;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class PhoneWhitCodeVO {

    /**
     *旧手机
     */
    @NotNull
    @Pattern(regexp = GwyUtils.REG_PHONE
            ,message = "输入正确手机号")
    private String oldPhone;
    /**
     *新手机
     */
    @NotNull
    @Pattern(regexp = GwyUtils.REG_PHONE
            ,message = "输入正确手机号")
    private String newPhone;
    /**
     *旧手机的验证码
     */
    @NotNull
    @Pattern(regexp = GwyUtils.REG_PHONE_CODE,message = "输入正确验证码")
    private String oldPhoneCode;
    /**
     * 新手机的验证码
     */
    @NotNull
    @Pattern(regexp = GwyUtils.REG_PHONE_CODE,message = "输入正确验证码")
    private String newPhoneCode;
}
