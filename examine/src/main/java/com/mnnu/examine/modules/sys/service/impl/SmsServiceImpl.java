package com.mnnu.examine.modules.sys.service.impl;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.google.gson.Gson;
import com.mnnu.examine.common.utils.GwyUtils;
import com.mnnu.examine.modules.sys.service.SmsService;
import com.mnnu.examine.modules.sys.service.UserService;
import com.mnnu.examine.modules.sys.vo.PhoneWhitCodeVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 短信认证
 *
 * @author qiaoh
 */
@Service
public class SmsServiceImpl implements SmsService {
    @Resource
    RedisTemplate redisTemplate;
    @Resource
    UserService userService;

    @Value("${spring.cloud.alicloud.secret-key}")
    private String accessKeySecret;

    @Value("${spring.cloud.alicloud.access-key}")
    private String accessId;


    @Override
    public boolean sendMsg(String phone) {
        DefaultProfile profile = DefaultProfile.getProfile("cn-beijing", accessId, accessKeySecret);

        IAcsClient client = new DefaultAcsClient(profile);

        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(phone);//接收短信的手机号码
        request.setSignName("万鹿网络");//短信签名名称
        request.setTemplateCode("SMS_227715368");//短信模板CODE
        StringBuilder code = new StringBuilder();
        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < 6; i++) {
            code.append(random.nextInt(10));
        }
        redisTemplate.opsForValue().set(phone, code, 5, TimeUnit.MINUTES);
        request.setTemplateParam("{\"code\":\"" + code.toString() + "\"}");//短信模板变量对应的实际值

        try {
            SendSmsResponse response = client.getAcsResponse(request);
            System.out.println(new Gson().toJson(response));
            return true;
        } catch (ServerException e) {
            e.printStackTrace();
            return false;
        } catch (ClientException e) {
            System.out.println("ErrCode:" + e.getErrCode());
            System.out.println("ErrMsg:" + e.getErrMsg());
            System.out.println("RequestId:" + e.getRequestId());
            return false;
        }


    }

    @Override
    public boolean validateCaptcha(String phone, String code) {
        String o = (String) redisTemplate.opsForValue().get(phone);
        if (o == null || StringUtils.isBlank(code) || !o.equalsIgnoreCase(code)) {
            return false;
        } else {
            redisTemplate.expire(phone, 20, TimeUnit.SECONDS);
            return true;
        }
    }

    /**
     * 获取短信验证码
     *
     * @param phone 手机号
     * @return
     * @author JOJO
     */
    @Override
    public boolean getPhoneCode(String phone) {
        boolean matches = GwyUtils.validatePhone(phone);
        if (matches && this.sendMsg(phone)) {

            return true;
        } else {
            return false;
        }
    }

    /**
     * 查询手机的验证码是否正确
     *
     * @param phone
     * @param phoneCode
     * @return
     */
    @Override
    public boolean queryPhoneWithPhone(String phone, String phoneCode) {
        boolean matches = GwyUtils.validatePhone(phone);
        boolean matches1 = GwyUtils.validatePhoneCode(phoneCode);
        if (matches && matches1 && this.validateCaptcha(phone, phoneCode)) {
            return true;
        }
        return false;
    }

    /**
     * 检查旧手机是否存在以及验证是否正确--
     * 检查新手机是否寻找以及验证是否正确
     *
     * @param phoneWhitCodeVO
     * @return
     */
    @Override
    public boolean checkPhoneWithCode(PhoneWhitCodeVO phoneWhitCodeVO) {
        String newPhoneCode = phoneWhitCodeVO.getNewPhoneCode();
        String oldPhoneCode = phoneWhitCodeVO.getOldPhoneCode();
        String oldPhone = phoneWhitCodeVO.getOldPhone();
        String newPhone = phoneWhitCodeVO.getNewPhone();
        // 判断原手机是否存在 不存在放回false
//        if (userService.queryPhoneExist(oldPhone)) {
//            return false;
//        }
        // 判断新手机是否已经存在
        if (!userService.queryPhoneExist(newPhone)) {
            return false;
        }
        // 判断旧手机的验证是否正确
        if (queryPhoneWithPhone(oldPhone, oldPhoneCode)) {
            // 判断新手机的验证码是否正确
            if (queryPhoneWithPhone(newPhone, newPhoneCode)) {
                return true;
            }
        }
        return false;
    }
}
