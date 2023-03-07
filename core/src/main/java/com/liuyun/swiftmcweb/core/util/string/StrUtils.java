package com.liuyun.swiftmcweb.core.util.string;

/**
 * @author flyan
 * @version 1.0
 * @date 3/6/23
 */
public class StrUtils {

	public static String toHtmlText(String s) {
		if(s == null) {
			return null;
		}
		return s.replace(" ", "&nbsp;")
				.replace("\n", "<br>")
				.replace("\r\n", "<br>")
				.replace("<", "&lt;")
				.replace(">", "&gt;")
				.replace("'", "&rsquo;");
	}

}
