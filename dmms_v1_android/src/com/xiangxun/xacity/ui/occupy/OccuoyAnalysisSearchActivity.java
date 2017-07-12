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
 * @ClassName: OccuoyCountSearchActivity.java
 * @Description: 挖占统计查询页面
 * @author: HanGJ
 * @date: 2016-1-26 下午3:57:50
 */
public class OccuoyAnalysisSearchActivity extends BaseActivity implements OnClickListener {
	private OccuoyAnalysisSearchActivity context;
	private TitleView titleView;
	private ViewFlipper mVF;
	private int childAt;
	private LoadDialog loadDialog;
	private Button btn_search;
	private boolean isFlag = false;
	private EditText et_road_name;
	private TextView tv_occupy_area;
	private LinearLayout ll_occupy_area_click;
	private EditText start_occupy_time;
	private EditText end_occupy_time;

	private PublishSelectTypeDialog areaDialog;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			loadDialog.dismiss();
			switch (msg.what) {
			case ConstantStatus.getOccupyAreaSuccess:
				List<Type> areaTypes = (List<Type>) msg.obj;
				if (areaTypes != null && areaTypes.size() > 0) {
					areaDialog = new PublishSelectTypeDialog(context, areaTypes, tv_occupy_area, "请选择辖区");
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
		context = this;
		setContentView(R.layout.occupy_statistics_search_layout);
		initView();
		initData();
		initListener();
	}

	@Override
	public void initView() {
		titleView = (TitleView) findViewById(R.id.comment_title);
		mVF = (ViewFlipper) findViewById(R.id.viewFlipper);
		btn_search = (Button) findViewById(R.id.btn_search);
		et_road_name = (EditText) findViewById(R.id.et_road_name);
		tv_occupy_area = (TextView) findViewById(R.id.tv_occupy_area);
		ll_occupy_area_click = (LinearLayout) findViewById(R.id.ll_occupy_area_click);
		start_occupy_time = (EditText) findViewById(R.id.start_occupy_time);
		end_occupy_time = (EditText) findViewById(R.id.end_occupy_time);
	}

	@Override
	public void initData() {
		String title = getIntent().getStringExtra("title");
		childAt = getIntent().getIntExtra("childAt", 0);
		titleView.setTitle(title + "查询");
		mVF.setDisplayedChild(0);
		loadDialog = new LoadDialog(context);
		loadDialog.setTitle("正在加载数据,请稍后...");
		RequestList();
	}

	private void RequestList() {
		loadDialog.show();
		DcNetWorkUtils.getOccupyAreaInfoList(context, handler, account, password);
	}

	@Override
	public void initListener() {
		btn_search.setOnClickListener(context);
		ll_occupy_area_click.setOnClickListener(context);
		start_occupy_time.setOnClickListener(context);
		end_occupy_time.setOnClickListener(context);
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
		case R.id.ll_occupy_area_click:
			if (areaDialog != null) {
				areaDialog.show();
			} else {
				RequestList();
			}
			break;
		case R.id.btn_search:
			String managearea = tv_occupy_area.getText().toString().trim();
			String roadname = et_road_name.getText().toString().trim();
			String starttime = start_occupy_time.getText().toString().trim();
			String endtime = end_occupy_time.getText().toString().trim();
			if (managearea.length() > 0) {
				isFlag = true;
				Type types = (Type) tv_occupy_area.getTag();
				managearea = types.id;
			} else {
				managearea = "";
			}
			if (roadname.length() > 0) {
				isFlag = true;
			} else {
				roadname = "";
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
				intent.putExtra("managearea", managearea);
				intent.putExtra("roadname", roadname);
				intent.putExtra("startTime", starttime);
				intent.putExtra("endTime", endtime);
				setResult(childAt, intent);
				finish();
			} else {
				MsgToast.geToast().setMsg("搜索內容不能為空~");
			}
			break;
		}
	}

}
