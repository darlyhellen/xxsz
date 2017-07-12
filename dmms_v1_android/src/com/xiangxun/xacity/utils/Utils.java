package com.xiangxun.xacity.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

/**
 * @package: com.huatek.api.utils
 * @ClassName: Utils
 * @Description: 工具集合
 * @author: aaron_han
 * @data: 2015-1-26 下午6:22:13
 */
public class Utils {
	public static boolean click_play = false;
	public static final int TOTAL_SECONDS = 24 * 3600;

	public static Calendar Progress2Calendar(float progress) {
		int seconds = (int) (TOTAL_SECONDS * progress);
		int h = seconds / 3600;
		int m = (seconds / 60) % 60;
		int s = seconds % 60;
		Calendar time = Calendar.getInstance();
		time.set(0, 0, 0, h, m, s);
		return time;
	}

	/*
	 * '100x75' ,'75x56' ,'90x67' ,'174x130' ,'135x101' ,'210x157' ,'120x90'
	 * ,'368x276' ,'68x51' ,'280x210' ,'45x45' ,'325x195' ,'800x2000'
	 */
	// 请求图片设置尺寸
	public static int[] sizes = { 174, 368 };
	public static String[] sizess = { "240x180", "800x480", "1280x720" };

	public static int getSize(Context c, int width) {
		// int size = Tools.dip2px(c, width);
		for (int i = 0; i < sizes.length; i++) {
			if (width < sizes[i]) {
				return i;
			}
		}
		return sizes.length - 1;
	}

	public static String trimImageUrl(String imgUrl, int i) {
		if (TextUtils.isEmpty(imgUrl) || !imgUrl.contains("http://")) {
			return "";
		}
		int index = imgUrl.lastIndexOf(".");
		String subEnd = imgUrl.substring(index, imgUrl.length());
		String subStart = imgUrl.substring(0, index);
		String str = subStart + "_" + sizess[i] + subEnd;
		return str;
	}

	public static void tvTextDeleteLineStyle(TextView tv) {
		tv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);// 并加清晰
		tv.getPaint().setAntiAlias(true);// 抗锯齿
	}

	/**
	 * 
	 * @param pic
	 *            原字符串
	 * @param str1
	 *            插入的字符串
	 * @return
	 */
	public static String getInsert(String pic, String str1) {
		StringBuffer s1 = new StringBuffer(pic); // 原字符串
		String hh = s1.substring(s1.lastIndexOf("."));
		System.out.println("ppp==" + hh);
		Pattern p = Pattern.compile(hh); // 插入位置
		Matcher m = p.matcher(s1.toString());

		if (m.find()) {
			s1.insert((m.start() + 0), str1); // 插入字符串

		}
		return s1.toString();
	}

	// 将list等分
	public static <T> List<List<T>> splitList(List<T> list, int pageSize) {
		int listSize = list.size(); // list的大小
		int page = (listSize + (pageSize - 1)) / pageSize; // 页数
		List<List<T>> listArray = new ArrayList<List<T>>(); // 创建list数组
		for (int i = 0; i < page; i++) { // 按照数组大小遍历
			List<T> subList = new ArrayList<T>(); // 数组每一位放入一个分割后的list
			for (int j = 0; j < listSize; j++) { // 遍历待分割的list
				int pageIndex = ((j + 1) + (pageSize - 1)) / pageSize; // 当前记录的页码(第几页)
				if (pageIndex == (i + 1)) { // 当前记录的页码等于要放入的页码时
					subList.add(list.get(j)); // 放入list中的元素到分割后的list(subList)
				}

				if ((j + 1) == ((j + 1) * pageSize)) { // 当放满一页时退出当前循环
					break;
				}
			}
			listArray.add(subList); // 将分割后的list放入对应的数组的位中
		}
		return listArray;
	}

	public static boolean copyApkFromAssets(Context context, String fileName, String path) {
		boolean copyIsFinish = false;
		InputStream is = null;
		FileOutputStream fos = null;
		try {
			is = context.getAssets().open(fileName);
			File file = new File(path);
			file.createNewFile();
			fos = new FileOutputStream(file);
			byte[] temp = new byte[1024];
			int i = 0;
			while ((i = is.read(temp)) > 0) {
				fos.write(temp, 0, i);
			}
			copyIsFinish = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return copyIsFinish;
	}

	/**
	 * 
	 * @Title:
	 * @Description: 对象转换为字节
	 * @param: @param obj
	 * @param: @return
	 * @return: byte[]
	 * @throws
	 */
	public static byte[] jserialize(Object obj) {
		ObjectOutputStream oos = null;
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(obj);
			return baos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (oos != null)
				try {
					oos.close();
					baos.close();
				} catch (IOException e) {
				}
		}
		return null;
	}

	/**
	 * 
	 * @Title:
	 * @Description: 字节转换为对象
	 * @param: @param bits
	 * @param: @return
	 * @return: Object
	 * @throws
	 */
	public static Object jdeserialize(byte[] bits) {
		ObjectInputStream ois = null;
		ByteArrayInputStream bais = null;
		try {
			bais = new ByteArrayInputStream(bits);
			ois = new ObjectInputStream(bais);
			return ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (ois != null)
				try {
					ois.close();
					bais.close();
				} catch (IOException e) {
				}
		}
		return null;
	}

	/**
	 * 
	 * @Title:
	 * @Description: 对象转字符串
	 * @param: @param obj
	 * @param: @return
	 * @return: String
	 * @throws
	 */
	public static String object2String(Object obj) {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ObjectOutputStream objectOutputStream = null;
		String str = null;
		try {
			objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
			objectOutputStream.writeObject(obj);
			str = new String(Base64.encode(byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (objectOutputStream != null) {
					objectOutputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return str;
	}

	/**
	 * 
	 * @Title:
	 * @Description: 字符串转对象
	 * @param: @param str
	 * @param: @return
	 * @return: Object
	 * @throws
	 */
	public static Object string2Object(String str) {
		byte[] mobileBytes = Base64.decode(str.getBytes(), Base64.DEFAULT);
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(mobileBytes);
		ObjectInputStream objectInputStream = null;
		Object obj = null;
		try {
			objectInputStream = new ObjectInputStream(byteArrayInputStream);
			obj = objectInputStream.readObject();
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (objectInputStream != null) {
					objectInputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				byteArrayInputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return obj;
	}

	/**
	 * 
	 * @Title:
	 * @Description: 分钟 小时倒计时
	 * @param: @param currentTimeMillis
	 * @param: @param itemtime
	 * @param: @return
	 * @return: String
	 * @throws
	 */
	@SuppressLint("SimpleDateFormat")
	public static String showTime(long currentTimeMillis, long itemtime) {
		if (currentTimeMillis > itemtime) {
			long datetime = (currentTimeMillis - itemtime) / 60000;
			if (datetime == 0) {
				return "1分钟前";
			} else if (datetime < 60) {
				return datetime + "分钟前";
			} else if (datetime < 1440) {
				return datetime / 60 + "小时前";
			} else {
				Date d = new Date(itemtime);
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
		} else {
			Date d = new Date(itemtime);
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
	}

	@SuppressLint("SimpleDateFormat")
	public static String showTime(long currentTimeMillis, long itemtime, long date) {
		if (currentTimeMillis > itemtime) {
			long datetime = (currentTimeMillis - itemtime) / 60000;
			if (datetime == 0) {
				return "1分钟前";
			} else if (datetime < 60) {
				return datetime + "分钟前";
			} else if (datetime < 1440) {
				return datetime / 60 + "小时前";
			} else {
				Date d = new Date(date);
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
		} else {
			Date d = new Date(date);
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
	}

	/**
	 * 秒转化为小时分钟
	 */
	public static String ssToHHmmss(int s) {
		int N = s / 3600;
		s = s % 3600;
		int K = s / 60;
		s = s % 60;
		int M = s;
		if (N == 0 && K != 0) {
			return K + "分钟 " + M + "秒";
		} else if (N == 0 && K == 0) {
			return M + "秒";
		} else {
			return N + "小时 " + K + "分钟 " + M + "秒";
		}
	}

	/**
	 * 
	 * @Title:
	 * @Description: 隐藏输入法
	 * @param: @param act
	 * @param: @param view
	 * @return: void
	 * @throws
	 */
	public static void hideSoftInputFromWindow(Activity act, View view) {
		InputMethodManager imm = (InputMethodManager) act.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	public static void hideSoftInputFromWindow(Activity act) {
		View view = act.getCurrentFocus();
		if (view != null)
			((InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	// 隐藏输入发
	public static void hideSoftInputFromWindow(Context context) {
		View view = ((Activity) context).getWindow().peekDecorView();
		if (view != null && view.getWindowToken() != null) {
			InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}

	/**
	 * 保留一位小数
	 */
	public static String decimalFormatNoe(float scale) {
		DecimalFormat fnum = new DecimalFormat("##0.0");
		return fnum.format(scale);
	}

	// 读取pcm文件
	public static byte[] getPCMData(String filePath) {
		File file = new File(filePath);
		FileInputStream inStream;
		try {
			inStream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}

		byte[] data_pack = null;
		if (inStream != null) {
			long size = file.length();

			data_pack = new byte[(int) size];
			try {
				inStream.read(data_pack);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			} finally {
				try {
					inStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return data_pack;
	}

	public static short[] byteArray2ShortArray(byte[] b) {
		int len = b.length / 2;
		int index = 0;
		short[] re = new short[len];
		byte[] buf = new byte[2];
		for (int i = 0; i < b.length;) {
			buf[0] = b[i];
			buf[1] = b[i + 1];
			short st = byteToShort(buf);
			re[index] = st;
			index++;
			i += 2;
		}
		return re;
	}

	public static short byteToShort(byte[] b) {
		short s = 0;
		short s0 = (short) (b[0] & 0xff);// 最低位
		short s1 = (short) (b[1] & 0xff);
		s1 <<= 8;
		s = (short) (s0 | s1);
		return s;
	}

	/**
	 * 
	 * @Title:
	 * @Description:浏览网页
	 * @param: @param act
	 * @param: @param phone
	 * @return: void
	 * @throws
	 */
	public static void browseWebpage(Activity act, String url) {
		if (TextUtils.isEmpty(url)) {
			return;
		}
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		Uri content_url = Uri.parse(url);
		intent.setData(content_url);
		act.startActivity(intent);
	}

	/**
	 * 
	 * @Title:
	 * @Description: 格式化
	 * @param: @param context
	 * @param: @param res
	 * @param: @param obj
	 * @param: @return
	 * @return: String
	 * @throws
	 */
	public static String getFormatString(Context context, int res, Object... obj) {
		return String.format(context.getString(res), obj);
	}

	private static final int UNCONSTRAINED = -1;

	/**
	 * @Description:
	 * @param options
	 * @param minSideLength
	 * @param maxNumOfPixels
	 * @return 动态计算出图片的inSampleSize
	 * @return: int
	 */
	public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}
		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;
		// Math.ceil(Math.sqrt(w * h / maxNumOfPixels)) ：w * h /
		// maxNumOfPixels平方结果的小数部分一律向整数部分进位
		int lowerBound = (maxNumOfPixels == UNCONSTRAINED) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
		// Math.floor(w / minSideLength) 将w / minSideLength结果值一律舍去小数，仅保留整数
		int upperBound = (minSideLength == UNCONSTRAINED) ? 128 : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));
		if (upperBound < lowerBound) {
			return lowerBound;
		}
		if ((maxNumOfPixels == UNCONSTRAINED) && (minSideLength == UNCONSTRAINED)) {
			return 1;
		} else if (minSideLength == UNCONSTRAINED) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	public static String getSDcardPath() {
		File sdDir = Environment.getExternalStorageDirectory();
		return new File(sdDir, "xiangxun/cache").getAbsolutePath();
	}

	public static String getSDcardVideoPath() {
		File sdDir = Environment.getExternalStorageDirectory();
		return new File(sdDir, "xiangxun/video").getAbsolutePath();
	}

}
