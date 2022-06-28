package com.mnnu.examine.modules.security.component;

import com.google.gson.Gson;
import com.mnnu.examine.common.utils.R;
import com.mnnu.examine.modules.sys.entity.UserEntity;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 登出返回信息
 *
 * @author qiaoh
 */
@Component
public class JsonLogoutSuccessHandler implements LogoutSuccessHandler {

    @Resource
    RedisTemplate<String, String> redisTemplate;
    @Resource
    private Gson gson;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 删除权限信息
        String userRole = "USER_ROLE_";
        // 退出登录也要有令牌，所以要把存放令牌的操作放在所有的过滤器前面
        try {
            redisTemplate.expire(userRole + ((UserEntity) authentication.getPrincipal()).getId(), 5, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write(gson.toJson(R.ok()));
        }
    }
}
