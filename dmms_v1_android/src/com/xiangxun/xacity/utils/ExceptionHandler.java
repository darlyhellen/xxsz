package com.xiangxun.xacity.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * @package: com.xiangxun.util
 * @ClassName: ExceptionHandler.java
 * @Description: 统计全局异常
 * @author: HanGJ
 * @date: 2015-8-15 上午8:33:27
 */
public class ExceptionHandler implements UncaughtExceptionHandler {

	private static final String TAG = ExceptionHandler.class.getSimpleName();
	@SuppressLint("SimpleDateFormat")
	private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static ExceptionHandler INSTANCE;
	private Context context;
	// 用来存储设备信息和异常信息
	private Map<String, String> infos = null;
	private boolean isSave = false;
	private String filename;
	private String fileDirs;

	private Handler handler = null;

	private HashMap<String, String> exceptionBean;
	private UncaughtExceptionHandler mDefaultUEH;

	/**
	 * 获取拦截器的对象
	 * 
	 * @param context
	 * @return
	 * @return ExceptionHandler
	 */
	public static ExceptionHandler getInstance(Context context) {
		if (INSTANCE == null) {
			INSTANCE = new ExceptionHandler(context);
		}
		return INSTANCE;
	}

	/**
	 * 如果需要在异常以后处理什么信息 请传入handler
	 * 
	 * @param handler
	 * @return void
	 */
	public void SetHandler(Handler handler) {
		this.handler = handler;
	}

	/**
	 * 设置是否保存日志文件 默认不保存 记得设置sd卡读取权限
	 * 
	 * @param isSave
	 * @return void
	 */
	public void setSave(boolean isSave) {
		this.isSave = isSave;
	}

	/**
	 * 当保存文件的前提下 设置文件名称 文件（名称-时间-毫秒数.log）
	 * 
	 * @param filename
	 * @return void
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}

	/**
	 * 当保存文件的前提下 设置保存目录 如/sdcard/logs/
	 * 
	 * @param fileDirs
	 * @return void
	 */
	public void setFileDirs(String fileDirs) {
		this.fileDirs = fileDirs;
	}

	public ExceptionHandler(Context context) {
		// 在前面得到系统的
		mDefaultUEH = Thread.getDefaultUncaughtExceptionHandler();
		// 设置该CrashHandler为程序的默认处理器
		Thread.setDefaultUncaughtExceptionHandler(this);
		this.context = context;
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		ex.printStackTrace();
		final String msg = collectDeviceInfo(context, ex);
		// Intent localIntent = new Intent(context,
		// ExceptionActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// localIntent.putExtra("projectName",
		// exceptionBean.get("projectName"));
		//
		// localIntent.putExtra("versionName",
		// exceptionBean.get("versionName"));
		// localIntent.putExtra("versionCode",
		// exceptionBean.get("versionCode"));
		// localIntent.putExtra("devInfo", exceptionBean.get("devInfo"));
		//
		// localIntent.putExtra("exceptionMsg",
		// exceptionBean.get("exceptionMsg"));
		// context.startActivity(localIntent);
		if (handler != null) {
			Message message = new Message();
			message.obj = msg;
			handler.sendMessage(message);
		}
		System.out.println("错误：" + msg);
		if (isSave) {
			Logger.print(msg);
		}
//		if (isSave && filename != null && fileDirs != null) {
//			new Thread(new Runnable() {
//				@Override
//				public void run() {
//					saveCrashInfoToFile(msg);
//				}
//			}).start();
//		}

		mDefaultUEH.uncaughtException(thread, ex);
		// ex.printStackTrace();
		// android.os.Process.killProcess(android.os.Process.myPid());
	}

	/**
	 * 收集设备参数信息
	 * 
	 * @param ctx
	 */
	private String collectDeviceInfo(Context ctx, Throwable ex) {
		exceptionBean = new HashMap<String, String>();
		infos = new HashMap<String, String>();
		exceptionBean.put("projectName", ctx.getApplicationInfo().loadLabel(ctx.getPackageManager()).toString());
		try {
			PackageManager pm = ctx.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
			if (pi != null) {
				String versionName = pi.versionName == null ? "null" : pi.versionName;
				String versionCode = pi.versionCode + "";
				exceptionBean.put("versionName", versionName);
				exceptionBean.put("versionCode", versionCode);
				infos.put("versionName", versionName);
				infos.put("versionCode", versionCode);
			}
		} catch (NameNotFoundException e) {
		}
		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				infos.put(field.getName(), field.get(null).toString());
			} catch (Exception e) {
			}
		}
		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, String> entry : infos.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			sb.append(key + "=" + value + "\n");
		}

		exceptionBean.put("devInfo", sb.toString());

		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		String result = writer.toString();
		exceptionBean.put("exceptionMsg", result);
		sb.append(result);
		return sb.toString();
	}

	/**
	 * 保存错误信息到文件中
	 * 
	 * @param ex
	 * @return 返回文件名称,便于将文件传送到服务器
	 */
	public String saveCrashInfoToFile(String msg) {
		FileOutputStream fos = null;
		try {
			long timestamp = System.currentTimeMillis();
			String time = formatter.format(new Date());
			String fileName = filename + "-" + time + "-" + timestamp + ".log";
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				String path = fileDirs;
				if (!path.startsWith("/") || !path.endsWith("/")) {
					Log.e(TAG, "the file path is err");
					return null;
				}
				File dir = new File(path);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				fos = new FileOutputStream(path + fileName);
				fos.write(msg.getBytes());
			}
			return fileName;
		} catch (Exception e) {
			Log.e(TAG, "an error occured while writing file...", e);
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
