package com.liuyun.swiftmcweb.core.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.liuyun.swiftmcweb.core.exception.ErrorCode;
import com.liuyun.swiftmcweb.core.exception.ServiceException;
import com.liuyun.swiftmcweb.core.exception.enums.GlobalErrorCodeConstants;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.util.Assert;

import java.util.Objects;

/**
 * 消息通信接口响应的消息，继承自 {@link Message}，单独定义该消息对象是因为接口
 * 响应的信息并不是所有情况都需要的，例如对于内部消息通信或微服务下的远程调用，它们都有
 * 可能只使用普通消息传递即可。
 *
 * @author flyan
 * @version 1.0
 * @date 10/20/22
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ResponseMessage<T> extends Message<T> {

    /**
     * 错误码
     */
    private Integer errcode;

    /**
     * 错误提示，用户可阅读
     */
    private String msg;

    public ResponseMessage(Message<?> message) {
        this.service = message.service;
        this.messtype = message.messtype;
    }

    public ResponseMessage() {  }

    public <R> ResponseMessage<R> baseClone() {
        return (ResponseMessage<R>) new ResponseMessage<R>().setService(service).setMesstype(messtype);
    }

    public static <T> ResponseMessage<T> error(String service, String messtype, Integer errcode, String msg) {
        Assert.isTrue(!GlobalErrorCodeConstants.SUCCESS.getErrcode().equals(errcode), "code 必须是错误的！");
        ResponseMessage<T> result = new ResponseMessage<>();
        result.setService(service);
        result.setMesstype(messtype);
        result.errcode = errcode;
        result.msg = msg;
        return result;
    }

    public static <T> ResponseMessage<T> error(ResponseMessage<?> result) {
        return error(result.getService(), result.getMesstype(), result.getErrcode(), result.getMsg());
    }

    public static <T> ResponseMessage<T> error(String service, String messtype, ErrorCode errorCode) {
        return error(service, messtype, errorCode.getErrcode(), errorCode.getMsg());
    }

    /* === */

    public static <T> ResponseMessage<T> error(Message<?> message, Integer errcode, String msg) {
        return error(message.service, message.messtype, errcode, msg);
    }

    public static <T> ResponseMessage<T> error(Message<?> message, ErrorCode errorCode) {
        return error(message.service, message.messtype, errorCode.getErrcode(), errorCode.getMsg());
    }

    /* ==== */

    public static <T> ResponseMessage<T> error(Integer errcode, String msg) {
        Assert.isTrue(!GlobalErrorCodeConstants.SUCCESS.getErrcode().equals(errcode), "code 必须是错误的！");
        ResponseMessage<T> result = new ResponseMessage<>();
        result.errcode = errcode;
        result.msg = msg;
        return result;
    }

    public static <T> ResponseMessage<T> error(ErrorCode errorCode) {
        return error(errorCode.getErrcode(), errorCode.getMsg());
    }

    /* ==== */

    public static <T> ResponseMessage<T> success(String service, String messtype, T data) {
        ResponseMessage<T> result = new ResponseMessage<>();
        result.setService(service);
        result.setMesstype(messtype);
        result.errcode = GlobalErrorCodeConstants.SUCCESS.getErrcode();
        result.msg = GlobalErrorCodeConstants.SUCCESS.getMsg();
        result.setData(data);
        return result;
    }

    public static <T> ResponseMessage<T> success(Message<?> message, T data) {
        return success(message.service, message.messtype, data);
    }

    public static <T> ResponseMessage<T> success(T data) {
        ResponseMessage<T> result = new ResponseMessage<>();
        result.errcode = GlobalErrorCodeConstants.SUCCESS.getErrcode();
        result.msg = GlobalErrorCodeConstants.SUCCESS.getMsg();
        result.setData(data);
        return result;
    }

    public static boolean isSuccess(Integer errcode) {
        return Objects.equals(errcode, GlobalErrorCodeConstants.SUCCESS.getErrcode());
    }

    @JsonIgnore // 避免 jackson 序列化
    public boolean isSuccess() {
        return isSuccess(errcode);
    }

    @JsonIgnore // 避免 jackson 序列化
    public boolean isError() {
        return !isSuccess();
    }

    // ========= 和 Exception 异常体系集成 =========

    /**
     * 判断是否有异常。如果有，则抛出 {@link ServiceException} 异常
     */
    public void checkError() throws ServiceException {
        if (isSuccess()) {
            return;
        }
        // 业务异常
        throw new ServiceException(errcode, msg);
    }

    public static <T> ResponseMessage<T> error(ServiceException serviceException) {
        return error(serviceException.getErrcode(), serviceException.getMessage());
    }


}
