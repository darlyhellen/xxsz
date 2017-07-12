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
 * @ClassName: DeviceMaintainSearchActivity.java
 * @Description: 设备维修查询页面
 * @author: HanGJ
 * @date: 2016-1-25 下午2:58:13
 */
public class DeviceMaintainSearchActivity extends BaseActivity implements OnClickListener {
	private TitleView titleView;
	private ViewFlipper mVF;
	private int childAt;
	private Button btn_search;
	private boolean isFlag = false;
	/*************** 设备维修计划   ****************/
	private EditText et_device_name, et_device_model, et_device_plate;
	private EditText startMaintainDate;
	private EditText endMaintainDate;
	/*************** 维修工单   ****************/
	private EditText et_receive_org, et_device_name1, et_maintain_code;
	private EditText startArrivalDate;
	private EditText endArrivalDate;
	/*************** 维修记录   ****************/
	private EditText et_device_name2, et_device_code, et_device_cost;
	private EditText startApplyDate;
	private EditText endApplyDate;
	/*************** 事故报告记录   ****************/
	private EditText et_device_name3, et_device_code1, et_accident_code;
	private EditText startAccidentDate;
	private EditText endAccidentDate;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.device_maintain_search_layout);
		initView();
		initData();
		initListener();
	}

	@Override
	public void initView() {
		titleView = (TitleView) findViewById(R.id.comment_title);
		mVF = (ViewFlipper) findViewById(R.id.viewFlipper);
		btn_search = (Button) findViewById(R.id.btn_search);
		/*************** 设备维修计划   ****************/
		et_device_name = (EditText) findViewById(R.id.et_device_name);
		et_device_model = (EditText) findViewById(R.id.et_device_model);
		et_device_plate = (EditText) findViewById(R.id.et_device_plate);
		startMaintainDate = (EditText) findViewById(R.id.start_maintain_date);
		endMaintainDate = (EditText) findViewById(R.id.end_maintain_date);
		/*************** 维修工单   ****************/
		et_receive_org = (EditText) findViewById(R.id.et_receive_org);
		et_device_name1 = (EditText) findViewById(R.id.et_device_name1);
		et_maintain_code = (EditText) findViewById(R.id.et_maintain_code);
		startArrivalDate = (EditText) findViewById(R.id.start_arrival_date);
		endArrivalDate = (EditText) findViewById(R.id.end_arrival_date);
		/*************** 维修记录   ****************/
		et_device_name2 = (EditText) findViewById(R.id.et_device_name2);
		et_device_code = (EditText) findViewById(R.id.et_device_code);
		et_device_cost = (EditText) findViewById(R.id.et_device_cost);
		startApplyDate = (EditText) findViewById(R.id.start_apply_date);
		endApplyDate = (EditText) findViewById(R.id.end_apply_date);
		/*************** 事故报告记录   ****************/
		et_device_name3 = (EditText) findViewById(R.id.et_device_name3);
		et_device_code1 = (EditText) findViewById(R.id.et_device_code1);
		et_accident_code = (EditText) findViewById(R.id.et_accident_code);
		startAccidentDate = (EditText) findViewById(R.id.start_accident_date);
		endAccidentDate = (EditText) findViewById(R.id.end_accident_date);
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
		startMaintainDate.setOnClickListener(this);
		endMaintainDate.setOnClickListener(this);
		startArrivalDate.setOnClickListener(this);
		endArrivalDate.setOnClickListener(this);
		startApplyDate.setOnClickListener(this);
		endApplyDate.setOnClickListener(this);
		startAccidentDate.setOnClickListener(this);
		endAccidentDate.setOnClickListener(this);
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
		case R.id.start_accident_date:
			new DateTimePickDialogUtil(this).dateTimePicKDialog(startAccidentDate);
			break;
		case R.id.end_accident_date:
			new DateTimePickDialogUtil(this).dateTimePicKDialog(endAccidentDate);
			break;
		case R.id.start_arrival_date:
			new DateTimePickDialogUtil(this).dateTimePicKDialog(startArrivalDate);
			break;
		case R.id.end_arrival_date:
			new DateTimePickDialogUtil(this).dateTimePicKDialog(endArrivalDate);
			break;
		case R.id.start_maintain_date:
			new DateTimePickDialogUtil(this).dateTimePicKDialog(startMaintainDate);
			break;
		case R.id.end_maintain_date:
			new DateTimePickDialogUtil(this).dateTimePicKDialog(endMaintainDate);
			break;
		case R.id.start_apply_date:
			new DateTimePickDialogUtil(this).dateTimePicKDialog(startApplyDate);
			break;
		case R.id.end_apply_date:
			new DateTimePickDialogUtil(this).dateTimePicKDialog(endApplyDate);
			break;
		case R.id.btn_search:
			if (childAt == 0) {// 设备维修计划查询
				String deviceName = et_device_name.getText().toString().trim();
				String model = et_device_model.getText().toString().trim();
				String plate = et_device_plate.getText().toString().trim();
				String startDateTime = startMaintainDate.getText().toString().trim();
				String endDateTime = endMaintainDate.getText().toString().trim();
				if(deviceName.length() > 0){
					 isFlag = true;
				 } else {
					 deviceName = "";
				 }
				 if(model.length() > 0){
					 isFlag = true;
				 } else {
					 model = "";
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
					 intent.putExtra("deviceName", deviceName);
					 intent.putExtra("deviceCode", model);
					 intent.putExtra("plate", plate);
					 intent.putExtra("startDateTime", startDateTime);
					 intent.putExtra("endDateTime", endDateTime);
					 setResult(childAt, intent);
					 finish();
				 } else {
					 MsgToast.geToast().setMsg("搜索內容不能為空~");
				 }
			} else if (childAt == 1) {// 维修工单查询
				String repairFactory = et_receive_org.getText().toString().trim();
				String deviceName = et_device_name1.getText().toString().trim();
				String code = et_maintain_code.getText().toString().trim();
				String startDateTime = startArrivalDate.getText().toString().trim();
				String endDateTime = endArrivalDate.getText().toString().trim();
				if(deviceName.length() > 0){
					 isFlag = true;
				 } else {
					 deviceName = "";
				 }
				 if(repairFactory.length() > 0){
					 isFlag = true;
				 } else {
					 repairFactory = "";
				 }
				 if(code.length() > 0){
					 isFlag = true;
				 } else {
					 code = ""; 
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
					 intent.putExtra("deviceName", deviceName);
					 intent.putExtra("repairFactory", repairFactory);
					 intent.putExtra("code", code);
					 intent.putExtra("startDateTime", startDateTime);
					 intent.putExtra("endDateTime", endDateTime);
					 setResult(childAt, intent);
					 finish();
				 } else {
					 MsgToast.geToast().setMsg("搜索內容不能為空~");
				 }
			} else if (childAt == 2) {// 维修记录查询
				String deviceName = et_device_name2.getText().toString().trim();
				String deviceCode = et_device_code.getText().toString().trim();
				String cost = et_device_cost.getText().toString().trim();
				String startDateTime = startApplyDate.getText().toString().trim();	
				String endDateTime = endApplyDate.getText().toString().trim();
				if(deviceName.length() > 0){
					 isFlag = true;
				 } else {
					 deviceName = "";
				 }
				 if(deviceCode.length() > 0){
					 isFlag = true;
				 } else {
					 deviceCode = "";
				 }
				 if(cost.length() > 0){
					 isFlag = true;
				 } else {
					 cost = ""; 
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
					 intent.putExtra("deviceName", deviceName);
					 intent.putExtra("deviceCode", deviceCode);
					 intent.putExtra("cost", cost);
					 intent.putExtra("startDateTime", startDateTime);
					 intent.putExtra("endDateTime", endDateTime);
					 setResult(childAt, intent);
					 finish();
				 } else {
					 MsgToast.geToast().setMsg("搜索內容不能為空~");
				 }
			} else if (childAt == 3) {// 事故报告记录查询
				String deviceName = et_device_name3.getText().toString().trim();
				String deviceCode = et_device_code1.getText().toString().trim();
				String code = et_accident_code.getText().toString().trim();
				String startDateTime = startAccidentDate.getText().toString().trim();	
				String endDateTime = endAccidentDate.getText().toString().trim();
				if(deviceName.length() > 0){
					 isFlag = true;
				 } else {
					 deviceName = "";
				 }
				 if(deviceCode.length() > 0){
					 isFlag = true;
				 } else {
					 deviceCode = "";
				 }
				 if(code.length() > 0){
					 isFlag = true;
				 } else {
					 code = ""; 
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
					 intent.putExtra("deviceName", deviceName);
					 intent.putExtra("deviceCode", deviceCode);
					 intent.putExtra("code", code);
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
