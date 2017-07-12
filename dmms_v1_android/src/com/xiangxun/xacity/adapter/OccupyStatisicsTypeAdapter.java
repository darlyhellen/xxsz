package com.xiangxun.xacity.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xiangxun.xacity.R;
import com.xiangxun.xacity.bean.ResultBeans.OccupyStatistics;
import com.xiangxun.xacity.utils.Tools;

/**
 * @package: com.xiangxun.xacity.adapter
 * @ClassName: OccupyStatisicsAdapter.java
 * @Description: 项目统计数据适配器
 * @author: HanGJ
 * @date: 2016-3-17 下午5:38:31
 */
public class OccupyStatisicsTypeAdapter extends BaseAdapter {
	private List<OccupyStatistics> itemList;
	private LayoutInflater inflater;
	private String queryType = "area";

	public OccupyStatisicsTypeAdapter(Context context) {
		super();
		inflater = LayoutInflater.from(context);
	}

	public void setData(List<OccupyStatistics> itemList, String queryType) {
		this.itemList = itemList;
		this.queryType = queryType;
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
			convertView = inflater.inflate(R.layout.occupy_statistics_item_layout, null);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.tv_occupy_road_type = (TextView) convertView.findViewById(R.id.tv_occupy_road_type);
			holder.tv_occupy_length = (TextView) convertView.findViewById(R.id.tv_occupy_length);
			holder.tv_occupy_width = (TextView) convertView.findViewById(R.id.tv_occupy_width);
			holder.tv_occupy_area = (TextView) convertView.findViewById(R.id.tv_occupy_area);
			holder.tv_occupy_lengths = (TextView) convertView.findViewById(R.id.tv_occupy_lengths);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		OccupyStatistics types = itemList.get(position);
		if("area".equals(queryType)){
			holder.tv_name.setText("辖区");
		} else if("style".equals(queryType)){
			holder.tv_name.setText("路面种类");
		} else {
			holder.tv_name.setText("挖占类型");
		}
		holder.tv_occupy_road_type.setText(Tools.isEmpty(types.name));
		holder.tv_occupy_length.setText(types.wrecordLen + "");
		holder.tv_occupy_width.setText(types.zrecordLen + "");
		holder.tv_occupy_area.setText(types.wrecordSum + "");
		holder.tv_occupy_lengths.setText(types.zrecordSum + "");
		return convertView;
	}

	private static class ViewHolder {
		TextView tv_name;
		TextView tv_occupy_road_type;
		TextView tv_occupy_length;
		TextView tv_occupy_width;
		TextView tv_occupy_area;
		TextView tv_occupy_lengths;
	}
}
