package com.xiangxun.xacity.ui.materiel;

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
import com.xiangxun.xacity.adapter.ProjectSuperviseAdapter;
import com.xiangxun.xacity.app.BaseActivity;
import com.xiangxun.xacity.bean.LoginData.Children;
import com.xiangxun.xacity.bean.LoginData.ChildrenRoot;
import com.xiangxun.xacity.bean.ResponseResultBeans.PurchaseOrderManageBean;
import com.xiangxun.xacity.common.ConstantStatus;
import com.xiangxun.xacity.request.DcNetWorkUtils;
import com.xiangxun.xacity.ui.materiel.detail.ProjectManageDetailActivity;
import com.xiangxun.xacity.ui.materiel.detail.ProjectManageTodoDetailActivity;
import com.xiangxun.xacity.ui.materiel.detail.PurchaseManageDetailActivity;
import com.xiangxun.xacity.utils.ShareDataUtils;
import com.xiangxun.xacity.view.MsgToast;
import com.xiangxun.xacity.view.TabTopView;
import com.xiangxun.xacity.view.TitleView;
import com.xiangxun.xacity.view.xlistView.XListView;
import com.xiangxun.xacity.view.xlistView.XListView.IXListViewListener;

/**
 * @package: com.xiangxun.xacity.ui.materiel
 * @ClassName: ProjectManageListActivity.java
 * @Description: 物料管理-->工程监管页面
 * @author: HanGJ
 * @date: 2016-1-25 下午4:33:51
 */
public class ProjectManageListActivity extends BaseActivity implements OnClickListener, IXListViewListener, OnItemClickListener {
	private static int listState = -1;
	private static int listStateTodo = -1;
	private TitleView titleView;
	private boolean ifRefreshHistory = true; // 历史是否刷新开关
	private boolean ifRefreshTodo = true; // 待办是否刷新开关
	private XListView mXListView, mXListView_Todo;
	private ViewFlipper mVF, viewFlipper_todo, viewFlipper_history;
	private TabTopView tabtopView;
	private String title;
	private int childAt;
	private int currentPage = 1;
	private int PageSize = 10;
	private int totalSize = 0;
	private int totalSizeTodo = 0;
	private int currentPage_todo = 1;
	private boolean onTabTopLeft = true; // 是否切换历史任务
	private String menuid1 = "";
	private String menuid2 = "";
	/************* 搜索字段 **************/
	private String code = "";
	private String name = "";
	private String starttime = "";
	private String place = "";
	private String status = "";
	private String planendtime = "";
	private String commAddress = "";
	private String principal = "";
	public List<PurchaseOrderManageBean> data = new ArrayList<PurchaseOrderManageBean>();
	private ProjectSuperviseAdapter adapter;
	public List<PurchaseOrderManageBean> dataTodo = new ArrayList<PurchaseOrderManageBean>();
	private ProjectSuperviseAdapter adapterTodo;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			stopXListView();
			switch (msg.what) {
			case ConstantStatus.getMaterialDemanListSuccess:
				List<PurchaseOrderManageBean> materielDemandBeans = (List<PurchaseOrderManageBean>) msg.obj;
				if (onTabTopLeft) {
					if (materielDemandBeans != null) {
						setPurchaseOrderData(materielDemandBeans);
					} else {
						if (data.size() <= 0) {
							viewFlipper_history.setDisplayedChild(2);
						}
					}
				} else {
					if (materielDemandBeans != null) {
						setMaterielTodoData(materielDemandBeans);
					} else {
						if (data.size() <= 0) {
							viewFlipper_todo.setDisplayedChild(2);
						}
					}
				}
				break;
			case ConstantStatus.getMaterialDemanListFailed:
				if (onTabTopLeft) {
					if (data.size() <= 0) {
						viewFlipper_history.setDisplayedChild(2);
					}
				} else {
					if (dataTodo.size() <= 0) {
						viewFlipper_todo.setDisplayedChild(2);
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
		setContentView(R.layout.materiel_list_layout);
		initView();
		initData();
		initListener();
	}

	@Override
	public void initView() {
		titleView = (TitleView) findViewById(R.id.comment_title);
		tabtopView = (TabTopView) findViewById(R.id.tabtopView);
		mXListView = (XListView) findViewById(R.id.xlistview_history);
		mXListView_Todo = (XListView) findViewById(R.id.xlistview_todo);
		mVF = (ViewFlipper) findViewById(R.id.viewFlipper);
		viewFlipper_history = (ViewFlipper) findViewById(R.id.viewFlipper_history);
		viewFlipper_todo = (ViewFlipper) findViewById(R.id.viewFlipper_todo);
	}

	@Override
	public void initData() {
		title = getIntent().getStringExtra("title");
		childAt = getIntent().getIntExtra("childAt", 0);
		titleView.setTitle(title);
		if (childAt == 0) {
			tabtopView.setTabText("历史任务", "待办任务");
			tabtopView.OnClickLeftTab();
			listStateTodo = ConstantStatus.listStateFirst;
		} else {
			tabtopView.setVisibility(View.GONE);
		}
		listState = ConstantStatus.listStateFirst;
		getMenuId();
		adapter = new ProjectSuperviseAdapter(this, childAt, mXListView);
		adapterTodo = new ProjectSuperviseAdapter(this, childAt, mXListView_Todo);
		RequestListHistory();
	}

	@Override
	public void initListener() {
		mXListView.setPullLoadEnable(true);
		mXListView.setXListViewListener(this);
		mXListView.setOnItemClickListener(this);
		mXListView_Todo.setPullLoadEnable(true);
		mXListView_Todo.setXListViewListener(this);
		mXListView_Todo.setOnItemClickListener(this);
		tabtopView.OnClickLeftTabOnClickListener(this);
		tabtopView.OnClickRightTabOnClickListener(this);
		titleView.setRightOnClickListener(this);
		titleView.setLeftBackOneListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

	}

	private void RequestListHistory() {
		switch (listState) {
		case ConstantStatus.listStateFirst:
			if (ifRefreshHistory && data.size() == 0) {
				viewFlipper_history.setDisplayedChild(1);
			} else {
				viewFlipper_history.setDisplayedChild(0);
			}
			break;
		case ConstantStatus.listStateRefresh:
			viewFlipper_history.setDisplayedChild(0);
			break;
		case ConstantStatus.listStateLoadMore:
			viewFlipper_history.setDisplayedChild(0);
			break;
		}
		if (childAt == 0) {// 项目管理网络请求
			DcNetWorkUtils.getMaterielProjectManageList(this, handler, 1, account, password, currentPage + "", PageSize + "", menuid1, code, name, starttime, place, status, planendtime);
		} else if (childAt == 1) {// 供应商监管网络请求
			DcNetWorkUtils.getMaterielSupplierList(this, handler, 1, account, password, currentPage + "", PageSize + "", menuid2, name, commAddress, principal);
		}
	}

	private void RequestListTodo() {
		switch (listStateTodo) {
		case ConstantStatus.listStateFirst:
			if (ifRefreshTodo && dataTodo.size() == 0) {
				viewFlipper_todo.setDisplayedChild(1);
			} else {
				viewFlipper_todo.setDisplayedChild(0);
			}
			break;
		case ConstantStatus.listStateRefresh:
			viewFlipper_todo.setDisplayedChild(0);
			break;
		case ConstantStatus.listStateLoadMore:
			viewFlipper_todo.setDisplayedChild(0);
			break;
		}
		if (childAt == 0) {// 项目管理网络请求
			DcNetWorkUtils.getMaterielProjectManageList(this, handler, 0, account, password, currentPage_todo + "", PageSize + "", menuid1, code, name, starttime, place, status, planendtime);
		} else if (childAt == 1) {// 供应商监管网络请求
			DcNetWorkUtils.getMaterielSupplierList(this, handler, 0, account, password, currentPage_todo + "", PageSize + "", menuid2, name, commAddress, principal);
		}
	}

	protected void setPurchaseOrderData(List<PurchaseOrderManageBean> beans) {
		switch (listState) {
		case ConstantStatus.listStateFirst:
			data.clear();
			data.addAll(beans);
			adapter.setData(data, beans.size());
			mXListView.setAdapter(adapter);
			break;
		case ConstantStatus.listStateRefresh:
			data.clear();
			data.addAll(beans);
			adapter.setData(data, beans.size());
			mXListView.setAdapter(adapter);
			break;
		case ConstantStatus.listStateLoadMore:
			data.addAll(beans);
			adapter.setData(data, beans.size());
			break;
		}
		viewFlipper_history.setDisplayedChild(0);
		totalSize = beans.size();
		// 没有加载到数据
		if (data.size() == 0) {
			viewFlipper_history.setDisplayedChild(2);
		}
	}

	protected void setMaterielTodoData(List<PurchaseOrderManageBean> beans) {
		switch (listStateTodo) {
		case ConstantStatus.listStateFirst:
			dataTodo.clear();
			dataTodo.addAll(beans);
			adapterTodo.setData(dataTodo, beans.size());
			mXListView_Todo.setAdapter(adapterTodo);
			break;
		case ConstantStatus.listStateRefresh:
			dataTodo.clear();
			dataTodo.addAll(beans);
			adapterTodo.setData(dataTodo, beans.size());
			mXListView_Todo.setAdapter(adapterTodo);
			break;
		case ConstantStatus.listStateLoadMore:
			dataTodo.addAll(beans);
			adapterTodo.setData(dataTodo, beans.size());
			break;
		}
		viewFlipper_todo.setDisplayedChild(0);
		totalSizeTodo = beans.size();
		// 没有加载到数据
		if (dataTodo.size() == 0) {
			viewFlipper_todo.setDisplayedChild(2);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.xw_share:
			Intent intent = new Intent(this, ProjectManageSearchActivity.class);
			intent.putExtra("title", title);
			intent.putExtra("childAt", childAt);
			startActivityForResult(intent, 122);
			break;
		case R.id.toptabview_left_rlyout:
			mVF.setDisplayedChild(0);
			onTabTopLeft = true;
			tabtopView.OnClickLeftTab();
			if (data.size() <= 0) {
				RequestListHistory();
			}
			break;
		case R.id.toptabview_right_rlyout:
			mVF.setDisplayedChild(1);
			onTabTopLeft = false;
			tabtopView.OnClickRightTab();
			if (dataTodo.size() <= 0) {
				RequestListTodo();
			}
			break;
		}
	}

	private void getMenuId() {
		List<ChildrenRoot> children = new ArrayList<ChildrenRoot>();
		Object obj = ShareDataUtils.getObject(this, "menu_materiel");
		children.addAll((List) obj);
		if (children.size() > 0) {
			for (int i = 0; i < children.size(); i++) {
				ChildrenRoot childrenRoot = children.get(i);
				if (childrenRoot != null) {
					List<Children> childrens = childrenRoot.getChildren();
					if (childrens != null && childrens.size() > 0) {
						for (int j = 0; j < childrens.size(); j++) {
							Children children2 = childrens.get(j);
							if (children2 != null && "项目管理".equals(children2.getName())) {
								menuid1 = children2.getId();
							} else if (children2 != null && "供应商管理".equals(children2.getName())) {
								menuid2 = children2.getId();
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void onRefresh(View v) {
		switch (v.getId()) {
		case R.id.xlistview_history:
			currentPage = 1;
			listState = ConstantStatus.listStateRefresh;
			RequestListHistory();
			break;
		case R.id.xlistview_todo:
			currentPage_todo = 1;
			listStateTodo = ConstantStatus.listStateRefresh;
			RequestListTodo();
			break;
		}
	}

	@Override
	public void onLoadMore(View v) {
		switch (v.getId()) {
		case R.id.xlistview_history:
			if (totalSize < PageSize) {
				MsgToast.geToast().setMsg("已经是最后一页");
				mXListView.removeFooterView(mXListView.mFooterView);
			} else {
				currentPage++;
				listState = ConstantStatus.listStateLoadMore;
				RequestListHistory();
			}
			break;
		case R.id.xlistview_todo:
			if (totalSizeTodo < PageSize) {
				MsgToast.geToast().setMsg("已经是最后一页");
				mXListView_Todo.removeFooterView(mXListView.mFooterView);
			} else {
				currentPage_todo++;
				listStateTodo = ConstantStatus.listStateLoadMore;
				RequestListTodo();
			}
			break;
		}
	}

	// xLisView 停止
	private void stopXListView() {
		mXListView.stopRefresh();
		mXListView.stopLoadMore();
		mXListView_Todo.stopRefresh();
		mXListView_Todo.stopLoadMore();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (childAt == 1) {
			PurchaseOrderManageBean beanSupp = adapter.getItem(position - 1);
			Intent intent = new Intent(this, PurchaseManageDetailActivity.class);
			intent.putExtra("bean", (Serializable) beanSupp);
			intent.putExtra("childAt", 2);
			intent.putExtra("isFlag", getIntent().getIntExtra("isFlag", 0));
			intent.putExtra("title", title);
			startActivity(intent);
		} else {
			PurchaseOrderManageBean bean = null;
			Intent intent = null;
			switch (parent.getId()) {
			case R.id.xlistview_history:
				bean = adapter.getItem(position - 1);
				intent = new Intent(this, ProjectManageDetailActivity.class);
				break;
			case R.id.xlistview_todo:
				intent = new Intent(this, ProjectManageTodoDetailActivity.class);
				bean = adapterTodo.getItem(position - 1);
				break;
			}
			intent.putExtra("bean", (Serializable) bean);
			intent.putExtra("childAt", childAt);
			intent.putExtra("title", title);
			startActivityForResult(intent, 405);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null && requestCode == 122) {
			switch (resultCode) {
			case 0:
				name = data.getStringExtra("name");
				code = data.getStringExtra("code");
				place = data.getStringExtra("place");
				starttime = data.getStringExtra("starttime");
				planendtime = data.getStringExtra("planendtime");
				status = data.getStringExtra("status");
				break;
			case 1:
				name = data.getStringExtra("name");
				commAddress = data.getStringExtra("commAddress");
				principal = data.getStringExtra("principal");
				break;
			}
			if (onTabTopLeft) {
				this.data.clear();
				listState = ConstantStatus.listStateFirst;
				currentPage = 1;
				RequestListHistory();
			} else {
				this.dataTodo.clear();
				listStateTodo = ConstantStatus.listStateFirst;
				currentPage_todo = 1;
				RequestListTodo();
			}
		} else if(requestCode == 405 && resultCode == ConstantStatus.listStateRefresh){
			this.dataTodo.clear();
			listStateTodo = ConstantStatus.listStateFirst;
			currentPage_todo = 1;
			RequestListTodo();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
