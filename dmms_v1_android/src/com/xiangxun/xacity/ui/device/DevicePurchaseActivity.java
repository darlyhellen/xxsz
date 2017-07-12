package com.xiangxun.xacity.ui.device;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ViewFlipper;

import com.xiangxun.xacity.R;
import com.xiangxun.xacity.adapter.DevicePurchaseAdapter;
import com.xiangxun.xacity.app.BaseActivity;
import com.xiangxun.xacity.bean.ResponseResultBeans.ContractLedgerList;
import com.xiangxun.xacity.bean.ResponseResultBeans.InspectionList;
import com.xiangxun.xacity.bean.ResponseResultBeans.LeaseApplyList;
import com.xiangxun.xacity.bean.ResponseResultBeans.RentLedgerList;
import com.xiangxun.xacity.bean.ResponseResultBeans.SupplierBean;
import com.xiangxun.xacity.bean.ResponseResultBeans.SupplierList;
import com.xiangxun.xacity.common.ConstantStatus;
import com.xiangxun.xacity.request.DcNetWorkUtils;
import com.xiangxun.xacity.ui.device.detail.ContractLedgerDetailActivity;
import com.xiangxun.xacity.ui.device.detail.DevicePurchaseDetailActivity;
import com.xiangxun.xacity.view.MsgToast;
import com.xiangxun.xacity.view.TitleView;
import com.xiangxun.xacity.view.xlistView.XListView;
import com.xiangxun.xacity.view.xlistView.XListView.IXListViewListener;

/**
 * @package: com.xiangxun.xacity.ui.device
 * @ClassName: DevicePurchaseActivity.java
 * @Description: 设备采购菜单子页面
 * @author: HanGJ
 * @date: 2016-1-19 上午11:19:30
 */
public class DevicePurchaseActivity extends BaseActivity implements OnClickListener, IXListViewListener, OnItemClickListener {
	private static int listState = -1;
	private TitleView titleView;
	private XListView mXListView;
	private ViewFlipper mVF;
	private String title;
	private int childAt;
	private int currentPage = 1;
	private int PageSize = 10;
	private int totalSize = 0;
	public List<SupplierBean> data = new ArrayList<SupplierBean>();
	private DevicePurchaseAdapter adapter;
	/*************** 搜索字段 ****************/
	//
	private String name = "";
	private String linkman = "";
	private String product = "";
	//
	private String code = "";
	private String deviceName = "";
	private String model = "";
	private String startDate = "";
	private String endDate = "";
	//
	private String manufacturer = "";
	//
	private String deviceSupplierName = "";
	//
	private String cooperationDepartment = "";
	private String purchaseDeparmentId = "";

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			stopXListView();
			switch (msg.what) {
			case ConstantStatus.getDeviceQualifiedListSuccess:
				SupplierList supplierBeans = (SupplierList) msg.obj;
				if (supplierBeans != null && supplierBeans.supplierList != null) {
					setSupplierListData(supplierBeans.supplierList);
				} else {
					if (data.size() <= 0) {
						mVF.setDisplayedChild(2);
					}
				}
				break;
			case ConstantStatus.getDeviceQualifiedListFailed:
			case ConstantStatus.getDeviceLeaseApplyListFailed:
			case ConstantStatus.getDeviceRentLedgerListFailed:
			case ConstantStatus.getDeviceContractLedgerListFailed:
			case ConstantStatus.getDeviceAcceptanceListFailed:
				if (data.size() <= 0) {
					mVF.setDisplayedChild(2);
				}
				break;
			case ConstantStatus.getDeviceLeaseApplyListSuccess:
				LeaseApplyList leaseApplyList = (LeaseApplyList) msg.obj;
				if (leaseApplyList != null && leaseApplyList.purchaseList != null && leaseApplyList.purchaseList.size() > 0) {
					setSupplierListData(leaseApplyList.purchaseList);
				} else {
					if (data.size() <= 0) {
						mVF.setDisplayedChild(2);
					}
				}
				break;
			case ConstantStatus.getDeviceAcceptanceListSuccess:
				InspectionList InspectionList = (InspectionList) msg.obj;
				if (InspectionList != null && InspectionList.inspectionList != null && InspectionList.inspectionList.size() > 0) {
					setSupplierListData(InspectionList.inspectionList);
				} else {
					if (data.size() <= 0) {
						mVF.setDisplayedChild(2);
					}
				}
				break;
			case ConstantStatus.getDeviceRentLedgerListSuccess:
				RentLedgerList rentLedgerList = (RentLedgerList) msg.obj;
				if (rentLedgerList != null && rentLedgerList.rentList != null && rentLedgerList.rentList.size() > 0) {
					setSupplierListData(rentLedgerList.rentList);
				} else {
					if (data.size() <= 0) {
						mVF.setDisplayedChild(2);
					}
				}
				break;
			case ConstantStatus.getDeviceContractLedgerListSuccess:
				ContractLedgerList contractLedgerList = (ContractLedgerList) msg.obj;
				if (contractLedgerList != null && contractLedgerList.contractList != null && contractLedgerList.contractList.size() > 0) {
					setSupplierListData(contractLedgerList.contractList);
				} else {
					if (data.size() <= 0) {
						mVF.setDisplayedChild(2);
					}
				}
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
		setContentView(R.layout.device_purchase_list_layout);
		initView();
		initData();
		initListener();
	}

	@Override
	public void initView() {
		titleView = (TitleView) findViewById(R.id.comment_title);
		mXListView = (XListView) findViewById(R.id.xlistview);
		mVF = (ViewFlipper) findViewById(R.id.viewFlipper);
	}

	@Override
	public void initData() {
		title = getIntent().getStringExtra("title");
		childAt = getIntent().getIntExtra("childAt", 0);
		titleView.setTitle(title);
		listState = ConstantStatus.listStateFirst;
		adapter = new DevicePurchaseAdapter(this, childAt, mXListView);
		RequestList();
	}

	@Override
	public void initListener() {
		titleView.setRightOnClickListener(this);
		mXListView.setPullLoadEnable(true);
		mXListView.setXListViewListener(this);
		mXListView.setOnItemClickListener(this);
		titleView.setLeftBackOneListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.xw_share:
			Intent intent = new Intent(this, DevicePurchaseSearchActivity.class);
			intent.putExtra("title", title);
			intent.putExtra("childAt", childAt);
			startActivityForResult(intent, 110);
			break;
		}
	}

	/**
	 * 合格供方名册
	 * 
	 * @param supplierBeans
	 */
	protected void setSupplierListData(List<SupplierBean> supplierBeans) {
		switch (listState) {
		case ConstantStatus.listStateFirst:
			data.clear();
			data.addAll(supplierBeans);
			adapter.setData(data, supplierBeans.size());
			mXListView.setAdapter(adapter);
			break;
		case ConstantStatus.listStateRefresh:
			data.clear();
			data.addAll(supplierBeans);
			adapter.setData(data, supplierBeans.size());
			mXListView.setAdapter(adapter);
			break;
		case ConstantStatus.listStateLoadMore:
			data.addAll(supplierBeans);
			adapter.setData(data, supplierBeans.size());
			break;
		}
		mVF.setDisplayedChild(0);
		totalSize = supplierBeans.size();
		// 没有加载到数据
		if (data.size() == 0) {
			mVF.setDisplayedChild(2);
		}
	}

	private void RequestList() {
		switch (listState) {
		case ConstantStatus.listStateFirst:
			mVF.setDisplayedChild(1);
			break;
		case ConstantStatus.listStateRefresh:
			mVF.setDisplayedChild(0);
			break;
		case ConstantStatus.listStateLoadMore:
			mVF.setDisplayedChild(0);
			break;
		}
		if (childAt == 0) {
			DcNetWorkUtils.getDeviceQualifiedList(this, handler, account, password, currentPage + "", PageSize + "", name, linkman, product);
		} else if (childAt == 1) {
			DcNetWorkUtils.getDeviceLeaseApplyList(this, handler, account, password, currentPage + "", PageSize + "", code, deviceName, model, startDate, endDate);
		} else if (childAt == 2) {
			DcNetWorkUtils.getDeviceAcceptanceList(this, handler, account, password, currentPage + "", PageSize + "", manufacturer, deviceName, model, startDate, endDate);
		} else if (childAt == 3) {
			DcNetWorkUtils.getDeviceRentLedgerList(this, handler, account, password, currentPage + "", PageSize + "", deviceSupplierName, deviceName, model, startDate, endDate);
		} else if (childAt == 4) {
			DcNetWorkUtils.getDeviceContractLedgerList(this, handler, account, password, currentPage + "", PageSize + "", name, cooperationDepartment, purchaseDeparmentId, startDate, endDate);
		}
	}

	@Override
	public void onRefresh(View v) {
		currentPage = 1;
		listState = ConstantStatus.listStateRefresh;
		RequestList();
	}

	@Override
	public void onLoadMore(View v) {
		if (totalSize < PageSize) {
			MsgToast.geToast().setMsg("已经是最后一页");
			mXListView.removeFooterView(mXListView.mFooterView);
		} else {
			currentPage++;
			listState = ConstantStatus.listStateLoadMore;
			RequestList();
		}
	}

	// xLisView 停止
	private void stopXListView() {
		mXListView.stopRefresh();
		mXListView.stopLoadMore();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		SupplierBean bean = (SupplierBean) adapter.getItem(position - 1);
		if (childAt == 4) {
			Intent intent = new Intent(this, ContractLedgerDetailActivity.class);
			intent.putExtra("id", bean.id);
			startActivity(intent);
		} else {
			Intent intent = new Intent(this, DevicePurchaseDetailActivity.class);
			intent.putExtra("bean", (Serializable) bean);
			intent.putExtra("childAt", childAt);
			intent.putExtra("title", title);
			startActivity(intent);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null && requestCode == 110) {
			switch (resultCode) {
			case 0:
				name = data.getStringExtra("name");
				linkman = data.getStringExtra("linkman");
				product = data.getStringExtra("product");
				break;
			case 1:
				code = data.getStringExtra("code");
				deviceName = data.getStringExtra("deviceName");
				model = data.getStringExtra("model");
				startDate = data.getStringExtra("startDate");
				endDate = data.getStringExtra("endDate");
				break;
			case 2:
				manufacturer = data.getStringExtra("manufacturer");
				deviceName = data.getStringExtra("deviceName");
				model = data.getStringExtra("model");
				startDate = data.getStringExtra("startDate");
				endDate = data.getStringExtra("endDate");
				break;
			case 3:
				deviceSupplierName = data.getStringExtra("deviceSupplierName");
				deviceName = data.getStringExtra("deviceName");
				model = data.getStringExtra("model");
				startDate = data.getStringExtra("startDate");
				endDate = data.getStringExtra("endDate");
				break;
			case 4:
				name = data.getStringExtra("name");
				cooperationDepartment = data.getStringExtra("cooperationDepartment");
				purchaseDeparmentId = data.getStringExtra("purchaseDeparmentId");
				startDate = data.getStringExtra("startDate");
				endDate = data.getStringExtra("endDate");
				break;
			}
			this.data.clear();
			listState = ConstantStatus.listStateFirst;
			currentPage = 1;
			RequestList();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}
