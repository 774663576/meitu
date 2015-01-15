package com.meitu.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.text.TextUtils;

/**
 * 
 */
@SuppressLint("SimpleDateFormat")
public class DateUtils {

	private static final Map<String, DateFormat> DFS = new HashMap<String, DateFormat>();
	private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private DateUtils() {
	}

	public static Date stringToDate(String str) {
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = fmt.parse(str);
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new Date();
	}

	/**
	 * ��ȡ��ǰʱ���ַ�������ʽΪ��"yyyy-MM-dd HH:mm:ss"
	 * 
	 * @return
	 */
	public static String getCurrDateStr() {
		return getCurrDateStr(null);
	}

	/**
	 * ����ָ����ʽ�ĵ�ǰʱ���ַ��������formatΪ�ջ���Ϊnull��ʹ��Ĭ�ϸ�ʽ��"yyyy-MM-dd HH:mm:ss"
	 * 
	 * @Throws �������ĸ�ʽ��֧�����׳� IllegalArgumentException �쳣
	 * @param format
	 * @return
	 */
	public static String getCurrDateStr(String format) {
		if (TextUtils.isEmpty(format)) {
			format = "yyyy-MM-dd HH:mm:ss";
		}
		return format(new Date(), format);
	}

	public static String getYear(String str, String format) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat df1 = new SimpleDateFormat(format);
		String strdate = "";
		try {
			Date date = df.parse(str);
			strdate = df1.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return strdate;
	}

	/**
	 * ��ʱ���װ��Ϊһ����ʽ������
	 * 
	 * @param timestamp
	 */
	public static String timestampConvertToDate(String timestamp, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String date = sdf.format(new Date(Long.valueOf(timestamp) * 1000));
		return date;
	}

	public static String getMonth(String str, String format) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat df1 = new SimpleDateFormat(format);
		String strdate = "";
		try {
			Date date = df.parse(str);
			strdate = df1.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return strdate;
	}

	public static String getGrowthShowTime() {
		return format(new Date(), "yyyy-MM-dd HH:mm");
	}

	public static DateFormat getFormat(String pattern) {
		DateFormat format = DFS.get(pattern);
		if (format == null) {
			format = new SimpleDateFormat(pattern);
			DFS.put(pattern, format);
		}
		return format;
	}

	public static String format(Date date, String pattern) {
		if (date == null) {
			return null;
		}
		return getFormat(pattern).format(date);
	}

	private static Calendar convert(Date date) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		return calendar;
	}

	/**
	 * ��һ����ʽ��ȡ�ַ�������
	 * 
	 * @param str
	 * @return
	 */
	public static String interceptDateStr(String str, String format) {
		DateFormat df1 = new SimpleDateFormat(format);
		String strdate = "";
		try {
			Date date = df.parse(str);
			strdate = df1.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return strdate;

	}

	/**
	 * ����ָ��������Ӧλ�ƺ������
	 * 
	 * @param date
	 *            �ο�����
	 * @param field
	 *            λ�Ƶ�λ���� {@link Calendar}
	 * @param offset
	 *            λ��������������ʾ֮���ʱ�䣬������ʾ֮ǰ��ʱ��
	 * @return λ�ƺ������
	 */
	public static Date offsetDate(Date date, int field, int offset) {
		Calendar calendar = convert(date);
		calendar.add(field, offset);
		return calendar.getTime();
	}

	/**
	 * ת��Ϊphpʱ��� ��ȥ����λ
	 * 
	 * @param str
	 */
	public static String phpTime(Long str) {
		String strTime = String.valueOf(str);
		return strTime.substring(0, strTime.length() - 3);

	}

	public static String getCircleCreateTime() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(new Date());
	}

	/**
	 * ��string����ת��Ϊdate
	 * 
	 * @param str
	 */
	public static long convertToDate(String str) {
		Date date = null;
		try {
			date = df.parse(str);
			return date.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
			return 0L;
		}
	}

	/**
	 * ��string����ת��Ϊdate
	 * 
	 * @param str
	 */
	public static long convertGrowthToDate(String str) {
		Date date = null;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		try {
			date = df.parse(str);
			return date.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
			return 0L;
		}
	}

	/**
	 * ����ɳ���¼����ʱ��
	 * 
	 * @param strTime
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String publishedTime(String time) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long second = 0;
		long l = 0;
		long month = 0;
		long day = 0;
		long hour = 0;
		long min = 0;
		try {
			Date nowDate = df.parse(getCurrDateStr("yyyy-MM-dd HH:mm:ss"));
			Date date = df.parse(time);
			l = nowDate.getTime() - date.getTime();
			month = (l / (24 * 60 * 60 * 1000)) / 30;
			day = l / (24 * 60 * 60 * 1000);
			hour = (l / (60 * 60 * 1000) - day * 24);
			min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
			second = ((l / 1000) - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
			if (hour < 1 && day < 1 && nowDate.getDay() == date.getDay()) {
				return interceptDateStr(time, "HH:mm");
			}
			if (hour < 1 && day < 1 && nowDate.getDay() != date.getDay()) {
				return "����" + interceptDateStr(time, "HH:mm");
			}
			if (day < 1 && hour >= 1 && nowDate.getDay() == date.getDay()) {
				return "����  " + interceptDateStr(time, "HH:mm");
			}
			if (day < 1 && hour >= 1 && nowDate.getDay() != date.getDay()) {
				return "����  " + interceptDateStr(time, "HH:mm");
			}
			if (day < 2 && day >= 1 && (nowDate.getDay() - date.getDay() == 1)) {
				return "����" + interceptDateStr(time, "HH:mm");
			}
			if (day < 2 && day >= 1 && (nowDate.getDay() - date.getDay() != 1)) {
				return interceptDateStr(time, "MM-dd HH:mm");
			}
			if (day < 365 && day >= 2) {
				return interceptDateStr(time, "MM-dd HH:mm");
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return interceptDateStr(time, "yyyy-MM-dd HH:mm");
	}

	/**
	 * ����ɳ����۷���ʱ��
	 * 
	 * @param strTime
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String publishedTime2(String time) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long second = 0;
		long l = 0;
		long month = 0;
		long day = 0;
		long hour = 0;
		long min = 0;
		try {
			Date nowDate = df.parse(getCurrDateStr("yyyy-MM-dd HH:mm:ss"));
			Date date = df.parse(time);
			l = nowDate.getTime() - date.getTime();
			month = l / (30 * 24 * 60 * 60 * 1000);
			day = l / (24 * 60 * 60 * 1000);
			hour = (l / (60 * 60 * 1000) - day * 24);
			min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
			second = ((l / 1000) - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
			if (min < 1) {
				return second + "��ǰ";
			}
			if (hour < 1 && min >= 1 && day < 1) {
				return min + "����ǰ";
			}
			if (day < 1 && hour >= 1 && nowDate.getDay() == date.getDay()) {
				return "����  " + interceptDateStr(time, "HH:mm");
			}
			if (day < 1 && hour >= 1 && nowDate.getDay() != date.getDay()) {
				return "����  " + interceptDateStr(time, "HH:mm");
			}
			if (day < 2 && day >= 1 && (nowDate.getDay() - date.getDay() == 1)) {
				return "����" + interceptDateStr(time, "HH:mm");
			}
			if (day < 2 && day >= 1 && (nowDate.getDay() - date.getDay() != 1)) {
				return interceptDateStr(time, "MM-dd HH:mm");
			}
			if (day < 30 && day >= 2) {
				return interceptDateStr(time, "MM-dd HH:mm");
			}
			if (day < 365 && day >= 30) {
				return interceptDateStr(time, "MM-dd HH:mm");
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return interceptDateStr(time, "yyyy-MM-dd HH:mm");
	}

	/**
	 * ��������/��̬����ʱ��
	 * 
	 * @param strTime
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String publishedTime3(String time) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		long l = 0;
		long day = 0;
		try {
			Date nowDate = df.parse(getCurrDateStr("yyyy-MM-dd HH:mm"));
			Date date = df.parse(time);
			l = nowDate.getTime() - date.getTime();
			day = l / (24 * 60 * 60 * 1000);
			if (day < 1 && nowDate.getDay() == date.getDay()) {
				return interceptDateStr(time, "HH:mm");
			}
			if (day < 1 && nowDate.getDay() != date.getDay()) {
				return interceptDateStr(time, "MM-dd HH:mm");
			}
			if (day < 365 && day >= 1) {
				return interceptDateStr(time, "MM-dd HH:mm");
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return interceptDateStr(time, "yyyy-MM-dd HH:mm");
	}

	public static long getDay(String time) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		long day = 0;
		try {
			Date now = df.parse(getCurrDateStr());
			Date date = df.parse(time);
			long l = now.getTime() - date.getTime();
			day = l / (24 * 60 * 60 * 1000);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return day;
	}

	public static String getHour(String time) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long hour = 0;
		try {
			Date now = df.parse(getCurrDateStr("yyyy-MM-dd HH:mm:ss"));
			Date date = df.parse(time);
			long l = now.getTime() - date.getTime();
			long day = l / (24 * 60 * 60 * 1000);
			hour = (l / (60 * 60 * 1000) - day * 24);
			long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
			if (hour < 1) {
				if (min == 0) {
					return "�ո�";
				}
				return min + "����ǰ";
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return hour + "Сʱǰ";

	}

	/**
	 * �������ڱȽ� ����ʱʹ��
	 * 
	 * @param starTime
	 * @param endTime
	 * @return
	 */
	public static boolean compareDate(String starTime, String endTime,
			int timeInterval) {
		SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date begin = dfs.parse(starTime);
			Date end = dfs.parse(endTime);
			long between = (end.getTime() - begin.getTime()) / 1000;// ����1000��Ϊ��ת������
			long minute = between % 3600 / 60;
			if (minute >= timeInterval) {
				return true;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return false;

	}

	/**
	 * �༭����ʱѡ������ʹ��
	 * 
	 * @param starTime
	 * @param endTime
	 * @return
	 */
	public static boolean compareDate(String starTime, String endTime) {
		SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		boolean flag = false;
		try {
			Date begin = dfs.parse(starTime);
			Date end = dfs.parse(endTime);
			flag = end.after(begin);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return flag;

	}

	public static final long A_SECOND = 1000;
	public static final long A_MINUTE = 60 * A_SECOND;
	public static final long AN_HOUR = 60 * A_MINUTE;
	public static final long A_DAY = 24 * AN_HOUR;

	public static String formatTime(String time) {
		Calendar now = Calendar.getInstance();
		return formatTime(time, now.getTimeInMillis());
	}

	public static String formatTime(String time, long baseTime) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date date = df.parse(time);
			long dateTime = date.getTime();
			long diff = baseTime - dateTime;
			if (diff < 10 * A_SECOND) {
				return "�ո�";
			}
			if (diff < A_MINUTE) {
				return diff / A_SECOND + "��ǰ";
			}
			if (diff < AN_HOUR) {
				return diff / A_MINUTE + "����ǰ";
			}
			if (diff < A_DAY) {
				return diff / AN_HOUR + "Сʱǰ";
			}
			if (diff < 2 * A_DAY) {
				if (((baseTime - A_DAY) % A_DAY == dateTime % A_DAY)) {
					return "����";
				} else {
					return "ǰ��";
				}
			}
			if (diff < 3 * A_DAY) {
				if (((baseTime - 2 * A_DAY) % A_DAY == dateTime % A_DAY)) {
					return "ǰ��";
				}
			}

			Calendar target = Calendar.getInstance();
			target.setTimeInMillis(date.getTime());
			Calendar base = Calendar.getInstance();
			base.setTimeInMillis(baseTime);
			if (target.get(Calendar.YEAR) == base.get(Calendar.YEAR)) {
				SimpleDateFormat df2 = new SimpleDateFormat("MM-dd HH:mm");
				return df2.format(target.getTime());
			} else {
				SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				return df2.format(target.getTime());
			}
		} catch (ParseException e) {
			return time;
		}
	}

	public static String getRegisterTime() {
		DateFormat fileNameDF = new SimpleDateFormat("yyyy-MM-dd");
		return fileNameDF.format(new Date());
	}
}