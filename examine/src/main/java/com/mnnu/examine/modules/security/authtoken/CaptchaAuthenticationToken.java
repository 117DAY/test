package com.mnnu.examine.modules.security.authtoken;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;


/**
 * 手机验证码登录的凭据
 *
 * @author qiaoh
 */

public class CaptchaAuthenticationToken extends AbstractAuthenticationToken {
    /**
     * 手机号
     * 认证结束后改为用户id
     */
    private final Object principal;
    /**
     * 验证码
     */
    private String captcha;

    public CaptchaAuthenticationToken(Object principal, String captcha, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.captcha = captcha;
        super.setAuthenticated(true);
    }

    public CaptchaAuthenticationToken(Object principal, String captcha) {
        super(null);
        this.principal = principal;
        this.captcha = captcha;
        setAuthenticated(false);
    }

    @Override
    public Object getCredentials() {
        return this.captcha;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        captcha = null;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException(
                    "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }
        super.setAuthenticated(false);
    }

}
