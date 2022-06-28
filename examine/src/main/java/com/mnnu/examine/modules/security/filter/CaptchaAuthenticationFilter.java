package com.mnnu.examine.modules.security.filter;

import com.google.gson.Gson;
import com.mnnu.examine.modules.security.authtoken.CaptchaAuthenticationToken;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author qiaoh
 */
public class CaptchaAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    /**
     * 定义过滤器的请求和方式
     */
    public CaptchaAuthenticationFilter() {
        super(new AntPathRequestMatcher("/cap/login", "POST"));
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        /**
         * 判断请求类型
         */
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }
        /**
         * 获取手机号和验证码
         */
        String collect = null;
        try {
            collect = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        Map map = gson.fromJson(collect, Map.class);

        String phone =
                map.get("phone") == null ? "" : (String) map.get("phone");
        String code =
                map.get("code") == null ? "" : (String) map.get("code");



        /**
         * 生成未授权的 token
         */
        CaptchaAuthenticationToken token = new CaptchaAuthenticationToken(phone, code);


        token.setDetails(authenticationDetailsSource.buildDetails(request));


        return this.getAuthenticationManager().authenticate(token);
    }
}
