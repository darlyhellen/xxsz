package com.xiangxun.xacity.ui.device;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

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
import com.xiangxun.xacity.app.BaseActivity;
import com.xiangxun.xacity.bean.AddViewBean;
import com.xiangxun.xacity.bean.ResultBeans.DeviceVehicle;
import com.xiangxun.xacity.common.ConstantStatus;
import com.xiangxun.xacity.request.DcNetWorkUtils;
import com.xiangxun.xacity.utils.Logger;
import com.xiangxun.xacity.utils.Tools;
import com.xiangxun.xacity.view.MsgToast;
import com.xiangxun.xacity.view.TitleView;

/**
 * @package: com.xiangxun.xacity.ui.device
 * @ClassName: DeviceVehicleListActivity.java
 * @Description: 车辆定位页面
 * @author: HanGJ
 * @date: 2016-4-26 下午1:58:56
 */
public class DeviceVehicleListActivity extends BaseActivity implements OnClickListener {
	private TitleView titleView;
	public List<DeviceVehicle> data = new ArrayList<DeviceVehicle>();
	/*************** 搜索字段 ****************/
	private String deviceCode = "";// 设备名称
	private String plate = "";// 车牌号码
	/*************** 地图控件 ****************/
	private MapView mMapView = null;
	private BaiduMap mBaiduMap;
	private LocationClient mLocClient;
	private MyLocationListenner myListener = new MyLocationListenner();
	private boolean isFirstLoc = true;// 是否首次定位
	private List<AddViewBean> addViewBeans = new ArrayList<AddViewBean>();
	private InfoWindow mInfoWindow;
	private View view = null;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ConstantStatus.getOperatorLedgerListSuccess:
				@SuppressWarnings("unchecked")
				List<DeviceVehicle> deviceVehicles = (List<DeviceVehicle>) msg.obj;
				if (deviceVehicles != null) {
					setDeviceVehicleData(deviceVehicles);
				} else {
					mBaiduMap.clear();
					addViewBeans.clear();
				}
				break;
			case ConstantStatus.getOperatorLedgerListFailed:
				MsgToast.geToast().setMsg("数据加载失败,请稍后重试!");
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
		setContentView(R.layout.device_vehicle_map_layout);
		initView();
		initData();
		initListener();
	}

	@Override
	public void initView() {
		titleView = (TitleView) findViewById(R.id.comment_title);
		mMapView = (MapView) findViewById(R.id.mapview);
		mBaiduMap = mMapView.getMap();
	}

	@Override
	public void initData() {
		titleView.setTitle("车辆定位");
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		// 定位初始化
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();
		RequestList();
	}

	@Override
	public void initListener() {
		titleView.setRightOnClickListener(this);
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
//								LatLng ll = marker.getPosition();
//								LatLng llNew = new LatLng(ll.latitude + 0.005, ll.longitude + 0.005);
//								marker.setPosition(llNew);
								mBaiduMap.hideInfoWindow();
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.xw_share:
			startActivityForResult(new Intent(this, DeviceVehicleSearchActivity.class), 119);
			break;
		}
	}

	private void RequestList() {
		DcNetWorkUtils.getDeviceVehicleList(this, handler, account, password, plate, deviceCode);
	}

	protected void setDeviceVehicleData(List<DeviceVehicle> manageBeans) {
		data.clear();
		data.addAll(manageBeans);
		setMapData();
	}

	private void setMapData() {
		mBaiduMap.clear();
		addViewBeans.clear();
		if (data.size() > 0) {
			for (int i = 0; i < data.size(); i++) {
				DeviceVehicle deviceVehicle = data.get(i);
				Double latitude = Double.valueOf(TextUtils.isEmpty(deviceVehicle.latitude) ? "0" : deviceVehicle.latitude);
				Double longitude = Double.valueOf(TextUtils.isEmpty(deviceVehicle.longitude) ? "0" : deviceVehicle.longitude);
				LatLng latLng = new LatLng(latitude + 0.00420, longitude + 0.01113);
				BitmapDescriptor descriptor = BitmapDescriptorFactory.fromResource(R.drawable.icon_mark);
				MarkerOptions markerOptions = new MarkerOptions().position(latLng).icon(descriptor).zIndex(9).draggable(true);
				AddViewBean addViewBean = new AddViewBean();
				View view = getLayoutInflater().inflate(R.layout.device_map_pupup_layout, null);
				TextView tv_vehicle_name = (TextView) view.findViewById(R.id.tv_vehicle_name);
				TextView tv_address_detail = (TextView) view.findViewById(R.id.tv_address_detail);
				TextView tv_sim_number = (TextView) view.findViewById(R.id.tv_sim_number);
				TextView tv_vehicle_plate = (TextView) view.findViewById(R.id.tv_vehicle_plate);
				TextView tv_device_code = (TextView) view.findViewById(R.id.tv_device_code);
				TextView tv_vehicle_operator = (TextView) view.findViewById(R.id.tv_vehicle_operator);
				TextView tv_tel_phone = (TextView) view.findViewById(R.id.tv_tel_phone);
				TextView tv_vehicle_org = (TextView) view.findViewById(R.id.tv_vehicle_org);
				TextView tv_location_date = (TextView) view.findViewById(R.id.tv_location_date);
				tv_vehicle_name.setText(Tools.isEmpty(deviceVehicle.deviceName));
				tv_address_detail.setText(Tools.isEmpty(deviceVehicle.location));
				tv_sim_number.setText(Tools.isEmpty(deviceVehicle.code));
				tv_vehicle_plate.setText(Tools.isEmpty(deviceVehicle.vehNoCode));
				tv_device_code.setText(Tools.isEmpty(deviceVehicle.devCode));
				tv_vehicle_operator.setText(Tools.isEmpty(deviceVehicle.operator));
				tv_tel_phone.setText(Tools.isEmpty(deviceVehicle.telephone));
				tv_vehicle_org.setText(Tools.isEmpty(deviceVehicle.belongtoCompanyName));
				tv_location_date.setText(Tools.isEmpty(deviceVehicle.posTime));
				view.setTag(deviceVehicle.vehicleId);
				addViewBean.view = view;
				Marker marker = (Marker) mBaiduMap.addOverlay(markerOptions);
				addViewBean.marker = marker;
				addViewBeans.add(addViewBean);
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null && requestCode == 119) {
			if (resultCode == Activity.RESULT_OK) {
				deviceCode = data.getStringExtra("deviceCode");
				plate = data.getStringExtra("plate");
				this.data.clear();
				mBaiduMap.clear();
				addViewBeans.clear();
				RequestList();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
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

	private void closeLocation() {
		if (mLocClient != null) {
			if (myListener != null) {
				mLocClient.unRegisterLocationListener(myListener);
			}
			mLocClient.stop();
		}
	}

}
