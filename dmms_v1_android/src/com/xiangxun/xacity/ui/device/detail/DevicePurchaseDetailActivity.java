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
import com.xiangxun.xacity.bean.ResponseResultBeans.DeviceScrapVerify;
import com.xiangxun.xacity.bean.ResponseResultBeans.SupplierBean;
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
 * @ClassName: DevicePurchaseDetailActivity.java
 * @Description: 设备采购详情页面
 * @author: HanGJ
 * @date: 2016-2-20 下午3:47:44
 */
public class DevicePurchaseDetailActivity extends BaseActivity implements OnClickListener {
	private TitleView titleView;
	private String title;
	private int childAt;
	private TabTopView tabtopView;
	private ViewFlipper mVF;
	private ListView mListView;
	private LinearLayout ll_view01;
	private LinearLayout ll_view02;
	private LinearLayout ll_view03;
	private LinearLayout ll_image;
	private TextView tv_image_name;
	private NetworkImageView mFile;
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

	@Override
	public void initView() {
		titleView = (TitleView) findViewById(R.id.comment_title);
		tabtopView = (TabTopView) findViewById(R.id.tabtopView);
		mVF = (ViewFlipper) findViewById(R.id.viewFlipper);
		mListView = (ListView) findViewById(R.id.xlistview);
		ll_view01 = (LinearLayout) findViewById(R.id.ll_other_view01);
		ll_view02 = (LinearLayout) findViewById(R.id.ll_other_view02);
		ll_view03 = (LinearLayout) findViewById(R.id.ll_other_view03);
		ll_image = (LinearLayout) findViewById(R.id.ll_image);
		mFile = (NetworkImageView) findViewById(R.id.iv_file);
		tv_image_name = (TextView) findViewById(R.id.tv_image_name);
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
	}

	@Override
	public void initData() {
		title = getIntent().getStringExtra("title");
		childAt = getIntent().getIntExtra("childAt", 0);
		titleView.setTitle(title + "--详情");
		SupplierBean bean = (SupplierBean) getIntent().getSerializableExtra("bean");
		if (bean != null) {
			id = bean.id;
			if (childAt == 0) {
				ll_view01.setVisibility(View.VISIBLE);
				ll_view02.setVisibility(View.VISIBLE);
				tvTitle01.setText("供方名称: ");
				tvTitle02.setText(Html.fromHtml("产&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;品: "));
				tvTitle03.setText(Html.fromHtml("联&#160;&#160;系&#160;&#160;人: "));
				tvTitle04.setText(Html.fromHtml("电&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;话: "));
				tvTitle05.setText(Html.fromHtml("评&#160;&#160;价&#160;&#160;人: "));
				tvTitle06.setText("评价单位: ");
				tvTitle07.setText("评价日期: ");
				tvTitle08.setText(Html.fromHtml("评价内容: "));
				tvTitle09.setText(Html.fromHtml("供方地址: "));
				tvTitle10.setText(Html.fromHtml("评价结果: "));

				tvContent01.setText(Tools.isEmpty(bean.name));
				tvContent02.setText(Tools.isEmpty(bean.product));
				tvContent03.setText(Tools.isEmpty(bean.linkman));
				tvContent04.setText(Tools.isEmpty(bean.telephone));
				tvContent05.setText(Tools.isEmpty(bean.evaluateMan));
				tvContent06.setText(Tools.isEmpty(bean.evaluateUnit));
				tvContent07.setText(Tools.isEmpty(bean.dateTime));
				tvContent08.setText(Tools.isEmpty(bean.remark));
				tvContent09.setText(Tools.isEmpty(bean.address));
				tvContent10.setText(Tools.isEmpty(bean.result));
			} else if (childAt == 1) {
				tabtopView.setVisibility(View.VISIBLE);
				tabtopView.setTabText(title + "详情", "审核记录列表");
				tabtopView.OnClickLeftTab();
				adapter = new DeviceVerifyRecordAdapter(this);
				mListView.setAdapter(adapter);
				ll_view01.setVisibility(View.VISIBLE);
				ll_view02.setVisibility(View.VISIBLE);
				ll_view03.setVisibility(View.VISIBLE);
				tvTitle01.setText("采购单号: ");
				tvTitle02.setText("设备名称: ");
				tvTitle03.setText("规格型号: ");
				tvTitle04.setText(Html.fromHtml("数&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;量: "));
				tvTitle05.setText("申请单位: ");
				tvTitle06.setText(Html.fromHtml("申&#160;&#160;请&#160;&#160;人: "));
				tvTitle07.setText("申请时间: ");
				tvTitle08.setText("购置申请: ");
				tvTitle09.setText("审核状态: ");
				tvTitle10.setText("采购/租赁: ");
				tvTitle11.setText("采购预算资金: ");

				tvContent01.setText(Tools.isEmpty(bean.code));
				tvContent02.setText(Tools.isEmpty(bean.deviceName));
				tvContent03.setText(Tools.isEmpty(bean.model));
				tvContent04.setText(Tools.isEmpty(bean.num));
				tvContent05.setText(Tools.isEmpty(bean.orgName));
				tvContent06.setText(Tools.isEmpty(bean.applicant));
				tvContent07.setText(Tools.isEmpty(bean.applyDate));
				tvContent08.setText(Tools.isEmpty(bean.remark));
				tvContent09.setText(Tools.isEmpty(bean.statusHtml));
				tvContent10.setText(Tools.isEmpty(bean.ispurchase));
				tvContent11.setText(bean.budgetFund + "元");
			} else if (childAt == 2) {
				ll_view01.setVisibility(View.VISIBLE);
				ll_view02.setVisibility(View.VISIBLE);
				ll_view03.setVisibility(View.VISIBLE);
				tvTitle01.setText("验收单号: ");
				tvTitle02.setText("设备名称: ");
				tvTitle03.setText("规格型号: ");
				tvTitle04.setText(Html.fromHtml("生产厂家: "));
				tvTitle05.setText("出厂编号: ");
				tvTitle06.setText("出厂日期: ");
				tvTitle07.setText("到货日期: ");
				tvTitle08.setText("使用单位: ");
				tvTitle09.setText("使用地点: ");
				tvTitle10.setText("检查记录: ");
				tvTitle11.setText(Html.fromHtml("检&#160;&#160;验&#160;&#160;人: "));

				tvContent01.setText(Tools.isEmpty(bean.code));
				tvContent02.setText(Tools.isEmpty(bean.deviceName));
				tvContent03.setText(Tools.isEmpty(bean.model));
				tvContent04.setText(Tools.isEmpty(bean.manufacturer));
				tvContent05.setText(Tools.isEmpty(bean.levaeFactoryCode));
				tvContent06.setText(Tools.isEmpty(bean.levaeFactoryDate));
				tvContent07.setText(Tools.isEmpty(bean.aogDateTime));
				tvContent08.setText(Tools.isEmpty(bean.usingCompanyName));
				tvContent09.setText(Tools.isEmpty(bean.address));
				tvContent10.setText(Tools.isEmpty(bean.checkRecord));
				tvContent11.setText(Tools.isEmpty(bean.surveyor));
				if (bean.photo != null && bean.photo.length() > 0) {
					ll_image.setVisibility(View.VISIBLE);
					tv_image_name.setText("设备照片: ");
					DcHttpClient.getInstance().getImageForNIView(bean.photo, mFile, R.drawable.view_pager_default);
				}
			} else if (childAt == 3) {
				ll_view01.setVisibility(View.VISIBLE);
				ll_view02.setVisibility(View.VISIBLE);
				ll_view03.setVisibility(View.VISIBLE);
				tvTitle01.setText("单位名称: ");
				tvTitle02.setText("设备名称: ");
				tvTitle03.setText("规格型号: ");
				tvTitle04.setText("租赁时间: ");
				tvTitle05.setText("结束时间: ");
				tvTitle06.setText(Html.fromHtml("价&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;格: "));
				tvTitle07.setText("计价方式: ");
				tvTitle08.setText("联系电话: ");
				tvTitle09.setText(Html.fromHtml("联&#160;&#160;系&#160;&#160;人: "));
				tvTitle10.setText("使用地点: ");
				tvTitle11.setText("临时设备编号: ");

				tvContent01.setText(Tools.isEmpty(bean.deviceSupplierName));
				tvContent02.setText(Tools.isEmpty(bean.deviceName));
				tvContent03.setText(Tools.isEmpty(bean.model));
				tvContent04.setText(Tools.isEmpty(bean.dateTime));
				tvContent05.setText(Tools.isEmpty(bean.endDateTime));
				tvContent06.setText(Tools.isEmpty(bean.price +" "+ bean.unit));
				tvContent07.setText(Tools.isEmpty(bean.way));
				tvContent08.setText(Tools.isEmpty(bean.telephone));
				tvContent09.setText(Tools.isEmpty(bean.linkman));
				tvContent10.setText(Tools.isEmpty(bean.place));
				tvContent11.setText(Tools.isEmpty(bean.tempcode));
				if (bean.photo != null && bean.photo.length() > 0) {
					ll_image.setVisibility(View.VISIBLE);
					tv_image_name.setText("设备照片: ");
					DcHttpClient.getInstance().getImageForNIView(bean.photo, mFile, R.drawable.view_pager_default);
				}
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
			RequestList();
			break;
		}
	}
	
	protected void setData(List<DeviceScrapVerify> verifyList) {
		adapter.setData(verifyList);
	}

	private void RequestList() {
		DcNetWorkUtils.getDevicePurchaseVerifyList(this, handler, account, password, id);
	}
	
}
