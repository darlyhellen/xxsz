package com.xiangxun.xacity.ui.occupy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.xiangxun.xacity.R;
import com.xiangxun.xacity.adapter.OccupyManageAdapter;
import com.xiangxun.xacity.app.BaseActivity;
import com.xiangxun.xacity.bean.AddViewBean;
import com.xiangxun.xacity.bean.ResponseResultBeans.OccupyManageBean;
import com.xiangxun.xacity.common.ConstantStatus;
import com.xiangxun.xacity.request.DcNetWorkUtils;
import com.xiangxun.xacity.ui.occupy.detail.OccupyManageDetailActivity;
import com.xiangxun.xacity.utils.Logger;
import com.xiangxun.xacity.utils.Tools;
import com.xiangxun.xacity.view.MsgToast;
import com.xiangxun.xacity.view.TabTopView;
import com.xiangxun.xacity.view.TitleView;
import com.xiangxun.xacity.view.xlistView.XListView;
import com.xiangxun.xacity.view.xlistView.XListView.IXListViewListener;

/**
 * @package: com.xiangxun.xacity.ui.occupy
 * @ClassName: OccupyManageActivity.java
 * @Description: 挖占管理页面
 * @author: HanGJ
 * @date: 2016-1-26 下午2:44:52
 */
public class OccupyManageActivity extends BaseActivity implements OnClickListener, IXListViewListener, OnItemClickListener {
	private static int listState = -1;
	private TitleView titleView;
	private XListView mXListView;
	private ViewFlipper mVF;
	private String title;
	private int childAt;
	private int currentPage = 1;
	private int PageSize = 10;
	private int totalSize = 0;
	private ViewFlipper viewFlipper_list;
	private TabTopView tabtopView;
	private ImageView mIV_filter;
	private PopupWindow popupWindow;
	private boolean isRight = false;
	public List<OccupyManageBean> data = new ArrayList<OccupyManageBean>();
	private OccupyManageAdapter adapter;
	/*************** 地图控件 ****************/
	private MapView mMapView = null;
	private BaiduMap mBaiduMap;
	private LocationClient mLocClient;
	private MyLocationListenner myListener = new MyLocationListenner();
	private boolean isFirstLoc = true;// 是否首次定位
	private Map<String, Integer> map = new HashMap<String, Integer>();
	private List<AddViewBean> addViewBeans = new ArrayList<AddViewBean>();
	private InfoWindow mInfoWindow;
	/*************** 弹出框菜单控件 ****************/
	private TextView tv_occupy_verify;
	private TextView tv_occupy_builder;
	private TextView tv_occupy_work;
	private TextView tv_occupy_finish;
	private TextView tv_occupy_overdeadtime;
	private TextView tv_occupy_deadtime;
	/*************** 搜索字段 ****************/
	private String projectName = "";
	private String applicantId = "";
	private String builderId = "";
	private String starttime = "";
	private String endtime = "";
	private String roadname = "";
	private String status = "";
	private String principal = "";
	private String managearea = "";
	private String type = "";

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			stopXListView();
			switch (msg.what) {
			case ConstantStatus.getOccupyListSuccess:
				@SuppressWarnings("unchecked")
				List<OccupyManageBean> manageBeans = (List<OccupyManageBean>) msg.obj;
				if (manageBeans != null) {
					setOccupyManageData(manageBeans);
				} else {
					if (data.size() <= 0) {
						viewFlipper_list.setDisplayedChild(2);
					}
				}
				break;
			case ConstantStatus.getOccupyListFailed:
				if (data.size() <= 0) {
					viewFlipper_list.setDisplayedChild(2);
				}
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
		setContentView(R.layout.occupy_manage_list_layout);
		initView();
		initData();
		initListener();
	}

	@Override
	public void initView() {
		titleView = (TitleView) findViewById(R.id.comment_title);
		tabtopView = (TabTopView) findViewById(R.id.tabtopView);
		mXListView = (XListView) findViewById(R.id.xlistview);
		mVF = (ViewFlipper) findViewById(R.id.viewFlipper);
		viewFlipper_list = (ViewFlipper) findViewById(R.id.viewFlipper_list);
		mMapView = (MapView) findViewById(R.id.mapview);
		mIV_filter = (ImageView) findViewById(R.id.filter);
		mBaiduMap = mMapView.getMap();
	}

	@Override
	public void initData() {
		title = getIntent().getStringExtra("title");
		childAt = getIntent().getIntExtra("childAt", 0);
		titleView.setTitle(title);
		tabtopView.setTabText("列表", "地图");
		tabtopView.OnClickLeftTab();
		mMapView.showZoomControls(false);
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		// 定位初始化
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
//		option.setLocationMode(LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
		option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
		option.setScanSpan(3600000);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
		option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
		option.setOpenGps(true);// 可选，默认false,设置是否使用gps
//		option.setLocationNotify(true);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
//		option.setIsNeedLocationDescribe(true);// 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
//		option.setIsNeedLocationPoiList(true);// 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
		mLocClient.setLocOption(option);
		mLocClient.start();
		map.put("0", R.drawable.ico0);
		map.put("1", R.drawable.ico1);
		map.put("2", R.drawable.ico2);
		map.put("3", R.drawable.ico3);
		map.put("4", R.drawable.ico4);
		map.put("5", R.drawable.ico5);
		map.put("6", R.drawable.ico4);
		map.put("7", R.drawable.ico4);
		map.put("8", R.drawable.ico4);
		adapter = new OccupyManageAdapter(this, childAt, mXListView);
		listState = ConstantStatus.listStateFirst;
		initPopupWindowView();
		RequestList();
	}

	private void initPopupWindowView() {
		popupWindow = new PopupWindow(this);
		final View view = getLayoutInflater().inflate(R.layout.occupy_select_pupop_layout, null);
		tv_occupy_verify = (TextView) view.findViewById(R.id.tv_occupy_verify);
		tv_occupy_builder = (TextView) view.findViewById(R.id.tv_occupy_builder);
		tv_occupy_work = (TextView) view.findViewById(R.id.tv_occupy_work);
		tv_occupy_finish = (TextView) view.findViewById(R.id.tv_occupy_finish);
		tv_occupy_overdeadtime = (TextView) view.findViewById(R.id.tv_occupy_overdeadtime);
		tv_occupy_deadtime = (TextView) view.findViewById(R.id.tv_occupy_deadtime);
		popupWindow.setContentView(view);
		popupWindow.setWidth(LayoutParams.WRAP_CONTENT);
		popupWindow.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		popupWindow.setFocusable(true);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0000000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		popupWindow.setBackgroundDrawable(dw);
		// mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		view.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {

				int height = view.findViewById(R.id.popup_window_view).getTop();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (y < height) {
						popupWindow.dismiss();
					}
				}
				return true;
			}
		});
	}

	private void RequestList() {
		switch (listState) {
		case ConstantStatus.listStateFirst:
			if (!isRight) {
				mVF.setDisplayedChild(0);
				viewFlipper_list.setDisplayedChild(1);
			}
			break;
		case ConstantStatus.listStateRefresh:
			viewFlipper_list.setDisplayedChild(0);
			break;
		case ConstantStatus.listStateLoadMore:
			viewFlipper_list.setDisplayedChild(0);
			break;
		}
		if (childAt == 0) {// 道路挖掘管理(occupytype=1)网络请求
			DcNetWorkUtils.getOccupyRoadPmList(this, handler, "1", account, password, currentPage + "", PageSize + "", projectName, applicantId, builderId, starttime, endtime, roadname, status, principal, managearea, type);
		} else if (childAt == 1) {// 道路占用管理(occupytype=2)网络请求 =
			DcNetWorkUtils.getOccupyRoadPmList(this, handler, "2", account, password, currentPage + "", PageSize + "", projectName, applicantId, builderId, starttime, endtime, roadname, status, principal, managearea, type);
		} else if (childAt == 2) {// 占道街具管理(occupytype=3)网络请求
			DcNetWorkUtils.getOccupyRoadPmList(this, handler, "3", account, password, currentPage + "", PageSize + "", projectName, applicantId, builderId, starttime, endtime, roadname, status, principal, managearea, type);
		}
	}

	private View view = null;

	@Override
	public void initListener() {
		mXListView.setPullLoadEnable(true);
		mXListView.setXListViewListener(this);
		mXListView.setOnItemClickListener(this);
		titleView.setRightOnClickListener(this);
		tabtopView.OnClickLeftTabOnClickListener(this);
		tabtopView.OnClickRightTabOnClickListener(this);
		mIV_filter.setOnClickListener(this);
		tv_occupy_verify.setOnClickListener(this);
		tv_occupy_builder.setOnClickListener(this);
		tv_occupy_work.setOnClickListener(this);
		tv_occupy_finish.setOnClickListener(this);
		tv_occupy_overdeadtime.setOnClickListener(this);
		tv_occupy_deadtime.setOnClickListener(this);
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(final Marker marker) {
				OnInfoWindowClickListener listener = null;
				for (int i = 0; i < addViewBeans.size(); i++) {
					AddViewBean addViewBean = addViewBeans.get(i);
					if (marker == addViewBean.marker) {
						view = addViewBean.view;
						listener = new OnInfoWindowClickListener() {
							public void onInfoWindowClick() {
								LatLng ll = marker.getPosition();
								LatLng llNew = new LatLng(ll.latitude + 0.005, ll.longitude + 0.005);
								marker.setPosition(llNew);
								mBaiduMap.hideInfoWindow();
								Intent intent = new Intent(OccupyManageActivity.this, OccupyManageDetailActivity.class);
								intent.putExtra("id", (String) view.getTag());
								intent.putExtra("childAt", childAt);
								intent.putExtra("title", title);
								startActivity(intent);
							}
						};
					}
				}
				LatLng ll = marker.getPosition();
				mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(view), ll, -47, listener);
				mBaiduMap.showInfoWindow(mInfoWindow);
				return true;
			}
		});
		titleView.setLeftBackOneListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}

	protected void setOccupyManageData(List<OccupyManageBean> manageBeans) {
		switch (listState) {
		case ConstantStatus.listStateFirst:
			data.clear();
			data.addAll(manageBeans);
			adapter.setData(data, manageBeans.size());
			mXListView.setAdapter(adapter);
			break;
		case ConstantStatus.listStateRefresh:
			data.clear();
			data.addAll(manageBeans);
			adapter.setData(data, manageBeans.size());
			mXListView.setAdapter(adapter);
			break;
		case ConstantStatus.listStateLoadMore:
			data.addAll(manageBeans);
			adapter.setData(data, manageBeans.size());
			break;
		}
		viewFlipper_list.setDisplayedChild(0);
		setMapData();
		totalSize = manageBeans.size();
		// 没有加载到数据
		if (data.size() == 0) {
			viewFlipper_list.setDisplayedChild(2);
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.xw_share:
			Intent intent = new Intent(this, OccupyManageSearchActivity.class);
			intent.putExtra("title", title);
			intent.putExtra("childAt", childAt);
			startActivityForResult(intent, 131);
			break;
		case R.id.toptabview_left_rlyout:
			isRight = false;
			mVF.setDisplayedChild(0);
			tabtopView.OnClickLeftTab();
			break;
		case R.id.toptabview_right_rlyout:
			isRight = true;
			mVF.setDisplayedChild(1);
			tabtopView.OnClickRightTab();
			break;
		case R.id.filter:
			if (popupWindow != null) {
				titleView.getBottom();
				int y = titleView.getBottom();
				popupWindow.showAtLocation(titleView, Gravity.BOTTOM | Gravity.RIGHT, 10, y);
			}
			break;
		case R.id.tv_occupy_verify:
			popupWindow.dismiss();
			status = "0";
			currentPage = 1;
			listState = ConstantStatus.listStateFirst;
			RequestList();
			break;
		case R.id.tv_occupy_builder:
			status = "1";
			popupWindow.dismiss();
			currentPage = 1;
			listState = ConstantStatus.listStateFirst;
			RequestList();
			break;
		case R.id.tv_occupy_work:
			status = "2";
			popupWindow.dismiss();
			currentPage = 1;
			listState = ConstantStatus.listStateFirst;
			RequestList();
			break;
		case R.id.tv_occupy_finish:
			status = "3";
			popupWindow.dismiss();
			currentPage = 1;
			listState = ConstantStatus.listStateFirst;
			RequestList();
			break;
		case R.id.tv_occupy_overdeadtime:
			status = "4";
			popupWindow.dismiss();
			currentPage = 1;
			listState = ConstantStatus.listStateFirst;
			RequestList();
			break;
		case R.id.tv_occupy_deadtime:
			status = "5";
			popupWindow.dismiss();
			currentPage = 1;
			listState = ConstantStatus.listStateFirst;
			RequestList();
			break;
		}
	}

	/**
	 * MapView添加图层
	 */
	private void setMapData() {
		mBaiduMap.clear();
		addViewBeans.clear();
		if (data.size() > 0) {
			for (int i = 0; i < data.size(); i++) {
				OccupyManageBean occupyManageBean = data.get(i);
				Double latitude = Double.valueOf(TextUtils.isEmpty(occupyManageBean.latitude) ? "0" : occupyManageBean.latitude);
				Double longitude = Double.valueOf(TextUtils.isEmpty(occupyManageBean.longitude) ? "0" : occupyManageBean.longitude);
				LatLng latLng = new LatLng(latitude + 0.00420, longitude + 0.01113);
				BitmapDescriptor descriptor = BitmapDescriptorFactory.fromResource(map.get(occupyManageBean.status));
				MarkerOptions markerOptions = new MarkerOptions().position(latLng).icon(descriptor).zIndex(9).draggable(true);
				AddViewBean addViewBean = new AddViewBean();
				View view = getLayoutInflater().inflate(R.layout.map_pupup_shop_layout, null);
				TextView tvTitleName = (TextView) view.findViewById(R.id.tv_title_org);
				TextView tvOrgName = (TextView) view.findViewById(R.id.tv_org_name);
				TextView tvTitleType = (TextView) view.findViewById(R.id.tv_title_type);
				TextView tvOccupyType = (TextView) view.findViewById(R.id.tv_occupy_type);
				TextView tvTitleStatus = (TextView) view.findViewById(R.id.tv_title_status);
				TextView tvOccupyStatus = (TextView) view.findViewById(R.id.tv_occupy_status);
				if (childAt == 0) {
					tvTitleName.setText("建设单位: ");
					tvOrgName.setText(Tools.isEmpty(occupyManageBean.applicantName));
					tvTitleType.setText("占用类别: ");
					tvOccupyType.setText(Tools.isEmpty(occupyManageBean.typeName));
					tvTitleStatus.setText("工单状态: ");
					tvOccupyStatus.setText(Tools.isEmpty(occupyManageBean.statusHtml));
				} else if (childAt == 1) {
					tvTitleName.setText("申请单位: ");
					tvOrgName.setText(Tools.isEmpty(occupyManageBean.applicantName));
					tvTitleType.setText("占用类别: ");
					tvOccupyType.setText(Tools.isEmpty(occupyManageBean.typeName));
					tvTitleStatus.setText("工单状态: ");
					tvOccupyStatus.setText(Tools.isEmpty(occupyManageBean.statusHtml));
				} else {
					tvTitleName.setText("申请单位: ");
					tvOrgName.setText(Tools.isEmpty(occupyManageBean.applicantName));
					tvTitleType.setText("占用类别: ");
					tvOccupyType.setText(Tools.isEmpty(occupyManageBean.typeName));
					tvTitleStatus.setText("工单状态: ");
					tvOccupyStatus.setText(Tools.isEmpty(occupyManageBean.statusHtml));
				}
				view.setTag(occupyManageBean.id);
				addViewBean.view = view;
				Marker marker = (Marker) mBaiduMap.addOverlay(markerOptions);
				addViewBean.marker = marker;
				addViewBeans.add(addViewBean);
			}
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		mMapView.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		// activity 恢复时同时恢复地图控件
		mMapView.onResume();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mLocClient.stop();
//		mLocClient.unRegisterLocationListener(myListener);
		// activity 销毁时同时销毁地图控件
		mMapView.onDestroy();
		mMapView = null;
	}

	@Override
	public void onRefresh(View v) {
		currentPage = 1;
		listState = ConstantStatus.listStateRefresh;
		RequestList();
	}

	@Override
	public void onLoadMore(View v) {
		if (totalSize < PageSize) {
			MsgToast.geToast().setMsg("已经是最后一页");
			mXListView.removeFooterView(mXListView.mFooterView);
		} else {
			currentPage++;
			listState = ConstantStatus.listStateLoadMore;
			RequestList();
		}
	}

	// xLisView 停止
	private void stopXListView() {
		mXListView.stopRefresh();
		mXListView.stopLoadMore();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		OccupyManageBean bean = (OccupyManageBean) adapter.getItem(position - 1);
		Intent intent = new Intent(this, OccupyManageDetailActivity.class);
		intent.putExtra("id", bean.id);
		intent.putExtra("childAt", childAt);
		intent.putExtra("title", title);
		startActivity(intent);
	}

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null) {
				return;
			}
			if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
				Logger.i("gps定位成功");
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
				Logger.i("网络定位成功");
			} else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
				Logger.i("离线定位成功，离线定位结果也是有效的");
			} else if (location.getLocType() == BDLocation.TypeServerError) {
				Logger.i("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
				return;
			} else if (location.getLocType() == BDLocation.TypeNetWorkException) {
				Logger.i("网络不同导致定位失败，请检查网络是否通畅");
				return;
			} else if (location.getLocType() == BDLocation.TypeCriteriaException) {
				Logger.i("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
				return;
			}
			closeLocation();
			MyLocationData locData = new MyLocationData.Builder().accuracy(location.getRadius())
			// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude()).longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			if (isFirstLoc) {
				isFirstLoc = false;
				double latitude = location.getLatitude();
				LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
				MapStatus mMapStatus = new MapStatus.Builder().target(ll).zoom(14).build();
				MapStatusUpdate u = MapStatusUpdateFactory.newMapStatus(mMapStatus);
				mBaiduMap.animateMapStatus(u);
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null && requestCode == 131) {
			switch (resultCode) {
			case 0:
				projectName = data.getStringExtra("projectName");
				roadname = data.getStringExtra("roadname");
				applicantId = data.getStringExtra("applicantId");
				builderId = data.getStringExtra("builderId");
				status = data.getStringExtra("status");
				managearea = data.getStringExtra("managearea");
				type = data.getStringExtra("type");
				starttime = data.getStringExtra("starttime");
				endtime = data.getStringExtra("endtime");
				break;
			case 1:
				projectName = data.getStringExtra("projectName");
				roadname = data.getStringExtra("roadname");
				applicantId = data.getStringExtra("applicantId");
				status = data.getStringExtra("status");
				managearea = data.getStringExtra("managearea");
				type = data.getStringExtra("type");
				starttime = data.getStringExtra("starttime");
				endtime = data.getStringExtra("endtime");
				break;
			case 2:
				roadname = data.getStringExtra("roadname");
				applicantId = data.getStringExtra("applicantId");
				status = data.getStringExtra("status");
				principal = data.getStringExtra("principal");
				type = data.getStringExtra("type");
				starttime = data.getStringExtra("starttime");
				endtime = data.getStringExtra("endtime");
				break;
			}
			this.data.clear();
			listState = ConstantStatus.listStateFirst;
			currentPage = 1;
			RequestList();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private void closeLocation() {
		if (mLocClient != null) {
			if (myListener != null) {
				mLocClient.unRegisterLocationListener(myListener);
			}
			mLocClient.stop();
		}
	}

}
