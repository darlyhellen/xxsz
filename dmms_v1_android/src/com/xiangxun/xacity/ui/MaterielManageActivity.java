package com.xiangxun.xacity.ui;

import java.util.ArrayList;
import java.util.List;

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
import com.xiangxun.xacity.app.BaseActivity;
import com.xiangxun.xacity.bean.LoginData.Children;
import com.xiangxun.xacity.bean.LoginData.ChildrenRoot;
import com.xiangxun.xacity.bean.ResultBeans.MaterielHome;
import com.xiangxun.xacity.common.ConstantStatus;
import com.xiangxun.xacity.request.DcNetWorkUtils;
import com.xiangxun.xacity.ui.materiel.PurchaseManageListActivity;
import com.xiangxun.xacity.ui.materiel.RequirementManageListActivity;
import com.xiangxun.xacity.ui.materiel.SupplierManageListActivity;
import com.xiangxun.xacity.utils.ShareDataUtils;
import com.xiangxun.xacity.utils.Tools;
import com.xiangxun.xacity.view.LoadDialog;
import com.xiangxun.xacity.view.MaterielMenuView;
import com.xiangxun.xacity.view.MsgToast;
import com.xiangxun.xacity.view.TitleView;

/**
 * @package: com.xiangxun.xacity.ui
 * @ClassName: MaterielManageActivity.java
 * @Description: 物料管理
 * @author: HanGJ
 * @date: 2015-12-30 下午5:20:07
 */
public class MaterielManageActivity extends BaseActivity implements OnClickListener {
	private TitleView titleView;
	private MaterielMenuView materielMenuView;
	/** DrawerLayout */
	private DrawerLayout mDrawerLayout;
	private RelativeLayout left_drawer;
	private LoadDialog loadDialog;
	/** 菜单打开/关闭状态 */
	private boolean isDirection_left = false;
	private LinearLayout ll_materials_purchases;
	private LinearLayout ll_materials_requirement;
	private LinearLayout ll_materials_repair;
	private LinearLayout ll_materials_stocks;
	/************* 物料管理首页 需求数量控件 ****************/
	private TextView mDemandNum;
	private TextView mDemandInfoNum;
	private TextView mPurchaseInfoNum;
	private TextView mStocksNum;
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			loadDialog.dismiss();
			switch (msg.what) {
			case ConstantStatus.getMaterialListSuccess:
				MaterielHome materielHome = (MaterielHome) msg.obj;
				if(materielHome != null){
					mDemandNum.setText(Html.fromHtml(String.format(getString(R.string.trade_n), Tools.isEmpty(materielHome.plansum))));
					mDemandInfoNum.setText(Html.fromHtml(String.format(getString(R.string.trade_n), Tools.isEmpty(materielHome.formsum))));
					mPurchaseInfoNum.setText(Html.fromHtml(String.format(getString(R.string.trade_n), Tools.isEmpty(materielHome.purchasesum))));
					mStocksNum.setText(Html.fromHtml(String.format(getString(R.string.trade_n), Tools.isEmpty(materielHome.suppliersum))));
				}
				break;
			case ConstantStatus.getMaterialListFailed:
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
		setContentView(R.layout.materiel_manage_home_layout);
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
		mDemandNum = (TextView) findViewById(R.id.materials_purchases_num);
		mDemandInfoNum = (TextView) findViewById(R.id.materials_scrap_num);
		mPurchaseInfoNum = (TextView) findViewById(R.id.materials_repair_num);
		mStocksNum = (TextView) findViewById(R.id.materials_stocks_num);
		ll_materials_purchases = (LinearLayout) findViewById(R.id.ll_materials_purchases);
		ll_materials_requirement = (LinearLayout) findViewById(R.id.ll_materials_requirement);
		ll_materials_repair = (LinearLayout) findViewById(R.id.ll_materials_repair);
		ll_materials_stocks = (LinearLayout) findViewById(R.id.ll_materials_stocks);
		materielMenuView = new MaterielMenuView(this, mDrawerLayout, left_drawer);

		left_drawer.removeAllViews();
		left_drawer.addView(materielMenuView.getMenuView());
	}

	@Override
	public void initData() {
		// 设置抽屉打开时，主要内容区被自定义阴影覆盖
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		titleView.setTitle("物资管理");
		titleView.setRightBackgroundResource(R.drawable.back);
		mDrawerLayout.setDrawerListener(new DrawerLayoutStateListener());
		loadDialog = new LoadDialog(this);
		loadDialog.setTitle("正在加载数据,请稍后...");
		getMenuId();
		RequestData();
	}

	private void RequestData() {
		loadDialog.show();
		DcNetWorkUtils.getMaterielHome(this, handler, account, password, menuid);
	}
	
	private String menuid = "";
	@SuppressWarnings({ "unchecked", "rawtypes" } )
	private void getMenuId() {
		List<ChildrenRoot> children = new ArrayList<ChildrenRoot>();
		Object obj = ShareDataUtils.getObject(this, "menu_materiel");
		children.addAll((List) obj);
		if (children.size() > 0) {
			for (int i = 0; i < children.size(); i++) {
				ChildrenRoot childrenRoot = children.get(i);
				if (childrenRoot != null) {
					List<Children> childrens = childrenRoot.getChildren();
					if (childrens != null && childrens.size() > 0) {
						for (int j = 0; j < childrens.size(); j++) {
							Children children2 = childrens.get(j);
							if (children2 != null) {
								menuid = children2.getId();
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void initListener() {
		ll_materials_purchases.setOnClickListener(this);
		ll_materials_requirement.setOnClickListener(this);
		ll_materials_repair.setOnClickListener(this);
		ll_materials_stocks.setOnClickListener(this);
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
		case R.id.ll_materials_purchases:
			intent.putExtra("title", "需求计划");
			intent.putExtra("childAt", 1);
			intent.setClass(this, RequirementManageListActivity.class);
			break;
		case R.id.ll_materials_requirement:
			intent.putExtra("title", "需求单管理");
			intent.putExtra("childAt", 0);
			intent.setClass(this, PurchaseManageListActivity.class);
			break;
		case R.id.ll_materials_repair:
			intent.putExtra("title", "采购单管理");
			intent.putExtra("childAt", 1);
			intent.setClass(this, PurchaseManageListActivity.class);
			break;
		case R.id.ll_materials_stocks:
			intent.putExtra("title", "库存管理");
			intent.putExtra("childAt", 2);
			intent.setClass(this, SupplierManageListActivity.class);
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
