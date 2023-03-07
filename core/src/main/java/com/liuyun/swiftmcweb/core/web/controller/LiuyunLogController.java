package com.liuyun.swiftmcweb.core.web.controller;

import com.liuyun.swiftmcweb.core.annotation.OriginApi;
import com.liuyun.swiftmcweb.core.web.manager.log.LiuyunLogManager;
import com.liuyun.swiftmcweb.core.web.service.vo.log.LogQueryVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author flyan
 * @version 1.0
 * @date 3/6/23
 */
@RestController
@Tag(name = "流云脚手架 - WEB - 日志(原始接口)")
@Slf4j
public class LiuyunLogController {

	private final LiuyunLogManager liuyunLogManager;

	public LiuyunLogController(LiuyunLogManager liuyunLogManager) {
		this.liuyunLogManager = liuyunLogManager;
	}

	@OriginApi(auth = false)    /* 使用原生接口完成文件上传功能 */
	@GetMapping("/liuyun/log/current_day_log")
	@Operation(summary = "获取当天日志文件信息")
	public String currentDayLog(LogQueryVO logQueryVO) {
		return liuyunLogManager.currentDayLog(logQueryVO);
	}

	@OriginApi(auth = false)    /* 使用原生接口完成文件上传功能 */
	@GetMapping("/liuyun/log/start_log")
	@Operation(summary = "获取当天启动日志文件信息")
	public String currentDayStartLog(LogQueryVO logQueryVO) {
		return liuyunLogManager.currentDayStartLog(logQueryVO);
	}

}
