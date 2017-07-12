package com.xiangxun.xacity.ui.patrol;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.xiangxun.xacity.R;
import com.xiangxun.xacity.app.BaseActivity;
import com.xiangxun.xacity.bean.ResultBeans.GPSData;
import com.xiangxun.xacity.utils.Logger;
import com.xiangxun.xacity.utils.Tools;
import com.xiangxun.xacity.view.TitleView;

/**
 * @package: com.xiangxun.xacity.ui.patrol
 * @ClassName: PersonnelManageActivity.java
 * @Description: 人员管理页面
 * @author: HanGJ
 * @date: 2016-3-9 下午2:07:24
 */
public class PersonnelManageActivity extends BaseActivity {
	private TitleView titleView;
	private MapView mMapView = null;
	private BaiduMap mBaiduMap;
	private LocationClient mLocClient;
	private MyLocationListenner myListener = new MyLocationListenner();
	private boolean isFirstLoc = true;// 是否首次定位

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.patrol_personnel_manage_layout);
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
		titleView.setTitle("人员管理");
		titleView.setRightBackgroundResource(R.drawable.back);
		mMapView.showZoomControls(false);
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		// 定位初始化
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(3600000);
		option.disableCache(true);
		mLocClient.setLocOption(option);
		mLocClient.start();
	}

	@Override
	public void initListener() {
		titleView.setLeftBackOneListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		titleView.setRightOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivityForResult(new Intent(PersonnelManageActivity.this, PersonnelLocationActivity.class), 200);
			}
		});
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
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// 退出时销毁定位
		mLocClient.stop();
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		mBaiduMap.clear();
		if (data != null) {
			switch (resultCode) {
			case 200:
				GPSData gpsData = (GPSData) data.getSerializableExtra("data");
				String name = data.getStringExtra("name");
				Double latitude = Double.valueOf(TextUtils.isEmpty(gpsData.latitude) ? "0" : gpsData.latitude);
				Double longitude = Double.valueOf(TextUtils.isEmpty(gpsData.longitude) ? "0" : gpsData.longitude);
				LatLng latLng = new LatLng(latitude + 0.00420, longitude + 0.01113);
				BitmapDescriptor descriptor = BitmapDescriptorFactory.fromResource(R.drawable.icon_mark);
				MarkerOptions markerOptions = new MarkerOptions().position(latLng).icon(descriptor).zIndex(9).draggable(true);
				View view = getLayoutInflater().inflate(R.layout.patrol_map_pupup_layout, null);
				TextView tv_user_name = (TextView) view.findViewById(R.id.tv_user_name);
				TextView tv_org_name = (TextView) view.findViewById(R.id.tv_org_name);
				TextView tv_location_time = (TextView) view.findViewById(R.id.tv_location_time);
				TextView tv_location_address = (TextView) view.findViewById(R.id.tv_location_address);
				tv_user_name.setText(Tools.isEmpty(name));
				tv_org_name.setText(Tools.isEmpty(gpsData.orgId));
				tv_location_time.setText(Tools.isEmpty(gpsData.gpsTime));
				tv_location_address.setText(Tools.isEmpty(gpsData.locateAddress));
				Marker marker = (Marker) mBaiduMap.addOverlay(markerOptions);
				LatLng ll = marker.getPosition();
				InfoWindow mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(view), ll, -47, null);
				mBaiduMap.showInfoWindow(mInfoWindow);
				break;

			case 201:
				List<GPSData> datas = (List<GPSData>) data.getSerializableExtra("datas");
				if (datas != null && datas.size() > 0) {
					List<LatLng> points = new ArrayList<LatLng>();
					BitmapDescriptor startDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
					BitmapDescriptor endDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
					for (int i = 0; i < datas.size(); i++) {
						
						GPSData geoData = datas.get(i);
						Double lat = Double.valueOf(TextUtils.isEmpty(geoData.latitude) ? "0" : geoData.latitude);
						Double lgt = Double.valueOf(TextUtils.isEmpty(geoData.longitude) ? "0" : geoData.longitude);
						LatLng lng = new LatLng(lat + 0.00420, lgt + 0.01113);
						points.add(lng);
						if(i == 0){
							MarkerOptions startMarkerOptions = new MarkerOptions().position(lng).icon(startDescriptor).draggable(true);
							mBaiduMap.addOverlay(startMarkerOptions);
						} else if(i == datas.size() - 1){
							MarkerOptions endMarkerOptions = new MarkerOptions().position(lng).icon(endDescriptor).draggable(true);
							mBaiduMap.addOverlay(endMarkerOptions);
						}
					}
					List<Integer> colorValue = new ArrayList<Integer>();
					colorValue.add(0xAAFF0000);
					colorValue.add(0xAA00FF00);
					colorValue.add(0xAA0000FF);
					OverlayOptions ooPolyline = new PolylineOptions().width(15).color(0xAAFF0000).points(points).colorsValues(colorValue);
					mBaiduMap.addOverlay(ooPolyline);
				}
				Logger.d("历史轨迹");
				break;
			}
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
