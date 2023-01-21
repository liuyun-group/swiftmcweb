package com.liuyun.swiftmcweb.core.web.service;

import com.liuyun.swiftmcweb.core.exception.ErrorCode;
import com.liuyun.swiftmcweb.core.pojo.Message;
import com.liuyun.swiftmcweb.core.pojo.ResponseMessage;
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

    /* ========= 便捷的做出消息响应 */

    default <T> ResponseMessage<T> error(String service, String messtype, Integer errcode, String msg) {
        return ResponseMessage.error(service, messtype, errcode, msg);
    }

    default <T> ResponseMessage<T> error(ResponseMessage<?> result) {
        return ResponseMessage.error(result);
    }

    default <T> ResponseMessage<T> error(String service, String messtype, ErrorCode errorCode) {
        return ResponseMessage.error(service, messtype, errorCode);
    }

    default <T> ResponseMessage<T> error(Message<?> message, Integer errcode, String msg) {
        return ResponseMessage.error(message, errcode, msg);
    }

    default <T> ResponseMessage<T> error(Message<?> message, ErrorCode errorCode) {
        return ResponseMessage.error(message, errorCode);
    }

    default <T> ResponseMessage<T> error(Integer errcode, String msg) {
        return ResponseMessage.error(errcode, msg);
    }

    default <T> ResponseMessage<T> error(ErrorCode errorCode) {
        return ResponseMessage.error(errorCode);
    }

    default <T> ResponseMessage<T> success(String service, String messtype, T data) {
        return ResponseMessage.success(service, messtype, data);
    }

    default <T> ResponseMessage<T> success(Message<?> message, T data) {
        return ResponseMessage.success(message, data);
    }

    default <T> ResponseMessage<T> success(T data) {
        return ResponseMessage.success(data);
    }

    default boolean isSuccess(Integer errcode) {
        return ResponseMessage.isSuccess(errcode);
    }

}
