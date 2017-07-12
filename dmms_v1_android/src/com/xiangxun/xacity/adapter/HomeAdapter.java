package com.xiangxun.xacity.adapter;

import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.xiangxun.xacity.R;
import com.xiangxun.xacity.bean.LoginData.Menu;
import com.xiangxun.xacity.view.XwHomeModeButton;

/**
 * @package: com.xiangxun.xacity.adapter
 * @ClassName: HomeAdapter.java
 * @Description: 首页数据适配器
 * @author: HanGJ
 * @date: 2016-3-24 下午3:40:27
 */
public class HomeAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<Menu> menus;
	private Map<String, Integer> map;

	public HomeAdapter(Context context, List<Menu> menus, Map<String, Integer> map) {
		super();
		this.menus = menus;
		this.map = map;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return menus.size();
	}

	@Override
	public Object getItem(int position) {
		return menus.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.home_grideview_layout, null);
			holder.modeButton = (XwHomeModeButton) convertView.findViewById(R.id.mode_button);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Menu menu = menus.get(position);
		holder.modeButton.setIV(map.get(menu.getName()), menu.getName());
		return convertView;
	}

	private static class ViewHolder {
		XwHomeModeButton modeButton;
	}

}
