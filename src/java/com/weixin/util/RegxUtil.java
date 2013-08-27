package com.weixin.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegxUtil {

	public static void regxChinese() {
		// 要匹配的字符串
		String source = "<span title='5 星级酒店' class='dx dx5'>";
		// 将上面要匹配的字符串转换成小写
		// source = source.toLowerCase();
		// 匹配的字符串的正则表达式
		String reg_charset = "<span[^>]*?title=\'([0-9]*[\\s|\\S]*[\u4E00-\u9FA5]*)\'[\\s|\\S]*class=\'[a-z]*[\\s|\\S]*[a-z]*[0-9]*\'";

		Pattern p = Pattern.compile(reg_charset);
		Matcher m = p.matcher(source);
		while (m.find()) {
			System.out.println(m.group(1));
		}
	}

}
