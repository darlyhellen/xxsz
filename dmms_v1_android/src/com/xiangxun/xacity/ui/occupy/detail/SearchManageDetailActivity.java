package com.xiangxun.xacity.ui.occupy.detail;

import java.util.List;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.xiangxun.xacity.R;
import com.xiangxun.xacity.app.BaseActivity;
import com.xiangxun.xacity.bean.ResponseResultBeans.ImageBean;
import com.xiangxun.xacity.bean.ResponseResultBeans.OccupySearchBean;
import com.xiangxun.xacity.common.DcHttpClient;
import com.xiangxun.xacity.request.Api;
import com.xiangxun.xacity.utils.Tools;
import com.xiangxun.xacity.view.FullScreenSlidePopupWindow;
import com.xiangxun.xacity.view.TabTopView;
import com.xiangxun.xacity.view.TitleView;
import com.xiangxun.xacity.volley.toolbox.NetworkImageView;

/**
 * @package: com.xiangxun.xacity.ui.occupy.detail
 * @ClassName: SearchManageDetailActivity.java
 * @Description: 查询管理详情页面
 * @author: HanGJ
 * @date: 2016-3-2 下午5:07:21
 */
public class SearchManageDetailActivity extends BaseActivity implements OnClickListener {
	private TitleView titleView;
	private String title;
	private int childAt;
	private TabTopView tabtopView;
	private ViewFlipper mVF;
	private LinearLayout ll_view01;
	private LinearLayout ll_view02;
	private LinearLayout ll_view03;
	private LinearLayout ll_view04;
	private LinearLayout ll_view05;
	private LinearLayout ll_view06;
	private LinearLayout ll_view07;
	private LinearLayout ll_view08;
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
	/**************** 延期/勘察信息控件 **********************/
	private LinearLayout ll_occupy_ramark;
	private LinearLayout ll_image_type;
	private NetworkImageView mFile;
	private TextView tv_title_oprator;
	private TextView tv_content_oprator;
	private TextView tv_title_oprator_time;
	private TextView tv_content_oprator_time;
	private TextView tv_title_oprator_dec;
	private TextView tv_content_oprator_dec;

	private TextView tv_title_oprator_date;
	private TextView tv_content_oprator_date;
	private TextView tv_title_ramark;
	private TextView tv_content_ramark;
	private TextView tv_title_image_type;
	private TextView tv_content_image_type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.occupy_search_detail_layout);
		initView();
		initData();
		initListener();
	}

	@Override
	public void initView() {
		titleView = (TitleView) findViewById(R.id.comment_title);
		tabtopView = (TabTopView) findViewById(R.id.tabtopView);
		mVF = (ViewFlipper) findViewById(R.id.viewFlipper);
		ll_view01 = (LinearLayout) findViewById(R.id.ll_other_view01);
		ll_view02 = (LinearLayout) findViewById(R.id.ll_other_view02);
		ll_view03 = (LinearLayout) findViewById(R.id.ll_other_view03);
		ll_view04 = (LinearLayout) findViewById(R.id.ll_other_view04);
		ll_view05 = (LinearLayout) findViewById(R.id.ll_other_view05);
		ll_view06 = (LinearLayout) findViewById(R.id.ll_other_view06);
		ll_view07 = (LinearLayout) findViewById(R.id.ll_other_view07);
		ll_view08 = (LinearLayout) findViewById(R.id.ll_other_view08);
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
		//
		mFile = (NetworkImageView) findViewById(R.id.iv_file);
		ll_occupy_ramark = (LinearLayout) findViewById(R.id.ll_occupy_ramark);
		ll_image_type = (LinearLayout) findViewById(R.id.ll_image_type);
		tv_title_oprator = (TextView) findViewById(R.id.tv_title_oprator);
		tv_content_oprator = (TextView) findViewById(R.id.tv_content_oprator);
		tv_title_oprator_time = (TextView) findViewById(R.id.tv_title_oprator_time);
		tv_content_oprator_time = (TextView) findViewById(R.id.tv_content_oprator_time);
		tv_title_oprator_dec = (TextView) findViewById(R.id.tv_title_oprator_dec);
		tv_content_oprator_dec = (TextView) findViewById(R.id.tv_content_oprator_dec);
		tv_title_oprator_date = (TextView) findViewById(R.id.tv_title_oprator_date);
		tv_content_oprator_date = (TextView) findViewById(R.id.tv_content_oprator_date);
		tv_title_ramark = (TextView) findViewById(R.id.tv_title_ramark);
		tv_content_ramark = (TextView) findViewById(R.id.tv_content_ramark);
		tv_title_image_type = (TextView) findViewById(R.id.tv_title_image_type);
		tv_content_image_type = (TextView) findViewById(R.id.tv_content_image_type);
	}

	@Override
	public void initData() {
		title = getIntent().getStringExtra("title");
		childAt = getIntent().getIntExtra("childAt", 0);
		titleView.setTitle(title + "--详情");
		tabtopView.OnClickLeftTab();
		OccupySearchBean searchBean = (OccupySearchBean) getIntent().getSerializableExtra("bean");
		if (childAt == 0) {
			tabtopView.setTabText("项目详情", "延期信息");
			setViewRightData(searchBean);
		} else if (childAt == 1) {
			tabtopView.setTabText("项目详情", "勘察信息");
			setViewRightData(searchBean);
		} else {
			tabtopView.setVisibility(View.GONE);
		}
		setViewData(searchBean);
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

	/**
	 * 延期/勘察信息数据
	 * 
	 * @param searchBean
	 */
	private void setViewRightData(OccupySearchBean searchBean) {
		if (childAt == 0) {
			tv_title_oprator.setText("操作人员: ");
			tv_content_oprator.setText(Tools.isEmpty(searchBean.operator));
			tv_title_oprator_time.setText("操作时间: ");
			tv_content_oprator_time.setText(Tools.isEmpty(searchBean.applyTime));
			tv_title_oprator_dec.setText("延期原因: ");
			tv_content_oprator_dec.setText(Tools.isEmpty(searchBean.reasonName));
			tv_title_oprator_date.setText("截止时间: ");
			tv_content_oprator_date.setText(Tools.isEmpty(searchBean.deadtimeStr));
			if (searchBean.picPath != null && searchBean.picPath.length() > 0) {
				DcHttpClient.getInstance().getImageForNIView(Api.urlImage + searchBean.picPath, mFile, R.drawable.view_pager_default);
			} else {
				mFile.setBackgroundResource(R.drawable.view_pager_default);
			}
		} else {
			ll_occupy_ramark.setVisibility(View.VISIBLE);
			ll_image_type.setVisibility(View.VISIBLE);
			tv_title_oprator.setText("勘察人员: ");
			tv_content_oprator.setText(Tools.isEmpty(searchBean.inspector));
			tv_title_oprator_time.setText("勘察时间: ");
			tv_content_oprator_time.setText(Tools.isEmpty(searchBean.inspectTime));
			tv_title_oprator_dec.setText("勘察说明: ");
			tv_content_oprator_dec.setText(Tools.isEmpty(searchBean.illustrationName));
			tv_title_oprator_date.setText("操作时间: ");
			tv_content_oprator_date.setText(Tools.isEmpty(searchBean.opratorTime));
			tv_title_ramark.setText(Html.fromHtml("备&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;注: "));
			tv_content_ramark.setText(Tools.isEmpty(searchBean.remark));
			tv_title_image_type.setText("图片类型: ");
			tv_content_image_type.setText(Tools.isEmpty(searchBean.picType));
			if (searchBean.picList != null) {
				final List<ImageBean> picList = searchBean.picList;
				if (picList.size() > 0) {
					DcHttpClient.getInstance().getImageForNIView(Api.urlImage + picList.get(0).img, mFile, R.drawable.view_pager_default);
					mFile.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {

							new FullScreenSlidePopupWindow(SearchManageDetailActivity.this, picList).startFullScreenSlide(0);
						}
					});
				}

			} else {
				mFile.setBackgroundResource(R.drawable.view_pager_default);
			}
		}
	}

	/**
	 * 延期/勘察/更改状态详情数据
	 * 
	 * @param searchBean
	 */
	private void setViewData(OccupySearchBean searchBean) {
		if (childAt == 0) {
			ll_view01.setVisibility(View.VISIBLE);
			tvTitle01.setText("项目名称: ");
			tvTitle02.setText("项目编号: ");
			tvTitle03.setText("施工类别: ");
			tvTitle04.setText(Html.fromHtml("道路名称: "));
			tvTitle05.setText("建设单位: ");
			tvTitle06.setText(Html.fromHtml("建设单位负责人: "));
			tvTitle07.setText("施工单位: ");
			tvTitle08.setText("施工单位负责人: ");
			tvTitle09.setText(Html.fromHtml("电&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;话: "));

			tvContent01.setText(Tools.isEmpty(searchBean.pname));
			tvContent02.setText(Tools.isEmpty(searchBean.pcode));
			tvContent03.setText(Tools.isEmpty(searchBean.occupyType));
			tvContent04.setText(Tools.isEmpty(searchBean.roadId));
			tvContent05.setText(Tools.isEmpty(searchBean.applicantName));
			tvContent06.setText(Tools.isEmpty(searchBean.aleadingOfficial));
			tvContent07.setText(Tools.isEmpty(searchBean.builderName));
			tvContent08.setText(Tools.isEmpty(searchBean.bleadingOfficial));
			tvContent09.setText(Tools.isEmpty(searchBean.mobile));
		} else if (childAt == 1) {
			ll_view01.setVisibility(View.VISIBLE);
			ll_view02.setVisibility(View.VISIBLE);
			tvTitle01.setText("项目名称: ");
			tvTitle02.setText("项目编号: ");
			tvTitle03.setText("施工类别: ");
			tvTitle04.setText(Html.fromHtml("道路名称: "));
			tvTitle05.setText("建设单位: ");
			tvTitle06.setText(Html.fromHtml("建设单位负责人: "));
			tvTitle07.setText("施工单位: ");
			tvTitle08.setText("施工单位负责人: ");
			tvTitle09.setText(Html.fromHtml("电&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;话: "));
			tvTitle10.setText(Html.fromHtml("辖&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;区: "));

			tvContent01.setText(Tools.isEmpty(searchBean.pname));
			tvContent02.setText(Tools.isEmpty(searchBean.pcode));
			tvContent03.setText(Tools.isEmpty(searchBean.occupyType));
			tvContent04.setText(Tools.isEmpty(searchBean.roadId));
			tvContent05.setText(Tools.isEmpty(searchBean.applicantName));
			tvContent06.setText(Tools.isEmpty(searchBean.aleadingOfficial));
			tvContent07.setText(Tools.isEmpty(searchBean.builderName));
			tvContent08.setText(Tools.isEmpty(searchBean.bleadingOfficial));
			tvContent09.setText(Tools.isEmpty(searchBean.mobile));
			tvContent10.setText(Tools.isEmpty(searchBean.managearea));
		} else {
			ll_view01.setVisibility(View.VISIBLE);
			ll_view02.setVisibility(View.VISIBLE);
			ll_view03.setVisibility(View.VISIBLE);
			ll_view04.setVisibility(View.VISIBLE);
			ll_view05.setVisibility(View.VISIBLE);
			ll_view06.setVisibility(View.VISIBLE);
			ll_view07.setVisibility(View.VISIBLE);
			ll_view08.setVisibility(View.VISIBLE);
			tvTitle01.setText("项目名称: ");
			tvTitle02.setText("建设单位: ");
			tvTitle03.setText("施工单位: ");
			tvTitle04.setText(Html.fromHtml("施工类别: "));
			tvTitle05.setText("道路名称: ");
			tvTitle06.setText(Html.fromHtml("更改前状态: "));
			tvTitle07.setText("更改后状态: ");
			tvTitle08.setText("施工单位负责人: ");
			tvTitle09.setText(Html.fromHtml("电&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;话: "));
			tvTitle10.setText(Html.fromHtml("操作账号: "));
			tvTitle11.setText(Html.fromHtml("操作时间: "));
			tvTitle12.setText(Html.fromHtml("建设/施工单位签字: "));
			tvTitle13.setText(Html.fromHtml("签字时间: "));
			tvTitle14.setText(Html.fromHtml("中队签字: "));
			tvTitle15.setText(Html.fromHtml("签字时间: "));
			tvTitle16.setText(Html.fromHtml("完工情况说明: "));

			tvContent01.setText(Tools.isEmpty(searchBean.pname));
			tvContent02.setText(Tools.isEmpty(searchBean.applicantName));
			tvContent03.setText(Tools.isEmpty(searchBean.builderId));
			tvContent04.setText(Tools.isEmpty(searchBean.type));
			tvContent05.setText(Tools.isEmpty(searchBean.roadname));
			tvContent06.setText(Tools.isEmpty(searchBean.beforeStatus));
			tvContent07.setText(Tools.isEmpty(searchBean.afterStatus));
			tvContent08.setText(Tools.isEmpty(searchBean.principal));
			tvContent09.setText(Tools.isEmpty(searchBean.mobile));
			tvContent10.setText(Tools.isEmpty(searchBean.operator));
			tvContent11.setText(Tools.isEmpty(searchBean.updateTime));

			tvContent12.setText(Tools.isEmpty(searchBean.builderName));
			tvContent13.setText(Tools.isEmpty(searchBean.builderSignDate));
			tvContent14.setText(Tools.isEmpty(searchBean.detachmentName));
			tvContent15.setText(Tools.isEmpty(searchBean.detachmentSignDate));
			tvContent16.setText(Tools.isEmpty(searchBean.completeExplain));
		}
	}

}
