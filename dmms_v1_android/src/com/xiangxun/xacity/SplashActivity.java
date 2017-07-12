package com.xiangxun.xacity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.xiangxun.xacity.app.BaseActivity;

public class SplashActivity extends BaseActivity {
	boolean isFirstIn = false;
	private static final int GO_HOME = 1000;
	private static final long SPLASH_DELAY_MILLIS = 2500;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GO_HOME:
				Intent intent = null;
				if (!isFirstIn) {
					intent = new Intent(SplashActivity.this, LoginActivity.class);
				} else {
					intent = new Intent(SplashActivity.this, MainActivity.class);
				}
				startActivity(intent);
				finish();
				break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome_splash);
		initView();
		initData();
		initListener();
	}

	@Override
	public void initView() {
	}

	@Override
	public void initData() {
//		isFirstIn = XiangXunApplication.getInstance().getIsFirstIn();
		if (mHandler != null) {
			mHandler.sendEmptyMessageDelayed(GO_HOME, SPLASH_DELAY_MILLIS);
		}
	}

	@Override
	public void initListener() {
	}

}
