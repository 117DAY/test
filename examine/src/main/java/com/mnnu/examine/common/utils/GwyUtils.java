package com.mnnu.examine.common.utils;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.teaopenapi.models.Config;
import com.mnnu.examine.modules.sys.entity.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * 其他工具类
 * 1、短信验证码
 * 2、普通验证码
 *
 * @author qiaoh
 */
@Component
public class GwyUtils {


    public static final String REG_PHONE = "^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$";
    public static final String REG_PHONE_CODE = "^[a-zA-Z0-9]{6}$";
    public static final String REG_PASSWORD = "^(?=.*[a-zA-Z])(?=.*[0-9])[A-Za-z0-9]{8,18}$";
    public static final String REG_LESSON_ID = "^[\\d]+$";
    public static final String REG_LESSON_TYPE = "^[0-1]$";
    public static final String REG_USERNAME = "^[\\u4e00-\\u9fa5a-zA-Z0-9_]{3,10}$";


    /**
     * 验证手机是否为11位数字
     *
     * @param phone 手机号
     * @return boolean
     * @author JOJO
     */
    public static boolean validatePhone(String phone) {
        return phone.matches(REG_PHONE);
    }

    /**
     * @param username
     * @return
     * @author JOJO
     */
    public static boolean validateUsername(String username) {
        return username.matches(REG_USERNAME);
    }

    /**
     * 验证手机验证码是否位4位数字
     *
     * @param phoneCode 手机验证码
     * @return boolean
     * @author JOJO
     */
    public static boolean validatePhoneCode(String phoneCode) {
        return phoneCode.matches(REG_PHONE_CODE);
    }

    /**
     * 验证密码是否为8到20位,包含一个大写字母、一个小写字母、一个数字
     *
     * @param password 密码
     * @return boolean
     * @author JOJO
     */
    public static boolean validatePassword(String password) {
        return password.matches(REG_PASSWORD);
    }

    /**
     * 生成订单
     *
     * @return {@link String}
     */
    public static String generateOrder(Long userId) {
        return "" + System.currentTimeMillis() + userId;
    }


    /**
     * 获取当前登录用户
     *
     * @return {@link UserEntity}
     */
    public static UserEntity getUser() {
        return (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Value("${spring.cloud.alicloud.access-key}")
    private String accessKeyId;
    @Value("${spring.cloud.alicloud.secret-key}")
    private String accessKeySecret;

    /**
     * 发送消息
     * todo
     *
     * @param phone 电话
     * @return {@link Boolean}
     */
    public Boolean sendMessage(String phone) {
        Config config = new Config();
        config.setAccessKeyId(accessKeyId);
        config.setAccessKeySecret(accessKeySecret);

        try {
            Client client = new Client(config);
            new SendSmsRequest()
                    .setPhoneNumbers(phone)
                    .setTemplateCode("SMS_227715368");
//                    .setTemplateParam()
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
