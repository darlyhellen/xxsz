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
import com.xiangxun.xacity.adapter.DeviceSpareOilAdapter;
import com.xiangxun.xacity.app.BaseActivity;
import com.xiangxun.xacity.bean.ResponseResultBeans.DeviceSpareOilBean;
import com.xiangxun.xacity.common.ConstantStatus;
import com.xiangxun.xacity.request.DcNetWorkUtils;
import com.xiangxun.xacity.ui.device.detail.DeviceSpareOilDetailActivity;
import com.xiangxun.xacity.view.MsgToast;
import com.xiangxun.xacity.view.TitleView;
import com.xiangxun.xacity.view.xlistView.XListView;
import com.xiangxun.xacity.view.xlistView.XListView.IXListViewListener;
/**
 * @package: com.xiangxun.xacity.ui.device
 * @ClassName: DeviceSpareOilActivity.java
 * @Description: 备件油料列表页面
 * @author: HanGJ
 * @date: 2016-2-24 下午2:12:22
 */
public class DeviceSpareOilActivity extends BaseActivity implements OnClickListener, IXListViewListener, OnItemClickListener {
	private static int listState = -1;
	private TitleView titleView;
	private XListView mXListView;
	private ViewFlipper mVF;
	private String title;
	private int childAt;
	private int currentPage = 1;
	private int PageSize = 10;
	private int totalSize = 0;
	public List<DeviceSpareOilBean> data = new ArrayList<DeviceSpareOilBean>();
	private DeviceSpareOilAdapter adapter;
	/*************** 搜索字段 ****************/
	//
	private String name = "";
	private String oname = "";
	private String model = "";
	private String startDateTime = "";
	private String endDateTime = "";
	//
	private String code = "";
	private String getPerson = "";
	private String projectName = "";
	//
	private String orgName = "";
	private String plate = "";
	//
	private String company = "";
	
	
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			stopXListView();
			switch (msg.what) {
			case ConstantStatus.getDeviceSpareOilListSuccess:
				List<DeviceSpareOilBean> spareOilBeans = (List<DeviceSpareOilBean>) msg.obj;
				if(spareOilBeans != null){
					setDeviceSpareOilData(spareOilBeans);
				} else {
					if (data.size() <= 0) {
						mVF.setDisplayedChild(2);
					}
				}
				break;
			case ConstantStatus.getDeviceSpareOilListFailed:
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

	protected void setDeviceSpareOilData(List<DeviceSpareOilBean> spareOilBeans) {
		switch (listState) {
		case ConstantStatus.listStateFirst:
			data.clear();
			data.addAll(spareOilBeans);
			adapter.setData(data, spareOilBeans.size());
			mXListView.setAdapter(adapter);
			break;
		case ConstantStatus.listStateRefresh:
			data.clear();
			data.addAll(spareOilBeans);
			adapter.setData(data, spareOilBeans.size());
			mXListView.setAdapter(adapter);
			break;
		case ConstantStatus.listStateLoadMore:
			data.addAll(spareOilBeans);
			adapter.setData(data, spareOilBeans.size());
			break;
		}
		mVF.setDisplayedChild(0);
		totalSize = spareOilBeans.size();
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
		listState = ConstantStatus.listStateFirst;
		adapter = new DeviceSpareOilAdapter(this, childAt, mXListView);
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
			Intent intent = new Intent(this, DeviceSparePartOilSearchActivity.class);
			intent.putExtra("title", title);
			intent.putExtra("childAt", childAt);
			startActivityForResult(intent, 112);
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
		if (childAt == 0) {// 入库台账记录
			DcNetWorkUtils.getInLedgerRecordsList(this, handler, account, password, currentPage + "", PageSize + "", name, oname, model, startDateTime, endDateTime);
		} else if (childAt == 1) {// 出库台账记录
			DcNetWorkUtils.getOutLedgerRecordsList(this, handler, account, password, currentPage + "", PageSize + "", name, oname, code, getPerson, projectName, startDateTime, endDateTime);
		} else if (childAt == 2) {// 加油管理
			DcNetWorkUtils.getOilPetrolManageList(this, handler, account, password, currentPage + "", PageSize + "", name, orgName, plate, startDateTime, endDateTime);
		} else if (childAt == 3) {// 领料申请
			DcNetWorkUtils.getMaterialApplyList(this, handler, account, password, currentPage + "", PageSize + "", name, company, model, startDateTime, endDateTime);
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
		DeviceSpareOilBean bean = (DeviceSpareOilBean) adapter.getItem(position - 1);
		Intent intent = new Intent(this, DeviceSpareOilDetailActivity.class);
		intent.putExtra("bean", (Serializable) bean);
		intent.putExtra("childAt", childAt);
		intent.putExtra("title", title);
		startActivity(intent);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(data != null && requestCode == 112){
			switch (resultCode) {
			case 0:
				name = data.getStringExtra("name");
				oname = data.getStringExtra("oname");
				model = data.getStringExtra("model");
				startDateTime = data.getStringExtra("startDateTime");
				endDateTime = data.getStringExtra("endDateTime");
				break;
			case 1:
				name = data.getStringExtra("name");
				oname = data.getStringExtra("oname");
				code = data.getStringExtra("code");
				getPerson = data.getStringExtra("getPerson");
				projectName = data.getStringExtra("projectName");
				startDateTime = data.getStringExtra("startDateTime");
				endDateTime = data.getStringExtra("endDateTime");
				break;
			case 2:
				name = data.getStringExtra("name");
				orgName = data.getStringExtra("orgName");
				plate = data.getStringExtra("plate");
				startDateTime = data.getStringExtra("startDateTime");
				endDateTime = data.getStringExtra("endDateTime");
				break;
			case 3:
				name = data.getStringExtra("name");
				company = data.getStringExtra("company");
				model = data.getStringExtra("model");
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
