package com.mnnu.examine.modules.security.component;

import com.google.gson.Gson;
import com.mnnu.examine.common.utils.GwyConstant;
import com.mnnu.examine.common.utils.R;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 无权访问的控制类
 *
 * @author qiaoh
 */
@Component
public class JsonAccessDeniedHandler implements AccessDeniedHandler {
    @Resource
    private Gson gson;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(403);
        response.setContentType("text/json;charset=utf-8");
        //塞到HttpServletResponse中返回给前台
        response.getWriter().write(gson.toJson(R.error(GwyConstant.BizCode.ACCESS_DENIED.getCode(), GwyConstant.BizCode.ACCESS_DENIED.getMessage())));

    }
}
