package com.xiangxun.xacity.ui.materiel;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.xiangxun.xacity.R;
import com.xiangxun.xacity.app.BaseActivity;
import com.xiangxun.xacity.bean.ResultBeans.Type;
import com.xiangxun.xacity.common.ConstantStatus;
import com.xiangxun.xacity.request.DcNetWorkUtils;
import com.xiangxun.xacity.utils.DateMonthPickDialogUtil;
import com.xiangxun.xacity.utils.DateTimePickDialogUtil;
import com.xiangxun.xacity.view.LoadDialog;
import com.xiangxun.xacity.view.MsgToast;
import com.xiangxun.xacity.view.PublishSelectTypeDialog;
import com.xiangxun.xacity.view.TitleView;

/**
 * @package: com.xiangxun.xacity.ui.materiel
 * @ClassName: RequirementManageSearchActivity.java
 * @Description: 需求管理查询页面
 * @author: HanGJ
 * @date: 2016-1-26 上午9:32:43
 */
public class RequirementManageSearchActivity extends BaseActivity implements OnClickListener {
	private RequirementManageSearchActivity context;
	private TitleView titleView;
	private ViewFlipper mVF;
	private TextView tvStatus;
	private LinearLayout llStatusClick;
	private Button btn_search;
	/*************** 项目管理 ****************/
	private EditText et_project_name, et_project_code, et_project_address;
	private EditText start_up_date;
	private EditText plan_complete_date;
	/*************** 需求计划 ****************/
	private EditText et_project_name1, et_project_code1, et_plan_month;

	private int childAt;
	private LoadDialog loadDialog;
	private PublishSelectTypeDialog typeDialog;
	private boolean isFlag = false;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			loadDialog.dismiss();
			switch (msg.what) {
			case ConstantStatus.getMaterialListSuccess:
				List<Type> types = (List<Type>) msg.obj;
				if (types != null && types.size() > 0) {
					typeDialog = new PublishSelectTypeDialog(context, types, tvStatus, "请选择项目状态");
				}
				break;
			case ConstantStatus.getMaterialListFailed:

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
		setContentView(R.layout.mareriel_requirement_search_layout);
		initView();
		initData();
		initListener();
	}

	@Override
	public void initView() {
		titleView = (TitleView) findViewById(R.id.comment_title);
		mVF = (ViewFlipper) findViewById(R.id.viewFlipper);
		btn_search = (Button) findViewById(R.id.btn_search);
		tvStatus = (TextView) findViewById(R.id.tv_project_status);
		llStatusClick = (LinearLayout) findViewById(R.id.ll_project_status_click);
		// 项目管理
		et_project_name = (EditText) findViewById(R.id.et_project_name);
		et_project_code = (EditText) findViewById(R.id.et_project_code);
		et_project_address = (EditText) findViewById(R.id.et_project_address);
		start_up_date = (EditText) findViewById(R.id.start_up_date);
		plan_complete_date = (EditText) findViewById(R.id.plan_complete_date);
		// 需求计划
		et_project_name1 = (EditText) findViewById(R.id.et_project_name1);
		et_project_code1 = (EditText) findViewById(R.id.et_project_code1);
		et_plan_month = (EditText) findViewById(R.id.et_plan_month);
		context = this;
	}

	@Override
	public void initData() {
		String title = getIntent().getStringExtra("title");
		childAt = getIntent().getIntExtra("childAt", 0);
		titleView.setTitle(title + "查询");
		mVF.setDisplayedChild(childAt);
		loadDialog = new LoadDialog(context);
		loadDialog.setTitle("正在加载数据,请稍后...");
		RequestList();
	}

	@Override
	public void initListener() {
		llStatusClick.setOnClickListener(context);
		start_up_date.setOnClickListener(context);
		plan_complete_date.setOnClickListener(context);
		et_plan_month.setOnClickListener(context);
		btn_search.setOnClickListener(context);
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
		case R.id.start_up_date:
			new DateTimePickDialogUtil(this).dateTimePicKDialog(start_up_date);
			break;
		case R.id.plan_complete_date:
			new DateTimePickDialogUtil(this).dateTimePicKDialog(plan_complete_date);
			break;
		case R.id.et_plan_month:
			new DateMonthPickDialogUtil(this).dateTimePicKDialog(et_plan_month);
			break;
		case R.id.ll_project_status_click:
			if (typeDialog != null) {
				typeDialog.show();
			} else {
				RequestList();
			}
			break;
		case R.id.btn_search:
			if (childAt == 0) {
				String name = et_project_name.getText().toString().trim();
				String code = et_project_code.getText().toString().trim();
				String place = et_project_address.getText().toString().trim();
				String starttime = start_up_date.getText().toString().trim();
				String planendtime = plan_complete_date.getText().toString().trim();
				String status = tvStatus.getText().toString().trim();
				if (name.length() > 0) {
					isFlag = true;
				} else {
					name = "";
				}
				if (code.length() > 0) {
					isFlag = true;
				} else {
					code = "";
				}
				if (place.length() > 0) {
					isFlag = true;
				} else {
					place = "";
				}
				if (starttime.length() > 0) {
					isFlag = true;
				} else {
					starttime = "";
				}
				if (planendtime.length() > 0) {
					isFlag = true;
				} else {
					planendtime = "";
				}
				if (status.length() > 0) {
					isFlag = true;
					Type type = (Type) tvStatus.getTag();
					status = type != null ? type.code : "";
				} else {
					status = "";
				}
				if (isFlag) {
					Intent intent = new Intent();
					intent.putExtra("name", name);
					intent.putExtra("code", code);
					intent.putExtra("place", place);
					intent.putExtra("starttime", starttime);
					intent.putExtra("planendtime", planendtime);
					intent.putExtra("status", status);
					setResult(childAt, intent);
					finish();
				} else {
					MsgToast.geToast().setMsg("搜索內容不能為空~");
				}
			} else {
				String projectName = et_project_name1.getText().toString().trim();
				String code = et_project_code1.getText().toString().trim();
				String month = et_plan_month.getText().toString().trim();
				if (projectName.length() > 0) {
					isFlag = true;
				} else {
					projectName = "";
				}
				if (code.length() > 0) {
					isFlag = true;
				} else {
					code = "";
				}
				if (month.length() > 0) {
					isFlag = true;
				} else {
					month = "";
				}
				if (isFlag) {
					Intent intent = new Intent();
					intent.putExtra("projectName", projectName);
					intent.putExtra("code", code);
					intent.putExtra("month", month);
					setResult(childAt, intent);
					finish();
				} else {
					MsgToast.geToast().setMsg("搜索內容不能為空~");
				}
			}
			break;
		}
	}

	private void RequestList() {
		if (childAt == 0) {
			loadDialog.show();
			DcNetWorkUtils.getProjectStatusList(this, handler, account, password);
		}
	}

}
