package com.xiangxun.xacity.ui.occupy;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.ViewFlipper;

import com.xiangxun.xacity.R;
import com.xiangxun.xacity.adapter.OccupyStatisicsTypeAdapter;
import com.xiangxun.xacity.app.BaseActivity;
import com.xiangxun.xacity.bean.ResultBeans.OccupyStatistics;
import com.xiangxun.xacity.common.ConstantStatus;
import com.xiangxun.xacity.request.DcNetWorkUtils;
import com.xiangxun.xacity.view.MsgToast;
import com.xiangxun.xacity.view.TitleView;

/**
 * @package: com.xiangxun.xacity.ui.occupy
 * @ClassName: OccuoyCountSearchActivity.java
 * @Description: 挖占统计页面
 * @author: HanGJ
 * @date: 2016-4-27 上午8:41:50
 */
public class OccupyStatisticsActivity extends BaseActivity implements
		OnClickListener {
	private TitleView titleView;
	private ListView mXListView;
	private ViewFlipper mVF;
	/*************** 搜索字段 ****************/
	private String queryType = "area";
	private String startTime = "";
	private String endTime = "";
	private String type = "";
	private String faceType = "";
	private String managearea = "";
	private OccupyStatisicsTypeAdapter adapter;
	public List<OccupyStatistics> data = new ArrayList<OccupyStatistics>();
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ConstantStatus.getOccupyListSuccess:
				List<OccupyStatistics> occupyStatistics = (List<OccupyStatistics>) msg.obj;
				if (occupyStatistics != null) {
					setOccupyManageData(occupyStatistics);
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
		setContentView(R.layout.occupy_statis_list_layout);
		initView();
		initData();
		initListener();
	}

	@Override
	public void initView() {
		titleView = (TitleView) findViewById(R.id.comment_title);
		mXListView = (ListView) findViewById(R.id.xlistview);
		mVF = (ViewFlipper) findViewById(R.id.viewFlipper);
	}

	@Override
	public void initData() {
		titleView.setTitle("挖占统计");
		adapter = new OccupyStatisicsTypeAdapter(this);
		RequestList();
	}

	private void RequestList() {
		mVF.setDisplayedChild(1);
		DcNetWorkUtils.getOccupyStatisticsList(this, handler, account,
				password, queryType, faceType, startTime, endTime, managearea,
				type);
	}

	@Override
	public void initListener() {
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
			startActivityForResult(new Intent(this,
					OccupyStatisticsSearchActivity.class), 133);
			break;
		}
	}

	protected void setOccupyManageData(List<OccupyStatistics> manageBeans) {
		data.clear();
		data.addAll(manageBeans);
		adapter.setData(data, queryType);
		mXListView.setAdapter(adapter);
		mVF.setDisplayedChild(0);
		// 没有加载到数据
		if (data.size() == 0) {
			mVF.setDisplayedChild(2);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null && requestCode == 133) {
			if (resultCode == RESULT_OK) {
				queryType = data.getStringExtra("queryType");
				managearea = data.getStringExtra("managearea");
				type = data.getStringExtra("type");
				faceType = data.getStringExtra("faceType");
				startTime = data.getStringExtra("startTime");
				endTime = data.getStringExtra("endTime");
			}
			this.data.clear();
			RequestList();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}
