package com.xiangxun.xacity.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.xiangxun.xacity.R;
import com.xiangxun.xacity.adapter.PatrolHomeAdapter;
import com.xiangxun.xacity.app.BaseActivity;
import com.xiangxun.xacity.bean.LoginData.ChildrenRoot;
import com.xiangxun.xacity.ui.patrol.ContactActivity;
import com.xiangxun.xacity.ui.patrol.PersonnelManageActivity;
import com.xiangxun.xacity.ui.patrol.WorkOrderListActivity;
import com.xiangxun.xacity.ui.patrol.WorkOrderReportActivity;
import com.xiangxun.xacity.utils.ShareDataUtils;
import com.xiangxun.xacity.view.TitleView;

/**
 * @package: com.xiangxun.xacity.ui
 * @ClassName: PatrolManageActivity.java
 * @Description: 巡视系统
 * @author: HanGJ
 * @date: 2015-12-30 下午5:19:03
 */
public class PatrolManageActivity extends BaseActivity implements OnItemClickListener {
	private TitleView titleView;
	private GridView gv_home;
	private Map<String, Integer> map = new HashMap<String, Integer>();
	private PatrolHomeAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.patrol_manage_home_layout);
		initView();
		initData();
		initListener();
	}

	@Override
	public void initView() {
		titleView = (TitleView) findViewById(R.id.comment_title);
		gv_home = (GridView) findViewById(R.id.gv_home);
	}

	@Override
	public void initData() {
		titleView.setTitle("巡视系统");
		map.put("通讯录", R.drawable.contact_phone);
		map.put("人员管理", R.drawable.man_user_manage);
		map.put("工单查询", R.drawable.work_order_search);
		map.put("工单上报", R.drawable.work_order_repor);
		map.put("工单管理", R.drawable.work_order_search);
		@SuppressWarnings("unchecked")
		List<ChildrenRoot> menus = (List<ChildrenRoot>) ShareDataUtils.getObject(this, "menu_patrol");
		if (menus != null && menus.size() > 0) {
			adapter = new PatrolHomeAdapter(this, menus, map);
			gv_home.setAdapter(adapter);
		}
	}

	@Override
	public void initListener() {
		gv_home.setOnItemClickListener(this);
		titleView.setLeftBackOneListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		ChildrenRoot menu = (ChildrenRoot) adapter.getItem(position);
		if ("通讯录".equals(menu.getName())) {
			startActivity(new Intent(this, ContactActivity.class));
		} else if ("人员管理".equals(menu.getName())) {
			startActivity(new Intent(this, PersonnelManageActivity.class));
		} else if ("工单查询".equals(menu.getName())) {
			startActivity(new Intent(this, WorkOrderListActivity.class));
		} else if ("工单上报".equals(menu.getName())) {
			startActivity(new Intent(this, WorkOrderReportActivity.class));
		} else if ("工单管理".equals(menu.getName())) {
			startActivity(new Intent(this, WorkOrderListActivity.class));
		}
	}

}
