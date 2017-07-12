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
 * @ClassName: DevicePurchaseSearchActivity.java
 * @Description: 设备采购查询页面
 * @author: HanGJ
 * @date: 2016-1-19 下午2:20:16
 */
public class DevicePurchaseSearchActivity extends BaseActivity implements OnClickListener {
	private TitleView titleView;
	private ViewFlipper mVF;
	private int childAt;
	private Button btn_search;
	private boolean isFlag = false;
	/*************** 合格供方名册  ****************/
	private EditText et_supplier_name, et_supplier_link, et_supplier_product;
	/*************** 采购租赁申请  ****************/
	private EditText et_purchase_code, et_device_name, et_device_model;
	private EditText startApplyDate;
	private EditText endApplyDate;
	/*************** 设备验收  ****************/
	private EditText et_manufacturer_name, et_manufacturer_deviceName, et_manufacturer_model;
	private EditText startArrivalDate;
	private EditText endArrivalDate;
	/*************** 外租设备台账  ****************/
	private EditText et_org_name, et_org_deviceName, et_org_model;
	private EditText startLeaseDate;
	private EditText endLeaseDate;
	/*************** 合同台账  ****************/
	private EditText et_contract_name, et_other_org, et_purchase_org;
	private EditText startSignedDate;
	private EditText endSignedDate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.device_purchase_search_layout);
		initView();
		initData();
		initListener();
	}

	@Override
	public void initView() {
		titleView = (TitleView) findViewById(R.id.comment_title);
		mVF = (ViewFlipper) findViewById(R.id.viewFlipper);
		btn_search = (Button) findViewById(R.id.btn_search);
		/*************** 合格供方名册  ****************/
		et_supplier_name = (EditText) findViewById(R.id.et_supplier_name);
		et_supplier_link = (EditText) findViewById(R.id.et_supplier_link);
		et_supplier_product = (EditText) findViewById(R.id.et_supplier_product);
		/*************** 采购租赁申请  ****************/
		et_purchase_code = (EditText) findViewById(R.id.et_purchase_code);
		et_device_name = (EditText) findViewById(R.id.et_device_name);
		et_device_model = (EditText) findViewById(R.id.et_device_model);
		startApplyDate = (EditText) findViewById(R.id.start_apply_date);
		endApplyDate = (EditText) findViewById(R.id.end_apply_date);
		/*************** 设备验收  ****************/
		et_manufacturer_name = (EditText) findViewById(R.id.et_manufacturer_name);
		et_manufacturer_deviceName = (EditText) findViewById(R.id.et_manufacturer_deviceName);
		et_manufacturer_model = (EditText) findViewById(R.id.et_manufacturer_model);
		startArrivalDate = (EditText) findViewById(R.id.start_arrival_date);
		endArrivalDate = (EditText) findViewById(R.id.end_arrival_date);
		/*************** 外租设备台账  ****************/
		et_org_name = (EditText) findViewById(R.id.et_org_name);
		et_org_deviceName = (EditText) findViewById(R.id.et_org_deviceName);
		et_org_model = (EditText) findViewById(R.id.et_org_model);
		startLeaseDate = (EditText) findViewById(R.id.start_lease_date);
		endLeaseDate = (EditText) findViewById(R.id.end_lease_date);
		/*************** 合同台账  ****************/
		et_contract_name = (EditText) findViewById(R.id.et_contract_name);
		et_other_org = (EditText) findViewById(R.id.et_other_org);
		et_purchase_org = (EditText) findViewById(R.id.et_purchase_org);
		startSignedDate = (EditText) findViewById(R.id.start_signed_date);
		endSignedDate = (EditText) findViewById(R.id.end_signed_date);
	}

	@Override
	public void initData() {
		String title = getIntent().getStringExtra("title");
		childAt = getIntent().getIntExtra("childAt", 0);
		titleView.setTitle(title + "查询");
		startApplyDate.setOnClickListener(this);
//		endApplyDate.setText(Tools.getTodayDate());
//		startArrivalDate.setText(Tools.getTodayDate());
//		endArrivalDate.setText(Tools.getTodayDate());
//		startLeaseDate.setText(Tools.getTodayDate());
//		endLeaseDate.setText(Tools.getTodayDate());
//		startSignedDate.setText(Tools.getTodayDate());
//		endSignedDate.setText(Tools.getTodayDate());
		mVF.setDisplayedChild(childAt);
	}

	@Override
	public void initListener() {
		startApplyDate.setOnClickListener(this);
		endApplyDate.setOnClickListener(this);
		startArrivalDate.setOnClickListener(this);
		endArrivalDate.setOnClickListener(this);
		startLeaseDate.setOnClickListener(this);
		endLeaseDate.setOnClickListener(this);
		startSignedDate.setOnClickListener(this);
		endSignedDate.setOnClickListener(this);
		btn_search.setOnClickListener(this);
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
			new DateTimePickDialogUtil(this).dateTimePicKDialog(startApplyDate);
			break;
		case R.id.end_apply_date:
			new DateTimePickDialogUtil(this).dateTimePicKDialog(endApplyDate);
			break;
		case R.id.start_arrival_date:
			new DateTimePickDialogUtil(this).dateTimePicKDialog(startArrivalDate);
			break;
		case R.id.end_arrival_date:
			new DateTimePickDialogUtil(this).dateTimePicKDialog(endArrivalDate);
			break;
		case R.id.start_lease_date:
			new DateTimePickDialogUtil(this).dateTimePicKDialog(startLeaseDate);
			break;
		case R.id.end_lease_date:
			new DateTimePickDialogUtil(this).dateTimePicKDialog(endLeaseDate);
			break;
		case R.id.start_signed_date:
			new DateTimePickDialogUtil(this).dateTimePicKDialog(startSignedDate);
			break;
		case R.id.end_signed_date:
			new DateTimePickDialogUtil(this).dateTimePicKDialog(endSignedDate);
			break;
		case R.id.btn_search:
			if (childAt == 0) {
				 String name = et_supplier_name.getText().toString().trim();
				 String linkman = et_supplier_link.getText().toString().trim();
				 String product = et_supplier_product.getText().toString().trim();
				 if(name.length() > 0){
					 isFlag = true;
				 } else {
					 name = "";
				 }
				 if(linkman.length() > 0){
					 isFlag = true;
				 } else {
					 linkman = "";
				 }
				 if(product.length() > 0){
					 isFlag = true;
				 } else {
					 product = ""; 
				 }
				 if(isFlag){
					 Intent intent = new Intent();
					 intent.putExtra("name", name);
					 intent.putExtra("linkman", linkman);
					 intent.putExtra("product", product);
					 setResult(childAt, intent);
					 finish();
				 } else {
					 MsgToast.geToast().setMsg("搜索內容不能為空~");
				 }
			} else if (childAt == 1) {
				String code = et_purchase_code.getText().toString().trim();
				String deviceName = et_device_name.getText().toString().trim();
				String model = et_device_model.getText().toString().trim();
				String startDate = startApplyDate.getText().toString().trim();
				String endDate = endApplyDate.getText().toString().trim();
				 if(code.length() > 0){
					 isFlag = true;
				 } else {
					 code = "";
				 }
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
				 if(startDate.length() > 0){
					 isFlag = true;
				 } else {
					 startDate = ""; 
				 }
				 if(endDate.length() > 0){
					 isFlag = true;
				 } else {
					 endDate = ""; 
				 }
				 if(isFlag){
					 Intent intent = new Intent();
					 intent.putExtra("code", code);
					 intent.putExtra("deviceName", deviceName);
					 intent.putExtra("model", model);
					 intent.putExtra("startDate", startDate);
					 intent.putExtra("endDate", endDate);
					 setResult(childAt, intent);
					 finish();
				 } else {
					 MsgToast.geToast().setMsg("搜索內容不能為空~");
				 }
			} else if (childAt == 2) {
				String manufacturer = et_manufacturer_name.getText().toString().trim();
				String deviceName = et_manufacturer_deviceName.getText().toString().trim();
				String model = et_manufacturer_model.getText().toString().trim();
				String startDate = startArrivalDate.getText().toString().trim();
				String endDate = endArrivalDate.getText().toString().trim();
				 if(manufacturer.length() > 0){
					 isFlag = true;
				 } else {
					 manufacturer = "";
				 }
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
				 if(startDate.length() > 0){
					 isFlag = true;
				 } else {
					 startDate = ""; 
				 }
				 if(endDate.length() > 0){
					 isFlag = true;
				 } else {
					 endDate = ""; 
				 }
				 if(isFlag){
					 Intent intent = new Intent();
					 intent.putExtra("manufacturer", manufacturer);
					 intent.putExtra("deviceName", deviceName);
					 intent.putExtra("model", model);
					 intent.putExtra("startDate", startDate);
					 intent.putExtra("endDate", endDate);
					 setResult(childAt, intent);
					 finish();
				 } else {
					 MsgToast.geToast().setMsg("搜索內容不能為空~");
				 }
			} else if (childAt == 3) {
				String deviceSupplierName = et_org_name.getText().toString().trim();
				String deviceName = et_org_deviceName.getText().toString().trim();
				String model = et_org_model.getText().toString().trim();
				String startDate = startLeaseDate.getText().toString().trim();
				String endDate = endLeaseDate.getText().toString().trim();
				 if(deviceSupplierName.length() > 0){
					 isFlag = true;
				 } else {
					 deviceSupplierName = "";
				 }
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
				 if(startDate.length() > 0){
					 isFlag = true;
				 } else {
					 startDate = ""; 
				 }
				 if(endDate.length() > 0){
					 isFlag = true;
				 } else {
					 endDate = ""; 
				 }
				 if(isFlag){
					 Intent intent = new Intent();
					 intent.putExtra("deviceSupplierName", deviceSupplierName);
					 intent.putExtra("deviceName", deviceName);
					 intent.putExtra("model", model);
					 intent.putExtra("startDate", startDate);
					 intent.putExtra("endDate", endDate);
					 setResult(childAt, intent);
					 finish();
				 } else {
					 MsgToast.geToast().setMsg("搜索內容不能為空~");
				 }
			} else if (childAt == 4) {
				String name = et_contract_name.getText().toString().trim();
				String cooperationDepartment = et_other_org.getText().toString().trim();
				String purchaseDeparmentId = et_purchase_org.getText().toString().trim();
				String startDate = startSignedDate.getText().toString().trim();
				String endDate = endSignedDate.getText().toString().trim();
				 if(name.length() > 0){
					 isFlag = true;
				 } else {
					 name = "";
				 }
				 if(cooperationDepartment.length() > 0){
					 isFlag = true;
				 } else {
					 cooperationDepartment = "";
				 }
				 if(purchaseDeparmentId.length() > 0){
					 isFlag = true;
				 } else {
					 purchaseDeparmentId = ""; 
				 }
				 if(startDate.length() > 0){
					 isFlag = true;
				 } else {
					 startDate = ""; 
				 }
				 if(endDate.length() > 0){
					 isFlag = true;
				 } else {
					 endDate = ""; 
				 }
				 if(isFlag){
					 Intent intent = new Intent();
					 intent.putExtra("name", name);
					 intent.putExtra("cooperationDepartment", cooperationDepartment);
					 intent.putExtra("purchaseDeparmentId", purchaseDeparmentId);
					 intent.putExtra("startDate", startDate);
					 intent.putExtra("endDate", endDate);
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
