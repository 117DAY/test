package com.mnnu.examine.modules.sys.service.impl;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.mnnu.examine.common.utils.GwyConstant;
import com.mnnu.examine.modules.sys.service.CodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

/**
 * 验证码验证
 *
 * @author qiaoh
 */
@Service
public class CodeServiceImpl implements CodeService {
    @Resource
    RedisTemplate<String, String> redisTemplate;

    @Autowired
    DefaultKaptcha defaultKaptcha;

    @Override
    public BufferedImage createCodeWithIP(String ip) {
        String key = ip + ":code";
        String code = defaultKaptcha.createText();
        // 存放验证码
        redisTemplate.opsForValue().set(key, code, 5, TimeUnit.MINUTES);

        BufferedImage image = defaultKaptcha.createImage(code);
        return image;
    }

    @Override
    public boolean validateCode(@NotNull String ip, @NotNull String code) {
        String key = ip + ":code";
        String rawCode = (String) redisTemplate.opsForValue().get(key);
        if (rawCode == null) {
            throw new BadCredentialsException(GwyConstant.BizCode.CODE_EXPIRED.getMessage());
        }
        redisTemplate.delete(key);

        return code.equalsIgnoreCase(rawCode);
    }
}
