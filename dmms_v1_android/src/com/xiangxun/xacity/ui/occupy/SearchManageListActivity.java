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
import com.xiangxun.xacity.adapter.OccupySearchAdapter;
import com.xiangxun.xacity.app.BaseActivity;
import com.xiangxun.xacity.bean.ResponseResultBeans.OccupySearchBean;
import com.xiangxun.xacity.common.ConstantStatus;
import com.xiangxun.xacity.request.DcNetWorkUtils;
import com.xiangxun.xacity.ui.occupy.detail.SearchManageDetailActivity;
import com.xiangxun.xacity.view.MsgToast;
import com.xiangxun.xacity.view.TitleView;
import com.xiangxun.xacity.view.xlistView.XListView;
import com.xiangxun.xacity.view.xlistView.XListView.IXListViewListener;

/**
 * @package: com.xiangxun.xacity.ui.occupy
 * @ClassName: SearchManageActivity.java
 * @Description: 查询管理页面
 * @author: HanGJ
 * @date: 2016-1-26 下午2:45:54
 */
public class SearchManageListActivity extends BaseActivity implements OnClickListener, IXListViewListener, OnItemClickListener {
	private static int listState = -1;
	private TitleView titleView;
	private XListView mXListView;
	private ViewFlipper mVF;
	private String title;
	private int childAt;
	private int currentPage = 1;
	private int PageSize = 10;
	private int totalSize = 0;
	public List<OccupySearchBean> data = new ArrayList<OccupySearchBean>();
	private OccupySearchAdapter adapter;
	/*************** 搜索字段 ****************/
	private String pname = "";
	private String applicantId = "";
	private String builderId = "";
	private String starttime = "";
	private String endtime = "";
	private String roadId = "";
	private String operator = "";
	private String type = "";
	private String occupyType = "";
	private String inspector = "";
	private String roadname = "";
	private String beforeStatus = "";
	private String note = "";
	private String afterStatus = "";

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {

		@Override
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			stopXListView();
			switch (msg.what) {
			case ConstantStatus.getOccupySearchListSuccess:
				List<OccupySearchBean> occupySearchBeans = (List<OccupySearchBean>) msg.obj;
				if (occupySearchBeans != null) {
					setOccupySearchData(occupySearchBeans);
				} else {
					if (data.size() <= 0) {
						mVF.setDisplayedChild(2);
					}
				}
				break;
			case ConstantStatus.getOccupySearchListFailed:
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

	protected void setOccupySearchData(List<OccupySearchBean> occupySearchBeans) {
		switch (listState) {
		case ConstantStatus.listStateFirst:
			data.clear();
			data.addAll(occupySearchBeans);
			adapter.setData(data, occupySearchBeans.size());
			mXListView.setAdapter(adapter);
			break;
		case ConstantStatus.listStateRefresh:
			data.clear();
			data.addAll(occupySearchBeans);
			adapter.setData(data, occupySearchBeans.size());
			mXListView.setAdapter(adapter);
			break;
		case ConstantStatus.listStateLoadMore:
			data.addAll(occupySearchBeans);
			adapter.setData(data, occupySearchBeans.size());
			break;
		}
		mVF.setDisplayedChild(0);
		totalSize = occupySearchBeans.size();
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
		adapter = new OccupySearchAdapter(this, childAt, mXListView);
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
		if (childAt == 0) {// 延期项目查询网络请求
			DcNetWorkUtils.getOccupyRoadSpendList(this, handler, account, password, currentPage + "", PageSize + "", pname, applicantId, builderId, starttime, endtime, roadId, operator, type, occupyType);
		} else if (childAt == 1) {// 勘察信息查询网络请求
			DcNetWorkUtils.getOccupyRoadInspectList(this, handler, account, password, currentPage + "", PageSize + "", pname, applicantId, builderId, starttime, endtime, roadId, inspector, type, occupyType);
		} else if (childAt == 2) {// 更改状态查询网络请求
			DcNetWorkUtils.getOccupyStateSearchList(this, handler, account, password, currentPage + "", PageSize + "", pname, roadname, applicantId, beforeStatus, starttime, endtime, note, operator, type, builderId, afterStatus);
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
			Intent intent = new Intent(this, SearchProjectInfoActivity.class);
			intent.putExtra("title", title);
			intent.putExtra("childAt", childAt);
			startActivityForResult(intent, 134);
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
		OccupySearchBean bean = (OccupySearchBean) adapter.getItem(position - 1);
		Intent intent = new Intent(this, SearchManageDetailActivity.class);
		intent.putExtra("bean", (Serializable) bean);
		intent.putExtra("childAt", childAt);
		intent.putExtra("title", title);
		startActivity(intent);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null && requestCode == 134) {
			switch (resultCode) {
			case 0:
				occupyType = data.getStringExtra("occupyType");
				pname = data.getStringExtra("pname");
				roadId = data.getStringExtra("roadId");
				applicantId = data.getStringExtra("applicantId");
				builderId = data.getStringExtra("builderId");
				operator = data.getStringExtra("operator");
				type = data.getStringExtra("type");
				starttime = data.getStringExtra("starttime");
				endtime = data.getStringExtra("endtime");
				break;
			case 1:
				occupyType = data.getStringExtra("occupyType");
				pname = data.getStringExtra("pname");
				roadId = data.getStringExtra("roadId");
				applicantId = data.getStringExtra("applicantId");
				builderId = data.getStringExtra("builderId");
				operator = data.getStringExtra("operator");
				type = data.getStringExtra("type");
				starttime = data.getStringExtra("starttime");
				endtime = data.getStringExtra("endtime");
				break;
			case 2:
				occupyType = data.getStringExtra("occupyType");
				roadname = data.getStringExtra("roadname");
				applicantId = data.getStringExtra("applicantId");
				beforeStatus = data.getStringExtra("beforeStatus");
				operator = data.getStringExtra("operator");
				type = data.getStringExtra("type");
				builderId = data.getStringExtra("builderId");
				afterStatus = data.getStringExtra("afterStatus");
				starttime = data.getStringExtra("starttime");
				endtime = data.getStringExtra("endtime");
				break;
			}
			this.data.clear();
			listState = ConstantStatus.listStateFirst;
			currentPage = 1;
			RequestList();
		}
	}

}
