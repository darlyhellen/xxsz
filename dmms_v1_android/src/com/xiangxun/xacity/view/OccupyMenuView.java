package com.xiangxun.xacity.view;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiangxun.xacity.R;
import com.xiangxun.xacity.bean.LoginData.Children;
import com.xiangxun.xacity.bean.LoginData.ChildrenRoot;
import com.xiangxun.xacity.ui.RoadManageActivity;
import com.xiangxun.xacity.ui.occupy.OccupyAnalysisActivity;
import com.xiangxun.xacity.ui.occupy.OccupyManageActivity;
import com.xiangxun.xacity.ui.occupy.OccupyStatisticsActivity;
import com.xiangxun.xacity.ui.occupy.OrganizationManageActivity;
import com.xiangxun.xacity.ui.occupy.SearchManageListActivity;
import com.xiangxun.xacity.utils.ShareDataUtils;

/**
 * @package: com.xiangxun.xacity.view
 * @ClassName: OccupyMenuView.java
 * @Description: 道路挖占侧滑菜单
 * @author: HanGJ
 * @date: 2016-2-2 上午11:30:15
 */
public class OccupyMenuView implements OnClickListener {
	private RoadManageActivity mContext;
	private View mMenuView;
	private TextView tv_device_menu01;//
	private TextView tv_device_menu02;//
	private TextView tv_device_menu03;//
	private TextView tv_device_menu04;//
	private LinearLayout ll_device_menu01;//
	private LinearLayout ll_device_menu02;//
	private LinearLayout ll_device_menu03;//
	private LinearLayout ll_device_menu04;//
	/************* 道路挖占抽屉菜单 挖占管理子控件 ****************/
	private TextView roadExcavatePm;// 道路挖掘管理
	private TextView roadSpendPm;// 道路占用管理
	private TextView roadTool;// 占道街具管理
	/************* 道路挖占抽屉菜单 查询管理子控件 ****************/
	private TextView projaceSearch;// 延期项目查询
	private TextView infoSearch;// 勘察信息查询
	private TextView stateSearch;// 更改状态查询
	/************* 道路挖占抽屉菜单 挖占统计子控件 ****************/
	private TextView startStatistics;// 挖占统计
	private TextView returnStatistics;// 退让面积统计
	/************* 道路挖占抽屉菜单 单位管理子控件 ****************/
	private TextView applyLedge;// 申请单位档案
	private TextView constructionLedge;// 施工单位档案
	private DrawerLayout mDrawerLayout;
	private RelativeLayout left_drawer;

	public OccupyMenuView(RoadManageActivity context, DrawerLayout mDrawerLayout, RelativeLayout left_drawer) {
		mContext = context;
		this.mDrawerLayout = mDrawerLayout;
		this.left_drawer = left_drawer;
		initView();
		initData();
		initListener();
	}

	public View getMenuView() {
		if (mMenuView == null) {
			initView();
			initData();
			initListener();
		}
		return mMenuView;
	}

	@SuppressLint("InflateParams")
	private void initView() {
		mMenuView = mContext.getLayoutInflater().inflate(R.layout.sliding_road_menu_layout, null);
		tv_device_menu01 = (TextView) mMenuView.findViewById(R.id.tv_device_menu01);//
		tv_device_menu02 = (TextView) mMenuView.findViewById(R.id.tv_device_menu02);//
		tv_device_menu03 = (TextView) mMenuView.findViewById(R.id.tv_device_menu03);//
		tv_device_menu04 = (TextView) mMenuView.findViewById(R.id.tv_device_menu04);//
		//
		ll_device_menu01 = (LinearLayout) mMenuView.findViewById(R.id.ll_device_menu01);
		ll_device_menu02 = (LinearLayout) mMenuView.findViewById(R.id.ll_device_menu02);
		ll_device_menu03 = (LinearLayout) mMenuView.findViewById(R.id.ll_device_menu03);
		ll_device_menu04 = (LinearLayout) mMenuView.findViewById(R.id.ll_device_menu04);
		/************* 道路挖占抽屉菜单 挖占管理子控件 ****************/
		roadExcavatePm = (TextView) mMenuView.findViewById(R.id.road_excavate_pm);// 道路挖掘管理
		roadSpendPm = (TextView) mMenuView.findViewById(R.id.road_spend_pm);// 道路占用管理
		roadTool = (TextView) mMenuView.findViewById(R.id.road_spend_tool);// 占道街具管理
		/************* 道路挖占抽屉菜单 查询管理子控件 ****************/
		projaceSearch = (TextView) mMenuView.findViewById(R.id.delay_projace_search);// 延期项目查询
		infoSearch = (TextView) mMenuView.findViewById(R.id.survey_info_search);// 勘察信息查询
		stateSearch = (TextView) mMenuView.findViewById(R.id.change_state_search);// 更改状态查询
		/************* 道路挖占抽屉菜单 挖占统计子控件 ****************/
		startStatistics = (TextView) mMenuView.findViewById(R.id.start_area_statistics);// 挖占统计
		returnStatistics = (TextView) mMenuView.findViewById(R.id.return_area_statistics);// 退让面积统计
		/************* 道路挖占抽屉菜单 单位管理子控件 ****************/
		applyLedge = (TextView) mMenuView.findViewById(R.id.apply_organization_ledge);// 申请单位档案
		constructionLedge = (TextView) mMenuView.findViewById(R.id.construction_organization_ledge);// 施工单位档案
	}

	private void initListener() {
		roadExcavatePm.setOnClickListener(this);// 道路挖掘管理
		roadSpendPm.setOnClickListener(this);// 道路占用管理
		roadTool.setOnClickListener(this);// 占道街具管理
		projaceSearch.setOnClickListener(this);// 延期项目查询
		infoSearch.setOnClickListener(this);// 勘察信息查询
		stateSearch.setOnClickListener(this);// 更改状态查询
		startStatistics.setOnClickListener(this);// 挖占统计
		returnStatistics.setOnClickListener(this);// 退让面积统计
		applyLedge.setOnClickListener(this);// 申请单位档案
		constructionLedge.setOnClickListener(this);// 施工单位档案
	}

	/**
	 * 注释该怎么写呢，需求文档没提这个功能,侧滑菜单动态控制(开发之前没说这事，我以为是静态的)
	 * 
	 * @Description: 如果你要改这个地方, 请慎重
	 */
	private void initData() {
		List<ChildrenRoot> childrenRoots = new ArrayList<ChildrenRoot>();
		Object obj = ShareDataUtils.getObject(mContext, "menu_occupy");
		childrenRoots.addAll((List) obj);
		if (childrenRoots.size() > 0) {
			for (int i = 0; i < childrenRoots.size(); i++) {
				ChildrenRoot childrenRoot = childrenRoots.get(i);
				if (childrenRoot != null && "挖占管理".equals(childrenRoot.getName())) {
					tv_device_menu01.setVisibility(View.VISIBLE);
					ll_device_menu01.setVisibility(View.VISIBLE);
					List<Children> childrens = childrenRoot.getChildren();
					if (childrens != null && childrens.size() > 0) {
						for (int j = 0; j < childrens.size(); j++) {
							Children children = childrens.get(j);
							if ("道路挖掘管理".equals(children.getName())) {
								roadExcavatePm.setVisibility(View.VISIBLE);
							} else if ("道路占用管理".equals(children.getName())) {
								roadSpendPm.setVisibility(View.VISIBLE);
							} else if ("占道街具管理".equals(children.getName())) {
								roadTool.setVisibility(View.VISIBLE);
							}
						}
					}
				} else if (childrenRoot != null && "查询管理".equals(childrenRoot.getName())) {
					tv_device_menu02.setVisibility(View.VISIBLE);
					ll_device_menu02.setVisibility(View.VISIBLE);
					List<Children> childrens = childrenRoot.getChildren();
					if (childrens != null && childrens.size() > 0) {
						for (int j = 0; j < childrens.size(); j++) {
							Children children = childrens.get(j);
							if ("延期项目查询".equals(children.getName())) {
								projaceSearch.setVisibility(View.VISIBLE);
							} else if ("勘察信息查询".equals(children.getName())) {
								infoSearch.setVisibility(View.VISIBLE);
							} else if ("更改状态查询".equals(children.getName())) {
								stateSearch.setVisibility(View.VISIBLE);
							}
						}
					}
				} else if (childrenRoot != null && "挖占统计".equals(childrenRoot.getName())) {
					tv_device_menu03.setVisibility(View.VISIBLE);
					ll_device_menu03.setVisibility(View.VISIBLE);
					List<Children> childrens = childrenRoot.getChildren();
					if (childrens != null && childrens.size() > 0) {
						for (int j = 0; j < childrens.size(); j++) {
							Children children = childrens.get(j);
							if ("开挖面积统计".equals(children.getName())) {
								startStatistics.setVisibility(View.VISIBLE);
							} else if ("退让面积统计".equals(children.getName())) {
								returnStatistics.setVisibility(View.VISIBLE);
							}
						}
					}
				} else if (childrenRoot != null && "单位管理".equals(childrenRoot.getName())) {
					tv_device_menu04.setVisibility(View.VISIBLE);
					ll_device_menu04.setVisibility(View.VISIBLE);
					List<Children> childrens = childrenRoot.getChildren();
					if (childrens != null && childrens.size() > 0) {
						for (int j = 0; j < childrens.size(); j++) {
							Children children = childrens.get(j);
							if ("申请单位档案".equals(children.getName())) {
								applyLedge.setVisibility(View.VISIBLE);
							} else if ("施工单位档案".equals(children.getName())) {
								constructionLedge.setVisibility(View.VISIBLE);
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.road_excavate_pm:// 道路挖掘管理
			intent.putExtra("title", "道路挖掘管理");
			intent.putExtra("childAt", 0);
			intent.setClass(mContext, OccupyManageActivity.class);
			break;
		case R.id.road_spend_pm:// 道路占用管理
			intent.putExtra("title", "道路占用管理");
			intent.putExtra("childAt", 1);
			intent.setClass(mContext, OccupyManageActivity.class);
			break;
		case R.id.road_spend_tool:// 占道街具管理
			intent.putExtra("title", "占道街具管理");
			intent.putExtra("childAt", 2);
			intent.setClass(mContext, OccupyManageActivity.class);
			break;
		case R.id.delay_projace_search:// 延期项目查询
			intent.putExtra("title", "延期项目查询");
			intent.putExtra("childAt", 0);
			intent.setClass(mContext, SearchManageListActivity.class);
			break;
		case R.id.survey_info_search:// 勘察信息查询
			intent.putExtra("title", "勘察信息查询");
			intent.putExtra("childAt", 1);
			intent.setClass(mContext, SearchManageListActivity.class);
			break;
		case R.id.change_state_search:// 更改状态查询
			intent.putExtra("title", "更改状态查询");
			intent.putExtra("childAt", 2);
			intent.setClass(mContext, SearchManageListActivity.class);
			break;
		case R.id.start_area_statistics:// 挖占统计
			intent.putExtra("title", "挖占统计");
			intent.putExtra("childAt", 0);
			intent.setClass(mContext, OccupyStatisticsActivity.class);
			break;
		case R.id.return_area_statistics:// 退让面积统计
			intent.putExtra("title", "退让面积统计");
			intent.putExtra("childAt", 1);
			intent.setClass(mContext, OccupyAnalysisActivity.class);
			break;
		case R.id.apply_organization_ledge:// 申请单位档案
			intent.putExtra("title", "申请单位档案");
			intent.putExtra("childAt", 0);
			intent.setClass(mContext, OrganizationManageActivity.class);
			break;
		case R.id.construction_organization_ledge:// 施工单位档案
			intent.putExtra("title", "施工单位档案");
			intent.putExtra("childAt", 1);
			intent.setClass(mContext, OrganizationManageActivity.class);
			break;
		}
		mDrawerLayout.closeDrawer(left_drawer);
		((RoadManageActivity) mContext).startActivity(intent);
	}
}
