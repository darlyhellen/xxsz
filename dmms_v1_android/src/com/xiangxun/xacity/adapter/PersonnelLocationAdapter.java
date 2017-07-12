package com.xiangxun.xacity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiangxun.xacity.R;

public class PersonnelLocationAdapter extends BaseAdapter {
	private LayoutInflater inflater;

	public PersonnelLocationAdapter(Context context) {
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return 10;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.expandable_list_item, null);
			holder = new ViewHolder();
			holder.mTvExState = (TextView) convertView.findViewById(R.id.tv_ex_state);
			holder.tv_child_name = (TextView) convertView.findViewById(R.id.tv_child_name);
			holder.toggle_button = (ImageView) convertView.findViewById(R.id.toggle_button);
			holder.child_item = (RelativeLayout) convertView.findViewById(R.id.child_item);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.mTvExState.setText("西安市市政设施管理局");
		holder.tv_child_name.setText("西安市市政设施管理局" + position);
		holder.tv_child_name.setTag(R.id.item_id);
		holder.toggle_button.setTag(R.id.item_id);
		holder.child_item.setTag(R.id.item_id);
		return convertView;
	}

	private class ViewHolder {
		TextView mTvExState;
		TextView tv_child_name;
		ImageView toggle_button;
		RelativeLayout child_item;
	}

}
