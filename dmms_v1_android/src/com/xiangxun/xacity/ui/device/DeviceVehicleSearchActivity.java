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
import com.xiangxun.xacity.view.MsgToast;
import com.xiangxun.xacity.view.TitleView;

/**
 * @package: com.xiangxun.xacity.ui.device
 * @ClassName: DeviceSparePartOilSearchActivity.java
 * @Description: 车辆信息查询页面
 * @author: HanGJ
 * @date: 2016-4-26 下午3:13:05
 */
public class DeviceVehicleSearchActivity extends BaseActivity implements
		OnClickListener {
	private TitleView titleView;
	private Button btn_search;
	private boolean isFlag = false;
	private EditText et_device_name, et_vehicle_plate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.device_vehicle_search_layout);
		initView();
		initData();
		initListener();
	}

	@Override
	public void initView() {
		titleView = (TitleView) findViewById(R.id.comment_title);
		btn_search = (Button) findViewById(R.id.btn_search);
		et_device_name = (EditText) findViewById(R.id.et_device_name);
		et_vehicle_plate = (EditText) findViewById(R.id.et_vehicle_plate);
	}

	@Override
	public void initData() {
		titleView.setTitle("车辆信息查询");
	}

	@Override
	public void initListener() {
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
		case R.id.btn_search:
			String deviceName = et_device_name.getText().toString().trim();
			String plate = et_vehicle_plate.getText().toString().trim();
			if (deviceName.length() > 0) {
				isFlag = true;
			} else {
				deviceName = "";
			}
			if (plate.length() > 0) {
				isFlag = true;
			} else {
				plate = "";
			}
			if (isFlag) {
				Intent intent = new Intent();
				intent.putExtra("deviceCode", deviceName);
				intent.putExtra("plate", plate);
				setResult(Activity.RESULT_OK, intent);
				finish();
			} else {
				MsgToast.geToast().setMsg("搜索內容不能為空~");
			}
			break;
		}
	}

}
