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
import com.xiangxun.xacity.adapter.PurchaseManageAdapter;
import com.xiangxun.xacity.app.BaseActivity;
import com.xiangxun.xacity.bean.LoginData.Children;
import com.xiangxun.xacity.bean.LoginData.ChildrenRoot;
import com.xiangxun.xacity.bean.ResponseResultBeans.PurchaseOrderManageBean;
import com.xiangxun.xacity.common.ConstantStatus;
import com.xiangxun.xacity.request.DcNetWorkUtils;
import com.xiangxun.xacity.ui.materiel.detail.PurchaseManageDetailActivity;
import com.xiangxun.xacity.utils.ShareDataUtils;
import com.xiangxun.xacity.view.MsgToast;
import com.xiangxun.xacity.view.TabTopView;
import com.xiangxun.xacity.view.TitleView;
import com.xiangxun.xacity.view.xlistView.XListView;
import com.xiangxun.xacity.view.xlistView.XListView.IXListViewListener;

/**
 * @package: com.xiangxun.xacity.ui.materiel
 * @ClassName: PurchaseManageListActivity.java
 * @Description: 物料管理-->采购管理页面
 * @author: HanGJ
 * @date: 2016-1-25 下午4:31:16
 */
public class PurchaseManageListActivity extends BaseActivity implements OnClickListener, IXListViewListener, OnItemClickListener {
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

	private String menuid1 = "1412271706275843ab82e3000cd63006";
	private String menuid2 = "141229173843673891172b0ce0b80f52";
	private String menuid3 = "";
	/************* 搜索字段 **************/
	private String code = "";
	private String projectName = "";
	private String month = "";
	private String type = "";
	private String supplier = "";
	private String name = "";
	private String commAddress = "";
	private String principal = "";
	public List<PurchaseOrderManageBean> data = new ArrayList<PurchaseOrderManageBean>();
	private PurchaseManageAdapter adapter;
	public List<PurchaseOrderManageBean> dataTodo = new ArrayList<PurchaseOrderManageBean>();
	private PurchaseManageAdapter adapterTodo;

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
						setPurchaseOrderTodoData(materielDemandBeans);
					} else {
						if (dataTodo.size() <= 0) {
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
		tabtopView.setTabText("历史任务", "待办任务");
		tabtopView.OnClickLeftTab();
		listState = ConstantStatus.listStateFirst;
		listStateTodo = ConstantStatus.listStateFirst;
		getMenuId();
		adapter = new PurchaseManageAdapter(this, childAt, mXListView);
		adapterTodo = new PurchaseManageAdapter(this, childAt, mXListView_Todo);
		RequestListHistory();
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
		if (childAt == 0) {// 需求单管理网络请求
			DcNetWorkUtils.getMaterielDemanPurchasesList(this, handler, 1, account, password, currentPage + "", PageSize + "", menuid1, code, projectName, month, type);
		} else if (childAt == 1) {// 采购单管理网络请求
			DcNetWorkUtils.getPurchasesOrderManageList(this, handler, 1, account, password, currentPage + "", PageSize + "", menuid2, projectName, month, type, supplier);
		} else if (childAt == 2) {// 外部供应商管理网络请求
			DcNetWorkUtils.getMaterielSupplierManageList(this, handler, 1, account, password, currentPage + "", PageSize + "", menuid3, name, commAddress, principal);
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
		if (childAt == 0) {// 需求单管理网络请求
			DcNetWorkUtils.getMaterielDemanPurchasesList(this, handler, 0, account, password, currentPage_todo + "", PageSize + "", menuid1, code, projectName, month, type);
		} else if (childAt == 1) {// 采购单管理网络请求
			DcNetWorkUtils.getPurchasesOrderManageList(this, handler, 0, account, password, currentPage_todo + "", PageSize + "", menuid2, projectName, month, type, supplier);
		} else if (childAt == 2) {// 外部供应商管理网络请求
			DcNetWorkUtils.getMaterielSupplierManageList(this, handler, 0, account, password, currentPage_todo + "", PageSize + "", menuid3, name, commAddress, principal);
		}
	}

	@Override
	public void initListener() {
		titleView.setRightOnClickListener(this);
		mXListView.setPullLoadEnable(true);
		mXListView.setXListViewListener(this);
		mXListView.setOnItemClickListener(this);
		mXListView_Todo.setPullLoadEnable(true);
		mXListView_Todo.setXListViewListener(this);
		mXListView_Todo.setOnItemClickListener(this);
		tabtopView.OnClickLeftTabOnClickListener(this);
		tabtopView.OnClickRightTabOnClickListener(this);
		titleView.setLeftBackOneListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

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

	protected void setPurchaseOrderTodoData(List<PurchaseOrderManageBean> beans) {
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
			Intent intent = new Intent(this, PurchaseManageSearchActivity.class);
			intent.putExtra("title", title);
			intent.putExtra("childAt", childAt);
			startActivityForResult(intent, 121);
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
							if (children2 != null && "需求单管理".equals(children2.getName())) {
								menuid1 = children2.getId();
							} else if (children2 != null && "采购单管理".equals(children2.getName())) {
								menuid2 = children2.getId();
							} else if (children2 != null && "外部供应商管理".equals(children2.getName())) {
								menuid3 = children2.getId();
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
		PurchaseOrderManageBean bean = null;
		int disabled = 1;
		switch (parent.getId()) {
		case R.id.xlistview_history:
			disabled = 1;
			bean = adapter.getItem(position - 1);
			break;
		case R.id.xlistview_todo:
			disabled = 0;
			bean = adapterTodo.getItem(position - 1);
			break;
		}
		Intent intent = new Intent(this, PurchaseManageDetailActivity.class);
		intent.putExtra("bean", (Serializable) bean);
		intent.putExtra("childAt", childAt);
		intent.putExtra("disabled", disabled);
		if (childAt == 0) {
			intent.putExtra("menuid", menuid1);
		} else {
			intent.putExtra("menuid", menuid2);
		}
		intent.putExtra("title", title);
		startActivity(intent);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null && requestCode == 121) {
			switch (resultCode) {
			case 0:
				month = data.getStringExtra("month");
				code = data.getStringExtra("code");
				projectName = data.getStringExtra("projectName");
				type = data.getStringExtra("type");
				if (onTabTopLeft) {
					listState = ConstantStatus.listStateRefresh;
					RequestListHistory();
				} else {
					listStateTodo = ConstantStatus.listStateRefresh;
					RequestListTodo();
				}
				break;
			case 1:
				month = data.getStringExtra("month");
				supplier = data.getStringExtra("supplier");
				projectName = data.getStringExtra("projectName");
				type = data.getStringExtra("type");
				break;
			case 2:
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
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}
