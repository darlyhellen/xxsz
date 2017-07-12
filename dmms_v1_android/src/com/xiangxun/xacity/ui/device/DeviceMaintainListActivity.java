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
import com.xiangxun.xacity.adapter.DeviceMaintainAdapter;
import com.xiangxun.xacity.app.BaseActivity;
import com.xiangxun.xacity.bean.ResponseResultBeans.DeviceMaintainBean;
import com.xiangxun.xacity.common.ConstantStatus;
import com.xiangxun.xacity.request.DcNetWorkUtils;
import com.xiangxun.xacity.ui.device.detail.DeviceMaintainDetailActivity;
import com.xiangxun.xacity.view.MsgToast;
import com.xiangxun.xacity.view.TitleView;
import com.xiangxun.xacity.view.xlistView.XListView;
import com.xiangxun.xacity.view.xlistView.XListView.IXListViewListener;

public class DeviceMaintainListActivity extends BaseActivity implements OnClickListener, IXListViewListener, OnItemClickListener {
	private static int listState = -1;
	private TitleView titleView;
	private XListView mXListView;
	private ViewFlipper mVF;
	private String title;
	private int childAt;
	private int currentPage = 1;
	private int PageSize = 10;
	private int totalSize = 0;
	public List<DeviceMaintainBean> data = new ArrayList<DeviceMaintainBean>();
	private DeviceMaintainAdapter adapter;
	/*************** 搜索字段 ****************/
	private String deviceName = "";
	private String deviceCode = "";
	private String plate = "";
	private String cost = "";
	private String startDateTime = "";
	private String endDateTime = "";
	//
	private String code = "";
	private String repairFactory = "";
	
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			stopXListView();
			switch (msg.what) {
			case ConstantStatus.getDeviceMaintainListSuccess:
				List<DeviceMaintainBean> maintainBeans = (List<DeviceMaintainBean>) msg.obj;
				if(maintainBeans != null){
					setDeviceMaintainData(maintainBeans);
				} else {
					if(data.size() <= 0){
						mVF.setDisplayedChild(2);
					}
				}
				break;
			case ConstantStatus.getDeviceMaintainListFailed:
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

	protected void setDeviceMaintainData(List<DeviceMaintainBean> maintainBeans) {
		switch (listState) {
		case ConstantStatus.listStateFirst:
			data.clear();
			data.addAll(maintainBeans);
			adapter.setData(data, maintainBeans.size());
			mXListView.setAdapter(adapter);
			break;
		case ConstantStatus.listStateRefresh:
			data.clear();
			data.addAll(maintainBeans);
			adapter.setData(data, maintainBeans.size());
			mXListView.setAdapter(adapter);
			break;
		case ConstantStatus.listStateLoadMore:
			data.addAll(maintainBeans);
			adapter.setData(data, maintainBeans.size());
			break;
		}
		mVF.setDisplayedChild(0);
		totalSize = maintainBeans.size();
		// 没有加载到数据
		if (data.size() == 0) {
			mVF.setDisplayedChild(2);
		}
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
		adapter = new DeviceMaintainAdapter(this, childAt, mXListView);
		listState = ConstantStatus.listStateFirst;
		RequestList();
	}

	@Override
	public void initListener() {
		mXListView.setPullLoadEnable(true);
		mXListView.setXListViewListener(this);
		mXListView.setOnItemClickListener(this);
		titleView.setRightOnClickListener(this);
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
			Intent intent = new Intent(this, DeviceMaintainSearchActivity.class);
			intent.putExtra("title", title);
			intent.putExtra("childAt", childAt);
			startActivityForResult(intent, 113);
			break;
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
		if (childAt == 0) {// 设备维修计划网络请求
			DcNetWorkUtils.getMaintenancePlanList(this, handler, account, password, currentPage + "", PageSize + "", deviceName, deviceCode, plate, startDateTime, endDateTime);
		} else if (childAt == 1) {// 维修工单网络请求
			DcNetWorkUtils.getMaintenanceOrderList(this, handler, account, password, currentPage + "", PageSize + "", code, repairFactory, deviceName, startDateTime, endDateTime);
		} else if (childAt == 2) {// 维修记录网络请求
			DcNetWorkUtils.getMaintenanceRecordList(this, handler, account, password, currentPage + "", PageSize + "", deviceName, deviceCode, cost, startDateTime, endDateTime);
		} else if (childAt == 3) {// 事故报告记录网络请求
			DcNetWorkUtils.getAccidentReportRecordList(this, handler, account, password, currentPage + "", PageSize + "", code, deviceName, deviceCode, startDateTime, endDateTime);
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
		DeviceMaintainBean bean = (DeviceMaintainBean) adapter.getItem(position - 1);
		Intent intent = new Intent(this, DeviceMaintainDetailActivity.class);
		intent.putExtra("bean", (Serializable) bean);
		intent.putExtra("childAt", childAt);
		intent.putExtra("title", title);
		startActivity(intent);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(data != null && requestCode == 113){
			switch (resultCode) {
			case 0:
				deviceName = data.getStringExtra("deviceName");
				deviceCode = data.getStringExtra("deviceCode");
				plate = data.getStringExtra("plate");
				startDateTime = data.getStringExtra("startDateTime");
				endDateTime = data.getStringExtra("endDateTime");
				break;
			case 1:
				code = data.getStringExtra("code");
				repairFactory = data.getStringExtra("repairFactory");
				deviceName = data.getStringExtra("deviceName");
				startDateTime = data.getStringExtra("startDateTime");
				endDateTime = data.getStringExtra("endDateTime");
				break;
			case 2:
				deviceName = data.getStringExtra("deviceName");
				deviceCode = data.getStringExtra("deviceCode");
				cost = data.getStringExtra("cost");
				startDateTime = data.getStringExtra("startDateTime");
				endDateTime = data.getStringExtra("endDateTime");
				break;
			case 3:
				code = data.getStringExtra("code");
				deviceName = data.getStringExtra("deviceName");
				deviceCode = data.getStringExtra("deviceCode");
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
