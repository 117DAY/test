package com.mnnu.examine.modules.sys.vo;

import com.mnnu.examine.common.utils.GwyUtils;
import lombok.Data;

import javax.validation.constraints.*;

@Data
public class RegisterUserVO {

    /**
     * 用户名
     */
    @NotNull
    @Size(max = 10,min = 3)
    private String username;
    /**
     * 密码
     */

    @NotNull
    @Pattern(regexp = GwyUtils.REG_PASSWORD,
            message = "8到20位,包含一个大写字母、一个小写字母、一个数字" )
    private String password;
    /**
     * 用户手机号
     */
    @NotNull
    @Pattern(regexp = GwyUtils.REG_PHONE
            ,message = "输入正确手机号")
    private String phone;

}
