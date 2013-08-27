package com.barrage.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {

	private static final SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private static final SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	private static final SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy年MM月");

	private static final String DATE_PATTERN = "yyMMddHHmm";

	/**
	 * 获得一天的开始时间
	 * @param date
	 * @return
	 */
	public static Date getBeginOfDay(Date date) {
		if (date == null) {
			return null;
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		return cal.getTime();
	}

	/**
	 * 获得一天的结束时间
	 * @param date
	 * @return
	 */
	public static Date getEndOfDay(Date date) {
		if (date == null) {
			return null;
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);

		return cal.getTime();
	}

	/**
	 * 获得当月第一天的开始时间
	 * @param date
	 * @return
	 */
	public static Date getBeginOfMonth(Date date) {
		if (date == null) {
			return null;
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		return cal.getTime();
	}

	/**
	 * 获得上个月第一天的开始时间
	 * @param date
	 * @return
	 */
	public static Date getBeginOfLastMonth(Date date) {
		if (date == null) {
			return null;
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		return cal.getTime();
	}

	/**
	 * 把yyyy-MM-dd格式的字符串解析成Date
	 * @param day
	 * @return
	 */
	public static Date parseDate(String day) {
		Date date = null;
		try {
			date = dateFormat.parse(day);
		} catch (ParseException e) {
		}
		return date;
	}

	/**
	 * 把yyyy-MM-dd HH:mm:ss格式的字符串解析成Date
	 * @param day
	 * @return
	 */
	public static Date parseTimestamp(String timestamp) {
		Date date = null;
		try {
			date = timestampFormat.parse(timestamp);
		} catch (ParseException e) {
		}
		return date;
	}

	/**
	 * 把Date格式成yyyy-MM-dd HH:mm:ss格式的字符串
	 * @param day
	 * @return
	 */
	public static String format2Timestamp(Date date) {
		if (date == null) {
			return "";
		}
		return timestampFormat.format(date);
	}

	/**
	 * 把Date格式成yyyy-MM-dd格式的字符串
	 * @param day
	 * @return
	 */
	public static String format2Date(Date date) {
		if (date == null) {
			return "";
		}
		return dateFormat.format(date);
	}

	/**
	 * 比较时间，判断是否已经结束
	 * @param date  格式：yyyy-MM-dd 00:00:00 ,用于与当前时间比较
	 * @return true : 已经结束，else 未结束
	 */
	public static boolean isFinish(Date date) {
		boolean isFinish = false;
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		long currentDay = cal.getTimeInMillis();
		if ((date != null) && (date.getTime() < currentDay)) {
			isFinish = true;
		}
		return isFinish;
	}

	public static Date getBeforeDate(int i) {

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DATE, cal.get(Calendar.DATE) - i);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		return cal.getTime();
	}

	public static boolean isValidDateString(String date) {
		if (date == null || date.trim().length() != 14) {
			return false;
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			df.parse(date);
			return true;
		} catch (ParseException e) {
			return false;
		}
	}

	public static String httpFormatDay(Date date) {
		if (date == null) {
			return "";
		}

		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		return df.format(date);
	}

	public static String httpFormatDate(Date date) {
		if (date == null) {
			return "";
		}

		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		return df.format(date);
	}

	public static Date httpGetDate(String s) {

		if (s == null || s.trim().length() == 0) {
			return null;
		}

		Date date = null;
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");

		try {
			date = df.parse(s);
		} catch (ParseException e) {
		}

		return date;
	}

	public static boolean httpDateCompare(Date start, Date end) {

		long s = start.getTime();
		long e = end.getTime();

		return e - s <= 3 * 24 * 3600 * 1000;
	}

	/**
	 * 获得月份1-12
	 * @param date
	 * @return
	 */
	public static int getMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.MONTH) + 1;
	}

	/**
	 * 获得日期1-31
	 * @param date
	 * @return
	 */
	public static int getDayOfMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 格式成yyyy年MM月
	 * @param date
	 * @return
	 */
	public static String format2Month(Date date) {
		if (date == null) {
			return "";
		}
		return monthFormat.format(date);
	}

	/**
	 * 把Date格式成yyyy-MM-dd HH:mm:格式的字符串
	 * @param day
	 * @return
	 */
	public static String format2Time(Date date) {
		if (date == null) {
			return "";
		}
		return timeFormat.format(date);
	}

	/**
	 * 把yyyy-MM-dd HH:mm:ss格式的字符串解析成Date
	 * @param day
	 * @return
	 */
	public static Date parseTime(String timestamp) {
		Date date = null;
		try {
			date = timeFormat.parse(timestamp);
		} catch (ParseException e) {
		}
		return date;
	}

	public static int getDayOfYear() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.DAY_OF_YEAR);
	}

	public static int getDayOfYear(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_YEAR);
	}

	/**
	 * 获取详单分区表分区字段
	 * @return
	 */
	public static int getDayPart() {
		Calendar cal = Calendar.getInstance();
		// return ((cal.get(Calendar.DAY_OF_YEAR) - 1) % 91) + 1;
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 获取详单分区表分区字段
	 * @return
	 */
	public static int getDayPart(Date date) {
		Calendar cal = Calendar.getInstance();
		if (date != null) {
			cal.setTime(date);
		}
		// return ((cal.get(Calendar.DAY_OF_YEAR) - 1) % 91) + 1;
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 获取详单分区表分区字段
	 * @return
	 */
	public static int getDayPart(Calendar cal) {
		if (cal == null) {
			cal = Calendar.getInstance();
		}
		// return ((cal.get(Calendar.DAY_OF_YEAR) - 1) % 91) + 1;
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 获取详单分区表分区字段
	 * @param date
	 * @return
	 */
	public static int getMonthPart(Date date) {
		Calendar cal = Calendar.getInstance();
		if (date != null) {
			cal.setTime(date);
		}
		return cal.get(Calendar.MONTH) + 1;
	}

	/**
	 * Pattern为:yyMMddHHmm
	 * @return
	 */
	public static String getNowTime() {
		SimpleDateFormat format = new SimpleDateFormat(DATE_PATTERN);
		return format.format(new Date());
	}

	public static String getNowTimeByPattern(String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(new Date());
	}

	/**
	 * 
	 * @param time 格式为 YYMMDDHHMI 1303221029
	 * @return YYYYMMDDHHMISS
	 */
	public static String getFeeTime(String time) {
		return String.valueOf(Calendar.getInstance().get(Calendar.YEAR)).substring(0, 2) + time + "00";
	}
}
