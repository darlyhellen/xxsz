package com.xiangxun.xacity.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xiangxun.xacity.R;
import com.xiangxun.xacity.bean.ResponseResultBeans.DisposeBean;
import com.xiangxun.xacity.utils.Tools;

/**
 * @package: com.xiangxun.xacity.adapter
 * @ClassName: WorkOrderAdapter.java
 * @Description: 工单处理记录适配器
 * @author: HanGJ
 * @date: 2016-3-10 下午2:50:50
 */
public class WorkOrderAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private int allsize = 0;
	private List<DisposeBean> itemList;

	public WorkOrderAdapter(Context context) {
		super();
		inflater = LayoutInflater.from(context);
	}

	public void setData(List<DisposeBean> itemList, int size) {
		if (itemList != null) {
			this.itemList = itemList;
			allsize = itemList.size();
		}
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return allsize;
	}

	@Override
	public Object getItem(int position) {
		return itemList.get(position);
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
			convertView = inflater.inflate(R.layout.materiel_list_item_layout, null);
			holder.project_title = (TextView) convertView.findViewById(R.id.tv_project_title);
			holder.project_type = (TextView) convertView.findViewById(R.id.tv_project_type);
			holder.project_model = (TextView) convertView.findViewById(R.id.tv_project_model);
			holder.project_count = (TextView) convertView.findViewById(R.id.tv_project_count);
			holder.project_common = (TextView) convertView.findViewById(R.id.tv_project_common);
			holder.project_common01 = (TextView) convertView.findViewById(R.id.tv_project_common01);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		DisposeBean manageBean = itemList.get(position);
		holder.project_title.setText("序号: " + (position + 1));
		holder.project_type.setText("操作人: " + Tools.isEmpty(manageBean.operaterId));
		holder.project_model.setText("处理时间: " + Tools.isEmpty(manageBean.disposeTime));
		holder.project_count.setText("承接人: " + Tools.isEmpty(manageBean.acceptName));
		holder.project_common.setVisibility(View.VISIBLE);
		holder.project_common01.setVisibility(View.VISIBLE);
		holder.project_common.setText("处理类型: " + Tools.isEmpty(manageBean.disposeType));
		holder.project_common01.setText("处理详情: " + Tools.isEmpty(manageBean.disposeText));
		return convertView;
	}

	private static class ViewHolder {
		TextView project_title;
		TextView project_type;
		TextView project_model;
		TextView project_count;
		TextView project_common;
		TextView project_common01;
	}

}
