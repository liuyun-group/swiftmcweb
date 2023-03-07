package com.liuyun.swiftmcweb.core.framework.web.core.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotBlank;

/**
 * 流云日志配置属性
 *
 * @author flyan
 * @version 1.0
 * @date 3/6/23
 */
@ConfigurationProperties(prefix = "liuyun.log")
@Data
public class LiuyunLogProperties {

	@NotBlank(message = "日志令牌不能为空")
	private String token;

	@NotBlank(message = "启动日志路径不能为空")
	private String startLogFilePath;

}
