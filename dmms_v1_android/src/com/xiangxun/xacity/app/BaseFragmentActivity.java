package com.xiangxun.xacity.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.xiangxun.xacity.R;
import com.xiangxun.xacity.common.ActivityBusiness;
import com.xiangxun.xacity.utils.ShareDataUtils;

/**
 * @package: com.xiangxun.app
 * @ClassName: BaseFragmentActivity.java
 * @Description:
 * @author: HanGJ
 * @date: 2015-7-29 上午9:45:23
 */
public abstract class BaseFragmentActivity extends FragmentActivity {
	public String mTitleName = "TIELENAME";
	public String actLable = null;
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
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
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
		overridePendingTransition(R.anim.right_in, R.anim.activity_nothing);
	}

	@Override
	public void finish() {
		super.finish();
		out2Left();
	}

	public abstract void initView();

	public abstract void initData();

	public abstract void initListener();

}