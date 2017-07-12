package com.xiangxun.xacity.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.xiangxun.xacity.R;
import com.xiangxun.xacity.app.BaseFragment;

public class MapViewFragment extends BaseFragment {
	private MapView mMapView = null;
	private BaiduMap mBaiduMap;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.video_map_view_layout, container, false);
		initView(view);
		initListener();
		initData();
		return view;
	}

	@Override
	public void initView(View view) {
		mMapView = (MapView) view.findViewById(R.id.mapview);
		mBaiduMap = mMapView.getMap();
	}

	@Override
	public void initData() {
		//开启交通图   
		mBaiduMap.setTrafficEnabled(true);
		// 设定中心点坐标
		LatLng cenpt = new LatLng(34.276651, 108.936983);
		// 定义地图状态
		MapStatus mMapStatus = new MapStatus.Builder().target(cenpt).zoom(14).build();
		// 定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
		MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
		// 改变地图状态
		mBaiduMap.setMapStatus(mMapStatusUpdate);
	}

	@Override
	public void initListener() {
		// TODO Auto-generated method stub

	}

	@Override
	public void load() {
		// TODO Auto-generated method stub

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

}
