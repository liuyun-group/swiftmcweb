package com.flyan.swiftmcweb.core.exception;

import com.flyan.swiftmcweb.core.exception.enums.GlobalErrorCodeConstants;
import com.flyan.swiftmcweb.core.exception.enums.ServiceErrorCodeRange;
import lombok.Data;

/**
 * 错误码对象
 *
 * 全局错误码，占用 [0, 999], 参见 {@link GlobalErrorCodeConstants}
 * 业务异常错误码，占用 [1 000 000 000, +∞)，参见 {@link ServiceErrorCodeRange}
 *
 * TODO 错误码设计成对象的原因，为未来的 i18 国际化做准备
 *
 * @author flyan
 * @version 1.0
 * @date 10/20/22
 */
@Data
public class ErrorCode {

    private final Integer errcode;

    private final String msg;

    public ErrorCode(Integer errcode, String msg) {
        this.errcode = errcode;
        this.msg = msg;
    }

}
