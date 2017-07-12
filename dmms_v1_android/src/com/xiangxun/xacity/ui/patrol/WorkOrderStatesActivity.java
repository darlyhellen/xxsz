package com.xiangxun.xacity.ui.patrol;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.xiangxun.xacity.R;
import com.xiangxun.xacity.app.BaseActivity;
import com.xiangxun.xacity.bean.ResponseResultBeans.WorkOrderBean;
import com.xiangxun.xacity.bean.ResultBeans.PublishOrderData;
import com.xiangxun.xacity.bean.ResultBeans.UpLoadResule;
import com.xiangxun.xacity.common.ConstantStatus;
import com.xiangxun.xacity.request.DcNetWorkUtils;
import com.xiangxun.xacity.view.LoadDialog;
import com.xiangxun.xacity.view.MsgToast;
import com.xiangxun.xacity.view.TitleView;

/**
 * @package: com.xiangxun.xacity.ui.patrol
 * @ClassName: WorkOrderStatesActivity.java
 * @Description: 工单状态修改页面
 * @author: HanGJ
 * @date: 2016-4-18 上午11:35:29
 */
public class WorkOrderStatesActivity extends BaseActivity implements OnClickListener, OnCheckedChangeListener {
	private WorkOrderStatesActivity mContext = null;
	private TitleView titleView;
	private TextView tvSubmit;
	private TextView tv_title;
	private EditText tv_publish_remark = null;
	private RadioGroup rgQualify;
//	private RadioButton qualify;
//	private RadioButton unQualify;
	private LinearLayout ll_verify;
	// 已经上传result
	private List<UpLoadResule> mUpLoadImageUrls = new ArrayList<UpLoadResule>();
	private LoadDialog loadDialog;
	private int isFlag = 0;
	private WorkOrderBean order;
	private int check = -1;
	private String hintContent = "";

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			loadDialog.dismiss();
			switch (msg.what) {
			case ConstantStatus.NetWorkError:
				MsgToast.geToast().setMsg("网络异常,请稍后重试!");
				break;
			case ConstantStatus.PublishOrderSuccess:
				MsgToast.geToast().setMsg(hintContent + "成功");
				finish();
				break;
			case ConstantStatus.PublishOrderFailed:
				MsgToast.geToast().setMsg(hintContent + "失败,请重试~");
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.patrol_order_states_layout);
		mContext = this;
		initView();
		initData();
		initListener();
	}

	@Override
	public void initView() {
		titleView = (TitleView) findViewById(R.id.comment_title);
		tvSubmit = titleView.getTitleViewOperationText();
		tv_publish_remark = (EditText) findViewById(R.id.tv_publish_remark);
		tv_title = (TextView) findViewById(R.id.tv_title);
		rgQualify = (RadioGroup) findViewById(R.id.rg_qualify);
//		qualify = (RadioButton) findViewById(R.id.cb_qualify);
//		unQualify = (RadioButton) findViewById(R.id.cb_unqualify);
		ll_verify = (LinearLayout) findViewById(R.id.ll_verify);
	}

	@Override
	public void initData() {
		titleView.setTitle(getIntent().getStringExtra("title"));
		tvSubmit.setText("提交");
		isFlag = getIntent().getIntExtra("isFlag", 0);
		order = (WorkOrderBean) getIntent().getSerializableExtra("bean");
		if (isFlag == 3) {
			ll_verify.setVisibility(View.VISIBLE);
		} else if (isFlag == 4) {
			tv_title.setText("退单说明");
		}
	}

	@Override
	public void initListener() {
		titleView.setRightImageTextFlipper(mContext);
		rgQualify.setOnCheckedChangeListener(mContext);
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
		case R.id.title_view_right_Flipper01:
			if (tv_publish_remark.getText().toString().trim().equals("")) {
				MsgToast.geToast().setMsg("请输入工单备注内容");
				return;
			}
			if (order != null) {
				loadDialog = new LoadDialog(mContext);
				loadDialog.show();
				PublishOrderData publishInfo = new PublishOrderData();
				publishInfo.workOrderId = order.workOrderId;
				publishInfo.files = mUpLoadImageUrls;
				if (isFlag == 1) {// 设置难点
					hintContent = "工单设置难点";
					publishInfo.workIsNodus = "1";
					publishInfo.workOrderRemark = tv_publish_remark.getText().toString().trim();
				} else if (isFlag == 2) {// 重新办理
					hintContent = "工单重新办理";
					publishInfo.workIsIssued = "0";
					publishInfo.workEventState = order.dutyOrgType == "市政维管" || order.dutyOrgType == "SZNW001" ? "GDZT02" : "GDZT03";
					publishInfo.workOrderRemark = tv_publish_remark.getText().toString().trim();
				} else if (isFlag == 3) {// 工单审核
					if (check != -1) {
						hintContent = "工单审核";
						publishInfo.checkStatus = check + "";
						if(check == 1){
							publishInfo.workEventState = "GDZT07";	
						} else {
							publishInfo.workEventState = "GDZT03";	
						}
						publishInfo.workIsIssued = "0";
						publishInfo.checkDescription = tv_publish_remark.getText().toString().trim();
					} else {
						MsgToast.geToast().setMsg("请选择审核状态");
						return;
					}
				} else if (isFlag == 4) {// 工单退回
					hintContent = "工单退回";
					publishInfo.workIsIssued = "0";
					publishInfo.workEventState = "GDZT10";
					publishInfo.workBackExplain = tv_publish_remark.getText().toString().trim();
				}
				DcNetWorkUtils.publishOrderUrl(this, handler, publishInfo, account, password);
			} else {
				MsgToast.geToast().setMsg("工单数据异常,请重新处理");
			}
			break;
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.cb_qualify:
			check = 1;
			break;
		case R.id.cb_unqualify:
			check = 0;
			break;
		}
	}

}
