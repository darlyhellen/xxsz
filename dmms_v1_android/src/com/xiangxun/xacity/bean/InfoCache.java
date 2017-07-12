package com.xiangxun.xacity.bean;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.baidu.location.BDLocation;
import com.xiangxun.xacity.bean.LoginData.UserData;
import com.xiangxun.xacity.utils.Logger;

public class InfoCache {
	private static InfoCache infoCache;
	private String locaiton_addres;
	private BDLocation mylocation;
	private UserData userData;
	private UpdateData mData;// 更新数据
	public String curVersionName = "";// 当前版本号
	private boolean isNewVer = false;// 是否有新版本

	private InfoCache() {
	}

	public static InfoCache getInstance() {
		if (infoCache == null) {
			infoCache = new InfoCache();
		}
		return infoCache;
	}

	// 获取定位地址
	public String getmAddr() {
		return locaiton_addres;
	}

	public void setmAddr(String mAddr) {
		this.locaiton_addres = mAddr;
	}

	// 定位的location
	public void setLocaiton(BDLocation location) {
		mylocation = location;
	}

	public BDLocation getLocation() {
		return mylocation;
	}

	public UserData getUserData() {
		return userData;
	}

	public void setUserData(UserData userData) {
		this.userData = userData;
	}
	
	/**
	 * 返回当前程序版本名
	 */
	public String getAppVersionName(Context context) {
		if (TextUtils.isEmpty(curVersionName)) {
			try {
				PackageManager pm = context.getPackageManager();
				PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
				curVersionName = pi.versionName;
			} catch (Exception e) {
				Logger.e("VersionInfo"+"Exception", e);
			}
		}
		return curVersionName;
	}
	
	/**
	 * 返回当前程序更新信息
	 */
	public UpdateData getmData() {
		return mData;
	}

	public void setmData(UpdateData mData) {
		this.mData = mData;
	}
	
	public boolean isNewVer() {
		return isNewVer;
	}

	public void setNewVer(boolean isNewVer) {
		this.isNewVer = isNewVer;
	}

}
