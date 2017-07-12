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
import com.xiangxun.xacity.utils.Tools;
import com.xiangxun.xacity.view.xlistView.XListView;

public class DeviceManageAdapter extends BaseAdapter {
	private int childAt;
	private XListView mListView;
	private LayoutInflater inflater;
	private int allsize = 0;
	private List<DeviceManageBean> itemList;

	public DeviceManageAdapter(Context context, int childAt, XListView mListView) {
		super();
		this.childAt = childAt;
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
		if (childAt == 0) {// 操作人员台账
			holder.manage_title.setText(Tools.isEmpty(manageBean.name));
			holder.manage_contact.setText("岗位: " + Tools.isEmpty(manageBean.station));
			holder.manage_product.setText("电话: " + Tools.isEmpty(manageBean.telephone));
			holder.manage_org.setText("工作单位: " + Tools.isEmpty(manageBean.orgName));
		} else if (childAt == 1) {// 设备台账
			holder.manage_title.setText(Tools.isEmpty(manageBean.name));
			holder.manage_contact.setText("使用单位: " + Tools.isEmpty(manageBean.usingCompanyName));
			holder.manage_product.setText("设备归属: " + Tools.isEmpty(manageBean.stypeName));
			holder.manage_org.setText("启用时间: " + Tools.isEmpty(manageBean.buyDate));
		} else if (childAt == 2) {// 安全保护装置
			holder.manage_title.setText(Tools.isEmpty(manageBean.deviceName));
			holder.manage_contact.setText("资产编号: " + Tools.isEmpty(manageBean.deviceCode));
			holder.manage_product.setText("安全保护装置: " + Tools.isEmpty(manageBean.deviceSafety));
			holder.manage_org.setText("");
		} else if (childAt == 3) {// 设备派遣
			holder.manage_title.setText(Tools.isEmpty(manageBean.deviceName));
			holder.manage_contact.setText("型号/牌照: " + Tools.isEmpty(manageBean.deviceModel));
			holder.manage_product.setText("使用单位: " + Tools.isEmpty(manageBean.usingCompanyName));
			holder.manage_org.setText("使用时间: " + Tools.isEmpty(manageBean.dateTime));
		} else if (childAt == 4) {// 使用记录
			holder.manage_title.setText(Tools.isEmpty(manageBean.deviceName));
			holder.manage_contact.setText("产权单位: " + Tools.isEmpty(manageBean.belongtoCompany));
			holder.manage_product.setText("规格型号: " + Tools.isEmpty(manageBean.model));
			holder.manage_org.setText("牌照号码: " + Tools.isEmpty(manageBean.plate));
		} else if (childAt == 5) {// 设备到场验证
			holder.manage_title.setText(Tools.isEmpty(manageBean.deviceName));
			holder.manage_contact.setText("设备型号: " + Tools.isEmpty(manageBean.model));
			holder.manage_product.setText("施工地点: " + Tools.isEmpty(manageBean.address));
			holder.manage_org.setText("到场时间: " + Tools.isEmpty(manageBean.dateTime));
		} else if (childAt == 6) {// 日常检查记录
			holder.manage_title.setText(Tools.isEmpty(manageBean.deviceName));
			holder.manage_contact.setText("设备型号: " + Tools.isEmpty(manageBean.deviceModel));
			holder.manage_product.setText("设备类型: " + Tools.isEmpty(manageBean.deviceType));
			holder.manage_org.setText("检查日期: " + Tools.isEmpty(manageBean.dateTime));
		} else if (childAt == 7) {// 完好利用率
			holder.manage_title.setText(Tools.isEmpty(manageBean.deviceName));
			holder.manage_contact.setText("使用单位: " + Tools.isEmpty(manageBean.usingCompanyName));
			holder.manage_product.setText("规格型号: " + Tools.isEmpty(manageBean.deviceModel));
			holder.manage_org.setText("月份: " + Tools.isEmpty(manageBean.month));
		} else if (childAt == 8) {// 重点设备月报表
			holder.manage_title.setText(Tools.isEmpty(manageBean.deviceName));
			holder.manage_contact.setText("型号/牌照: " + Tools.isEmpty(manageBean.deviceModel));
			holder.manage_product.setText("使用单位: " + Tools.isEmpty(manageBean.companyName));
			holder.manage_org.setText("使用时间: " + Tools.isEmpty(manageBean.month));
		} else if (childAt == 9) {// 保险管理
			holder.manage_title.setText(Tools.isEmpty(manageBean.deviceName));
			holder.manage_contact.setText("保险公司: " + Tools.isEmpty(manageBean.insureCompany));
			holder.manage_product.setText("设备型号: " + Tools.isEmpty(manageBean.deviceModel));
			holder.manage_org.setText("开始时间: " + Tools.isEmpty(manageBean.startDate));
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
