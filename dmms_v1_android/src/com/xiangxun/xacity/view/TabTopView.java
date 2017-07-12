package com.xiangxun.xacity.view;

import android.app.Activity;
import android.content.Context;
import android.text.Spanned;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiangxun.xacity.R;

/**
 * @package: com.xiangxun.xacity.view
 * @ClassName: TabTopView.java
 * @Description: 头部 
 * @author: HanGJ
 * @date: 2016-2-25 下午1:51:53
 */
public class TabTopView extends FrameLayout implements OnClickListener {
	private TextView mTabtopviewLeftText, mRabtopviewRightText, mTabtopviewMiddleText;
	private View mTabtopviewLeftLine, mTabtopviewMiddleLine, mTabtopviewRightLine, mTabtopviewAnimLine;
	private RelativeLayout mToptabviewLeftRlyout;
	private RelativeLayout mToptabviewMiddleRlyout;
	private RelativeLayout mToptabviewRightRlyout;
	private LinearLayout mToptabviewMiddleLlyout;
	public LinearLayout mLlTopIndex;
	private onTabToListener mTabToListener;
	private Context mContext;
	private int currIndex;
	private TranslateAnimation animation;

	public TabTopView(Context context) {
		super(context);
	}

	public void setTabToListener(onTabToListener tabToListener) {
		getmToptabviewLeftRlyout().setOnClickListener(this);
		getmToptabviewRightRlyout().setOnClickListener(this);
		getmToptabviewMiddleLlyout().setOnClickListener(this);
		mTabToListener = tabToListener;
	}

	public TabTopView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.tabtopview, this, true);
		mTabtopviewLeftText = (TextView) findViewById(R.id.tabtopview_left_text);
		mRabtopviewRightText = (TextView) findViewById(R.id.tabtopview_right_text);
		mTabtopviewMiddleText = (TextView) findViewById(R.id.tabtopview_middle_text);
		mTabtopviewLeftLine = (View) findViewById(R.id.tabtopview_left_line);
		mTabtopviewMiddleLine = (View) findViewById(R.id.tabtopview_middle_line);
		mTabtopviewRightLine = (View) findViewById(R.id.tabtopview_right_line);
		mTabtopviewAnimLine = (View) findViewById(R.id.tabtopview_anim_line);
		setmToptabviewRightRlyout((RelativeLayout) findViewById(R.id.toptabview_right_rlyout));
		setmToptabviewMiddleRlyout((RelativeLayout) findViewById(R.id.toptabview_middle_rlyout));
		setmToptabviewLeftRlyout((RelativeLayout) findViewById(R.id.toptabview_left_rlyout));
		setmToptabviewMiddleLlyout((LinearLayout) findViewById(R.id.toptabview_middle_llyout));
		mLlTopIndex = (LinearLayout) findViewById(R.id.ll_top_index);
	}

	public void OnClickLeftTab() {
		mTabtopviewLeftText.setTextColor(this.getResources().getColor(R.color.citylist_item_textcolor_frist));
		mTabtopviewLeftLine.setVisibility(View.VISIBLE);
		mTabtopviewMiddleText.setTextColor(this.getResources().getColor(R.color.color333333));
		mTabtopviewMiddleLine.setVisibility(View.INVISIBLE);
		mRabtopviewRightText.setTextColor(this.getResources().getColor(R.color.color333333));
		mTabtopviewRightLine.setVisibility(View.INVISIBLE);
	}
	
	public void OnClickMiddleTab() {
		mTabtopviewLeftText.setTextColor(this.getResources().getColor(R.color.color333333));
		mTabtopviewLeftLine.setVisibility(View.INVISIBLE);
		mTabtopviewMiddleText.setTextColor(this.getResources().getColor(R.color.citylist_item_textcolor_frist));
		mTabtopviewMiddleLine.setVisibility(View.VISIBLE);
		mRabtopviewRightText.setTextColor(this.getResources().getColor(R.color.color333333));
		mTabtopviewRightLine.setVisibility(View.INVISIBLE);
	}

	public void OnClickRightTab() {
		mTabtopviewLeftText.setTextColor(this.getResources().getColor(R.color.color333333));
		mTabtopviewLeftLine.setVisibility(View.INVISIBLE);
		mTabtopviewMiddleText.setTextColor(this.getResources().getColor(R.color.color333333));
		mTabtopviewMiddleLine.setVisibility(View.INVISIBLE);
		mRabtopviewRightText.setTextColor(this.getResources().getColor(R.color.citylist_item_textcolor_frist));
		mTabtopviewRightLine.setVisibility(View.VISIBLE);
	}

	public void setTabText(String lefttext, String righttext) {
		mTabtopviewLeftText.setText(lefttext);
		mRabtopviewRightText.setText(righttext);
	}

	public void setTabMiddleText(String text) {
//		initAnimView();
		mTabtopviewMiddleText.setText(text);
		getmToptabviewMiddleLlyout().setVisibility(View.VISIBLE);
	}

	public void setTabMiddleText(Spanned text) {
		initAnimView();
		mTabtopviewMiddleText.setText(text);
		getmToptabviewMiddleLlyout().setVisibility(View.VISIBLE);
	}

	public void setTabText(int lefttext, int righttext) {
		mTabtopviewLeftText.setText(lefttext);
		mRabtopviewRightText.setText(righttext);
	}

	public void OnClickLeftTabOnClickListener(OnClickListener listener) {
		getmToptabviewLeftRlyout().setOnClickListener(listener);
	}
	
	public void OnClickMiddleTabOnClickListener(OnClickListener listener) {
		getmToptabviewMiddleRlyout().setOnClickListener(listener);
	}

	public void OnClickRightTabOnClickListener(OnClickListener listener) {
		getmToptabviewRightRlyout().setOnClickListener(listener);
	}

	public interface onTabToListener {
		public void onLeftListener();

		public void onMiddleListener();

		public void onRightListener();

		public void onClickAllListener(int index);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.toptabview_left_rlyout:
			if (currIndex == 0) {
				return;
			}
			mTabToListener.onLeftListener();
			if (currIndex == 1) {
				animation = new TranslateAnimation(one, 0, 0, 0);
			} else if (currIndex == 2) {
				animation = new TranslateAnimation(two, 0, 0, 0);
			}
			currIndex = 0;
			break;
		case R.id.toptabview_middle_llyout:
			if (currIndex == 1) {
				return;
			}
			mTabToListener.onMiddleListener();
			if (currIndex == 0) {
				animation = new TranslateAnimation(offset, one, 0, 0);
			} else if (currIndex == 2) {
				animation = new TranslateAnimation(two, one, 0, 0);
			}
			currIndex = 1;
			break;
		case R.id.toptabview_right_rlyout:
			if (currIndex == 2) {
				return;
			}
			mTabToListener.onRightListener();
			if (currIndex == 0) {
				animation = new TranslateAnimation(offset, two, 0, 0);
			} else if (currIndex == 1) {
				animation = new TranslateAnimation(one, two, 0, 0);
			}
			currIndex = 2;
			break;

		default:
			break;
		}
		animation.setFillAfter(true);// True:图片停在动画结束位置
		animation.setDuration(150);
		mTabtopviewAnimLine.startAnimation(animation);
		mTabToListener.onClickAllListener(currIndex);
	}

	private int offset = 0;// 动画图片偏移量
	private int bmpW;// 动画图片宽度
	int one;// 页卡1 -> 页卡2 偏移量
	int two;// 页卡1 -> 页卡3 偏移量

	private void initAnimView() {
		mLlTopIndex.setVisibility(View.VISIBLE);
		// cursor = (ImageView) findViewById(R.id.cursor);
		bmpW = mTabtopviewAnimLine.getWidth();// 获取图片宽度
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;// 获取分辨率宽度
		offset = (screenW / 3 - bmpW) / 2;// 计算偏移量
		one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
		two = one * 2;// 页卡1 -> 页卡3 偏移量
		// Matrix matrix = new Matrix();
		// matrix.postTranslate(offset, 0);
		// tabtopview_left_line.setImageMatrix(matrix);// 设置动画初始位置
	}

	public RelativeLayout getmToptabviewLeftRlyout() {
		return mToptabviewLeftRlyout;
	}
	
	public RelativeLayout getmToptabviewMiddleRlyout() {
		return mToptabviewMiddleRlyout;
	}

	public void setmToptabviewLeftRlyout(RelativeLayout mToptabviewLeftRlyout) {
		this.mToptabviewLeftRlyout = mToptabviewLeftRlyout;
	}
	
	public void setmToptabviewMiddleRlyout(RelativeLayout mToptabviewMiddleRlyout) {
		this.mToptabviewMiddleRlyout = mToptabviewMiddleRlyout;
	}

	public LinearLayout getmToptabviewMiddleLlyout() {
		return mToptabviewMiddleLlyout;
	}

	public void setmToptabviewMiddleLlyout(LinearLayout mToptabviewMiddleLlyout) {
		this.mToptabviewMiddleLlyout = mToptabviewMiddleLlyout;
	}

	public RelativeLayout getmToptabviewRightRlyout() {
		return mToptabviewRightRlyout;
	}

	public void setmToptabviewRightRlyout(RelativeLayout mToptabviewRightRlyout) {
		this.mToptabviewRightRlyout = mToptabviewRightRlyout;
	}
}
