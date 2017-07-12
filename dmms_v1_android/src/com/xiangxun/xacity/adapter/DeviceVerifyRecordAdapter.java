package com.xiangxun.xacity.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xiangxun.xacity.R;
import com.xiangxun.xacity.bean.ResponseResultBeans.DeviceScrapVerify;
import com.xiangxun.xacity.utils.Tools;

/**
 * @package: com.xiangxun.xacity.adapter
 * @ClassName: DeviceVerifyRecordAdapter.java
 * @Description: 设备审核记录数据适配器
 * @author: HanGJ
 * @date: 2016-5-09 上午11:03:57
 */
public class DeviceVerifyRecordAdapter extends BaseAdapter {
	private List<DeviceScrapVerify> itemList;
	private LayoutInflater inflater;
	private int allsize = 0;
	
	public DeviceVerifyRecordAdapter(Context context) {
		super();
		inflater = LayoutInflater.from(context);
	}

	public void setData(List<DeviceScrapVerify> itemList) {
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
			holder.project_common02 = (TextView) convertView.findViewById(R.id.tv_project_common02);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		DeviceScrapVerify verify = itemList.get(position);
		holder.project_title.setText("序号: " + (position + 1));
		holder.project_type.setText("审核人: " + Tools.isEmpty(verify.verifier));
		holder.project_model.setText("审核结果: " + Tools.isEmpty(verify.verifyresult));
		holder.project_count.setText("审核意见: " + Tools.isEmpty(verify.suggestion));
		holder.project_common.setVisibility(View.VISIBLE);
		holder.project_common01.setVisibility(View.VISIBLE);
		holder.project_common02.setVisibility(View.VISIBLE);
		holder.project_common.setText("驳回原因: " + Tools.isEmpty(verify.rebutreason));
		holder.project_common01.setText("审核人角色: " + Tools.isEmpty(verify.verifierRole));
		holder.project_common02.setText("审核时间: " + Tools.isEmpty(verify.verifytime));
		return convertView;
	}

	private static class ViewHolder {
		TextView project_title;
		TextView project_type;
		TextView project_model;
		TextView project_count;
		TextView project_common;
		TextView project_common01;
		TextView project_common02;
	}
}
