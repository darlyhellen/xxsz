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
import android.widget.ViewFlipper;

import com.xiangxun.xacity.R;
import com.xiangxun.xacity.app.BaseActivity;
import com.xiangxun.xacity.bean.ResultBeans.Type;
import com.xiangxun.xacity.common.ConstantStatus;
import com.xiangxun.xacity.request.DcNetWorkUtils;
import com.xiangxun.xacity.utils.DateTimePickDialogUtil;
import com.xiangxun.xacity.view.LoadDialog;
import com.xiangxun.xacity.view.MsgToast;
import com.xiangxun.xacity.view.PublishSelectTypeDialog;
import com.xiangxun.xacity.view.TitleView;

/**
 * @package: com.xiangxun.xacity.ui.occupy
 * @ClassName: OccupyManageSearchActivity.java
 * @Description: 挖占管理查询页面
 * @author: HanGJ
 * @date: 2016-1-26 下午2:58:55
 */
public class OccupyManageSearchActivity extends BaseActivity implements OnClickListener {
	private OccupyManageSearchActivity context;
	private TitleView titleView;
	private ViewFlipper mVF;
	private int childAt;
	private LoadDialog loadDialog;
	private TextView tv_applicant_title;
	private TextView tv_type_title;
	private LinearLayout ll_occupy_builder;
	private Button btn_search;
	private boolean isFlag = false;
	/*************** 道路挖掘/道路占用管理 ****************/
	private EditText et_project_name, et_road_name;
	private TextView tv_occupy_applicant;
	private LinearLayout ll_project_name;
	private LinearLayout ll_occupy_applicant_click;
	private TextView tv_occupy_builder;
	private LinearLayout ll_occupy_builder_click;
	private TextView tv_occupy_progress;
	private LinearLayout ll_occupy_progress_click;
	private TextView tv_occupy_area;
	private LinearLayout ll_occupy_area_click;
	private TextView tv_occupy_type;
	private LinearLayout ll_occupy_type_click;
	private EditText start_occupy_time;
	private EditText end_occupy_time;
	/*************** 占道街具管理 ****************/
	private EditText et_road_name01, et_occupy_principal;
	private TextView tv_occupy_applicant01;
	private LinearLayout ll_occupy_applicant_click01;
	private TextView tv_occupy_progress01;
	private LinearLayout ll_occupy_progress_click01;
	private TextView tv_occupy_type01;
	private LinearLayout ll_occupy_type_click01;
	private EditText start_occupy_time01;
	private EditText end_occupy_time01;

	private PublishSelectTypeDialog applicantDialog;
	private PublishSelectTypeDialog builderDialog;
	private PublishSelectTypeDialog progressDialog;
	private PublishSelectTypeDialog areaDialog;
	private PublishSelectTypeDialog typeDialog;

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
					if (childAt <= 1) {
						applicantDialog = new PublishSelectTypeDialog(context, applicantTypes, tv_occupy_applicant, applicant);
					} else {
						applicantDialog = new PublishSelectTypeDialog(context, applicantTypes, tv_occupy_applicant01, applicant);
					}
				}
				break;
			case ConstantStatus.getOccupyBuilderSuccess:
				List<Type> builderTypes = (List<Type>) msg.obj;
				if (builderTypes != null && builderTypes.size() > 0) {
					builderDialog = new PublishSelectTypeDialog(context, builderTypes, tv_occupy_builder, "请选择施工单位");
				}
				break;
			case ConstantStatus.getOccupyProgressSuccess:
				List<Type> progressTypes = (List<Type>) msg.obj;
				if (progressTypes != null && progressTypes.size() > 0) {
					if (childAt <= 1) {
						progressDialog = new PublishSelectTypeDialog(context, progressTypes, tv_occupy_progress, "请选择施工进度");
					} else {
						progressDialog = new PublishSelectTypeDialog(context, progressTypes, tv_occupy_progress01, "请选择施工进度");
					}
				}
				break;
			case ConstantStatus.getOccupyAreaSuccess:
				List<Type> areaTypes = (List<Type>) msg.obj;
				if (areaTypes != null && areaTypes.size() > 0) {
					areaDialog = new PublishSelectTypeDialog(context, areaTypes, tv_occupy_area, "请选择辖区");
				}
				break;
			case ConstantStatus.getOccupyTypeSuccess:
				List<Type> types = (List<Type>) msg.obj;
				if (types != null && types.size() > 0) {
					if (childAt <= 1) {
						typeDialog = new PublishSelectTypeDialog(context, types, tv_occupy_type, type);
					} else {
						typeDialog = new PublishSelectTypeDialog(context, types, tv_occupy_type01, type);
					}
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
		setContentView(R.layout.occupy_manage_search_layout);
		initView();
		initData();
		initListener();
	}

	@Override
	public void initView() {
		titleView = (TitleView) findViewById(R.id.comment_title);
		mVF = (ViewFlipper) findViewById(R.id.viewFlipper);
		tv_applicant_title = (TextView) findViewById(R.id.tv_applicant_title);
		tv_type_title = (TextView) findViewById(R.id.tv_type_title);
		ll_occupy_builder = (LinearLayout) findViewById(R.id.ll_occupy_builder);
		btn_search = (Button) findViewById(R.id.btn_search);
		/*************** 道路挖掘/道路占用管理 ****************/
		et_project_name = (EditText) findViewById(R.id.et_project_name);
		et_road_name = (EditText) findViewById(R.id.et_road_name);
		tv_occupy_applicant = (TextView) findViewById(R.id.tv_occupy_applicant);
		tv_occupy_builder = (TextView) findViewById(R.id.tv_occupy_builder);
		tv_occupy_progress = (TextView) findViewById(R.id.tv_occupy_progress);
		tv_occupy_area = (TextView) findViewById(R.id.tv_occupy_area);
		tv_occupy_type = (TextView) findViewById(R.id.tv_occupy_type);
		ll_project_name = (LinearLayout) findViewById(R.id.ll_project_name);
		ll_occupy_applicant_click = (LinearLayout) findViewById(R.id.ll_occupy_applicant_click);
		ll_occupy_builder_click = (LinearLayout) findViewById(R.id.ll_occupy_builder_click);
		ll_occupy_progress_click = (LinearLayout) findViewById(R.id.ll_occupy_progress_click);
		ll_occupy_area_click = (LinearLayout) findViewById(R.id.ll_occupy_area_click);
		ll_occupy_type_click = (LinearLayout) findViewById(R.id.ll_occupy_type_click);
		start_occupy_time = (EditText) findViewById(R.id.start_occupy_time);
		end_occupy_time = (EditText) findViewById(R.id.end_occupy_time);

		et_road_name01 = (EditText) findViewById(R.id.et_road_name01);
		tv_occupy_applicant01 = (TextView) findViewById(R.id.tv_occupy_applicant01);
		tv_occupy_progress01 = (TextView) findViewById(R.id.tv_occupy_progress01);
		tv_occupy_type01 = (TextView) findViewById(R.id.tv_occupy_type01);
		ll_occupy_applicant_click01 = (LinearLayout) findViewById(R.id.ll_occupy_applicant_click01);
		ll_occupy_progress_click01 = (LinearLayout) findViewById(R.id.ll_occupy_progress_click01);
		ll_occupy_type_click01 = (LinearLayout) findViewById(R.id.ll_occupy_type_click01);
		start_occupy_time01 = (EditText) findViewById(R.id.start_occupy_time01);
		end_occupy_time01 = (EditText) findViewById(R.id.end_occupy_time01);
		et_occupy_principal = (EditText) findViewById(R.id.et_occupy_principal);

		context = this;
	}

	private String type = "";
	private String applicant = "";

	@Override
	public void initData() {
		String title = getIntent().getStringExtra("title");
		childAt = getIntent().getIntExtra("childAt", 0);
		titleView.setTitle(title + "查询");
		if (childAt <= 1) {
			if (childAt == 1) {
				type = "请选择占用类别";
				applicant = "请选择申请单位";
				tv_applicant_title.setText("申请单位:");
				tv_type_title.setText("占用类别:");
				tv_occupy_applicant.setHint("请选择申请单位");
				tv_occupy_type.setHint("请选择占用类别");
				ll_occupy_builder.setVisibility(View.GONE);
				ll_project_name.setVisibility(View.GONE);
			} else {
				type = "请选择施工类别";
				applicant = "请选择建设单位";
			}
			mVF.setDisplayedChild(0);
		} else {
			type = "请选择街具类别";
			applicant = "请选择申请单位";
			mVF.setDisplayedChild(1);
		}
		loadDialog = new LoadDialog(context);
		loadDialog.setTitle("正在加载数据,请稍后...");
		RequestList();
	}

	private void RequestList() {
		loadDialog.show();
		if (childAt <= 1) {
			DcNetWorkUtils.getOccupyApplicantInfoList(context, handler, account, password);
			DcNetWorkUtils.getOccupyProgressInfoList(context, handler, account, password);
			DcNetWorkUtils.getOccupyAreaInfoList(context, handler, account, password);
			if (childAt == 0) {
				DcNetWorkUtils.getOccupyBuilderInfoList(context, handler, account, password);
				DcNetWorkUtils.getOccupyTypeList(context, handler, "1", account, password);
			} else {
				DcNetWorkUtils.getOccupyTypeList(context, handler, "2", account, password);
			}
		} else {
			DcNetWorkUtils.getOccupyApplicantInfoList(context, handler, account, password);
			DcNetWorkUtils.getOccupyProgressInfoList(context, handler, account, password);
			DcNetWorkUtils.getOccupyTypeList(context, handler, "3", account, password);
		}
	}

	@Override
	public void initListener() {
		btn_search.setOnClickListener(context);
		ll_occupy_applicant_click.setOnClickListener(context);
		ll_occupy_applicant_click01.setOnClickListener(context);
		ll_occupy_builder_click.setOnClickListener(context);
		ll_occupy_progress_click.setOnClickListener(context);
		ll_occupy_progress_click01.setOnClickListener(context);
		ll_occupy_area_click.setOnClickListener(context);
		ll_occupy_type_click.setOnClickListener(context);
		ll_occupy_type_click01.setOnClickListener(context);
		start_occupy_time.setOnClickListener(context);
		end_occupy_time.setOnClickListener(context);
		start_occupy_time01.setOnClickListener(context);
		end_occupy_time01.setOnClickListener(context);
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
		case R.id.start_occupy_time:
			new DateTimePickDialogUtil(this).dateTimePicKDialog(start_occupy_time);
			break;
		case R.id.end_occupy_time:
			new DateTimePickDialogUtil(this).dateTimePicKDialog(end_occupy_time);
			break;
		case R.id.start_occupy_time01:
			new DateTimePickDialogUtil(this).dateTimePicKDialog(start_occupy_time01);
			break;
		case R.id.end_occupy_time01:
			new DateTimePickDialogUtil(this).dateTimePicKDialog(end_occupy_time01);
			break;
		case R.id.ll_occupy_applicant_click:
			if (applicantDialog != null) {
				applicantDialog.show();
			} else {
				RequestList();
			}
			break;
		case R.id.ll_occupy_applicant_click01:
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
		case R.id.ll_occupy_progress_click:
			if (progressDialog != null) {
				progressDialog.show();
			} else {
				RequestList();
			}
			break;
		case R.id.ll_occupy_progress_click01:
			if (progressDialog != null) {
				progressDialog.show();
			} else {
				RequestList();
			}
			break;
		case R.id.ll_occupy_area_click:
			if (areaDialog != null) {
				areaDialog.show();
			} else {
				RequestList();
			}
			break;
		case R.id.ll_occupy_type_click:
			if (typeDialog != null) {
				typeDialog.show();
			} else {
				RequestList();
			}
			break;
		case R.id.ll_occupy_type_click01:
			if (typeDialog != null) {
				typeDialog.show();
			} else {
				RequestList();
			}
			break;
		case R.id.btn_search:
			if (childAt <= 1) {
				String projectName = et_project_name.getText().toString().trim();
				String roadname = et_road_name.getText().toString().trim();
				String applicantId = tv_occupy_applicant.getText().toString().trim();
				String builderId = tv_occupy_builder.getText().toString().trim();
				String progress = tv_occupy_progress.getText().toString().trim();
				String managearea = tv_occupy_area.getText().toString().trim();
				String type = tv_occupy_type.getText().toString().trim();
				String starttime = start_occupy_time.getText().toString().trim();
				String endtime = end_occupy_time.getText().toString().trim();
				if (projectName.length() > 0) {
					isFlag = true;
				} else {
					projectName = "";
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
				if (managearea.length() > 0) {
					isFlag = true;
				} else {
					managearea = "";
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
				String status = "";
				if (progress.length() > 0) {
					isFlag = true;
					Type types = (Type) tv_occupy_progress.getTag();
					status = types.code;
				} else {
					status = "";
				}
				if (isFlag) {
					Intent intent = new Intent();
					intent.putExtra("projectName", projectName);
					intent.putExtra("roadname", roadname);
					intent.putExtra("applicantId", applicantId);
					intent.putExtra("builderId", builderId);
					intent.putExtra("status", status);
					intent.putExtra("managearea", managearea);
					intent.putExtra("type", type);
					intent.putExtra("starttime", starttime);
					intent.putExtra("endtime", endtime);
					setResult(childAt, intent);
					finish();
				} else {
					MsgToast.geToast().setMsg("搜索內容不能為空~");
				}
			} else {
				String roadname = et_road_name01.getText().toString().trim();
				String applicantId = tv_occupy_applicant01.getText().toString().trim();
				String progress = tv_occupy_progress01.getText().toString().trim();
				String principal = et_occupy_principal.getText().toString().trim();
				String type = tv_occupy_type01.getText().toString().trim();
				String starttime = start_occupy_time01.getText().toString().trim();
				String endtime = end_occupy_time01.getText().toString().trim();
				if (roadname.length() > 0) {
					isFlag = true;
				} else {
					roadname = "";
				}
				if (applicantId.length() > 0) {
					isFlag = true;
					Type types = (Type) tv_occupy_applicant01.getTag();
					applicantId = types.id;
				} else {
					applicantId = "";
				}
				if (principal.length() > 0) {
					isFlag = true;
				} else {
					principal = "";
				}
				if (type.length() > 0) {
					isFlag = true;
					Type types = (Type) tv_occupy_type01.getTag();
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
				String status = "";
				if (progress.length() > 0) {
					isFlag = true;
					Type types = (Type) tv_occupy_progress01.getTag();
					status = types.code;
				} else {
					status = "";
				}

				if (isFlag) {
					Intent intent = new Intent();
					intent.putExtra("roadname", roadname);
					intent.putExtra("applicantId", applicantId);
					intent.putExtra("status", status);
					intent.putExtra("principal", principal);
					intent.putExtra("type", type);
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

}
