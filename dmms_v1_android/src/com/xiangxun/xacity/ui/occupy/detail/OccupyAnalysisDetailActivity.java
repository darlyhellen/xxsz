package com.xiangxun.xacity.ui.occupy.detail;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.xiangxun.xacity.R;
import com.xiangxun.xacity.app.BaseActivity;
import com.xiangxun.xacity.bean.ResponseResultBeans.OccupyManageBean;
import com.xiangxun.xacity.utils.Tools;
import com.xiangxun.xacity.view.TitleView;

/**
 * @package: com.xiangxun.xacity.ui.occupy.detail
 * @ClassName: OccupyAnalysisDetailActivity.java
 * @Description: 挖占统计详情页面
 * @author: HanGJ
 * @date: 2016-3-4 下午4:11:45
 */
public class OccupyAnalysisDetailActivity extends BaseActivity {
	private TitleView titleView;
	private TextView tv_project_name;
	private TextView tv_project_code;
	private TextView tv_constru_org;
	private TextView tv_start_time;
	private TextView tv_end_time;
	private TextView tv_apply_org;
	private TextView tv_apply_time;
	private TextView tv_apply_end;
	private TextView tv_project_area;
	private TextView tv_project_manage;
	private TextView tv_contact_tel;
	private TextView tv_official_man;
	private TextView tv_road_name;
	private TextView tv_road_type;
	private TextView tv_road_style;
	private TextView tv_occupy_type;
	private TextView tv_occupy_w;
	private TextView tv_occupy_z;
	private TextView tv_project_type;
	private TextView tv_project_remark;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.occupy_analysis_detail_layout);
		initView();
		initData();
		initListener();
	}

	@Override
	public void initView() {
		titleView = (TitleView) findViewById(R.id.comment_title);
		tv_project_name = (TextView) findViewById(R.id.tv_project_name);
		tv_project_code = (TextView) findViewById(R.id.tv_project_code);
		tv_constru_org = (TextView) findViewById(R.id.tv_constru_org);
		tv_start_time = (TextView) findViewById(R.id.tv_start_time);
		tv_end_time = (TextView) findViewById(R.id.tv_end_time);
		tv_apply_org = (TextView) findViewById(R.id.tv_apply_org);
		tv_apply_time = (TextView) findViewById(R.id.tv_apply_time);
		tv_apply_end = (TextView) findViewById(R.id.tv_apply_end);
		tv_project_area = (TextView) findViewById(R.id.tv_project_area);
		tv_project_manage = (TextView) findViewById(R.id.tv_project_manage);
		tv_contact_tel = (TextView) findViewById(R.id.tv_contact_tel);
		tv_official_man = (TextView) findViewById(R.id.tv_official_man);
		tv_road_name = (TextView) findViewById(R.id.tv_road_name);
		tv_road_type = (TextView) findViewById(R.id.tv_road_type);
		tv_road_style = (TextView) findViewById(R.id.tv_road_style);
		tv_occupy_type = (TextView) findViewById(R.id.tv_occupy_type);
		tv_occupy_w = (TextView) findViewById(R.id.tv_occupy_w);
		tv_occupy_z = (TextView) findViewById(R.id.tv_occupy_z);
		tv_project_type = (TextView) findViewById(R.id.tv_project_type);
		tv_project_remark = (TextView) findViewById(R.id.tv_project_remark);
	}

	@Override
	public void initData() {
		titleView.setTitle(getIntent().getStringExtra("title") + "--详情");
		OccupyManageBean manageBean = (OccupyManageBean) getIntent().getSerializableExtra("bean");
		setViewData(manageBean);
	}

	private void setViewData(OccupyManageBean manageBean) {
		tv_project_name.setText(Tools.isEmpty(manageBean.projectName));
		tv_project_code.setText(Tools.isEmpty(manageBean.projectCode));
		tv_constru_org.setText(Tools.isEmpty(manageBean.builderName));
		tv_start_time.setText(Tools.isEmpty(manageBean.starttimeStr));
		tv_end_time.setText(Tools.isEmpty(manageBean.endtimeStr));
		tv_apply_org.setText(Tools.isEmpty(manageBean.applicantName));
		tv_apply_time.setText(Tools.isEmpty(manageBean.applyTime));
		tv_apply_end.setText(Tools.isEmpty(manageBean.approveEndtimeStr));
		tv_project_area.setText(Tools.isEmpty(manageBean.managearea));
		tv_project_manage.setText(Tools.isEmpty(manageBean.manager));
		tv_contact_tel.setText(Tools.isEmpty(manageBean.mobile));
		tv_official_man.setText(Tools.isEmpty(manageBean.principal));
		tv_road_name.setText(Tools.isEmpty(manageBean.roadname));
		tv_road_type.setText(Tools.isEmpty(manageBean.roadType));
		tv_road_style.setText(Tools.isEmpty(manageBean.roadStyle));
		tv_occupy_type.setText(Tools.isEmpty(manageBean.occupytype));
		tv_occupy_w.setText(manageBean.wrecordSum + "");
		tv_occupy_z.setText(manageBean.recordSum + "");
		tv_project_type.setText(Tools.isEmpty(manageBean.typeName));
		tv_project_remark.setText(Tools.isEmpty(""));
	}

	@Override
	public void initListener() {
		titleView.setLeftBackOneListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}

}
