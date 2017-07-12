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
import com.xiangxun.xacity.bean.ResponseResultBeans.DeviceManageBean;
import com.xiangxun.xacity.view.xlistView.XListView;

/**
 * @package: com.xiangxun.xacity.adapter
 * @ClassName: DeviceVehicleAdapter.java
 * @Description: 车辆查询
 * @author: HanGJ
 * @date: 2016-4-26 下午2:08:22
 */
public class DeviceVehicleAdapter extends BaseAdapter {
	private XListView mListView;
	private LayoutInflater inflater;
	private int allsize = 0;
	private List<DeviceManageBean> itemList;

	public DeviceVehicleAdapter(Context context, XListView mListView) {
		super();
		this.mListView = mListView;
		inflater = LayoutInflater.from(context);
	}

	public void setData(List<DeviceManageBean> itemList, int size) {
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

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.device_manager_list_item_layout, null);
			holder.manage_title = (TextView) convertView.findViewById(R.id.tv_manage_title);
			holder.manage_contact = (TextView) convertView.findViewById(R.id.tv_manage_contact);
			holder.manage_product = (TextView) convertView.findViewById(R.id.tv_manage_product);
			holder.manage_org = (TextView) convertView.findViewById(R.id.tv_manage_org);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		DeviceManageBean manageBean = itemList.get(position);
		holder.manage_title.setText(manageBean.deviceName);
		holder.manage_contact.setText("产权单位: " + manageBean.usingCompanyName);
		holder.manage_product.setText("牌照号码: " + manageBean.stypeName);
		holder.manage_org.setText("入网时间: " + manageBean.buyDate);
		return convertView;
	}

	private static class ViewHolder {
		TextView manage_title;
		TextView manage_contact;
		TextView manage_product;
		TextView manage_org;
	}

}
