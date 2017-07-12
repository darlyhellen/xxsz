package com.xiangxun.xacity.ui.device.detail;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.xiangxun.xacity.R;
import com.xiangxun.xacity.adapter.DeviceMaintainRecordAdapter;
import com.xiangxun.xacity.app.BaseActivity;
import com.xiangxun.xacity.bean.ResponseResultBeans.DeviceMaintainBean;
import com.xiangxun.xacity.bean.ResponseResultBeans.ImageBean;
import com.xiangxun.xacity.bean.ResponseResultBeans.MaintainRecord;
import com.xiangxun.xacity.common.DcHttpClient;
import com.xiangxun.xacity.utils.Tools;
import com.xiangxun.xacity.view.FullScreenSlidePopupWindow;
import com.xiangxun.xacity.view.TabTopView;
import com.xiangxun.xacity.view.TitleView;
import com.xiangxun.xacity.volley.toolbox.NetworkImageView;

/**
 * @package: com.xiangxun.xacity.ui.device.detail
 * @ClassName: DeviceMaintainDetailActivity.java
 * @Description: 设备维修详情页面
 * @author: HanGJ
 * @date: 2016-2-24 下午2:35:35
 */
public class DeviceMaintainDetailActivity extends BaseActivity implements OnClickListener {
	private TitleView titleView;
	private int childAt;
	private LinearLayout ll_image;
	private TextView tv_image_name;
	private TabTopView tabtopView;
	private ViewFlipper mVF;
	private ListView mListView;
	private NetworkImageView mFile;
	private LinearLayout ll_view01;
	private LinearLayout ll_view02;
	private LinearLayout ll_view03;
	private LinearLayout ll_view04;
	private LinearLayout ll_view05;
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
	private List<MaintainRecord> maintainList;
	private DeviceMaintainRecordAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.device_maintain_detail_layout);
		initView();
		initData();
		initListener();
	}

	@Override
	public void initView() {
		titleView = (TitleView) findViewById(R.id.comment_title);
		ll_image = (LinearLayout) findViewById(R.id.ll_image);
		mFile = (NetworkImageView) findViewById(R.id.iv_file);
		tabtopView = (TabTopView) findViewById(R.id.tabtopView);
		mVF = (ViewFlipper) findViewById(R.id.viewFlipper);
		mListView = (ListView) findViewById(R.id.xlistview);
		ll_view01 = (LinearLayout) findViewById(R.id.ll_other_view01);
		ll_view02 = (LinearLayout) findViewById(R.id.ll_other_view02);
		ll_view03 = (LinearLayout) findViewById(R.id.ll_other_view03);
		ll_view04 = (LinearLayout) findViewById(R.id.ll_other_view04);
		ll_view05 = (LinearLayout) findViewById(R.id.ll_other_view05);
		/*************************************************/
		tv_image_name = (TextView) findViewById(R.id.tv_image_name);
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
	}

	@Override
	public void initData() {
		String title = getIntent().getStringExtra("title");
		childAt = getIntent().getIntExtra("childAt", 0);
		titleView.setTitle(title + "--详情");
		if (childAt == 2) {
			adapter = new DeviceMaintainRecordAdapter(this);
			maintainList = new ArrayList<MaintainRecord>();
			tabtopView.setVisibility(View.VISIBLE);
			tabtopView.setTabText("项目记录详情", "维修项目详情");
			tabtopView.OnClickLeftTab();
		} else {
			tabtopView.setVisibility(View.GONE);
		}
		DeviceMaintainBean bean = (DeviceMaintainBean) getIntent().getSerializableExtra("bean");
		setViewData(bean);
	}

	private void setViewData(DeviceMaintainBean bean) {
		if (childAt == 0) {// 设备维修计划
			tvTitle01.setText("资产编号: ");
			tvTitle02.setText("设备名称: ");
			tvTitle03.setText("牌照号码: ");
			tvTitle04.setText("计划维修地点: ");
			tvTitle05.setText("计划类型: ");
			tvTitle06.setText(Html.fromHtml("上次维修时间: "));
			tvTitle07.setText("下次维修时间: ");
			tvTitle08.setText(Html.fromHtml("维修内容: "));

			tvContent01.setText(Tools.isEmpty(bean.deviceCode));
			tvContent02.setText(Tools.isEmpty(bean.deviceName));
			tvContent03.setText(Tools.isEmpty(bean.plate));
			tvContent04.setText(Tools.isEmpty(bean.belongtoCompany));
			if ("0".equals(bean.status)) {
				tvContent05.setText("定期保养");
			} else {
				tvContent05.setText("大中修");
			}
			tvContent06.setText(Tools.isEmpty(bean.lastTime));
			tvContent07.setText(Tools.isEmpty(bean.nextTime));
			tvContent08.setText(Tools.isEmpty(bean.content));
		} else if (childAt == 1) {// 维修工单
			ll_view01.setVisibility(View.VISIBLE);
			ll_view02.setVisibility(View.VISIBLE);
			ll_view03.setVisibility(View.VISIBLE);
			tvTitle01.setText("维修单号: ");
			tvTitle02.setText("设备名称: ");
			tvTitle03.setText("资产编号: ");
			tvTitle04.setText(Html.fromHtml("派&#160;&#160;单&#160;&#160;人: "));
			tvTitle05.setText(Html.fromHtml("送&#160;&#160;修&#160;&#160;人: "));
			tvTitle06.setText("维修单位: ");
			tvTitle07.setText("维修日期: ");
			tvTitle08.setText("维修预算: ");
			tvTitle09.setText("维修项目: ");
			tvTitle10.setText("牌照号码: ");
			tvTitle11.setText("行驶公里/小时：: ");

			tvContent01.setText(Tools.isEmpty(bean.code));
			tvContent02.setText(Tools.isEmpty(bean.deviceName));
			tvContent03.setText(Tools.isEmpty(bean.deviceCode));
			tvContent04.setText(Tools.isEmpty(bean.recipient));
			tvContent05.setText(Tools.isEmpty(bean.sendRepair));
			tvContent06.setText(Tools.isEmpty(bean.pepairAddress));
			tvContent07.setText(Tools.isEmpty(bean.entranceDateTime));
			tvContent08.setText(Tools.isEmpty(bean.maintainCost + "元"));
			tvContent09.setText(Tools.isEmpty(bean.overhaulItme));
			tvContent10.setText(Tools.isEmpty(bean.plate));
			tvContent11.setText(Tools.isEmpty(bean.runKm));
		} else if (childAt == 2) {// 维修记录
			List<MaintainRecord> maintainList = bean.maintainList;
			if (maintainList != null && maintainList.size() > 0) {
				this.maintainList.addAll(maintainList);
			}
			adapter.setData(this.maintainList);
			mListView.setAdapter(adapter);
			ll_view01.setVisibility(View.VISIBLE);
			tvTitle01.setText("资产编号: ");
			tvTitle02.setText("设备名称: ");
			tvTitle03.setText("维修时间: ");
			tvTitle04.setText("使用单位: ");
			tvTitle05.setText("维修费用: ");
			tvTitle06.setText("维修地点: ");
			tvTitle07.setText("维修项目: ");
			tvTitle08.setText("牌照号码: ");
			tvTitle09.setText(Html.fromHtml("设备型号: "));

			tvContent01.setText(Tools.isEmpty(bean.deviceCode));
			tvContent02.setText(Tools.isEmpty(bean.deviceName));
			tvContent03.setText(Tools.isEmpty(bean.dateTime));
			tvContent04.setText(Tools.isEmpty(bean.usingCompanyName));
			tvContent05.setText(Tools.isEmpty(bean.cost + "元"));
			tvContent06.setText(Tools.isEmpty(bean.address));
			tvContent07.setText(Tools.isEmpty(bean.project));
			tvContent08.setText(Tools.isEmpty(bean.plate));
			tvContent09.setText(Tools.isEmpty(bean.model));
		} else { // 事故报告记录
			ll_view01.setVisibility(View.VISIBLE);
			ll_view02.setVisibility(View.VISIBLE);
			ll_view03.setVisibility(View.VISIBLE);
			ll_view04.setVisibility(View.VISIBLE);
			ll_view05.setVisibility(View.VISIBLE);
			tvTitle01.setText("责任划分: ");
			tvTitle02.setText("设备名称: ");
			tvTitle03.setText("资产编号: ");
			tvTitle04.setText("使用单位: ");
			tvTitle05.setText(Html.fromHtml("肇&#160;&#160;事&#160;&#160;人: "));
			tvTitle06.setText("经济损失: ");
			tvTitle07.setText("事发地点: ");
			tvTitle08.setText("事故描述: ");
			tvTitle09.setText(Html.fromHtml("处理报告: "));
			tvTitle10.setText(Html.fromHtml("事发时间: "));
			tvTitle11.setText(Html.fromHtml("牌照号码: "));
			tvTitle12.setText(Html.fromHtml("保险公司: "));
			tvTitle13.setText(Html.fromHtml("处理交警大队: "));

			tvContent01.setText(Tools.isEmpty(bean.dutyDivide));
			tvContent02.setText(Tools.isEmpty(bean.deviceName));
			tvContent03.setText(Tools.isEmpty(bean.deviceCode));
			tvContent04.setText(Tools.isEmpty(bean.usingCompanyName));
			tvContent05.setText(Tools.isEmpty(bean.principal));
			tvContent06.setText(Tools.isEmpty(bean.loss + "元"));
			tvContent07.setText(Tools.isEmpty(bean.address));
			tvContent08.setText(Tools.isEmpty(bean.describe));
			tvContent09.setText(Tools.isEmpty(bean.dispose));
			tvContent10.setText(Tools.isEmpty(bean.dateTime));
			tvContent11.setText(Tools.isEmpty(bean.plate));
			tvContent12.setText(Tools.isEmpty(bean.insureCompany));
			tvContent13.setText(Tools.isEmpty(bean.dealTrafficPolice));
			if (bean.photo != null && bean.photo.length() > 0) {
				ll_image.setVisibility(View.VISIBLE);
				tv_image_name.setText("现场图片: ");
				if (bean.photo.contains("http://")) {
					DcHttpClient.getInstance().getImageForNIView(bean.photo, mFile, R.drawable.view_pager_default);
				} else {
					mFile.setBackgroundResource(R.drawable.view_pager_default);
				}
				if (bean.picList != null) {
					final List<ImageBean> picList = bean.picList;
					if (picList.size() > 0) {
						mFile.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {

								new FullScreenSlidePopupWindow(DeviceMaintainDetailActivity.this, picList).startFullScreenSlide(0);
							}
						});
					}
				}
			} else {
				mFile.setBackgroundResource(R.drawable.view_pager_default);
			}
		}
	}

	@Override
	public void initListener() {
		tabtopView.OnClickLeftTabOnClickListener(this);
		tabtopView.OnClickRightTabOnClickListener(this);
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
		case R.id.toptabview_left_rlyout:
			mVF.setDisplayedChild(0);
			tabtopView.OnClickLeftTab();
			break;
		case R.id.toptabview_right_rlyout:
			mVF.setDisplayedChild(1);
			tabtopView.OnClickRightTab();
			break;
		}
	}

}
