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
import com.xiangxun.xacity.ui.DeviceManageActivity;
import com.xiangxun.xacity.ui.device.DeviceMaintainListActivity;
import com.xiangxun.xacity.ui.device.DeviceManageListActivity;
import com.xiangxun.xacity.ui.device.DevicePurchaseActivity;
import com.xiangxun.xacity.ui.device.DeviceScrapListActivity;
import com.xiangxun.xacity.ui.device.DeviceSpareOilActivity;
import com.xiangxun.xacity.ui.device.DeviceVehicleListActivity;
import com.xiangxun.xacity.utils.ShareDataUtils;

/**
 * @package: com.xiangxun.xacity.view
 * @ClassName: DeviceMenuView.java
 * @Description: 设备管理侧滑菜单
 * @author: HanGJ
 * @date: 2016-2-2 上午09:18:37
 */
public class DeviceMenuView implements OnClickListener {
	private DeviceManageActivity mContext;
	private View mMenuView;
	private TextView tv_device_menu01;//
	private TextView tv_device_menu02;//
	private TextView tv_device_menu03;//
	private TextView tv_device_menu04;//
	private TextView tv_device_menu05;//
	private LinearLayout ll_device_menu01;//
	private LinearLayout ll_device_menu02;//
	private LinearLayout ll_device_menu03;//
	private LinearLayout ll_device_menu04;//
	private LinearLayout ll_device_menu05;//
	/************* 设备管理抽屉菜单 设备采购子控件 ****************/
	private TextView qualifiedSupplier;// 合格供方名册
	private TextView purchaseLeaseApply;// 采购租赁申请
	private TextView equipmentAcceptance;// 设备验收
	private TextView rentEquipmentLedger;// 外租设备台账
	private TextView contractLedger;// 合同台账
	/************* 设备管理抽屉菜单 设备管理子控件 ****************/
	private TextView operatorLedger;// 操作人员台账
	private TextView deviceLedger;// 设备台账
	private TextView safetyProtection;// 安全保护装置
	private TextView deviceDispatch;// 设备派遣
	private TextView deviceUseRecord;// 使用记录
	private TextView deviceVerify;// 设备到场验证
	private TextView dailyInspectionRecord;// 日常检查记录
	private TextView utilizationRate;// 完好利用率
	private TextView monthlyRreport;// 重点设备月报表
	private TextView insuranceManage;// 保险管理
	/************* 设备管理抽屉菜单 设备维修子控件 ****************/
	private TextView maintenancePlan;// 设备维修计划
	private TextView maintenanceOrder;// 维修工单
	private TextView maintenanceRecord;// 维修记录
	private TextView accidentReportRecord;// 事故报告记录
	/************* 设备管理抽屉菜单 设备报废子控件 ****************/
	private TextView scrapApply;// 报废申请
	/************* 设备管理抽屉菜单 备件油料子控件 ****************/
	private TextView inLedgerRecords;// 入库台账记录
	private TextView outLedgerRecords;// 出库台账记录
	private TextView petrolManage;// 加油管理
	private TextView materialApply;// 领料申请
	private TextView carLocation;// 车辆定位
	private DrawerLayout mDrawerLayout;
	private RelativeLayout left_drawer;

	public DeviceMenuView(DeviceManageActivity context, DrawerLayout mDrawerLayout, RelativeLayout left_drawer) {
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
		mMenuView = mContext.getLayoutInflater().inflate(R.layout.sliding_menu_layout, null);
		tv_device_menu01 = (TextView) mMenuView.findViewById(R.id.tv_device_menu01);//
		tv_device_menu02 = (TextView) mMenuView.findViewById(R.id.tv_device_menu02);//
		tv_device_menu03 = (TextView) mMenuView.findViewById(R.id.tv_device_menu03);//
		tv_device_menu04 = (TextView) mMenuView.findViewById(R.id.tv_device_menu04);//
		tv_device_menu05 = (TextView) mMenuView.findViewById(R.id.tv_device_menu05);//
		//
		ll_device_menu01 = (LinearLayout) mMenuView.findViewById(R.id.ll_device_menu01);
		ll_device_menu02 = (LinearLayout) mMenuView.findViewById(R.id.ll_device_menu02);
		ll_device_menu03 = (LinearLayout) mMenuView.findViewById(R.id.ll_device_menu03);
		ll_device_menu04 = (LinearLayout) mMenuView.findViewById(R.id.ll_device_menu04);
		ll_device_menu05 = (LinearLayout) mMenuView.findViewById(R.id.ll_device_menu05);
		/************* 设备管理抽屉菜单 设备采购子控件 ****************/
		qualifiedSupplier = (TextView) mMenuView.findViewById(R.id.qualified_supplier_list);// 合格供方名册
		purchaseLeaseApply = (TextView) mMenuView.findViewById(R.id.purchase_lease_apply);// 采购租赁申请
		equipmentAcceptance = (TextView) mMenuView.findViewById(R.id.device_equipment_acceptance);// 设备验收
		rentEquipmentLedger = (TextView) mMenuView.findViewById(R.id.rent_equipment_ledger);// 外租设备台账
		contractLedger = (TextView) mMenuView.findViewById(R.id.device_contract_ledger);// 合同台账
		/************* 设备管理抽屉菜单 设备管理子控件 ****************/
		operatorLedger = (TextView) mMenuView.findViewById(R.id.device_operator_ledger);// 操作人员台账
		deviceLedger = (TextView) mMenuView.findViewById(R.id.device_ledger);// 设备台账
		safetyProtection = (TextView) mMenuView.findViewById(R.id.safety_protection_device);// 安全保护装置
		deviceDispatch = (TextView) mMenuView.findViewById(R.id.device_dispatch);// 设备派遣
		deviceUseRecord = (TextView) mMenuView.findViewById(R.id.device_use_record);// 使用记录
		deviceVerify = (TextView) mMenuView.findViewById(R.id.device_verify);// 设备到场验证
		dailyInspectionRecord = (TextView) mMenuView.findViewById(R.id.daily_inspection_record);// 日常检查记录
		utilizationRate = (TextView) mMenuView.findViewById(R.id.sound_utilization_rate);// 完好利用率
		monthlyRreport = (TextView) mMenuView.findViewById(R.id.Key_equipment_monthly_report);// 重点设备月报表
		insuranceManage = (TextView) mMenuView.findViewById(R.id.insurance_management);// 保险管理
		/************* 设备管理抽屉菜单 设备维修子控件 ****************/
		maintenancePlan = (TextView) mMenuView.findViewById(R.id.device_maintenance_plan);// 设备维修计划
		maintenanceOrder = (TextView) mMenuView.findViewById(R.id.device_maintenance_order);// 维修工单
		maintenanceRecord = (TextView) mMenuView.findViewById(R.id.device_maintenance_record);// 维修记录
		accidentReportRecord = (TextView) mMenuView.findViewById(R.id.Accident_report_record);// 事故报告记录
		/************* 设备管理抽屉菜单 设备报废子控件 ****************/
		scrapApply = (TextView) mMenuView.findViewById(R.id.device_scrap_apply);// 设备报废
		/************* 设备管理抽屉菜单 备件油料子控件 ****************/
		inLedgerRecords = (TextView) mMenuView.findViewById(R.id.in_ledger_records);// 入库台账记录
		outLedgerRecords = (TextView) mMenuView.findViewById(R.id.out_ledger_records);// 出库台账记录
		petrolManage = (TextView) mMenuView.findViewById(R.id.petrol_manage);// 加油管理
		materialApply = (TextView) mMenuView.findViewById(R.id.material_apply);// 领料申请
		carLocation = (TextView) mMenuView.findViewById(R.id.car_location);// 车辆定位
	}

	private void initListener() {
		qualifiedSupplier.setOnClickListener(this);// 合格供方名册
		purchaseLeaseApply.setOnClickListener(this);// 采购租赁申请
		equipmentAcceptance.setOnClickListener(this);// 设备验收
		rentEquipmentLedger.setOnClickListener(this);// 外租设备台账
		contractLedger.setOnClickListener(this);// 合同台账
		operatorLedger.setOnClickListener(this);// 操作人员台账
		deviceLedger.setOnClickListener(this);// 设备台账
		safetyProtection.setOnClickListener(this);// 安全保护装置
		deviceDispatch.setOnClickListener(this);// 设备派遣
		deviceUseRecord.setOnClickListener(this);// 使用记录
		deviceVerify.setOnClickListener(this);// 设备到场验证
		dailyInspectionRecord.setOnClickListener(this);// 日常检查记录
		utilizationRate.setOnClickListener(this);// 完好利用率
		monthlyRreport.setOnClickListener(this);// 重点设备月报表
		insuranceManage.setOnClickListener(this);// 保险管理
		maintenancePlan.setOnClickListener(this);// 设备维修计划
		maintenanceOrder.setOnClickListener(this);// 维修工单
		maintenanceRecord.setOnClickListener(this);// 维修记录
		accidentReportRecord.setOnClickListener(this);// 事故报告记录
		scrapApply.setOnClickListener(this);//
		inLedgerRecords.setOnClickListener(this);// 入库台账记录
		outLedgerRecords.setOnClickListener(this);// 出库台账记录
		petrolManage.setOnClickListener(this);// 加油管理
		materialApply.setOnClickListener(this);// 领料申请
		carLocation.setOnClickListener(this);// 车辆定位

	}

	/**
	 * 注释该怎么写呢，需求文档没提这个功能,侧滑菜单动态控制(开发之前没说这事，我以为是静态的)
	 *  @Description: 如果你要改这个地方, 请慎重
	 */
	private void initData() {
		List<ChildrenRoot> childrenRoots = new ArrayList<ChildrenRoot>();
		Object obj = ShareDataUtils.getObject(mContext, "menu_device");
		childrenRoots.addAll((List) obj);
		if (childrenRoots.size() > 0) {
			for (int i = 0; i < childrenRoots.size(); i++) {
				ChildrenRoot childrenRoot = childrenRoots.get(i);
				if (childrenRoot != null && "设备采购".equals(childrenRoot.getName())) {
					tv_device_menu01.setVisibility(View.VISIBLE);
					ll_device_menu01.setVisibility(View.VISIBLE);
					List<Children> childrens = childrenRoot.getChildren();
					if (childrens != null && childrens.size() > 0) {
						for (int j = 0; j < childrens.size(); j++) {
							Children children = childrens.get(j);
							if ("合格供方名册".equals(children.getName())) {
								qualifiedSupplier.setVisibility(View.VISIBLE);
							} else if ("采购租赁申请".equals(children.getName())) {
								purchaseLeaseApply.setVisibility(View.VISIBLE);
							} else if ("设备验收".equals(children.getName())) {
								equipmentAcceptance.setVisibility(View.VISIBLE);
							} else if ("外租设备台账".equals(children.getName())) {
								rentEquipmentLedger.setVisibility(View.VISIBLE);
							} else if ("合同台账".equals(children.getName())) {
								contractLedger.setVisibility(View.VISIBLE);
							}
						}
					}
				} else if (childrenRoot != null && "设备管理".equals(childrenRoot.getName())) {
					tv_device_menu02.setVisibility(View.VISIBLE);
					ll_device_menu02.setVisibility(View.VISIBLE);
					List<Children> childrens = childrenRoot.getChildren();
					if (childrens != null && childrens.size() > 0) {
						for (int j = 0; j < childrens.size(); j++) {
							Children children = childrens.get(j);
							if ("操作人员台账".equals(children.getName())) {
								operatorLedger.setVisibility(View.VISIBLE);
							} else if ("设备台账".equals(children.getName())) {
								deviceLedger.setVisibility(View.VISIBLE);
							} else if ("安全保护装置".equals(children.getName())) {
								safetyProtection.setVisibility(View.VISIBLE);
							} else if ("设备派遣".equals(children.getName())) {
								deviceDispatch.setVisibility(View.VISIBLE);
							} else if ("使用记录".equals(children.getName())) {
								deviceUseRecord.setVisibility(View.VISIBLE);
							} else if ("设备到场验证".equals(children.getName())) {
								deviceVerify.setVisibility(View.VISIBLE);
							} else if ("日常检查记录".equals(children.getName())) {
								dailyInspectionRecord.setVisibility(View.VISIBLE);
							} else if ("完好利用率".equals(children.getName())) {
								utilizationRate.setVisibility(View.VISIBLE);
							} else if ("重点设备月报表".equals(children.getName())) {
								monthlyRreport.setVisibility(View.VISIBLE);
							} else if ("保险管理".equals(children.getName())) {
								insuranceManage.setVisibility(View.VISIBLE);
							}
						}
					}
				} else if (childrenRoot != null && "设备维修".equals(childrenRoot.getName())) {
					tv_device_menu03.setVisibility(View.VISIBLE);
					ll_device_menu03.setVisibility(View.VISIBLE);
					List<Children> childrens = childrenRoot.getChildren();
					if (childrens != null && childrens.size() > 0) {
						for (int j = 0; j < childrens.size(); j++) {
							Children children = childrens.get(j);
							if ("设备维修计划".equals(children.getName())) {
								maintenancePlan.setVisibility(View.VISIBLE);
							} else if ("维修工单".equals(children.getName())) {
								maintenanceOrder.setVisibility(View.VISIBLE);
							} else if ("维修记录".equals(children.getName())) {
								maintenanceRecord.setVisibility(View.VISIBLE);
							} else if ("事故报告记录".equals(children.getName())) {
								accidentReportRecord.setVisibility(View.VISIBLE);
							}
						}
					}
				} else if (childrenRoot != null && "设备报废".equals(childrenRoot.getName())) {
					tv_device_menu04.setVisibility(View.VISIBLE);
					ll_device_menu04.setVisibility(View.VISIBLE);
					List<Children> childrens = childrenRoot.getChildren();
					if (childrens != null && childrens.size() > 0) {
						for (int j = 0; j < childrens.size(); j++) {
							Children children = childrens.get(j);
							if ("报废申请".equals(children.getName())) {
								scrapApply.setVisibility(View.VISIBLE);
							}
						}
					}
				} else if (childrenRoot != null && "备件油料".equals(childrenRoot.getName())) {
					tv_device_menu05.setVisibility(View.VISIBLE);
					ll_device_menu05.setVisibility(View.VISIBLE);
					List<Children> childrens = childrenRoot.getChildren();
					if (childrens != null && childrens.size() > 0) {
						for (int j = 0; j < childrens.size(); j++) {
							Children children = childrens.get(j);
							if ("入库台账记录".equals(children.getName())) {
								inLedgerRecords.setVisibility(View.VISIBLE);
							} else if ("出库台账记录".equals(children.getName())) {
								outLedgerRecords.setVisibility(View.VISIBLE);
							} else if ("加油管理".equals(children.getName())) {
								petrolManage.setVisibility(View.VISIBLE);
							} else if ("领料申请".equals(children.getName())) {
								materialApply.setVisibility(View.VISIBLE);
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
		case R.id.qualified_supplier_list:// 合格供方名册
			intent.putExtra("title", "合格供方名册");
			intent.putExtra("childAt", 0);
			intent.setClass(mContext, DevicePurchaseActivity.class);
			break;
		case R.id.purchase_lease_apply:// 采购租赁申请
			intent.putExtra("title", "采购租赁申请");
			intent.putExtra("childAt", 1);
			intent.setClass(mContext, DevicePurchaseActivity.class);
			break;
		case R.id.device_equipment_acceptance:// 设备验收
			intent.putExtra("title", "设备验收");
			intent.putExtra("childAt", 2);
			intent.setClass(mContext, DevicePurchaseActivity.class);
			break;
		case R.id.rent_equipment_ledger:// 外租设备台账
			intent.putExtra("title", "外租设备台账");
			intent.putExtra("childAt", 3);
			intent.setClass(mContext, DevicePurchaseActivity.class);
			break;
		case R.id.device_contract_ledger:// 合同台账
			intent.putExtra("title", "合同台账");
			intent.putExtra("childAt", 4);
			intent.setClass(mContext, DevicePurchaseActivity.class);
			break;
		case R.id.device_operator_ledger:// 操作人员台账
			intent.putExtra("title", "操作人员台账");
			intent.putExtra("childAt", 0);
			intent.setClass(mContext, DeviceManageListActivity.class);
			break;
		case R.id.device_ledger:// 设备台账
			intent.putExtra("title", "设备台账");
			intent.putExtra("childAt", 1);
			intent.setClass(mContext, DeviceManageListActivity.class);
			break;
		case R.id.safety_protection_device:// 安全保护装置
			intent.putExtra("title", "安全保护装置");
			intent.putExtra("childAt", 2);
			intent.setClass(mContext, DeviceManageListActivity.class);
			break;
		case R.id.device_dispatch:// 设备派遣
			intent.putExtra("title", "设备派遣");
			intent.putExtra("childAt", 3);
			intent.setClass(mContext, DeviceManageListActivity.class);
			break;
		case R.id.device_use_record:// 使用记录
			intent.putExtra("title", "使用记录");
			intent.putExtra("childAt", 4);
			intent.setClass(mContext, DeviceManageListActivity.class);
			break;
		case R.id.device_verify:// 设备到场验证
			intent.putExtra("title", "设备到场验证");
			intent.putExtra("childAt", 5);
			intent.setClass(mContext, DeviceManageListActivity.class);
			break;
		case R.id.daily_inspection_record:// 日常检查记录
			intent.putExtra("title", "日常检查记录");
			intent.putExtra("childAt", 6);
			intent.setClass(mContext, DeviceManageListActivity.class);
			break;
		case R.id.sound_utilization_rate:// 完好利用率
			intent.putExtra("title", "完好利用率");
			intent.putExtra("childAt", 7);
			intent.setClass(mContext, DeviceManageListActivity.class);
			break;
		case R.id.Key_equipment_monthly_report:// 重点设备月报表
			intent.putExtra("title", "重点设备月报表");
			intent.putExtra("childAt", 8);
			intent.setClass(mContext, DeviceManageListActivity.class);
			break;
		case R.id.insurance_management:// 保险管理
			intent.putExtra("title", "保险管理");
			intent.putExtra("childAt", 9);
			intent.setClass(mContext, DeviceManageListActivity.class);
			break;
		case R.id.device_maintenance_plan:// 设备维修计划
			intent.putExtra("title", "设备维修计划");
			intent.putExtra("childAt", 0);
			intent.setClass(mContext, DeviceMaintainListActivity.class);
			break;
		case R.id.device_maintenance_order:// 维修工单
			intent.putExtra("title", "维修工单");
			intent.putExtra("childAt", 1);
			intent.setClass(mContext, DeviceMaintainListActivity.class);
			break;
		case R.id.device_maintenance_record:// 维修记录
			intent.putExtra("title", "维修记录");
			intent.putExtra("childAt", 2);
			intent.setClass(mContext, DeviceMaintainListActivity.class);
			break;
		case R.id.Accident_report_record:// 事故报告记录
			intent.putExtra("title", "事故报告记录");
			intent.putExtra("childAt", 3);
			intent.setClass(mContext, DeviceMaintainListActivity.class);
			break;
		case R.id.device_scrap_apply:// 报废申请
			intent.putExtra("title", " 报废申请");
			intent.setClass(mContext, DeviceScrapListActivity.class);
			break;
		case R.id.in_ledger_records:// 入库台账记录
			intent.putExtra("title", "入库台账记录");
			intent.putExtra("childAt", 0);
			intent.setClass(mContext, DeviceSpareOilActivity.class);
			break;
		case R.id.out_ledger_records:// 出库台账记录
			intent.putExtra("title", "出库台账记录");
			intent.putExtra("childAt", 1);
			intent.setClass(mContext, DeviceSpareOilActivity.class);
			break;
		case R.id.petrol_manage:// 加油管理
			intent.putExtra("title", "加油管理");
			intent.putExtra("childAt", 2);
			intent.setClass(mContext, DeviceSpareOilActivity.class);
			break;
		case R.id.material_apply:// 领料申请
			intent.putExtra("title", "领料申请");
			intent.putExtra("childAt", 3);
			intent.setClass(mContext, DeviceSpareOilActivity.class);
			break;
		case R.id.ll_device_purchase:
			intent.putExtra("title", "采购租赁申请");
			intent.putExtra("childAt", 1);
			intent.setClass(mContext, DevicePurchaseActivity.class);
			break;
		case R.id.ll_device_scrap:
			intent.putExtra("title", " 报废申请");
			intent.setClass(mContext, DeviceScrapListActivity.class);
			break;
		case R.id.ll_device_maintain_plan:
			intent.putExtra("title", "设备维修计划");
			intent.putExtra("childAt", 0);
			intent.setClass(mContext, DeviceMaintainListActivity.class);
			break;
		case R.id.ll_device_accid:
			intent.putExtra("title", "事故报告记录");
			intent.putExtra("childAt", 3);
			intent.setClass(mContext, DeviceMaintainListActivity.class);
			break;
		case R.id.ll_device_insurance:
			intent.putExtra("title", "保险管理");
			intent.putExtra("childAt", 9);
			intent.setClass(mContext, DeviceManageListActivity.class);
			break;
		case R.id.car_location:
			MsgToast.geToast().setMsg("车辆定位");
			intent.putExtra("title", "车辆定位");
			intent.putExtra("childAt", 9);
			intent.setClass(mContext, DeviceVehicleListActivity.class);
			break;
		}
		mDrawerLayout.closeDrawer(left_drawer);
		((DeviceManageActivity) mContext).startActivity(intent);
	}
}
