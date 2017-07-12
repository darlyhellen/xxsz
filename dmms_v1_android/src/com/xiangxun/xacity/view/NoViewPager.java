package com.xiangxun.xacity.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @package: com.xiangxun.xacity.view
 * @ClassName: NoViewPager.java
 * @Description: 修改适配器getPageWidth 后 不满一屏幕闪动问题
 * @author: HanGJ
 * @date: 2016-2-4 上午10:39:13
 */
public class NoViewPager extends ViewPager {

	private boolean isPagingEnabled = false;

	public NoViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public NoViewPager(Context context) {
		super(context);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		if (isPagingEnabled) {
			return super.onInterceptTouchEvent(event);
		}
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (isPagingEnabled) {
			return super.onTouchEvent(event);
		}
		return false;
	}

	public void setPagingEnabled(boolean pagingEnabled) {
		isPagingEnabled = pagingEnabled;
	}
}
