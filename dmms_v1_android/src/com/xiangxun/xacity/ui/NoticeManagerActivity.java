package com.xiangxun.xacity.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ViewFlipper;

import com.xiangxun.xacity.R;
import com.xiangxun.xacity.adapter.NoticeAdapter;
import com.xiangxun.xacity.app.BaseActivity;
import com.xiangxun.xacity.bean.ResponseResultBeans.GetNoticeList;
import com.xiangxun.xacity.bean.ResponseResultBeans.NoticeBean;
import com.xiangxun.xacity.common.ConstantStatus;
import com.xiangxun.xacity.request.DcNetWorkUtils;
import com.xiangxun.xacity.view.MsgToast;
import com.xiangxun.xacity.view.TitleView;
import com.xiangxun.xacity.view.xlistView.XListView;
import com.xiangxun.xacity.view.xlistView.XListView.IXListViewListener;

/**
 * @package: com.xiangxun.xacity.ui
 * @ClassName: NoticeManagerActivity.java
 * @Description: 通知公告
 * @author: HanGJ
 * @param <E>
 * @date: 2015-12-30 下午5:19:55
 */
public class NoticeManagerActivity extends BaseActivity implements IXListViewListener, OnItemClickListener {
	private static int listState = -1;
	private int currentPage = 1;
	private int PageSize = 10;
	private int totalSize = 0;
	private TitleView titleView;
	private XListView mXListView;
	private ViewFlipper mVF;
	private NoticeAdapter mRAdapter;
	public List<NoticeBean> data = new ArrayList<NoticeBean>();
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			stopXListView();
			switch (msg.what) {
			case ConstantStatus.getNoticeBeanListSuccess:
				GetNoticeList list = (GetNoticeList) msg.obj;
				if (list != null && list.result != null) {
					setListData(list.result);
				} else {
					if (data.size() <= 0) {
						mVF.setDisplayedChild(2);
					}
				}
				break;
			case ConstantStatus.getNoticeBeanListFailed:
				if (data.size() <= 0) {
					mVF.setDisplayedChild(2);
				}
				break;
			case ConstantStatus.NetWorkError:
				mVF.setDisplayedChild(3);
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notice_list_layout);
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
		titleView.setTitle("通知公告");
		listState = ConstantStatus.listStateFirst;
		mRAdapter = new NoticeAdapter(this, mXListView);
		RequestList();
	}

	@Override
	public void initListener() {
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
		DcNetWorkUtils.getNoticeList(this, account, password, currentPage, PageSize, handler);
	}

	private void setListData(List<NoticeBean> niticeBeans) {
		switch (listState) {
		case ConstantStatus.listStateFirst:
			data.clear();
			data.addAll(niticeBeans);
			mRAdapter.setData(data, niticeBeans.size());
			mXListView.setAdapter(mRAdapter);
			break;
		case ConstantStatus.listStateRefresh:
			data.clear();
			data.addAll(niticeBeans);
			mRAdapter.setData(data, niticeBeans.size());
			mXListView.setAdapter(mRAdapter);
			break;
		case ConstantStatus.listStateLoadMore:
			data.addAll(niticeBeans);
			mRAdapter.setData(data, niticeBeans.size());
			break;
		}
		totalSize = niticeBeans.size();
		mVF.setDisplayedChild(0);

		// 没有加载到数据
		if (data.size() == 0) {
			mVF.setDisplayedChild(2);
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
		NoticeBean item = mRAdapter.getItem(position - 1);
		Intent intent = new Intent(this, NoticeDetialActivity.class);
		intent.putExtra("noticeBean", (Serializable) item);
		startActivity(intent);
	}
}
