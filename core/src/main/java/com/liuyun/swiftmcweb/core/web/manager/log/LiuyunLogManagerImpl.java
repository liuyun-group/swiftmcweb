package com.liuyun.swiftmcweb.core.web.manager.log;

import cn.hutool.core.util.StrUtil;
import com.liuyun.swiftmcweb.core.annotation.MessageHandleManager;
import com.liuyun.swiftmcweb.core.exception.ErrorCode;
import com.liuyun.swiftmcweb.core.framework.web.core.props.LiuyunLogProperties;
import com.liuyun.swiftmcweb.core.util.exception.ServiceExceptionUtil;
import com.liuyun.swiftmcweb.core.web.service.vo.log.LogQueryVO;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author flyan
 * @version 1.0
 * @date 3/6/23
 */
@MessageHandleManager(name = "liuyunLogManager")
public class LiuyunLogManagerImpl implements LiuyunLogManager {

	private final static String NO_PERM = """
					<html>
                    	<body>
                        	<p>抱歉，您没有权限访问日志<p>
                    	</body>
                	</html>
					""";
	private final static Set<String> LOG_TAGS = Set.of("DEBUG", "INFO", "WARN", "ERROR");
	private final static String[] LOG_LEVEL_TAG_MAP = new String[]{"DEBUG", "INFO", "WARN", "ERROR"};

	@Value(value = "${logging.file.name}")
	private String logFilePath;

	private final LiuyunLogProperties liuyunLogProperties;

	public LiuyunLogManagerImpl(LiuyunLogProperties liuyunLogProperties) {
		this.liuyunLogProperties = liuyunLogProperties;
	}

	@Override
	public String currentDayLog(LogQueryVO logQueryVO) {
		if(!liuyunLogProperties.getToken().equals(logQueryVO.getDevLogToken())) {
			return NO_PERM;
		}

		return logN(logQueryVO.getTailN(), logFilePath, logQueryVO.getLevel(), logQueryVO.getKey());
	}

	@Override
	public String currentDayStartLog(LogQueryVO logQueryVO) {
		if(!liuyunLogProperties.getToken().equals(logQueryVO.getDevLogToken())) {
			return NO_PERM;
		}

		return logN(logQueryVO.getTailN(), liuyunLogProperties.getStartLogFilePath(), logQueryVO.getLevel(), logQueryVO.getKey());
	}

	private String logN(int tailN, String logFilePath, Integer level, String key) {
		try {
			var lines = getTextWithLines(new File(logFilePath));
			var logBuilder = new StringBuilder();
			int startIndex = (tailN == -1 || tailN >= lines.size()) ? 0 : lines.size() - tailN;
			for (int i = startIndex; i < lines.size(); i++) {
				var line = lines.get(i);
				if(matched(line, level, key)) {
					appendLogLine(line, logBuilder);
				}
			}

			return """
                    <html>
                        <body>
                            <p>
                                ${log}
                            <p>
                        </body>
                    </html>
                    """
					.replace("${log}", logBuilder);
		} catch (IOException e) {
			throw ServiceExceptionUtil.exception(new ErrorCode(-1, "日志文件读取失败"));
		}
	}

	/**
	 * 检查日志是否匹配条件
	 *
	 * @param logLine
	 * @param level 日志等级[0: DEBUG, 1: INFO, 2: WARN, 3: ERROR]
	 * @param key 日志关键字，模糊查询
	 * @return
	 */
	private boolean matched(String logLine, Integer level, String key) {
		/* 检查日志等级 */
		if(level != null) {
			var logRs = isLog(logLine);
			if(!logRs.isLog || logRs.level != level) {
				return false;
			}
		}

		/* 检查日志关键字 */
		if(StrUtil.isNotBlank(key) && !LOG_TAGS.contains(key.trim())) {
			return logLine.contains(key.trim());
		}

		return true;
	}

	@AllArgsConstructor
	private static class LogCheckResult {
		private boolean isLog;
		private int level;
		static LogCheckResult yes(int level) {
			return new LogCheckResult(true, level);
		}
		static LogCheckResult no() {
			return new LogCheckResult(false, -1);
		}
	}

	private LogCheckResult isLog(String logLine) {
		for (String logTag : LOG_TAGS) {
			if (logLine.contains(" " + logTag + " ") && logLine.contains("---")) {
				return LogCheckResult.yes(getLogLevel(logTag));
			}
		}
		return LogCheckResult.no();
	}

	private Integer getLogLevel(String logTag) {
		switch (logTag) {
			case "DEBUG" -> {return 0;}
			case "INFO" -> {return 1;}
			case "WARN" -> {return 2;}
			case "ERROR" -> {return 3;}
			default -> {return -1;}
		}
	}

	private List<String> getTextWithLines(File file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		List<String> ans = new ArrayList<>();
		for(String line = reader.readLine(); line != null; line = reader.readLine()) {
			ans.add(line);
		}
		return ans;
	}

	private void appendLogLine(String logLine, StringBuilder logBuilder) {
		var logRs = isLog(logLine);
		if (logRs.isLog) {
			var logTag = LOG_LEVEL_TAG_MAP[logRs.level];
			logLine = logLine.replace("\n", "<br>")
					.replace("\r\n", "<br>")
					.replace("<", "&lt;")
					.replace(">", "&gt;")
					.replace("'", "&rsquo;");

				/*
				对于一个典型日志行: 2023-03-06 15:27:22.737 DEBUG 85103 --- [io-48081-exec-2] o.s.web.servlet.DispatcherServlet        : Completed 200 OK
				我们将 DEBUG、INFO、WARN、ERROR 分别以绿、绿、黄、红颜色标识
				将 o.s.web.servlet.DispatcherServlet 的略缩类名以蓝色标识
				 */
			var arr = logLine.toCharArray();
			var tagStartIndex = logLine.indexOf(logTag);
			int i = 0;
			for (; i < tagStartIndex; i++) {
				logBuilder.append(arr[i]);
			}
			switch (logRs.level) {
				case 0, 1 -> logBuilder.append("<font color=\"green\">").append(logTag).append("</font>").append(" ");
				case 2 -> logBuilder.append("<font color=\"yellow\">").append(logTag).append("</font>").append(" ");
				case 3 -> logBuilder.append("<font color=\"red\">").append(logTag).append("</font>").append(" ");
			}
			i += logTag.length() + 1;

			for (; i < arr.length; i++) {
				logBuilder.append(arr[i]);
				if(arr[i] == ']') {
					break;
				}
			}
			i += 1;

			logBuilder.append(" ").append("<font color=\"blue\">");
			i += 1;
			for (; i < arr.length; i++) {
				if(arr[i] == ' ') {
					break;
				}
				logBuilder.append(arr[i]);
			}
			i += 1;
			logBuilder.append("</font>").append(" ");

			for (; i < arr.length; i++) {
				logBuilder.append(arr[i]);
			}
		} else {
			logBuilder.append(logLine);
		}
		logBuilder.append("</br>");
	}

}
