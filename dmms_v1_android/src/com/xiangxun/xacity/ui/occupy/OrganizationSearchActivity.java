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

import com.xiangxun.xacity.R;
import com.xiangxun.xacity.app.BaseActivity;
import com.xiangxun.xacity.bean.ResultBeans.Type;
import com.xiangxun.xacity.common.ConstantStatus;
import com.xiangxun.xacity.request.DcNetWorkUtils;
import com.xiangxun.xacity.view.LoadDialog;
import com.xiangxun.xacity.view.MsgToast;
import com.xiangxun.xacity.view.PublishSelectArrayDialog;
import com.xiangxun.xacity.view.PublishSelectTypeDialog;
import com.xiangxun.xacity.view.TitleView;

/**
 * @package: com.xiangxun.xacity.ui.occupy
 * @ClassName: OrganizationSearchActivity.java
 * @Description: 单位管理查询页面
 * @author: HanGJ
 * @date: 2016-1-26 下午4:44:16
 */
public class OrganizationSearchActivity extends BaseActivity implements OnClickListener {
	private TitleView titleView;
	private int childAt;
	private LoadDialog loadDialog;
	private Button btn_search;
	private boolean isFlag = false;
	private EditText et_org_name;
	private TextView tv_occupy_attribute;
	private TextView tv_occupy_level;
	private LinearLayout ll_occupy_attribute_click;
	private LinearLayout ll_occupy_level_click;
	private LinearLayout ll_occupy_attribute;
	private PublishSelectTypeDialog typeDialog;
	private PublishSelectArrayDialog arrayDialog;
	private String[] attribute = { "市政", "非市政" };

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			loadDialog.dismiss();
			switch (msg.what) {
			case ConstantStatus.getOccupyTypeSuccess:
				List<Type> types = (List<Type>) msg.obj;
				if (types != null && types.size() > 0) {
					typeDialog = new PublishSelectTypeDialog(OrganizationSearchActivity.this, types, tv_occupy_level, "请选择信用等级");
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
		setContentView(R.layout.occupy_organization_search_layout);
		initView();
		initData();
		initListener();
	}

	@Override
	public void initView() {
		titleView = (TitleView) findViewById(R.id.comment_title);
		btn_search = (Button) findViewById(R.id.btn_search);
		et_org_name = (EditText) findViewById(R.id.et_org_name);
		tv_occupy_attribute = (TextView) findViewById(R.id.tv_occupy_attribute);
		ll_occupy_attribute_click = (LinearLayout) findViewById(R.id.ll_occupy_attribute_click);
		tv_occupy_level = (TextView) findViewById(R.id.tv_occupy_level);
		ll_occupy_level_click = (LinearLayout) findViewById(R.id.ll_occupy_level_click);
		ll_occupy_attribute = (LinearLayout) findViewById(R.id.ll_occupy_attribute);
	}

	@Override
	public void initData() {
		String title = getIntent().getStringExtra("title");
		childAt = getIntent().getIntExtra("childAt", 0);
		titleView.setTitle(title + "查询");
		loadDialog = new LoadDialog(this);
		loadDialog.setTitle("正在加载数据,请稍后...");
		if (childAt == 1) {
			ll_occupy_attribute.setVisibility(View.GONE);
		} else {
			arrayDialog = new PublishSelectArrayDialog(this, attribute, tv_occupy_attribute, "请选择单位属性");
		}
		RequestList();
	}

	private void RequestList() {
		loadDialog.show();
		DcNetWorkUtils.getOccupyLevelCodeList(this, handler, account, password);
	}

	@Override
	public void initListener() {
		btn_search.setOnClickListener(this);
		ll_occupy_attribute_click.setOnClickListener(this);
		ll_occupy_level_click.setOnClickListener(this);
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
		case R.id.ll_occupy_level_click:
			if (typeDialog != null) {
				typeDialog.show();
			} else {
				RequestList();
			}
			break;
		case R.id.ll_occupy_attribute_click:
			if (arrayDialog != null) {
				arrayDialog.show();
			}
			break;
		case R.id.btn_search:
			String name = et_org_name.getText().toString().trim();
			String unitAttribute = tv_occupy_attribute.getText().toString().trim();
			String levelCode = tv_occupy_level.getText().toString().trim();
			if (name.length() > 0) {
				isFlag = true;
			} else {
				name = "";
			}
			if (unitAttribute.length() > 0) {
				isFlag = true;
				if ("市政".equals(unitAttribute)) {
					unitAttribute = "0";
				} else {
					unitAttribute = "1";
				}
			} else {
				unitAttribute = "";
			}
			if (levelCode.length() > 0) {
				isFlag = true;
				Type types = (Type) tv_occupy_level.getTag();
				levelCode = types.code;
			} else {
				levelCode = "";
			}
			if (isFlag) {
				Intent intent = new Intent();
				intent.putExtra("name", name);
				intent.putExtra("unitAttribute", unitAttribute);
				intent.putExtra("levelCode", levelCode);
				setResult(childAt, intent);
				finish();
			} else {
				MsgToast.geToast().setMsg("搜索內容不能為空~");
			}
			break;
		}
	}

}
