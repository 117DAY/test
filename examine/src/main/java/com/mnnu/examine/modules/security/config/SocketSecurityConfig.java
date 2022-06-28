package com.mnnu.examine.modules.security.config;

import com.mnnu.examine.common.utils.GwyConstant;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

/**
 * 套接字的安全配置
 *
 * @author qiaoh
 * @date 2021/12/08
 */
@Configuration
public class SocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {
    /**
     * 同源禁用
     *
     * @return boolean
     */
    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }

    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages
                .simpTypeMatchers(SimpMessageType.CONNECT,
                        SimpMessageType.DISCONNECT, SimpMessageType.OTHER).permitAll()
                .simpDestMatchers("/app/**")
                .hasAnyRole(GwyConstant.RoleConstant.TEACHER.getRoleName(),
                        GwyConstant.RoleConstant.STUDENT.getRoleName(),
                        GwyConstant.RoleConstant.TEMPTEACH.getRoleName())
                .simpSubscribeDestMatchers("/message/**")
                .hasAnyRole(GwyConstant.RoleConstant.TEACHER.getRoleName(),
                        GwyConstant.RoleConstant.STUDENT.getRoleName(),
                        GwyConstant.RoleConstant.TEMPTEACH.getRoleName())
                .anyMessage().denyAll();

    }
}
