package com.xiangxun.xacity.request;

import android.os.Environment;

import com.xiangxun.xacity.app.AppBuildConfig;

/**
 * @package: com.xiangxun.xacity.request
 * @ClassName: Api.java
 * @Description: 所有链接头部公共部分
 * @author: HanGJ
 * @date: 2015-12-16 上午10:01:31
 */
public class Api {
	/** true为测试机 */
	private static final boolean DEBUGURL = AppBuildConfig.DEBUGURL;

	/** m模块下 */
	public static String urlHostPic = getUrlHead();
	//
	public static String urlHeadMobile = urlHostPic + "mobile/";
	//
	public static String upLoadImageHead = getUpLoadImageHead();

	public static String urlImage = getImageUrl();
	public static String urlImageContract = urlImage + "upload_res/contract/";
	public static String urlImageOccupy = urlImage + "occupy/";
	// path
	public static String XaCity = Environment.getExternalStorageDirectory() + "/XaCity/";

	// 发布拍照path
	public static String xaCityPublishPictureDir = XaCity.concat("publishPicture/");

	private static String getUrlHead() {
		if (DEBUGURL) {
			return "http://193.169.100.217:8090/dmms/";
		}
//		return "http://211.149.205.5:8080/dmms/";
		return "http://124.114.151.2:15689/dmms/";
	}

	private static String getImageUrl() {
		if (DEBUGURL) {
			return "http://193.169.100.217:8090/dmms/";
		}
//		return "http://211.149.205.5:8080/dmms/";
		return "http://124.114.151.2:15689/dmms/";

	}

	private static String getUpLoadImageHead() {
		if (DEBUGURL) {
			return urlHeadMobile + "/file/upload/?callback=JSON_CALLBACK";
		}
		return urlHeadMobile + "/file/upload/?callback=JSON_CALLBACK";
	}

}
