package com.xiangxun.xacity.ui.device.detail;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiangxun.xacity.R;
import com.xiangxun.xacity.app.BaseActivity;
import com.xiangxun.xacity.bean.ResponseResultBeans.DeviceSpareOilBean;
import com.xiangxun.xacity.utils.Tools;
import com.xiangxun.xacity.view.TitleView;

/**
 * @package: com.xiangxun.xacity.ui.device.detail
 * @ClassName: DeviceSpareOilDetailActivity.java
 * @Description: 备件油料詳情頁面
 * @author: HanGJ
 * @date: 2016-2-24 下午2:38:17
 */
public class DeviceSpareOilDetailActivity extends BaseActivity {
	private TitleView titleView;
	private int childAt;
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
		String title = getIntent().getStringExtra("title");
		childAt = getIntent().getIntExtra("childAt", 0);
		titleView.setTitle(title + "--详情");
		DeviceSpareOilBean bean = (DeviceSpareOilBean) getIntent().getSerializableExtra("bean");
		setViewData(bean);
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

	private void setViewData(DeviceSpareOilBean bean) {
		if (childAt == 0) {
			ll_view01.setVisibility(View.VISIBLE);
			ll_view02.setVisibility(View.VISIBLE);
			ll_view03.setVisibility(View.VISIBLE);
			ll_view04.setVisibility(View.VISIBLE);
			ll_view05.setVisibility(View.VISIBLE);
			ll_view06.setVisibility(View.VISIBLE);
			tvTitle01.setText(Html.fromHtml("材料名称: "));
			tvTitle02.setText(Html.fromHtml("型&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;号: "));
			tvTitle03.setText(Html.fromHtml("数&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;量: "));
			tvTitle04.setText(Html.fromHtml("单&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;位: "));
			tvTitle05.setText(Html.fromHtml("供应单位: "));
			tvTitle06.setText(Html.fromHtml("单&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;价: "));
			tvTitle07.setText(Html.fromHtml("计量单位: "));
			tvTitle08.setText(Html.fromHtml("质量证明编号: "));
			tvTitle09.setText("检验状态: ");
			tvTitle10.setText("检验内容: ");
			tvTitle11.setText(Html.fromHtml("检&#160;&#160;验&#160;&#160;人: "));
			tvTitle12.setText(Html.fromHtml("采&#160;&#160;购&#160;&#160;人: "));
			tvTitle13.setText(Html.fromHtml("采购日期: "));
			tvTitle14.setText(Html.fromHtml("存放地点: "));

			tvContent01.setText(Tools.isEmpty(bean.name));
			tvContent02.setText(Tools.isEmpty(bean.model));
			tvContent03.setText(Tools.isEmpty(bean.quantity));
			tvContent04.setText(Tools.isEmpty(bean.oname));
			tvContent05.setText(Tools.isEmpty(bean.sellOrganization));
			tvContent06.setText(Tools.isEmpty(bean.price));
			tvContent07.setText(Tools.isEmpty(bean.unit));
			tvContent08.setText(Tools.isEmpty(bean.proveCode));
			if ("0".equals(bean.checkStatus)) {
				tvContent09.setText("完好");
			} else if ("1".equals(bean.checkStatus)) {
				tvContent09.setText("良好");
			} else if ("2".equals(bean.checkStatus)) {
				tvContent09.setText("较差");
			} else {
				tvContent09.setText("很差");
			}
			tvContent10.setText(Tools.isEmpty(bean.checkContent));
			tvContent11.setText(Tools.isEmpty(bean.inspectPerson));
			tvContent12.setText(Tools.isEmpty(bean.buyer));
			tvContent13.setText(Tools.isEmpty(bean.dateTime));
			tvContent14.setText(Tools.isEmpty(bean.depositAddress));
		} else if (childAt == 1) {
			ll_view01.setVisibility(View.VISIBLE);
			ll_view02.setVisibility(View.VISIBLE);
			tvTitle01.setText(Html.fromHtml("领料单号: "));
			tvTitle02.setText(Html.fromHtml("材料名称: "));
			tvTitle03.setText(Html.fromHtml("规格型号: "));
			tvTitle04.setText(Html.fromHtml("数&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;量: "));
			tvTitle05.setText(Html.fromHtml("单&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;位: "));
			tvTitle06.setText(Html.fromHtml("使用部门: "));
			tvTitle07.setText(Html.fromHtml("领&#160;&#160;料&#160;&#160;人: "));
			tvTitle08.setText("领料日期: ");
			tvTitle09.setText(Html.fromHtml("物料用途: "));
			tvTitle10.setText(Html.fromHtml("备&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;注: "));

			tvContent01.setText(Tools.isEmpty(bean.code));
			tvContent02.setText(Tools.isEmpty(bean.name));
			tvContent03.setText(Tools.isEmpty(bean.model));
			tvContent04.setText(Tools.isEmpty(bean.quantity));
			tvContent05.setText(Tools.isEmpty(bean.oname));
			tvContent06.setText(Tools.isEmpty(bean.userPlace));
			tvContent07.setText(Tools.isEmpty(bean.getPerson));
			tvContent08.setText(Tools.isEmpty(bean.dateTime));
			tvContent09.setText(Tools.isEmpty(bean.projectName));
			tvContent10.setText(Tools.isEmpty(bean.remark));
		} else if (childAt == 2) {
			ll_view01.setVisibility(View.VISIBLE);
			ll_view02.setVisibility(View.VISIBLE);
			tvTitle01.setText(Html.fromHtml("设备名称: "));
			tvTitle02.setText(Html.fromHtml("型&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;号: "));
			tvTitle03.setText(Html.fromHtml("单位名称: "));
			tvTitle04.setText(Html.fromHtml("牌照号码: "));
			tvTitle05.setText(Html.fromHtml("驾&#160;&#160;驶&#160;&#160;员: "));
			tvTitle06.setText(Html.fromHtml("油&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;品: "));
			tvTitle07.setText(Html.fromHtml("油&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;号: "));
			tvTitle08.setText(Html.fromHtml("加油量/升: "));
			tvTitle09.setText(Html.fromHtml("金&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;额: "));
			tvTitle10.setText(Html.fromHtml("加油时间: "));

			tvContent01.setText(Tools.isEmpty(bean.name));
			tvContent02.setText(Tools.isEmpty(bean.model));
			tvContent03.setText(Tools.isEmpty(bean.orgName));
			tvContent04.setText(Tools.isEmpty(bean.plate));
			tvContent05.setText(Tools.isEmpty(bean.driver));
			tvContent06.setText(Tools.isEmpty(bean.fat));
			tvContent07.setText(Tools.isEmpty(bean.fatModel));
			tvContent08.setText(Tools.isEmpty(bean.measure));
			tvContent09.setText(Tools.isEmpty(bean.money) + "元");
			tvContent10.setText(Tools.isEmpty(bean.dateTime));
		} else {
			ll_view01.setVisibility(View.VISIBLE);
			tvTitle01.setVisibility(View.GONE);
			tvTitle02.setText(Html.fromHtml("材料名称: "));
			tvTitle03.setText(Html.fromHtml("型&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;号: "));
			tvTitle04.setText(Html.fromHtml("数&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;量: "));
			tvTitle05.setText(Html.fromHtml("单&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;位: "));
			tvTitle06.setText(Html.fromHtml("申&#160;&#160;请&#160;&#160;人: "));
			tvTitle07.setText(Html.fromHtml("物料用途: "));
			tvTitle08.setText(Html.fromHtml("备&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;注: "));
			tvTitle09.setText(Html.fromHtml("申请时间: "));

			tvContent01.setVisibility(View.GONE);
			tvContent02.setText(Tools.isEmpty(bean.name));
			tvContent03.setText(Tools.isEmpty(bean.model));
			tvContent04.setText(Tools.isEmpty(bean.num));
			tvContent05.setText(Tools.isEmpty(bean.companyName));
			tvContent06.setText(Tools.isEmpty(bean.drawPerson));
			tvContent07.setText(Tools.isEmpty(bean.projectName));
			tvContent08.setText(Tools.isEmpty(bean.remark));
			tvContent09.setText(Tools.isEmpty(bean.dateTime));
		}
	}

}
