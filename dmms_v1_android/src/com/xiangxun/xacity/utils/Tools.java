package com.xiangxun.xacity.utils;

import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * @package: com.huatek.api.utils
 * @ClassName: Tools
 * @Description: 工具类
 * @author: aaron_han
 * @data: 2015-1-16 下午5:03:23
 */
@SuppressLint("SimpleDateFormat")
public class Tools {

	// 判断字符串是否为邮件格式
	public static boolean isEmail(String email) {
		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);
		return m.matches();
	}

	/**
	 * 是否为IP地址
	 * 
	 * @param ip
	 * @return
	 */
	public static boolean isIP(String ip) {
		String str = "^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(ip);
		return m.matches();
	}

	public static boolean isSpecialCharacter(String str) {

		String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		if (m.find()) {
			return true;
		}
		return false;
	}

	public static boolean isSpecialCharacterBut(String str) {

		String regEx = "[`~!@#$%^&*()+=|{}':;'\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’、？]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		if (m.find()) {
			return true;
		}
		return false;
	}

	public static boolean isCarnumberNO(String carnumber) {
		/*
		 * 车牌号格式：汉字 + A-Z + 5位A-Z或0-9 （只包括了普通车牌号，教练车和部分部队车等车牌号不包括在内）
		 */
		String carnumRegex = "[\u4e00-\u9fa5]{1}[A-Z]{1}([A-Z_0-9]{5}|[A-Z_0-9]{4}[\u4e00-\u9fa5]{1})";
		if (TextUtils.isEmpty(carnumber))
			return false;
		else
			return carnumber.matches(carnumRegex);
	}

	public static boolean isSpecialCharacterContact(String str) {
		String regEx = "[{~!@#$￥%^&*<>,.:;?'=\\+|}/()]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		if (m.find()) {
			return true;
		}
		return false;
	}

	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}

	// 判断是否为手机号码
	public static boolean isPhoneNumber(String number) {
		if (number != null && number.length() == 11) {
			String value = number;
			String regExp = "^[1]([38][0-9]{1}|[4][75]|[5][012356789]|[7][0678])[0-9]{8}$";
			Pattern p = Pattern.compile(regExp);
			Matcher m = p.matcher(value);
			return m.find();
		} else {
			return false;
		}
	}

	// 格式化日期
	public static String getCurrentDate(String dateTime) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String date = formatter.format(Long.valueOf(dateTime) * 1000);
		return date;
	}

	public static final String getDateAsFileName() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		Date d = new Date();
		return format.format(d);
	}

	public static final String getDate() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = new Date();
		return format.format(d);
	}

	// 格式化日期
	public static String getConsumerNoteTime(String dateTime) {
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		// 今年不显示年份
		if (dateTime.substring(0, 4).equals(String.valueOf(year))) {
			return dateTime.substring(4, 6) + "-" + dateTime.substring(6, 8) + " " + dateTime.substring(8, 10) + ":" + dateTime.substring(10, 12) + ":" + dateTime.substring(12, 14);
		} else {
			return dateTime.substring(0, 4) + "-" + dateTime.substring(4, 6) + "-" + dateTime.substring(6, 8) + " " + dateTime.substring(8, 10) + ":" + dateTime.substring(10, 12) + ":" + dateTime.substring(12, 14);
		}
	}

	public static String getTime(String dateTime) {
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		// 今年不显示年份
		if (dateTime.substring(0, 4).equals(String.valueOf(year))) {
			dateTime.substring(5, 7);
			return dateTime.substring(5, dateTime.length());
		} else {
			return dateTime;
		}
	}

	public static String getDateTime(String dateTime) {
		return dateTime.substring(0, 4) + "年" + dateTime.substring(4, 6) + "月" + dateTime.substring(6, 8) + "日";
	}

	public static String getDateTime2(String dateTime) {
		return dateTime.substring(0, 4) + "年" + dateTime.substring(4, 6) + "月" + dateTime.substring(6, 8) + "日 " + dateTime.substring(8, 10) + ":" + dateTime.substring(10, 12);
	}

	public static String getAlarmTime(String dateTime) {
		return dateTime.substring(8, 10) + ":" + dateTime.substring(10, 12) + ":" + dateTime.substring(12, 14);
	}

	public static String getDateTimeFormat(String dateTime) {
		return dateTime.substring(0, 4) + "年" + dateTime.substring(4, 6) + "月" + dateTime.substring(6, 8) + "日";
	}

	public static String getDateFormat(long l) {
		Date d = new Date(l);
		GregorianCalendar g1 = new GregorianCalendar();
		GregorianCalendar g2 = new GregorianCalendar();
		g2.setTime(d);
		if (g1.get(Calendar.YEAR) == g2.get(Calendar.YEAR)) {
			SimpleDateFormat sdf2 = new SimpleDateFormat("MM-dd");
			return sdf2.format(d);
		} else {
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
			return sdf1.format(d);
		}
	}

	// 只显示月-日格式的日期字符串_by_maogy
	public static String getMDData(String dateTime) {
		SimpleDateFormat formatter = new SimpleDateFormat("MM-dd");
		return formatter.format(Long.valueOf(dateTime) * 1000);
	}

	/**
	 * 设置前景色
	 * 
	 * @param str
	 *            目标字符串
	 * @param start
	 *            开始位置
	 * @param end
	 *            结束位置
	 * @param color
	 *            颜色值 如Color.BLACK
	 * @return
	 */
	public static SpannableString getForegroundColorSpan(String str, int start, int end, int color) {
		SpannableString ss = new SpannableString(str);
		ss.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return ss;
	}

	// 获取手机唯一标识
	public static String getDeviceId(Activity act) {
		TelephonyManager mTelephonyMgr = (TelephonyManager) act.getSystemService(Context.TELEPHONY_SERVICE);
		return mTelephonyMgr.getDeviceId();
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	public static int px2dp(Context context, float px) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (px / scale + 0.5f);
	}

	/**
	 * 将sp值转换为px值，保证文字大小不变
	 * 
	 * @param spValue
	 * @param fontScale
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static int sp2px(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

	/**
	 * 设置字体大小，用px
	 * 
	 * @param context
	 * 
	 * @param str
	 *            目标字符串
	 * @param start
	 *            开始位置
	 * @param end
	 *            结束位置
	 * @param pxSize
	 *            像素大小
	 * @return
	 */
	public static SpannableString getSizeSpanUsePx(String str, int start, int end, int pxSize) {
		SpannableString ss = new SpannableString(str);
		ss.setSpan(new AbsoluteSizeSpan(pxSize), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return ss;
	}

	private static long lastClickTime;

	public static boolean isFastDoubleClick() {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;
		if (0 < timeD && timeD < 1500) {
			return true;
		}
		lastClickTime = time;
		return false;
	}

	// 获取屏幕分辨率
	public static int[] getScreenPixel(Activity context) {
		int[] pixel = new int[2];
		DisplayMetrics mDisplayMetrics = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
		int width = mDisplayMetrics.widthPixels;
		int height = mDisplayMetrics.heightPixels;
		pixel[0] = width;
		pixel[1] = height;
		return pixel;
	}

	/**
	 * 
	 * @Title:
	 * @Description:递归统计文件夹大小
	 * @param: @param f
	 * @param: @return
	 * @param: @throws Exception
	 * @return: long
	 * @throws
	 */
	public static long getFileSize(File f) throws Exception {
		long size = 0;
		File flist[] = f.listFiles();
		for (int i = 0; i < flist.length; i++) {
			if (flist[i].isDirectory()) {
				size = size + getFileSize(flist[i]);
			} else {
				size = size + flist[i].length();
			}
		}
		return size;
	}

	public static void deleteFile(File file) {
		if (file.exists()) { // 判断文件是否存在
			if (file.isFile()) { // 判断是否是文件
				file.delete(); // delete()方法 你应该知道 是删除的意思;
			} else if (file.isDirectory()) { // 否则如果它是一个目录
				File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
				for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
					deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
				}
			}
			file.delete();
		} else {
			System.out.println("所删除的文件不存在！" + '\n');
		}
	}

	public static StringBuilder sb;
	static {
		sb = new StringBuilder();
	}

	public static StringBuilder getSB() {
		return sb.delete(0, sb.length());
	}

	/**
	 * @return
	 * @Title:
	 * @Description: 计算总页数
	 * @param: @param result
	 * @return: void
	 * @throws
	 */
	public static int getTotalPage(int k) {
		float i = (float) (k / 10.0);
		int j = k / 10;
		if (i > j) {
			return k / 10 + 1;
		} else {
			return k / 10;
		}
	}

	public static String getDecimalFormatTwo(float d) {
		DecimalFormat df = new DecimalFormat("#.#");
		if (d > (int) d) {
			return df.format(d);
		} else {
			return "" + (int) d;
		}
	}

	public static void ETDecimalFormatTwo(final EditText et) {
		et.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				String temp = s.toString();
				int d = temp.indexOf(".");
				if (d < 0) {
					return;
				} else {
					if (temp.length() - d - 1 > 2) {
						s.delete(d + 3, d + 4);
					} else if (d == 0) {
						s.delete(d, d + 1);
					} else if (d == 6) {
						s.delete(d, d + 1);
					}
				}
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
		});
	}

	// 重新计算lisView 的高度
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	// 重新计算lisView 的高度
	public static boolean getUpdataOneDay(long time_start, long time_end) {
		long time = 0;
		if (time_start > time_end) {
			time = time_end - time_start;
		} else {
			time = time_start - time_end;
		}
		if (time > 86400000) {
			return true;
		} else {
			return false;
		}
	}

	// 字符串大于多少省略号结尾
	public static String subStringCN(final String str, final int maxLength) {
		if (str == null) {
			return str;
		}
		String suffix = "...";
		int suffixLen = suffix.length();

		final StringBuffer sbuffer = new StringBuffer();
		final char[] chr = str.trim().toCharArray();
		int len = 0;
		for (int i = 0; i < chr.length; i++) {

			if (chr[i] >= 0xa1) {
				len += 2;
			} else {
				len++;
			}
		}

		if (len <= maxLength) {
			return str.replace(" ", "");
		}

		len = 0;
		for (int i = 0; i < chr.length; i++) {

			if (chr[i] >= 0xa1) {
				len += 2;
				if (len + suffixLen > maxLength) {
					break;
				} else {
					sbuffer.append(chr[i]);
				}
			} else {
				len++;
				if (len + suffixLen > maxLength) {
					break;
				} else {
					sbuffer.append(chr[i]);
				}
			}
		}
		sbuffer.append(suffix);
		return sbuffer.toString().replace(" ", "");
	}

	public static String number3Add(String str1) {
		str1 = new StringBuilder(str1).reverse().toString(); // 先将字符串颠倒顺序
		String str2 = "";
		for (int i = 0; i < str1.length(); i++) {
			if (i * 3 + 3 > str1.length()) {
				str2 += str1.substring(i * 3, str1.length());
				break;
			}
			str2 += str1.substring(i * 3, i * 3 + 3) + ",";
		}
		if (str2.endsWith(",")) {
			str2 = str2.substring(0, str2.length() - 1);
		}
		// 最后再将顺序反转过来
		// System.err.println(new StringBuilder(str2).reverse().toString());
		return new StringBuilder(str2).reverse().toString();
	}

	// 判断是否是汉字
	public static boolean chontainsChinese(String s) {
		if (null == s || "".equals(s.trim()))
			return false;
		return !s.matches("[\\u4E00-\\u9FA5A-Za-z]+");
	}

	/**
	 * 
	 * @Title:
	 * @Description: 没有一个汉字和字母
	 * @param: @param s
	 * @param: @return
	 * @return: boolean
	 * @throws
	 */
	public static boolean iSnullityString(String s) {
		int size = s.length();
		for (int i = 0; i < size; i++) {
			if (!chontainsChinese(s.substring(i, i + 1))) {
				return false;
			}
		}
		return true;
	}

	public static boolean isChinese(char ch) {

		if (Character.getType(ch) == Character.OTHER_LETTER) {
			Logger.e("chinese_leter:" + ch);
		}
		// 数字
		else if (Character.isDigit(ch)) {
			Logger.e("digit:" + ch);
		}
		// 字母
		else if (Character.isLetter(ch)) {
			Logger.e("letter:" + ch);
		}
		// 其它字符
		else {
			Logger.e("others:" + ch);
		}
		int c = (int) ch;
		if ((c >= 0x0391 && c <= 0xFFE5) // 中文字符
				|| (c >= 0x0000 && c <= 0x00FF)) // 英文字符
			return true;
		return false;
	}

	public static String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			Logger.e("WifiPreference IpAddress" + ex.toString());
		}
		return null;
	}

	/**
	 * 获取当前ip地址
	 * 
	 * @param context
	 * @return
	 */
	public static String getLocalIpAddress(Context context) {
		try {
			WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			WifiInfo wifiInfo = wifiManager.getConnectionInfo();
			int i = wifiInfo.getIpAddress();
			return int2ip(i);
		} catch (Exception ex) {
			return " 获取IP出错鸟!!!!请保证是WIFI,或者请重新打开网络!\n" + ex.getMessage();
		}
	}

	/**
	 * 将ip的整数形式转换成ip形式
	 * 
	 * @param ipInt
	 * @return
	 */
	public static String int2ip(int ipInt) {
		StringBuilder sb = new StringBuilder();
		sb.append(ipInt & 0xFF).append(".");
		sb.append((ipInt >> 8) & 0xFF).append(".");
		sb.append((ipInt >> 16) & 0xFF).append(".");
		sb.append((ipInt >> 24) & 0xFF);
		return sb.toString();
	}

	public static String dayToMonth(float day) {
		if (day >= 30) {
			int month = (int) (day / 30);
			int day2 = (int) (day - month * 30);
			if (day2 <= 0) {
				return Tools.getSB().append(month).append("个月").toString();
			} else {
				return Tools.getSB().append(month).append("个月").append(day2).append("天").toString();
			}
		} else {
			// if (day < 1) {
			// day = 1;
			// }
			return Tools.getSB().append((int) day).append("天").toString();
		}
	}

	public static String MonthToMD(float mPeriod) {
		StringBuilder sBuilder = new StringBuilder();
		int periodM = (int) mPeriod;
		int periodD = (int) ((mPeriod - periodM) * 30);
		if (periodM != 0) {
			sBuilder.append(periodM).append("个月");
		}
		if (periodM == 0 || periodD != 0) {
			sBuilder.append(periodD).append("天");
		}
		return sBuilder.toString();
	}

	/**
	 * 处理日期
	 * 
	 * @param addDay
	 *            当天日期增加几天
	 * @param dateTime
	 *            当前日期时间(格式必须是XXXX-XX-XX XX:XX:XX)
	 * @return
	 */
	public static String dateTimeInfo(int addDay, String dateTime) {
		String[] months_big = { "1", "3", "5", "7", "8", "10", "12" };
		String[] months_little = { "4", "6", "9", "11" };
		List<String> list_big = Arrays.asList(months_big);
		List<String> list_little = Arrays.asList(months_little);
		String startYearSplit = dateTime.split(" ")[0].split("-")[0];
		String startMonthSplit = dateTime.split(" ")[0].split("-")[1];
		String startDaySplit = dateTime.split(" ")[0].split("-")[2];
		int year = Integer.parseInt(startYearSplit);
		int month = Integer.parseInt(startMonthSplit);
		int day = Integer.parseInt(startDaySplit);
		int addday = 0;
		if (list_big.contains(startMonthSplit)) {// 大月 31天
			addday = day + addDay;
			if (addday > 31) {// 判断是否跨月
				// 跨月 月份+1 天-本月最大天数
				month = month + 1;
				addday = addday - 31;
			}
			if (month > 12) {// 判断是否跨年
				// 跨年 年分+1 月份-12
				year = year + 1;
				month = month - 12;
			}
		} else if (list_little.contains(startMonthSplit)) {// 小月 30天
			addday = day + addDay;
			if (addday > 30) {// 判断是否跨月
				// 跨月 月份+1 天-本月最大天数
				month = month + 1;
				addday = addday - 30;
			}
			if (month > 12) {// 判断是否跨年
				// 跨年 年分+1 月份-12
				year = year + 1;
				month = month - 12;
			}
		} else {
			addday = day + addDay;
			if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {// 二月
				if (addday > 29) {// 判断是否跨月
					// 跨月 月份+1 天-本月最大天数
					month = month + 1;
					addday = addday - 29;
				}
				if (month > 12) {// 判断是否跨年
					// 跨年 年分+1 月份-12
					year = year + 1;
					month = month - 12;
				}
			} else {// 二月 28天
				if (addday > 28) {// 判断是否跨月
					// 跨月 月份+1 天-本月最大天数
					month = month + 1;
					addday = addday - 28;
				}
				if (month > 12) {// 判断是否跨年
					// 跨年 年分+1 月份-12
					year = year + 1;
					month = month - 12;
				}
			}
		}
		return year + "-" + (month < 10 ? "0" + month : month) + "-" + (addday < 10 ? "0" + addday : addday) + " " + dateTime.split(" ")[1];
	}

	/**
	 * 处理日期相减操作
	 * 
	 * @param subDay
	 *            当天日期减去几天
	 * @param dateTime
	 *            当前日期时间(格式必须是XXXX-XX-XX XX:XX:XX)
	 * @return
	 */
	public static String subDataTimeInfo(int subDay, String dateTime) {
		String[] months_big = { "1", "3", "5", "7", "8", "10", "12" };
		String[] months_little = { "4", "6", "9", "11" };
		List<String> list_big = Arrays.asList(months_big);
		List<String> list_little = Arrays.asList(months_little);
		String startYearSplit = dateTime.split(" ")[0].split("-")[0];
		String startMonthSplit = dateTime.split(" ")[0].split("-")[1];
		String startDaySplit = dateTime.split(" ")[0].split("-")[2];
		int year = Integer.parseInt(startYearSplit);
		int month = Integer.parseInt(startMonthSplit);
		int day = Integer.parseInt(startDaySplit);
		int subday = 0;
		if (list_big.contains(startMonthSplit)) {// 大月 31天
			subday = day - subDay;
			if (subday <= 0) {// 判断是否跨月
				// 跨月 月份-1 天+本月最大天数
				month = month - 1;
				if (month <= 0) {// 判断是否跨年
					// 跨年 年分-1 月份+12
					year = year - 1;
					month = month + 12;
				}
				// 判断相减后是否跨月
				if (list_big.contains(String.valueOf(month))) {
					subday = subday + 31;
				} else if (list_little.contains(String.valueOf(month))) {// 小月
																			// 30天
					subday = subday + 30;
				} else {
					if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {// 二月
						subday = subday + 29;
					} else {
						subday = subday + 28;
					}
				}
			}
		} else if (list_little.contains(startMonthSplit)) {// 小月 30天
			subday = day - subDay;
			if (subday <= 0) {// 判断是否跨月
				// 跨月 月份-1 天+本月最大天数
				month = month - 1;
				if (month <= 0) {// 判断是否跨年
					// 跨年 年分-1 月份+12
					year = year - 1;
					month = month + 12;
				}
				if (list_big.contains(String.valueOf(month))) {
					subday = subday + 31;
				} else if (list_little.contains(String.valueOf(month))) {// 小月
																			// 30天
					subday = subday + 30;
				} else {
					if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {// 二月
						subday = subday + 29;
					} else {
						subday = subday + 28;
					}
				}
			}
		} else {
			subday = day - subDay;
			if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {// 二月
				if (subday <= 0) {// 判断是否跨月
					// 跨月 月份-1 天+本月最大天数
					month = month - 1;
					if (month <= 0) {// 判断是否跨年
						// 跨年 年分-1 月份+12
						year = year - 1;
						month = month + 12;
					}
					if (list_big.contains(String.valueOf(month))) {
						subday = subday + 31;
					} else if (list_little.contains(String.valueOf(month))) {// 小月
																				// 30天
						subday = subday + 30;
					} else {
						if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {// 二月
							subday = subday + 29;
						} else {
							subday = subday + 28;
						}
					}
				}

			} else {// 二月 28天
				if (subday <= 0) {// 判断是否跨月
					// 跨月 月份-1 天+本月最大天数
					month = month - 1;
					if (month <= 0) {// 判断是否跨年
						// 跨年 年分-1 月份+12
						year = year - 1;
						month = month + 12;
					}
					if (list_big.contains(String.valueOf(month))) {
						subday = subday + 31;
					} else if (list_little.contains(String.valueOf(month))) {// 小月
																				// 30天
						subday = subday + 30;
					} else {
						if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {// 二月
							subday = subday + 29;
						} else {
							subday = subday + 28;
						}
					}
				}
			}
		}
		return year + "-" + (month < 10 ? "0" + month : month) + "-" + (subday < 10 ? "0" + subday : subday) + " " + dateTime.split(" ")[1];
	}

	/**
	 * 返回当前程序版本名
	 */
	public static String getAppVersionName(Context context) {
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			return pi.versionName;
		} catch (Exception e) {
			Logger.e("VersionInfo" + "Exception", e);
			return "";
		}
	}

	public static final String getTodayDate() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date d = new Date();
		String date = format.format(d);
		return date;
	}

	public static String isEmpty(String info) {
		return TextUtils.isEmpty(info) ? "" : info;
	}

	public static String computeTime(String startTime, String endTime) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		java.util.Date now;
		long l = 0;
		long day = 0;
		long hour = 0;
		try {
			now = df.parse(startTime);
			java.util.Date date = df.parse(endTime);
			if (now.getTime() >= date.getTime()) {
				return "-1";
			}
			l = date.getTime() - now.getTime();
			day = l / (24 * 60 * 60 * 1000);
			hour = (l / (60 * 60 * 1000) - day * 24);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String dayFloat = "0";
		if (hour > 4) {
			day += 1;
		} else if (hour <= 4 && hour != 0) {
			dayFloat = "5";
		}
		return "" + day + "." + dayFloat;
	}

	public static String computeTimeStr(String startTime, String endTime) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		java.util.Date now;
		try {
			now = df.parse(startTime);
			java.util.Date date = df.parse(endTime);
			if (now.getTime() > date.getTime()) {
				return "-1";
			}
		} catch (ParseException e) {
			e.printStackTrace();
			return "-1";
		}
		return "0";
	}

	public static boolean isServiceRunning(Context context, String serviceName) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> runningServiceInfos = am.getRunningServices(200);
		if (runningServiceInfos.size() <= 0) {
			return false;
		}
		for (ActivityManager.RunningServiceInfo serviceInfo : runningServiceInfos) {
			if (serviceInfo.service.getClassName().equals(serviceName)) {
				return true;
			}
		}
		return false;
	}

}
