package com.xiangxun.xacity.ui.occupy;

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
import com.xiangxun.xacity.adapter.OccupyAnalysisAdapter;
import com.xiangxun.xacity.app.BaseActivity;
import com.xiangxun.xacity.bean.ResponseResultBeans.OccupyManageBean;
import com.xiangxun.xacity.common.ConstantStatus;
import com.xiangxun.xacity.request.DcNetWorkUtils;
import com.xiangxun.xacity.ui.occupy.detail.OccupyAnalysisDetailActivity;
import com.xiangxun.xacity.view.MsgToast;
import com.xiangxun.xacity.view.TitleView;
import com.xiangxun.xacity.view.xlistView.XListView;
import com.xiangxun.xacity.view.xlistView.XListView.IXListViewListener;

/**
 * @package: com.xiangxun.xacity.ui.occupy
 * @ClassName: OccupyCountActivity.java
 * @Description: 挖占统计页面
 * @author: HanGJ
 * @date: 2016-1-26 下午2:47:31
 */
public class OccupyAnalysisActivity extends BaseActivity implements OnClickListener, IXListViewListener, OnItemClickListener {
	private static int listState = -1;
	private TitleView titleView;
	private XListView mXListView;
	private ViewFlipper mVF;
	private String title;
	private int childAt;
	private int currentPage = 1;
	private int PageSize = 10;
	private int totalSize = 0;
	public List<OccupyManageBean> data = new ArrayList<OccupyManageBean>();
	private OccupyAnalysisAdapter adapter;
	/*************** 搜索字段 ****************/
	private String startTime = "";
	private String endTime = "";
	private String managearea = "";
	private String roadname = "";

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			stopXListView();
			switch (msg.what) {
			case ConstantStatus.getOccupyListSuccess:
				List<OccupyManageBean> manageBeans = (List<OccupyManageBean>) msg.obj;
				if (manageBeans != null) {
					setOccupyManageData(manageBeans);
				} else {
					if (data.size() <= 0) {
						mVF.setDisplayedChild(2);
					}
				}
				break;
			case ConstantStatus.getOccupyListFailed:
				if (data.size() <= 0) {
					mVF.setDisplayedChild(2);
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

	protected void setOccupyManageData(List<OccupyManageBean> manageBeans) {
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
		adapter = new OccupyAnalysisAdapter(this, mXListView);
		RequestList();
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
		if (childAt == 0) {// 挖占统计网络请求
			DcNetWorkUtils.getOccupyStatisticsList(this, handler, account, password, currentPage + "", PageSize + "", startTime, endTime, managearea, roadname);
		} else if (childAt == 1) {// 退让面积统计网络请求 =
			DcNetWorkUtils.getOccupyConcessionList(this, handler, account, password, currentPage + "", PageSize + "", startTime, endTime, managearea, roadname);
		}
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
			Intent intent = new Intent(this, OccuoyAnalysisSearchActivity.class);
			intent.putExtra("title", title);
			intent.putExtra("childAt", childAt);
			startActivityForResult(intent, 132);
			break;
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
		OccupyManageBean bean = (OccupyManageBean) adapter.getItem(position - 1);
		Intent intent = new Intent(this, OccupyAnalysisDetailActivity.class);
		intent.putExtra("bean", (Serializable) bean);
		intent.putExtra("title", title);
		startActivity(intent);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(data != null && requestCode == 132){
			switch (resultCode) {
			case 0:
				managearea = data.getStringExtra("managearea");
				roadname = data.getStringExtra("roadname");
				startTime = data.getStringExtra("startTime");
				endTime = data.getStringExtra("endTime");
				break;
			case 1:
				managearea = data.getStringExtra("managearea");
				roadname = data.getStringExtra("roadname");
				startTime = data.getStringExtra("startTime");
				endTime = data.getStringExtra("endTime");
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
