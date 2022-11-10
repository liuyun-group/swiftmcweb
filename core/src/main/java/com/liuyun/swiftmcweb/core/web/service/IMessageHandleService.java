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
public interface IMessageHandleService {

    /**
     * 获取请求属性参数
     *
     * @return
     */
    default ServletRequestAttributes getRequestAttributes() {
        return ServletUtils.getRequestAttributes();
    }

    /**
     * 获取 Http-Request
     *
     * @return
     */
    default HttpServletRequest getRequest() {
        return ServletUtils.getRequest();
    }

    /**
     * 获取 Http-Response
     *
     * @return
     */
    default HttpServletResponse getResponse() {
        return ServletUtils.getResponse();
    }

    /**
     * 获取当前服务名称
     *
     * @return
     */
    default String getServiceName() {
        return MessageCoreUtil.getMessageHandlerServiceName(this);
    }

}
