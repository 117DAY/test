package com.mnnu.examine.modules.security.component;

import cn.hutool.core.date.DateUtil;
import cn.hutool.jwt.JWT;
import com.google.gson.Gson;
import com.mnnu.examine.common.utils.IPUtils;
import com.mnnu.examine.common.utils.R;
import com.mnnu.examine.common.utils.RedisUtils;
import com.mnnu.examine.modules.security.entity.GwyUser;
import com.mnnu.examine.modules.sys.entity.UserEntity;
import com.mnnu.examine.modules.sys.service.RoleUserRelationService;
import com.mnnu.examine.modules.sys.service.UserService;
import com.mnnu.examine.modules.sys.vo.UserRespVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 实现登陆成功返回 R
 *
 * @author qiaoh
 */
@Component
public class JsonAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Resource
    private Gson gson;

    @Resource
    RedisUtils redisUtils;

    @Resource
    UserService userService;
    @Autowired
    RoleUserRelationService roleUserRelationService;
    @Value("${tokenKey}")
    private String tokenKey;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {


        UserEntity userEntity = ((GwyUser) authentication.getDetails()).getUserEntity();
        // 将角色信息存放在redis中
        String userRole = "USER_ROLE_";
        redisUtils.setExpire(userRole + userEntity.getId(), authentication.getAuthorities(), 7, TimeUnit.DAYS);

        //  生成token
        String token = JWT.create().setPayload("id", userEntity.getId())
                .setPayload("username", userEntity.getUsername())
                .setPayload("phone", userEntity.getPhone())
                .setPayload("openid", userEntity.getOpenid())
                .setExpiresAt(DateUtil.offsetDay(new Date(), 7))
                .setKey(tokenKey.getBytes(StandardCharsets.UTF_8))
                .sign();
        //  返回用户数据
        UserRespVO userRespVO = new UserRespVO();
        BeanUtils.copyProperties(userEntity, userRespVO);
        userRespVO.setRoleType(roleUserRelationService.getUserRoleByUserId(String.valueOf(userRespVO.getId())));

        //  更新数据库用户登陆时间和ip
        recordLogin(((UserEntity) authentication.getPrincipal()).getId(),
                IPUtils.getIpAddr(request)
                , LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        response.setContentType("text/json;charset=utf-8");
        response.setHeader("token", token);
        //塞到HttpServletResponse中返回给前台
        response.getWriter().write(gson.toJson(R.ok().put("user", userRespVO)));

    }


    public void recordLogin(Long id, String ip, String time) {
        //  更新数据库用户登陆时间和ip
        UserEntity userEntity = new UserEntity();
        userEntity.setId(id);
        userEntity.setLastLoginIp(ip);
        userEntity.setLastLoginTime(time);
        userService.updateById(userEntity);
    }

}
