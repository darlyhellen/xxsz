package com.xiangxun.xacity.ui.occupy;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
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
import com.xiangxun.xacity.request.DcNetWorkUtils;
import com.xiangxun.xacity.utils.DateTimePickDialogUtil;
import com.xiangxun.xacity.view.LoadDialog;
import com.xiangxun.xacity.view.MsgToast;
import com.xiangxun.xacity.view.PublishSelectTypeDialog;
import com.xiangxun.xacity.view.PublishSelectTypeDialog.onSelectItemClick;
import com.xiangxun.xacity.view.TitleView;

/**
 * @package: com.xiangxun.xacity.ui.occupy
 * @ClassName: OccuoyCountSearchActivity.java
 * @Description: 挖占统计搜索页面
 * @author: HanGJ
 * @date: 2016-4-27 下午4:06:50
 */
public class OccupyStatisticsSearchActivity extends BaseActivity implements
		OnClickListener, onSelectItemClick {
	private OccupyStatisticsSearchActivity context;
	private TitleView titleView;
	private TextView tv_type01;
	private TextView tv_type02;
	private EditText start_occupy_time;
	private EditText end_occupy_time;
	private List<Type> types = new ArrayList<Type>();
	private TextView tv_occupy_querytype;
	private LinearLayout ll_occupy_querytype_click;
	private PublishSelectTypeDialog analyDialog;
	private TextView tv_occupy_area;
	private LinearLayout ll_occupy_area_click;
	private PublishSelectTypeDialog roadDialog;
	private TextView tv_occupy_faceType;
	private LinearLayout ll_occupy_faceType_click;
	private PublishSelectTypeDialog typeDialog;
	private LoadDialog loadDialog;
	private Button btn_search;
	private int type = 0;
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			loadDialog.dismiss();
			switch (msg.what) {
			case ConstantStatus.getOccupyTypeSuccess:
				List<Type> types = (List<Type>) msg.obj;
				if (types != null && types.size() > 0) {
					typeDialog = new PublishSelectTypeDialog(context, types,
							tv_occupy_faceType, "请选择挖占类型");
				}
				break;
			case ConstantStatus.getOccupyRoadTypeSuccess:
				List<Type> roadTypes = (List<Type>) msg.obj;
				if (roadTypes != null && roadTypes.size() > 0) {
					if (type == 2) {
						typeDialog = new PublishSelectTypeDialog(context,
								roadTypes, tv_occupy_faceType, "请选择路面种类");
					} else {
						roadDialog = new PublishSelectTypeDialog(context,
								roadTypes, tv_occupy_area, "请选择路面种类");
					}
				}
				break;
			case ConstantStatus.getOccupyAreaSuccess:
				List<Type> areaTypes = (List<Type>) msg.obj;
				if (areaTypes != null && areaTypes.size() > 0) {
					roadDialog = new PublishSelectTypeDialog(context,
							areaTypes, tv_occupy_area, "请选择辖区");

				}
				break;
			case ConstantStatus.getOccupyListFailed:

				break;
			case ConstantStatus.NetWorkError:
				MsgToast.geToast().setMsg("网络异常,请稍后重试!");
				break;
			}
			super.handleMessage(msg);
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.occupy_statis_search_layout);
		initView();
		initData();
		initListener();
	}

	@Override
	public void initView() {
		titleView = (TitleView) findViewById(R.id.comment_title);
		start_occupy_time = (EditText) findViewById(R.id.start_occupy_time);
		end_occupy_time = (EditText) findViewById(R.id.end_occupy_time);
		tv_type01 = (TextView) findViewById(R.id.tv_type01);
		tv_type02 = (TextView) findViewById(R.id.tv_type02);
		btn_search = (Button) findViewById(R.id.btn_search);
		tv_occupy_querytype = (TextView) findViewById(R.id.tv_occupy_querytype);
		ll_occupy_querytype_click = (LinearLayout) findViewById(R.id.ll_occupy_querytype_click);
		tv_occupy_area = (TextView) findViewById(R.id.tv_occupy_area);
		ll_occupy_area_click = (LinearLayout) findViewById(R.id.ll_occupy_area_click);
		tv_occupy_faceType = (TextView) findViewById(R.id.tv_occupy_faceType);
		ll_occupy_faceType_click = (LinearLayout) findViewById(R.id.ll_occupy_faceType_click);
	}

	@Override
	public void initData() {
		titleView.setTitle("挖占统计查询");
		loadDialog = new LoadDialog(context);
		loadDialog.setTitle("正在加载数据,请稍后...");
		queryType();
		analyDialog = new PublishSelectTypeDialog(this, types,
				tv_occupy_querytype, "请选择统计粒度");
		analyDialog.setSelection(0);
		RequestList(0);
	}

	private void RequestList(int type) {
		if (type == 0) {
			DcNetWorkUtils.getOccupyTypeList(context, handler, "1", account,
					password);
			DcNetWorkUtils.getOccupyRoadTypeList(context, handler, account,
					password);
		} else if (type == 1) {
			DcNetWorkUtils.getOccupyAreaInfoList(context, handler, account,
					password);
			DcNetWorkUtils.getOccupyTypeList(context, handler, "1", account,
					password);
		} else {
			DcNetWorkUtils.getOccupyAreaInfoList(context, handler, account,
					password);
			DcNetWorkUtils.getOccupyRoadTypeList(context, handler, account,
					password);
		}
	}

	@Override
	public void initListener() {
		start_occupy_time.setOnClickListener(context);
		end_occupy_time.setOnClickListener(context);
		ll_occupy_querytype_click.setOnClickListener(context);
		ll_occupy_faceType_click.setOnClickListener(context);
		ll_occupy_area_click.setOnClickListener(context);
		analyDialog.setSelectItemClick(context);
		btn_search.setOnClickListener(context);
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
			new DateTimePickDialogUtil(this)
					.dateTimePicKDialog(start_occupy_time);
			break;
		case R.id.end_occupy_time:
			new DateTimePickDialogUtil(this)
					.dateTimePicKDialog(end_occupy_time);
			break;
		case R.id.ll_occupy_querytype_click:
			analyDialog.show();
			break;
		case R.id.ll_occupy_faceType_click:
			if (typeDialog != null) {
				typeDialog.show();
			}
			break;
		case R.id.ll_occupy_area_click:
			if (roadDialog != null) {
				roadDialog.show();
			}
			break;
		case R.id.btn_search:
			String queryType = "";
			String startTime = "";
			String endTime = "";
			String type = "";
			String faceType = "";
			String managearea = "";
			if(this.type == 0){
				queryType = "area";
				faceType = tv_occupy_area.getText().toString();
				if(faceType.length() > 0){
					Type typeface = (Type) tv_occupy_area.getTag();
					faceType = typeface.code;
				}
				type = tv_occupy_faceType.getText().toString();
				if(type.length() > 0){
					Type typeface = (Type) tv_occupy_faceType.getTag();
					type = typeface.code;
				}
			} else if(this.type == 1){
				queryType = "style";
				managearea = tv_occupy_area.getText().toString();
				if(managearea.length() > 0){
					Type typeface = (Type) tv_occupy_area.getTag();
					managearea = typeface.code;
				}
				type = tv_occupy_faceType.getText().toString();
				if(type.length() > 0){
					Type typeface = (Type) tv_occupy_faceType.getTag();
					type = typeface.code;
				}
			} else {
				queryType = "type";
				managearea = tv_occupy_area.getText().toString();
				if(managearea.length() > 0){
					Type typeface = (Type) tv_occupy_area.getTag();
					managearea = typeface.code;
				}
				faceType = tv_occupy_faceType.getText().toString();
				if(faceType.length() > 0){
					Type typeface = (Type) tv_occupy_faceType.getTag();
					faceType = typeface.code;
				}
			}
			Intent intent = new Intent();
			intent.putExtra("queryType", queryType);
			intent.putExtra("type", type);
			intent.putExtra("faceType", faceType);
			intent.putExtra("managearea", managearea);
			intent.putExtra("startTime", startTime);
			intent.putExtra("endTime", endTime);
			setResult(Activity.RESULT_OK, intent);
			finish();
			break;
		}
	}

	private void queryType() {
		types.clear();
		Type type = new Type();
		type.name = "按辖区";
		type.type = "area";
		types.add(type);
		type = new Type();
		type.name = "按路面种类";
		type.type = "style";
		types.add(type);
		type = new Type();
		type.name = "按挖占类型";
		type.type = "type";
		types.add(type);
	}

	@Override
	public void changeState(Type type) {
		if ("按辖区".equals(type.name)) {
			this.type = 0;
			tv_type01.setText("路面种类:");
			tv_type02.setText("挖占类型:");
			tv_occupy_area.setHint("请选择路面种类");
			tv_occupy_faceType.setHint("请选择挖占类型");
			tv_occupy_area.setText("");
			tv_occupy_faceType.setText("");
			RequestList(0);
		} else if ("按路面种类".equals(type.name)) {
			this.type = 1;
			tv_occupy_area.setHint("请选择辖区");
			tv_occupy_faceType.setHint("请选择挖占类型");
			tv_occupy_area.setText("");
			tv_occupy_faceType.setText("");
			tv_type01.setText(Html.fromHtml(getResources().getString(
					R.string.project_area)));
			tv_type02.setText("挖占类型:");
			RequestList(1);
		} else {
			this.type = 2;
			tv_occupy_area.setHint("请选择辖区");
			tv_occupy_faceType.setHint("请选择路面种类");
			tv_occupy_area.setText("");
			tv_occupy_faceType.setText("");
			tv_type01.setText(Html.fromHtml(getResources().getString(
					R.string.project_area)));
			tv_type02.setText("路面种类:");
			RequestList(2);
		}
	}

}
