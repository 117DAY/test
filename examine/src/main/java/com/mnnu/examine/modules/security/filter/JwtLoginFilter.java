package com.mnnu.examine.modules.security.filter;

import com.google.gson.Gson;
import com.mnnu.examine.common.utils.IPUtils;
import com.mnnu.examine.modules.security.authtoken.UsernamePasswordCodeAuthenticationToken;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

public class JwtLoginFilter extends UsernamePasswordAuthenticationFilter {


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }
        String collect = null;
        try {
            collect = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        Map map = gson.fromJson(collect, Map.class);
        String username = (String) map.get("username");
        String password = (String) map.get("password");
        String code = (String) map.get("code");
        if (username == null) {
            username = "";
        }

        if (password == null) {
            password = "";
        }

        if (code == null) {
            code = "";
        }


        // Allow subclasses to set the "details" property
        UsernamePasswordCodeAuthenticationToken authRequest = new UsernamePasswordCodeAuthenticationToken(
                username, password, code, IPUtils.getIpAddr(request));
        setDetails(request, authRequest);


        return this.getAuthenticationManager().authenticate(authRequest);
    }


}
