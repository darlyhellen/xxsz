package com.xiangxun.xacity.ui.patrol;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import com.xiangxun.xacity.bean.ResultBeans.GetPatrolBulderList;
import com.xiangxun.xacity.bean.ResultBeans.GetPatrolTypeList;
import com.xiangxun.xacity.bean.ResultBeans.Type;
import com.xiangxun.xacity.common.ConstantStatus;
import com.xiangxun.xacity.request.DcNetWorkUtils;
import com.xiangxun.xacity.utils.DateTimePickDialogUtil;
import com.xiangxun.xacity.view.LoadDialog;
import com.xiangxun.xacity.view.MsgToast;
import com.xiangxun.xacity.view.PublishSelectArrayDialog;
import com.xiangxun.xacity.view.PublishSelectTypeDialog;
import com.xiangxun.xacity.view.PublishSelectTypeDialog.onSelectItemClick;
import com.xiangxun.xacity.view.TitleView;

/**
 * @package: com.xiangxun.xacity.ui.patrol
 * @ClassName: WorkOrderSearchActivity.java
 * @Description: 工单查询搜索页面
 * @author: HanGJ
 * @date: 2016-3-10 上午11:07:29
 */
public class WorkOrderSearchActivity extends BaseActivity implements OnClickListener, onSelectItemClick {
	private WorkOrderSearchActivity context;
	private TitleView titleView;
	private Button btn_search;
	private boolean isFlag = false;
	private LoadDialog loadDialog;
	private EditText et_order_code, et_complaint_name, et_employee_name;
	private TextView tv_order_type;
	private LinearLayout ll_order_type_click;
	private TextView tv_order_state;
	private LinearLayout ll_order_state_click;
	private TextView tv_order_nodus;
	private LinearLayout ll_order_nodus_click;
	private TextView tv_order_back;
	private LinearLayout ll_order_back_click;
	private TextView tv_order_org;
	private LinearLayout ll_order_org_click;
	private TextView tv_order_duty;
	private LinearLayout ll_order_duty_click;
	private TextView tv_order_assess;
	private LinearLayout ll_order_assess_click;
	private EditText et_order_text;
	private EditText et_order_phone;
	private EditText start_complaint_date;
	private EditText end_complaint_date;
	private EditText start_state_date;
	private EditText end_state_date;

	private String dicTypes;
	private PublishSelectTypeDialog orderType;
	private PublishSelectTypeDialog orderStatus;
	private PublishSelectTypeDialog orderBack;
	private PublishSelectTypeDialog orderOrgType;
	private PublishSelectTypeDialog orderDutyType;
	private PublishSelectTypeDialog orderKpState;
	private PublishSelectArrayDialog arrayDialog;
	private String[] orderNodus = { "否", "是" };

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			loadDialog.dismiss();
			switch (msg.what) {
			case ConstantStatus.OccupyTypeSuccess:
				GetPatrolTypeList patrolTypeList = (GetPatrolTypeList) msg.obj;
				if (patrolTypeList != null) {
					List<Type> tourOrderType = patrolTypeList.tourOrderType;
					if (tourOrderType != null && tourOrderType.size() > 0) {
						orderType = new PublishSelectTypeDialog(context, tourOrderType, tv_order_type, "请选择工单类型");
					}
					List<Type> tourOrderState = patrolTypeList.tourOrderState;
					if (tourOrderState != null && tourOrderState.size() > 0) {
						orderStatus = new PublishSelectTypeDialog(context, tourOrderState, tv_order_state, "请选择工单状态");
					}
					List<Type> tourIsBack = patrolTypeList.tourIsBack;
					if (tourIsBack != null && tourIsBack.size() > 0) {
						orderBack = new PublishSelectTypeDialog(context, tourIsBack, tv_order_back, "请选择是否回访");
					}
					List<Type> tourDutyType = patrolTypeList.tourDutyType;
					if (tourDutyType != null && tourDutyType.size() > 0) {
						orderOrgType = new PublishSelectTypeDialog(context, tourDutyType, tv_order_org, "请选择市政内/外");
						orderOrgType.setSelectItemClick(context);
					}
					List<Type> tourKpState = patrolTypeList.tourKpState;
					if (tourKpState != null && tourKpState.size() > 0) {
						orderKpState = new PublishSelectTypeDialog(context, tourKpState, tv_order_assess, "请选择考评状态");
					}
				}
				break;
			case ConstantStatus.OccupyStatusSuccess:
				GetPatrolBulderList typeBulders = (GetPatrolBulderList) msg.obj;
				if (typeBulders != null) {
					List<Type> types = typeBulders.type;
					if (types != null && types.size() > 0) {
						orderDutyType = new PublishSelectTypeDialog(context, types, tv_order_duty, "请选择责任单位");
					}
				}
				break;
			case ConstantStatus.OccupyTypeFailed:
				MsgToast.geToast().setMsg("数据加载失败~");
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
		setContentView(R.layout.work_order_search_layout);
		context = this;
		initView();
		initData();
		initListener();
	}

	@Override
	public void initView() {
		titleView = (TitleView) findViewById(R.id.comment_title);
		btn_search = (Button) findViewById(R.id.btn_search);
		start_complaint_date = (EditText) findViewById(R.id.start_complaint_date);
		end_complaint_date = (EditText) findViewById(R.id.end_complaint_date);
		start_state_date = (EditText) findViewById(R.id.start_state_date);
		end_state_date = (EditText) findViewById(R.id.end_state_date);
		et_order_code = (EditText) findViewById(R.id.et_order_code);
		et_complaint_name = (EditText) findViewById(R.id.et_complaint_name);
		et_employee_name = (EditText) findViewById(R.id.et_employee_name);
		tv_order_type = (TextView) findViewById(R.id.tv_order_type);
		ll_order_type_click = (LinearLayout) findViewById(R.id.ll_order_type_click);
		tv_order_state = (TextView) findViewById(R.id.tv_order_state);
		ll_order_state_click = (LinearLayout) findViewById(R.id.ll_order_state_click);
		tv_order_nodus = (TextView) findViewById(R.id.tv_order_nodus);
		ll_order_nodus_click = (LinearLayout) findViewById(R.id.ll_order_nodus_click);
		tv_order_back = (TextView) findViewById(R.id.tv_order_back);
		ll_order_back_click = (LinearLayout) findViewById(R.id.ll_order_back_click);
		tv_order_org = (TextView) findViewById(R.id.tv_order_org);
		ll_order_org_click = (LinearLayout) findViewById(R.id.ll_order_org_click);
		tv_order_duty = (TextView) findViewById(R.id.tv_order_duty);
		ll_order_duty_click = (LinearLayout) findViewById(R.id.ll_order_duty_click);
		tv_order_assess = (TextView) findViewById(R.id.tv_order_assess);
		ll_order_assess_click = (LinearLayout) findViewById(R.id.ll_order_assess_click);
		et_order_text = (EditText) findViewById(R.id.et_order_text);
		et_order_phone = (EditText) findViewById(R.id.et_order_phone);
	}

	@Override
	public void initData() {
		titleView.setTitle("工单查询");
		loadDialog = new LoadDialog(context);
		loadDialog.setTitle("正在加载数据,请稍后...");
		arrayDialog = new PublishSelectArrayDialog(context, orderNodus, tv_order_nodus, "请选择是否难点");
		dicTypes = "tourOrderType,tourOrderState,tourIsBack,tourKpState,tourDutyType";
		loadDialog.show();
		RequestList(dicTypes, 0);
	}

	private void RequestList(String dicTypes, int type) {
		DcNetWorkUtils.getPatrolBaseData(this, handler, type, account, password, dicTypes);
	}

	@Override
	public void initListener() {
		btn_search.setOnClickListener(this);
		start_complaint_date.setOnClickListener(this);
		end_complaint_date.setOnClickListener(this);
		start_state_date.setOnClickListener(this);
		end_state_date.setOnClickListener(this);
		ll_order_type_click.setOnClickListener(this);
		ll_order_state_click.setOnClickListener(this);
		ll_order_nodus_click.setOnClickListener(this);
		ll_order_back_click.setOnClickListener(this);
		ll_order_org_click.setOnClickListener(this);
		ll_order_duty_click.setOnClickListener(this);
		ll_order_assess_click.setOnClickListener(this);
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
		case R.id.start_complaint_date:
			new DateTimePickDialogUtil(this).dateTimePicKDialog(start_complaint_date);
			break;
		case R.id.end_complaint_date:
			new DateTimePickDialogUtil(this).dateTimePicKDialog(end_complaint_date);
			break;
		case R.id.start_state_date:
			new DateTimePickDialogUtil(this).dateTimePicKDialog(start_state_date);
			break;
		case R.id.end_state_date:
			new DateTimePickDialogUtil(this).dateTimePicKDialog(end_state_date);
			break;
		case R.id.ll_order_type_click:
			if (orderType != null) {
				orderType.show();
			} else {
				RequestList(dicTypes, 0);
			}
			break;
		case R.id.ll_order_state_click:
			if (orderStatus != null) {
				orderStatus.show();
			} else {
				RequestList(dicTypes, 0);
			}
			break;
		case R.id.ll_order_nodus_click:
			arrayDialog.show();
			break;
		case R.id.ll_order_back_click:
			if (orderBack != null) {
				orderBack.show();
			} else {
				RequestList(dicTypes, 0);
			}
			break;
		case R.id.ll_order_org_click:
			if (orderOrgType != null) {
				orderOrgType.show();
			} else {
				RequestList(dicTypes, 0);
			}
			break;
		case R.id.ll_order_duty_click:
			String info = tv_order_org.getText().toString();
			if (info.length() > 0) {
				if (orderDutyType != null) {
					orderDutyType.show();
				}
			} else {
				MsgToast.geToast().setMsg("请选择市政内/外类型");
			}
			break;
		case R.id.ll_order_assess_click:
			if (orderKpState != null) {
				orderKpState.show();
			} else {
				RequestList(dicTypes, 0);
			}
			break;
		case R.id.btn_search:
			String workOrderCode = et_order_code.getText().toString().trim();
			String complaintName = et_complaint_name.getText().toString().trim();
			String employeeName = et_employee_name.getText().toString().trim();
			String workEventType = tv_order_type.getText().toString().trim();
			String workEventStatus = tv_order_state.getText().toString().trim();
			String workIsNodus = tv_order_nodus.getText().toString().trim();
			String workIsBack = tv_order_back.getText().toString().trim();
			String dutyOrgType = tv_order_org.getText().toString().trim();
			String dutyOrgId = tv_order_duty.getText().toString().trim();
			String workAssess = tv_order_assess.getText().toString().trim();
			String workOrderText = et_order_text.getText().toString().trim();
			String complaintTel = et_order_phone.getText().toString().trim();
			String workOrderTimeBegin = start_complaint_date.getText().toString().trim();
			String workOrderTimeEnd = end_complaint_date.getText().toString().trim();
			String closeBegin = start_state_date.getText().toString().trim();
			String closeEnd = end_state_date.getText().toString().trim();
			if (closeEnd.length() > 0) {
				isFlag = true;
			} else {
				closeEnd = "";
			}
			if (closeBegin.length() > 0) {
				isFlag = true;
			} else {
				closeBegin = "";
			}
			if (workOrderTimeEnd.length() > 0) {
				isFlag = true;
			} else {
				workOrderTimeEnd = "";
			}
			if(workIsNodus.length() > 0){
				if("是".equals(workIsNodus)){
					workIsNodus = "1";
				} else {
					workIsNodus = "0";
				}
				isFlag = true;
			} else {
				workIsNodus = "";
			}
			if (workOrderTimeBegin.length() > 0) {
				isFlag = true;
			} else {
				workOrderTimeBegin = "";
			}
			if (complaintTel.length() > 0) {
				isFlag = true;
			} else {
				complaintTel = "";
			}
			if (workOrderText.length() > 0) {
				isFlag = true;
			} else {
				workOrderText = "";
			}
			if (workOrderCode.length() > 0) {
				isFlag = true;
			} else {
				workOrderCode = "";
			}
			if (complaintName.length() > 0) {
				isFlag = true;
			} else {
				complaintName = "";
			}
			if (employeeName.length() > 0) {
				isFlag = true;
			} else {
				employeeName = "";
			}
			if (workEventType.length() > 0) {
				isFlag = true;
				Type type = (Type) tv_order_type.getTag();
				workEventType = type.code;
			} else {
				workEventType = "";
			}
			if (workEventStatus.length() > 0) {
				isFlag = true;
				Type type = (Type) tv_order_state.getTag();
				workEventStatus = type.code;
			} else {
				workEventStatus = "";
			}
			if (workIsBack.length() > 0) {
				isFlag = true;
				Type type = (Type) tv_order_back.getTag();
				workIsBack = type.code;
			} else {
				workIsBack = "";
			}
			if (dutyOrgType.length() > 0) {
				isFlag = true;
				Type type = (Type) tv_order_org.getTag();
				dutyOrgType = type.code;
			} else {
				dutyOrgType = "";
			}
			if (dutyOrgId.length() > 0) {
				isFlag = true;
				Type type = (Type) tv_order_duty.getTag();
				dutyOrgId = type.code;
			} else {
				dutyOrgId = "";
			}
			if (workAssess.length() > 0) {
				isFlag = true;
				Type type = (Type) tv_order_assess.getTag();
				workAssess = type.code;
			} else {
				workAssess = "";
			}
			if (isFlag) {
				Intent intent = new Intent();
				intent.putExtra("workOrderCode", workOrderCode);
				intent.putExtra("complaintName", complaintName);
				intent.putExtra("employeeName", employeeName);
				intent.putExtra("workEventType", workEventType);
				intent.putExtra("workEventStatus", workEventStatus);
				intent.putExtra("workIsBack", workIsBack);
				intent.putExtra("dutyOrgType", dutyOrgType);
				intent.putExtra("dutyOrgId", dutyOrgId);
				intent.putExtra("workIsNodus", workIsNodus);
				intent.putExtra("workAssess", workAssess);
				intent.putExtra("workOrderText", workOrderText);
				intent.putExtra("complaintTel", complaintTel);
				intent.putExtra("workOrderTimeBegin", workOrderTimeBegin);
				intent.putExtra("workOrderTimeEnd", workOrderTimeEnd);
				intent.putExtra("closeBegin", closeBegin);
				intent.putExtra("closeEnd", closeEnd);
				setResult(Activity.RESULT_OK, intent);
				finish();
			} else {
				MsgToast.geToast().setMsg("搜索內容不能为空~");
			}
			break;
		}
	}

	@Override
	public void changeState(Type type) {
		String dicTypes = type.code;
		RequestList(dicTypes, 1);
	}

}
