package com.liuyun.swiftmcweb.core.framework.web.core.util;

import com.liuyun.swiftmcweb.core.pojo.ResponseMessage;
import lombok.experimental.UtilityClass;

import javax.servlet.ServletRequest;

/**
 * 专属于 web 包的工具类
 *
 * @author flyan
 * @version 1.0
 * @date 10/20/22
 */
@UtilityClass
public class WebFrameworkUtil {

    private static final String REQUEST_ATTRIBUTE_RESPONSE_MESSAGE = "response_message";

    public static void setResponseMessage(ServletRequest request, ResponseMessage<?> result) {
        request.setAttribute(REQUEST_ATTRIBUTE_RESPONSE_MESSAGE, result);
    }

}
