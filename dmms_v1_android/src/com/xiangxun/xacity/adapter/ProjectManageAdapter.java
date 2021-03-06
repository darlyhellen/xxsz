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
import com.xiangxun.xacity.bean.ResponseResultBeans.ProjectInfo;
import com.xiangxun.xacity.utils.Tools;

/**
 * @package: com.xiangxun.xacity.adapter
 * @ClassName: ProjectManageAdapter.java
 * @Description: 需求管理详情页面数据适配器
 * @author: HanGJ
 * @date: 2016-2-25 下午5:15:03
 */
public class ProjectManageAdapter extends BaseAdapter {
	private int childAt;
	private LayoutInflater inflater;
	private int allsize = 0;
	private List<ProjectInfo> itemList;
	private boolean isFlag = false;

	public ProjectManageAdapter(Context context, int childAt, boolean isFlag) {
		super();
		this.childAt = childAt;
		this.isFlag = isFlag;
		inflater = LayoutInflater.from(context);
	}

	public void setData(List<ProjectInfo> itemList, int size) {
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
			holder.project_common02 = (TextView) convertView.findViewById(R.id.tv_project_common02);
			holder.project_common03 = (TextView) convertView.findViewById(R.id.tv_project_common03);
			holder.project_common04 = (TextView) convertView.findViewById(R.id.tv_project_common04);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ProjectInfo manageBean = itemList.get(position);
		if (childAt == 0) {//
			if (isFlag) {
				holder.project_title.setText("序号: " + (position + 1));
				holder.project_type.setText("审核类别: " + Tools.isEmpty(manageBean.type));
				holder.project_model.setText("申请人: " + Tools.isEmpty(manageBean.applier));
				holder.project_count.setText("申请时间: " + Tools.isEmpty(manageBean.applytime));
				holder.project_common.setVisibility(View.VISIBLE);
				holder.project_common01.setVisibility(View.VISIBLE);
				holder.project_common02.setVisibility(View.VISIBLE);
				holder.project_common03.setVisibility(View.VISIBLE);
				holder.project_common04.setVisibility(View.VISIBLE);
				holder.project_common.setText("审核人: " + Tools.isEmpty(manageBean.verifier));
				holder.project_common01.setText("审核时间: " + Tools.isEmpty(manageBean.verifytime));
				holder.project_common02.setText("审核结果: " + Tools.isEmpty(manageBean.verifyresult));
				holder.project_common03.setText("驳回原因: " + Tools.isEmpty(manageBean.rebutreason));
				holder.project_common04.setText("审核状态: " + Tools.isEmpty(manageBean.verifyflag));
			} else {
				holder.project_title.setText("序号: " + (position + 1));
				holder.project_type.setText("物资类别: " + Tools.isEmpty(manageBean.type));
				holder.project_model.setText("规格: " + Tools.isEmpty(manageBean.specification));
				holder.project_count.setText("数量: " + Tools.isEmpty(manageBean.amount) + Tools.isEmpty(manageBean.unit));
			}

		} else if (childAt == 1) {//
			if (isFlag) {
				holder.project_title.setText("序号: " + (position + 1));
				holder.project_type.setText("审核类别: " + Tools.isEmpty(manageBean.type));
				holder.project_model.setText("申请人: " + Tools.isEmpty(manageBean.applier));
				holder.project_count.setText("申请时间: " + Tools.isEmpty(manageBean.applytime));
				holder.project_common.setVisibility(View.VISIBLE);
				holder.project_common01.setVisibility(View.VISIBLE);
				holder.project_common02.setVisibility(View.VISIBLE);
				holder.project_common03.setVisibility(View.VISIBLE);
				holder.project_common04.setVisibility(View.VISIBLE);
				holder.project_common.setText("审核人: " + Tools.isEmpty(manageBean.verifier));
				holder.project_common01.setText("审核时间: " + Tools.isEmpty(manageBean.verifytime));
				holder.project_common02.setText("审核结果: " + Tools.isEmpty(manageBean.verifyresult));
				holder.project_common03.setText("驳回原因: " + Tools.isEmpty(manageBean.rebutreason));
				holder.project_common04.setText("审核状态: " + Tools.isEmpty(manageBean.verifyflag));
			} else {
				holder.project_title.setText("序号: " + (position + 1));
				holder.project_type.setText("物资类别: " + Tools.isEmpty(manageBean.type));
				holder.project_model.setText("规格: " + Tools.isEmpty(manageBean.specification));
				holder.project_count.setText("数量: " + Tools.isEmpty(manageBean.amount) + Tools.isEmpty(manageBean.unit));
			}

		}
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
		TextView project_common03;
		TextView project_common04;
	}

}
