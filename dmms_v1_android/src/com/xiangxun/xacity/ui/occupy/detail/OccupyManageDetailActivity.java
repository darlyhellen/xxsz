package com.xiangxun.xacity.ui.occupy.detail;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.xiangxun.xacity.R;
import com.xiangxun.xacity.adapter.OccupyImageViewAdapter;
import com.xiangxun.xacity.app.BaseActivity;
import com.xiangxun.xacity.bean.LoginData.Children;
import com.xiangxun.xacity.bean.LoginData.ChildrenRoot;
import com.xiangxun.xacity.bean.LoginData.Roles;
import com.xiangxun.xacity.bean.ResponseResultBeans.OccupyAreaBean;
import com.xiangxun.xacity.bean.ResponseResultBeans.OccupyImageBean;
import com.xiangxun.xacity.bean.ResponseResultBeans.OccupyManageBean;
import com.xiangxun.xacity.bean.ResultBeans.Type;
import com.xiangxun.xacity.common.ConstantStatus;
import com.xiangxun.xacity.request.DcNetWorkUtils;
import com.xiangxun.xacity.ui.occupy.OccupyDelayActivity;
import com.xiangxun.xacity.ui.occupy.OccupySurveyActivity;
import com.xiangxun.xacity.utils.MyUtils;
import com.xiangxun.xacity.utils.ShareDataUtils;
import com.xiangxun.xacity.utils.Tools;
import com.xiangxun.xacity.view.LoadDialog;
import com.xiangxun.xacity.view.MsgToast;
import com.xiangxun.xacity.view.ProjectChangeDialog;
import com.xiangxun.xacity.view.ProjectChangeDialog.onSelectItemClick;
import com.xiangxun.xacity.view.ProjectVerifyDialog;
import com.xiangxun.xacity.view.ProjectVerifyDialog.SelectItemClick;
import com.xiangxun.xacity.view.TabTopView;
import com.xiangxun.xacity.view.TitleView;
import com.xiangxun.xacity.view.photoview.HackyViewPager;

/**
 * @package: com.xiangxun.xacity.ui.occupy.detail
 * @ClassName: OccupyManageDetailActivity.java
 * @Description: 挖占管理详情页面
 * @author: HanGJ
 * @date: 2016-3-4 上午9:41:51
 */
public class OccupyManageDetailActivity extends BaseActivity implements OnClickListener, SelectItemClick, onSelectItemClick, OnPageChangeListener {
	private TitleView titleView;
	private String title;
	private int childAt;
	private TabTopView tabtopView;
	private ViewFlipper mVF;
	private LinearLayout tabBottomView;
	private TextView project_delay;
	private TextView project_survey;
	private TextView project_change;
	private TextView project_mark;
	private ViewPager mViewPager = null;
	private LinearLayout mLlPlacePointLayout = null;
	private ImageView[] mPoints = null;
	private List<OccupyImageBean> mPictures = new ArrayList<OccupyImageBean>();
	private int mCurrentPage = 0;
	private int isMiddleFlag = 1;
	private LoadDialog loadDialog;
	private String id = "";
	private String projectId = "";
	private LinearLayout llViewArea;
	private LinearLayout ll_view01;
	private LinearLayout ll_view02;
	private LinearLayout ll_view03;
	private LinearLayout ll_view04;
	private LinearLayout ll_view05;
	private LinearLayout ll_view06;
	private LinearLayout ll_view07;
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
	private TextView tvTitle15;
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
	private TextView tvContent15;

	private ProjectVerifyDialog dialog;
	private List<Type> types = new ArrayList<Type>();
	private ProjectChangeDialog changeDialog;
	private TextView tv_image_type;
	private TextView tv_operator_name;
	private TextView tv_upload_time;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			loadDialog.dismiss();
			switch (msg.what) {
			case ConstantStatus.getOccupyDetailSuccess:// 项目详情
				OccupyManageBean manageBean = (OccupyManageBean) msg.obj;
				if (manageBean != null) {
					setOccupyManageDetailData(manageBean);
				} else {
					mVF.setDisplayedChild(3);
				}
				break;
			case ConstantStatus.getOccupyDetailStaticSuccess:// 统计面积
				List<OccupyAreaBean> occupyList = (List<OccupyAreaBean>) msg.obj;
				if (occupyList != null && occupyList.size() > 0) {
					setOccupyAreaData(occupyList);
				} else {
					mVF.setDisplayedChild(3);
				}
				break;
			case ConstantStatus.getOccupyProgressSuccess:
				List<Type> progressTypes = (List<Type>) msg.obj;
				if (progressTypes != null && progressTypes.size() > 0) {
					types.clear();
					types.addAll(progressTypes);
					changeDialog = new ProjectChangeDialog(OccupyManageDetailActivity.this, types, "更改状态", "状态");
					changeDialog.setSelectItemClick(OccupyManageDetailActivity.this);
				}
				break;
			case ConstantStatus.VerifySuccess:
				MsgToast.geToast().setMsg("标记内容提交成功");
				if (childAt == 0) {
					tvContent15.setText(Tools.isEmpty(markContent));
				} else if (childAt == 1) {
					tvContent12.setText(Tools.isEmpty(markContent));
				} else {
					tvContent12.setText(Tools.isEmpty(markContent));
				}
				break;
			case ConstantStatus.VerifyFailed:
				MsgToast.geToast().setMsg("标记内容提交失败,请稍后重试");
				break;
			case ConstantStatus.VerifyResultSuccess:
				MsgToast.geToast().setMsg("项目状态更改成功");
				break;
			case ConstantStatus.VerifyResultFailed:
				MsgToast.geToast().setMsg("项目状态更改失败,请稍后重试");
				break;
			case ConstantStatus.getOccupyDetailImageSuccess:// 现场图片
				List<OccupyImageBean> manageBeans = (List<OccupyImageBean>) msg.obj;
				if (manageBeans != null && manageBeans.size() > 0) {
					setOccupyImageDetailData(manageBeans);
				} else {
					mVF.setDisplayedChild(3);
				}
				break;
			case ConstantStatus.getOccupySearchListFailed:
				MsgToast.geToast().setMsg("数据加载失败,请稍后重试!");
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
		setContentView(R.layout.occupy_manage_detail_layout);
		initView();
		initData();
		initListener();
	}

	protected void setOccupyAreaData(List<OccupyAreaBean> occupyList) {
		llViewArea.removeAllViews();
		for (int i = 0; i < occupyList.size(); i++) {
			OccupyAreaBean occupyAreaBean = occupyList.get(i);
			View view = getLayoutInflater().inflate(R.layout.occupy_detail_area_layout, null);
			TextView tvRoadType = (TextView) view.findViewById(R.id.tv_occupy_road_type);
			TextView tvLength = (TextView) view.findViewById(R.id.tv_occupy_length);
			TextView tvWidth = (TextView) view.findViewById(R.id.tv_occupy_width);
			TextView tvArea = (TextView) view.findViewById(R.id.tv_occupy_area);
			TextView tvLengths = (TextView) view.findViewById(R.id.tv_occupy_lengths);
			TextView tvAreas = (TextView) view.findViewById(R.id.tv_occupy_areas);
			TextView tvType = (TextView) view.findViewById(R.id.tv_occupy_type);
			tvRoadType.setText(Tools.isEmpty(occupyAreaBean.faceType));
			tvLength.setText(Tools.isEmpty(occupyAreaBean.lengths));
			tvWidth.setText(Tools.isEmpty(occupyAreaBean.widths));
			tvArea.setText(Tools.isEmpty(occupyAreaBean.proportion));
			tvLengths.setText(Tools.isEmpty(occupyAreaBean.analysisLengths));
			tvAreas.setText(Tools.isEmpty(occupyAreaBean.analysisProportion));
			if ("0".equals(occupyAreaBean.type)) {
				tvType.setText("挖");
			} else {
				tvType.setText("占");
			}
			llViewArea.addView(view);
		}
	}

	@Override
	public void initView() {
		titleView = (TitleView) findViewById(R.id.comment_title);
		tabtopView = (TabTopView) findViewById(R.id.tabtopView);
		tabBottomView = (LinearLayout) findViewById(R.id.tabBottomView);
		mVF = (ViewFlipper) findViewById(R.id.viewFlipper);
		project_delay = (TextView) findViewById(R.id.project_delay);
		project_survey = (TextView) findViewById(R.id.project_survey);
		project_change = (TextView) findViewById(R.id.project_change);
		project_mark = (TextView) findViewById(R.id.project_mark);
		mViewPager = (HackyViewPager) findViewById(R.id.vp_phone_view);
		mLlPlacePointLayout = (LinearLayout) findViewById(R.id.ll_place_point);
		llViewArea = (LinearLayout) findViewById(R.id.ll_add_view_area);
		ll_view01 = (LinearLayout) findViewById(R.id.ll_other_view01);
		ll_view02 = (LinearLayout) findViewById(R.id.ll_other_view02);
		ll_view03 = (LinearLayout) findViewById(R.id.ll_other_view03);
		ll_view04 = (LinearLayout) findViewById(R.id.ll_other_view04);
		ll_view05 = (LinearLayout) findViewById(R.id.ll_other_view05);
		ll_view06 = (LinearLayout) findViewById(R.id.ll_other_view06);
		ll_view07 = (LinearLayout) findViewById(R.id.ll_other_view07);
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
		tvTitle15 = (TextView) findViewById(R.id.tv_title15);
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
		tvContent15 = (TextView) findViewById(R.id.tv_content15);
		//
		tv_image_type = (TextView) findViewById(R.id.tv_image_type);
		tv_operator_name = (TextView) findViewById(R.id.tv_operator_name);
		tv_upload_time = (TextView) findViewById(R.id.tv_upload_time);
	}

	@Override
	public void initData() {
		title = getIntent().getStringExtra("title");
		id = getIntent().getStringExtra("id");
		childAt = getIntent().getIntExtra("childAt", 0);
		titleView.setTitle(title + "--详情");
		tabtopView.setTabText("项目详情", "现场图片");
		tabtopView.setTabMiddleText("查看面积");
		tabtopView.OnClickLeftTab();
		loadDialog = new LoadDialog(this);
		dialog = new ProjectVerifyDialog(this, "标记操作", "请输入标记内容");
		loadDialog.setTitle("正在加载数据,请稍后...");
		if (childAt == 2) {
			setDataRoles("占道街具管理");
			project_survey.setVisibility(View.GONE);
			project_change.setVisibility(View.GONE);
		} else if (childAt == 1) {
			setDataRoles("道路占用管理");
		} else {
			setDataRoles("道路挖掘管理");
		}
		RequestList();
	}

	@Override
	public void initListener() {
		tabtopView.OnClickLeftTabOnClickListener(this);
		tabtopView.OnClickMiddleTabOnClickListener(this);
		tabtopView.OnClickRightTabOnClickListener(this);
		project_delay.setOnClickListener(this);
		project_survey.setOnClickListener(this);
		project_change.setOnClickListener(this);
		project_mark.setOnClickListener(this);
		dialog.setSelectItemClick(this);
		mViewPager.setOnPageChangeListener(this);
		titleView.setLeftBackOneListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}

	private void setDataRoles(String roleTitle) {
		int d = 0;
		int v = 0;
		int c = 0;
		int m = 0;
		List<ChildrenRoot> children = new ArrayList<ChildrenRoot>();
		Object obj = ShareDataUtils.getObject(this, "menu_occupy");
		children.addAll((List) obj);
		if (children.size() > 0) {
			for (int i = 0; i < children.size(); i++) {
				ChildrenRoot childrenRoot = children.get(i);
				if (childrenRoot != null && "挖占管理".equals(childrenRoot.getName())) {
					List<Children> childrens = childrenRoot.getChildren();
					if (childrens != null && childrens.size() > 0) {
						for (int j = 0; j < childrens.size(); j++) {
							Children children2 = childrens.get(j);
							if (children2 != null && roleTitle.equals(children2.getName())) {
								List<Roles> roles = children2.getRoles();
								if (roles != null && roles.size() > 0) {
									for (int k = 0; k < roles.size(); k++) {
										Roles roles2 = roles.get(k);
										if (roles2 != null && roles2.getName().contains("延期")) {
											d = 1;
										} else if (roles2 != null && roles2.getName().contains("勘察")) {
											v = 1;
										} else if (roles2 != null && roles2.getName().contains("更改状态")) {
											c = 1;
										} else if (roles2 != null && roles2.getName().contains("标记")) {
											m = 1;
										}
									}
								}
							}
						}
					}
				}
			}
		}
		if (d == 1) {
			project_delay.setVisibility(View.GONE);
		} else {
			project_delay.setVisibility(View.GONE);
		}

		if (v == 1) {
			project_survey.setVisibility(View.VISIBLE);
		} else {
			project_survey.setVisibility(View.GONE);
		}
		if (c == 1) {
			project_change.setVisibility(View.GONE);
		} else {
			project_change.setVisibility(View.GONE);
		}

		if (m == 1) {
			project_mark.setVisibility(View.VISIBLE);
		} else {
			project_mark.setVisibility(View.GONE);
		}

		if (v == 0 && c == 0 && m == 0) {
			tabBottomView.setVisibility(View.GONE);
		}
	}

	private String endtimeStr;
	protected void setOccupyManageDetailData(OccupyManageBean manageBean) {
		if ("3".equals(manageBean.status)) {
			tabBottomView.setVisibility(View.GONE);
		} else if("6".equals(manageBean.status)){
			tabBottomView.setVisibility(View.GONE);
		}
		projectId = manageBean.id;
		if ("1".equals(manageBean.isDelay)) {
			endtimeStr = manageBean.approveEndtime != null && manageBean.approveEndtime.length() > 0 ? MyUtils.StringToDate(manageBean.approveEndtime) : "";
		} else {
			endtimeStr = manageBean.endtimeStr;
		}
		if (childAt == 0) {
			ll_view01.setVisibility(View.VISIBLE);
			ll_view02.setVisibility(View.VISIBLE);
			ll_view03.setVisibility(View.VISIBLE);
			ll_view04.setVisibility(View.VISIBLE);
			ll_view05.setVisibility(View.VISIBLE);
			ll_view06.setVisibility(View.VISIBLE);
			ll_view07.setVisibility(View.VISIBLE);
			tvTitle01.setText("项目名称: ");
			tvTitle02.setText("建设单位: ");
			tvTitle03.setText("施工单位: ");
			tvTitle04.setText(Html.fromHtml("施工类别: "));
			tvTitle05.setText("道路名称: ");
			tvTitle06.setText(Html.fromHtml("道路类别: "));
			tvTitle07.setText(Html.fromHtml("经&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;度: "));
			tvTitle08.setText(Html.fromHtml("纬&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;度: "));
			tvTitle09.setText(Html.fromHtml("起始日期: "));
			tvTitle10.setText(Html.fromHtml("截止日期: "));
			tvTitle11.setText(Html.fromHtml("工地负责人: "));
			tvTitle12.setText(Html.fromHtml("负责人手机号: "));
			tvTitle13.setText(Html.fromHtml("施工管理人: "));
			tvTitle14.setText(Html.fromHtml("辖&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;区: "));
			tvTitle15.setText(Html.fromHtml("标记内容: "));

			tvContent01.setText(Tools.isEmpty(manageBean.projectName));
			tvContent02.setText(Tools.isEmpty(manageBean.applicantName));
			tvContent03.setText(Tools.isEmpty(manageBean.builderName));
			tvContent04.setText(Tools.isEmpty(manageBean.typeName));
			tvContent05.setText(Tools.isEmpty(manageBean.roadname));
			tvContent06.setText(Tools.isEmpty(manageBean.roadType));
			tvContent07.setText(Tools.isEmpty(manageBean.longitude));
			tvContent08.setText(Tools.isEmpty(manageBean.latitude));
			tvContent09.setText(Tools.isEmpty(manageBean.starttimeStr));
			tvContent10.setText(Tools.isEmpty(endtimeStr));
			tvContent11.setText(Tools.isEmpty(manageBean.principal));
			tvContent12.setText(Tools.isEmpty(manageBean.mobile));
			tvContent13.setText(Tools.isEmpty(manageBean.manager));
			tvContent14.setText(Tools.isEmpty(manageBean.managearea));
			tvContent15.setText(Tools.isEmpty(manageBean.applyCode));
		} else if (childAt == 1) {
			ll_view01.setVisibility(View.VISIBLE);
			ll_view02.setVisibility(View.VISIBLE);
			ll_view03.setVisibility(View.VISIBLE);
			ll_view04.setVisibility(View.VISIBLE);
			tvTitle01.setText("申请单位: ");
			tvTitle02.setText("占用类别: ");
			tvTitle03.setText("道路名称: ");
			tvTitle04.setText(Html.fromHtml("道路类别: "));
			tvTitle05.setText(Html.fromHtml("经&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;度: "));
			tvTitle06.setText(Html.fromHtml("纬&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;度: "));
			tvTitle07.setText(Html.fromHtml("起始日期: "));
			tvTitle08.setText(Html.fromHtml("截止日期: "));
			tvTitle09.setText(Html.fromHtml("联&#160;&#160;系&#160;&#160;人: "));
			tvTitle10.setText(Html.fromHtml("联系电话: "));
			tvTitle11.setText(Html.fromHtml("辖&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;区: "));
			tvTitle12.setText(Html.fromHtml("标记内容: "));

			tvContent01.setText(Tools.isEmpty(manageBean.applicantName));
			tvContent02.setText(Tools.isEmpty(manageBean.typeName));
			tvContent03.setText(Tools.isEmpty(manageBean.roadname));
			tvContent04.setText(Tools.isEmpty(manageBean.roadType));
			tvContent05.setText(Tools.isEmpty(manageBean.longitude));
			tvContent06.setText(Tools.isEmpty(manageBean.latitude));
			tvContent07.setText(Tools.isEmpty(manageBean.starttimeStr));
			tvContent08.setText(Tools.isEmpty(endtimeStr));
			tvContent09.setText(Tools.isEmpty(manageBean.principal));
			tvContent10.setText(Tools.isEmpty(manageBean.mobile));
			tvContent11.setText(Tools.isEmpty(manageBean.managearea));
			tvContent12.setText(Tools.isEmpty(manageBean.applyCode));
		} else {
			ll_view01.setVisibility(View.VISIBLE);
			ll_view02.setVisibility(View.VISIBLE);
			ll_view03.setVisibility(View.VISIBLE);
			ll_view04.setVisibility(View.VISIBLE);
			tvTitle01.setText("申请单位: ");
			tvTitle02.setText("街具类别: ");
			tvTitle03.setText("道路名称: ");
			tvTitle04.setText(Html.fromHtml("道路类别: "));
			tvTitle05.setText(Html.fromHtml("经&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;度: "));
			tvTitle06.setText(Html.fromHtml("纬&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;度: "));
			tvTitle07.setText(Html.fromHtml("起始日期: "));
			tvTitle08.setText(Html.fromHtml("截止日期: "));
			tvTitle09.setText(Html.fromHtml("联&#160;&#160;系&#160;&#160;人: "));
			tvTitle10.setText(Html.fromHtml("联系电话: "));
			tvTitle11.setText(Html.fromHtml("辖&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;区: "));
			tvTitle12.setText(Html.fromHtml("标记内容: "));

			tvContent01.setText(Tools.isEmpty(manageBean.applicantName));
			tvContent02.setText(Tools.isEmpty(manageBean.typeName));
			tvContent03.setText(Tools.isEmpty(manageBean.roadname));
			tvContent04.setText(Tools.isEmpty(manageBean.roadType));
			tvContent05.setText(Tools.isEmpty(manageBean.longitude));
			tvContent06.setText(Tools.isEmpty(manageBean.latitude));
			tvContent07.setText(Tools.isEmpty(manageBean.starttimeStr));
			tvContent08.setText(Tools.isEmpty(endtimeStr));
			tvContent09.setText(Tools.isEmpty(manageBean.principal));
			tvContent10.setText(Tools.isEmpty(manageBean.mobile));
			tvContent11.setText(Tools.isEmpty(manageBean.managearea));
			tvContent12.setText(Tools.isEmpty(manageBean.applyCode));
		}
	}

	protected void setOccupyImageDetailData(List<OccupyImageBean> imageBeans) {
		mPictures.clear();
		mPictures.addAll(imageBeans);
		setPlacePoint();
		setViewData();
	}

	private void setViewData() {
		mViewPager.setAdapter(new OccupyImageViewAdapter(this, mPictures));
		mViewPager.setCurrentItem(mCurrentPage);
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
		tv_image_type.setText(Html.fromHtml(String.format(getString(R.string.image_type), mPictures.get(0).typeName)));
		tv_operator_name.setText(Html.fromHtml(String.format(getString(R.string.operator_name), mPictures.get(0).operator)));
		tv_upload_time.setText(Html.fromHtml(String.format(getString(R.string.upload_time), mPictures.get(0).uploadTime)));
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
			tv_image_type.setText(Html.fromHtml(String.format(getString(R.string.image_type), mPictures.get(i).typeName)));
			tv_operator_name.setText(Html.fromHtml(String.format(getString(R.string.operator_name), mPictures.get(i).operator)));
			tv_upload_time.setText(Html.fromHtml(String.format(getString(R.string.upload_time), mPictures.get(i).uploadTime)));
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.toptabview_left_rlyout:
			isMiddleFlag = 1;
			loadDialog.setTitle("正在加载数据,请稍后...");
			loadDialog.show();
			mVF.setDisplayedChild(0);
			tabtopView.OnClickLeftTab();
			RequestList();
			break;
		case R.id.toptabview_middle_rlyout:
			isMiddleFlag = 2;
			loadDialog.setTitle("正在加载数据,请稍后...");
			loadDialog.show();
			mVF.setDisplayedChild(1);
			tabtopView.OnClickMiddleTab();
			RequestList();
			break;
		case R.id.toptabview_right_rlyout:
			isMiddleFlag = 3;
			loadDialog.setTitle("正在加载数据,请稍后...");
			loadDialog.show();
			mVF.setDisplayedChild(2);
			tabtopView.OnClickRightTab();
			RequestList();
			break;
		case R.id.project_delay: // 延期
			Intent delayIntent = new Intent(this, OccupyDelayActivity.class);
			delayIntent.putExtra("id", projectId);
			delayIntent.putExtra("endtimeStr", endtimeStr);
			startActivity(delayIntent);
			break;
		case R.id.project_survey: // 勘察
			Intent intent = new Intent(this, OccupySurveyActivity.class);
			intent.putExtra("id", projectId);
			startActivity(intent);
			break;
		case R.id.project_change: // 状态更改
			if (types.size() > 0 && changeDialog != null) {
				changeDialog.show();
			} else {
				DcNetWorkUtils.getOccupyProgressInfoList(this, handler, account, password);
			}
			break;
		case R.id.project_mark: // 标记
			dialog.show();
			break;
		}
	}

	private void RequestList() {
		if (types.size() <= 0) {
			DcNetWorkUtils.getOccupyProgressInfoList(this, handler, account, password);
		}
		if (isMiddleFlag == 1) {// 详情
			DcNetWorkUtils.getOccupySearchDetailList(this, handler, account, password, id);
		} else if (isMiddleFlag == 2) {// 查看面积
			DcNetWorkUtils.getOccupyStatisticsDetailList(this, handler, account, password, id);
		} else { // 现场图片
			DcNetWorkUtils.getOccupyDetailImageList(this, handler, account, password, id);
		}
	}

	String markContent = "";
	@Override
	public void itemOnClick(String item) {
		markContent = item;
		loadDialog.setTitle("标记内容提交中,请稍后...");
		loadDialog.show();
		DcNetWorkUtils.getOccupyMark(this, handler, account, password, projectId, item);
	}

	@Override
	public void changeState(Type type) {
		loadDialog.setTitle("项目更改状态提交中,请稍后...");
		loadDialog.show();
		DcNetWorkUtils.getOccupyChange(this, handler, account, password, projectId, type.code);
	}
	
	@Override
	protected void onResume() {
		//RequestList();
		super.onResume();
	}

}
