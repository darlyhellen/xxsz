package com.xiangxun.xacity.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.xiangxun.xacity.utils.ImageCacheLoader.GetLocalCallBack;

/**
 * @package: com.huatek.api.common
 * @ClassName: LocalNetWorkView
 * @Description: 加载本地图片自定义视图
 * @author: aaron_han
 * @data: 2015-1-16 下午4:56:44
 */

public class LocalNetWorkView extends ImageView {
	public String filePath;// 本地路径
	public String url;
	public GetLocalCallBack gCallBack;// 本地获取图片回调接口
	public Bitmap bm;
	public boolean isInWindow = false;

	/**
	 * @param context
	 */
	public LocalNetWorkView(Context context) {
		super(context);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public LocalNetWorkView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setGCallBack(GetLocalCallBack gCallBack) {
		this.gCallBack = gCallBack;
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		isInWindow = true;
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		isInWindow = false;
	}

}
