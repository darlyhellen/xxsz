package com.xiangxun.xacity.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.xiangxun.xacity.R;
import com.xiangxun.xacity.common.ActivityBusiness;
import com.xiangxun.xacity.utils.ShareDataUtils;

/**
 * @package: com.xiangxun.app
 * @ClassName: BaseActivity.java
 * @Description:
 * @author: HanGJ
 * @date: 2015-7-29 上午9:45:31
 */
public abstract class BaseActivity extends Activity {
	public String actLable = null;
	public String mTitleName = "TIELENAME";
	private long time = 0;
	// 两次事件的间隔时间
	private static final long CLICK_INTER_TIME = 800;
	public String account;
	public String password;

	// 加入activity栈
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityBusiness.instance().addBusiness(this);
		account = ShareDataUtils.getSharedStringData(this, "loginName");
		password = ShareDataUtils.getSharedStringData(this, "password");
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	// 从栈中除去
	protected void onDestroy() {
		super.onDestroy();
		ActivityBusiness.instance().removeBusiness(this);
	}

	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
	}

	public abstract void initView();

	public abstract void initData();

	public abstract void initListener();

	// 记录第一次事件发生时间
	public void recodeTime() {
		time = System.currentTimeMillis();
	}

	// 检查两次时间间隔时间，小于CLICK_INTER_TIME，则两次事件间隔太短，
	public boolean checkShortTime() {
		if (time > 0) {
			return (System.currentTimeMillis() - time) < CLICK_INTER_TIME;
		} else
			return false;
	}

	@Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
		int4Right();
	}

	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		super.startActivityForResult(intent, requestCode);
		int4Right();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		out2Left();
	}

	public void out2Left() {
		// overridePendingTransition(R.anim.new_dync_no,
		// R.anim.new_dync_out_to_left);
		overridePendingTransition(R.anim.activity_nothing, R.anim.activity_out_to_buttom);
	}

	public void int4Right() {
		// overridePendingTransition(R.anim.new_dync_in_from_right,
		// R.anim.new_dync_no);
		overridePendingTransition(R.anim.right_in, R.anim.activity_nothing);
	}

}
