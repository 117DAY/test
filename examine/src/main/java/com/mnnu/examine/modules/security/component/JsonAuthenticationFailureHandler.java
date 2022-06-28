package com.mnnu.examine.modules.security.component;

import com.google.gson.Gson;
import com.mnnu.examine.common.utils.GwyConstant;
import com.mnnu.examine.common.utils.R;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录验证失败处理逻辑
 *
 * @author qiaoh
 */
@Component

public class JsonAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Resource
    private Gson gson;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        R r = null;
        if (exception instanceof AccountExpiredException) {
            //账号过期
            r = R.error(GwyConstant.BizCode.USER_NOT_EXIST.getCode(), GwyConstant.BizCode.USER_NOT_EXIST.getMessage());
        } else if (exception instanceof BadCredentialsException) {
            //密码错误
            r = R.error(GwyConstant.BizCode.PASSWORD_ERROR.getCode(), exception.getMessage());
        } else if (exception instanceof CredentialsExpiredException) {
            //密码过期
            r = R.error(GwyConstant.BizCode.PASSWORD_ERROR.getCode(), GwyConstant.BizCode.PASSWORD_ERROR.getMessage());
        } else if (exception instanceof DisabledException) {
            //账号不可用
            r = R.error(GwyConstant.BizCode.USER_ACCOUNT_DISABLE.getCode(), GwyConstant.BizCode.USER_ACCOUNT_DISABLE.getMessage());
        } else if (exception instanceof LockedException) {
            //账号锁定
            r = R.error();
        } else if (exception instanceof InternalAuthenticationServiceException) {
            //用户不存在
            r = R.error(GwyConstant.BizCode.USER_NOT_EXIST.getCode(), GwyConstant.BizCode.USER_NOT_EXIST.getMessage());

        } else {
            //其他错误
            r = R.error();
        }
        //处理编码方式，防止中文乱码的情况
        response.setContentType("text/json;charset=utf-8");
        response.setStatus(401);
        //塞到HttpServletResponse中返回给前台
        response.getWriter().write(gson.toJson(r));
    }
}
