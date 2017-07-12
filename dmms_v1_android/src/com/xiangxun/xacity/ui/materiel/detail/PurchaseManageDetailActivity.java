package com.xiangxun.xacity.ui.materiel.detail;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.xiangxun.xacity.R;
import com.xiangxun.xacity.adapter.PurchaseOrderAdapter;
import com.xiangxun.xacity.app.BaseActivity;
import com.xiangxun.xacity.bean.ResponseResultBeans.PurchaseOrderManageBean;
import com.xiangxun.xacity.common.ConstantStatus;
import com.xiangxun.xacity.request.DcNetWorkUtils;
import com.xiangxun.xacity.utils.MyUtils;
import com.xiangxun.xacity.utils.Tools;
import com.xiangxun.xacity.view.LoadDialog;
import com.xiangxun.xacity.view.MsgToast;
import com.xiangxun.xacity.view.TabTopView;
import com.xiangxun.xacity.view.TitleView;

/**
 * @package: com.xiangxun.xacity.ui.materiel.detail
 * @ClassName: PurchaseManageDetailActivity.java
 * @Description: 采购管理详情历史任务页面
 * @author: HanGJ
 * @date: 2016-2-26 上午10:23:16
 */
public class PurchaseManageDetailActivity extends BaseActivity implements OnClickListener {
	private TitleView titleView;
	private String title;
	private int childAt;
	private TabTopView tabtopView;
	private ViewFlipper mVF;
	private ListView mListView, mListView01;
	private boolean isMiddleFlag = false;
	private String menuid = "";
	private String id = "";
	private LoadDialog loadDialog;
	private List<PurchaseOrderManageBean> projectInfos = new ArrayList<PurchaseOrderManageBean>();
	private PurchaseOrderAdapter adapter;
	private List<PurchaseOrderManageBean> projectRecords = new ArrayList<PurchaseOrderManageBean>();
	private PurchaseOrderAdapter adapter01;
	private LinearLayout ll_view01;
	private LinearLayout ll_view02;
	private LinearLayout ll_view03;
	private LinearLayout ll_view04;
	private LinearLayout ll_view05;
	private LinearLayout ll_view06;
	/**************** 标题控件 **********************/
	private TextView tvTitle01;
	private TextView tvTitle02;
	private TextView tvTitle03;
	private TextView tvTitle04;
	private TextView tvTitle05;
	private TextView tvTitle06;
	private TextView tvTitle07;
	private TextView tvTitle08;
	private TextView tvTitle09;
	private TextView tvTitle10;
	private TextView tvTitle11;
	private TextView tvTitle12;
	private TextView tvTitle13;
	private TextView tvTitle14;
	/**************** 内容控件 **********************/
	private TextView tvContent01;
	private TextView tvContent02;
	private TextView tvContent03;
	private TextView tvContent04;
	private TextView tvContent05;
	private TextView tvContent06;
	private TextView tvContent07;
	private TextView tvContent08;
	private TextView tvContent09;
	private TextView tvContent10;
	private TextView tvContent11;
	private TextView tvContent12;
	private TextView tvContent13;
	private TextView tvContent14;
	private PurchaseOrderManageBean orderManageBean;
	private int disabled = 0;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {

		@Override
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			loadDialog.dismiss();
			switch (msg.what) {
			case ConstantStatus.getMaterialDemanListSuccess:
				List<PurchaseOrderManageBean> projectInfos = (List<PurchaseOrderManageBean>) msg.obj;
				if (projectInfos != null && projectInfos.size() > 0) {
					mVF.setDisplayedChild(1);
					setProjectListData(projectInfos);
				} else {
					mVF.setDisplayedChild(3);
				}
				break;
			case ConstantStatus.GetLocalSuccess:
				List<PurchaseOrderManageBean> projects = (List<PurchaseOrderManageBean>) msg.obj;
				if (projects != null && projects.size() > 0) {
					setProjectListData(projects);
				} else {
					mVF.setDisplayedChild(3);
				}
				break;
			case ConstantStatus.getMaterialListSuccess:
				PurchaseOrderManageBean demandBean = (PurchaseOrderManageBean) msg.obj;
				if (demandBean != null) {
					setViewData(demandBean);
				} else {
					setViewData(orderManageBean);
				}
				break;
			case ConstantStatus.getMaterialDemanListFailed:
				MsgToast.geToast().setMsg("数据加载失败~");
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
		setContentView(R.layout.purchase_manage_detail_layout);
		initView();
		initData();
		initListener();
	}

	protected void setProjectListData(List<PurchaseOrderManageBean> beans) {
		if (childAt == 0) {
			projectInfos.clear();
			projectInfos.addAll(beans);
			adapter.setData(projectInfos, beans.size());
			mListView.setAdapter(adapter);
			// 没有加载到数据
			if (projectInfos.size() == 0) {
				mVF.setDisplayedChild(3);
			}
		} else {
			if (isMiddleFlag) {
				projectInfos.clear();
				projectInfos.addAll(beans);
				adapter.setData(projectInfos, beans.size());
				mListView.setAdapter(adapter);
				// 没有加载到数据
				if (projectInfos.size() == 0) {
					mVF.setDisplayedChild(3);
				}
			} else {
				projectRecords.clear();
				projectRecords.addAll(beans);
				adapter01.setData(projectRecords, beans.size());
				mListView01.setAdapter(adapter01);
				// 没有加载到数据
				if (projectRecords.size() == 0) {
					mVF.setDisplayedChild(3);
				}
			}
		}
	}

	@Override
	public void initView() {
		titleView = (TitleView) findViewById(R.id.comment_title);
		tabtopView = (TabTopView) findViewById(R.id.tabtopView);
		mVF = (ViewFlipper) findViewById(R.id.viewFlipper);
		mListView = (ListView) findViewById(R.id.xlistview);
		mListView01 = (ListView) findViewById(R.id.xlistview01);
		ll_view01 = (LinearLayout) findViewById(R.id.ll_other_view01);
		ll_view02 = (LinearLayout) findViewById(R.id.ll_other_view02);
		ll_view03 = (LinearLayout) findViewById(R.id.ll_other_view03);
		ll_view04 = (LinearLayout) findViewById(R.id.ll_other_view04);
		ll_view05 = (LinearLayout) findViewById(R.id.ll_other_view05);
		ll_view06 = (LinearLayout) findViewById(R.id.ll_other_view06);
		/*************************************************/
		tvTitle01 = (TextView) findViewById(R.id.tv_title01);
		tvTitle02 = (TextView) findViewById(R.id.tv_title02);
		tvTitle03 = (TextView) findViewById(R.id.tv_title03);
		tvTitle04 = (TextView) findViewById(R.id.tv_title04);
		tvTitle05 = (TextView) findViewById(R.id.tv_title05);
		tvTitle06 = (TextView) findViewById(R.id.tv_title06);
		tvTitle07 = (TextView) findViewById(R.id.tv_title07);
		tvTitle08 = (TextView) findViewById(R.id.tv_title08);
		tvTitle09 = (TextView) findViewById(R.id.tv_title09);
		tvTitle10 = (TextView) findViewById(R.id.tv_title10);
		tvTitle11 = (TextView) findViewById(R.id.tv_title11);
		tvTitle12 = (TextView) findViewById(R.id.tv_title12);
		tvTitle13 = (TextView) findViewById(R.id.tv_title13);
		tvTitle14 = (TextView) findViewById(R.id.tv_title14);
		/**********************************************/
		tvContent01 = (TextView) findViewById(R.id.tv_content01);
		tvContent02 = (TextView) findViewById(R.id.tv_content02);
		tvContent03 = (TextView) findViewById(R.id.tv_content03);
		tvContent04 = (TextView) findViewById(R.id.tv_content04);
		tvContent05 = (TextView) findViewById(R.id.tv_content05);
		tvContent06 = (TextView) findViewById(R.id.tv_content06);
		tvContent07 = (TextView) findViewById(R.id.tv_content07);
		tvContent08 = (TextView) findViewById(R.id.tv_content08);
		tvContent09 = (TextView) findViewById(R.id.tv_content09);
		tvContent10 = (TextView) findViewById(R.id.tv_content10);
		tvContent11 = (TextView) findViewById(R.id.tv_content11);
		tvContent12 = (TextView) findViewById(R.id.tv_content12);
		tvContent13 = (TextView) findViewById(R.id.tv_content13);
		tvContent14 = (TextView) findViewById(R.id.tv_content14);
	}

	@Override
	public void initData() {
		title = getIntent().getStringExtra("title");
		childAt = getIntent().getIntExtra("childAt", 0);
		disabled = getIntent().getIntExtra("disabled", 0);
		menuid = getIntent().getStringExtra("menuid");
		loadDialog = new LoadDialog(this);
		orderManageBean = (PurchaseOrderManageBean) getIntent().getSerializableExtra("bean");
		id = orderManageBean.id;
		loadDialog.setTitle("正在加载数据,请稍后...");
		titleView.setTitle(title + "--详情");
		if (childAt == 0) {
			tabtopView.setTabText("需求单详情", "需求单信息");
			loadDialog.show();
			DcNetWorkUtils.getMaterielDetail(this, handler, account, password, id);
		} else if (childAt == 1) {
			tabtopView.setTabText("采购单详情", "审核记录");
			tabtopView.setTabMiddleText("发货信息");
			setViewData(orderManageBean);
		} else {
			tabtopView.setVisibility(View.GONE);
			setViewData(orderManageBean);
		}
		tabtopView.OnClickLeftTab();
		adapter = new PurchaseOrderAdapter(this, childAt, false);
		adapter01 = new PurchaseOrderAdapter(this, childAt, true);
	}

	@Override
	public void initListener() {
		tabtopView.OnClickLeftTabOnClickListener(this);
		tabtopView.OnClickMiddleTabOnClickListener(this);
		tabtopView.OnClickRightTabOnClickListener(this);
		titleView.setLeftBackOneListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}

	private void setViewData(PurchaseOrderManageBean demandBean) {
		if (childAt == 0) {
			ll_view01.setVisibility(View.VISIBLE);
			tvTitle01.setText("项目名称: ");
			tvTitle02.setText(Html.fromHtml("计划月份: "));
			tvTitle03.setText(Html.fromHtml("项目编号: "));
			tvTitle04.setText(Html.fromHtml("计划编号: "));
			tvTitle05.setText(Html.fromHtml("操作账号: "));
			tvTitle06.setText("添加时间: ");
			tvTitle07.setText("需求时限: ");
			tvTitle08.setText(Html.fromHtml("状&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;态: "));
			tvTitle09.setText(Html.fromHtml("备&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;注: "));

			tvContent01.setText(Tools.isEmpty(demandBean.projectName));
			tvContent02.setText(Tools.isEmpty(demandBean.monthStr));
			tvContent03.setText(Tools.isEmpty(demandBean.projectCode));
			tvContent04.setText(Tools.isEmpty(demandBean.planCode));
			tvContent05.setText(Tools.isEmpty(demandBean.operator));
			tvContent06.setText(Tools.isEmpty(demandBean.addtime));
			tvContent07.setText(demandBean.deadtime != null && demandBean.deadtime.length() > 0 ? Tools.isEmpty(MyUtils.StringToDate(demandBean.deadtime)) : "");
			tvContent08.setText(Tools.isEmpty(demandBean.statusHtml));
			tvContent09.setText(Tools.isEmpty(demandBean.note));
		} else if (childAt == 1) {
			ll_view01.setVisibility(View.VISIBLE);
			ll_view02.setVisibility(View.VISIBLE);
			ll_view03.setVisibility(View.VISIBLE);
			ll_view04.setVisibility(View.VISIBLE);
			ll_view05.setVisibility(View.VISIBLE);
			ll_view06.setVisibility(View.VISIBLE);
			tvTitle01.setText("项目名称: ");
			tvTitle02.setText("计划月份: ");
			tvTitle03.setText("项目编号: ");
			tvTitle04.setText(Html.fromHtml("计划编号: "));
			tvTitle05.setText("物资类别: ");
			tvTitle06.setText(Html.fromHtml("规&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;格: "));
			tvTitle07.setText(Html.fromHtml("需&#160;&#160;求&#160;&#160;量: "));
			tvTitle08.setText(Html.fromHtml("供&#160;&#160;应&#160;&#160;商: "));
			tvTitle09.setText("需求时限: ");
			tvTitle10.setText("供应商类别: ");
			tvTitle11.setText("添加账号: ");
			tvTitle12.setText("添加时间: ");
			tvTitle13.setText(Html.fromHtml("状&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;态: "));
			tvTitle14.setText(Html.fromHtml("备&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;注: "));

			tvContent01.setText(Tools.isEmpty(demandBean.projectName));
			tvContent02.setText(Tools.isEmpty(demandBean.monthStr));
			tvContent03.setText(Tools.isEmpty(demandBean.projectCode));
			tvContent04.setText(Tools.isEmpty(demandBean.planCode));
			tvContent05.setText(Tools.isEmpty(demandBean.type));
			tvContent06.setText(Tools.isEmpty(demandBean.specification));
			tvContent07.setText(Tools.isEmpty(demandBean.amount + "米(m)"));
			tvContent08.setText(Tools.isEmpty(demandBean.supplier));
			tvContent09.setText(demandBean.deadtime != null && demandBean.deadtime.length() > 0 ? Tools.isEmpty(MyUtils.StringToDate(demandBean.deadtime)) : "");
			tvContent10.setText(Tools.isEmpty(demandBean.supplierType));
			tvContent11.setText(Tools.isEmpty(demandBean.operator));
			tvContent12.setText(Tools.isEmpty(demandBean.addtime));
			tvContent13.setText(Tools.isEmpty(demandBean.statusHtml));
			tvContent14.setText(Tools.isEmpty(demandBean.remark));
		} else {
			ll_view01.setVisibility(View.VISIBLE);
			tvTitle01.setText(Html.fromHtml("名&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;称: "));
			tvTitle02.setText(Html.fromHtml("类&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;别: "));
			tvTitle03.setText(Html.fromHtml("通讯地址: "));
			tvTitle04.setText(Html.fromHtml("联系电话: "));
			tvTitle05.setText(Html.fromHtml("负&#160;&#160;责&#160;&#160;人: "));
			tvTitle06.setText("负责人电话: ");
			tvTitle07.setText("操作账号: ");
			tvTitle08.setText(Html.fromHtml("操作时间: "));
			tvTitle09.setText(Html.fromHtml("备&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;注: "));

			tvContent01.setText(Tools.isEmpty(demandBean.name));
			tvContent02.setText(Tools.isEmpty(demandBean.type));
			tvContent03.setText(Tools.isEmpty(demandBean.commAddress));
			tvContent04.setText(Tools.isEmpty(demandBean.phone));
			tvContent05.setText(Tools.isEmpty(demandBean.principal));
			tvContent06.setText(Tools.isEmpty(demandBean.mobile));
			tvContent07.setText(Tools.isEmpty(demandBean.operator));
			tvContent08.setText(Tools.isEmpty(demandBean.addtime));
			tvContent09.setText(Tools.isEmpty(demandBean.note));

			if (getIntent().getIntExtra("isFlag", 0) == 1) {
				ll_view02.setVisibility(View.VISIBLE);
				tvTitle10.setText(Html.fromHtml("产品类别: "));
				tvContent10.setText(Tools.isEmpty(demandBean.productName));
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.toptabview_left_rlyout:
			mVF.setDisplayedChild(0);
			tabtopView.OnClickLeftTab();
			break;
		case R.id.toptabview_middle_rlyout:
			isMiddleFlag = true;
			loadDialog.show();
			mVF.setDisplayedChild(1);
			tabtopView.OnClickMiddleTab();
			RequestList();
			break;
		case R.id.toptabview_right_rlyout:
			isMiddleFlag = false;
			loadDialog.show();
			mVF.setDisplayedChild(2);
			tabtopView.OnClickRightTab();
			RequestList();
			break;
		}
	}

	private void RequestList() {
		if (childAt == 0) {
			DcNetWorkUtils.getDemanOrderDetailList(this, handler, account, password, menuid, id, disabled + "");
		} else {
			if (isMiddleFlag) {// 采购单发货信息
				DcNetWorkUtils.getPurchaseOrderDeliverList(this, handler, account, password, id, disabled + "");
			} else {// 采购单审核记录
				DcNetWorkUtils.getPurchaseOrderVerifyList(this, handler, account, password, id, disabled + "");
			}
		}
	}

}
