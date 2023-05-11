package com.liuyun.swiftmcweb.core.framework.security.core.handler;

import com.alibaba.fastjson2.JSON;
import com.liuyun.swiftmcweb.core.exception.enums.GlobalErrorCodeConstants;
import com.liuyun.swiftmcweb.core.framework.security.core.util.SecurityFrameworkUtil;
import com.liuyun.swiftmcweb.core.pojo.ResponseMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.ExceptionTranslationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 访问一个需要认证的 URL 资源，已经认证（登录）但是没有权限的情况下，返回 {@link GlobalErrorCodeConstants#FORBIDDEN} 错误码。
 *
 * 补充：Spring Security 通过
 * 	{@link ExceptionTranslationFilter#handleAccessDeniedException(HttpServletRequest, HttpServletResponse, FilterChain, AccessDeniedException)}
 * 方法，调用当前类
 *
 * @author flyan
 * @version 1.0
 * @date 3/14/23
 */
@Slf4j
@SuppressWarnings("JavadocReference")
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
		// 打印 warn 的原因是，不定期合并 warn，看看有没恶意破坏
		log.warn("[commence][访问 URL({}) 时，用户({}) 权限不够]", request.getRequestURI(),
				SecurityFrameworkUtil.getLoginUserId(), accessDeniedException);
		/* 403无权限 */
		response.setStatus(GlobalErrorCodeConstants.FORBIDDEN.getErrcode());
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().write(JSON.toJSONString(ResponseMessage.error("SYSTEM", "error", GlobalErrorCodeConstants.FORBIDDEN)));
	}

}
