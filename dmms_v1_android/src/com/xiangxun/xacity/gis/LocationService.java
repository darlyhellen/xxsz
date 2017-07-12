package com.xiangxun.xacity.gis;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.xiangxun.xacity.app.XiangXunApplication;
import com.xiangxun.xacity.bean.ResultBeans.GPSData;
import com.xiangxun.xacity.request.DcNetWorkUtils;
import com.xiangxun.xacity.utils.Logger;
import com.xiangxun.xacity.utils.ShareDataUtils;
import com.xiangxun.xacity.utils.SharedPreferencesKeys;

public class LocationService extends Service {
	private Timer timer;
	private LocationClient mLocationClient;
	private GisLocationListener locListener = new GisLocationListener();
	private int locateSpaceUnit = 180000;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			acquireWakeLock();
			requestLocation();
		}
	};

	@Override
	public void onCreate() {
		locateSpaceUnit = ShareDataUtils.getSharedIntData(this, SharedPreferencesKeys.LOCATESPACEUNIT);
		if (locateSpaceUnit <= 0) {
			locateSpaceUnit = 180000;
		}
		initLocation();
		startSyncLocation();
		super.onCreate();
	}

	private void startLocation() {
		mLocationClient = new LocationClient(this);
		mLocationClient.registerLocationListener(locListener);
		mLocationClient.start();
	}
	
	@Override
	@Deprecated
	public void onStart(Intent intent, int startId) {
		acquireWakeLock();
		super.onStart(intent, startId);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// 执行文件的下载或者播放等操作
		/*
		 * 这里返回状态有三个值，分别是:
		 * 1、START_STICKY：当服务进程在运行时被杀死，系统将会把它置为started状态，但是不保存其传递的Intent对象
		 * ，之后，系统会尝试重新创建服务;
		 * 2、START_NOT_STICKY：当服务进程在运行时被杀死，并且没有新的Intent对象传递过来的话，
		 * 系统将会把它置为started状态， 但是系统不会重新创建服务，直到startService(Intent intent)方法再次被调用;
		 * 3、START_REDELIVER_INTENT：当服务进程在运行时被杀死，它将会在隔一段时间后自动创建，
		 * 并且最后一个传递的Intent对象将会再次传递过来。
		 */
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private void startSyncLocation() {
		TimerTask timerTask = new LocationSyncTimerTask();
		timer = new Timer(true);
		Logger.i("执行间隔时间--->" + locateSpaceUnit);
		timer.scheduleAtFixedRate(timerTask, 1000, locateSpaceUnit);
	}

	private void stopSyncLocation() {
		timer.cancel();
	}

	private class LocationSyncTimerTask extends TimerTask {

		@Override
		public void run() {
			Logger.i("每隔" + locateSpaceUnit + "执行一次");
			handler.sendEmptyMessage(0);
		}
	}

	@Override
	public void onDestroy() {
		stopSyncLocation();
		releaseWakeLock();
		super.onDestroy();
	}

	private void initLocation() {
		startLocation();
		LocationClientOption option = new LocationClientOption();
		// option.setScanSpan(0);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
		option.setOpenGps(true);// 可选，默认false,设置是否使用gps
		option.setAddrType("all");
		option.setCoorType("bd09ll");
		option.disableCache(true);
		option.setPriority(LocationClientOption.GpsFirst);
		mLocationClient.setLocOption(option);
	}

	private long tempTime = 0;

	private class GisLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null) {
				return;
			}
			Logger.i("坐标系类型:coorType = " + location.getCoorType());
			if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
				Logger.i("gps定位成功");
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
				Logger.i("网络定位成功  运营商信息" + location.getOperators());
			} else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
				Logger.i("离线定位成功，离线定位结果也是有效的");
			} else if (location.getLocType() == BDLocation.TypeServerError) {
				Logger.i("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
				closeLocation();
				return;
			} else if (location.getLocType() == BDLocation.TypeNetWorkException) {
				closeLocation();
				Logger.i("网络不同导致定位失败，请检查网络是否通畅");
				return;
			} else if (location.getLocType() == BDLocation.TypeCriteriaException) {
				closeLocation();
				Logger.i("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
				return;
			}
			if ("gcj02".equals(location.getCoorType())) {
				Logger.i("gcj02 坐标系转换成bd09ll坐标系");
				double[] gps = gcj02_To_Bd09_Lat(location.getLatitude(), location.getLongitude());
				location.setLatitude(gps[0]);
				location.setLongitude(gps[1]);
			}

			if (System.currentTimeMillis() - tempTime < locateSpaceUnit) {
				tempTime = System.currentTimeMillis();
				Logger.i("定位上传时间间隔小于定位间隔时间参数");
				return;
			}
			tempTime = System.currentTimeMillis();
			closeLocation();
			double latitudeX = location.getLatitude();
			double tempX = latitudeX + 0.00420;
			double longitudeY = location.getLongitude();
			double tempY = longitudeY + 0.01113;
			double offX = location.getLatitude() - tempX;
			double offY = location.getLongitude() - tempY;
			DecimalFormat decimalFormat = new DecimalFormat("#.#########");
			double resultX = location.getLatitude() + offX;
			double resultY = location.getLongitude() + offY;
			String latitude = String.valueOf(decimalFormat.format(resultX));
			String longitude = String.valueOf(decimalFormat.format(resultY));//
			String address = location.getAddrStr();// 获取地址
			Logger.e("+++处理前经纬度 " + "经度 =" + location.getLongitude() + ",  纬度 = " + location.getLatitude());
			Logger.e("---处理后经纬度 " + "经度 =" + longitude + ",  纬度 = " + latitude);
			sucess(latitude, longitude, address);
		}
	}

	private double pi = 3.14159265358979324 * 3000.0 / 180.0;

	private double[] gcj02_To_Bd09_Lat(double gg_lat, double gg_lon) {
		double[] gps = new double[2];
		double x = gg_lon, y = gg_lat;
		double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * pi);
		double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * pi);
		double bd_lon = z * Math.cos(theta) + 0.0065;
		double bd_lat = z * Math.sin(theta) + 0.006;
		gps[0] = bd_lat;
		gps[1] = bd_lon;
		return gps;
	}

	@SuppressLint("SimpleDateFormat")
	private void sucess(String latitude, String longitude, String address) {
		String userid = ShareDataUtils.getSharedStringData(XiangXunApplication.getInstance(), SharedPreferencesKeys.USERID);
		String name = ShareDataUtils.getSharedStringData(XiangXunApplication.getInstance(), SharedPreferencesKeys.NAME);
		String deptid = ShareDataUtils.getSharedStringData(XiangXunApplication.getInstance(), SharedPreferencesKeys.DEPTID);
		String userPhone = ShareDataUtils.getSharedStringData(XiangXunApplication.getInstance(), SharedPreferencesKeys.USERPHONE);
		GPSData data = new GPSData();
		data.orgId = deptid;
		data.gpsStatus = "1";
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		data.gpsTime = dateFormat.format(new Date());
		data.latitude = latitude;
		data.longitude = longitude;
		data.userPhone = userPhone;
		data.locateAddress = address;
		data.userId = userid;
		if (userid.length() > 0 && name.length() > 0 && deptid.length() > 0) {
			Logger.e("LocationService-xiangxun ---" + " 上传定位数据");
			DcNetWorkUtils.locationPosition(XiangXunApplication.getInstance(), data);
		} else {
			Logger.e("LocationService-xiangxun ---" + " 用户数据获取失败");
		}
	}

	public void requestLocation() {
		if (mLocationClient != null && mLocationClient.isStarted()) {
			initLocation();
			mLocationClient.requestLocation();
		} else {
			startLocation();
		}
	}

	private void closeLocation() {
		if (mLocationClient != null) {
			if (locListener != null) {
				mLocationClient.unRegisterLocationListener(locListener);
			}
			mLocationClient.stop();
			mLocationClient = null;
		}
	}

	private WakeLock wakeLock;

	// 获取电源锁，保持该服务在屏幕熄灭时仍然获取CPU时，保持运行
	private void acquireWakeLock() {
		if (null == wakeLock) {
			PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
			wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, getClass().getCanonicalName());
			if (null != wakeLock) {
				// Log.i(TAG, "call acquireWakeLock");
				wakeLock.acquire();
			}
		}
	}

	// 释放设备电源锁
	private void releaseWakeLock() {
		if (null != wakeLock && wakeLock.isHeld()) {
			// Log.i(TAG, "call releaseWakeLock");
			wakeLock.release();
			wakeLock = null;
		}
	}

}
