package com.xiangxun.xacity.ui.patrol.detail;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.xiangxun.xacity.R;
import com.xiangxun.xacity.adapter.PhoneViewAdapter;
import com.xiangxun.xacity.adapter.WorkOrderAdapter;
import com.xiangxun.xacity.app.BaseActivity;
import com.xiangxun.xacity.app.XiangXunApplication;
import com.xiangxun.xacity.bean.LoginData.Children;
import com.xiangxun.xacity.bean.LoginData.ChildrenRoot;
import com.xiangxun.xacity.bean.ResponseResultBeans.AttBean;
import com.xiangxun.xacity.bean.ResponseResultBeans.DisposeBean;
import com.xiangxun.xacity.bean.ResponseResultBeans.GetWorkOrderDetail;
import com.xiangxun.xacity.bean.ResponseResultBeans.WorkOrderBean;
import com.xiangxun.xacity.bean.ResultBeans.PublishOrderData;
import com.xiangxun.xacity.bean.ResultBeans.UpLoadResule;
import com.xiangxun.xacity.common.ConstantStatus;
import com.xiangxun.xacity.request.DcNetWorkUtils;
import com.xiangxun.xacity.ui.patrol.PersonnelLocationActivity;
import com.xiangxun.xacity.ui.patrol.WorkOrderProgressActivity;
import com.xiangxun.xacity.ui.patrol.WorkOrderReportActivity;
import com.xiangxun.xacity.ui.patrol.WorkOrderStatesActivity;
import com.xiangxun.xacity.utils.ShareDataUtils;
import com.xiangxun.xacity.utils.SharedPreferencesKeys;
import com.xiangxun.xacity.utils.Tools;
import com.xiangxun.xacity.view.LoadDialog;
import com.xiangxun.xacity.view.MsgDialog;
import com.xiangxun.xacity.view.MsgToast;
import com.xiangxun.xacity.view.TabTopView;
import com.xiangxun.xacity.view.TitleView;
import com.xiangxun.xacity.view.photoview.HackyViewPager;

/**
 * @package: com.xiangxun.xacity.ui.patrol.detail
 * @ClassName: WorkOrderDetailActivity.java
 * @Description: 工单查询详情页面
 * @author: HanGJ
 * @date: 2016-3-10 下午1:47:32
 */
public class WorkOrderDetailActivity extends BaseActivity implements OnClickListener, OnPageChangeListener {
	private TitleView titleView;
	private TabTopView tabtopView;
	private ViewFlipper mVF;
	private LoadDialog loadDialog;
	private String id = "";
	private ListView mListView;
	private ViewPager mViewPager = null;
	private LinearLayout mLlPlacePointLayout = null;
	private LinearLayout ll_complaintTel = null;
	private GetWorkOrderDetail orderBean = null;
	private List<AttBean> mPictures = null;
	private ImageView[] mPoints = null;

	private int mCurrentPage = 0;
	private WorkOrderAdapter adapter;
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
	private TextView tvContent15;
	private TextView tvContent16;
	private TextView tvContent17;
	private TextView tvContent18;
	private TextView tvContent19;
	private TextView tvContent20;
	private TextView tvContent21;
	private TextView tvContent22;
	private TextView tvContent23;
	private TextView tvContent24;
	private TextView tvContent25;

	private ViewFlipper mVFStatue;
	// 待办
	private TextView wait_new_change;
	private TextView wait_order_back;
	private TextView wait_order_banl;
	private TextView wait_order_return;
	private TextView wait_order_desc;
	private TextView wait_order_next;
	// 核实
	private TextView check_order_new;
	private TextView check_order_banl;
	// 催办
	private TextView reminder_order_new;
	private TextView reminder_order_modify;
	private TextView reminder_order_back;
	private TextView reminder_order_close;
	private TextView reminder_order_desc;
	// 已办
	private TextView already_order_diffic;
	private TextView already_order_close;
	private TextView already_order_again;
	// 闭合
	private TextView close_order_new;
	private TextView close_order_diffic;
	private TextView close_order_again;
	private TextView close_order_verfy;
	// 改派
	private TextView change_order_diffic;
	private MsgDialog msgDialog;
	private WorkOrderBean manageBean = null;
	private short isClick = -1;
	private String hintContent = "";
	private String RoleMobile = "二级管理员";
	private String mobileRoles = "";
	private boolean isRefresh = false;
	private String userId = "";

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			loadDialog.dismiss();
			switch (msg.what) {
			case ConstantStatus.getPatrolSearchListSuccess:
				orderBean = (GetWorkOrderDetail) msg.obj;
				if (orderBean != null) {
					if (orderBean.result != null) {
						setWorkOrderDetailData(orderBean.result.order);
					}
					setDisposeDetailData(orderBean.result.disposes);
					setWorkOrderAttDetailData(orderBean.result.files);
				}
				break;
			case ConstantStatus.getPatrolSearchListFailed:
				MsgToast.geToast().setMsg("数据加载失败,请稍后重试!");
				break;
			case ConstantStatus.NetWorkError:
				MsgToast.geToast().setMsg("网络异常,请稍后重试!");
				break;
			case ConstantStatus.PublishOrderSuccess:
				isRefresh = true;
				MsgToast.geToast().setMsg(hintContent + "成功");
				loadDialog.setTitle("重新加载数据中,请稍后...");
				RequestList();
				break;
			case ConstantStatus.PublishOrderFailed:
				MsgToast.geToast().setMsg(hintContent + "失败,请重新上传~");
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.work_order_detail_layout);
		initView();
		initData();
		initListener();
	}

	@Override
	public void initView() {
		titleView = (TitleView) findViewById(R.id.comment_title);
		tabtopView = (TabTopView) findViewById(R.id.tabtopView);
		mVF = (ViewFlipper) findViewById(R.id.viewFlipper);
		mVFStatue = (ViewFlipper) findViewById(R.id.order_state);
		mListView = (ListView) findViewById(R.id.xlistview);
		mViewPager = (HackyViewPager) findViewById(R.id.vp_phone_view);
		mLlPlacePointLayout = (LinearLayout) findViewById(R.id.ll_place_point);
		ll_complaintTel = (LinearLayout) findViewById(R.id.ll_complaintTel);
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
		tvContent15 = (TextView) findViewById(R.id.tv_content15);
		tvContent16 = (TextView) findViewById(R.id.tv_content16);
		tvContent17 = (TextView) findViewById(R.id.tv_content17);
		tvContent18 = (TextView) findViewById(R.id.tv_content18);
		tvContent19 = (TextView) findViewById(R.id.tv_content19);
		tvContent20 = (TextView) findViewById(R.id.tv_content20);
		tvContent21 = (TextView) findViewById(R.id.tv_content21);
		tvContent22 = (TextView) findViewById(R.id.tv_content22);
		tvContent23 = (TextView) findViewById(R.id.tv_content23);
		tvContent24 = (TextView) findViewById(R.id.tv_content24);
		tvContent25 = (TextView) findViewById(R.id.tv_content25);
		// 待办
		wait_new_change = (TextView) findViewById(R.id.wait_new_change);
		wait_order_back = (TextView) findViewById(R.id.wait_order_back);
		wait_order_banl = (TextView) findViewById(R.id.wait_order_banl);
		wait_order_return = (TextView) findViewById(R.id.wait_order_return);
		wait_order_desc = (TextView) findViewById(R.id.wait_order_desc);
		wait_order_next = (TextView) findViewById(R.id.wait_order_next);
		// 核实
		check_order_new = (TextView) findViewById(R.id.check_order_new);
		check_order_banl = (TextView) findViewById(R.id.check_order_banl);
		// 催办
		reminder_order_new = (TextView) findViewById(R.id.reminder_order_new);
		reminder_order_modify = (TextView) findViewById(R.id.reminder_order_modify);
		reminder_order_back = (TextView) findViewById(R.id.reminder_order_back);
		reminder_order_close = (TextView) findViewById(R.id.reminder_order_close);
		reminder_order_desc = (TextView) findViewById(R.id.reminder_order_desc);
		// 已办
		already_order_diffic = (TextView) findViewById(R.id.already_order_diffic);
		already_order_close = (TextView) findViewById(R.id.already_order_close);
		already_order_again = (TextView) findViewById(R.id.already_order_again);
		// 闭合
		close_order_new = (TextView) findViewById(R.id.close_order_new);
		close_order_diffic = (TextView) findViewById(R.id.close_order_diffic);
		close_order_again = (TextView) findViewById(R.id.close_order_again);
		close_order_verfy = (TextView) findViewById(R.id.close_order_verfy);
		// 改派
		change_order_diffic = (TextView) findViewById(R.id.change_order_diffic);
	}

	@Override
	public void initData() {
		id = getIntent().getStringExtra("id");
		userId = ShareDataUtils.getSharedStringData(XiangXunApplication.getInstance(), SharedPreferencesKeys.USERID);
		mobileRoles = ShareDataUtils.getSharedStringData(this, SharedPreferencesKeys.MOBILEROLES);
		titleView.setTitle("工单详情");
		tabtopView.OnClickLeftTab();
		tabtopView.setTabText("工单详情", "处理记录");
		tabtopView.setTabMiddleText("附件列表");
		tabtopView.OnClickLeftTab();
		loadDialog = new LoadDialog(this);
		msgDialog = new MsgDialog(this);
		loadDialog.setTitle("正在加载数据,请稍后...");
		adapter = new WorkOrderAdapter(this);
		RequestList();
	}

	private void RequestList() {
		loadDialog.show();
		DcNetWorkUtils.getWorkOrderDetail(this, handler, account, password, id);
	}

	@Override
	public void initListener() {
		tabtopView.OnClickLeftTabOnClickListener(this);
		tabtopView.OnClickMiddleTabOnClickListener(this);
		tabtopView.OnClickRightTabOnClickListener(this);
		mViewPager.setOnPageChangeListener(this);
		wait_order_desc.setOnClickListener(this);
		reminder_order_desc.setOnClickListener(this);
		reminder_order_modify.setOnClickListener(this);
		reminder_order_new.setOnClickListener(this);
		wait_new_change.setOnClickListener(this);
		check_order_new.setOnClickListener(this);
		close_order_new.setOnClickListener(this);
		wait_order_next.setOnClickListener(this);
		reminder_order_back.setOnClickListener(this);
		wait_order_back.setOnClickListener(this);
		wait_order_banl.setOnClickListener(this);
		check_order_banl.setOnClickListener(this);
		reminder_order_close.setOnClickListener(this);
		already_order_close.setOnClickListener(this);
		already_order_diffic.setOnClickListener(this);
		close_order_diffic.setOnClickListener(this);
		change_order_diffic.setOnClickListener(this);
		already_order_again.setOnClickListener(this);
		close_order_again.setOnClickListener(this);
		close_order_verfy.setOnClickListener(this);
		wait_order_return.setOnClickListener(this);
		ll_complaintTel.setOnClickListener(this);
		msgDialog.setButRightListener(this);
		titleView.setLeftBackOneListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isRefresh) {
					setResult(ConstantStatus.listStateRefresh);
				}
				onBackPressed();
			}
		});
	}

	protected void setWorkOrderAttDetailData(List<AttBean> attList) {
		if (attList != null && attList.size() > 0) {
			this.mPictures = attList;
			setPlacePoint();
			setViewData();
		}
	}

	protected void setDisposeDetailData(List<DisposeBean> dispose) {
		if (dispose != null && dispose.size() > 0) {
			adapter.setData(dispose, dispose.size());
			mListView.setAdapter(adapter);
		}
	}

	protected void setWorkOrderDetailData(WorkOrderBean manageBean) {
		this.manageBean = manageBean;
		setDataRoles(manageBean.workEventState, manageBean.workIsNodus, manageBean.workIsIssued, manageBean.issuedRegId);
		setViewRoles(manageBean.workEventState);
		tvContent01.setText(Tools.isEmpty(manageBean.workOrderCode));
		tvContent02.setText(Tools.isEmpty(manageBean.complaintName));
		tvContent03.setText(Tools.isEmpty(manageBean.createTime));
		tvContent04.setText(Tools.isEmpty(manageBean.complaintTel));
		tvContent05.setText(Tools.isEmpty(manageBean.workEventSource));
		tvContent06.setText(Tools.isEmpty(manageBean.workEventType));
		tvContent07.setText(Tools.isEmpty(manageBean.dutyOrgType));
		tvContent08.setText(Tools.isEmpty(manageBean.dutyOrgCode));
		tvContent09.setText(manageBean.workDays + " 天");
		tvContent10.setText(Tools.isEmpty(manageBean.workEventPointExplain));
		tvContent11.setText(Tools.isEmpty(manageBean.workEventState));
		tvContent12.setText(Tools.isEmpty(manageBean.workOrderText));
		tvContent13.setText(Tools.isEmpty(manageBean.workDisposeRequest));
		tvContent14.setText(Tools.isEmpty(manageBean.siteDescription));
		if ("0".equals(manageBean.workIsNodus)) {
			tvContent15.setText("否");
		} else {
			tvContent15.setText("是");
		}
		if ("0".equals(manageBean.workIsIssued)) {
			tvContent16.setText("否");
		} else {
			tvContent16.setText("是");
		}
		tvContent17.setText(Tools.isEmpty(manageBean.workOrderRemark));
		tvContent18.setText(Tools.isEmpty(manageBean.satisfactionDegree));
		if ("CLSX001".equals(manageBean.dealLimit)) {
			tvContent19.setText("符合");
		} else if ("CLSX002".equals(manageBean.dealLimit)) {
			tvContent19.setText("不符合");
		} else {
			tvContent19.setText("");
		}
		tvContent20.setText(Tools.isEmpty(manageBean.employeeId));
		tvContent21.setText(Tools.isEmpty(manageBean.workBackExplain));
		tvContent22.setText(Tools.isEmpty(manageBean.updateTime));
		tvContent23.setText(Tools.isEmpty(manageBean.workAssess));
		if ("0".equals(manageBean.checkStatus)) {
			tvContent24.setText("审核不通过");
		} else if ("1".equals(manageBean.checkStatus)) {
			tvContent24.setText("审核通过");
		} else {
			tvContent24.setText("未审核");
		}
		tvContent25.setText(Tools.isEmpty(manageBean.checkDescription));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.toptabview_left_rlyout:
			mVF.setDisplayedChild(0);
			tabtopView.OnClickLeftTab();
			if (orderBean == null) {
				RequestList();
			}
			break;
		case R.id.toptabview_middle_rlyout:
			mVF.setDisplayedChild(1);
			tabtopView.OnClickMiddleTab();
			if (orderBean == null) {
				RequestList();
			}
			break;
		case R.id.toptabview_right_rlyout:
			mVF.setDisplayedChild(2);
			tabtopView.OnClickRightTab();
			if (orderBean == null) {
				RequestList();
			}
			break;
		case R.id.reminder_order_desc:
		case R.id.wait_order_desc:
			Intent intent = new Intent(this, WorkOrderReportActivity.class);
			intent.putExtra("isFlag", 1);
			intent.putExtra("title", "现场说明");
			intent.putExtra("bean", (Serializable) manageBean);
			isRefresh = true;
			startActivity(intent);
			break;
		case R.id.reminder_order_modify:
			Intent intentModify = new Intent(this, WorkOrderReportActivity.class);
			intentModify.putExtra("isFlag", 2);
			intentModify.putExtra("title", "工单修改");
			isRefresh = true;
			intentModify.putExtra("bean", (Serializable) manageBean);
			startActivity(intentModify);
			break;
		case R.id.reminder_order_new:
		case R.id.wait_new_change:
		case R.id.check_order_new:
		case R.id.close_order_new:
			Intent intentProgress = new Intent(this, WorkOrderProgressActivity.class);
			intentProgress.putExtra("bean", (Serializable) manageBean);
			isRefresh = true;
			startActivity(intentProgress);
			break;
		case R.id.wait_order_next:
			Intent intentNext = new Intent(this, PersonnelLocationActivity.class);
			intentNext.putExtra("isFlag", 1);
			intentNext.putExtra("title", "选择下发人");
			isRefresh = true;
			intentNext.putExtra("bean", (Serializable) manageBean);
			startActivity(intentNext);
			break;
		case R.id.reminder_order_back:
		case R.id.wait_order_back:
			isClick = 1;
			hintContent = "工单反馈";
			msgDialog.setTiele("工单反馈");
			msgDialog.setMsg("是否申请反馈\"" + manageBean.workOrderCode + "\"工单");
			msgDialog.show();
			break;
		case R.id.dia_tv_but2:
			msgDialog.dismiss();
			loadDialog.setTitle("正在提交数据,请稍后...");
			loadDialog.show();
			PublishOrderData publishInfo = new PublishOrderData();
			publishInfo.workOrderId = manageBean.workOrderId;
			publishInfo.files = new ArrayList<UpLoadResule>();
			if (isClick == 1) {
				publishInfo.workEventState = "GDZT04";
			} else if (isClick == 2) {
				publishInfo.workEventState = "GDZT05";
			} else if (isClick == 3) {
				publishInfo.workEventState = "GDZT06";
			}
			DcNetWorkUtils.publishOrderUrl(this, handler, publishInfo, account, password);
			break;
		case R.id.wait_order_banl:
		case R.id.check_order_banl:
			isClick = 2;
			hintContent = "工单办理";
			msgDialog.setTiele("工单办理");
			msgDialog.setMsg("是否设置办理\"" + manageBean.workOrderCode + "\"工单");
			msgDialog.show();
			break;
		case R.id.reminder_order_close:
		case R.id.already_order_close:
			isClick = 3;
			hintContent = "工单闭合";
			msgDialog.setTiele("工单闭合");
			msgDialog.setMsg("是否闭合\"" + manageBean.workOrderCode + "\"工单");
			msgDialog.show();
			break;
		case R.id.already_order_diffic:
		case R.id.close_order_diffic:
		case R.id.change_order_diffic:
			Intent intentNodus = new Intent(this, WorkOrderStatesActivity.class);
			intentNodus.putExtra("title", "设置难点");
			intentNodus.putExtra("isFlag", 1);
			isRefresh = true;
			intentNodus.putExtra("bean", (Serializable) manageBean);
			startActivity(intentNodus);
			break;
		case R.id.already_order_again:
		case R.id.close_order_again:
			Intent intentAgain = new Intent(this, WorkOrderStatesActivity.class);
			intentAgain.putExtra("title", "重新办理");
			intentAgain.putExtra("isFlag", 2);
			isRefresh = true;
			intentAgain.putExtra("bean", (Serializable) manageBean);
			startActivity(intentAgain);
			break;
		case R.id.close_order_verfy:
			Intent intentVerfy = new Intent(this, WorkOrderStatesActivity.class);
			intentVerfy.putExtra("title", "工单审核");
			intentVerfy.putExtra("isFlag", 3);
			isRefresh = true;
			intentVerfy.putExtra("bean", (Serializable) manageBean);
			startActivity(intentVerfy);
			break;
		case R.id.wait_order_return:
			Intent intentReturn = new Intent(this, WorkOrderStatesActivity.class);
			intentReturn.putExtra("title", "工单退回");
			intentReturn.putExtra("isFlag", 4);
			isRefresh = true;
			intentReturn.putExtra("bean", (Serializable) manageBean);
			startActivity(intentReturn);
			break;
		case R.id.ll_complaintTel:
			String phone = tvContent04.getText().toString().trim();
			if (phone != null && phone.length() > 0) {
				Intent intentCall = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
				intentCall.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intentCall);
			}
			break;
		}
	}

	private void setViewData() {
		mViewPager.setAdapter(new PhoneViewAdapter(this, mPictures));
		mViewPager.setCurrentItem(mCurrentPage);
	}

	private void setDataRoles(String state, String workIsNodus, String workIsIssued, String updateId) {
		List<ChildrenRoot> children = new ArrayList<ChildrenRoot>();
		Object obj = ShareDataUtils.getObject(this, "menu_patrol");
		children.addAll((List) obj);
		if (children.size() > 0) {
			for (int i = 0; i < children.size(); i++) {
				ChildrenRoot childrenRoot = children.get(i);
				if (childrenRoot != null && "工单查询".equals(childrenRoot.getName())) {
					List<Children> childrens = childrenRoot.getChildren();
					if (childrens != null && childrens.size() > 0) {
						for (int j = 0; j < childrens.size(); j++) {
							Children children2 = childrens.get(j);
							if ("催办".equals(state)) {
								if (children2 != null && "新增进展".equals(children2.getName())) {
									reminder_order_new.setVisibility(View.VISIBLE);
								} else if (children2 != null && "工单修改".equals(children2.getName())) {
									reminder_order_modify.setVisibility(View.VISIBLE);
								} else if (children2 != null && "工单反馈".equals(children2.getName())) {
									if ("0".equals(workIsIssued)) {
										reminder_order_back.setVisibility(View.GONE);
									} else if ("1".equals(workIsIssued) && userId.equals(updateId)) {
										reminder_order_back.setVisibility(View.VISIBLE);
									}
								} else if (children2 != null && "闭合".equals(children2.getName())) {
									reminder_order_close.setVisibility(View.VISIBLE);
								} else if (children2 != null && "现场说明".equals(children2.getName())) {
									reminder_order_desc.setVisibility(View.VISIBLE);
								}
							} else if ("待办".equals(state)) {
								if (children2 != null && "新增进展".equals(children2.getName())) {
									wait_new_change.setVisibility(View.VISIBLE);
								} else if (children2 != null && "办理".equals(children2.getName())) {
									if ("1".equals(workIsIssued) && mobileRoles.contains(RoleMobile)) {
										wait_order_banl.setVisibility(View.GONE);
									} else {
										wait_order_banl.setVisibility(View.VISIBLE);
									}
								} else if (children2 != null && "工单反馈".equals(children2.getName())) {
									if ("0".equals(workIsIssued)) {
										wait_order_back.setVisibility(View.GONE);
									} else if ("1".equals(workIsIssued) && userId.equals(updateId)) {
										wait_order_back.setVisibility(View.VISIBLE);
									}
								} else if (children2 != null && "退回".equals(children2.getName())) {
									if ("1".equals(workIsIssued) && mobileRoles.contains(RoleMobile)) {
										wait_order_return.setVisibility(View.GONE);
									} else {
										wait_order_return.setVisibility(View.VISIBLE);
									}
								} else if (children2 != null && "现场说明".equals(children2.getName())) {
									wait_order_desc.setVisibility(View.VISIBLE);
								} else if (children2 != null && "工单下发".equals(children2.getName())) {
									if ("0".equals(workIsIssued)) {
										wait_order_next.setVisibility(View.VISIBLE);
									} else {
										wait_order_next.setVisibility(View.GONE);
									}
								}
							} else if ("核实".equals(state)) {
								if (children2 != null && "新增进展".equals(children2.getName())) {
									check_order_new.setVisibility(View.VISIBLE);
								} else if (children2 != null && "办理".equals(children2.getName())) {
									check_order_banl.setVisibility(View.VISIBLE);
								}
							} else if ("已办".equals(state)) {
								if (children2 != null && "设置难点".equals(children2.getName())) {
									if ("0".equals(workIsNodus)) {
										already_order_diffic.setVisibility(View.VISIBLE);
									} else {
										already_order_diffic.setVisibility(View.GONE);
									}
								} else if (children2 != null && "闭合".equals(children2.getName())) {
									already_order_close.setVisibility(View.VISIBLE);
								} else if (children2 != null && "重新办理".equals(children2.getName())) {
									already_order_again.setVisibility(View.VISIBLE);
								}
							} else if ("闭合".equals(state)) {
								if (children2 != null && "新增进展".equals(children2.getName())) {
									close_order_new.setVisibility(View.VISIBLE);
								} else if (children2 != null && "设置难点".equals(children2.getName())) {
									if ("0".equals(workIsNodus)) {
										close_order_diffic.setVisibility(View.VISIBLE);
									} else {
										close_order_diffic.setVisibility(View.GONE);
									}
								} else if (children2 != null && "重新办理".equals(children2.getName())) {
									close_order_again.setVisibility(View.VISIBLE);
								} else if (children2 != null && "工单审核".equals(children2.getName())) {
									close_order_verfy.setVisibility(View.VISIBLE);
								}
							} else if ("改派".equals(state)) {
								if (children2 != null && "设置难点".equals(children2.getName())) {
									if ("0".equals(workIsNodus)) {
										change_order_diffic.setVisibility(View.VISIBLE);
									} else {
										change_order_diffic.setVisibility(View.GONE);
									}
								} else {
									mVFStatue.setVisibility(View.GONE);
								}
							}
						}
					}
				}
			}
		}
	}

	private void setViewRoles(String state) {
		if ("派发".equals(state)) {
		} else if ("催办".equals(state)) {
			mVFStatue.setDisplayedChild(2);
			mVFStatue.setVisibility(View.VISIBLE);
		} else if ("待办".equals(state)) {
			mVFStatue.setDisplayedChild(0);
			mVFStatue.setVisibility(View.VISIBLE);
		} else if ("核实".equals(state)) {
			mVFStatue.setDisplayedChild(1);
			mVFStatue.setVisibility(View.VISIBLE);
		} else if ("已办".equals(state)) {
			mVFStatue.setDisplayedChild(3);
			mVFStatue.setVisibility(View.VISIBLE);
		} else if ("闭合".equals(state)) {
			mVFStatue.setDisplayedChild(4);
			mVFStatue.setVisibility(View.VISIBLE);
		} else if ("已审".equals(state)) {
			mVFStatue.setVisibility(View.GONE);
		} else if ("改派".equals(state)) {
			mVFStatue.setDisplayedChild(5);
			mVFStatue.setVisibility(View.VISIBLE);
		}
	}

	private void setPlacePoint() {
		mLlPlacePointLayout.removeAllViews();
		mPoints = new ImageView[mPictures.size()];
		for (int i = 0, size = mPictures.size(); i < size; i++) {
			mPoints[i] = new ImageView(this);
			mPoints[i].setImageResource(R.drawable.circle_white);
			mPoints[i].setLayoutParams(new LayoutParams(20, 20));
			mPoints[i].setPadding(5, 5, 5, 5);
			mLlPlacePointLayout.addView(mPoints[i]);
		}
		mPoints[0].setImageResource(R.drawable.circle_orange);
		if (mPictures.size() <= 1) {
			mLlPlacePointLayout.setVisibility(View.INVISIBLE);
		} else {
			mLlPlacePointLayout.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int position) {
		for (int i = 0, size = mPictures.size(); i < size; i++) {
			if (position == i) {
				mPoints[i].setImageResource(R.drawable.circle_orange);
			} else {
				mPoints[i].setImageResource(R.drawable.circle_white);
			}
		}
	}

	@Override
	protected void onResume() {
		if (isRefresh) {
			loadDialog.setTitle("重新加载数据中,请稍后...");
			RequestList();
		}
		super.onResume();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (isRefresh) {
				setResult(ConstantStatus.listStateRefresh);
			}
		}
		return super.onKeyDown(keyCode, event);
	}

}
