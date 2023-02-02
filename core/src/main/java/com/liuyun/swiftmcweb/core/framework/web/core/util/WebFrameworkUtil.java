package com.liuyun.swiftmcweb.core.framework.web.core.util;

import com.liuyun.swiftmcweb.core.pojo.ResponseMessage;
import org.springframework.boot.autoconfigure.web.WebProperties;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

public class WebFrameworkUtil {

    private static final String REQUEST_ATTRIBUTE_LOGIN_USER_ID = "login_user_id";
    private static final String REQUEST_ATTRIBUTE_RESPONSE_MESSAGE = "response_message";

    private static WebProperties properties;

    public WebFrameworkUtil(WebProperties webProperties) {
        WebFrameworkUtil.properties = webProperties;
    }

    public static void setLoginUserId(ServletRequest request, Long userId) {
        request.setAttribute(REQUEST_ATTRIBUTE_LOGIN_USER_ID, userId);
    }

    /**
     * 获得当前用户的编号，从请求中
     * 注意：该方法仅限于 framework 框架使用！！！
     *
     * @param request 请求
     * @return 用户编号
     */
    public static Long getLoginUserId(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        return (Long) request.getAttribute(REQUEST_ATTRIBUTE_LOGIN_USER_ID);
    }

    public static void setResponseMessage(ServletRequest request, ResponseMessage<?> result) {
        request.setAttribute(REQUEST_ATTRIBUTE_RESPONSE_MESSAGE, result);
    }

}
