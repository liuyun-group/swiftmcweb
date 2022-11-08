package com.liuyun.swiftmcweb.core.exception;

import com.liuyun.swiftmcweb.core.exception.enums.ServiceErrorCodeRange;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 业务逻辑异常 Exception
 *
 * @author flyan
 */
@Data
@EqualsAndHashCode(callSuper = true)
public final class ServiceException extends RuntimeException {

    /**
     * 业务错误码
     *
     * @see ServiceErrorCodeRange
     */
    private Integer errcode;
    /**
     * 错误提示
     */
    private String msg;

    /**
     * 空构造方法，避免反序列化问题
     */
    public ServiceException() {
    }

    public ServiceException(ErrorCode errorCode) {
        this.errcode = errorCode.getErrcode();
        this.msg = errorCode.getMsg();
    }

    public ServiceException(Integer errcode, String msg) {
        this.errcode = errcode;
        this.msg = msg;
    }

    public Integer getErrcode() {
        return errcode;
    }

    public ServiceException setErrcode(Integer errcode) {
        this.errcode = errcode;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public ServiceException setMsg(String msg) {
        this.msg = msg;
        return this;
    }

}
