package com.xiangxun.xacity.ui.patrol;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
import com.xiangxun.xacity.adapter.WorkOrderSearchAdapter;
import com.xiangxun.xacity.app.BaseActivity;
import com.xiangxun.xacity.bean.AddViewBean;
import com.xiangxun.xacity.bean.ResponseResultBeans.WorkOrderBean;
import com.xiangxun.xacity.common.ConstantStatus;
import com.xiangxun.xacity.request.DcNetWorkUtils;
import com.xiangxun.xacity.ui.patrol.detail.WorkOrderDetailActivity;
import com.xiangxun.xacity.utils.Logger;
import com.xiangxun.xacity.utils.Tools;
import com.xiangxun.xacity.view.MsgToast;
import com.xiangxun.xacity.view.TabTopView;
import com.xiangxun.xacity.view.TitleView;
import com.xiangxun.xacity.view.xlistView.XListView;
import com.xiangxun.xacity.view.xlistView.XListView.IXListViewListener;

/**
 * @package: com.xiangxun.xacity.ui.patrol
 * @ClassName: WorkOrderListActivity.java
 * @Description: 工单查询页面
 * @author: HanGJ
 * @date: 2016-3-10 上午9:26:11
 */
public class WorkOrderListActivity extends BaseActivity implements IXListViewListener, OnItemClickListener, OnClickListener {
	private static int listState = -1;
	private TitleView titleView;
	private XListView mXListView;
	private ViewFlipper mVF;
	private int currentPage = 1;
	private int PageSize = 10;
	private int totalSize = 0;
	private ViewFlipper viewFlipper_list;
	private TabTopView tabtopView;
	private boolean isRight = false;
	public List<WorkOrderBean> data = new ArrayList<WorkOrderBean>();
	private WorkOrderSearchAdapter adapter;
	/*************** 地图控件 ****************/
	private MapView mMapView = null;
	private BaiduMap mBaiduMap;
	private LocationClient mLocClient;
	private MyLocationListenner myListener = new MyLocationListenner();
	private boolean isFirstLoc = true;// 是否首次定位
	private List<AddViewBean> addViewBeans = new ArrayList<AddViewBean>();
	private InfoWindow mInfoWindow;
	private View view = null;
	/*************** 搜索字段 ****************/
	private String workOrderCode = "";
	private String employeeName = "";
	private String complaintName = "";
	private String workEventType = "";
	private String workEventStatus = "";
	private String workIsBack = "";
	private String workIsNodus = "";
	private String dutyOrgType = "";
	private String dutyOrgId = "";
	private String dealLimit = "";
	private String satisfactionDegree = "";
	private String workEventSource = "";
	private String workAssess = "";
	private String workOrderText = "";
	private String workOrderTimeBegin = "";
	private String workOrderTimeEnd = "";
	private String complaintTel = "";
	private String closeBegin = "";
	private String closeEnd = "";

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {

		@Override
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			stopXListView();
			switch (msg.what) {
			case ConstantStatus.getPatrolSearchListSuccess:
				List<WorkOrderBean> orderBeans = (List<WorkOrderBean>) msg.obj;
				if (orderBeans != null) {
					setWorkOrderData(orderBeans);
				} else {
					if (data.size() <= 0) {
						viewFlipper_list.setDisplayedChild(2);
					}
				}
				break;
			case ConstantStatus.getPatrolSearchListFailed:
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
		setContentView(R.layout.work_order_list_layout);
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
		mBaiduMap = mMapView.getMap();
	}

	@Override
	public void initData() {
		titleView.setTitle("工单查询");
		tabtopView.setTabText("工单列表", "工单地图");
		tabtopView.OnClickLeftTab();
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		// 定位初始化
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(3600000);
		mLocClient.setLocOption(option);
		mLocClient.start();
		listState = ConstantStatus.listStateFirst;
		adapter = new WorkOrderSearchAdapter(this, mXListView);
		RequestList();
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
		DcNetWorkUtils.getWorkOrderList(this, handler, account, password, currentPage + "", PageSize + "", workOrderCode, employeeName, complaintName, workEventType, workEventStatus, workIsBack, dutyOrgType, dutyOrgId, dealLimit, satisfactionDegree, workEventSource, workAssess, workOrderText, workOrderTimeBegin, workOrderTimeEnd, complaintTel, closeBegin, closeEnd, workIsNodus);
	}

	@Override
	public void initListener() {
		mXListView.setPullLoadEnable(true);
		mXListView.setXListViewListener(this);
		mXListView.setOnItemClickListener(this);
		titleView.setRightOnClickListener(this);
		tabtopView.OnClickLeftTabOnClickListener(this);
		tabtopView.OnClickRightTabOnClickListener(this);
		titleView.setLeftBackOneListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
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
								Intent intent = new Intent(WorkOrderListActivity.this, WorkOrderDetailActivity.class);
								intent.putExtra("id", (String) view.getTag());
								startActivityForResult(intent, 404);
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
	}

	protected void setWorkOrderData(List<WorkOrderBean> orderBeans) {
		switch (listState) {
		case ConstantStatus.listStateFirst:
			data.clear();
			data.addAll(orderBeans);
			adapter.setData(data, orderBeans.size());
			mXListView.setAdapter(adapter);
			break;
		case ConstantStatus.listStateRefresh:
			data.clear();
			data.addAll(orderBeans);
			adapter.setData(data, orderBeans.size());
			mXListView.setAdapter(adapter);
			break;
		case ConstantStatus.listStateLoadMore:
			data.addAll(orderBeans);
			adapter.setData(data, orderBeans.size());
			break;
		}
		viewFlipper_list.setDisplayedChild(0);
		setMapData();
		totalSize = orderBeans.size();
		// 没有加载到数据
		if (data.size() == 0) {
			viewFlipper_list.setDisplayedChild(2);
		}
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
		WorkOrderBean bean = (WorkOrderBean) adapter.getItem(position - 1);
		Intent intent = new Intent(this, WorkOrderDetailActivity.class);
		intent.putExtra("id", bean.workOrderId);
		startActivityForResult(intent, 404);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.xw_share:
			startActivityForResult(new Intent(this, WorkOrderSearchActivity.class), 135);
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
				WorkOrderBean orderBean = data.get(i);
				String workEventPointLatlon = orderBean.workEventPointLatlon;
				if (workEventPointLatlon != null && workEventPointLatlon.length() > 0 && !workEventPointLatlon.contains("null")) {
					String[] split = workEventPointLatlon.split(",");
					Double latitude = Double.valueOf(TextUtils.isEmpty(split[1]) ? "0" : split[1]);
					Double longitude = Double.valueOf(TextUtils.isEmpty(split[0]) ? "0" : split[0]);
					LatLng latLng = new LatLng(latitude + 0.00420, longitude + 0.01113);
					BitmapDescriptor descriptor = BitmapDescriptorFactory.fromResource(R.drawable.icon_mark);
					MarkerOptions markerOptions = new MarkerOptions().position(latLng).icon(descriptor).zIndex(9).draggable(true);
					AddViewBean addViewBean = new AddViewBean();
					View view = getLayoutInflater().inflate(R.layout.map_pupup_shop_layout, null);
					TextView tvTitleName = (TextView) view.findViewById(R.id.tv_title_org);
					TextView tvOrgName = (TextView) view.findViewById(R.id.tv_org_name);
					TextView tvTitleType = (TextView) view.findViewById(R.id.tv_title_type);
					TextView tvOccupyType = (TextView) view.findViewById(R.id.tv_occupy_type);
					TextView tvTitleStatus = (TextView) view.findViewById(R.id.tv_title_status);
					TextView tvOccupyStatus = (TextView) view.findViewById(R.id.tv_occupy_status);
					tvTitleName.setText("责任单位: ");
					tvOrgName.setText(Tools.isEmpty(orderBean.dutyOrgName));
					tvTitleType.setText("工单内容: ");
					tvOccupyType.setText(Tools.isEmpty(orderBean.workOrderText));
					tvTitleStatus.setText("工单状态: ");
					tvOccupyStatus.setText(Tools.isEmpty(orderBean.workEventState));
					view.setTag(orderBean.workOrderId);
					addViewBean.view = view;
					Marker marker = (Marker) mBaiduMap.addOverlay(markerOptions);
					addViewBean.marker = marker;
					addViewBeans.add(addViewBean);
				}
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
		// activity 销毁时同时销毁地图控件
		mMapView.onDestroy();
	}

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
		if (data != null && requestCode == 135) {
			workOrderCode = data.getStringExtra("workOrderCode");
			employeeName = data.getStringExtra("employeeName");
			complaintName = data.getStringExtra("complaintName");
			workEventType = data.getStringExtra("workEventType");
			workEventStatus = data.getStringExtra("workEventStatus");
			workIsBack = data.getStringExtra("workIsBack");
			dutyOrgType = data.getStringExtra("dutyOrgType");
			dutyOrgId = data.getStringExtra("dutyOrgId");
			workIsNodus = data.getStringExtra("workIsNodus");
//			dealLimit = data.getStringExtra("dealLimit");
//			satisfactionDegree = data.getStringExtra("satisfactionDegree");
//			workEventSource = data.getStringExtra("workEventSource");
			workAssess = data.getStringExtra("workAssess");
			workOrderText = data.getStringExtra("workOrderText");
			workOrderTimeBegin = data.getStringExtra("workOrderTimeBegin");
			workOrderTimeEnd = data.getStringExtra("workOrderTimeEnd");
			complaintTel = data.getStringExtra("complaintTel");
			closeBegin = data.getStringExtra("closeBegin");
			closeEnd = data.getStringExtra("closeEnd");
			listState = ConstantStatus.listStateFirst;
			this.data.clear();
			currentPage = 1;
			RequestList();
		} else if(requestCode == 404 && resultCode == ConstantStatus.listStateRefresh){
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
