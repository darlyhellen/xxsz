package com.xiangxun.xacity.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xiangxun.xacity.R;
import com.xiangxun.xacity.bean.ResponseResultBeans.DeviceMaintainBean;
import com.xiangxun.xacity.utils.Tools;
import com.xiangxun.xacity.view.xlistView.XListView;

/**
 * @package: com.xiangxun.xacity.adapter
 * @ClassName: DeviceMaintainAdapter.java
 * @Description: 设备維修子页面数据适配器
 * @author: HanGJ
 * @date: 2016-2-24 上午11:03:57
 */
public class DeviceMaintainAdapter extends BaseAdapter {
	private List<DeviceMaintainBean> itemList;
	private int childAt;
	private XListView mListView;
	private LayoutInflater inflater;
	private int allsize = 0;

	public DeviceMaintainAdapter(Context context, int childAt, XListView mXListView) {
		super();
		this.childAt = childAt;
		this.mListView = mXListView;
		inflater = LayoutInflater.from(context);
	}

	public void setData(List<DeviceMaintainBean> itemList, int size) {
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
		DeviceMaintainBean maintainBean = itemList.get(position);
		if (childAt == 0) {
			holder.supplier_title.setText(Tools.isEmpty(maintainBean.deviceName));
			holder.supplier_contact.setText("资产编号: " + Tools.isEmpty(maintainBean.deviceCode));
			holder.supplier_product.setText("所属单位: " + Tools.isEmpty(maintainBean.belongtoCompanyName));
			holder.appraise_man.setText(Tools.isEmpty(maintainBean.operator));
			holder.appraise_date.setText("上次维修时间: " + Tools.isEmpty(maintainBean.lastTime));
		} else if (childAt == 1) {
			holder.supplier_title.setText(Tools.isEmpty(maintainBean.deviceName));
			holder.supplier_contact.setText("派单人: " + Tools.isEmpty(maintainBean.recipient));
			holder.supplier_product.setText("维修单号: " + Tools.isEmpty(maintainBean.code));
			holder.appraise_man.setText(Tools.isEmpty(maintainBean.recipient));
			holder.appraise_date.setText("维修时间: " + Tools.isEmpty(maintainBean.entranceDateTime));
		} else if (childAt == 2) {
			holder.supplier_title.setText(Tools.isEmpty(maintainBean.deviceName));
			holder.supplier_contact.setText("使用单位: " + Tools.isEmpty(maintainBean.usingCompanyName));
			holder.supplier_product.setText("维修费用: " + Tools.isEmpty(maintainBean.cost));
			holder.appraise_man.setText(Tools.isEmpty(maintainBean.plate));
			holder.appraise_date.setText("维修时间: " + Tools.isEmpty(maintainBean.dateTime));
		} else if (childAt == 3) {
			holder.supplier_title.setText(Tools.isEmpty(maintainBean.deviceName));
			holder.supplier_contact.setText("使用单位: " + Tools.isEmpty(maintainBean.usingCompanyName));
			holder.supplier_product.setText("资产编号: " + Tools.isEmpty(maintainBean.deviceCode));
			holder.appraise_man.setText(Tools.isEmpty(maintainBean.principal));
			holder.appraise_date.setText("事故发生时间: " + Tools.isEmpty(maintainBean.dateTime));
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
