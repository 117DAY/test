package com.mnnu.examine.modules.security.filter;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.JWTValidator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mnnu.examine.common.utils.RedisUtils;
import com.mnnu.examine.modules.sys.entity.UserEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 套接字端拦截器
 *
 * @author qiaoh
 * @date 2021/12/09
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class SocketClientInterceptor implements ChannelInterceptor {

    private final Gson gson = new Gson();

    @Value("${tokenKey}")
    private String tokenKey;

    @Resource
    RedisUtils redisUtils;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            // access authentication header(s)
            // stomp 获取 header
            List<String> list = accessor.getNativeHeader("token");
            if (list != null && list.size() > 0) {

                String token = list.get(0);
                Authentication user = validateToken(token);
                accessor.setUser(user);
            }
        }
        return message;
    }

    private Authentication validateToken(String token) {
        // 未登录
        if (StringUtils.isBlank(token)) {
            return null;
        }

        boolean verify = JWTUtil.verify(token, tokenKey.getBytes(StandardCharsets.UTF_8));
        if (!verify) {
            return null;
        }

        // 判断是否过期
        try {
            JWTValidator.of(token).validateDate(new Date());
        } catch (Exception ignored) {
            return null;
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

        return authentication;

    }
}
