package com.hafu.eban.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;

/**
 * @author: Administrator Date: 2010-12-18 Time: 16:06:49
 *          <p/>
 *          <p>
 *          DateUtil.java 日期转换工具类
 *          </p>
 */
public class DateUtil {

	// 用来全局控制 上一周，本周，下一周的周数变化
	private static int weeks = 0;
	private int MaxDate;// 一月最大天数
	private int MaxYear;// 一年最大天数

	private static final String sdf1reg = "^\\d{2,4}\\-\\d{1,2}\\-\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}$";

	private static final SimpleDateFormat sdf1 = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	private static final String sdf2reg = "^\\d{2,4}\\-\\d{1,2}\\-\\d{1,2}$";

	private static final SimpleDateFormat sdf2 = new SimpleDateFormat(
			"yyyy-MM-dd");

	private static final String sdf3reg = "^\\d{2,4}\\/\\d{1,2}\\/\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}$";

	private static final SimpleDateFormat sdf3 = new SimpleDateFormat(
			"yyyy/MM/dd HH:mm:ss");

	private static final String sdf4reg = "^\\d{2,4}\\/\\d{1,2}\\/\\d{1,2}$";

	private static final SimpleDateFormat sdf4 = new SimpleDateFormat(
			"yyyy/MM/dd");

	private static final String sdf5reg = "^\\d{2,4}\\-\\d{1,2}\\-\\d{1,2} \\d{1,2}:\\d{1,2}$";

	private static final SimpleDateFormat sdf5 = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm");

	// 新添加日期转化成时间
	private static final String sdf6reg = "^\\d{1,2}:\\d{1,2}:\\d{1,2}$";

	private static final SimpleDateFormat sdf6 = new SimpleDateFormat(
			"HH:mm:ss");

	@SuppressWarnings("unused")
	private static final String sdf7reg = "^\\d{2,4}\\-\\d{1,2}\\-\\d{1,2}$";

	private static final SimpleDateFormat sdf7 = new SimpleDateFormat(
			"yyyy-M-d");

	private static final SimpleDateFormat sdf8 = new SimpleDateFormat(
			"yyyymmddhhmm");
	private static final SimpleDateFormat sdf9 = new SimpleDateFormat(
			"yyyymmdd");
	private static final SimpleDateFormat sdf10 = new SimpleDateFormat("hhmmss");
	/**
	 * 定义一天的毫妙数
	 */
	private static final long MILLSECOND_OF_DAY = 86400000;

	/**
	 * <p/>
	 * 将日期字符串解析成日期对象，支持以下格式
	 * <li>yyyy-MM-dd HH:mm:ss
	 * <li>yyyy-MM-dd
	 * <li>yyyy/MM/dd HH:mm:ss
	 * <li>yyyy/MM/dd
	 * <li>yyyyMMdd
	 * <li>HHmmss
	 * </p>
	 * 
	 * @param str
	 * @return
	 */
	public static Date parse(String str) {
		Date date = null;
		Pattern p1 = Pattern.compile(sdf1reg);
		Matcher m1 = p1.matcher(str);
		Pattern p2 = Pattern.compile(sdf2reg);
		Matcher m2 = p2.matcher(str);
		Pattern p3 = Pattern.compile(sdf3reg);
		Matcher m3 = p3.matcher(str);
		Pattern p4 = Pattern.compile(sdf4reg);
		Matcher m4 = p4.matcher(str);
		Pattern p5 = Pattern.compile(sdf5reg);
		Matcher m5 = p5.matcher(str);
		Pattern p6 = Pattern.compile(sdf6reg);
		Matcher m6 = p6.matcher(str);
		try {
			if (m1.matches()) {
				date = sdf1.parse(str);
			} else if (m2.matches()) {
				date = sdf2.parse(str);
			} else if (m3.matches()) {
				date = sdf3.parse(str);
			} else if (m4.matches()) {
				date = sdf4.parse(str);
			} else if (m5.matches()) {
				date = sdf5.parse(str);
			} else if (m6.matches()) {
				date = sdf6.parse(str);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static String formatLongTime(Date date) {
		return sdf1.format(date);
	}

	public static String formatDateAndTime(Date date) {

		return sdf8.format(date);
	}

	/**
	 * <p/>
	 * 将日期格式化成字符串：yyyy-MM-dd
	 * </p>
	 * 
	 * @param date
	 * @return
	 */
	public static String format(Date date) {
		return sdf2.format(date);
	}

	public static String formatF1(Date date) {
		return sdf1.format(date);
	}

	public static String formatTime(Date date) {
		return sdf6.format(date);
	}

	public static String formatDate(Date date) {

		return sdf7.format(date);
	}

	// 格式化为yyyymmdd
	public static String formatDate2(Date date) {

		return sdf9.format(date);
	}

	// 格式化为hhmmss
	public static String formatTime2(Date date) {

		return sdf10.format(date);
	}

	/**
	 * <p/>
	 * 将日期格式化成相应格式的字符串，如：
	 * <li>yyyy-MM-dd HH:mm:ss
	 * <li>yyyy-MM-dd
	 * <li>yyyy/MM/dd HH:mm:ss
	 * <li>yyyy/MM/dd
	 * </p>
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String format(Date date, String pattern) {
		if (date == null)
			return "";
		final SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}

	/**
	 * 取得字符串日期中的年
	 * 
	 * @param date
	 * @return year
	 */
	public static int getYear(String date) {
		try {
			return getYear(sdf2.parse(date));
		} catch (ParseException e) {
			System.out.println("日期转换格式出错");
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * 取得日期对象中的年
	 * 
	 * @param date
	 * @return year
	 */
	public static int getYear(Date date) {
		if (date == null)
			return 0;
		Calendar calendar = getCalendar();
		calendar.setTime(date);
		return calendar.get(Calendar.YEAR);
	}

	/**
	 * 取得日期对象中的月
	 * 
	 * @param date
	 * @return month
	 */
	public static int getMonth(Date date) {
		if (date == null)
			return 0;
		Calendar calendar = getCalendar();
		calendar.setTime(date);
		return calendar.get(Calendar.MONTH) + 1;
	}

	/**
	 * 取得字符串日期中的月
	 * 
	 * @param date
	 * @return month
	 */
	public static int getMonth(String date) {
		try {
			return getMonth(sdf2.parse(date));
		} catch (ParseException e) {
			System.out.println("日期转换格式出错");
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * 取得日期对象中的日
	 * 
	 * @param date
	 * @return date
	 */
	public static int getDate(Date date) {
		if (date == null)
			return 0;
		Calendar calendar = getCalendar();
		calendar.setTime(date);
		return calendar.get(Calendar.DATE);
	}

	/**
	 * 取得字符串日期中的日
	 * 
	 * @param date
	 * @return date
	 */
	public static int getDate(String date) {
		try {
			return getDate(sdf2.parse(date));
		} catch (ParseException e) {
			System.out.println("日期转换格式出错");
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * 取得日期格式中的小时。24小时制 add by zsc 2007-11-9
	 * 
	 * @param date
	 * @return
	 */
	public static int getHour(Date date) {
		if (date == null)
			return 0;
		Calendar calendar = getCalendar();
		calendar.setTime(date);
		return calendar.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * 取得字符串日期中的小时。24小时制 add by zsc 2007-11-9
	 * 
	 * @param date
	 * @return
	 */
	public static int getHour(String date) {
		try {
			return getHour(sdf1.parse(date));
		} catch (ParseException e) {
			System.out.println("日期转换格式出错");
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * 取得字符串日期中的小时。24小时制 add by zsc 2007-11-9
	 * 
	 * @param date
	 * @return
	 */
	public static int getWeek(Date date) {
		if (date == null)
			return 0;
		Calendar calendar = getCalendar();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_WEEK) - 1;

	}

	/**
	 * 取得Calendar实例
	 * 
	 * @return
	 */
	public static Calendar getCalendar() {
		return Calendar.getInstance();
	}

	public static void main(String[] args) {
		System.out.println(format(getDate(new Date(), 7)));
	}
	
	/**
	 * 根据日期偏移天数取得日期。offset > 0 ,往后延迟offset天， offset < 0 向前推进 offset天
	 * 
	 * @param date
	 *            :基日期
	 * @param offset
	 *            :日期天数偏移量
	 * @return
	 */
	public static Date getDate(Date date, int offset) {
		if (date == null)
			return date;
		Calendar calendar = getCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_YEAR, offset);
		return calendar.getTime();
	}

	/** ************************* old mothd****************** */
	/**
	 * 格式化日期
	 * 
	 * @param strDate
	 *            符合格式的字符串
	 * @return 格式后的日期（yyyy-MM-dd HH:mm:ss）
	 */
	public static Date parser(String strDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return sdf.parse(strDate);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 格式化日期
	 * 
	 * @param strDate
	 *            日期时间
	 * @return 格式后的日期（yyyy-MM-dd）
	 */
	public static Date parser(Date testDate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(testDate);
		calendar.clear(Calendar.MILLISECOND);
		calendar.clear(Calendar.SECOND);
		calendar.clear(Calendar.MINUTE);
		calendar.clear(Calendar.HOUR_OF_DAY);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return sdf.parse(sdf.format(calendar.getTime()));
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 格式化日期
	 * 
	 * @param strDate
	 *            日期时间
	 * @return 格式后的日期（yyyy-MM-dd）
	 */
	public static Date parserTime(Date testDate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(testDate);
		calendar.clear(Calendar.MILLISECOND);
		calendar.clear(Calendar.SECOND);
		calendar.clear(Calendar.MINUTE);
		calendar.clear(Calendar.HOUR_OF_DAY);
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		try {
			return sdf.parse(sdf.format(calendar.getTime()));
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 格式化日期
	 * 
	 * @param strDate
	 *            符合格式的字符串
	 * @return 格式后的日期（yyyy-MM-dd）
	 */
	public static Date parserTo(String strDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return sdf.parse(strDate);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 得到当前月份的周期开始日期
	 * 
	 * @param currentDate
	 *            当前日期
	 * @return 当前月份的周期开始日期
	 */
	public static Date getCurBeginCycleDate(Date currentDate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(currentDate);

		String year = "" + calendar.get(Calendar.YEAR);
		String month = (calendar.get(Calendar.MONTH) + 1) + "";
		if (month.length() < 2) {
			month = "0" + month;
		}
		String dateStr = year + "-" + month + "-01 00:00:00";
		return DateUtil.parser(dateStr);
	}
	
	/***
	 * 获取当前月份的天数
	 * @return
	 */
	public static int getDayOfMonth(){
		Calendar aCalendar = Calendar.getInstance(Locale.CHINA);
		int day=aCalendar.getActualMaximum(Calendar.DATE);
		return day;
	}

	/**
	 * 取得当前周期的周期结束日期
	 * 
	 * @param currentDate
	 *            当前日期
	 * @return 当前周期的周期结束日期
	 */
	public static Date getCurEndCycleDate(Date currentDate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(currentDate);

		String year = "" + calendar.get(Calendar.YEAR);
		String month = (calendar.get(Calendar.MONTH) + 2) + "";
		if (month.length() < 2) {
			month = "0" + month;
		}
		String dateStr = year + "-" + month + "-01 23:59:59";
		calendar.setTime(DateUtil.parser(dateStr));
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		return calendar.getTime();
	}

	/**
	 * 获取开始和结束日期之间的间隔日期
	 * 
	 * @param startDate
	 *            开始日期
	 * @param endDate
	 *            结束日期
	 * @param roundingMode
	 *            是枚举值 舍入方式 见 BigDecimal的定义
	 * @return 相隔的日期数
	 */
	public static long getDaysBetweenDate(Date startDate, Date endDate,
			RoundingMode roundingMode) {

		BigDecimal bStart = new BigDecimal(startDate.getTime());
		BigDecimal bEnd = new BigDecimal(endDate.getTime());
		BigDecimal bUnit = new BigDecimal(MILLSECOND_OF_DAY);
		return (bEnd.subtract(bStart)).divide(bUnit, roundingMode).longValue();
	}

	/**
	 * 获取开始和结束日期之间的间隔日期
	 * 
	 * @param startDate
	 *            开始日期
	 * @param endDate
	 *            结束日期
	 * @return 相隔的日期数
	 */
	public static long getDaysBetweenDateWithoutTime(Date startDate,
			Date endDate) {
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(startDate);
		cal2.setTime(endDate);

		cal1.clear(Calendar.MILLISECOND);
		cal1.clear(Calendar.SECOND);
		cal1.clear(Calendar.MINUTE);
		cal1.clear(Calendar.HOUR_OF_DAY);

		cal2.clear(Calendar.MILLISECOND);
		cal2.clear(Calendar.SECOND);
		cal2.clear(Calendar.MINUTE);
		cal2.clear(Calendar.HOUR_OF_DAY);

		return (cal2.getTime().getTime() - cal1.getTime().getTime())
				/ (24 * 60 * 60 * 1000);
	}

	/**
	 * 还回给定日期的后一天
	 * 
	 * @param date
	 * @return 还回给定日期的后一天
	 */
	public static Date getTomorrowDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		return cal.getTime();
	}

	/**
	 * 还回给定日期的前一天
	 * 
	 * @param date
	 * @return 还回给定日期的前一天
	 */
	public static Date getYesterdayDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	/**
	 * 返回给定距离days天的日期
	 * 
	 * @param date
	 * @param days
	 * @return 给定距离days天的日期
	 */
	public static Date getFixedDate(Date date, Integer days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, days);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		return cal.getTime();
	}

	/**
	 * 测试给定的日期是否在当前日期(yyyy-MM-dd 00:00:00 000)的前面
	 * 
	 * @param date
	 * @return
	 */
	public static boolean beforeCurrentDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return date.before(calendar.getTime());
	}

	/**
	 * 测试给定的日期是否在当前日期(yyyy-MM-dd 23:59:59 999)的后面
	 * 
	 * @param date
	 * @return
	 */
	public static boolean afterCurrentDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return date.after(calendar.getTime());
	}

	/**
	 * 返回两个时间之间相隔天数
	 * 
	 * @param dateA
	 * 
	 * @param dateB
	 * 
	 * @return int 相隔天数
	 */
	public static int getBetweenDayNumber(Date dateA, Date dateB) {
		long DAY = 24L * 60L * 60L * 1000L;
		return (int) ((dateA.getTime() - dateB.getTime()) / DAY);
	}
	
	/**
	 * 获取两个日期之间相差的月份数
	 * @param date1
	 * @param date2
	 * @param pattern
	 * @return
	 */
	public static int countMonths(String date1,String date2){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Calendar c1=Calendar.getInstance();
        Calendar c2=Calendar.getInstance();
        try {
			c1.setTime(sdf.parse(date1));
			c2.setTime(sdf.parse(date2));
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
        int year =c2.get(Calendar.YEAR)-c1.get(Calendar.YEAR);
        
        //开始日期若小月结束日期
        if(year<0){
            year=-year;
            return year*12+c1.get(Calendar.MONTH)-c2.get(Calendar.MONTH);
        }
       
        return year*12+c2.get(Calendar.MONTH)-c1.get(Calendar.MONTH);
    }

	/**
	 * 返回传入时间加上dayNumber一定数量的天（天为单位）后的时间
	 * 
	 * @param date
	 *            需要加上一定数量的时间----天（天为单位）
	 * @param dayNumber
	 *            数量----天（天为单位）
	 * @return
	 */
	public static Date getAddDayNumber(Date date, int dayNumber) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, dayNumber);
		return calendar.getTime();
	}

	/**
	 * 获得系统当前时间
	 */
	@SuppressWarnings("deprecation")
	public static Date getSystemDate() {
		Date date = null;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(new Date()
					.toLocaleString());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/********************************************* 网上整理到的一些方法 ******************************************/
	/**
	 * 得到二个日期间的间隔天数
	 */
	public static String getTwoDay(String sj1, String sj2) {
		SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
		long day = 0;
		try {
			java.util.Date date = myFormatter.parse(sj1);
			java.util.Date mydate = myFormatter.parse(sj2);
			day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
		} catch (Exception e) {
			return "";
		}
		return day + "";
	}

	/**
	 * 根据一个日期，返回是星期几的字符串
	 * 
	 * @param sdate
	 * @return
	 */
	public static String getWeek(String sdate) {
		// 再转换为时间
		Date date = DateUtil.strToDate(sdate);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		// int hour=c.get(Calendar.DAY_OF_WEEK);
		// hour中存的就是星期几了，其范围 1~7
		// 1=星期日 7=星期六，其他类推
		return new SimpleDateFormat("EEEE").format(c.getTime());
	}

	/**
	 * 将短时间格式字符串转换为时间 yyyy-MM-dd
	 * 
	 * @param strDate
	 * @return
	 */
	public static Date strToDate(String strDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatter.parse(strDate, pos);
		return strtodate;
	}

	/**
	 * 两个时间之间的天数
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static long getDays(String date1, String date2) {
		if (date1 == null || date1.equals(""))
			return 0;
		if (date2 == null || date2.equals(""))
			return 0;
		// 转换为标准时间
		SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date = null;
		java.util.Date mydate = null;
		try {
			date = myFormatter.parse(date1);
			mydate = myFormatter.parse(date2);
		} catch (Exception e) {
		}
		long day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
		return day;
	}

	// 计算当月最后一天,返回字符串
	public String getDefaultDay(Date date) {

		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Calendar lastDate = Calendar.getInstance();
		lastDate.set(Calendar.DATE, 1);// 设为当前月的1号

		lastDate.add(DateUtil.getMonth(date), 1);// 加一个月，变为下月的1号
		lastDate.add(Calendar.DATE, -1);// 减去一天，变为当月最后一天

		str = sdf.format(lastDate.getTime());
		return str;
	}

	// 上月第一天
	public String getPreviousMonthFirst() {
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Calendar lastDate = Calendar.getInstance();
		lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
		lastDate.add(Calendar.MONTH, -1);// 减一个月，变为下月的1号
		// lastDate.add(Calendar.DATE,-1);//减去一天，变为当月最后一天

		str = sdf.format(lastDate.getTime());
		return str;
	}

	// 获取当月第一天
	public String getFirstDayOfMonth() {
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Calendar lastDate = Calendar.getInstance();
		lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
		str = sdf.format(lastDate.getTime());
		return str;
	}

	// 获得本周星期日的日期
	public static String getCurrentWeekday() {
		weeks = 0;
		int mondayPlus = getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, mondayPlus + 6);
		Date monday = currentDate.getTime();

		DateFormat df = DateFormat.getDateInstance();
		String preMonday = df.format(monday);
		return preMonday;
	}

	// 获取当天时间
	public String getNowTime(String dateformat) {
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat(dateformat);// 可以方便地修改日期格式
		String hehe = dateFormat.format(now);
		return hehe;
	}

	// 获得当前日期与本周日相差的天数
	private static int getMondayPlus() {
		Calendar cd = Calendar.getInstance();
		// 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
		int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1; // 因为按中国礼拜一作为第一天所以这里减1
		if (dayOfWeek == 1) {
			return 0;
		} else {
			return 1 - dayOfWeek;
		}
	}

	// 获得本周一的日期
	public static String getMondayOFWeek() {
		weeks = 0;
		int mondayPlus = getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, mondayPlus);
		Date monday = currentDate.getTime();

		DateFormat df = DateFormat.getDateInstance();
		String preMonday = df.format(monday);
		return preMonday;
	}

	// 获得相应周的周六的日期
	public String getSaturday() {
		int mondayPlus = getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 * weeks + 6);
		Date monday = currentDate.getTime();
		DateFormat df = DateFormat.getDateInstance();
		String preMonday = df.format(monday);
		return preMonday;
	}

	// 获得上周星期日的日期
	public String getPreviousWeekSunday() {
		weeks = 0;
		weeks--;
		int mondayPlus = getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, mondayPlus + weeks);
		Date monday = currentDate.getTime();
		DateFormat df = DateFormat.getDateInstance();
		String preMonday = df.format(monday);
		return preMonday;
	}

	// 获得上周星期一的日期
	public String getPreviousWeekday() {
		weeks--;
		int mondayPlus = getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 * weeks);
		Date monday = currentDate.getTime();
		DateFormat df = DateFormat.getDateInstance();
		String preMonday = df.format(monday);
		return preMonday;
	}

	// 获得下周星期一的日期
	public String getNextMonday() {
		weeks++;
		int mondayPlus = getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, mondayPlus + 7);
		Date monday = currentDate.getTime();
		DateFormat df = DateFormat.getDateInstance();
		String preMonday = df.format(monday);
		return preMonday;
	}

	// 获得下周星期日的日期
	public String getNextSunday() {

		int mondayPlus = getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 + 6);
		Date monday = currentDate.getTime();
		DateFormat df = DateFormat.getDateInstance();
		String preMonday = df.format(monday);
		return preMonday;
	}

	@SuppressWarnings("unused")
	private int getMonthPlus() {
		Calendar cd = Calendar.getInstance();
		int monthOfNumber = cd.get(Calendar.DAY_OF_MONTH);
		cd.set(Calendar.DATE, 1);// 把日期设置为当月第一天
		cd.roll(Calendar.DATE, -1);// 日期回滚一天，也就是最后一天
		MaxDate = cd.get(Calendar.DATE);
		if (monthOfNumber == 1) {
			return -MaxDate;
		} else {
			return 1 - monthOfNumber;
		}
	}

	// 获得上月最后一天的日期
	public static Date getPreviousMonthEnd(Date date) {
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Calendar lastDate = Calendar.getInstance();
		lastDate.setTime(date);
		lastDate.add(Calendar.MONTH, -1);// 减一个月
		lastDate.set(Calendar.DATE, 1);// 把日期设置为当月第一天
		lastDate.roll(Calendar.DATE, -1);// 日期回滚一天，也就是本月最后一天
		// str = sdf.format(lastDate.getTime());
		return lastDate.getTime();
	}

	// 获得下个月第一天的日期
	public static Date getNextMonthFirst(Date date) {
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Calendar lastDate = Calendar.getInstance();
		lastDate.setTime(date);
		lastDate.add(Calendar.MONTH, 1);// 减一个月
		lastDate.set(Calendar.DATE, 1);// 把日期设置为当月第一天
		// str = sdf.format(lastDate.getTime());
		return lastDate.getTime();
	}

	// 获得下个月最后一天的日期
	public String getNextMonthEnd() {
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Calendar lastDate = Calendar.getInstance();
		lastDate.add(Calendar.MONTH, 1);// 加一个月
		lastDate.set(Calendar.DATE, 1);// 把日期设置为当月第一天
		lastDate.roll(Calendar.DATE, -1);// 日期回滚一天，也就是本月最后一天
		str = sdf.format(lastDate.getTime());
		return str;
	}

	// 获得明年最后一天的日期
	public String getNextYearEnd() {
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Calendar lastDate = Calendar.getInstance();
		lastDate.add(Calendar.YEAR, 1);// 加一个年
		lastDate.set(Calendar.DAY_OF_YEAR, 1);
		lastDate.roll(Calendar.DAY_OF_YEAR, -1);
		str = sdf.format(lastDate.getTime());
		return str;
	}

	// 获得明年第一天的日期
	public String getNextYearFirst() {
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Calendar lastDate = Calendar.getInstance();
		lastDate.add(Calendar.YEAR, 1);// 加一个年
		lastDate.set(Calendar.DAY_OF_YEAR, 1);
		str = sdf.format(lastDate.getTime());
		return str;

	}

	// 获得本年有多少天
	@SuppressWarnings("unused")
	private int getMaxYear() {
		Calendar cd = Calendar.getInstance();
		cd.set(Calendar.DAY_OF_YEAR, 1);// 把日期设为当年第一天
		cd.roll(Calendar.DAY_OF_YEAR, -1);// 把日期回滚一天。
		int MaxYear = cd.get(Calendar.DAY_OF_YEAR);
		return MaxYear;
	}

	private int getYearPlus() {
		Calendar cd = Calendar.getInstance();
		int yearOfNumber = cd.get(Calendar.DAY_OF_YEAR);// 获得当天是一年中的第几天
		cd.set(Calendar.DAY_OF_YEAR, 1);// 把日期设为当年第一天
		cd.roll(Calendar.DAY_OF_YEAR, -1);// 把日期回滚一天。
		int MaxYear = cd.get(Calendar.DAY_OF_YEAR);
		if (yearOfNumber == 1) {
			return -MaxYear;
		} else {
			return 1 - yearOfNumber;
		}
	}

	// 获得本年第一天的日期
	public String getCurrentYearFirst() {
		int yearPlus = this.getYearPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, yearPlus);
		Date yearDay = currentDate.getTime();
		DateFormat df = DateFormat.getDateInstance();
		String preYearDay = df.format(yearDay);
		return preYearDay;
	}

	// 获得本年最后一天的日期 *
	public String getCurrentYearEnd() {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");// 可以方便地修改日期格式
		String years = dateFormat.format(date);
		return years + "-12-31";
	}

	// 获得上年第一天的日期 *
	public String getPreviousYearFirst() {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");// 可以方便地修改日期格式
		String years = dateFormat.format(date);
		int years_value = Integer.parseInt(years);
		years_value--;
		return years_value + "-1-1";
	}

	// 获得上年最后一天的日期
	public String getPreviousYearEnd() {
		weeks--;
		int yearPlus = this.getYearPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, yearPlus + MaxYear * weeks
				+ (MaxYear - 1));
		Date yearDay = currentDate.getTime();
		DateFormat df = DateFormat.getDateInstance();
		String preYearDay = df.format(yearDay);
		getThisSeasonTime(11);
		return preYearDay;
	}

	// 获得本季度
	public String getThisSeasonTime(int month) {
		int array[][] = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 }, { 10, 11, 12 } };
		int season = 1;
		if (month >= 1 && month <= 3) {
			season = 1;
		}
		if (month >= 4 && month <= 6) {
			season = 2;
		}
		if (month >= 7 && month <= 9) {
			season = 3;
		}
		if (month >= 10 && month <= 12) {
			season = 4;
		}
		int start_month = array[season - 1][0];
		int end_month = array[season - 1][2];

		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");// 可以方便地修改日期格式
		String years = dateFormat.format(date);
		int years_value = Integer.parseInt(years);

		int start_days = 1;// years+"-"+String.valueOf(start_month)+"-1";//getLastDayOfMonth(years_value,start_month);
		int end_days = getLastDayOfMonth(years_value, end_month);
		String seasonDate = years_value + "-" + start_month + "-" + start_days
				+ ";" + years_value + "-" + end_month + "-" + end_days;
		return seasonDate;

	}

	/**
	 * 获取某年某月的最后一天
	 * 
	 * @param year
	 *            年
	 * @param month
	 *            月
	 * @return 最后一天
	 */
	private int getLastDayOfMonth(int year, int month) {
		if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8
				|| month == 10 || month == 12) {
			return 31;
		}
		if (month == 4 || month == 6 || month == 9 || month == 11) {
			return 30;
		}
		if (month == 2) {
			if (isLeapYear(year)) {
				return 29;
			} else {
				return 28;
			}
		}
		return 0;
	}

	/**
	 * lxz 获得某年某月的总的天数
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static int getLastDayOfMouthYear(int year, int month) {
		if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8
				|| month == 10 || month == 12) {
			return 31;
		}
		if (month == 4 || month == 6 || month == 9 || month == 11) {
			return 30;
		}
		if (month == 2) {
			if (isLeapYear(year)) {
				return 29;
			} else {
				return 28;
			}
		}
		return 0;
	}

	/**
	 * 是否闰年
	 * 
	 * @param year
	 *            年
	 * @return
	 */
	public static boolean isLeapYear(int year) {
		return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
	}

	/**
	 * lxz 返回两个时间之间相隔的月份个数
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static int countMouthBetweenTwoDate(String startDate, String endDate) {
		int mouthYear = 12;
		int startMouth = DateUtil.getMonth(startDate);
		int endMouth = DateUtil.getMonth(endDate);
		int startYear = DateUtil.getYear(startDate);
		int endYear = DateUtil.getYear(endDate);
		int yearCount = endYear - startYear;
		int countMouth = 0;
		if (yearCount > 0) {// 年不同
			countMouth = yearCount * mouthYear - startMouth + endMouth - 1;
		} else {// ==年相同
			countMouth = endMouth - startMouth - 1;
		}
		if (countMouth < 0) {
			countMouth = 0;
		}
		return countMouth;
	}

	/**
	 * 比较某一直是否在某一个区间范围内 小时和分钟的大小
	 * 
	 * @param start区间的开始
	 * @param end区间的结束
	 * @param param值
	 * @return 0表示在区间范围内，1表示在区间范围之外的
	 */
	public static int countTwoMinite(String start, String end, String param) {

		String starts = DateUtil.format(DateUtil.parse(start), "HHmm");
		String ends = DateUtil.format(DateUtil.parse(end), "HHmm");
		String params = DateUtil.format(DateUtil.parse(param), "HHmm");
		Integer intStart = Integer.valueOf(starts);
		Integer intEnd = Integer.valueOf(ends);
		Integer intParams = Integer.valueOf(params);
		// 在范围之内的
		if (intStart <= intParams && intParams <= intEnd) {
			return 0;
		} else {
			return 1;
		}

	}

	public static Date TimeStamp2Date(String timestampString) {
		Long timestamp = Long.parseLong(timestampString) * 1000;
		//String date = sdf1.format(new java.util.Date(timestamp));
		return new java.util.Date(timestamp);
	}
	
	
	public static Date TimeStamp2DateFomart(String timestampString) {
		Long timestamp = Long.parseLong(timestampString) * 1000;
		String date = sdf1.format(new java.util.Date(timestamp));
		return parse(date);
	}
	
	public static String parserTominute(String strDate){
		Long timestamp = Long.parseLong(strDate) * 1000;
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		try {
			return sdf.format(new java.util.Date(timestamp));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	
  
    /** 
     * 获取某一时间段特定星期几的日期 
     * @param dateFrom 开始时间 
     * @param dateEnd 结束时间 
     * @param weekDays 星期 
     * @return 返回时间数组 
     */  
    public static String getDates(String dateFrom, String dateEnd, String weekDays) {  
        long time = 1l;  
        long perDayMilSec = 24 * 60 * 60 * 1000;  
        List<String> dateList = new ArrayList<String>();  
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
        //需要查询的星期系数  
        String strWeekNumber = weekForNum(weekDays);  
        try {  
            dateFrom = sdf.format(sdf.parse(dateFrom).getTime() - perDayMilSec);  
            while (true) {  
                    time = sdf.parse(dateFrom).getTime();  
                    time = time + perDayMilSec;  
                    Date date = new Date(time);  
                    dateFrom = sdf.format(date);  
                    if (dateFrom.compareTo(dateEnd) <= 0) {  
                        //查询的某一时间的星期系数  
                        Integer weekDay = dayForWeek(date);                      
                        //判断当期日期的星期系数是否是需要查询的  
                        if (strWeekNumber.indexOf(weekDay.toString())!=-1) {  
                            dateList.add(dateFrom);  
                        }  
                    } else {  
                        break;  
                    }  
            }  
        } catch (ParseException e1) {  
            e1.printStackTrace();  
        }  
        String[] dateArray = new String[dateList.size()];  
        dateList.toArray(dateArray);  
        return dateArray[0];  
    }  
  
    //等到当期时间的周系数。星期日：1，星期一：2，星期二：3，星期三：4，星期四：5，星期五：6，星期六：7  
    public static Integer dayForWeek(Date date) {  
        Calendar calendar = Calendar.getInstance();  
        calendar.setTime(date);  
        return calendar.get(Calendar.DAY_OF_WEEK);  
    }  
      
    /** 
     * 得到对应星期的系数  星期日：1，星期一：2，星期二：3，星期三：4，星期四：5，星期五：6，星期六：7 
     * @param weekDays 星期格式  星期一|星期二 
     */  
    public static String weekForNum(String weekDays){  
        //返回结果为组合的星期系数  
        String weekNumber = "";  
        //解析传入的星期  
        if(weekDays.indexOf("|")!=-1){//多个星期数  
            String []strWeeks = weekDays.split("\\|");  
            for(int i=0;i<strWeeks.length;i++){  
                weekNumber = weekNumber + "" + strWeeks[i];  
            }  
        }else{//一个星期数  
            weekNumber = weekDays;  
        }  
          
        return weekNumber;  
          
    }  
    
    public static Integer getWeekNum(String strWeek){  
        Integer number = 1;//默认为星期日  
        if("星期日".equals(strWeek)){  
            number = 1;  
        }else if("星期一".equals(strWeek)){  
            number = 2;  
        }else if("星期二".equals(strWeek)){  
            number = 3;  
        }else if("星期三".equals(strWeek)){  
            number = 4;  
        }else if("星期四".equals(strWeek)){  
            number = 5;  
        }else if("星期五".equals(strWeek)){  
            number = 6;  
        }else if("星期六".equals(strWeek)){  
            number = 7;  
        }else{  
            number = 1;  
        }  
        return number;  
    }  
    
    
    /**
	 * 将日期转换成字符格式
	 * 
	 * @param date
	 *            java.util.Date类型
	 * @param format
	 *            如果为null或""，默认为DATE格式
	 * @return 无法成功转换则返回null
	 */
	public static String date2String(java.util.Date date, String format) {
		String result = null;
		if (date == null) {
			return result;
		}
		if (StringUtils.isEmpty(format)) {
			format = "yyyy-MM-dd";
		}
		try {
			result = DateFormatUtils.format(date, format);
		} catch (Exception ex) {
		}

		return result;
	}
	
	
	
	private static final int FIRST_DAY = Calendar.MONDAY;

	public static String[] printWeekdays() {
		String strs[] = new String[7];
		Calendar calendar = Calendar.getInstance();
		setToFirstDay(calendar);
		for (int i = 0; i < 7; i++) {
			strs[i] = printDay(calendar);
			calendar.add(Calendar.DATE, 1);
		}
		return strs;
	}

	private static void setToFirstDay(Calendar calendar) {
		while (calendar.get(Calendar.DAY_OF_WEEK) != FIRST_DAY) {
			calendar.add(Calendar.DATE, -1);
		}
	}

	private static String printDay(Calendar calendar) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.format(calendar.getTime());
	}
}
