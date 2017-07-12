package com.xiangxun.xacity.view;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.xiangxun.xacity.R;
import com.xiangxun.xacity.adapter.FullPhoneViewAdapter;
import com.xiangxun.xacity.bean.ResponseResultBeans.ImageBean;
import com.xiangxun.xacity.view.photoview.HackyViewPager;

/**
 * @ClassName: FullScreenSlidePopupWindow.java
 * @Description: 全屏图片
 * @author: HanGJ
 * @date: 2016年5月07日 上午12:17:39
 */
public class FullScreenSlidePopupWindow implements OnClickListener, OnPageChangeListener {
	private Context mContext = null;
	private PopupWindow mPopupWindow = null;
	private View mPopupWindowView = null;
	private LayoutInflater mLayoutInflater = null;

	private TitleView mTitleView = null;
	private ViewPager mViewPager = null;
	private LinearLayout mLlPlacePointLayout = null;

	private List<ImageBean> mPictures = null;
	private ImageView[] mPoints = null;

	private int mCurrentPage = 0;

	public FullScreenSlidePopupWindow(Context context, List<ImageBean> pictures) {
		this.mContext = context;
		this.mPictures = pictures;
	}

	@SuppressWarnings("static-access")
	private void initView() {
		mLayoutInflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
		mPopupWindowView = mLayoutInflater.inflate(R.layout.full_screen_slide_popup_window, null);
		mTitleView = (TitleView) mPopupWindowView.findViewById(R.id.tv_picture_detail_title);
		mViewPager = (HackyViewPager) mPopupWindowView.findViewById(R.id.vp_phone_view);
		mLlPlacePointLayout = (LinearLayout) mPopupWindowView.findViewById(R.id.ll_place_point);
	}

	private void setListener() {
		mTitleView.setOnClickListener(this);
		mTitleView.setLeftBackOneListener(this);
		mViewPager.setOnPageChangeListener(this);
	}

	@SuppressWarnings("deprecation")
	private void setWindowAttribute() {
		mPopupWindow = new PopupWindow(mPopupWindowView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		mPopupWindow.setContentView(mPopupWindowView);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		mPopupWindow.setAnimationStyle(R.style.AnimationFade);
		mPopupWindow.setFocusable(true);
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.setTouchable(true);
		mPopupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
		mPopupWindow.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
		mPopupWindow.showAtLocation(mPopupWindowView, Gravity.CENTER, 0, 0);
	}

	public void startFullScreenSlide(int position) {
		mCurrentPage = position;
		initView();
		setListener();
		setPlacePoint();
		setViewData();
		setWindowAttribute();
	}

	private void setPlacePoint() {
		mPoints = new ImageView[mPictures.size()];
		for (int i = 0, size = mPictures.size(); i < size; i++) {
			mPoints[i] = new ImageView(mContext);
			mPoints[i].setImageResource(R.drawable.circle_white);
			mPoints[i].setLayoutParams(new LayoutParams(20, 20));
			mPoints[i].setPadding(5, 5, 5, 5);
			mLlPlacePointLayout.addView(mPoints[i]);
		}
		mPoints[0].setImageResource(R.drawable.circle_orange);

		if (mPictures.size() <= 1) {
			mLlPlacePointLayout.setVisibility(View.INVISIBLE);
		} else {
			mLlPlacePointLayout.setVisibility(View.VISIBLE);
		}
	}

	private void setViewData() {
		mTitleView.setTitle(R.string.picture_detail_title);
		mViewPager.setAdapter(new FullPhoneViewAdapter(mContext, mPictures));
		mViewPager.setCurrentItem(mCurrentPage);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.title_view_back_llayout:
			mPopupWindow.dismiss();
			break;
		default:
			break;
		}
	}

	@Override
	public void onPageScrollStateChanged(int state) {
		// ToDo
	}

	@Override
	public void onPageScrolled(int position, float percent, int pixel) {
		// ToDo
	}

	@Override
	public void onPageSelected(int position) {
		for (int i = 0, size = mPictures.size(); i < size; i++) {
			if (position == i) {
				mPoints[i].setImageResource(R.drawable.circle_orange);
			} else {
				mPoints[i].setImageResource(R.drawable.circle_white);
			}
		}
	}

}
