package com.xiangxun.xacity.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.xiangxun.xacity.R;

/**
 * @package: com.xiangxun.device.view
 * @ClassName: TitleView.java
 * @Description: 自定义title
 * @author: HanGJ
 * @date: 2015-10-12 上午8:10:10
 */
public class TitleView extends FrameLayout {
	// 中间title内容
	private TextView mTVTitle;
	// 返回
	private LinearLayout mBtnBack;
	private ImageView mBtnShare;
	private ImageView back_img;
	private ViewFlipper title_view_right_Flipper01;
	private TextView title_view_operation_text;

	public TitleView(Context context) {
		super(context);
	}

	public TitleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.title_view, this, true);
		mBtnBack = (LinearLayout) findViewById(R.id.title_view_back_llayout);
		mTVTitle = (TextView) findViewById(R.id.title_view_operation_text);
		mBtnShare = (ImageView) findViewById(R.id.xw_share);
		back_img = (ImageView) findViewById(R.id.title_view_back_img);
		title_view_right_Flipper01 = (ViewFlipper) findViewById(R.id.title_view_right_Flipper01);
		title_view_operation_text = (TextView) findViewById(R.id.title_view_text);
	}

	// 中间title
	public void setTitle(int i) {
		mTVTitle.setText(i);
	}

	public void setTitle(String str) {
		mTVTitle.setText(str);
	}

	/**
	 * 返回
	 * 
	 * @param listener
	 *            监听事件
	 */
	public void setLeftBackOneListener(OnClickListener listener) {
		mBtnBack.setVisibility(View.VISIBLE);
		mBtnBack.setOnClickListener(listener);
	}

	public void setRightOnClickListener(OnClickListener listener) {
		title_view_right_Flipper01.setVisibility(View.VISIBLE);
		mBtnShare.setOnClickListener(listener);
	}

	public void setRightBackgroundResource(int drawable) {
		title_view_right_Flipper01.setVisibility(View.VISIBLE);
		title_view_right_Flipper01.setDisplayedChild(0);
		mBtnShare.setBackgroundResource(drawable);
	}
	
	
	public void setLeftBackgroundResource(int drawable) {
		back_img.setBackgroundResource(drawable);
	}

	public void setRightImageTextFlipper(OnClickListener listener) {
		title_view_right_Flipper01.setVisibility(View.VISIBLE);
		title_view_right_Flipper01.setDisplayedChild(1);
		title_view_right_Flipper01.setOnClickListener(listener);
	}

	// 获取右边textView
	public TextView getTitleViewOperationText() {
		return title_view_operation_text;
	}

	// 获取右边textView颜色
	public void setTitleViewOperationTextColor(int color) {
		title_view_operation_text.setTextColor(this.getResources().getColor(color));
	}

}
