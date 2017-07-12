package com.xiangxun.xacity.ui.occupy;

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

import com.xiangxun.xacity.R;
import com.xiangxun.xacity.app.BaseActivity;
import com.xiangxun.xacity.bean.ResultBeans.Type;
import com.xiangxun.xacity.common.ConstantStatus;
import com.xiangxun.xacity.constant.ConstantType;
import com.xiangxun.xacity.request.DcNetWorkUtils;
import com.xiangxun.xacity.utils.DateTimePickDialogUtil;
import com.xiangxun.xacity.view.LoadDialog;
import com.xiangxun.xacity.view.MsgToast;
import com.xiangxun.xacity.view.PublishSelectArrayDialog;
import com.xiangxun.xacity.view.PublishSelectArrayDialog.SelectItemClick;
import com.xiangxun.xacity.view.PublishSelectTypeDialog;
import com.xiangxun.xacity.view.TitleView;

/**
 * @package: com.xiangxun.xacity.ui.occupy
 * @ClassName: SearchProjectInfoActivity.java
 * @Description: 查询管理查询页面
 * @author: HanGJ
 * @date: 2016-1-26 下午3:37:22
 */
public class SearchProjectInfoActivity extends BaseActivity implements OnClickListener, SelectItemClick {
	private SearchProjectInfoActivity context;
	private TitleView titleView;
	private int childAt;
	private LoadDialog loadDialog;
	private TextView tv_operater_title;
	private TextView tv_date_title;
	private Button btn_search;
	private boolean isFlag = false;
	private String[] arrays = { "请选择", "挖掘", "占用", "街具" };
	private TextView tv_project_type;
	private LinearLayout ll_project_type_click;
	private TextView tv_occupy_applicant;
	private LinearLayout ll_occupy_applicant_click;
	private EditText et_project_address, et_project_name;
	private EditText et_project_man;
	private TextView tv_occupy_type;
	private LinearLayout ll_occupy_type_click;
	private TextView tv_occupy_builder;
	private LinearLayout ll_occupy_builder_click;

	private TextView tv_occupy_before_status;
	private LinearLayout ll_occupy_before_status_click;
	private TextView tv_occupy_after_status;
	private LinearLayout ll_occupy_after_status_click;

	private EditText start_occupy_time;
	private EditText end_occupy_time;
	private LinearLayout ll_project_name;
	private LinearLayout ll_construct_type;
	private LinearLayout ll_occupy_builder;
	private LinearLayout ll_occupy_after_status;
	private LinearLayout ll_occupy_before_status;
	private TextView tv_construct_title;
	private TextView tv_applicant_title;

	private String selectType = "";
	private PublishSelectTypeDialog applicantDialog;
	private PublishSelectTypeDialog builderDialog;
	private PublishSelectArrayDialog arrayDialog;
	private PublishSelectTypeDialog typeDialog;
	private PublishSelectTypeDialog afterDialog;
	private PublishSelectTypeDialog beforeDialog;
	private String type = "";
	private String applicant = "";
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			loadDialog.dismiss();
			switch (msg.what) {
			case ConstantStatus.getOccupyApplicantSuccess:
				List<Type> applicantTypes = (List<Type>) msg.obj;
				if (applicantTypes != null && applicantTypes.size() > 0) {
					applicantDialog = new PublishSelectTypeDialog(context, applicantTypes, tv_occupy_applicant, applicant);
				}
				break;
			case ConstantStatus.getOccupyBuilderSuccess:
				List<Type> builderTypes = (List<Type>) msg.obj;
				if (builderTypes != null && builderTypes.size() > 0) {
					builderDialog = new PublishSelectTypeDialog(context, builderTypes, tv_occupy_builder, "请选择施工单位");
				}
				break;
			case ConstantStatus.getOccupyTypeSuccess:
				List<Type> types = (List<Type>) msg.obj;
				if (types != null && types.size() > 0) {
					typeDialog = new PublishSelectTypeDialog(context, types, tv_occupy_type, type);
				}
				break;
			case ConstantStatus.getOccupyProgressSuccess:
				List<Type> progressTypes = (List<Type>) msg.obj;
				if (progressTypes != null && progressTypes.size() > 0) {
					afterDialog = new PublishSelectTypeDialog(context, progressTypes, tv_occupy_after_status, "请选择更改后状态");
					beforeDialog = new PublishSelectTypeDialog(context, progressTypes, tv_occupy_before_status, "请选择更改前状态");
				}
				break;
			case ConstantStatus.getOccupyListFailed:

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
		setContentView(R.layout.occupy_project_search_layout);
		context = this;
		initView();
		initData();
		initListener();
	}

	@Override
	public void initView() {
		titleView = (TitleView) findViewById(R.id.comment_title);
		tv_operater_title = (TextView) findViewById(R.id.tv_operater);
		tv_date_title = (TextView) findViewById(R.id.tv_date_time);
		btn_search = (Button) findViewById(R.id.btn_search);
		tv_project_type = (TextView) findViewById(R.id.tv_project_type);
		ll_project_type_click = (LinearLayout) findViewById(R.id.ll_project_type_click);
		tv_occupy_applicant = (TextView) findViewById(R.id.tv_occupy_applicant);
		ll_occupy_applicant_click = (LinearLayout) findViewById(R.id.ll_occupy_applicant_click);
		tv_occupy_type = (TextView) findViewById(R.id.tv_construct_type);
		tv_occupy_builder = (TextView) findViewById(R.id.tv_occupy_builder);
		ll_occupy_builder_click = (LinearLayout) findViewById(R.id.ll_occupy_builder_click);
		ll_occupy_type_click = (LinearLayout) findViewById(R.id.ll_construct_type_click);
		et_project_man = (EditText) findViewById(R.id.et_project_man);
		et_project_address = (EditText) findViewById(R.id.et_occupy_address);
		start_occupy_time = (EditText) findViewById(R.id.start_apply_date);
		end_occupy_time = (EditText) findViewById(R.id.end_apply_date);
		et_project_name = (EditText) findViewById(R.id.et_project_name);
		ll_project_name = (LinearLayout) findViewById(R.id.ll_project_name);
		ll_construct_type = (LinearLayout) findViewById(R.id.ll_construct_type);
		ll_occupy_builder = (LinearLayout) findViewById(R.id.ll_occupy_builder);
		ll_occupy_after_status = (LinearLayout) findViewById(R.id.ll_occupy_after_status);
		ll_occupy_before_status = (LinearLayout) findViewById(R.id.ll_occupy_before_status);
		tv_occupy_before_status = (TextView) findViewById(R.id.tv_occupy_before_status);
		ll_occupy_before_status_click = (LinearLayout) findViewById(R.id.ll_occupy_before_status_click);
		tv_occupy_after_status = (TextView) findViewById(R.id.tv_occupy_after_status);
		ll_occupy_after_status_click = (LinearLayout) findViewById(R.id.ll_occupy_after_status_click);
		tv_construct_title = (TextView) findViewById(R.id.tv_construct_title);
		tv_applicant_title = (TextView) findViewById(R.id.tv_applicant_title);
	}

	@Override
	public void initData() {
		String title = getIntent().getStringExtra("title");
		childAt = getIntent().getIntExtra("childAt", 0);
		titleView.setTitle(title);
		if (childAt <= 1) {
			if (childAt == 1) {
				tv_operater_title.setText("勘察人员:");
				tv_date_title.setText("勘察时间:");
				type = "请选择占用类别";
				applicant = "请选择申请单位";
			} else {
				type = "请选择施工类别";
				applicant = "请选择建设单位";
			}
		} else {
			ll_occupy_before_status.setVisibility(View.VISIBLE);
			ll_occupy_after_status.setVisibility(View.VISIBLE);
		}
		loadDialog = new LoadDialog(context);
		loadDialog.setTitle("正在加载数据,请稍后...");
		arrayDialog = new PublishSelectArrayDialog(this, arrays, tv_project_type, "请选择项目类别");
		RequestList();
	}

	private void RequestList() {
		loadDialog.show();
		DcNetWorkUtils.getOccupyApplicantInfoList(context, handler, account, password);
		DcNetWorkUtils.getOccupyBuilderInfoList(context, handler, account, password);
		if (childAt <= 1) {
			if (childAt == 0) {
				DcNetWorkUtils.getOccupyTypeList(context, handler, "1", account, password);
			} else {
				DcNetWorkUtils.getOccupyTypeList(context, handler, "2", account, password);
			}
		} else {
			DcNetWorkUtils.getOccupyTypeList(context, handler, "3", account, password);
			DcNetWorkUtils.getOccupyProgressInfoList(context, handler, account, password);
		}
	}

	@Override
	public void initListener() {
		btn_search.setOnClickListener(context);
		arrayDialog.setSelectItemClick(context);
		start_occupy_time.setOnClickListener(context);
		ll_project_type_click.setOnClickListener(context);
		end_occupy_time.setOnClickListener(context);
		ll_occupy_type_click.setOnClickListener(context);
		ll_occupy_applicant_click.setOnClickListener(context);
		ll_occupy_builder_click.setOnClickListener(context);
		ll_occupy_after_status_click.setOnClickListener(context);
		ll_occupy_before_status_click.setOnClickListener(context);
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
		case R.id.start_apply_date:
			new DateTimePickDialogUtil(this).dateTimePicKDialog(start_occupy_time);
			break;
		case R.id.end_apply_date:
			new DateTimePickDialogUtil(this).dateTimePicKDialog(end_occupy_time);
			break;
		case R.id.ll_project_type_click:
			arrayDialog.show();
			break;
		case R.id.ll_occupy_applicant_click:
			if (applicantDialog != null) {
				applicantDialog.show();
			} else {
				RequestList();
			}
			break;
		case R.id.ll_occupy_builder_click:
			if (builderDialog != null) {
				builderDialog.show();
			} else {
				RequestList();
			}
			break;
		case R.id.ll_construct_type_click:
			if (typeDialog != null) {
				typeDialog.show();
			} else {
				RequestList();
			}
			break;
		case R.id.ll_occupy_after_status_click:
			if (afterDialog != null) {
				afterDialog.show();
			} else {
				RequestList();
			}
			break;
		case R.id.ll_occupy_before_status_click:
			if (beforeDialog != null) {
				beforeDialog.show();
			} else {
				RequestList();
			}
			break;
		case R.id.btn_search:
			if (childAt <= 1) {
				String pname = et_project_name.getText().toString().trim();
				String type = tv_occupy_type.getText().toString().trim();
				String roadId = et_project_address.getText().toString().trim();
				String applicantId = tv_occupy_applicant.getText().toString().trim();
				String builderId = tv_occupy_builder.getText().toString().trim();
				String operator = et_project_man.getText().toString().trim();
				String starttime = start_occupy_time.getText().toString().trim();
				String endtime = end_occupy_time.getText().toString().trim();
				if (pname.length() > 0) {
					isFlag = true;
				} else {
					pname = "";
				}
				if (roadId.length() > 0) {
					isFlag = true;
				} else {
					roadId = "";
				}
				if (applicantId.length() > 0) {
					isFlag = true;
					Type types = (Type) tv_occupy_applicant.getTag();
					applicantId = types.id;
				} else {
					applicantId = "";
				}
				if (builderId.length() > 0) {
					isFlag = true;
					Type types = (Type) tv_occupy_builder.getTag();
					builderId = types.id;
				} else {
					builderId = "";
				}
				if (operator.length() > 0) {
					isFlag = true;
				} else {
					operator = "";
				}
				if (type.length() > 0) {
					isFlag = true;
					Type types = (Type) tv_occupy_type.getTag();
					type = types.code;
				} else {
					type = "";
				}
				if (starttime.length() > 0) {
					isFlag = true;
				} else {
					starttime = "";
				}
				if (endtime.length() > 0) {
					isFlag = true;
				} else {
					endtime = "";
				}
				if (isFlag) {
					Intent intent = new Intent();
					intent.putExtra("occupyType", selectType);
					intent.putExtra("pname", pname);
					intent.putExtra("roadId", roadId);
					intent.putExtra("applicantId", applicantId);
					intent.putExtra("builderId", builderId);
					intent.putExtra("operator", operator);
					intent.putExtra("type", type);
					intent.putExtra("starttime", starttime);
					intent.putExtra("endtime", endtime);
					setResult(childAt, intent);
					finish();
				} else {
					MsgToast.geToast().setMsg("搜索內容不能為空~");
				}
			} else {
				String pname = et_project_name.getText().toString().trim();
				String type = tv_occupy_type.getText().toString().trim();
				String roadname = et_project_address.getText().toString().trim();
				String applicantId = tv_occupy_applicant.getText().toString().trim();
				String builderId = tv_occupy_builder.getText().toString().trim();
				String operator = et_project_man.getText().toString().trim();
				String starttime = start_occupy_time.getText().toString().trim();
				String endtime = end_occupy_time.getText().toString().trim();
				String beforeStatus = tv_occupy_before_status.getText().toString().trim();
				String afterStatus = tv_occupy_after_status.getText().toString().trim();
				if (pname.length() > 0) {
					isFlag = true;
				} else {
					pname = "";
				}
				if (roadname.length() > 0) {
					isFlag = true;
				} else {
					roadname = "";
				}
				if (applicantId.length() > 0) {
					isFlag = true;
					Type types = (Type) tv_occupy_applicant.getTag();
					applicantId = types.id;
				} else {
					applicantId = "";
				}
				if (builderId.length() > 0) {
					isFlag = true;
					Type types = (Type) tv_occupy_builder.getTag();
					builderId = types.id;
				} else {
					builderId = "";
				}
				if (operator.length() > 0) {
					isFlag = true;
				} else {
					operator = "";
				}
				if (type.length() > 0) {
					isFlag = true;
					Type types = (Type) tv_occupy_type.getTag();
					type = types.code;
				} else {
					type = "";
				}
				if (beforeStatus.length() > 0) {
					isFlag = true;
					Type types = (Type) tv_occupy_before_status.getTag();
					beforeStatus = types.code;
				} else {
					beforeStatus = "";
				}
				if (afterStatus.length() > 0) {
					isFlag = true;
					Type types = (Type) tv_occupy_after_status.getTag();
					afterStatus = types.code;
				} else {
					type = "";
				}
				if (starttime.length() > 0) {
					isFlag = true;
				} else {
					starttime = "";
				}
				if (endtime.length() > 0) {
					isFlag = true;
				} else {
					endtime = "";
				}
				if (isFlag) {
					Intent intent = new Intent();
					intent.putExtra("occupyType", selectType);
					intent.putExtra("pname", pname);
					intent.putExtra("roadname", roadname);
					intent.putExtra("applicantId", applicantId);
					intent.putExtra("builderId", builderId);
					intent.putExtra("operator", operator);
					intent.putExtra("type", type);
					intent.putExtra("beforeStatus", beforeStatus);
					intent.putExtra("afterStatus", afterStatus);
					intent.putExtra("starttime", starttime);
					intent.putExtra("endtime", endtime);
					setResult(childAt, intent);
					finish();
				} else {
					MsgToast.geToast().setMsg("搜索內容不能為空~");
				}
			}
			break;
		}
	}

	@Override
	public void itemOnClick(String item) {
		if (!"请选择".equals(item)) {
			selectType = ConstantType.getProjectTyoe(item) + "";
		} else {
			selectType = "";
		}
		setViewVisible();
		if ("挖掘".equals(item)) {
			ll_project_name.setVisibility(View.VISIBLE);
			ll_occupy_builder.setVisibility(View.VISIBLE);
			ll_construct_type.setVisibility(View.VISIBLE);
			tv_construct_title.setText("施工类别:");
			tv_applicant_title.setText("建设单位:");
		} else if ("占用".equals(item)) {
			ll_construct_type.setVisibility(View.VISIBLE);
			tv_occupy_builder.setText("");
			tv_occupy_type.setText("");
			tv_construct_title.setText("占用类别:");
			tv_applicant_title.setText("申请单位:");
		} else if ("街具".equals(item)) {
			ll_construct_type.setVisibility(View.VISIBLE);
			tv_occupy_builder.setText("");
			tv_occupy_type.setText("");
			tv_construct_title.setText("街具类别:");
			tv_applicant_title.setText("申请单位:");
		} else {
			tv_occupy_builder.setText("");
			tv_occupy_type.setText("");
			tv_applicant_title.setText("申请单位:");
		}
	}

	private void setViewVisible() {
		ll_project_name.setVisibility(View.GONE);
		ll_construct_type.setVisibility(View.GONE);
		ll_occupy_builder.setVisibility(View.GONE);
	}

}
