package com.liuyun.swiftmcweb.core.web.service;

import com.liuyun.swiftmcweb.core.util.MessageCoreUtil;
import com.liuyun.swiftmcweb.core.util.servlet.ServletUtils;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 基本消息处理服务接口
 *
 * @author flyan
 * @version 1.0
 * @date 10/18/22
 */
public interface BaseMessageHandleService {

    default ServletRequestAttributes getRequestAttributes() {
        return ServletUtils.getRequestAttributes();
    }

    default HttpServletRequest getRequest() {
        return ServletUtils.getRequest();
    }

    default HttpServletResponse getResponse() {
        return ServletUtils.getResponse();
    }

    default String getServiceName() {
        return MessageCoreUtil.getMessageHandlerServiceName(this);
    }

}
