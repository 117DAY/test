package com.mnnu.examine.modules.security.provider;

import com.mnnu.examine.modules.security.authtoken.UsernamePasswordCodeAuthenticationToken;
import com.mnnu.examine.modules.security.entity.GwyUser;
import com.mnnu.examine.modules.security.service.UsernameDetailsService;
import com.mnnu.examine.modules.sys.service.CodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 自定义用户名登录验证
 *
 * @author qiaoh
 */
@Component
public class UserNameAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    UsernameDetailsService userDetailsService;

    @Resource
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    CodeService codeService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        /*
          provider 验证过程中产生的异常需要是指定几种异常中的一种。不然的话不会进 AuthenticationFailureHandler
          而是认为认证过程出现异常，会启用另一种鉴权流程，就是进 AuthenticationEntryPoint
          鉴权异常---鉴权运行过程中的异常
         */
        UsernamePasswordCodeAuthenticationToken auth = (UsernamePasswordCodeAuthenticationToken) authentication;

        String username = auth.getName();
        String password = auth.getCredentials().toString();
        String ip = auth.getIp();
        String code = auth.getCode();


        UserDetails userDetails = userDetailsService.loadUserByUsername(username);


        /*if (codeService.validateCode(ip, code)) {
            throw new BadCredentialsException("验证码错误");
        }*/

        if (userDetails == null || !passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("密码错误");
        }

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(((GwyUser) userDetails).getUserEntity(), null, userDetails.getAuthorities());
        token.setDetails(userDetails);
        return token;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        boolean res = UsernamePasswordCodeAuthenticationToken.class.isAssignableFrom(aClass);
        return res;
    }
}
