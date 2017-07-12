package com.xiangxun.xacity.ui.occupy.detail;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiangxun.xacity.R;
import com.xiangxun.xacity.app.BaseActivity;
import com.xiangxun.xacity.bean.ResponseResultBeans.OccupyArchiveBean;
import com.xiangxun.xacity.bean.ResultBeans.Type;
import com.xiangxun.xacity.common.ConstantStatus;
import com.xiangxun.xacity.request.DcNetWorkUtils;
import com.xiangxun.xacity.utils.Tools;
import com.xiangxun.xacity.view.LoadDialog;
import com.xiangxun.xacity.view.MsgToast;
import com.xiangxun.xacity.view.ProjectChangeDialog;
import com.xiangxun.xacity.view.ProjectChangeDialog.onSelectItemClick;
import com.xiangxun.xacity.view.TitleView;

/**
 * @package: com.xiangxun.xacity.ui.occupy.detail
 * @ClassName: OrganizaManageDetailActivity.java
 * @Description: 单位管理详情页面
 * @author: HanGJ
 * @date: 2016-3-3 上午10:14:52
 */
public class OrganizaManageDetailActivity extends BaseActivity implements OnClickListener, onSelectItemClick {
	private TitleView titleView;
	private int childAt;
	private TextView tv_change_level;
	private TextView tv_org_name;
	private TextView tv_org_code;
	private TextView tv_insert_date;
	private TextView tv_org_official;
	private TextView tv_contact;
	private TextView tv_level_code;
	private TextView tv_level_enable;
	private TextView tv_level_isblack;
	private TextView tv_org_type;
	private LinearLayout ll_org_official;
	private LinearLayout ll_org_type;
	private LinearLayout ll_contact;
	private String id = "";
	private String level = "";
	private LoadDialog loadDialog;
	private List<Type> types = new ArrayList<Type>();
	private ProjectChangeDialog changeDialog;
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			loadDialog.dismiss();
			switch (msg.what) {
			case ConstantStatus.getOccupyTypeSuccess:
				List<Type> levelTypes = (List<Type>) msg.obj;
				if (levelTypes != null && levelTypes.size() > 0) {
					types.addAll(levelTypes);
					changeDialog = new ProjectChangeDialog(OrganizaManageDetailActivity.this, types, "更改状态", "状态");
					changeDialog.setSelectItemClick(OrganizaManageDetailActivity.this);
				}
				break;
			case ConstantStatus.VerifyResultSuccess:
				MsgToast.geToast().setMsg("信用状态更改成功");
				break;
			case ConstantStatus.VerifyResultFailed:
				MsgToast.geToast().setMsg("信用状态更改失败,请稍后重试");
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
		setContentView(R.layout.organiza_manage_detail_layout);
		initView();
		initData();
		initListener();
	}

	@Override
	public void initView() {
		titleView = (TitleView) findViewById(R.id.comment_title);
		tv_change_level = (TextView) findViewById(R.id.tv_change_level);
		tv_org_name = (TextView) findViewById(R.id.tv_org_name);
		tv_org_code = (TextView) findViewById(R.id.tv_org_code);
		tv_insert_date = (TextView) findViewById(R.id.tv_insert_date);
		tv_org_official = (TextView) findViewById(R.id.tv_org_official);
		tv_contact = (TextView) findViewById(R.id.tv_contact);
		tv_level_code = (TextView) findViewById(R.id.tv_level_code);
		tv_level_enable = (TextView) findViewById(R.id.tv_level_enable);
		tv_level_isblack = (TextView) findViewById(R.id.tv_level_isblack);
		tv_org_type = (TextView) findViewById(R.id.tv_org_type);
		ll_org_official = (LinearLayout) findViewById(R.id.ll_org_official);
		ll_org_type = (LinearLayout) findViewById(R.id.ll_org_type);
		ll_contact = (LinearLayout) findViewById(R.id.ll_contact);
	}

	@Override
	public void initData() {
		String title = getIntent().getStringExtra("title");
		childAt = getIntent().getIntExtra("childAt", 0);
		titleView.setTitle(title + "--详情");
		loadDialog = new LoadDialog(this);
		loadDialog.setTitle("正在加载数据,请稍后...");
		RequestList();
		OccupyArchiveBean bean = (OccupyArchiveBean) getIntent().getSerializableExtra("bean");
		if (bean != null) {
			id = bean.id;
			if (childAt == 0) {
				ll_org_type.setVisibility(View.VISIBLE);
				tv_org_name.setText(Tools.isEmpty(bean.name));
				tv_org_code.setText(Tools.isEmpty(bean.id));
				tv_org_type.setText(Tools.isEmpty(bean.unitAttribute));
				tv_insert_date.setText(Tools.isEmpty(bean.insertTime));
				tv_level_code.setText(Tools.isEmpty(bean.levelCode));
				tv_level_enable.setText("0".equals(Tools.isEmpty(bean.disabled)) ? "否" : "是");
				tv_level_isblack.setText("0".equals(Tools.isEmpty(bean.isblack)) ? "否" : "是");
			} else {
				id = bean.id;
				tv_org_name.setText(Tools.isEmpty(bean.name));
				tv_org_code.setText(Tools.isEmpty(bean.id));
				tv_insert_date.setText(Tools.isEmpty(bean.insertTime));
				tv_org_official.setText(Tools.isEmpty(bean.leadingOfficial));
				tv_contact.setText(Tools.isEmpty(bean.leadingOfficialMobile));
				tv_level_code.setText(Tools.isEmpty(bean.levelCode));
				tv_level_enable.setText("0".equals(Tools.isEmpty(bean.disabled)) ? "否" : "是");
				tv_level_isblack.setText("0".equals(Tools.isEmpty(bean.isblack)) ? "否" : "是");
				ll_org_official.setVisibility(View.VISIBLE);
				ll_contact.setVisibility(View.VISIBLE);
			}
		}
	}

	@Override
	public void initListener() {
		tv_change_level.setOnClickListener(this);
		titleView.setLeftBackOneListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}

	private void RequestList() {
		loadDialog.show();
		DcNetWorkUtils.getOccupyLevelCodeList(this, handler, account, password);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_change_level:
			if (types.size() > 0 && changeDialog != null) {
				changeDialog.show();
			} else {
				DcNetWorkUtils.getOccupyLevelCodeList(this, handler, account, password);
			}
			break;
		}
	}

	@Override
	public void changeState(Type type) {
		loadDialog.setTitle("信用更改状态提交中,请稍后...");
		loadDialog.show();
		String apiType = "";
		if (childAt == 0) {
			apiType = "applicant";
		} else {
			apiType = "builder";
		}
		DcNetWorkUtils.getOccupyLevelChange(this, handler, apiType, account, password, id, type.code);
	}

}
