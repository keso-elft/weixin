package com.barrage.util;

public class PatternUtil {

	/**
	 * 汉字转UNICODE pattern
	 */
	public static String toUnicodePattern(String s) {
		String as[] = new String[s.length()];
		String s1 = "";
		for (int i = 0; i < s.length(); i++) {
			as[i] = Integer.toHexString(s.charAt(i) & 0xffff);
			s1 = s1 + "[\\u" + as[i] + "]";
		}
		return s1;
	}
	
	/**
	 * 特殊字符转UNICODE
	 */
//	public static String replaceFirst(String source) {
//		char sqls[] = source.toCharArray();
//		  StringBuffer sb = new StringBuffer();
//		  int index = 0;
//		  while(index<sqls.length){
//		   if(sqls[index]=='('){
//		    sb.append("\\u0028");
//		   }
//		   else if(sqls[index]==')'){
//		    sb.append("\\u0029");
//		   }
//		   else if(sqls[index]=='*'){
//		    sb.append("\\u002A");
//		   }
//		   else if(sqls[index]=='+'){
//		    sb.append("\\u002B");
//		   }
//		   else sb.append(sqls[index]);
//		   index ++;
//		  }
//	}
}
