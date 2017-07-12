package com.xiangxun.xacity.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


import android.app.Application;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.xiangxun.xacity.bean.InfoCache;
import com.xiangxun.xacity.bean.TreeNode;
import com.xiangxun.xacity.common.DcHttpClient;
import com.xiangxun.xacity.constant.ConstantType;
import com.xiangxun.xacity.map.OnLocationUpdate;
import com.xiangxun.xacity.request.Api;
import com.xiangxun.xacity.utils.Logger;
import com.xiangxun.xacity.utils.ShareDataUtils;
import com.xiangxun.xacity.utils.SharedPreferencesKeys;
import com.xiangxun.xacity.utils.Tools;
import com.xiaoqi.libjpegcompress.ImageUtils;

public class XiangXunApplication extends Application {
	public static Context mContext;
	private static XiangXunApplication sApplication;
	public LocationClient mLocationClient = null;
	public MyLocationListenner myListener = new MyLocationListenner();
	private LocationClientOption option = null;
	private Vector<OnLocationUpdate> vUpdates = new Vector<OnLocationUpdate>();
	public boolean isOpenLocation = true;
	private String mDevId;
	private Logger mLogger;
    public static int CHANNEL_TYPE = 0;
    public static int FIRST_TYPE = 1;
    public static int SECOND_NODE = 2;
    public static Context instance;
    public static List<TreeNode>  firsTreeNodes = new ArrayList<TreeNode>(); //第一级 , 在GroupListFragment.onItemClick()
    public static List<TreeNode>  secondTreeNodes = new ArrayList<TreeNode>(); //第二级 同上
    public static boolean isSuppoertLibjpeg = false;
    
    @Override
	public void onCreate() {
		sApplication = this;
		super.onCreate();
		isSuppoertLibjpeg = ImageUtils.isSupportLibJpeg();
		new Thread() {
			public void run() {
				// 初始化一些常量
				ConstantType.init();
			}
		}.start();
		mContext = getBaseContext();
		// 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
		SDKInitializer.initialize(this);
		// 日志记录
		mLogger = Logger.getLogger("xiangxun");
		Logger.tag = "XaCity";
		DcHttpClient.getInstance().init(getBaseContext());
		mLogger.initLogWriter(Api.XaCity.concat("logs/outDebug.log"));
		mLogger.initExceptionHandler(this);
//		CrashReport.initCrashReport(getApplicationContext(), "900021765", true);
		init();
	}

	public static XiangXunApplication getInstance() {
		return sApplication;
	}
	
	private void init() {
		// 定位
		mLocationClient = new LocationClient(this);
		getLocationDate();
	}

	// 百度定位成功监听
	public class MyLocationListenner implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null) {
			} else {
				try {
					String str = null;
					InfoCache.getInstance().setLocaiton(location);
					InfoCache.getInstance().setmAddr(location.getAddrStr());
					if (location.getCity() != null) {
						try {
							str = location.getCity().substring(0, location.getCity().lastIndexOf("市"));
							setmLocationCity(str);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					for (int i = 0, size = vUpdates.size(); i < size; i++) {
						vUpdates.get(i).update(location);
					}
					if (TextUtils.isEmpty(str)) {
						Logger.e("定位失败");
					} else {
						closeLocation();
						Logger.e("定位成功并关闭" + str);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	};
	
	// 初始化城市信息
	public void getLocationDate() {
		startLocation();
		option = new LocationClientOption();
		try {
			option.setCoorType("bd09ll");
			option.setScanSpan(3600000);
			option.setPriority(LocationClientOption.NetWorkFirst);
			option.setAddrType("all");
			option.disableCache(true);
			mLocationClient.setLocOption(option);
		} catch (Exception e) {
			Log.i("TAG", "打开定位异常" + e.toString());
		}
	}
	
	public void startLocation() {
		mLocationClient.registerLocationListener(myListener);
		mLocationClient.start();
	}

	public void closeLocation() {
		if (mLocationClient != null) {
			if (myListener != null) {
				mLocationClient.unRegisterLocationListener(myListener);
			}
			mLocationClient.stop();
		}
	}

	public void requestLocation() {
		if (mLocationClient != null && mLocationClient.isStarted()) {
			getLocationDate();
			mLocationClient.requestLocation();
		} else {
			startLocation();
		}
	}
	
	public void addLocationListenner(OnLocationUpdate update) {
		vUpdates.add(update);
	}

	public void removeLocationListenner(OnLocationUpdate update) {
		vUpdates.remove(update);
	}

	public String getDevId() {
		if (mDevId == null) {
			TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
			mDevId = tm.getDeviceId();
		}
		return mDevId;
	}
	
	// 定位城市
	public String getmLocationCity() {
		return ShareDataUtils.getSharedStringData(mContext, SharedPreferencesKeys.LOCATION_CITY);
	}

	public void setmLocationCity(String mCity) {
		ShareDataUtils.setSharedStringData(mContext, SharedPreferencesKeys.LOCATION_CITY, mCity);
	}

	// 判断是否第一次运行
	public void setIsFirstIn(String verName) {
		ShareDataUtils.setSharedStringData(mContext, SharedPreferencesKeys.ISFRISTIN, verName);
	}

	public boolean getIsFirstIn() {
		String verNameSP = ShareDataUtils.getSharedStringData(mContext, SharedPreferencesKeys.ISFRISTIN);
		String verName = Tools.getAppVersionName(mContext);
		return verName.compareTo(verNameSP) <= 0;
	}

}
