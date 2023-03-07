package com.liuyun.swiftmcweb.core.web.manager.log;

import com.liuyun.swiftmcweb.core.web.manager.IMessageHandleManager;
import com.liuyun.swiftmcweb.core.web.service.vo.log.LogQueryVO;

/**
 * 流云日志管理器
 *
 * @author flyan
 * @version 1.0
 * @date 3/6/23
 */
public interface LiuyunLogManager extends IMessageHandleManager {

	/**
	 * 获取当天日志文件信息
	 */
	String currentDayLog(LogQueryVO logQueryVO);

	/**
	 * 获取当天的启动日志文件信息
	 */
	String currentDayStartLog(LogQueryVO logQueryVO);

}
