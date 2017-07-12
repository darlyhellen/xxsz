package com.xiangxun.xacity.ui;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

import org.xclcharts.chart.PieData;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.xiangxun.xacity.R;
import com.xiangxun.xacity.adapter.OccupyStatisicsAdapter;
import com.xiangxun.xacity.app.BaseFragmentActivity;
import com.xiangxun.xacity.bean.ResultBeans.OccupyTotal;
import com.xiangxun.xacity.bean.ResultBeans.OccupyType;
import com.xiangxun.xacity.bean.ResultBeans.Types;
import com.xiangxun.xacity.chart.PieChartView;
import com.xiangxun.xacity.common.ConstantStatus;
import com.xiangxun.xacity.request.DcNetWorkUtils;
import com.xiangxun.xacity.view.LoadDialog;
import com.xiangxun.xacity.view.MsgToast;
import com.xiangxun.xacity.view.OccupyMenuView;
import com.xiangxun.xacity.view.TabTopView;
import com.xiangxun.xacity.view.TitleView;

/**
 * @package: com.xiangxun.xacity.ui
 * @ClassName: RoadManageActivity.java
 * @Description: 道路挖占
 * @author: HanGJ
 * @date: 2015-12-30 下午5:19:34
 */
public class RoadManageActivity extends BaseFragmentActivity implements OnClickListener {
	private OccupyMenuView occupyMenuView;
	private TitleView titleView;
	/** DrawerLayout */
	private DrawerLayout mDrawerLayout;
	private RelativeLayout left_drawer;
	/** 菜单打开/关闭状态 */
	private boolean isDirection_left = false;
	private TabTopView tabtopView;
	private LinearLayout ll_occupy_progress;
	private LinearLayout ll_occupy_type;
	private ListView mListView;
	private TextView tv_count;
	private TextView tv_start;
	private TextView tv_weihu;
	private TextView tv_yanqi;
	private ViewFlipper mVF;
	private LoadDialog loadDialog;
	private OccupyStatisicsAdapter adapter;
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			loadDialog.dismiss();
			switch (msg.what) {
			case ConstantStatus.OccupyPercentageSuccess:
				OccupyTotal types = (OccupyTotal) msg.obj;
				if (types != null) {
					List<Types> typesList = types.typestatusInfoList;
					if (typesList != null && typesList.size() > 0) {
						adapter.setData(typesList);
						mListView.setAdapter(adapter);
					}
					tv_count.setText(Html.fromHtml(String.format(getString(R.string.trade_count), String.valueOf(types.totals))));
					int[] arr = types.totalArr;
					if (arr.length == 3) {
						tv_start.setText(Html.fromHtml(String.format(getString(R.string.trade_start), String.valueOf(arr[0]))));
						tv_weihu.setText(Html.fromHtml(String.format(getString(R.string.trade_weihu), String.valueOf(arr[1]))));
						tv_yanqi.setText(Html.fromHtml(String.format(getString(R.string.trade_yanqi), String.valueOf(arr[2]))));
					}
				}
				break;
			case ConstantStatus.OccupyStatusSuccess:
				List<OccupyType> applyStatusList = (List<OccupyType>) msg.obj;
				if (applyStatusList != null && applyStatusList.size() > 0) {
					setOccupyStatusData(applyStatusList);
				}
				break;
			case ConstantStatus.OccupyTypeSuccess:
				List<OccupyType> applyTypeList = (List<OccupyType>) msg.obj;
				if (applyTypeList != null && applyTypeList.size() > 0) {
					setOccupyTypeData(applyTypeList);
				}
				break;
			case ConstantStatus.OccupyStatusFailed:
				MsgToast.geToast().setMsg("施工进度数据加载失败~");
				break;
			case ConstantStatus.OccupyTypeFailed:
				MsgToast.geToast().setMsg("类别统计数据加载失败~");
				break;
			case ConstantStatus.OccupyPercentageFailed:
				MsgToast.geToast().setMsg("项目统计数据加载失败~");
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
		setContentView(R.layout.road_manage_home_layout);
		initView();
		initData();
		initListener();
	}

	/**
	 * 项目进度统计
	 * 
	 * @param applyStatusList
	 */
	protected void setOccupyStatusData(List<OccupyType> applyStatusList) {
		ll_occupy_progress.removeAllViews();
		int[] color = { Color.rgb(180, 205, 230), Color.rgb(75, 132, 1), Color.rgb(180, 205, 230), Color.rgb(148, 159, 181), Color.rgb(253, 180, 90),//
				Color.rgb(193, 37, 82), Color.rgb(255, 102, 0), Color.rgb(245, 199, 0), Color.rgb(106, 150, 31), Color.rgb(179, 100, 53),//
				Color.rgb(192, 255, 140), Color.rgb(255, 247, 140), Color.rgb(255, 208, 140), Color.rgb(140, 234, 255), Color.rgb(255, 140, 157), //
				Color.rgb(207, 248, 246), Color.rgb(148, 212, 212), Color.rgb(136, 180, 187), Color.rgb(118, 174, 175), Color.rgb(42, 109, 130),//
				Color.rgb(217, 80, 138), Color.rgb(254, 149, 7), Color.rgb(254, 247, 120), Color.rgb(106, 167, 134), Color.rgb(53, 194, 209), Color.rgb(254, 247, 120) };
		
		LinkedList<PieData> chartData = new LinkedList<PieData>();
		int max = 0;
		DecimalFormat format = new DecimalFormat(".00");
		for (int i = 0; i < applyStatusList.size(); i++) {
			max += applyStatusList.get(i).occnum;
		}
		for (int i = 0; i < applyStatusList.size(); i++) {
			OccupyType occupyType = applyStatusList.get(i);
			PieData pieData;
//			if (occupyType.occnum == 0) {
//				pieData = new PieData(occupyType.statusname, occupyType.statusname + ": " + occupyType.occnum + "个", 0.1, color[i]);
//			} else {
				float a = (((float) occupyType.occnum) / ((float) max));
				float temp = a * 100;
				float occnum = Float.valueOf(format.format(temp));
				pieData = new PieData(occupyType.statusname, occupyType.statusname + ": " + occupyType.occnum + "个", occnum, color[i]);
//			}
			pieData.setItemLabelRotateAngle(45.f);
			chartData.add(pieData);
		}
		PieChartView pieChartView = new PieChartView(this, chartData);
		LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		ll_occupy_progress.addView(pieChartView, layoutParams);
	}

	/**
	 * 项目类别统计
	 * 
	 * @param applyTypeList
	 */
	protected void setOccupyTypeData(List<OccupyType> applyTypeList) {
		ll_occupy_type.removeAllViews();
		int[] colors = { Color.rgb(64, 89, 128), Color.rgb(149, 165, 124), Color.rgb(217, 184, 162), Color.rgb(191, 134, 134), Color.rgb(179, 48, 80),//
				Color.rgb(193, 37, 82), Color.rgb(255, 102, 0), Color.rgb(245, 199, 0), Color.rgb(106, 150, 31), Color.rgb(179, 100, 53),//
				Color.rgb(192, 255, 140), Color.rgb(255, 247, 140), Color.rgb(255, 208, 140), Color.rgb(140, 234, 255), Color.rgb(255, 140, 157), //
				Color.rgb(207, 248, 246), Color.rgb(148, 212, 212), Color.rgb(136, 180, 187), Color.rgb(118, 174, 175), Color.rgb(42, 109, 130),//
				Color.rgb(217, 80, 138), Color.rgb(254, 149, 7), Color.rgb(254, 247, 120), Color.rgb(106, 167, 134), Color.rgb(53, 194, 209), Color.rgb(254, 247, 120),//
				Color.rgb(217, 184, 162), Color.rgb(255, 102, 0)};
		LinkedList<PieData> chartData = new LinkedList<PieData>();
		int max = 0;
		DecimalFormat format = new DecimalFormat(".00");
		for (int i = 0; i < applyTypeList.size(); i++) {
			max += applyTypeList.get(i).occnum;
		}
		for (int i = 0; i < applyTypeList.size(); i++) {
			OccupyType occupyType = applyTypeList.get(i);
			PieData pieData;
			int color = 0;
			if (applyTypeList.size() > colors.length) {
				if (i > colors.length - 1) {
					color = colors[i - colors.length];
				} else {
					color = colors[i];
				}
			} else {
				color = colors[i];
			}
//			if (occupyType.occnum == 0) {
//				pieData = new PieData(occupyType.typename, occupyType.typename + ": " + occupyType.occnum + "个", 0.1, color);
//			} else {
				float a = (((float) occupyType.occnum) / ((float) max));
				float temp = a * 100;
				float occnum = Float.valueOf(format.format(temp));
				if (i % 2 == 0) {
					pieData = new PieData(occupyType.typename, occupyType.typename + ": " + occupyType.occnum + "个", occnum, color);
				} else {
					pieData = new PieData(occupyType.typename, occupyType.typename + ": " + occupyType.occnum + "个", occnum, color);
				}
//			}
			pieData.setItemLabelRotateAngle(45.f);
			chartData.add(pieData);
		}
		PieChartView pieChartView = new PieChartView(this, chartData);
		LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		ll_occupy_type.addView(pieChartView, layoutParams);
	}

	@Override
	public void initView() {
		titleView = (TitleView) findViewById(R.id.comment_title);
		tabtopView = (TabTopView) findViewById(R.id.tabtopView);
		mVF = (ViewFlipper) findViewById(R.id.viewFlipper);
		mListView = (ListView) findViewById(R.id.xlistview);
		tv_count = (TextView) findViewById(R.id.tv_count);
		tv_start = (TextView) findViewById(R.id.tv_start);
		tv_weihu = (TextView) findViewById(R.id.tv_weihu);
		tv_yanqi = (TextView) findViewById(R.id.tv_yanqi);
		ll_occupy_progress = (LinearLayout) findViewById(R.id.ll_occupy_progress);
		ll_occupy_type = (LinearLayout) findViewById(R.id.ll_occupy_type);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		left_drawer = (RelativeLayout) findViewById(R.id.left_drawer);
		occupyMenuView = new OccupyMenuView(this, mDrawerLayout, left_drawer);
		left_drawer.removeAllViews();
		left_drawer.addView(occupyMenuView.getMenuView());
	}

	@Override
	public void initData() {
		// 设置抽屉打开时，主要内容区被自定义阴影覆盖
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		titleView.setTitle("道路挖占");
		titleView.setRightBackgroundResource(R.drawable.back);
		mDrawerLayout.setDrawerListener(new DrawerLayoutStateListener());
		tabtopView.OnClickLeftTab();
		tabtopView.setTabText("项目统计", "施工进度/类别统计");
		loadDialog = new LoadDialog(this);
		loadDialog.setTitle("正在加载数据,请稍后...");
		adapter = new OccupyStatisicsAdapter(this);
		RequestList();
	}

	private void RequestList() {
		loadDialog.show();
		DcNetWorkUtils.getProjectResultMap(this, handler, account, password);
		DcNetWorkUtils.getProjectOccupyStatus(this, handler, account, password);
		DcNetWorkUtils.getProjectOccupyInfos(this, handler, account, password);
	}

	@Override
	public void initListener() {
		tabtopView.OnClickLeftTabOnClickListener(this);
		tabtopView.OnClickRightTabOnClickListener(this);
		titleView.setLeftBackOneListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		titleView.setRightOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!isDirection_left) { // 左边栏菜单关闭时，打开
					mDrawerLayout.openDrawer(left_drawer);
				} else {// 左边栏菜单打开时，关闭
					mDrawerLayout.closeDrawer(left_drawer);
				}
			}
		});
	}

	/**
	 * DrawerLayout状态变化监听
	 */
	private class DrawerLayoutStateListener extends DrawerLayout.SimpleDrawerListener {
		@Override
		public void onDrawerSlide(View drawerView, float slideOffset) {
		}

		@Override
		public void onDrawerOpened(android.view.View drawerView) {
			titleView.setRightBackgroundResource(R.drawable.titleview_x);
			isDirection_left = true;
		}

		@Override
		public void onDrawerClosed(android.view.View drawerView) {
			titleView.setRightBackgroundResource(R.drawable.back);
			isDirection_left = false;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.toptabview_left_rlyout:
			mVF.setDisplayedChild(0);
			tabtopView.OnClickLeftTab();
			break;
		case R.id.toptabview_right_rlyout:
			mVF.setDisplayedChild(1);
			tabtopView.OnClickRightTab();
			break;
		}
	}
	
	@Override
	protected void onResume() {
		RequestList();
		super.onResume();
	}

}
