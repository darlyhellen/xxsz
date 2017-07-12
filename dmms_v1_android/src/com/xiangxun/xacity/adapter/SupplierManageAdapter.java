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
import com.xiangxun.xacity.bean.ResponseResultBeans.PurchaseOrderManageBean;
import com.xiangxun.xacity.utils.Tools;
import com.xiangxun.xacity.view.xlistView.XListView;

/**
 * @package: com.xiangxun.xacity.adapter
 * @ClassName: SupplierManageAdapter.java
 * @Description: 供应商管理数据适配器
 * @author: HanGJ
 * @date: 2016-2-29 上午11:35:24
 */
public class SupplierManageAdapter extends BaseAdapter {
	private int childAt;
	private XListView mListView;
	private LayoutInflater inflater;
	private int allsize = 0;
	private List<PurchaseOrderManageBean> itemList;

	public SupplierManageAdapter(Context context, int childAt, XListView mListView) {
		super();
		this.childAt = childAt;
		this.mListView = mListView;
		inflater = LayoutInflater.from(context);
	}

	public void setData(List<PurchaseOrderManageBean> itemList, int size) {
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
	public PurchaseOrderManageBean getItem(int position) {
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
			convertView = inflater.inflate(R.layout.project_manage_list_item_layout, null);
			holder.supplier_title = (TextView) convertView.findViewById(R.id.tv_supplier_title);
			holder.supplier_contact = (TextView) convertView.findViewById(R.id.tv_supplier_contact);
			holder.supplier_product = (TextView) convertView.findViewById(R.id.tv_supplier_product);
			holder.appraise_man = (TextView) convertView.findViewById(R.id.tv_appraise_man);
			holder.appraise_date = (TextView) convertView.findViewById(R.id.tv_appraise_date);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		PurchaseOrderManageBean manageBean = itemList.get(position);
		if (childAt == 0) {// 
			holder.supplier_title.setText(manageBean.projectName);
			holder.supplier_contact.setText("物资类别: " + Tools.isEmpty(manageBean.type));
			holder.supplier_product.setText("供应商: " + Tools.isEmpty(manageBean.supplier));
			holder.appraise_man.setText(manageBean.statusHtml);
			holder.appraise_date.setText("计划月份: " + manageBean.monthStr);
		} else if (childAt == 1) {
			holder.supplier_title.setText(manageBean.projectName);
			holder.supplier_contact.setText("物资类别: " + Tools.isEmpty(manageBean.type));
			holder.supplier_product.setText("供应商: " + Tools.isEmpty(manageBean.supplier));
			holder.appraise_man.setText(manageBean.statusHtml);
			holder.appraise_date.setText("计划月份: " + manageBean.monthStr);
		} else if (childAt == 2) {
			holder.supplier_title.setText(manageBean.name);
			holder.supplier_contact.setText("物资类别: " + Tools.isEmpty(manageBean.type));
			holder.supplier_product.setText("供应商: " + Tools.isEmpty(manageBean.supplier));
			holder.appraise_man.setText(manageBean.operator);
			holder.appraise_date.setText("添加时间: " + Tools.isEmpty(manageBean.entertime));
		} else if (childAt == 3) {
			holder.supplier_title.setText(manageBean.name);
			holder.supplier_contact.setText("物资类别: " + Tools.isEmpty(manageBean.type));
			holder.supplier_product.setText("供应商: " + Tools.isEmpty(manageBean.supplier));
			holder.appraise_man.setText(manageBean.statusHtml);
			holder.appraise_date.setText("入库时间: " + Tools.isEmpty(manageBean.entertime));
		} else {
			holder.supplier_title.setText(manageBean.name);
			holder.supplier_contact.setText("物资类别: " + Tools.isEmpty(manageBean.type));
			holder.supplier_product.setText("供应商: " + Tools.isEmpty(manageBean.supplier));
			holder.appraise_man.setText(manageBean.statusHtml);
			holder.appraise_date.setText("出库时间: " + Tools.isEmpty(manageBean.entertime));
		}
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
