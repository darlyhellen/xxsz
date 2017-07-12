package com.xiangxun.xacity.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xiangxun.xacity.R;
import com.xiangxun.xacity.bean.ResponseResultBeans.WorkOrderBean;
import com.xiangxun.xacity.utils.Tools;
import com.xiangxun.xacity.view.xlistView.XListView;

/**
 * @package: com.xiangxun.xacity.adapter
 * @ClassName: WorkOrderSearchAdapter.java
 * @Description: 工单查询数据适配器
 * @author: HanGJ
 * @date: 2016-3-10 上午10:33:44
 */
public class WorkOrderSearchAdapter extends BaseAdapter {
	private List<WorkOrderBean> itemList;
	private XListView mListView;
	private LayoutInflater inflater;
	private int allsize = 0;

	public WorkOrderSearchAdapter(Context context, XListView mXListView) {
		super();
		this.mListView = mXListView;
		inflater = LayoutInflater.from(context);
	}

	public void setData(List<WorkOrderBean> itemList, int size) {
		if (itemList != null) {
			this.itemList = itemList;
			allsize = itemList.size();
		}

		mListView.removeFooterView(mListView.mFooterView);
		if (size > 9) {
			mListView.addFooterView(mListView.mFooterView);
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

	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.device_purchase_list_item_layout, null);
			holder.supplier_title = (TextView) convertView.findViewById(R.id.tv_supplier_title);
			holder.supplier_contact = (TextView) convertView.findViewById(R.id.tv_supplier_contact);
			holder.supplier_product = (TextView) convertView.findViewById(R.id.tv_supplier_product);
			holder.appraise_man = (TextView) convertView.findViewById(R.id.tv_appraise_man);
			holder.appraise_date = (TextView) convertView.findViewById(R.id.tv_appraise_date);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		WorkOrderBean orderBean = itemList.get(position);
		holder.supplier_title.setText("工单编号:" + Tools.isEmpty(orderBean.workOrderCode));
		holder.supplier_contact.setText("责任单位: " + Tools.isEmpty(orderBean.dutyOrgCode));
		holder.supplier_product.setText("工单内容: " + Tools.isEmpty(orderBean.workOrderText));
		if (TextUtils.isEmpty(orderBean.workEventState)) {
			holder.appraise_man.setVisibility(View.INVISIBLE);
		} else {
			if("待办".equals(orderBean.workEventState)){
				holder.appraise_man.setBackground(convertView.getResources().getDrawable(R.drawable.bg_line_blue));
			} else if("闭合".equals(orderBean.workEventState)){
				holder.appraise_man.setBackground(convertView.getResources().getDrawable(R.drawable.bg_line_green));
			}else if("已审".equals(orderBean.workEventState)){
				holder.appraise_man.setBackground(convertView.getResources().getDrawable(R.drawable.bg_line_black));
			} else if("核实".equals(orderBean.workEventState)){
				holder.appraise_man.setBackground(convertView.getResources().getDrawable(R.drawable.bg_line_purple));
			} else if("派发".equals(orderBean.workEventState)){
				holder.appraise_man.setBackground(convertView.getResources().getDrawable(R.drawable.bg_line_cccff));
			} else if("已办".equals(orderBean.workEventState)){
				holder.appraise_man.setBackground(convertView.getResources().getDrawable(R.drawable.bg_line_a7f0dc));
			} else if("改派".equals(orderBean.workEventState)){
				holder.appraise_man.setBackground(convertView.getResources().getDrawable(R.drawable.bg_line_ccc00));
			} else {
				holder.appraise_man.setBackground(convertView.getResources().getDrawable(R.drawable.bg_line_orange));
			}
			holder.appraise_man.setVisibility(View.VISIBLE);
			holder.appraise_man.setText(Tools.isEmpty(orderBean.workEventState));
		}
		holder.appraise_date.setText("发布时间: " + Tools.isEmpty(orderBean.createTime));
		return convertView;
	}

	private static class ViewHolder {
		TextView supplier_title;
		TextView supplier_contact;
		TextView supplier_product;
		TextView appraise_man;
		TextView appraise_date;
	}
}
