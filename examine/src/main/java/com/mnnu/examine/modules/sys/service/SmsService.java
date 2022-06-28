package com.mnnu.examine.modules.sys.service;

import com.mnnu.examine.modules.sys.vo.PhoneWhitCodeVO;

/**
 * @author qiaoh
 */
public interface SmsService {

    /**
     * 发送手机验证码
     *
     * @param phone
     * @return
     */
    boolean sendMsg(String phone);

    /**
     * 验证手机验证码是否合法
     *
     * @param phone
     * @param code
     * @return
     */
    boolean validateCaptcha(String phone, String code);
    boolean getPhoneCode(String phone);
    /**
     * 查询手机的验证码是否正确
     * @param phone
     * @param phoneCode
     * @return
     * @author JOJO
     */
    boolean queryPhoneWithPhone(String phone,String phoneCode);
    /**检查旧手机是否存在以及验证是否正确--
     * 检查新手机是否寻找以及验证是否正确
     * @param phoneWhitCodeVO
     * @return
     * @author JOJO
     */
    boolean checkPhoneWithCode(PhoneWhitCodeVO phoneWhitCodeVO);

}
