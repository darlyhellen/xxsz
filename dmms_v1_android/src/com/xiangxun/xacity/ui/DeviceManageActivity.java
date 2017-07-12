package com.xiangxun.xacity.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiangxun.xacity.R;
import com.xiangxun.xacity.app.BaseFragmentActivity;
import com.xiangxun.xacity.bean.ResultBeans.DeviceHome;
import com.xiangxun.xacity.common.ConstantStatus;
import com.xiangxun.xacity.request.DcNetWorkUtils;
import com.xiangxun.xacity.ui.device.DeviceMaintainListActivity;
import com.xiangxun.xacity.ui.device.DeviceManageListActivity;
import com.xiangxun.xacity.ui.device.DevicePurchaseActivity;
import com.xiangxun.xacity.ui.device.DeviceScrapListActivity;
import com.xiangxun.xacity.utils.Tools;
import com.xiangxun.xacity.view.DeviceMenuView;
import com.xiangxun.xacity.view.LoadDialog;
import com.xiangxun.xacity.view.MsgToast;
import com.xiangxun.xacity.view.TitleView;

/**
 * @package: com.xiangxun.xacity.ui
 * @ClassName: DeviceManageActivity.java
 * @Description: 设备管理
 * @author: HanGJ
 * @date: 2015-12-30 下午5:20:18
 */
@SuppressLint("NewApi")
public class DeviceManageActivity extends BaseFragmentActivity implements OnClickListener {
	private TitleView titleView;
	private DeviceMenuView deviceMenuView;
	/** DrawerLayout */
	private DrawerLayout mDrawerLayout;
	private RelativeLayout left_drawer;
	private LoadDialog loadDialog;
	/** 菜单打开/关闭状态 */
	private boolean isDirection_left = false;
	private LinearLayout ll_device_purchase;
	private LinearLayout ll_device_scrap;
	private LinearLayout ll_device_maintain_plan;
	private LinearLayout ll_device_accid;
	private LinearLayout ll_device_insurance;
	/************* 设备管理首页 需求数量控件 ****************/
	private TextView mPurchasesNum;
	private TextView mScrapNum;
	private TextView mRepairNum;
	private TextView mAccidentNum;
	private TextView mInsuranceNum;
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			loadDialog.dismiss();
			switch (msg.what) {
			case ConstantStatus.DeviceHomeSuccess:
				DeviceHome deviceHome = (DeviceHome) msg.obj;
				if (deviceHome != null) {
					mPurchasesNum.setText(Html.fromHtml(String.format(getString(R.string.trade_n), Tools.isEmpty(deviceHome.purchasesum))));
					mScrapNum.setText(Html.fromHtml(String.format(getString(R.string.trade_n), Tools.isEmpty(deviceHome.scrapsum))));
					mRepairNum.setText(Html.fromHtml(String.format(getString(R.string.trade_n), Tools.isEmpty(deviceHome.maintainplansum))));
					mAccidentNum.setText(Html.fromHtml(String.format(getString(R.string.trade_n), Tools.isEmpty(deviceHome.accidentsum))));
					mInsuranceNum.setText(Html.fromHtml(String.format(getString(R.string.trade_n), Tools.isEmpty(deviceHome.insuresum))));
				}
				break;
			case ConstantStatus.DeviceHomeFailed:
				MsgToast.geToast().setMsg("统计数据加载失败~");
				break;
			case ConstantStatus.NetWorkError:
				MsgToast.geToast().setMsg("网络异常,请稍后重试!");
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.device_manage_home_layout);
		initView();
		initData();
		initListener();
	}

	@Override
	@SuppressLint("InflateParams")
	public void initView() {
		titleView = (TitleView) findViewById(R.id.comment_title);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		left_drawer = (RelativeLayout) findViewById(R.id.left_drawer);
		mPurchasesNum = (TextView) findViewById(R.id.device_purchases_num);
		mScrapNum = (TextView) findViewById(R.id.device_scrap_num);
		mRepairNum = (TextView) findViewById(R.id.device_repair_num);
		mAccidentNum = (TextView) findViewById(R.id.device_accidents_num);
		mInsuranceNum = (TextView) findViewById(R.id.device_insurance_num);
		ll_device_purchase = (LinearLayout) findViewById(R.id.ll_device_purchase);
		ll_device_scrap = (LinearLayout) findViewById(R.id.ll_device_scrap);
		ll_device_maintain_plan = (LinearLayout) findViewById(R.id.ll_device_maintain_plan);
		ll_device_accid = (LinearLayout) findViewById(R.id.ll_device_accid);
		ll_device_insurance = (LinearLayout) findViewById(R.id.ll_device_insurance);
		deviceMenuView = new DeviceMenuView(this, mDrawerLayout, left_drawer);
		left_drawer.removeAllViews();
		left_drawer.addView(deviceMenuView.getMenuView());
	}

	@Override
	public void initData() {
		// 设置抽屉打开时，主要内容区被自定义阴影覆盖
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		titleView.setTitle("设备管理");
		titleView.setRightBackgroundResource(R.drawable.back);
		mDrawerLayout.setDrawerListener(new DrawerLayoutStateListener());
		loadDialog = new LoadDialog(this);
		loadDialog.setTitle("正在加载数据,请稍后...");
		RequestData();
	}

	private void RequestData() {
		loadDialog.show();
		DcNetWorkUtils.getDevicelHome(this, handler, account, password);
	}

	@Override
	public void initListener() {
		ll_device_purchase.setOnClickListener(this);
		ll_device_scrap.setOnClickListener(this);
		ll_device_maintain_plan.setOnClickListener(this);
		ll_device_accid.setOnClickListener(this);
		ll_device_insurance.setOnClickListener(this);
		titleView.setLeftBackOneListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		titleView.setRightOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!isDirection_left) { // 左边栏菜单关闭时，打开
					mDrawerLayout.openDrawer(left_drawer);
				} else {// 左边栏菜单打开时，关闭
					mDrawerLayout.closeDrawer(left_drawer);
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.ll_device_purchase:
			intent.putExtra("title", "采购租赁申请");
			intent.putExtra("childAt", 1);
			intent.setClass(this, DevicePurchaseActivity.class);
			break;
		case R.id.ll_device_scrap:
			intent.putExtra("title", "报废申请");
			intent.setClass(this, DeviceScrapListActivity.class);
			break;
		case R.id.ll_device_maintain_plan:
			intent.putExtra("title", "设备维修计划");
			intent.putExtra("childAt", 0);
			intent.setClass(this, DeviceMaintainListActivity.class);
			break;
		case R.id.ll_device_accid:
			intent.putExtra("title", "事故报告记录");
			intent.putExtra("childAt", 3);
			intent.setClass(this, DeviceMaintainListActivity.class);
			break;
		case R.id.ll_device_insurance:
			intent.putExtra("title", "保险管理");
			intent.putExtra("childAt", 9);
			intent.setClass(this, DeviceManageListActivity.class);
			break;
		}
		startActivity(intent);
	}

	/**
	 * DrawerLayout状态变化监听
	 */
	private class DrawerLayoutStateListener extends DrawerLayout.SimpleDrawerListener {
		@Override
		public void onDrawerSlide(View drawerView, float slideOffset) {
		}

		@Override
		public void onDrawerOpened(android.view.View drawerView) {
			titleView.setRightBackgroundResource(R.drawable.titleview_x);
			isDirection_left = true;
		}

		@Override
		public void onDrawerClosed(android.view.View drawerView) {
			titleView.setRightBackgroundResource(R.drawable.back);
			isDirection_left = false;
		}
	}

}
