package choose.lm.com.fileselector.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by wjq on 2016/9/6.
 * 时间转换
 */
public class DateUtil {
	public static final TimeZone tz = TimeZone.getTimeZone("GMT+8:00");
	public static final SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy-MM-dd");

	private static final long ONEDAY = 86400000;
	public static final int SHOW_TYPE_SIMPLE = 0;
	public static final int SHOW_TYPE_COMPLEX = 1;
	public static final int SHOW_TYPE_ALL = 2;
	public static final int SHOW_TYPE_CALL_LOG = 3;
	public static final int SHOW_TYPE_CALL_DETAIL = 4;

	/**
	 * 获取当前当天日期的毫秒数 2012-03-21的毫秒数
	 *
	 * @return
	 */
	public static long getCurrentDayTime() {
		Date d = new Date(System.currentTimeMillis());
		String formatDate = yearFormat.format(d);
		try {
			return (yearFormat.parse(formatDate)).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static String formatDate(int year, int month, int day) {
		Date d = new Date(year - 1900, month, day);
		return yearFormat.format(d);
	}
	public static String getCommunityTime(long time) {
		SimpleDateFormat df2 = new SimpleDateFormat("MM月dd日 HH:mm");
		return df2.format(time);

	}
	public static long getDateMills(int year, int month, int day) {
		//Date d = new Date(year, month, day);
		// 1960 4 22
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, day);
		TimeZone tz = TimeZone.getTimeZone("GMT");
		calendar.setTimeZone(tz);
		return calendar.getTimeInMillis();
	}

	/**
	 * 根据用户生日计算年龄
	 */
	public static String getAgeByBirthday(Date birthday) {
		String suishu="";
		Calendar cal = Calendar.getInstance();
		if (cal.before(birthday)) {
			throw new IllegalArgumentException(
					"The birthDay is before Now.It's unbelievable!");
		}
		int yearNow = cal.get(Calendar.YEAR);
		int monthNow = cal.get(Calendar.MONTH) + 1;
		int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
		cal.setTime(birthday);
		int yearBirth = cal.get(Calendar.YEAR);
		int monthBirth = cal.get(Calendar.MONTH) + 1;
		int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
		int age = yearNow - yearBirth;
		if(age==0){
			int month=monthNow-monthBirth;
			if(month==0){
				month=1;
			}
			suishu=month+"个月";
		}else {
			if (monthNow <= monthBirth) {
				if (monthNow == monthBirth) {
					// monthNow==monthBirth
					if (dayOfMonthNow < dayOfMonthBirth) {
						age--;
					}
				} else {
					// monthNow>monthBirth
					age--;
				}
			}
			suishu=age+"岁";
		}
		return suishu;
	}


	public static String getDateString(long time){
		String nowTime="";
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(time);
		String msgTime=formatter.format(date);
        long currentTime= System.currentTimeMillis();

		long betweenDay=(currentTime-time)/(1000*3600*24);
		if(betweenDay>1){
			nowTime=msgTime;
		}else if(betweenDay==1){
			nowTime="昨天 "+msgTime.substring(10,msgTime.length());
		}else{
			nowTime=msgTime.substring(10,msgTime.length());
		}
		return nowTime;
	}

	public static String getDateString(long time, int type) {
		Calendar c = Calendar.getInstance();
		c = Calendar.getInstance(tz);
		c.setTimeInMillis(time);
		long currentTime = System.currentTimeMillis();
		Calendar current_c = Calendar.getInstance();
		current_c = Calendar.getInstance(tz);
		current_c.setTimeInMillis(currentTime);

		int currentYear = current_c.get(Calendar.YEAR);
		int y = c.get(Calendar.YEAR);
		int m = c.get(Calendar.MONTH) + 1;
		int d = c.get(Calendar.DAY_OF_MONTH);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		int second = c.get(Calendar.SECOND);
		long t = currentTime - time;
		long t2 = currentTime - getCurrentDayTime();
		String dateStr = "";
		if (t < t2 && t > 0) {
			if (type == SHOW_TYPE_SIMPLE) {
				dateStr = (hour < 10 ? "0" + hour : hour) + ":"
						+ (minute < 10 ? "0" + minute : minute);
			} else if (type == SHOW_TYPE_COMPLEX) {
				dateStr = "今天  " + (hour < 10 ? "0" + hour : hour) + ":"
						+ (minute < 10 ? "0" + minute : minute);
			} else if (type == SHOW_TYPE_CALL_LOG) {
				dateStr = "今天  " + (hour < 10 ? "0" + hour : hour) + ":"
						+ (minute < 10 ? "0" + minute : minute);
			} else if (type == SHOW_TYPE_CALL_DETAIL) {
				dateStr = "今天  ";
			}else {
				dateStr = (hour < 10 ? "0" + hour : hour) + ":"
						+ (minute < 10 ? "0" + minute : minute) + ":"
						+ (second < 10 ? "0" + second : second);
			}
		} else if (t < (t2 + ONEDAY) && t > 0) {
			if (type == SHOW_TYPE_SIMPLE || type == SHOW_TYPE_CALL_DETAIL) {
				dateStr = "昨天  ";
			} else if (type == SHOW_TYPE_COMPLEX ) {
				dateStr = "昨天  " + (hour < 10 ? "0" + hour : hour) + ":"
						+ (minute < 10 ? "0" + minute : minute);
			} else if (type == SHOW_TYPE_CALL_LOG) {
				dateStr = "昨天  " + (hour < 10 ? "0" + hour : hour) + ":"
						+ (minute < 10 ? "0" + minute : minute);
			} else {
				dateStr = "昨天  " + (hour < 10 ? "0" + hour : hour) + ":"
						+ (minute < 10 ? "0" + minute : minute) + ":"
						+ (second < 10 ? "0" + second : second);
			}
		} else if (y == currentYear) {
			if (type == SHOW_TYPE_SIMPLE) {
				dateStr = (m < 10 ? "0" + m : m) + "/" + (d < 10 ? "0" + d : d);
			} else if (type == SHOW_TYPE_COMPLEX) {
				dateStr = (m < 10 ? "0" + m : m) + "月" + (d < 10 ? "0" + d : d)
						+ "日";
			} else if (type == SHOW_TYPE_CALL_LOG || type == SHOW_TYPE_COMPLEX) {
				dateStr = (m < 10 ? "0" + m : m) + /* 月 */"/"
						+ (d < 10 ? "0" + d : d) + /* 日 */" "
						+ (hour < 10 ? "0" + hour : hour) + ":"
						+ (minute < 10 ? "0" + minute : minute);
			} else if (type == SHOW_TYPE_CALL_DETAIL) {
				dateStr = y + "/" + (m < 10 ? "0" + m : m) + "/"
						+ (d < 10 ? "0" + d : d);
			} else {
				dateStr = (m < 10 ? "0" + m : m) + "月" + (d < 10 ? "0" + d : d)
						+ "日 " + (hour < 10 ? "0" + hour : hour) + ":"
						+ (minute < 10 ? "0" + minute : minute) + ":"
						+ (second < 10 ? "0" + second : second);
			}
		} else {
			if (type == SHOW_TYPE_SIMPLE) {
				dateStr = y + "/" + (m < 10 ? "0" + m : m) + "/"
						+ (d < 10 ? "0" + d : d);
			} else if (type == SHOW_TYPE_COMPLEX ) {
				dateStr = y + "年" + (m < 10 ? "0" + m : m) + "月"
						+ (d < 10 ? "0" + d : d) + "日";
			} else if (type == SHOW_TYPE_CALL_LOG || type == SHOW_TYPE_COMPLEX) {
				dateStr = y + /* 年 */"/" + (m < 10 ? "0" + m : m) + /* 月 */"/"
						+ (d < 10 ? "0" + d : d) + /* 日 */"  "/*
																 * + (hour < 10
																 * ? "0" + hour
																 * : hour) + ":"
																 * + (minute <
																 * 10 ? "0" +
																 * minute :
																 * minute)
																 */;
			} else if (type == SHOW_TYPE_CALL_DETAIL) {
				dateStr = y + "/" + (m < 10 ? "0" + m : m) + "/"
						+ (d < 10 ? "0" + d : d);
			} else {
				dateStr = y + "年" + (m < 10 ? "0" + m : m) + "月"
						+ (d < 10 ? "0" + d : d) + "日 "
						+ (hour < 10 ? "0" + hour : hour) + ":"
						+ (minute < 10 ? "0" + minute : minute) + ":"
						+ (second < 10 ? "0" + second : second);
			}
		}
		return dateStr;
	}

	/**
	 *
	 * @return
	 */
	public static long getCurrentTime() {
		return System.currentTimeMillis() / 1000;
	}

	public static long getActiveTimelong(String result) {
		try {
			Date parse = yearFormat.parse(result);
			return parse.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return System.currentTimeMillis();
	}

	public static String getDefaultFormat() {
		return getDateFormat(sequenceFormat);
	}

	public static String getDateFormat(DateFormat df) {
		return getDateFormat(System.currentTimeMillis(), df);
	}

	public static String getDateFormat(long time, DateFormat df) {
		Date d = new Date(time);
		return df.format(d);
	}
	public static final SimpleDateFormat sequenceFormat = new SimpleDateFormat(
			"yyyyMMddHHmmss");

	public static long getMilliSeconds(String reqtime){
		try {
			long time = DateUtil.sequenceFormat.parse(reqtime).getTime();
			return time;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static String toGMTString(long milliseconds) {
		SimpleDateFormat sdf = new SimpleDateFormat("d MMM y HH:mm:ss 'GMT'", Locale.US);
		TimeZone gmtZone = TimeZone.getTimeZone("GMT");
		sdf.setTimeZone(gmtZone);
		GregorianCalendar gc = new GregorianCalendar(gmtZone);
		gc.setTimeInMillis(milliseconds);
		return sdf.format(new Date(milliseconds));
	}
}
