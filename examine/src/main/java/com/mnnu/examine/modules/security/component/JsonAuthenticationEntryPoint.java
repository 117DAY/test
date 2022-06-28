package com.mnnu.examine.modules.security.component;

import com.google.gson.Gson;
import com.mnnu.examine.common.utils.GwyConstant;
import com.mnnu.examine.common.utils.R;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 将spring security的返回结果调整为统一返回替
 * 匿名用户访问无权限资源的返回
 *
 * @author qiaoh
 */
@Component
public class JsonAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Resource
    private Gson gson;

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        //todo 判断不同的异常类型

        httpServletResponse.setContentType("text/json;charset=utf-8");
        httpServletResponse.setStatus(401);
        httpServletResponse.getWriter().write(gson.toJson(R.error(GwyConstant.BizCode.ACCESS_DENIED.getCode(), "认证过程出现错误，请检查输入的信息是否有误")));
    }
}
