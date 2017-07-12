package com.xiangxun.xacity.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xiangxun.xacity.R;
import com.xiangxun.xacity.bean.ResultBeans.Types;
import com.xiangxun.xacity.utils.Tools;
import com.xiangxun.xacity.view.HorizontalProgressBarWithNumber;

/**
 * @package: com.xiangxun.xacity.adapter
 * @ClassName: OccupyStatisicsAdapter.java
 * @Description: 项目统计数据适配器
 * @author: HanGJ
 * @date: 2016-3-17 下午5:38:31
 */
public class OccupyStatisicsAdapter extends BaseAdapter {
	private List<Types> itemList;
	private LayoutInflater inflater;

	public OccupyStatisicsAdapter(Context context) {
		super();
		inflater = LayoutInflater.from(context);
	}

	public void setData(List<Types> itemList) {
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
			convertView = inflater.inflate(R.layout.occupy_project_statistics_item_layout, null);
			holder.tv_project_title = (TextView) convertView.findViewById(R.id.tv_project_title);
			holder.tv_project_start = (TextView) convertView.findViewById(R.id.tv_project_start);
			holder.progress_start = (HorizontalProgressBarWithNumber) convertView.findViewById(R.id.progress_start);
			holder.tv_project_weihu = (TextView) convertView.findViewById(R.id.tv_project_weihu);
			holder.progress_weihu = (HorizontalProgressBarWithNumber) convertView.findViewById(R.id.progress_weihu);
			holder.tv_project_yanqi = (TextView) convertView.findViewById(R.id.tv_project_yanqi);
			holder.progress_yanqi = (HorizontalProgressBarWithNumber) convertView.findViewById(R.id.progress_yanqi);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Types types = itemList.get(position);
		holder.tv_project_title.setText(Tools.isEmpty(types.name) + ": " + types.sum);
		holder.tv_project_start.setText(types.num[0]);
		holder.progress_start.setProgress(types.percent[0]);
		holder.tv_project_weihu.setText(types.num[1]);
		holder.progress_weihu.setProgress(types.percent[1]);
		holder.tv_project_yanqi.setText(types.num[2]);
		holder.progress_yanqi.setProgress(types.percent[2]);
		return convertView;
	}

	private static class ViewHolder {
		TextView tv_project_title;
		TextView tv_project_start;
		HorizontalProgressBarWithNumber progress_start;
		TextView tv_project_weihu;
		HorizontalProgressBarWithNumber progress_weihu;
		TextView tv_project_yanqi;
		HorizontalProgressBarWithNumber progress_yanqi;
	}
}
