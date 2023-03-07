package com.liuyun.swiftmcweb.core.web.service.vo.log;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author flyan
 * @version 1.0
 * @date 3/6/23
 */
@Data
public class LogQueryVO {

	@Schema(description = "开发日志令牌", example = "flyan 是帅哥")
	@NotBlank(message = "必须指定开发日志令牌")
	String devLogToken;

	@Schema(description = "要最新的 n 行，-1 则查阅全部", example = "50")
	@NotNull(message = "必须指定查询行数范围")
	Integer tailN;

	@Schema(description = "日志等级[0: DEBUG, 1: INFO, 2: WARN, 3: ERROR]", example = "0")
	Integer level;

	@Schema(description = "日志关键字，模糊查询", example = "API调用失败")
	String key;

}
