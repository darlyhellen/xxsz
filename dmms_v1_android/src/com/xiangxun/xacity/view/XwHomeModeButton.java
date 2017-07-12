package com.xiangxun.xacity.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiangxun.xacity.R;

public class XwHomeModeButton extends RelativeLayout {
	private Context mContext;
	private TextView tv_mode_button, tv_hint;
	private RelativeLayout mRlModeButton;

	public XwHomeModeButton(Context context) {
		super(context);
		mContext = context;
		init();
	}

	public XwHomeModeButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}

	private void init() {
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.home_mode_button, this, true);

		tv_mode_button = (TextView) findViewById(R.id.tv_mode_button);
		tv_hint = (TextView) findViewById(R.id.tv_hint);
		mRlModeButton = (RelativeLayout) findViewById(R.id.rlayout_mode_button);
	}

	public void setHint(int num) {
		tv_hint.setVisibility(View.VISIBLE);
		tv_hint.setText("" + num);
	}

	public void reset() {
		tv_hint.setText("");
		tv_hint.setVisibility(View.INVISIBLE);
	}

	@SuppressLint("NewApi")
	public void setIV(int i, String j) {
		tv_mode_button.setText(j);
		tv_mode_button.setCompoundDrawablesRelativeWithIntrinsicBounds(null, mContext.getResources().getDrawable(i), null, null);
	}

	// public void setOnClickListener(OnClickListener l) {
	// mRlModeButton.setOnClickListener(l);
	// }
}
