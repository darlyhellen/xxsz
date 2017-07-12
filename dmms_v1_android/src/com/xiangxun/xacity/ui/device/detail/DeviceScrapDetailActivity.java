package com.xiangxun.xacity.ui.device.detail;

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
import com.xiangxun.xacity.adapter.DeviceVerifyRecordAdapter;
import com.xiangxun.xacity.app.BaseActivity;
import com.xiangxun.xacity.bean.ResponseResultBeans.DeviceScrapBean;
import com.xiangxun.xacity.bean.ResponseResultBeans.DeviceScrapVerify;
import com.xiangxun.xacity.common.ConstantStatus;
import com.xiangxun.xacity.common.DcHttpClient;
import com.xiangxun.xacity.request.DcNetWorkUtils;
import com.xiangxun.xacity.utils.Tools;
import com.xiangxun.xacity.view.MsgToast;
import com.xiangxun.xacity.view.TabTopView;
import com.xiangxun.xacity.view.TitleView;
import com.xiangxun.xacity.volley.toolbox.NetworkImageView;

/**
 * @package: com.xiangxun.xacity.ui.device.detail
 * @ClassName: DeviceScrapDetailActivity.java
 * @Description: 设备报废详情页面
 * @author: HanGJ
 * @date: 2016-2-24 上午11:51:06
 */
public class DeviceScrapDetailActivity extends BaseActivity implements OnClickListener {
	private TitleView titleView;
	private TabTopView tabtopView;
	private ViewFlipper mVF;
	private ListView mListView;
	private LinearLayout ll_image;
	private TextView tv_image_name;
	private NetworkImageView mFile;
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

	private String id = "";
	private DeviceVerifyRecordAdapter adapter;
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ConstantStatus.DeviceHomeSuccess:
				@SuppressWarnings("unchecked")
				List<DeviceScrapVerify> verifyList = (List<DeviceScrapVerify>) msg.obj;
				if (verifyList != null && verifyList.size() > 0) {
					setData(verifyList);
				} else {
					mVF.setDisplayedChild(2);
				}
				break;

			case ConstantStatus.DeviceHomeFailed:
				MsgToast.geToast().setMsg("数据获取失败");
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
		setContentView(R.layout.device_maintain_detail_layout);
		initView();
		initData();
		initListener();
	}

	protected void setData(List<DeviceScrapVerify> verifyList) {
		adapter.setData(verifyList);
	}

	@Override
	public void initView() {
		titleView = (TitleView) findViewById(R.id.comment_title);
		tabtopView = (TabTopView) findViewById(R.id.tabtopView);
		mVF = (ViewFlipper) findViewById(R.id.viewFlipper);
		mListView = (ListView) findViewById(R.id.xlistview);
		ll_image = (LinearLayout) findViewById(R.id.ll_image);
		mFile = (NetworkImageView) findViewById(R.id.iv_file);
		tv_image_name = (TextView) findViewById(R.id.tv_image_name);
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
		titleView.setTitle("设备报废--详情");
		ll_view01.setVisibility(View.VISIBLE);
		ll_view02.setVisibility(View.VISIBLE);
		ll_view03.setVisibility(View.VISIBLE);
		ll_view04.setVisibility(View.VISIBLE);
		ll_view05.setVisibility(View.VISIBLE);
		ll_view06.setVisibility(View.VISIBLE);
		DeviceScrapBean bean = (DeviceScrapBean) getIntent().getSerializableExtra("bean");
		adapter = new DeviceVerifyRecordAdapter(this);
		mListView.setAdapter(adapter);
		id = bean.id;
		tabtopView.setVisibility(View.VISIBLE);
		tabtopView.setTabText("设备报废详情", "审核记录列表");
		tabtopView.OnClickLeftTab();
		tvTitle01.setText("累计使用年限: ");
		tvTitle02.setText("设备名称: ");
		tvTitle03.setText("资产编号: ");
		tvTitle04.setText(Html.fromHtml("设备型号: "));
		tvTitle05.setText("购置时间: ");
		tvTitle06.setText(Html.fromHtml("报废单位: "));
		tvTitle07.setText("报废原因: ");
		tvTitle08.setText("鉴定结果: ");
		tvTitle09.setText("申请日期: ");
		tvTitle10.setText("审核状态: ");
		tvTitle11.setText("号牌号码: ");
		tvTitle12.setText(Html.fromHtml("申&#160;&#160;请&#160;&#160;人: "));
		tvTitle13.setText("生产厂家: ");
		tvTitle14.setText("设备原值: ");

		tvContent01.setText(bean.usingCount + "年");
		tvContent02.setText(Tools.isEmpty(bean.deviceName));
		tvContent03.setText(Tools.isEmpty(bean.deviceCode));
		tvContent04.setText(Tools.isEmpty(bean.deviceModel));
		tvContent05.setText(Tools.isEmpty(bean.buyDateTime));
		tvContent06.setText(Tools.isEmpty(bean.scrapCompanyName));
		tvContent07.setText(Tools.isEmpty(bean.reason));
		tvContent08.setText(Tools.isEmpty(bean.appraisalResult));
		tvContent09.setText(Tools.isEmpty(bean.dateTime));
		tvContent10.setText(Tools.isEmpty(bean.statusHtml));
		tvContent11.setText(Tools.isEmpty(bean.plate));
		tvContent12.setText(Tools.isEmpty(bean.proposer));
		tvContent13.setText(Tools.isEmpty(bean.manufacturers));
		tvContent14.setText(Tools.isEmpty(bean.original) + "元");
		if (bean.photo != null && bean.photo.length() > 0) {
			ll_image.setVisibility(View.VISIBLE);
			tv_image_name.setText("拟报废设备图片: ");
			DcHttpClient.getInstance().getImageForNIView(bean.photo, mFile, R.drawable.view_pager_default);
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
			RequestList();
			break;
		}
	}

	private void RequestList() {
		DcNetWorkUtils.getDeviceScrapVerifyList(this, handler, account, password, id);
	}

}
