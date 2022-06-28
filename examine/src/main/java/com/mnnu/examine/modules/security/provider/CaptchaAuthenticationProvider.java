package com.mnnu.examine.modules.security.provider;

import com.mnnu.examine.modules.security.authtoken.CaptchaAuthenticationToken;
import com.mnnu.examine.modules.security.entity.GwyUser;
import com.mnnu.examine.modules.security.service.CaptchaDetailsService;
import com.mnnu.examine.modules.sys.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 手机验证码登录的验证器
 *
 * @author qiaoh
 */
@Component
public class CaptchaAuthenticationProvider implements AuthenticationProvider {
    @Resource
    SmsService smsService;
    @Autowired
    CaptchaDetailsService captchaDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String phone = authentication.getName();
        String rawCode = authentication.getCredentials().toString();

        UserDetails user = captchaDetailsService.loadUserByUsername(phone);

        if (!smsService.validateCaptcha(phone, rawCode)) {
            throw new BadCredentialsException("验证码错误");
        }
        CaptchaAuthenticationToken token = new CaptchaAuthenticationToken(((GwyUser) user).getUserEntity(), null, user.getAuthorities());
        token.setDetails(user);
        return token;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        boolean res = CaptchaAuthenticationToken.class.isAssignableFrom(aClass);
        return res;
    }
}
