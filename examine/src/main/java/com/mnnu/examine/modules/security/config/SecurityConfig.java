package com.mnnu.examine.modules.security.config;

import com.mnnu.examine.modules.security.component.*;
import com.mnnu.examine.modules.security.filter.CaptchaAuthenticationFilter;
import com.mnnu.examine.modules.security.filter.JwtLoginFilter;
import com.mnnu.examine.modules.security.filter.JwtTokenFilter;
import com.mnnu.examine.modules.security.provider.CaptchaAuthenticationProvider;
import com.mnnu.examine.modules.security.provider.UserNameAuthenticationProvider;
import com.mnnu.examine.modules.security.service.UsernameDetailsService;
import com.mnnu.examine.modules.sys.entity.UserEntity;
import com.mnnu.examine.modules.sys.service.CodeService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

import javax.annotation.Resource;
import java.util.Collections;

/**
 * security 配置类
 *
 * @author qiaoh
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Resource
    UsernameDetailsService userDetailsService;

    @Resource
    JsonAuthenticationEntryPoint authenticationEntryPoint;


    @Resource
    JsonAuthenticationFailureHandler authenticationFailureHandler;

    @Resource
    JsonAuthenticationSuccessHandler authenticationSuccessHandler;

    @Resource
    JsonAccessDeniedHandler accessDeniedHandler;

    @Resource
    JsonLogoutSuccessHandler logoutSuccessHandler;


    @Resource
    CaptchaAuthenticationProvider authenticationProvider;

    @Resource
    UserNameAuthenticationProvider userNameAuthenticationProvider;

    @Resource
    JwtTokenFilter jwtTokenFilter;

    @Resource
    CodeService codeService;


    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CaptchaAuthenticationFilter captchaAuthenticationFilter() {
        CaptchaAuthenticationFilter filter = new CaptchaAuthenticationFilter();
        filter.setAuthenticationManager(new ProviderManager(Collections.singletonList(authenticationProvider)));
        filter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        filter.setAuthenticationFailureHandler(authenticationFailureHandler);

        return filter;
    }

    @Bean
    public JwtLoginFilter jwtLoginFilter() {
        JwtLoginFilter jwtLoginFilter = new JwtLoginFilter();

        jwtLoginFilter.setAuthenticationManager(
                new ProviderManager(Collections.singletonList(userNameAuthenticationProvider)));
        jwtLoginFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        jwtLoginFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
        return jwtLoginFilter;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());

    }

    @Bean
    public WebSecurity webSecurity() {
        return new WebSecurity();
    }

    /**
     * 解决 无法直接注入 AuthenticationManager
     *
     * @return
     * @throws Exception
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean(name = "whiteList")
    public String[] whiteList() {
        return new String[]{
                "/sys/params/get/style",
                "/socketPort",
                "/sys/catchphrase/all",
                "/login",
                "/cap/login",
                "/sys/login/kaptcha",
                "/course/course/list",
                "/course/course/recommend",
                "/course/course/lessonList",
                "/course/course/recommend",
                "/course/course/lessonDetail",
                "/sys/service/list",
                "/sys/params/listentry",
                "/sys/propaganda/all",
                "/sys/pay/notify",
                "/live/live/recommend",
                "/live/live/liveRoom",
                "/course/viewrecord/info/",
                "/sys/pay/return",
                "/sys/service/list",
                "/live/live/rtm0",
                "/sys/user/exist",
                "/sys/user/getphonecode",
                "/sys/user/checkphonecode",
                "/sys/user/phone",
                "/sys/user/register",
                "/course/course/recommend",
//                "/exam/examcategory/tree",
//                "/exam/exam/list",
                "/exam/examcategory/path/all",
                "/sys/roleuserrelation/roleType",
                "/live/live/liveList",
                "/live/live/liveDetail",
                "/__UNI__B389552__20220627213748.apk",

        };
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests().antMatchers(whiteList()).permitAll()
                .antMatchers("/socketPort/**").permitAll()
                .antMatchers(whiteList()).anonymous()
//                .antMatchers("/sys/user/**/{id}").access("@webSecurity.checkUserId(authentication,#id)")
                .anyRequest().authenticated()
                .and().exceptionHandling().authenticationEntryPoint(authenticationEntryPoint).accessDeniedHandler(accessDeniedHandler)
                .and().formLogin().permitAll().successHandler(authenticationSuccessHandler).failureHandler(authenticationFailureHandler)
                .and().logout().permitAll().logoutSuccessHandler(logoutSuccessHandler).invalidateHttpSession(true)
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).sessionFixation().none().and().headers().frameOptions().disable()
                .and().addFilterAt(jwtLoginFilter(), UsernamePasswordAuthenticationFilter.class).addFilterAfter(captchaAuthenticationFilter(), JwtLoginFilter.class)
                .addFilterBefore(jwtTokenFilter, LogoutFilter.class)
        ;

    }
}

class WebSecurity {
    public boolean checkUserId(Authentication authentication, Long id) {
        return ((UserEntity) authentication.getPrincipal()).getId().equals(id);
    }
}
