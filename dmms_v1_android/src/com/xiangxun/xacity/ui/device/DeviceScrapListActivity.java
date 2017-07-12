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
import com.xiangxun.xacity.adapter.DeviceScrapAdapter;
import com.xiangxun.xacity.app.BaseActivity;
import com.xiangxun.xacity.bean.ResponseResultBeans.DeviceScrapBean;
import com.xiangxun.xacity.common.ConstantStatus;
import com.xiangxun.xacity.request.DcNetWorkUtils;
import com.xiangxun.xacity.ui.device.detail.DeviceScrapDetailActivity;
import com.xiangxun.xacity.view.MsgToast;
import com.xiangxun.xacity.view.TitleView;
import com.xiangxun.xacity.view.xlistView.XListView;
import com.xiangxun.xacity.view.xlistView.XListView.IXListViewListener;

public class DeviceScrapListActivity extends BaseActivity implements OnClickListener, IXListViewListener, OnItemClickListener {
	private static int listState = -1;
	private TitleView titleView;
	private XListView mXListView;
	private ViewFlipper mVF;
	private String title;
	private int currentPage = 1;
	private int PageSize = 10;
	private int totalSize = 0;
	public List<DeviceScrapBean> data = new ArrayList<DeviceScrapBean>();
	private DeviceScrapAdapter adapter;
	private String code = "";
	private String deviceName = "";
	private String deviceCode = "";
	private String startDate = "";
	private String endDate = "";
	
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			stopXListView();
			switch (msg.what) {
			case ConstantStatus.getDeviceScrapListSuccess:
				List<DeviceScrapBean> deviceScrapBeans = (List<DeviceScrapBean>) msg.obj;
				if(deviceScrapBeans != null){
					setDeviceScrapData(deviceScrapBeans);
				} else {
					if (data.size() <= 0) {
						mVF.setDisplayedChild(2);
					}
				}
				break;
			case ConstantStatus.getDeviceScrapListFailed:
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

	protected void setDeviceScrapData(List<DeviceScrapBean> deviceScrapBeans) {
		switch (listState) {
		case ConstantStatus.listStateFirst:
			data.clear();
			data.addAll(deviceScrapBeans);
			adapter.setData(data, deviceScrapBeans.size());
			mXListView.setAdapter(adapter);
			break;
		case ConstantStatus.listStateRefresh:
			data.clear();
			data.addAll(deviceScrapBeans);
			adapter.setData(data, deviceScrapBeans.size());
			mXListView.setAdapter(adapter);
			break;
		case ConstantStatus.listStateLoadMore:
			data.addAll(deviceScrapBeans);
			adapter.setData(data, deviceScrapBeans.size());
			break;
		}
		mVF.setDisplayedChild(0);
		totalSize = deviceScrapBeans.size();
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
		titleView.setTitle(title);
		adapter = new DeviceScrapAdapter(this, mXListView);
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
			Intent intent = new Intent(this, DeviceScrapSearchActivity.class);
			intent.putExtra("title", title);
			startActivityForResult(intent, 111);
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
		DcNetWorkUtils.getDeviceScrapApplyList(this, handler, account, password, currentPage + "", PageSize + "", code, deviceName, deviceCode, startDate, endDate);
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
		DeviceScrapBean bean = (DeviceScrapBean) adapter.getItem(position - 1);
		Intent intent = new Intent(this, DeviceScrapDetailActivity.class);
		intent.putExtra("bean", (Serializable) bean);
		startActivity(intent);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(data != null && requestCode == 111){
			if(resultCode == RESULT_OK){
				code = data.getStringExtra("code");
				deviceName = data.getStringExtra("deviceName");
				deviceCode = data.getStringExtra("deviceCode");
				startDate = data.getStringExtra("startDate");
				endDate = data.getStringExtra("endDate");
				this.data.clear();
				listState = ConstantStatus.listStateFirst;
				currentPage = 1;
				RequestList();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}
