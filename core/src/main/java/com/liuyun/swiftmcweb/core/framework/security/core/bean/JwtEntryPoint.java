package com.liuyun.swiftmcweb.core.framework.security.core.bean;

import com.alibaba.fastjson2.JSON;
import com.liuyun.swiftmcweb.core.exception.enums.GlobalErrorCodeConstants;
import com.liuyun.swiftmcweb.core.pojo.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author flyan
 * @version 1.0
 * @date 9/15/22
 */
public class JwtEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(JSON.toJSONString(ResponseMessage.error("SYSTEM", "", GlobalErrorCodeConstants.FORBIDDEN)));
    }

}
