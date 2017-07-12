package com.xiangxun.xacity.view;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiangxun.xacity.R;
import com.xiangxun.xacity.bean.LoginData.Children;
import com.xiangxun.xacity.bean.LoginData.ChildrenRoot;
import com.xiangxun.xacity.ui.MaterielManageActivity;
import com.xiangxun.xacity.ui.materiel.ProjectManageListActivity;
import com.xiangxun.xacity.ui.materiel.PurchaseManageListActivity;
import com.xiangxun.xacity.ui.materiel.RequirementManageListActivity;
import com.xiangxun.xacity.ui.materiel.SupplierManageListActivity;
import com.xiangxun.xacity.utils.ShareDataUtils;

/**
 * @package: com.xiangxun.xacity.view
 * @ClassName: MaterielMenuView.java
 * @Description: 物料管理侧滑菜单
 * @author: HanGJ
 * @date: 2016-2-2 上午11:18:00
 */
public class MaterielMenuView implements OnClickListener {
	private MaterielManageActivity mContext;
	private View mMenuView;
	private TextView tv_device_menu01;//
	private TextView tv_device_menu02;//
	private TextView tv_device_menu03;//
	private TextView tv_device_menu04;//
	private LinearLayout ll_device_menu01;//
	private LinearLayout ll_device_menu02;//
	private LinearLayout ll_device_menu03;//
	private LinearLayout ll_device_menu04;//
	/************* 物料管理抽屉菜单 需求管理子控件 ****************/
	private TextView materielDemandPM;// 项目管理
	private TextView materielDemandPlan;// 需求计划
	/************* 物料管理抽屉菜单 采购管理子控件 ****************/
	private TextView materielDemanPurchases;// 需求单管理
	private TextView purchasesManage;// 采购单管理
	private TextView materielSupplierManage;// 外部供应商管理
	/************* 物料管理抽屉菜单 工程监管子控件 ****************/
	private TextView projectManage;// 项目管理
	private TextView supplierManage;// 供应商管理
	/************* 物料管理抽屉菜单 供应商管理子控件 ****************/
	private TextView supplierPurchaseManage;// 采购单管理
	private TextView supplierDeliverManage;// 发货单管理
	private TextView supplierStoreManage;// 库存管理
	private TextView supplierInStorage;// 入库管理
	private TextView supplierPutStorage;// 出库管理
	private DrawerLayout mDrawerLayout;
	private RelativeLayout left_drawer;

	public MaterielMenuView(MaterielManageActivity context, DrawerLayout mDrawerLayout, RelativeLayout left_drawer) {
		mContext = context;
		this.mDrawerLayout = mDrawerLayout;
		this.left_drawer = left_drawer;
		initView();
		initData();
		initListener();
	}

	public View getMenuView() {
		if (mMenuView == null) {
			initView();
			initData();
			initListener();
		}
		return mMenuView;
	}

	@SuppressLint("InflateParams")
	private void initView() {
		mMenuView = mContext.getLayoutInflater().inflate(R.layout.sliding_materiel_menu_layout, null);
		tv_device_menu01 = (TextView) mMenuView.findViewById(R.id.tv_device_menu01);//
		tv_device_menu02 = (TextView) mMenuView.findViewById(R.id.tv_device_menu02);//
		tv_device_menu03 = (TextView) mMenuView.findViewById(R.id.tv_device_menu03);//
		tv_device_menu04 = (TextView) mMenuView.findViewById(R.id.tv_device_menu04);//
		//
		ll_device_menu01 = (LinearLayout) mMenuView.findViewById(R.id.ll_device_menu01);
		ll_device_menu02 = (LinearLayout) mMenuView.findViewById(R.id.ll_device_menu02);
		ll_device_menu03 = (LinearLayout) mMenuView.findViewById(R.id.ll_device_menu03);
		ll_device_menu04 = (LinearLayout) mMenuView.findViewById(R.id.ll_device_menu04);
		materielDemandPM = (TextView) mMenuView.findViewById(R.id.materiel_demand_pm);// 项目管理
		materielDemandPlan = (TextView) mMenuView.findViewById(R.id.materiel_demand_plan);// 需求计划
		materielDemanPurchases = (TextView) mMenuView.findViewById(R.id.materiel_deman_purchases_manage);// 需求单管理
		purchasesManage = (TextView) mMenuView.findViewById(R.id.materiel_purchases_manage);// 采购单管理
		materielSupplierManage = (TextView) mMenuView.findViewById(R.id.materiel_supplier_manage);// 外部供应商管理
		projectManage = (TextView) mMenuView.findViewById(R.id.materiel_project_manage);// 项目管理
		supplierManage = (TextView) mMenuView.findViewById(R.id.supplier_manage);// 供应商管理
		supplierPurchaseManage = (TextView) mMenuView.findViewById(R.id.supplier_purchase_manage);// 采购单管理
		supplierDeliverManage = (TextView) mMenuView.findViewById(R.id.supplier_deliver_manage);// 发货单管理
		supplierStoreManage = (TextView) mMenuView.findViewById(R.id.supplier_store_manage);// 库存管理
		supplierInStorage = (TextView) mMenuView.findViewById(R.id.supplier_in_storage_manage);// 入库管理
		supplierPutStorage = (TextView) mMenuView.findViewById(R.id.supplier_put_storage_manage);// 出库管理
	}

	private void initListener() {
		materielDemandPM.setOnClickListener(this);// 项目管理
		materielDemandPlan.setOnClickListener(this);// 需求计划
		materielDemanPurchases.setOnClickListener(this);// 需求单管理
		purchasesManage.setOnClickListener(this);// 采购单管理
		materielSupplierManage.setOnClickListener(this);// 外部供应商管理
		projectManage.setOnClickListener(this);// 项目管理
		supplierManage.setOnClickListener(this);// 供应商管理
		supplierPurchaseManage.setOnClickListener(this);// 采购单管理
		supplierDeliverManage.setOnClickListener(this);// 发货单管理
		supplierStoreManage.setOnClickListener(this);// 库存管理
		supplierInStorage.setOnClickListener(this);// 入库管理
		supplierPutStorage.setOnClickListener(this);// 出库管理
	}

	/**
	 * 注释该怎么写呢，需求文档没提动态控制,侧滑菜单动态控制(开发之前没说这事，我以为是静态的)
	 * 
	 * @Description: 如果你要改这个地方, 请慎重
	 */
	private void initData() {
		List<ChildrenRoot> childrenRoots = new ArrayList<ChildrenRoot>();
		Object obj = ShareDataUtils.getObject(mContext, "menu_materiel");
		childrenRoots.addAll((List) obj);
		if (childrenRoots.size() > 0) {
			for (int i = 0; i < childrenRoots.size(); i++) {
				ChildrenRoot childrenRoot = childrenRoots.get(i);
				if (childrenRoot != null && "需求管理".equals(childrenRoot.getName())) {
					tv_device_menu01.setVisibility(View.VISIBLE);
					ll_device_menu01.setVisibility(View.VISIBLE);
					List<Children> childrens = childrenRoot.getChildren();
					if (childrens != null && childrens.size() > 0) {
						for (int j = 0; j < childrens.size(); j++) {
							Children children = childrens.get(j);
							if ("项目管理".equals(children.getName())) {
								materielDemandPM.setVisibility(View.VISIBLE);
							} else if ("需求计划".equals(children.getName())) {
								materielDemandPlan.setVisibility(View.VISIBLE);
							}
						}
					}
				} else if (childrenRoot != null && "采购管理".equals(childrenRoot.getName())) {
					tv_device_menu02.setVisibility(View.VISIBLE);
					ll_device_menu02.setVisibility(View.VISIBLE);
					List<Children> childrens = childrenRoot.getChildren();
					if (childrens != null && childrens.size() > 0) {
						for (int j = 0; j < childrens.size(); j++) {
							Children children = childrens.get(j);
							if ("需求单管理".equals(children.getName())) {
								materielDemanPurchases.setVisibility(View.VISIBLE);
							} else if ("采购单管理".equals(children.getName())) {
								purchasesManage.setVisibility(View.VISIBLE);
							} else if ("外部供应商管理".equals(children.getName())) {
								materielSupplierManage.setVisibility(View.VISIBLE);
							}
						}
					}
				} else if (childrenRoot != null && "工程监管".equals(childrenRoot.getName())) {
					tv_device_menu03.setVisibility(View.VISIBLE);
					ll_device_menu03.setVisibility(View.VISIBLE);
					List<Children> childrens = childrenRoot.getChildren();
					if (childrens != null && childrens.size() > 0) {
						for (int j = 0; j < childrens.size(); j++) {
							Children children = childrens.get(j);
							if ("项目管理".equals(children.getName())) {
								projectManage.setVisibility(View.VISIBLE);
							} else if ("供应商监管".equals(children.getName())) {
								supplierManage.setVisibility(View.VISIBLE);
							}
						}
					}
				} else if (childrenRoot != null && "供应管理".equals(childrenRoot.getName())) {
					tv_device_menu04.setVisibility(View.VISIBLE);
					ll_device_menu04.setVisibility(View.VISIBLE);
					List<Children> childrens = childrenRoot.getChildren();
					if (childrens != null && childrens.size() > 0) {
						for (int j = 0; j < childrens.size(); j++) {
							Children children = childrens.get(j);
							if ("采购单管理".equals(children.getName())) {
								supplierPurchaseManage.setVisibility(View.VISIBLE);
							} else if ("发货单管理".equals(children.getName())) {
								supplierDeliverManage.setVisibility(View.VISIBLE);
							} else if ("库存管理".equals(children.getName())) {
								supplierStoreManage.setVisibility(View.VISIBLE);
							} else if ("入库管理".equals(children.getName())) {
								supplierInStorage.setVisibility(View.VISIBLE);
							} else if ("出库管理".equals(children.getName())) {
								supplierPutStorage.setVisibility(View.VISIBLE);
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.materiel_demand_pm:// 项目管理
			intent.putExtra("title", "项目管理");
			intent.putExtra("childAt", 0);
			intent.setClass(mContext, RequirementManageListActivity.class);
			break;
		case R.id.materiel_demand_plan:// 需求计划
			intent.putExtra("title", "需求计划");
			intent.putExtra("childAt", 1);
			intent.setClass(mContext, RequirementManageListActivity.class);
			break;
		case R.id.materiel_deman_purchases_manage:// 需求单管理
			intent.putExtra("title", "需求单管理");
			intent.putExtra("childAt", 0);
			intent.setClass(mContext, PurchaseManageListActivity.class);
			break;
		case R.id.materiel_purchases_manage:// 采购单管理
			intent.putExtra("title", "采购单管理");
			intent.putExtra("childAt", 1);
			intent.setClass(mContext, PurchaseManageListActivity.class);
			break;
		case R.id.materiel_supplier_manage:// 外部供应商管理
			intent.putExtra("title", "外部供应商管理");
			intent.putExtra("childAt", 2);
			intent.setClass(mContext, PurchaseManageListActivity.class);
			break;
		case R.id.materiel_project_manage:// 项目管理
			intent.putExtra("title", "项目管理");
			intent.putExtra("childAt", 0);
			intent.setClass(mContext, ProjectManageListActivity.class);
			break;
		case R.id.supplier_manage:// 供应商管理
			intent.putExtra("title", "供应商管理");
			intent.putExtra("childAt", 1);
			intent.putExtra("isFlag", 1);
			intent.setClass(mContext, ProjectManageListActivity.class);
			break;
		case R.id.supplier_purchase_manage:// 采购单管理
			intent.putExtra("title", "采购单管理");
			intent.putExtra("childAt", 0);
			intent.setClass(mContext, SupplierManageListActivity.class);
			break;
		case R.id.supplier_deliver_manage:// 发货单管理
			intent.putExtra("title", "发货单管理");
			intent.putExtra("childAt", 1);
			intent.setClass(mContext, SupplierManageListActivity.class);
			break;
		case R.id.supplier_store_manage:// 库存管理
			intent.putExtra("title", "库存管理");
			intent.putExtra("childAt", 2);
			intent.setClass(mContext, SupplierManageListActivity.class);
			break;
		case R.id.supplier_in_storage_manage:// 入库管理
			intent.putExtra("title", "入库管理");
			intent.putExtra("childAt", 3);
			intent.setClass(mContext, SupplierManageListActivity.class);
			break;
		case R.id.supplier_put_storage_manage:// 出库管理
			intent.putExtra("title", "出库管理");
			intent.putExtra("childAt", 4);
			intent.setClass(mContext, SupplierManageListActivity.class);
			break;
		}
		mDrawerLayout.closeDrawer(left_drawer);
		((MaterielManageActivity) mContext).startActivity(intent);
	}
}
