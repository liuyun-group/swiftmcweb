package com.liuyun.swiftmcweb.core.web.manager;

import com.liuyun.swiftmcweb.core.exception.ErrorCode;
import com.liuyun.swiftmcweb.core.exception.ServiceException;
import com.liuyun.swiftmcweb.core.util.exception.ServiceExceptionUtil;

/**
 * 基本消息处理器
 *
 * @author flyan
 * @version 1.0
 * @date 11/10/22
 */
public interface IMessageHandleManager {

	default ServiceException exception(Integer code, String msg) {
		return new ServiceException(code, msg);
	}

	default ServiceException exception(ErrorCode errorCode) {
		return ServiceExceptionUtil.exception(errorCode);
	}

	default ServiceException exception(ErrorCode errorCode, Object... params) {
		return ServiceExceptionUtil.exception(errorCode, params);
	}

	/**
	 * 创建指定编号的 ServiceException 的异常
	 *
	 * @param code 编号
	 * @return 异常
	 */
	default ServiceException exception(Integer code) {
		return ServiceExceptionUtil.exception(code);
	}

}
