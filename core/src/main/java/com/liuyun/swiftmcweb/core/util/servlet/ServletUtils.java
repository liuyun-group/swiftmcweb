package com.liuyun.swiftmcweb.core.util.servlet;

import lombok.experimental.UtilityClass;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author flyan
 * @version 1.0
 * @date 10/26/22
 */
@UtilityClass
public class ServletUtils {

    public static ServletRequestAttributes getRequestAttributes() {
        return ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes()));
    }

    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
    }

    public static HttpServletResponse getResponse() {
        return ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getResponse();
    }

}
