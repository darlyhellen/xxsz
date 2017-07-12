package com.xiangxun.xacity.ui.materiel.detail;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.xiangxun.xacity.R;
import com.xiangxun.xacity.adapter.ProjectManageAdapter;
import com.xiangxun.xacity.app.BaseActivity;
import com.xiangxun.xacity.bean.LoginData.Children;
import com.xiangxun.xacity.bean.LoginData.ChildrenRoot;
import com.xiangxun.xacity.bean.LoginData.Roles;
import com.xiangxun.xacity.bean.ResponseResultBeans.ProjectInfo;
import com.xiangxun.xacity.bean.ResponseResultBeans.PurchaseOrderManageBean;
import com.xiangxun.xacity.common.ConstantStatus;
import com.xiangxun.xacity.request.DcNetWorkUtils;
import com.xiangxun.xacity.utils.ShareDataUtils;
import com.xiangxun.xacity.utils.Tools;
import com.xiangxun.xacity.view.LoadDialog;
import com.xiangxun.xacity.view.MsgToast;
import com.xiangxun.xacity.view.ProjectVerifyDialog;
import com.xiangxun.xacity.view.ProjectVerifyDialog.SelectItemClick;
import com.xiangxun.xacity.view.TabTopView;
import com.xiangxun.xacity.view.TitleView;

/**
 * @package: com.xiangxun.xacity.ui.materiel.detail
 * @ClassName: ProjectManageDetailActivity.java
 * @Description: 工程监管详情待办任务页面
 * @author: HanGJ
 * @date: 2016-2-26 下午2:51:43
 */
public class ProjectManageTodoDetailActivity extends BaseActivity implements OnClickListener, SelectItemClick {
	private TitleView titleView;
	private String title;
	private int childAt;
	private TabTopView tabtopView;
	private ViewFlipper mVF;
	private ListView mListView, mListView01;
	private LinearLayout tabBottomView;
	private TextView tabtopview_left_text;
	private TextView tabtopview_right_text;
	private TextView bottomview_submit_text;
	private boolean isMiddleFlag = false;
	private String code = "";
	private String id = "";
	private String type = "";
	private LoadDialog loadDialog;
	private List<ProjectInfo> projectInfos = new ArrayList<ProjectInfo>();
	private ProjectManageAdapter adapter;
	private List<ProjectInfo> projectRecords = new ArrayList<ProjectInfo>();
	private ProjectManageAdapter adapter01;
	private LinearLayout ll_view01;
	private LinearLayout ll_view02;
	private LinearLayout ll_view03;
	private LinearLayout ll_view04;
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

	private ProjectVerifyDialog dialog;
	private boolean isRefresh = false;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			loadDialog.dismiss();
			switch (msg.what) {
			case ConstantStatus.getMaterialDemanListSuccess:
				List<ProjectInfo> projectInfos = (List<ProjectInfo>) msg.obj;
				if (projectInfos != null && projectInfos.size() > 0) {
					mVF.setDisplayedChild(1);
					setProjectListData(projectInfos);
				} else {

				}
				break;
			case ConstantStatus.GetLocalSuccess:
				List<ProjectInfo> projects = (List<ProjectInfo>) msg.obj;
				if (projects != null && projects.size() > 0) {
					setProjectListData(projects);
				}
				break;
			case ConstantStatus.SubmitVerifySuccess:
				isRefresh = true;
				tabBottomView.setVisibility(View.GONE);
				MsgToast.geToast().setMsg("项目提交审核成功~");
				break;
			case ConstantStatus.SubmitVerifyFailed:
				MsgToast.geToast().setMsg("项目提交审核失败, 请稍后重试");
				break;
			case ConstantStatus.VerifySuccess:
				isRefresh = true;
				tabBottomView.setVisibility(View.GONE);
				MsgToast.geToast().setMsg("项目审核通过~");
				break;
			case ConstantStatus.VerifyResultSuccess:
				isRefresh = true;
				tabBottomView.setVisibility(View.GONE);
				MsgToast.geToast().setMsg("项目驳回通过~");
				break;
			case ConstantStatus.VerifyFailed:
				MsgToast.geToast().setMsg("项目审核/驳回失败,请稍后重试");
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
		setContentView(R.layout.requirement_manage_detail_layout);
		initView();
		initData();
		initListener();
	}

	@Override
	public void initView() {
		titleView = (TitleView) findViewById(R.id.comment_title);
		tabtopView = (TabTopView) findViewById(R.id.tabtopView);
		mVF = (ViewFlipper) findViewById(R.id.viewFlipper);
		mListView = (ListView) findViewById(R.id.xlistview);
		mListView01 = (ListView) findViewById(R.id.xlistview01);
		tabtopview_left_text = (TextView) findViewById(R.id.tabBottomview_left_text);
		tabtopview_right_text = (TextView) findViewById(R.id.tabBottomview_right_text);
		bottomview_submit_text = (TextView) findViewById(R.id.tabBottomview_submit_text);
		tabBottomView = (LinearLayout) findViewById(R.id.tabBottomView);
		ll_view01 = (LinearLayout) findViewById(R.id.ll_other_view01);
		ll_view02 = (LinearLayout) findViewById(R.id.ll_other_view02);
		ll_view03 = (LinearLayout) findViewById(R.id.ll_other_view03);
		ll_view04 = (LinearLayout) findViewById(R.id.ll_other_view04);
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
	}

	@Override
	public void initData() {
		title = getIntent().getStringExtra("title");
		childAt = getIntent().getIntExtra("childAt", 0);
		titleView.setTitle(title + "--详情");
		tabtopView.setTabText("项目详情", "项目审核记录");
		tabtopView.setTabMiddleText("项目计划信息");
		tabtopView.OnClickLeftTab();
		loadDialog = new LoadDialog(this);
		loadDialog.setTitle("正在加载数据,请稍后...");
		dialog = new ProjectVerifyDialog(this, "驳回", "请输入驳回原因");
		adapter = new ProjectManageAdapter(this, childAt, false);
		adapter01 = new ProjectManageAdapter(this, childAt, true);
		PurchaseOrderManageBean demandBean = (PurchaseOrderManageBean) getIntent().getSerializableExtra("bean");
		setViewData(demandBean);
		setDataRoles();
	}

	private void setDataRoles() {
		int v = 0;
		int b = 0;
		int s = 0;
		List<ChildrenRoot> children = new ArrayList<ChildrenRoot>();
		Object obj = ShareDataUtils.getObject(this, "menu_materiel");
		children.addAll((List) obj);
		if (children.size() > 0) {
			for (int i = 0; i < children.size(); i++) {
				ChildrenRoot childrenRoot = children.get(i);
				if (childrenRoot != null && "工程监管".equals(childrenRoot.getName())) {
					List<Children> childrens = childrenRoot.getChildren();
					if (childrens != null && childrens.size() > 0) {
						for (int j = 0; j < childrens.size(); j++) {
							Children children2 = childrens.get(j);
							if (children2 != null && "项目管理".equals(children2.getName())) {
								List<Roles> roles = children2.getRoles();
								if (roles != null && roles.size() > 0) {
									for (int k = 0; k < roles.size(); k++) {
										Roles roles2 = roles.get(k);
										if (roles2 != null && roles2.getName().equals("审核通过")) {
											v = 1;
										} else if (roles2 != null && roles2.getName().equals("驳回")) {
											b = 1;
										} else if (roles2 != null && roles2.getName().equals("提交审核")) {
											s = 1;
										}
									}
								}
							}
						}
					}
				}
			}
		}
		if (v == 1) {
			tabtopview_left_text.setVisibility(View.VISIBLE);
		} else {
			tabtopview_left_text.setVisibility(View.GONE);
		}
		if (b == 1) {
			tabtopview_right_text.setVisibility(View.VISIBLE);
		} else {
			tabtopview_right_text.setVisibility(View.GONE);
		}
		if (s == 1) {
			bottomview_submit_text.setVisibility(View.VISIBLE);
		} else {
			bottomview_submit_text.setVisibility(View.GONE);
		}
		if (v == 0 && b == 0 && s == 0) {
			tabBottomView.setVisibility(View.GONE);
		}
	}

	@Override
	public void initListener() {
		tabtopView.OnClickLeftTabOnClickListener(this);
		tabtopView.OnClickMiddleTabOnClickListener(this);
		tabtopView.OnClickRightTabOnClickListener(this);
		tabtopview_left_text.setOnClickListener(this);
		tabtopview_right_text.setOnClickListener(this);
		bottomview_submit_text.setOnClickListener(this);
		dialog.setSelectItemClick(this);
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

	protected void setProjectListData(List<ProjectInfo> projects) {
		if (isMiddleFlag) {
			projectInfos.clear();
			projectInfos.addAll(projects);
			adapter.setData(projectInfos, projects.size());
			mListView.setAdapter(adapter);
			// 没有加载到数据
			if (projectInfos.size() == 0) {
				mVF.setDisplayedChild(3);
			}
		} else {
			projectRecords.clear();
			projectRecords.addAll(projects);
			adapter01.setData(projectRecords, projects.size());
			mListView01.setAdapter(adapter01);
			// 没有加载到数据
			if (projectRecords.size() == 0) {
				mVF.setDisplayedChild(3);
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
		case R.id.tabBottomview_left_text:// 审核
			loadDialog.setTitle("正在提交审核数据,请稍后...");
			loadDialog.show();
			DcNetWorkUtils.getProjectVerify(this, handler, "project2", account, password, id);
			break;
		case R.id.tabBottomview_right_text:// 驳回
			dialog.show();
			break;
		case R.id.tabBottomview_submit_text:
			loadDialog.setTitle("正在提交审核数据,请稍后...");
			loadDialog.show();
			DcNetWorkUtils.getProjectSubmitVerify(this, handler, "project2", account, password, id);
			break;
		}
	}

	private void RequestList() {
		if (isMiddleFlag) {// 项目计划信息网络请求
			DcNetWorkUtils.getMaterielProjectInfoList(this, handler, account, password, code);
		} else {// 项目审核记录网络请求
			DcNetWorkUtils.getMaterielProjectRecordList(this, handler, account, password, id);
		}
	}

	private void setViewData(PurchaseOrderManageBean demandBean) {
		code = demandBean.code;
		id = demandBean.id;
		ll_view01.setVisibility(View.VISIBLE);
		ll_view02.setVisibility(View.VISIBLE);
		ll_view03.setVisibility(View.VISIBLE);
		ll_view04.setVisibility(View.VISIBLE);
		tvTitle01.setText("项目名称: ");
		tvTitle02.setText("项目编号: ");
		tvTitle03.setText("施工单位: ");
		tvTitle04.setText(Html.fromHtml("施工地点: "));
		tvTitle05.setText("操作账号: ");
		tvTitle06.setText(Html.fromHtml("状&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;态: "));
		tvTitle07.setText("启动时间: ");
		tvTitle08.setText("计划完工时间: ");
		tvTitle09.setText("项目负责人: ");
		tvTitle10.setText("负责人电话: ");
		tvTitle11.setText("添加时间: ");
		tvTitle12.setText(Html.fromHtml("备&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;注: "));

		tvContent01.setText(Tools.isEmpty(demandBean.name));
		tvContent02.setText(Tools.isEmpty(demandBean.code));
		tvContent03.setText(Tools.isEmpty(demandBean.dept));
		tvContent04.setText(Tools.isEmpty(demandBean.place));
		tvContent05.setText(Tools.isEmpty(demandBean.operator));
		tvContent06.setText(Tools.isEmpty(demandBean.statusHtml));
		tvContent07.setText(Tools.isEmpty(demandBean.starttimeStr));
		tvContent08.setText(Tools.isEmpty(demandBean.planendtimeStr));
		tvContent09.setText(Tools.isEmpty(demandBean.principal));
		tvContent10.setText(Tools.isEmpty(demandBean.mobile));
		tvContent11.setText(Tools.isEmpty(demandBean.addtime));
		tvContent12.setText(Tools.isEmpty(demandBean.note));
	}

	@Override
	public void itemOnClick(String item) {
		loadDialog.setTitle("正在提交驳回数据,请稍后...");
		loadDialog.show();
		DcNetWorkUtils.getProjectVerifyRebut(this, handler, "project", account, password, id, item);
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
