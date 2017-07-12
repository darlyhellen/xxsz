package com.xiangxun.xacity.adapter;

import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.xiangxun.xacity.R;
import com.xiangxun.xacity.bean.LoginData.ChildrenRoot;
import com.xiangxun.xacity.view.XwHomeModeButton;

/**
 * @package: com.xiangxun.xacity.adapter
 * @ClassName: HomeAdapter.java
 * @Description: 巡视系统首页数据适配器
 * @author: HanGJ
 * @date: 2016-4-14 上午11:20:27
 */
public class PatrolHomeAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<ChildrenRoot> menus;
	private Map<String, Integer> map;
	private int[] colors = { R.drawable.bg_line_bule, R.drawable.bg_line_orange, R.drawable.bg_line_grey, R.drawable.bg_line_lv, R.drawable.bg_line_orange, R.drawable.bg_line_bule };

	public PatrolHomeAdapter(Context context, List<ChildrenRoot> menus, Map<String, Integer> map) {
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
			holder.ll_background = (LinearLayout) convertView.findViewById(R.id.ll_background);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ChildrenRoot menu = menus.get(position);
		if (menus.size() <= map.size()) {
			holder.modeButton.setIV(map.get(menu.getName()), menu.getName());
			holder.ll_background.setBackgroundResource(colors[position]);
		}
		return convertView;
	}

	private static class ViewHolder {
		XwHomeModeButton modeButton;
		LinearLayout ll_background;
	}

}
