package com.xiangxun.xacity.ui.device.detail;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiangxun.xacity.R;
import com.xiangxun.xacity.app.BaseActivity;
import com.xiangxun.xacity.bean.ResponseResultBeans.DeviceManageBean;
import com.xiangxun.xacity.common.DcHttpClient;
import com.xiangxun.xacity.utils.Tools;
import com.xiangxun.xacity.view.TitleView;
import com.xiangxun.xacity.volley.toolbox.NetworkImageView;

/**
 * @package: com.xiangxun.xacity.ui.device.detail
 * @ClassName: DeviceManageDetailActivity.java
 * @Description: 设备管理详情页面
 * @author: HanGJ
 * @date: 2016-2-23 下午3:47:34
 */
public class DeviceManageDetailActivity extends BaseActivity {
	private TitleView titleView;
	private int childAt;
	private LinearLayout ll_image;
	private TextView tv_image_name;
	private NetworkImageView mFile;
	private LinearLayout ll_image01;
	private TextView tv_image_name01;
	private NetworkImageView mFile01;
	private LinearLayout ll_view01;
	private LinearLayout ll_view02;
	private LinearLayout ll_view03;
	private LinearLayout ll_view04;
	private LinearLayout ll_view05;
	private LinearLayout ll_view06;
	private LinearLayout ll_view07;
	private LinearLayout ll_view08;
	private LinearLayout ll_view09;
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
	private TextView tvTitle16;
	private TextView tvTitle17;
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
	private TextView tvContent16;
	private TextView tvContent17;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.device_purchase_detail_layout);
		initView();
		initData();
		initListener();
	}

	@Override
	public void initView() {
		titleView = (TitleView) findViewById(R.id.comment_title);
		ll_view01 = (LinearLayout) findViewById(R.id.ll_other_view01);
		ll_view02 = (LinearLayout) findViewById(R.id.ll_other_view02);
		ll_view03 = (LinearLayout) findViewById(R.id.ll_other_view03);
		ll_view04 = (LinearLayout) findViewById(R.id.ll_other_view04);
		ll_view05 = (LinearLayout) findViewById(R.id.ll_other_view05);
		ll_view06 = (LinearLayout) findViewById(R.id.ll_other_view06);
		ll_view07 = (LinearLayout) findViewById(R.id.ll_other_view07);
		ll_view08 = (LinearLayout) findViewById(R.id.ll_other_view08);
		ll_view09 = (LinearLayout) findViewById(R.id.ll_other_view09);
		ll_image = (LinearLayout) findViewById(R.id.ll_image);
		mFile = (NetworkImageView) findViewById(R.id.iv_file);
		tv_image_name = (TextView) findViewById(R.id.tv_image_name);
		ll_image01 = (LinearLayout) findViewById(R.id.ll_image01);
		mFile01 = (NetworkImageView) findViewById(R.id.iv_file01);
		tv_image_name01 = (TextView) findViewById(R.id.tv_image_name01);
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
		tvTitle16 = (TextView) findViewById(R.id.tv_title16);
		tvTitle17 = (TextView) findViewById(R.id.tv_title17);
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
		tvContent16 = (TextView) findViewById(R.id.tv_content16);
		tvContent17 = (TextView) findViewById(R.id.tv_content17);
	}

	@Override
	public void initData() {
		String title = getIntent().getStringExtra("title");
		childAt = getIntent().getIntExtra("childAt", 0);
		titleView.setTitle(title + "--详情");
		DeviceManageBean bean = (DeviceManageBean) getIntent().getSerializableExtra("bean");
		if (childAt == 0) {// 操作人员台账
			ll_view01.setVisibility(View.VISIBLE);
			ll_view02.setVisibility(View.VISIBLE);
			ll_view03.setVisibility(View.VISIBLE);
			tvTitle01.setText(Html.fromHtml("姓&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;名: "));
			tvTitle02.setText(Html.fromHtml("性&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;别: "));
			tvTitle03.setText("出生年月: ");
			tvTitle04.setText("文化程度: ");
			tvTitle05.setText(Html.fromHtml("岗&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;位: "));
			tvTitle06.setText("持证日期: ");
			tvTitle07.setText("证书编号: ");
			tvTitle08.setText("复审时间: ");
			tvTitle09.setText("工作单位: ");
			tvTitle10.setText(Html.fromHtml("电&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;话: "));
			tvTitle11.setText(Html.fromHtml("备&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;注: "));

			tvContent01.setText(Tools.isEmpty(bean.name));
			if ("2".equals(bean.sex)) {
				tvContent02.setText("女");
			} else {
				tvContent02.setText("男");
			}
			tvContent03.setText(Tools.isEmpty(bean.birthdate));
			tvContent04.setText(Tools.isEmpty(bean.standardcultre));
			tvContent05.setText(Tools.isEmpty(bean.station));
			tvContent06.setText(Tools.isEmpty(bean.certificateDate));
			tvContent07.setText(Tools.isEmpty(bean.certificateCode));
			tvContent08.setText(Tools.isEmpty(bean.checkDate));
			tvContent09.setText(Tools.isEmpty(bean.orgName));
			tvContent10.setText(Tools.isEmpty(bean.telephone));
			tvContent11.setText(Tools.isEmpty(bean.remark));
			if (bean.photo != null && bean.photo.length() > 0) {
				ll_image.setVisibility(View.VISIBLE);
				tv_image_name.setText("操作人员照片: ");
				DcHttpClient.getInstance().getImageForNIView(bean.photo, mFile, R.drawable.view_pager_default);
			}
		} else if (childAt == 1) {// 设备台账
			ll_view01.setVisibility(View.VISIBLE);
			ll_view02.setVisibility(View.VISIBLE);
			ll_view03.setVisibility(View.VISIBLE);
			ll_view04.setVisibility(View.VISIBLE);
			ll_view05.setVisibility(View.VISIBLE);
			ll_view06.setVisibility(View.VISIBLE);
			ll_view07.setVisibility(View.VISIBLE);
			ll_view08.setVisibility(View.VISIBLE);
			ll_view09.setVisibility(View.VISIBLE);
			tvTitle01.setText("资产编号: ");
			tvTitle02.setText("设备分类: ");
			tvTitle03.setText("设备归属: ");
			tvTitle04.setText("设备名称: ");
			tvTitle05.setText("使用单位: ");
			tvTitle06.setText("产权单位: ");
			tvTitle07.setText("启用日期: ");
			tvTitle08.setText("牌照号码: ");
			tvTitle09.setText(Html.fromHtml("数&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;量: "));
			tvTitle10.setText("设备原值: ");
			tvTitle11.setText(Html.fromHtml("品&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;牌: "));
			tvTitle12.setText(Html.fromHtml("型&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;号: "));
			tvTitle13.setText("使用状态: ");
			tvTitle14.setText("资金来源: ");
			tvTitle15.setText("财务对应: ");
			tvTitle16.setText("燃油类型: ");
			tvTitle17.setText(Html.fromHtml("操&#160;&#160;作&#160;&#160;员: "));

			tvContent01.setText(Tools.isEmpty(bean.code));
			tvContent02.setText(Tools.isEmpty(bean.typeName));
			tvContent03.setText(Tools.isEmpty(bean.stypeName));
			tvContent04.setText(Tools.isEmpty(bean.name));
			tvContent05.setText(Tools.isEmpty(bean.usingCompanyName));
			tvContent06.setText(Tools.isEmpty(bean.belongtoCompanyName));
			tvContent07.setText(Tools.isEmpty(bean.startUsingDate));
			tvContent08.setText(Tools.isEmpty(bean.plate));
			tvContent09.setText(Tools.isEmpty(bean.num));
			tvContent10.setText(Tools.isEmpty(bean.original) + "元");
			tvContent11.setText(Tools.isEmpty(bean.brand));
			tvContent12.setText(Tools.isEmpty(bean.model));
			tvContent13.setText(Tools.isEmpty(bean.stateName));
			tvContent14.setText(Tools.isEmpty(bean.capitalName));
			tvContent15.setText(Tools.isEmpty(bean.curtainName));
			if ("1".equals(bean.oilPoint)) {
				tvContent16.setText("汽油");
			} else if ("2".equals(bean.oilPoint)) {
				tvContent16.setText("柴油");
			} else if ("3".equals(bean.oilPoint)) {
				tvContent16.setText("天然气");
			} else {
				tvContent16.setText("其他");
			}
			tvContent17.setText(Tools.isEmpty(bean.operatorName));
			if (bean.photo != null && bean.photo.length() > 0 && bean.photo.contains("http://")) {
				ll_image.setVisibility(View.VISIBLE);
				tv_image_name.setText("设备照片: ");
				DcHttpClient.getInstance().getImageForNIView(bean.photo, mFile, R.drawable.view_pager_default);
			}
			if (bean.checkReport != null && bean.checkReport.length() > 0 && bean.checkReport.contains("http://")) {
				ll_image01.setVisibility(View.VISIBLE);
				tv_image_name01.setText("检测报告: ");
				DcHttpClient.getInstance().getImageForNIView(bean.checkReport, mFile01, R.drawable.view_pager_default);
			}
		} else if (childAt == 2) {// 安全保护装置
			tvTitle01.setText("资产编号: ");
			tvTitle02.setText("设备名称: ");
			tvTitle03.setText("安全保护装置: ");
			tvTitle04.setText(Html.fromHtml("数&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;量: "));
			tvTitle05.setText(Html.fromHtml("说&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;明: "));
			tvTitle06.setText(Html.fromHtml("备&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;注: "));
			tvTitle07.setText("");
			tvTitle08.setText("");

			tvContent01.setText(Tools.isEmpty(bean.deviceCode));
			tvContent02.setText(Tools.isEmpty(bean.deviceName));
			tvContent03.setText(Tools.isEmpty(bean.deviceSafety));
			tvContent04.setText(Tools.isEmpty(bean.num));
			tvContent05.setText(Tools.isEmpty(bean.explain));
			tvContent06.setText(Tools.isEmpty(bean.remark));
			tvContent07.setText("");
			tvContent08.setText("");
		} else if (childAt == 3) {// 设备派遣
			ll_view01.setVisibility(View.VISIBLE);
			ll_view02.setVisibility(View.VISIBLE);
			tvTitle01.setText("资产编号: ");
			tvTitle02.setText("型号/牌照: ");
			tvTitle03.setText("设备名称: ");
			tvTitle04.setText("使用单位: ");
			tvTitle05.setText("使用时间: ");
			tvTitle06.setText(Html.fromHtml("地&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;点: "));
			tvTitle07.setText(Html.fromHtml("审&#160;&#160;批&#160;&#160;人: "));
			tvTitle08.setText(Html.fromHtml("事&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;由: "));
			tvTitle09.setText("使用时长: ");
			tvTitle10.setText(Html.fromHtml("台&#160;&#160;班&#160;&#160;数: "));

			tvContent01.setText(Tools.isEmpty(bean.deviceCode));
			tvContent02.setText(Tools.isEmpty(bean.deviceModel));
			tvContent03.setText(Tools.isEmpty(bean.deviceName));
			tvContent04.setText(Tools.isEmpty(bean.usingCompanyName));
			tvContent05.setText(Tools.isEmpty(bean.dateTime));
			tvContent06.setText(Tools.isEmpty(bean.address));
			tvContent07.setText(Tools.isEmpty(bean.affirmPerson));
			tvContent08.setText(Tools.isEmpty(bean.reason));
			tvContent09.setText(Tools.isEmpty(bean.usingTime));
			tvContent10.setText(Tools.isEmpty(bean.taibanNum));
		} else if (childAt == 4) {// 使用记录
			tvTitle01.setText("资产编号: ");
			tvTitle02.setText("设备名称: ");
			tvTitle03.setText("规格型号: ");
			tvTitle04.setText("产权单位: ");
			tvTitle05.setText("牌照号码: ");
			tvTitle06.setText(Html.fromHtml(""));
			tvTitle07.setText("");
			tvTitle08.setText("");

			tvContent01.setText(Tools.isEmpty(bean.code));
			tvContent02.setText(Tools.isEmpty(bean.deviceName));
			tvContent03.setText(Tools.isEmpty(bean.model));
			tvContent04.setText(Tools.isEmpty(bean.belongtoCompany));
			tvContent05.setText(Tools.isEmpty(bean.plate));
			tvContent06.setText(Tools.isEmpty(""));
			tvContent07.setText("");
			tvContent08.setText("");
		} else if (childAt == 5) {// 设备到场验证
			tvTitle01.setText("资产编号: ");
			tvTitle02.setText("设备名称: ");
			tvTitle03.setText("设备型号: ");
			tvTitle04.setText(Html.fromHtml("操&#160;&#160;作&#160;&#160;员: "));
			tvTitle05.setText(Html.fromHtml("进场日期: "));
			tvTitle06.setText(Html.fromHtml("施工地点: "));
			tvTitle07.setText("检查项目: ");
			tvTitle08.setText("牌照号码: ");

			tvContent01.setText(Tools.isEmpty(bean.deviceCode));
			tvContent02.setText(Tools.isEmpty(bean.deviceName));
			tvContent03.setText(Tools.isEmpty(bean.model));
			tvContent04.setText(Tools.isEmpty(bean.operator));
			tvContent05.setText(Tools.isEmpty(bean.dateTime));
			tvContent06.setText(Tools.isEmpty(bean.address));
			tvContent07.setText("操作人员持证上岗、受教育交底情况: " + Tools.isEmpty(bean.operatorCondition) + "\n动力装置，传动及工作能力: " + Tools.isEmpty(bean.dynamicPapt) + "\n整体结构，零部件及工具: " + Tools.isEmpty(bean.chassisPart) + "\n渗漏、尾气、噪音、灯光、转向、安全装置等: " + Tools.isEmpty(bean.minglePart));
			tvContent08.setText(Tools.isEmpty(bean.plate));
		} else if (childAt == 6) {// 日常检查记录
			ll_view01.setVisibility(View.VISIBLE);
			ll_view02.setVisibility(View.VISIBLE);
			ll_view03.setVisibility(View.VISIBLE);
			tvTitle01.setVisibility(View.GONE);
			tvTitle02.setText("设备名称: ");
			tvTitle03.setText("设备型号: ");
			tvTitle04.setText(Html.fromHtml("车&#160;&#160;牌&#160;&#160;号: "));
			tvTitle05.setText("设备类型: ");
			tvTitle06.setText("检查地点: ");
			tvTitle07.setText("检查日期: ");
			tvTitle08.setText(Html.fromHtml("操作手姓名: "));
			tvTitle09.setText(Html.fromHtml("检查人员: "));
			tvTitle10.setText("检查项目: ");
			tvTitle11.setText("存在重大问题: ");

			tvContent01.setVisibility(View.GONE);
			tvContent02.setText(Tools.isEmpty(bean.deviceName));
			tvContent03.setText(Tools.isEmpty(bean.deviceModel));
			tvContent04.setText(Tools.isEmpty(bean.plate));
			tvContent05.setText(Tools.isEmpty(bean.deviceType));
			tvContent06.setText(Tools.isEmpty(bean.constructionAddress));
			tvContent07.setText(Tools.isEmpty(bean.dateTime));
			tvContent08.setText(Tools.isEmpty(bean.principal));
			tvContent09.setText(Tools.isEmpty(bean.operator));
			tvContent10.setText("操作员上岗、维保运行记录填写情况: " + Tools.isEmpty(bean.operatorCondition) //
					+ "\n动力装置，传动及工作能力: " + Tools.isEmpty(bean.dynamicPapt) + "\n整体结构，零部件及工具: "//
					+ Tools.isEmpty(bean.chassisPart) + "\n渗漏、尾气、噪音、灯光、转向、安全装置等: " + Tools.isEmpty(bean.minglePart)//
					+ "\n仪器及特种设备检定/校准: " + Tools.isEmpty(bean.authenticateCorrect) + "\n仪器标识管理: " + Tools.isEmpty(bean.identificationManagement)//
					+ "\n仪器期间核查: " + Tools.isEmpty(bean.periodInspect));
			tvContent11.setText(Tools.isEmpty(bean.significantProblem));
		} else if (childAt == 7) {// 完好利用率
			ll_view01.setVisibility(View.VISIBLE);
			ll_view02.setVisibility(View.VISIBLE);
			ll_view03.setVisibility(View.VISIBLE);
			ll_view04.setVisibility(View.VISIBLE);
			ll_view05.setVisibility(View.VISIBLE);
			tvTitle01.setText(Html.fromHtml("规格型号: "));
			tvTitle02.setText(Html.fromHtml("设备名称: "));
			tvTitle03.setText(Html.fromHtml("使用单位: "));
			tvTitle04.setText("完好制度台班数: ");
			tvTitle05.setText("完好台日数: ");
			tvTitle06.setText(Html.fromHtml("完&#160;&#160;好&#160;&#160;率: "));
			tvTitle07.setText("牌照号码: ");
			tvTitle08.setText("实际台日数: ");
			tvTitle09.setText("加班台日数: ");
			tvTitle10.setText(Html.fromHtml("利&#160;&#160;用&#160;&#160;率: "));
			tvTitle11.setText(Html.fromHtml("月&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;份: "));
			tvTitle12.setText(Html.fromHtml("备&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;注: "));
			tvTitle13.setText("利用制度台班数: ");

			tvContent01.setText(Tools.isEmpty(bean.deviceModel));
			tvContent02.setText(Tools.isEmpty(bean.deviceName));
			tvContent03.setText(Tools.isEmpty(bean.usingCompanyName));
			tvContent04.setText(Tools.isEmpty(bean.intactRegime));
			tvContent05.setText(Tools.isEmpty(bean.intact));
			tvContent06.setText(Tools.isEmpty(bean.intactRatio) + "%");
			tvContent07.setText(Tools.isEmpty(bean.plate));
			tvContent08.setText(Tools.isEmpty(bean.reality));
			tvContent09.setText(Tools.isEmpty(bean.overtime));
			tvContent10.setText(Tools.isEmpty(bean.useRatio) + "%");
			tvContent11.setText(Tools.isEmpty(bean.month));
			tvContent12.setText(Tools.isEmpty(bean.useRegime));
			tvContent12.setText(Tools.isEmpty(bean.utilizeRegime));
		} else if (childAt == 8) {// 重点设备月报表
			ll_view01.setVisibility(View.VISIBLE);
			ll_view02.setVisibility(View.VISIBLE);
			ll_view03.setVisibility(View.VISIBLE);
			ll_view04.setVisibility(View.VISIBLE);
			tvTitle01.setText(Html.fromHtml("资产编号: "));
			tvTitle02.setText(Html.fromHtml("型号/牌照: "));
			tvTitle03.setText(Html.fromHtml("设备名称: "));
			tvTitle04.setText(Html.fromHtml("单&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;位: "));
			tvTitle05.setText(Html.fromHtml("月&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;份: "));
			tvTitle06.setText(Html.fromHtml("本月工作小时: "));
			tvTitle07.setText("本月累计小时: ");
			tvTitle08.setText("设备状态: ");
			tvTitle09.setText("应作清洁保养维修内容: ");
			tvTitle10.setText(Html.fromHtml("完成内容及时间: "));
			tvTitle11.setText(Html.fromHtml("操作人员: "));
			tvTitle12.setText(Html.fromHtml("管理人员: "));

			tvContent01.setText(Tools.isEmpty(bean.deviceCode));
			tvContent02.setText(Tools.isEmpty(bean.deviceModel));
			tvContent03.setText(Tools.isEmpty(bean.deviceName));
			tvContent04.setText(Tools.isEmpty(bean.companyName));
			tvContent05.setText(Tools.isEmpty(bean.month));
			tvContent06.setText(Tools.isEmpty(bean.workTime));
			tvContent07.setText(Tools.isEmpty(bean.totalTime));
			tvContent08.setText(Tools.isEmpty(bean.deviceState));
			tvContent09.setText(Tools.isEmpty(bean.maintainContent));
			tvContent10.setText(Tools.isEmpty(bean.completeContent));
			tvContent11.setText(Tools.isEmpty(bean.operatorName));
			tvContent12.setText(Tools.isEmpty(bean.manager));
		} else if (childAt == 9) {// 保险管理
			ll_view01.setVisibility(View.VISIBLE);
			ll_view02.setVisibility(View.VISIBLE);
			ll_view03.setVisibility(View.VISIBLE);
			ll_view04.setVisibility(View.VISIBLE);
			tvTitle01.setText("资产编号: ");
			tvTitle02.setText("设备型号: ");
			tvTitle03.setText("设备名称: ");
			tvTitle04.setText("车船税费: ");
			tvTitle05.setText("保险公司: ");
			tvTitle06.setText(Html.fromHtml("开始时间: "));
			tvTitle07.setText(Html.fromHtml("保险费用: "));
			tvTitle08.setText(Html.fromHtml("产权单位: "));
			tvTitle09.setText("提前预警天数: ");
			tvTitle10.setText(Html.fromHtml("号牌号码: "));
			tvTitle11.setText(Html.fromHtml("商业险费: "));
			tvTitle12.setText(Html.fromHtml("保险费小计: "));

			tvContent01.setText(Tools.isEmpty(bean.deviceCode));
			tvContent02.setText(Tools.isEmpty(bean.deviceModel));
			tvContent03.setText(Tools.isEmpty(bean.deviceName));
			tvContent04.setText(Tools.isEmpty(bean.shipPremium) + "元");
			tvContent05.setText(Tools.isEmpty(bean.insureCompany));
			tvContent06.setText(Tools.isEmpty(bean.startDate));
			tvContent07.setText(Tools.isEmpty(bean.premium) + "元");
			tvContent08.setText(Tools.isEmpty(bean.bcompanyName));
			tvContent09.setText(String.valueOf(bean.warningDays));
			tvContent10.setText(Tools.isEmpty(bean.plate));
			tvContent11.setText(Tools.isEmpty(bean.businessPremium) + "元");
			tvContent12.setText(Tools.isEmpty(bean.premiumSum) + "元");
		}
	}

	@Override
	public void initListener() {
		titleView.setLeftBackOneListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}

}
