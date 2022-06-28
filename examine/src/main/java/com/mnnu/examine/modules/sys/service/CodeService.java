package com.mnnu.examine.modules.sys.service;

import java.awt.image.BufferedImage;

/**
 * 普通验证码
 * @author qiaoh
 */
public interface CodeService {
    /**
     * 生成验证码，根据IP存放在redis中
     *
     * @param ip
     * @return
     */
    public BufferedImage createCodeWithIP(String ip);

    /**
     * 验证验证码是否正确
     *
     * @param ip
     * @param code
     * @return
     */
    public boolean validateCode(String ip, String code);
}
