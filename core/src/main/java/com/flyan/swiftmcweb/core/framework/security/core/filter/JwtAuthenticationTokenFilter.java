package com.flyan.swiftmcweb.core.framework.security.core.filter;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.alibaba.fastjson2.JSON;
import com.flyan.swiftmcweb.core.context.MessageIpcContext;
import com.flyan.swiftmcweb.core.exception.enums.GlobalErrorCodeConstants;
import com.flyan.swiftmcweb.core.framework.security.core.bean.RereadHttpServletRequestWrapper;
import com.flyan.swiftmcweb.core.framework.security.core.enums.JwtConsts;
import com.flyan.swiftmcweb.core.pojo.Message;
import com.flyan.swiftmcweb.core.pojo.ResponseMessage;
import com.flyan.swiftmcweb.core.web.manager.SwiftmcwebAuthManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

/**
 *
 *
 * @author flyan
 * @version 1.0
 * @date 9/15/22
 */
@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private SwiftmcwebAuthManager authManager;

    @Autowired
    private MessageIpcContext context;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        String authorization = request.getHeader(JwtConsts.AUTH_HEADER);

        /* 1. 如果是发送消息接口，那么先校验白名单 */
        if (requestURI.equals("/sendrec")) {
            request = new RereadHttpServletRequestWrapper(request);
            String bodyStr = ServletUtil.getBody(request);
            if(StrUtil.isNotBlank(bodyStr)) {
                Message<?> sendMessage = JSON.parseObject(bodyStr, Message.class);
                if (sendMessage == null) {
                    /* 无消息！ */
                    returnNoMessageResponse(response);
                    return;
                } else {
                    if (context.inAuthWhiteList(sendMessage.getService(), sendMessage.getMesstype())) {
                        /* 白名单，放行 */
                        filterChain.doFilter(request, response);
                        return;
                    } else {
                        /* 不在白名单，必须有 token 信息进行校验 */
                        if (StrUtil.isBlank(authorization)) {
                            returnNoAuthResponse(response);
                            return;
                        }
                    }
                }
            }
        }

        /* 2. 授权校验 */
        if (StrUtil.isNotBlank(authorization)) {
            if(!authManager.validateAuth(authorization)) {
                returnNoAuthResponse(response);
                return;
            }

            /* 记录授权用户信息 */
            Serializable userId = authManager.getUserId(authorization);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userId, null, null);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private void returnNoMessageResponse(HttpServletResponse response) throws IOException {
        response.setStatus(GlobalErrorCodeConstants.NO_MESSAGE.getErrcode());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(JSON.toJSONString(ResponseMessage.error("SYSTEM", "", GlobalErrorCodeConstants.NO_MESSAGE)));
    }

    private void returnNoAuthResponse(HttpServletResponse response) throws IOException {
        response.setStatus(GlobalErrorCodeConstants.UNAUTHORIZED.getErrcode());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(JSON.toJSONString(ResponseMessage.error("SYSTEM", "", GlobalErrorCodeConstants.UNAUTHORIZED)));
    }

}
