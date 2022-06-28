package com.mnnu.examine.modules.security.filter;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.JWTValidator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mnnu.examine.common.utils.GwyConstant;
import com.mnnu.examine.common.utils.R;
import com.mnnu.examine.common.utils.RedisUtils;
import com.mnnu.examine.modules.sys.entity.UserEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;

/**
 * @author qiaoh
 */
@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    @Resource(name = "whiteList")
    private String[] whiteList;

    private final Gson gson = new Gson();

    @Value("${tokenKey}")
    private String tokenKey;

    @Resource
    RedisUtils redisUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        for (String string : whiteList) {
            if (requestURI.contains(string)) {
                filterChain.doFilter(request, response);
                return;
            }
        }


        String token = request.getHeader("token");

        // 未登录
        if (StringUtils.isBlank(token)) {
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(401);
            response.getWriter().write(gson.toJson(R.error(GwyConstant.BizCode.ACCESS_DENIED.getCode(), GwyConstant.BizCode.ACCESS_DENIED.getMessage())));
            return;
        }

        boolean verify = JWTUtil.verify(token, tokenKey.getBytes(StandardCharsets.UTF_8));
        if (!verify) {
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(401);
            response.getWriter().write(gson.toJson(R.error(GwyConstant.BizCode.ACCESS_DENIED.getCode(), GwyConstant.BizCode.ACCESS_DENIED.getMessage())));
            return;
        }

        // 判断是否过期
        try {
            JWTValidator.of(token).validateDate(new Date());
        } catch (Exception ignored) {
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(401);
            response.getWriter().write(gson.toJson(R.error(GwyConstant.BizCode.ACCESS_DENIED.getCode(), GwyConstant.BizCode.ACCESS_DENIED.getMessage())));
            return;
        }
        JWT jwt = JWTUtil.parseToken(token);

        Integer id = (Integer) jwt.getPayload("id");
        String username = (String) jwt.getPayload("username");
        String phone = (String) jwt.getPayload("phone");
        String openid = String.valueOf(jwt.getPayload("openid"));

        String userRole = "USER_ROLE_";
        String roles = redisUtils.get(userRole + id);
        if (StringUtils.isEmpty(roles)) {
            throw new CredentialsExpiredException("token无效");
        }
        Gson gson = new Gson();
        Type type = new TypeToken<Collection<SimpleGrantedAuthority>>() {
        }.getType();
        Collection<? extends GrantedAuthority> role = gson.fromJson(roles, type);

        //设置认证主体
        UserEntity userEntity = new UserEntity();
        userEntity.setId(id.longValue());
        userEntity.setUsername(username);
        userEntity.setOpenid(openid);
        userEntity.setPhone(phone);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userEntity, null, role);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
