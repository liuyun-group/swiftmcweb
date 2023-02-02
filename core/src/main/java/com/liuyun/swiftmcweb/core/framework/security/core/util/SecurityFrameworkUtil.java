package com.liuyun.swiftmcweb.core.framework.security.core.util;

import com.liuyun.swiftmcweb.core.framework.web.core.util.WebFrameworkUtil;
import lombok.experimental.UtilityClass;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

/**
 * 安全服务工具类
 *
 * @author flyan
 * @version 1.0
 * @date 2/1/23
 */
@UtilityClass
public class SecurityFrameworkUtil {

	/**
	 * 从请求中，获得认证 Token
	 *
	 * @param request 请求
	 * @param header 认证 Token 对应的 Header 名字
	 * @return 认证 Token
	 */
	public static String obtainAuthorization(HttpServletRequest request, String header) {
		String authorization = request.getHeader(header);
		if (!StringUtils.hasText(authorization)) {
			return null;
		}
		int index = authorization.indexOf("Bearer ");
		if (index == -1) { // 未找到
			return null;
		}
		return authorization.substring(index + 7).trim();
	}

	/**
	 * 获得当前认证信息
	 *
	 * @return 认证信息
	 */
	public static Authentication getAuthentication() {
		SecurityContext context = SecurityContextHolder.getContext();
		if (context == null) {
			return null;
		}
		return context.getAuthentication();
	}

	/**
	 * 获得当前用户的编号，从上下文中
	 *
	 * @return 用户编号
	 */
	@Nullable
	public static Long getLoginUserId() {
		Authentication authentication = getAuthentication();
		if (authentication == null) {
			return null;
		}
		return authentication.getPrincipal() instanceof Long ? (Long) authentication.getPrincipal() : null;
	}

	public static void setLoginUserId(Long userId,HttpServletRequest request) {
		// 创建 Authentication，并设置到上下文
		Authentication authentication = buildAuthentication(userId, request);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		// 额外设置到 request 中，用于 ApiAccessLogFilter 可以获取到用户编号；
		// 原因是，Spring Security 的 Filter 在 ApiAccessLogFilter 后面，在它记录访问日志时，线上上下文已经没有用户编号等信息
		WebFrameworkUtil.setLoginUserId(request, userId);
	}

	private static Authentication buildAuthentication(Long userId, HttpServletRequest request) {
		// 创建 UsernamePasswordAuthenticationToken 对象
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
				userId, null, Collections.emptyList());
		authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		return authenticationToken;
	}


}
