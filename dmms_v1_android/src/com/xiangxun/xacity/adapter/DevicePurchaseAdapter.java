package com.xiangxun.xacity.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xiangxun.xacity.R;
import com.xiangxun.xacity.bean.ResponseResultBeans.SupplierBean;
import com.xiangxun.xacity.view.xlistView.XListView;

/**
 * @package: com.xiangxun.xacity.adapter
 * @ClassName: DevicePurchaseAdapter.java
 * @Description: 设备采购子页面数据适配器
 * @author: HanGJ
 * @date: 2016-2-20 上午10:19:43
 */
public class DevicePurchaseAdapter extends BaseAdapter {
	private List<SupplierBean> itemList;
	private int childAt;
	private XListView mListView;
	private LayoutInflater inflater;
	private int allsize = 0;

	public DevicePurchaseAdapter(Context context, int childAt, XListView mXListView) {
		super();
		this.childAt = childAt;
		this.mListView = mXListView;
		inflater = LayoutInflater.from(context);
	}

	public void setData(List<SupplierBean> itemList, int size) {
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
		SupplierBean supplierBean = itemList.get(position);
		if (childAt == 0) {
			holder.supplier_title.setText(supplierBean.name);
			holder.supplier_contact.setText("联系人: " + supplierBean.linkman);
			holder.supplier_product.setText("产品: " + supplierBean.product);
			holder.appraise_man.setText(supplierBean.evaluateMan);
			holder.appraise_date.setText("评价时间: " + supplierBean.dateTime);
		} else if (childAt == 1) {
			holder.supplier_title.setText(supplierBean.deviceName);
			holder.supplier_contact.setText("申请人: " + supplierBean.applicant);
			holder.supplier_product.setText("申请单位: " + supplierBean.orgName);
			holder.appraise_man.setText(supplierBean.statusHtml);
			holder.appraise_date.setText("申请时间: " + supplierBean.applyDate);
		} else if (childAt == 2) {
			holder.supplier_title.setText(supplierBean.deviceName);
			holder.supplier_contact.setText("使用单位: " + supplierBean.usingCompanyName);
			holder.supplier_product.setText("使用地点: " + supplierBean.address);
			holder.appraise_man.setText(supplierBean.surveyor);
			holder.appraise_date.setText("到货时间: " + supplierBean.aogDateTime);
		} else if (childAt == 3) {
			holder.supplier_title.setText(supplierBean.deviceName);
			holder.supplier_contact.setText("单位名称: " + supplierBean.deviceSupplierName);
			holder.supplier_product.setText("型号规格: " + supplierBean.model);
			holder.appraise_man.setText(supplierBean.linkman);
			holder.appraise_date.setText("租赁时间: " + supplierBean.dateTime);
		} else if (childAt == 4) {
			holder.supplier_title.setText(supplierBean.name);
			holder.supplier_contact.setText("采购单位: " + supplierBean.purchaseDeparment);
			holder.supplier_product.setText("对方单位: " + supplierBean.cooperationDepartment);
			holder.appraise_man.setText(supplierBean.operator);
			holder.appraise_date.setText("签订时间: " + supplierBean.registerDate);
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
