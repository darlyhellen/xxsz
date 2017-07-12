package com.xiangxun.xacity.ui.device;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ViewFlipper;

import com.xiangxun.xacity.R;
import com.xiangxun.xacity.app.BaseActivity;
import com.xiangxun.xacity.utils.DateTimePickDialogUtil;
import com.xiangxun.xacity.view.MsgToast;
import com.xiangxun.xacity.view.TitleView;

/**
 * @package: com.xiangxun.xacity.ui.device
 * @ClassName: DeviceSparePartOilSearchActivity.java
 * @Description: 备件油料查询页面
 * @author: HanGJ
 * @date: 2016-1-25 下午3:13:05
 */
public class DeviceSparePartOilSearchActivity extends BaseActivity implements OnClickListener {
	private TitleView titleView;
	private ViewFlipper mVF;
	private int childAt;
	private Button btn_search;
	private boolean isFlag = false;
	/*************** 入库台账记录  ****************/
	private EditText et_materiel_name, et_org_name, et_device_model;
	private EditText startStockDate;
	private EditText endStockDate;
	/*************** 出库台账记录  ****************/
	private EditText et_materiel_name1, et_org_name1, et_device_code, et_device_getPerson, et_project_name;
	private EditText startOutDate;
	private EditText endOutDate;
	/*************** 加油管理  ****************/
	private EditText et_device_name, et_org_name2, et_device_plate;
	private EditText startOilDate;
	private EditText endOilDate;
	/*************** 领料申请  ****************/
	private EditText et_materiel_name2, et_org_name3, et_device_model1;
	private EditText startApplyDate;
	private EditText endApplyDate;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.device_sparepart_search_layout);
		initView();
		initData();
		initListener();
	}

	@Override
	public void initView() {
		titleView = (TitleView) findViewById(R.id.comment_title);
		mVF = (ViewFlipper) findViewById(R.id.viewFlipper);
		btn_search = (Button) findViewById(R.id.btn_search);
		/*************** 入库台账记录  ****************/
		et_materiel_name = (EditText) findViewById(R.id.et_materiel_name);
		et_org_name = (EditText) findViewById(R.id.et_org_name);
		et_device_model = (EditText) findViewById(R.id.et_device_model);
		startStockDate = (EditText) findViewById(R.id.start_stock_date);
		endStockDate = (EditText) findViewById(R.id.end_stock_date);
		/*************** 出库台账记录  ****************/
		et_materiel_name1 = (EditText) findViewById(R.id.et_materiel_name1);
		et_org_name1 = (EditText) findViewById(R.id.et_org_name1);
		et_device_code = (EditText) findViewById(R.id.et_device_code);
		et_device_getPerson = (EditText) findViewById(R.id.et_device_getPerson);
		et_project_name = (EditText) findViewById(R.id.et_project_name);
		startOutDate = (EditText) findViewById(R.id.start_out_date);
		endOutDate = (EditText) findViewById(R.id.end_out_date);
		/*************** 加油管理  ****************/
		et_device_name = (EditText) findViewById(R.id.et_device_name);
		et_org_name2 = (EditText) findViewById(R.id.et_org_name2);
		et_device_plate = (EditText) findViewById(R.id.et_device_plate);
		startOilDate = (EditText) findViewById(R.id.start_oil_date);
		endOilDate = (EditText) findViewById(R.id.end_oil_date);
		/*************** 领料申请  ****************/
		et_materiel_name2 = (EditText) findViewById(R.id.et_materiel_name2);
		et_org_name3 = (EditText) findViewById(R.id.et_org_name3);
		et_device_model1 = (EditText) findViewById(R.id.et_device_model1);
		startApplyDate = (EditText) findViewById(R.id.start_apply_date);
		endApplyDate = (EditText) findViewById(R.id.end_apply_date);
	}

	@Override
	public void initData() {
		String title = getIntent().getStringExtra("title");
		childAt = getIntent().getIntExtra("childAt", 0);
		titleView.setTitle(title + "查询");
		mVF.setDisplayedChild(childAt);
	}

	@Override
	public void initListener() {
		btn_search.setOnClickListener(this);
		startStockDate.setOnClickListener(this);
		endStockDate.setOnClickListener(this);
		startOutDate.setOnClickListener(this);
		endOutDate.setOnClickListener(this);
		startOilDate.setOnClickListener(this);
		endOilDate.setOnClickListener(this);
		startApplyDate.setOnClickListener(this);
		endApplyDate.setOnClickListener(this);
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
		case R.id.start_stock_date:
			new DateTimePickDialogUtil(this).dateTimePicKDialog(startStockDate);
			break;
		case R.id.end_stock_date:
			new DateTimePickDialogUtil(this).dateTimePicKDialog(endStockDate);
			break;
		case R.id.start_out_date:
			new DateTimePickDialogUtil(this).dateTimePicKDialog(startOutDate);
			break;
		case R.id.end_out_date:
			new DateTimePickDialogUtil(this).dateTimePicKDialog(endOutDate);
			break;
		case R.id.start_oil_date:
			new DateTimePickDialogUtil(this).dateTimePicKDialog(startOilDate);
			break;
		case R.id.end_oil_date:
			new DateTimePickDialogUtil(this).dateTimePicKDialog(endOilDate);
			break;
		case R.id.start_apply_date:
			new DateTimePickDialogUtil(this).dateTimePicKDialog(startApplyDate);
			break;
		case R.id.end_apply_date:
			new DateTimePickDialogUtil(this).dateTimePicKDialog(endApplyDate);
			break;
		case R.id.btn_search:
			if (childAt == 0) {
				String name = et_materiel_name.getText().toString().trim();
				String oname = et_org_name.getText().toString().trim();
				String model = et_device_model.getText().toString().trim();
				String startDateTime = startStockDate.getText().toString().trim();
				String endDateTime = endStockDate.getText().toString().trim();
				 if(name.length() > 0){
					 isFlag = true;
				 } else {
					 name = "";
				 }
				 if(oname.length() > 0){
					 isFlag = true;
				 } else {
					 oname = "";
				 }
				 if(model.length() > 0){
					 isFlag = true;
				 } else {
					 model = ""; 
				 }
				 if(startDateTime.length() > 0){
					 isFlag = true;
				 } else {
					 startDateTime = ""; 
				 }
				 if(endDateTime.length() > 0){
					 isFlag = true;
				 } else {
					 endDateTime = ""; 
				 }
				 if(isFlag){
					 Intent intent = new Intent();
					 intent.putExtra("name", name);
					 intent.putExtra("oname", oname);
					 intent.putExtra("model", model);
					 intent.putExtra("startDateTime", startDateTime);
					 intent.putExtra("endDateTime", endDateTime);
					 setResult(childAt, intent);
					 finish();
				 } else {
					 MsgToast.geToast().setMsg("搜索內容不能為空~");
				 }
			} else if (childAt == 1){
				String name = et_materiel_name1.getText().toString().trim();
				String oname = et_org_name1.getText().toString().trim();
				String code = et_device_code.getText().toString().trim();
				String getPerson = et_device_getPerson.getText().toString().trim();
				String projectName = et_project_name.getText().toString().trim();
				String startDateTime = startOutDate.getText().toString().trim();
				String endDateTime = endOutDate.getText().toString().trim();
				 if(name.length() > 0){
					 isFlag = true;
				 } else {
					 name = "";
				 }
				 if(oname.length() > 0){
					 isFlag = true;
				 } else {
					 oname = "";
				 }
				 if(code.length() > 0){
					 isFlag = true;
				 } else {
					 code = ""; 
				 }
				 if(getPerson.length() > 0){
					 isFlag = true;
				 } else {
					 getPerson = ""; 
				 }
				 if(projectName.length() > 0){
					 isFlag = true;
				 } else {
					 projectName = ""; 
				 }
				 if(startDateTime.length() > 0){
					 isFlag = true;
				 } else {
					 startDateTime = ""; 
				 }
				 if(endDateTime.length() > 0){
					 isFlag = true;
				 } else {
					 endDateTime = ""; 
				 }
				 if(isFlag){
					 Intent intent = new Intent();
					 intent.putExtra("name", name);
					 intent.putExtra("oname", oname);
					 intent.putExtra("code", code);
					 intent.putExtra("getPerson", getPerson);
					 intent.putExtra("projectName", projectName);
					 intent.putExtra("startDateTime", startDateTime);
					 intent.putExtra("endDateTime", endDateTime);
					 setResult(childAt, intent);
					 finish();
				 } else {
					 MsgToast.geToast().setMsg("搜索內容不能為空~");
				 }
			} else if (childAt == 2){
				String name = et_device_name.getText().toString().trim();
				String orgName = et_org_name2.getText().toString().trim();
				String plate = et_device_plate.getText().toString().trim();
				String startDateTime = startOilDate.getText().toString().trim();
				String endDateTime = endOilDate.getText().toString().trim();
				 if(name.length() > 0){
					 isFlag = true;
				 } else {
					 name = "";
				 }
				 if(orgName.length() > 0){
					 isFlag = true;
				 } else {
					 orgName = "";
				 }
				 if(plate.length() > 0){
					 isFlag = true;
				 } else {
					 plate = ""; 
				 }
				 if(startDateTime.length() > 0){
					 isFlag = true;
				 } else {
					 startDateTime = ""; 
				 }
				 if(endDateTime.length() > 0){
					 isFlag = true;
				 } else {
					 endDateTime = ""; 
				 }
				 if(isFlag){
					 Intent intent = new Intent();
					 intent.putExtra("name", name);
					 intent.putExtra("orgName", orgName);
					 intent.putExtra("plate", plate);
					 intent.putExtra("startDateTime", startDateTime);
					 intent.putExtra("endDateTime", endDateTime);
					 setResult(childAt, intent);
					 finish();
				 } else {
					 MsgToast.geToast().setMsg("搜索內容不能為空~");
				 }
			} else if (childAt == 3){
				String name = et_materiel_name2.getText().toString().trim();
				String company = et_org_name3.getText().toString().trim();
				String model = et_device_model1.getText().toString().trim();	
				String startDateTime = startApplyDate.getText().toString().trim();	
				String endDateTime = endApplyDate.getText().toString().trim();
				 if(name.length() > 0){
					 isFlag = true;
				 } else {
					 name = "";
				 }
				 if(company.length() > 0){
					 isFlag = true;
				 } else {
					 company = "";
				 }
				 if(model.length() > 0){
					 isFlag = true;
				 } else {
					 model = ""; 
				 }
				 if(startDateTime.length() > 0){
					 isFlag = true;
				 } else {
					 startDateTime = ""; 
				 }
				 if(endDateTime.length() > 0){
					 isFlag = true;
				 } else {
					 endDateTime = ""; 
				 }
				 if(isFlag){
					 Intent intent = new Intent();
					 intent.putExtra("name", name);
					 intent.putExtra("company", company);
					 intent.putExtra("model", model);
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
