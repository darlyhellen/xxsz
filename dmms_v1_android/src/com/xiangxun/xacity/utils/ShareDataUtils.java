package com.xiangxun.xacity.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * @package: com.xiangxun.device.util
 * @ClassName: ShareDataUtils.java
 * @Description: SharedPreferences工具
 * @author: HanGJ
 * @date: 2015-10-16 上午10:28:02
 */
public class ShareDataUtils {
	private static SharedPreferences sp;

	private static void init(Context context) {
		sp = context.getSharedPreferences(SharedPreferencesKeys.SPNAME, Context.MODE_PRIVATE);
	}

	public static void setSharedIntData(Context context, String key, int value) {
		init(context);
		sp.edit().putInt(key, value).commit();
	}

	public static int getSharedIntData(Context context, String key) {
		init(context);
		return sp.getInt(key, 0);
	}

	public static void setSharedlongData(Context context, String key, long value) {
		init(context);
		sp.edit().putLong(key, value).commit();
	}

	public static long getSharedlongData(Context context, String key) {
		init(context);
		return sp.getLong(key, 0l);
	}

	public static void setSharedFloatData(Context context, String key, float value) {
		init(context);
		sp.edit().putFloat(key, value).commit();
	}

	public static Float getSharedFloatData(Context context, String key) {
		init(context);
		return sp.getFloat(key, 0f);
	}

	public static void setSharedBooleanData(Context context, String key, boolean value) {
		init(context);
		sp.edit().putBoolean(key, value).commit();
	}

	public static Boolean getSharedBooleanData(Context context, String key) {
		init(context);
		return sp.getBoolean(key, false);
	}

	public static void setSharedStringData(Context context, String key, String value) {
		init(context);
		sp.edit().putString(key, value).commit();
	}

	public static String getSharedStringData(Context context, String key) {
		init(context);
		return sp.getString(key, "");
	}

	public static SharedPreferences getSp() {
		return sp;
	}

	public static void setSp(SharedPreferences sp) {
		ShareDataUtils.sp = sp;
	}

	public static void saveObject(Context context, String key, Object value) {
		init(context);
		sp.edit().putString(key, Utils.object2String(value)).commit();
	}

	public static Object getObject(Context context, String key) {
		init(context);
		Object obj = Utils.string2Object(sp.getString(key, ""));
		return obj;
	}
	
	/**
	 * 
	 * @Title:
	 * @Description: 保存密码
	 * @param: @param context
	 * @param: @param name
	 * @param: @param key
	 * @param: @param value
	 * @return: void
	 * @throws
	 */
	public static void savePhonePwd(Context context, String name, String key, String value) {
		init(context);
		sp.edit().putString(key, Utils.object2String(value)).commit();
	}

	/**
	 * 
	 * @Title:
	 * @Description: 获得密码
	 * @param: @param context
	 * @param: @param name
	 * @param: @param key
	 * @param: @return
	 * @return: String
	 * @throws
	 */
	public static String findPhonePwd(Context context, String name, String key) {
		init(context);
		Object obj = Utils.string2Object(sp.getString(key, ""));
		return obj == null ? null : obj.toString();
	}

	/**
	 * 
	 * @Title:
	 * @Description: 得到记录的手机号
	 * @param: @param context
	 * @param: @param name
	 * @param: @return
	 * @return: List<String>
	 * @throws
	 */
	public static List<String> findPhoneNumber(Context context, String name) {
		List<String> phoneNumbers = new ArrayList<String>();
		init(context);
		int size = sp.getInt(name + "_size", 0);
		for (int i = 0; i < size; i++) {
			phoneNumbers.add(sp.getString(name + "_" + i, null));
		}
		return phoneNumbers;
	}

	/**
	 * 
	 * @Title:
	 * @Description: 最多保持5个号码
	 * @param: @param context
	 * @param: @param name
	 * @param: @param phoneNumbers
	 * @return: void
	 * @throws
	 */
	public static void savePhoneNumber(Context context, String name, List<String> phoneNumbers) {
		init(context);
		Editor mEdit = sp.edit();
		int phoneSize = phoneNumbers.size() <= 5 ? phoneNumbers.size() : 5;
		mEdit.putInt(name + "_size", phoneSize);

		for (int i = 0; i < phoneSize; i++) {
			mEdit.putString(name + "_" + i, phoneNumbers.get(i));
		}
		mEdit.commit();
	}
	
	public static void clearn(Context context){
		init(context);
		sp.edit().clear().commit();
	}

}