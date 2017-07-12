package com.xiangxun.xacity.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xiangxun.xacity.R;
import com.xiangxun.xacity.bean.ResponseResultBeans.MaintainRecord;
import com.xiangxun.xacity.utils.Tools;

/**
 * @package: com.xiangxun.xacity.adapter
 * @ClassName: OccupyStatisicsAdapter.java
 * @Description: 维修记录数据适配器
 * @author: HanGJ
 * @date: 2016-4-29 上午11:38:31
 */
public class DeviceMaintainRecordAdapter extends BaseAdapter {
	private List<MaintainRecord> itemList;
	private LayoutInflater inflater;

	public DeviceMaintainRecordAdapter(Context context) {
		super();
		inflater = LayoutInflater.from(context);
	}

	public void setData(List<MaintainRecord> itemList) {
		this.itemList = itemList;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return itemList.size();
	}

	@Override
	public Object getItem(int position) {
		return itemList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.device_maintain_item_layout, null);
			holder.tv_occupy_road_type = (TextView) convertView.findViewById(R.id.tv_occupy_road_type);
			holder.tv_occupy_length = (TextView) convertView.findViewById(R.id.tv_occupy_length);
			holder.tv_occupy_width = (TextView) convertView.findViewById(R.id.tv_occupy_width);
			holder.tv_occupy_area = (TextView) convertView.findViewById(R.id.tv_occupy_area);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		MaintainRecord types = itemList.get(position);
		if(types.maintainName.contains("费用合计")){
			holder.tv_occupy_road_type.setTextColor(Color.RED);
			holder.tv_occupy_area.setText(Tools.isEmpty(types.totalCount));
		} else {
			holder.tv_occupy_road_type.setTextColor(Color.BLACK);
			holder.tv_occupy_area.setText(Tools.isEmpty(types.costTotal));
		}
		holder.tv_occupy_road_type.setText(Tools.isEmpty(types.maintainName));
		holder.tv_occupy_length.setText(Tools.isEmpty(types.stuffCost));
		holder.tv_occupy_width.setText(Tools.isEmpty(types.taskCost));
		return convertView;
	}

	private static class ViewHolder {
		TextView tv_occupy_road_type;
		TextView tv_occupy_length;
		TextView tv_occupy_width;
		TextView tv_occupy_area;
	}
}
