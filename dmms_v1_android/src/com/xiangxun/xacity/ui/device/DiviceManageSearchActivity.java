package com.xiangxun.xacity.ui.device;

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
import com.xiangxun.xacity.constant.ConstantType;
import com.xiangxun.xacity.request.DcNetWorkUtils;
import com.xiangxun.xacity.utils.DateMonthPickDialogUtil;
import com.xiangxun.xacity.utils.DateTimePickDialogUtil;
import com.xiangxun.xacity.view.LoadDialog;
import com.xiangxun.xacity.view.MsgToast;
import com.xiangxun.xacity.view.PublishSelectArrayDialog;
import com.xiangxun.xacity.view.PublishSelectTypeDialog;
import com.xiangxun.xacity.view.TitleView;

/**
 * @package: com.xiangxun.xacity.ui.device
 * @ClassName: DiviceManageSearchActivity.java
 * @Description: 设备管理查询页面
 * @author: HanGJ
 * @date: 2016-1-20 上午8:50:47
 */
public class DiviceManageSearchActivity extends BaseActivity implements OnClickListener {
	private DiviceManageSearchActivity context;
	private TitleView titleView;
	private ViewFlipper mVF;
	private int childAt;
	private Button btn_search;
	private boolean isFlag = false;
	/*************** 操作人员台账 ****************/
	private EditText et_operator_name;
	private LinearLayout ll_publish_click_type, ll_device_click_degree;
	private TextView tv_publish_type, tv_device_degree;
	private EditText startBirthDate;
	private EditText endBirthDate;
	/*************** 设备台账 ****************/
	private TextView tv_device_class, tv_device_belong;
	private LinearLayout ll_device_class_click, ll_device_belong_click;
	private EditText et_device_nameing, et_device_using, et_device_model, et_device_brand;
	private EditText et_device_plate03, et_car_owner, et_device_product, et_property_org, et_device_operation;
	private TextView tv_device_oil, tv_device_envi;
	private LinearLayout ll_device_oil_click, ll_device_envi_click;
	private TextView tv_device_source, tv_device_finance;
	private LinearLayout ll_device_source_click, ll_device_finance_click;
	private EditText startUsingDate;
	private EditText endUsingDate;
	/*************** 安全保护装置 ****************/
	private EditText et_device_code, et_device_name, et_device_salf;
	/*************** 设备派遣 ****************/
	private EditText et_device_name01, et_device_user_org, et_device_plate;
	private EditText startUseDate;
	private EditText endUseDate;
	/*************** 使用记录 ****************/
	private EditText et_device_name02, et_device_user_org01, et_device_type;
	private EditText startEnableDate;
	private EditText endEnableDate;
	/*************** 设备到场验证 ****************/
	private EditText et_device_name03, et_device_code01, et_device_type01;
	private EditText startVerifyDate;
	private EditText endVerifyDate;
	/*************** 日常检查记录 ****************/
	private EditText et_device_name04, et_check_code, et_device_plate01;
	private EditText startPutDate;
	private EditText endPutDate;
	/*************** 完好利用率 ****************/
	private EditText et_device_name05, et_device_user_org02, et_device_type02;
	private EditText et_device_month;
	/*************** 重点设备月报表 ****************/
	private EditText et_device_name06, et_device_user_org03, et_device_plate02;
	private EditText startApplyDate;
	private EditText endApplyDate;
	/*************** 保险管理 ****************/
	private EditText et_device_name07, et_insurance_name, et_insurance_type;
	private EditText startInsuranceDate;
	private EditText endInsuranceDate;

	private PublishSelectArrayDialog sexDialog;
	private PublishSelectArrayDialog oilTypeDialog;
	private PublishSelectArrayDialog enviTyoeDialog;
	private PublishSelectArrayDialog sourceMoneyDialog;
	private PublishSelectArrayDialog financeTyoeDialog;
	private PublishSelectArrayDialog degreeDialog;

	private LoadDialog loadDialog;
	private PublishSelectTypeDialog typeDialog;
	private PublishSelectTypeDialog selectTypeDialog;
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {

		@Override
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			loadDialog.dismiss();
			switch (msg.what) {
			case ConstantStatus.getMaterialListSuccess:
				List<Type> types = (List<Type>) msg.obj;
				if (types != null && types.size() > 0) {
					typeDialog = new PublishSelectTypeDialog(context, types, tv_device_class, "请选择设备分类");
				}
				break;
			case ConstantStatus.getSupplierListSuccess:
				List<Type> supplierTypes = (List<Type>) msg.obj;
				if (supplierTypes != null && supplierTypes.size() > 0) {
					selectTypeDialog = new PublishSelectTypeDialog(context, supplierTypes, tv_device_belong, "请选择设备归属");
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
		setContentView(R.layout.device_manage_search_layout);
		initView();
		initData();
		initListener();
	}

	@Override
	public void initView() {
		titleView = (TitleView) findViewById(R.id.comment_title);
		mVF = (ViewFlipper) findViewById(R.id.viewFlipper);
		btn_search = (Button) findViewById(R.id.btn_search);
		/*************** 操作人员台账 ****************/
		et_operator_name = (EditText) findViewById(R.id.et_operator_name);
		ll_publish_click_type = (LinearLayout) findViewById(R.id.ll_publish_click_type);
		ll_device_click_degree = (LinearLayout) findViewById(R.id.ll_device_click_degree);
		tv_publish_type = (TextView) findViewById(R.id.tv_publish_type);
		tv_device_degree = (TextView) findViewById(R.id.tv_device_degree);
		startBirthDate = (EditText) findViewById(R.id.start_birth_date);
		endBirthDate = (EditText) findViewById(R.id.end_birth_date);
		/*************** 设备台账 ****************/
		tv_device_class = (TextView) findViewById(R.id.tv_device_class);
		tv_device_belong = (TextView) findViewById(R.id.tv_device_belong);
		tv_device_oil = (TextView) findViewById(R.id.tv_device_oil);
		tv_device_envi = (TextView) findViewById(R.id.tv_device_envi);
		tv_device_source = (TextView) findViewById(R.id.tv_device_source);
		tv_device_finance = (TextView) findViewById(R.id.tv_device_finance);
		ll_device_class_click = (LinearLayout) findViewById(R.id.ll_device_class_click);
		ll_device_belong_click = (LinearLayout) findViewById(R.id.ll_device_belong_click);
		ll_device_oil_click = (LinearLayout) findViewById(R.id.ll_device_oil_click);
		ll_device_envi_click = (LinearLayout) findViewById(R.id.ll_device_envi_click);
		ll_device_source_click = (LinearLayout) findViewById(R.id.ll_device_source_click);
		ll_device_finance_click = (LinearLayout) findViewById(R.id.ll_device_finance_click);
		et_device_nameing = (EditText) findViewById(R.id.et_device_nameing);
		et_device_using = (EditText) findViewById(R.id.et_device_using);
		et_device_model = (EditText) findViewById(R.id.et_device_model);
		et_device_brand = (EditText) findViewById(R.id.et_device_brand);
		et_device_plate03 = (EditText) findViewById(R.id.et_device_plate03);
		et_car_owner = (EditText) findViewById(R.id.et_car_owner);
		et_device_product = (EditText) findViewById(R.id.et_device_product);
		et_property_org = (EditText) findViewById(R.id.et_property_org);
		et_device_operation = (EditText) findViewById(R.id.et_device_operation);
		startUsingDate = (EditText) findViewById(R.id.start_using_time);
		endUsingDate = (EditText) findViewById(R.id.end_using_time);
		/*************** 安全保护装置 ****************/
		et_device_code = (EditText) findViewById(R.id.et_device_code);
		et_device_name = (EditText) findViewById(R.id.et_device_name);
		et_device_salf = (EditText) findViewById(R.id.et_device_salf);
		/*************** 设备派遣 ****************/
		et_device_name01 = (EditText) findViewById(R.id.et_device_name01);
		et_device_user_org = (EditText) findViewById(R.id.et_device_user_org);
		et_device_plate = (EditText) findViewById(R.id.et_device_plate);
		startUseDate = (EditText) findViewById(R.id.start_use_date);
		endUseDate = (EditText) findViewById(R.id.end_use_date);
		/*************** 使用记录 ****************/
		et_device_name02 = (EditText) findViewById(R.id.et_device_name02);
		et_device_user_org01 = (EditText) findViewById(R.id.et_device_user_org01);
		et_device_type = (EditText) findViewById(R.id.et_device_type);
		startEnableDate = (EditText) findViewById(R.id.start_enable_date);
		endEnableDate = (EditText) findViewById(R.id.end_enable_date);
		/*************** 设备到场验证 ****************/
		et_device_name03 = (EditText) findViewById(R.id.et_device_name03);
		et_device_code01 = (EditText) findViewById(R.id.et_device_code01);
		et_device_type01 = (EditText) findViewById(R.id.et_device_type01);
		startVerifyDate = (EditText) findViewById(R.id.start_verify_date);
		endVerifyDate = (EditText) findViewById(R.id.end_verify_date);
		/*************** 日常检查记录 ****************/
		et_device_name04 = (EditText) findViewById(R.id.et_device_name04);
		et_check_code = (EditText) findViewById(R.id.et_check_code);
		et_device_plate01 = (EditText) findViewById(R.id.et_device_plate01);
		startPutDate = (EditText) findViewById(R.id.start_put_date);
		endPutDate = (EditText) findViewById(R.id.end_put_date);
		/*************** 完好利用率 ****************/
		et_device_name05 = (EditText) findViewById(R.id.et_device_name05);
		et_device_user_org02 = (EditText) findViewById(R.id.et_device_user_org02);
		et_device_type02 = (EditText) findViewById(R.id.et_device_type02);
		et_device_month = (EditText) findViewById(R.id.et_device_month);
		/*************** 重点设备月报表 ****************/
		et_device_name06 = (EditText) findViewById(R.id.et_device_name06);
		et_device_user_org03 = (EditText) findViewById(R.id.et_device_user_org03);
		et_device_plate02 = (EditText) findViewById(R.id.et_device_plate02);
		startApplyDate = (EditText) findViewById(R.id.start_apply_date);
		endApplyDate = (EditText) findViewById(R.id.end_apply_date);
		/*************** 保险管理 ****************/
		et_device_name07 = (EditText) findViewById(R.id.et_device_name07);
		et_insurance_name = (EditText) findViewById(R.id.et_insurance_name);
		et_insurance_type = (EditText) findViewById(R.id.et_insurance_type);
		startInsuranceDate = (EditText) findViewById(R.id.start_using_date);
		endInsuranceDate = (EditText) findViewById(R.id.end_using_date);
		context = this;
	}

	@Override
	public void initData() {
		String title = getIntent().getStringExtra("title");
		childAt = getIntent().getIntExtra("childAt", 0);
		titleView.setTitle(title + "查询");
		mVF.setDisplayedChild(childAt);

		sexDialog = new PublishSelectArrayDialog(context, ConstantType.getSex(), tv_publish_type, "请选择性别");
		oilTypeDialog = new PublishSelectArrayDialog(context, ConstantType.getOilType(), tv_device_oil, "请选择燃油类型");
		enviTyoeDialog = new PublishSelectArrayDialog(context, ConstantType.getEnviTyoe(), tv_device_envi, "请选择环保标志");
		sourceMoneyDialog = new PublishSelectArrayDialog(context, ConstantType.getSourceMoney(), tv_device_source, "请选择资金来源");
		financeTyoeDialog = new PublishSelectArrayDialog(context, ConstantType.getFinanceTyoe(), tv_device_finance, "请选择财物对应");
		degreeDialog = new PublishSelectArrayDialog(context, ConstantType.getDegree(), tv_device_degree, "请选择文化程度");
		loadDialog = new LoadDialog(context);
		loadDialog.setTitle("正在加载数据,请稍后...");
		if (childAt == 1) {
			RequestList();
		}
	}

	private void RequestList() {
		loadDialog.show();
		DcNetWorkUtils.getDeviceCategoryList(context, handler, account, password);
		DcNetWorkUtils.getDeviceBelongList(context, handler, account, password);
	}

	@Override
	public void initListener() {
		ll_publish_click_type.setOnClickListener(context);
		ll_device_click_degree.setOnClickListener(context);
		startBirthDate.setOnClickListener(context);
		endBirthDate.setOnClickListener(context);
		startUseDate.setOnClickListener(context);
		endUseDate.setOnClickListener(context);
		startEnableDate.setOnClickListener(context);
		endEnableDate.setOnClickListener(context);
		btn_search.setOnClickListener(context);
		startVerifyDate.setOnClickListener(context);
		endVerifyDate.setOnClickListener(context);
		startPutDate.setOnClickListener(context);
		endPutDate.setOnClickListener(context);
		startApplyDate.setOnClickListener(context);
		endApplyDate.setOnClickListener(context);
		startInsuranceDate.setOnClickListener(context);
		endInsuranceDate.setOnClickListener(context);
		startUsingDate.setOnClickListener(context);
		endUsingDate.setOnClickListener(context);
		et_device_month.setOnClickListener(context);
		ll_device_class_click.setOnClickListener(context);
		ll_device_belong_click.setOnClickListener(context);
		ll_device_oil_click.setOnClickListener(context);
		ll_device_envi_click.setOnClickListener(context);
		ll_device_source_click.setOnClickListener(context);
		ll_device_finance_click.setOnClickListener(context);
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
		case R.id.start_using_time:
			new DateTimePickDialogUtil(this).dateTimePicKDialog(startUsingDate);
			break;
		case R.id.end_using_time:
			new DateTimePickDialogUtil(this).dateTimePicKDialog(endUsingDate);
			break;
		case R.id.start_using_date:
			new DateTimePickDialogUtil(this).dateTimePicKDialog(startInsuranceDate);
			break;
		case R.id.end_using_date:
			new DateTimePickDialogUtil(this).dateTimePicKDialog(endInsuranceDate);
			break;
		case R.id.start_apply_date:
			new DateTimePickDialogUtil(this).dateTimePicKDialog(startApplyDate);
			break;
		case R.id.end_apply_date:
			new DateTimePickDialogUtil(this).dateTimePicKDialog(endApplyDate);
			break;
		case R.id.et_device_month:
			new DateMonthPickDialogUtil(this).dateTimePicKDialog(et_device_month);
			break;
		case R.id.start_put_date:
			new DateTimePickDialogUtil(this).dateTimePicKDialog(startPutDate);
			break;
		case R.id.end_put_date:
			new DateTimePickDialogUtil(this).dateTimePicKDialog(endPutDate);
			break;
		case R.id.start_verify_date:
			new DateTimePickDialogUtil(this).dateTimePicKDialog(startVerifyDate);
			break;
		case R.id.end_verify_date:
			new DateTimePickDialogUtil(this).dateTimePicKDialog(endVerifyDate);
			break;
		case R.id.start_enable_date:
			new DateTimePickDialogUtil(this).dateTimePicKDialog(startEnableDate);
			break;
		case R.id.end_enable_date:
			new DateTimePickDialogUtil(this).dateTimePicKDialog(endEnableDate);
			break;
		case R.id.start_birth_date:
			new DateTimePickDialogUtil(this).dateTimePicKDialog(startBirthDate);
			break;
		case R.id.end_birth_date:
			new DateTimePickDialogUtil(this).dateTimePicKDialog(endBirthDate);
			break;
		case R.id.start_use_date:
			new DateTimePickDialogUtil(this).dateTimePicKDialog(startUseDate);
			break;
		case R.id.end_use_date:
			new DateTimePickDialogUtil(this).dateTimePicKDialog(endUseDate);
			break;
		case R.id.ll_publish_click_type:
			if (sexDialog != null) {
				sexDialog.show();
			}
			break;
		case R.id.ll_device_finance_click:
			if (financeTyoeDialog != null) {
				financeTyoeDialog.show();
			}
			break;
		case R.id.ll_device_oil_click:
			if (oilTypeDialog != null) {
				oilTypeDialog.show();
			}
			break;
		case R.id.ll_device_envi_click:
			if (enviTyoeDialog != null) {
				enviTyoeDialog.show();
			}
			break;
		case R.id.ll_device_source_click:
			if (sourceMoneyDialog != null) {
				sourceMoneyDialog.show();
			}
			break;
		case R.id.ll_device_click_degree:
			if (degreeDialog != null) {
				degreeDialog.show();
			}
			break;
		case R.id.ll_device_class_click:
			if (typeDialog != null) {
				typeDialog.show();
			}
			break;
		case R.id.ll_device_belong_click:
			if (selectTypeDialog != null) {
				selectTypeDialog.show();
			}

			break;
		case R.id.btn_search:
			if (childAt == 0) {
				String name = et_operator_name.getText().toString().trim();
				String sex = tv_publish_type.getText().toString().trim();
				String standardcultre = tv_device_degree.getText().toString().trim();
				String startDateTime = startBirthDate.getText().toString().trim();
				String endDateTime = endBirthDate.getText().toString().trim();
				if (name.length() > 0) {
					isFlag = true;
				} else {
					name = "";
				}
				if (sex.length() > 0) {
					isFlag = true;
					sex = ConstantType.getProjectSex(sex) + "";
				} else {
					sex = "";
				}
				if (standardcultre.length() > 0) {
					isFlag = true;
					standardcultre = ConstantType.getProjectDegree(standardcultre) + "";
				} else {
					standardcultre = "";
				}
				if (startDateTime.length() > 0) {
					isFlag = true;
				} else {
					startDateTime = "";
				}
				if (endDateTime.length() > 0) {
					isFlag = true;
				} else {
					endDateTime = "";
				}
				if (isFlag) {
					Intent intent = new Intent();
					intent.putExtra("name", name);
					intent.putExtra("sex", sex);
					intent.putExtra("standardcultre", standardcultre);
					intent.putExtra("startDateTime", startDateTime);
					intent.putExtra("endDateTime", endDateTime);
					setResult(childAt, intent);
					finish();
				} else {
					MsgToast.geToast().setMsg("搜索內容不能為空~");
				}
			} else if (childAt == 1) {
				String type = tv_device_class.getText().toString().trim();
				String smallType = tv_device_belong.getText().toString().trim();
				String fueltype = tv_device_oil.getText().toString().trim();
				String surroundingSign = tv_device_envi.getText().toString().trim();
				String capotalSource = tv_device_source.getText().toString().trim();
				String curtain = tv_device_finance.getText().toString().trim();
				String name = et_device_nameing.getText().toString().trim();
				String usingCompany = et_device_using.getText().toString().trim();
				String model = et_device_model.getText().toString().trim();
				String brand = et_device_brand.getText().toString().trim();
				String plates = et_device_plate03.getText().toString().trim();
				String driver = et_car_owner.getText().toString().trim();
				String manufacturers = et_device_product.getText().toString().trim();
				String belongtoCompany = et_property_org.getText().toString().trim();
				String operator = et_device_operation.getText().toString().trim();
				String startDateTime = startUsingDate.getText().toString().trim();
				String endDateTime = endUsingDate.getText().toString().trim();
				if (type.length() > 0) {
					isFlag = true;
					Type typeClass = (Type) tv_device_class.getTag();
					type = typeClass.code;
				} else {
					type = "";
				}
				if (smallType.length() > 0) {
					isFlag = true;
					Type typeBelong = (Type) tv_device_belong.getTag();
					smallType = typeBelong.code;
				} else {
					smallType = "";
				}
				if (fueltype.length() > 0) {
					isFlag = true;
					fueltype = ConstantType.getProjectOilType(fueltype) + "";
				} else {
					fueltype = "";
				}
				if (surroundingSign.length() > 0) {
					isFlag = true;
					surroundingSign = ConstantType.getProjectEnviTyoe(surroundingSign) + "";
				} else {
					surroundingSign = "";
				}
				if (capotalSource.length() > 0) {
					isFlag = true;
					capotalSource = ConstantType.getProjectSourceMoney(capotalSource) + "";
				} else {
					capotalSource = "";
				}
				if (curtain.length() > 0) {
					isFlag = true;
					curtain = ConstantType.getProjectFinanceType(curtain) + "";
				} else {
					curtain = "";
				}
				if (name.length() > 0) {
					isFlag = true;
				} else {
					name = "";
				}
				if (usingCompany.length() > 0) {
					isFlag = true;
				} else {
					usingCompany = "";
				}
				if (model.length() > 0) {
					isFlag = true;
				} else {
					model = "";
				}
				if (brand.length() > 0) {
					isFlag = true;
				} else {
					brand = "";
				}
				if (plates.length() > 0) {
					isFlag = true;
				} else {
					plates = "";
				}
				if (driver.length() > 0) {
					isFlag = true;
				} else {
					driver = "";
				}
				if (manufacturers.length() > 0) {
					isFlag = true;
				} else {
					manufacturers = "";
				}
				if (belongtoCompany.length() > 0) {
					isFlag = true;
				} else {
					belongtoCompany = "";
				}
				if (operator.length() > 0) {
					isFlag = true;
				} else {
					operator = "";
				}
				if (startDateTime.length() > 0) {
					isFlag = true;
				} else {
					startDateTime = "";
				}
				if (endDateTime.length() > 0) {
					isFlag = true;
				} else {
					endDateTime = "";
				}
				if (isFlag) {
					Intent intent = new Intent();
					intent.putExtra("type", type);
					intent.putExtra("smallType", smallType);
					intent.putExtra("fueltype", fueltype);
					intent.putExtra("surroundingSign", surroundingSign);
					intent.putExtra("capotalSource", capotalSource);
					intent.putExtra("curtain", curtain);
					intent.putExtra("name", name);
					intent.putExtra("usingCompany", usingCompany);
					intent.putExtra("model", model);
					intent.putExtra("brand", brand);
					intent.putExtra("plates", plates);
					intent.putExtra("driver", driver);
					intent.putExtra("manufacturers", manufacturers);
					intent.putExtra("belongtoCompany", belongtoCompany);
					intent.putExtra("operator", operator);
					intent.putExtra("startDateTime", startDateTime);
					intent.putExtra("endDateTime", endDateTime);
					setResult(childAt, intent);
					finish();
				} else {
					MsgToast.geToast().setMsg("搜索內容不能為空~");
				}
			} else if (childAt == 2) {
				String deviceCode = et_device_code.getText().toString().trim();
				String deviceName = et_device_name.getText().toString().trim();
				String deviceSafety = et_device_salf.getText().toString().trim();
				if (deviceCode.length() > 0) {
					isFlag = true;
				} else {
					deviceCode = "";
				}
				if (deviceName.length() > 0) {
					isFlag = true;
				} else {
					deviceName = "";
				}
				if (deviceSafety.length() > 0) {
					isFlag = true;
				} else {
					deviceSafety = "";
				}
				if (isFlag) {
					Intent intent = new Intent();
					intent.putExtra("deviceCode", deviceCode);
					intent.putExtra("deviceName", deviceName);
					intent.putExtra("deviceSafety", deviceSafety);
					setResult(childAt, intent);
					finish();
				} else {
					MsgToast.geToast().setMsg("搜索內容不能為空~");
				}
			} else if (childAt == 3) {
				String deviceName = et_device_name01.getText().toString().trim();
				String usingCompany = et_device_user_org.getText().toString().trim();
				String deviceModel = et_device_plate.getText().toString().trim();
				String startDateTime = startUseDate.getText().toString().trim();
				String endDateTime = endUseDate.getText().toString().trim();
				if (deviceName.length() > 0) {
					isFlag = true;
				} else {
					deviceName = "";
				}
				if (usingCompany.length() > 0) {
					isFlag = true;
				} else {
					usingCompany = "";
				}
				if (deviceModel.length() > 0) {
					isFlag = true;
				} else {
					deviceModel = "";
				}
				if (startDateTime.length() > 0) {
					isFlag = true;
				} else {
					startDateTime = "";
				}
				if (endDateTime.length() > 0) {
					isFlag = true;
				} else {
					endDateTime = "";
				}
				if (isFlag) {
					Intent intent = new Intent();
					intent.putExtra("deviceName", deviceName);
					intent.putExtra("usingCompany", usingCompany);
					intent.putExtra("deviceModel", deviceModel);
					intent.putExtra("startDateTime", startDateTime);
					intent.putExtra("endDateTime", endDateTime);
					setResult(childAt, intent);
					finish();
				} else {
					MsgToast.geToast().setMsg("搜索內容不能為空~");
				}
			} else if (childAt == 4) {
				String mdName = et_device_name02.getText().toString().trim();
				String mdeUsingCompanyName = et_device_user_org01.getText().toString().trim();
				String mdModel = et_device_type.getText().toString().trim();
				String startDateTime = startEnableDate.getText().toString().trim();
				String endDateTime = endEnableDate.getText().toString().trim();
				if (mdName.length() > 0) {
					isFlag = true;
				} else {
					mdName = "";
				}
				if (mdeUsingCompanyName.length() > 0) {
					isFlag = true;
				} else {
					mdeUsingCompanyName = "";
				}
				if (mdModel.length() > 0) {
					isFlag = true;
				} else {
					mdModel = "";
				}
				if (startDateTime.length() > 0) {
					isFlag = true;
				} else {
					startDateTime = "";
				}
				if (endDateTime.length() > 0) {
					isFlag = true;
				} else {
					endDateTime = "";
				}
				if (isFlag) {
					Intent intent = new Intent();
					intent.putExtra("mdName", mdName);
					intent.putExtra("mdeUsingCompanyName", mdeUsingCompanyName);
					intent.putExtra("mdModel", mdModel);
					intent.putExtra("startDateTime", startDateTime);
					intent.putExtra("endDateTime", endDateTime);
					setResult(childAt, intent);
					finish();
				} else {
					MsgToast.geToast().setMsg("搜索內容不能為空~");
				}
			} else if (childAt == 5) {
				String deviceName = et_device_name03.getText().toString().trim();
				String deviceCode = et_device_code01.getText().toString().trim();
				String model = et_device_type01.getText().toString().trim();
				String startDateTime = startVerifyDate.getText().toString().trim();
				String endDateTime = endVerifyDate.getText().toString().trim();
				if (deviceName.length() > 0) {
					isFlag = true;
				} else {
					deviceName = "";
				}
				if (deviceCode.length() > 0) {
					isFlag = true;
				} else {
					deviceCode = "";
				}
				if (model.length() > 0) {
					isFlag = true;
				} else {
					model = "";
				}
				if (startDateTime.length() > 0) {
					isFlag = true;
				} else {
					startDateTime = "";
				}
				if (endDateTime.length() > 0) {
					isFlag = true;
				} else {
					endDateTime = "";
				}
				if (isFlag) {
					Intent intent = new Intent();
					intent.putExtra("deviceName", deviceName);
					intent.putExtra("deviceCode", deviceCode);
					intent.putExtra("model", model);
					intent.putExtra("startDateTime", startDateTime);
					intent.putExtra("endDateTime", endDateTime);
					setResult(childAt, intent);
					finish();
				} else {
					MsgToast.geToast().setMsg("搜索內容不能為空~");
				}
			} else if (childAt == 6) {
				String deviceName = et_device_name04.getText().toString().trim();
				String code = et_check_code.getText().toString().trim();
				String plate = et_device_plate01.getText().toString().trim();
				String startDateTime = startPutDate.getText().toString().trim();
				String endDateTime = endPutDate.getText().toString().trim();
				if (deviceName.length() > 0) {
					isFlag = true;
				} else {
					deviceName = "";
				}
				if (code.length() > 0) {
					isFlag = true;
				} else {
					code = "";
				}
				if (plate.length() > 0) {
					isFlag = true;
				} else {
					plate = "";
				}
				if (startDateTime.length() > 0) {
					isFlag = true;
				} else {
					startDateTime = "";
				}
				if (endDateTime.length() > 0) {
					isFlag = true;
				} else {
					endDateTime = "";
				}
				if (isFlag) {
					Intent intent = new Intent();
					intent.putExtra("deviceName", deviceName);
					intent.putExtra("code", code);
					intent.putExtra("plate", plate);
					intent.putExtra("startDateTime", startDateTime);
					intent.putExtra("endDateTime", endDateTime);
					setResult(childAt, intent);
					finish();
				} else {
					MsgToast.geToast().setMsg("搜索內容不能為空~");
				}
			} else if (childAt == 7) {
				String deviceName = et_device_name05.getText().toString().trim();
				String usingCompanyName = et_device_user_org02.getText().toString().trim();
				String deviceModel = et_device_type02.getText().toString().trim();
				String month = et_device_month.getText().toString().trim();
				if (deviceName.length() > 0) {
					isFlag = true;
				} else {
					deviceName = "";
				}
				if (usingCompanyName.length() > 0) {
					isFlag = true;
				} else {
					usingCompanyName = "";
				}
				if (deviceModel.length() > 0) {
					isFlag = true;
				} else {
					deviceModel = "";
				}
				if (month.length() > 0) {
					isFlag = true;
				} else {
					month = "";
				}
				if (isFlag) {
					Intent intent = new Intent();
					intent.putExtra("deviceName", deviceName);
					intent.putExtra("usingCompanyName", usingCompanyName);
					intent.putExtra("deviceModel", deviceModel);
					intent.putExtra("month", month);
					setResult(childAt, intent);
					finish();
				} else {
					MsgToast.geToast().setMsg("搜索內容不能為空~");
				}
			} else if (childAt == 8) {
				String deviceName = et_device_name06.getText().toString().trim();
				String company = et_device_user_org03.getText().toString().trim();
				String deviceModel = et_device_plate02.getText().toString().trim();
				String startDateTime = startApplyDate.getText().toString().trim();
				String endDateTime = endApplyDate.getText().toString().trim();
				if (deviceName.length() > 0) {
					isFlag = true;
				} else {
					deviceName = "";
				}
				if (company.length() > 0) {
					isFlag = true;
				} else {
					company = "";
				}
				if (deviceModel.length() > 0) {
					isFlag = true;
				} else {
					deviceModel = "";
				}
				if (startDateTime.length() > 0) {
					isFlag = true;
				} else {
					startDateTime = "";
				}
				if (endDateTime.length() > 0) {
					isFlag = true;
				} else {
					endDateTime = "";
				}
				if (isFlag) {
					Intent intent = new Intent();
					intent.putExtra("deviceName", deviceName);
					intent.putExtra("company", company);
					intent.putExtra("deviceModel", deviceModel);
					intent.putExtra("startDateTime", startDateTime);
					intent.putExtra("endDateTime", endDateTime);
					setResult(childAt, intent);
					finish();
				} else {
					MsgToast.geToast().setMsg("搜索內容不能為空~");
				}
			} else if (childAt == 9) {
				String deviceName = et_device_name07.getText().toString().trim();
				String name = et_insurance_name.getText().toString().trim();
				String startDateTime = et_insurance_type.getText().toString().trim();
				String deviceModel = startInsuranceDate.getText().toString().trim();
				String endDateTime = endInsuranceDate.getText().toString().trim();
				if (deviceName.length() > 0) {
					isFlag = true;
				} else {
					deviceName = "";
				}
				if (name.length() > 0) {
					isFlag = true;
				} else {
					name = "";
				}
				if (deviceModel.length() > 0) {
					isFlag = true;
				} else {
					deviceModel = "";
				}
				if (startDateTime.length() > 0) {
					isFlag = true;
				} else {
					startDateTime = "";
				}
				if (endDateTime.length() > 0) {
					isFlag = true;
				} else {
					endDateTime = "";
				}
				if (isFlag) {
					Intent intent = new Intent();
					intent.putExtra("deviceName", deviceName);
					intent.putExtra("name", name);
					intent.putExtra("deviceModel", deviceModel);
					intent.putExtra("startDateTime", startDateTime);
					intent.putExtra("endDateTime", endDateTime);
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
