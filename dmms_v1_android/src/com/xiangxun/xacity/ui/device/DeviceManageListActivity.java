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
import com.xiangxun.xacity.adapter.DeviceManageAdapter;
import com.xiangxun.xacity.app.BaseActivity;
import com.xiangxun.xacity.bean.ResponseResultBeans.DeviceManageBean;
import com.xiangxun.xacity.common.ConstantStatus;
import com.xiangxun.xacity.request.DcNetWorkUtils;
import com.xiangxun.xacity.ui.device.detail.DeviceManageDetailActivity;
import com.xiangxun.xacity.view.MsgToast;
import com.xiangxun.xacity.view.TitleView;
import com.xiangxun.xacity.view.xlistView.XListView;
import com.xiangxun.xacity.view.xlistView.XListView.IXListViewListener;

/**
 * @package: com.xiangxun.xacity.ui.device
 * @ClassName: DeviceManageListActivity.java
 * @Description: 设备管理列表页面
 * @author: HanGJ
 * @date: 2016-2-25 上午8:57:11
 */
public class DeviceManageListActivity extends BaseActivity implements OnClickListener, IXListViewListener, OnItemClickListener {
	private static int listState = -1;
	private TitleView titleView;
	private XListView mXListView;
	private ViewFlipper mVF;
	private String title;
	private int childAt;
	private int currentPage = 1;
	private int PageSize = 10;
	private int totalSize = 0;
	private DeviceManageAdapter adapter;
	public List<DeviceManageBean> data = new ArrayList<DeviceManageBean>();
	/*************** 搜索字段 ****************/
	private String name = "";
	private String sex = "";
	private String standardcultre = "";
	private String startDateTime = "";
	private String endDateTime = "";
	//
	private String type = "";
	private String usingCompany = "";
	private String smallType = "";
	private String model = "";
	private String brand = "";
	private String plates = "";
	private String fueltype = "";
	private String surroundingSign = "";
	private String driver = "";
	private String manufacturers = "";
	private String capotalSource = "";
	private String curtain = "";
	private String belongtoCompany = "";
	private String operator = "";
	//
	private String deviceCode = "";
	private String deviceName = "";
	private String deviceSafety = "";

	//
	private String deviceModel = "";
	//
	private String mdeUsingCompanyName = "";
	private String mdName = "";
	private String mdModel = "";
	//
	private String code = "";
	private String plate = "";
	//
	private String usingCompanyName = "";
	private String month = "";
	//
	private String company = "";

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			stopXListView();
			switch (msg.what) {
			case ConstantStatus.getOperatorLedgerListSuccess:
				List<DeviceManageBean> operatorList = (List<DeviceManageBean>) msg.obj;
				if (operatorList != null) {
					setDeviceManageData(operatorList);
				} else {
					if (data.size() <= 0) {
						mVF.setDisplayedChild(2);
					}
				}
				break;
			case ConstantStatus.getOperatorLedgerListFailed:
				mVF.setDisplayedChild(2);
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
		adapter = new DeviceManageAdapter(this, childAt, mXListView);
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
			Intent intent = new Intent(this, DiviceManageSearchActivity.class);
			intent.putExtra("title", title);
			intent.putExtra("childAt", childAt);
			startActivityForResult(intent, 130);
			break;
		}
	}

	protected void setDeviceManageData(List<DeviceManageBean> manageBeans) {
		switch (listState) {
		case ConstantStatus.listStateFirst:
			data.clear();
			data.addAll(manageBeans);
			adapter.setData(data, manageBeans.size());
			mXListView.setAdapter(adapter);
			break;
		case ConstantStatus.listStateRefresh:
			data.clear();
			data.addAll(manageBeans);
			adapter.setData(data, manageBeans.size());
			mXListView.setAdapter(adapter);
			break;
		case ConstantStatus.listStateLoadMore:
			data.addAll(manageBeans);
			adapter.setData(data, manageBeans.size());
			break;
		}
		mVF.setDisplayedChild(0);
		totalSize = manageBeans.size();
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
		if (childAt == 0) {// 操作人员台账网络请求
			DcNetWorkUtils.getOperatorLedgerList(this, handler, account, password, currentPage + "", PageSize + "", name, sex, standardcultre, startDateTime, endDateTime);
		} else if (childAt == 1) {// 设备台账网络请求
			DcNetWorkUtils.getDeviceLedgerList(this, handler, account, password, currentPage + "", PageSize + "", type, name, usingCompany, startDateTime, endDateTime, smallType, model, brand, plates, fueltype, surroundingSign, driver, manufacturers, capotalSource, curtain, belongtoCompany, operator);
		} else if (childAt == 2) {// 安全保护装置网络请求
			DcNetWorkUtils.getSafetyProtectionList(this, handler, account, password, currentPage + "", PageSize + "", deviceCode, deviceName, deviceSafety);
		} else if (childAt == 3) {// 设备派遣网络请求
			DcNetWorkUtils.getDeviceDispatchList(this, handler, account, password, currentPage + "", PageSize + "", deviceName, usingCompany, deviceModel, startDateTime, endDateTime);
		} else if (childAt == 4) {// 使用记录网络请求
			DcNetWorkUtils.getDeviceUseRecordList(this, handler, account, password, currentPage + "", PageSize + "", mdeUsingCompanyName, mdName, mdModel, startDateTime, endDateTime);
		} else if (childAt == 5) {// 设备到场验证网络请求
			DcNetWorkUtils.getDeviceVerifyList(this, handler, account, password, currentPage + "", PageSize + "", deviceCode, deviceName, model, startDateTime, endDateTime);
		} else if (childAt == 6) {// 日常检查记录网络请求
			DcNetWorkUtils.getDailyInspectionRecordList(this, handler, account, password, currentPage + "", PageSize + "", code, deviceName, plate, startDateTime, endDateTime);
		} else if (childAt == 7) {// 完好利用率网络请求
			DcNetWorkUtils.getUtilizationRateList(this, handler, account, password, currentPage + "", PageSize + "", usingCompanyName, deviceName, deviceModel, month);
		} else if (childAt == 8) {// 重点设备月报表网络请求
			DcNetWorkUtils.getMonthlyRreportList(this, handler, account, password, currentPage + "", PageSize + "", deviceName, company, deviceModel, startDateTime, endDateTime);
		} else if (childAt == 9) {// 保险管理网络请求
			DcNetWorkUtils.getInsuranceManageList(this, handler, account, password, currentPage + "", PageSize + "", deviceName, name, deviceModel, startDateTime, endDateTime);
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
		DeviceManageBean bean = (DeviceManageBean) adapter.getItem(position - 1);
		Intent intent = new Intent(this, DeviceManageDetailActivity.class);
		intent.putExtra("bean", (Serializable) bean);
		intent.putExtra("childAt", childAt);
		intent.putExtra("title", title);
		startActivity(intent);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(data != null && requestCode == 130){
			switch (resultCode) {
			case 0:
				name = data.getStringExtra("name");
				sex = data.getStringExtra("sex");
				standardcultre = data.getStringExtra("standardcultre");
				startDateTime = data.getStringExtra("startDateTime");
				endDateTime = data.getStringExtra("endDateTime");
				break;
			case 1:
				type = data.getStringExtra("type");
				name = data.getStringExtra("name");
				usingCompany = data.getStringExtra("usingCompany");
				startDateTime = data.getStringExtra("startDateTime");
				endDateTime = data.getStringExtra("endDateTime");
				smallType = data.getStringExtra("smallType");
				model = data.getStringExtra("model");
				brand = data.getStringExtra("brand");
				plates = data.getStringExtra("plates");
				fueltype = data.getStringExtra("fueltype");
				surroundingSign = data.getStringExtra("surroundingSign");
				driver = data.getStringExtra("driver");
				manufacturers = data.getStringExtra("manufacturers");
				capotalSource = data.getStringExtra("capotalSource");
				curtain = data.getStringExtra("curtain");
				belongtoCompany = data.getStringExtra("belongtoCompany");
				operator = data.getStringExtra("operator");
				break;
			case 2:
				deviceCode = data.getStringExtra("deviceCode");
				deviceName = data.getStringExtra("deviceName");
				deviceSafety = data.getStringExtra("deviceSafety");
				break;
			case 3:
				usingCompany = data.getStringExtra("usingCompany");
				deviceName = data.getStringExtra("deviceName");
				deviceModel = data.getStringExtra("deviceModel");
				startDateTime = data.getStringExtra("startDateTime");
				endDateTime = data.getStringExtra("endDateTime");
				break;
			case 4:
				mdName = data.getStringExtra("mdName");
				mdeUsingCompanyName = data.getStringExtra("mdeUsingCompanyName");
				mdModel = data.getStringExtra("mdModel");
				startDateTime = data.getStringExtra("startDateTime");
				endDateTime = data.getStringExtra("endDateTime");
				break;
			case 5:
				deviceName = data.getStringExtra("deviceName");
				deviceCode = data.getStringExtra("deviceCode");
				model = data.getStringExtra("model");
				startDateTime = data.getStringExtra("startDateTime");
				endDateTime = data.getStringExtra("endDateTime");
				break;
			case 6:
				deviceName = data.getStringExtra("deviceName");
				code = data.getStringExtra("code");
				plate = data.getStringExtra("plate");
				startDateTime = data.getStringExtra("startDateTime");
				endDateTime = data.getStringExtra("endDateTime");
				break;
			case 7:
				deviceName = data.getStringExtra("deviceName");
				usingCompanyName = data.getStringExtra("usingCompanyName");
				deviceModel = data.getStringExtra("deviceModel");
				month = data.getStringExtra("month");
				break;
			case 8:
				deviceName = data.getStringExtra("deviceName");
				company = data.getStringExtra("company");
				deviceModel = data.getStringExtra("deviceModel");
				startDateTime = data.getStringExtra("startDateTime");
				endDateTime = data.getStringExtra("endDateTime");
				break;
			case 9:
				deviceName = data.getStringExtra("deviceName");
				name = data.getStringExtra("name");
				deviceModel = data.getStringExtra("deviceModel");
				startDateTime = data.getStringExtra("startDateTime");
				endDateTime = data.getStringExtra("endDateTime");
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
