package com.xiangxun.xacity.ui.device;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.xiangxun.xacity.R;
import com.xiangxun.xacity.app.BaseActivity;
import com.xiangxun.xacity.utils.DateTimePickDialogUtil;
import com.xiangxun.xacity.view.MsgToast;
import com.xiangxun.xacity.view.TitleView;

/**
 * @package: com.xiangxun.xacity.ui.device
 * @ClassName: DeviceScrapSearchActivity.java
 * @Description: 设备报废查询页面
 * @author: HanGJ
 * @date: 2016-1-25 下午2:58:34
 */
public class DeviceScrapSearchActivity extends BaseActivity implements OnClickListener {
	private TitleView titleView;
	private Button btn_search;
	private boolean isFlag = false;
	private EditText et_purchase_code, et_device_name, et_device_model;
	private EditText startApplyDate;
	private EditText endApplyDate;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.device_scrap_search_layout);
		initView();
		initData();
		initListener();
	}

	@Override
	public void initView() {
		titleView = (TitleView) findViewById(R.id.comment_title);
		btn_search = (Button) findViewById(R.id.btn_search);
		et_purchase_code = (EditText) findViewById(R.id.et_purchase_code);
		et_device_name = (EditText) findViewById(R.id.et_device_name);
		et_device_model = (EditText) findViewById(R.id.et_device_model);
		startApplyDate = (EditText) findViewById(R.id.start_apply_date);
		endApplyDate = (EditText) findViewById(R.id.end_apply_date);
	}

	@Override
	public void initData() {
		String title = getIntent().getStringExtra("title");
		titleView.setTitle(title + "查询");
	}

	@Override
	public void initListener() {
		startApplyDate.setOnClickListener(this);
		endApplyDate.setOnClickListener(this);
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
		case R.id.btn_search:
			String code = et_purchase_code.getText().toString().trim();
			String deviceName = et_device_name.getText().toString().trim();
			String deviceCode = et_device_model.getText().toString().trim();
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
			 if(deviceCode.length() > 0){
				 isFlag = true;
			 } else {
				 deviceCode = ""; 
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
				 intent.putExtra("deviceCode", deviceCode);
				 intent.putExtra("startDate", startDate);
				 intent.putExtra("endDate", endDate);
				 setResult(Activity.RESULT_OK, intent);
				 finish();
			 } else {
				 MsgToast.geToast().setMsg("搜索內容不能為空~");
			 }
			break;
		}
	}

}
