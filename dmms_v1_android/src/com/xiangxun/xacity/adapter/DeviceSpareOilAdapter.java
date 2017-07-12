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
import com.xiangxun.xacity.bean.ResponseResultBeans.DeviceSpareOilBean;
import com.xiangxun.xacity.utils.Tools;
import com.xiangxun.xacity.view.xlistView.XListView;

/**
 * @package: com.xiangxun.xacity.adapter
 * @ClassName: DeviceSpareOilAdapter.java
 * @Description: 备件油料数据适配器
 * @author: HanGJ
 * @date: 2016-2-24 下午2:11:17
 */
public class DeviceSpareOilAdapter extends BaseAdapter {
	private int childAt;
	private XListView mListView;
	private LayoutInflater inflater;
	private int allsize = 0;
	private List<DeviceSpareOilBean> itemList;

	public DeviceSpareOilAdapter(Context context, int childAt, XListView mListView) {
		super();
		this.childAt = childAt;
		this.mListView = mListView;
		inflater = LayoutInflater.from(context);
	}

	public void setData(List<DeviceSpareOilBean> itemList, int size) {
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
		DeviceSpareOilBean manageBean = itemList.get(position);
		if (childAt == 0) {// 入库台账记录
			holder.manage_title.setText(Tools.isEmpty(manageBean.name));
			holder.manage_contact.setText("单位: " + Tools.isEmpty(manageBean.oname));
			holder.manage_product.setText("规格型号: " + Tools.isEmpty(manageBean.model));
			holder.manage_org.setText("进货时间: " + Tools.isEmpty(manageBean.dateTime));
		} else if (childAt == 1) {// 出库台账记录
			holder.manage_title.setText(Tools.isEmpty(manageBean.name));
			holder.manage_contact.setText("单位: " + Tools.isEmpty(manageBean.oname));
			holder.manage_product.setText("工程名称: " + Tools.isEmpty(manageBean.projectName));
			holder.manage_org.setText("领料日期: " + Tools.isEmpty(manageBean.dateTime));
		} else if (childAt == 2) {// 加油管理
			holder.manage_title.setText(Tools.isEmpty(manageBean.name));
			holder.manage_contact.setText("单位: " + Tools.isEmpty(manageBean.orgName));
			holder.manage_product.setText("牌照: " + Tools.isEmpty(manageBean.plate));
			holder.manage_org.setText("加油时间: " + Tools.isEmpty(manageBean.dateTime));
		} else if (childAt == 3) {// 领料申请
			holder.manage_title.setText(Tools.isEmpty(manageBean.name));
			holder.manage_contact.setText("单位: " + Tools.isEmpty(manageBean.companyName));
			holder.manage_product.setText("型号: " + Tools.isEmpty(manageBean.model));
			holder.manage_org.setText("申请时间: " + Tools.isEmpty(manageBean.dateTime));
		}
		return convertView;
	}

	private static class ViewHolder {
		TextView manage_title;
		TextView manage_contact;
		TextView manage_product;
		TextView manage_org;
	}

}
