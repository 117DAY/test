package com.mnnu.examine.modules.security.authtoken;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author qiaoh
 */
public class UsernamePasswordCodeAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private String username;
    private String password;
    private String ip;
    private String code;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public UsernamePasswordCodeAuthenticationToken(String principal, String credentials, String code, String ip) {
        super(principal, credentials);
        this.username = principal;
        this.password = credentials;
        this.code = code;
        this.ip = ip;
        setAuthenticated(false);
    }

    public UsernamePasswordCodeAuthenticationToken(String principal, String credentials, String code, String ip, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
        this.username = principal;
        this.password = credentials;
        this.code = code;
        this.ip = ip;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return this.password;
    }

    @Override
    public Object getPrincipal() {
        return this.username;
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        password = null;
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
