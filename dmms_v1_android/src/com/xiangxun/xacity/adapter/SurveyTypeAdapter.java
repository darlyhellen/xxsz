package com.xiangxun.xacity.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.xiangxun.xacity.R;
import com.xiangxun.xacity.bean.ResultBeans.Type;

@SuppressLint("UseSparseArrays")
public class SurveyTypeAdapter extends BaseAdapter {
	private List<Type> types = null;
	private List<String> codes = null;
	private LayoutInflater mInflater = null;
	// 用来控制CheckBox的选中状况
	private static HashMap<Integer, Boolean> isSelected;

	public SurveyTypeAdapter(Context context) {
		mInflater = LayoutInflater.from(context);
		codes = new ArrayList<String>();
		isSelected = new HashMap<Integer, Boolean>();
	}

	public void setData(List<Type> types) {
		this.types = types;
		initDate();
		this.notifyDataSetChanged();
	}

	// 初始化isSelected的数据
	private void initDate() {
		for (int i = 0; i < types.size(); i++) {
			getIsSelected().put(i, false);
		}
	}

	@Override
	public int getCount() {
		return types.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	@SuppressLint("InflateParams")
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.occupy_select_item, null);
			holder = new ViewHolder();
			holder.mTvStyleName = (TextView) convertView.findViewById(R.id.tv_type_name);
			holder.cb = (CheckBox) convertView.findViewById(R.id.cb_device_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Type type = types.get(position);
		holder.mTvStyleName.setText(type.name);
		final String code = type.code;
		// 监听checkBox并根据原来的状态来设置新的状态
		holder.cb.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				if (isSelected.get(position)) {
					isSelected.put(position, false);
					setIsSelected(isSelected);
					if (codes.contains(code)) {
						codes.remove(code);
					}
				} else {
					isSelected.put(position, true);
					setIsSelected(isSelected);
					codes.add(code);
				}
			}
		});
		// 根据isSelected来设置checkbox的选中状况
		holder.cb.setChecked(getIsSelected().get(position));

		return convertView;
	}

	private class ViewHolder {
		private CheckBox cb;
		private TextView mTvStyleName;
	}

	public static HashMap<Integer, Boolean> getIsSelected() {
		return isSelected;
	}
	
	public List<String> getCodes() {
		return codes;
	}

	public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
		SurveyTypeAdapter.isSelected = isSelected;
	}

}
