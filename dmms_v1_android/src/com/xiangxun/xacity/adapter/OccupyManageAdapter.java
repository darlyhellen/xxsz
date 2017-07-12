package com.xiangxun.xacity.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xiangxun.xacity.R;
import com.xiangxun.xacity.bean.ResponseResultBeans.OccupyManageBean;
import com.xiangxun.xacity.utils.MyUtils;
import com.xiangxun.xacity.utils.Tools;
import com.xiangxun.xacity.view.xlistView.XListView;

/**
 * @package: com.xiangxun.xacity.adapter
 * @ClassName: OccupySearchAdapter.java
 * @Description: 查询管理数据适配器
 * @author: HanGJ
 * @date: 2016-3-2 下午3:42:40
 */
public class OccupyManageAdapter extends BaseAdapter {
	private List<OccupyManageBean> itemList;
	private int childAt;
	private XListView mListView;
	private LayoutInflater inflater;
	private int allsize = 0;

	public OccupyManageAdapter(Context context, int childAt, XListView mXListView) {
		super();
		this.childAt = childAt;
		this.mListView = mXListView;
		inflater = LayoutInflater.from(context);
	}

	public void setData(List<OccupyManageBean> itemList, int size) {
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
		OccupyManageBean manageBean = itemList.get(position);
		String endtimeStr = "";
		if ("1".equals(manageBean.isDelay)) {
			endtimeStr = manageBean.approveEndtime != null && manageBean.approveEndtime.length() > 0 ? MyUtils.StringToDate(manageBean.approveEndtime) : "";
		} else {
			endtimeStr = manageBean.endtimeStr;
		}
		if (childAt == 0) {
			holder.supplier_title.setText(Tools.isEmpty(manageBean.projectName).trim());
			holder.supplier_contact.setText("施工单位: " + manageBean.applicantName);
			holder.supplier_product.setText("道路名称: " + manageBean.roadname);
			holder.appraise_man.setText(Tools.isEmpty(manageBean.statusHtml));
			holder.appraise_date.setText("起始日期: " + manageBean.starttimeStr + "\n截止日期: " + endtimeStr);
		} else if (childAt == 1) {
			holder.supplier_title.setText(Tools.isEmpty(manageBean.projectName).trim());
			holder.supplier_contact.setText("申请单位: " + manageBean.applicantName);
			holder.supplier_product.setText("占用类型: " + manageBean.typeName);
			holder.appraise_man.setText(Tools.isEmpty(manageBean.statusHtml));
			holder.appraise_date.setText("起始日期: " + manageBean.starttimeStr + "\n截止日期: " + endtimeStr);
		} else if (childAt == 2) {
			holder.supplier_title.setText(Tools.isEmpty(manageBean.projectName).trim());
			holder.supplier_contact.setText("施工单位: " + manageBean.applicantName);
			holder.supplier_product.setText("施工地点: " + manageBean.roadname);
			holder.appraise_man.setText(Tools.isEmpty(manageBean.statusHtml));
			holder.appraise_date.setText("起始日期: " + manageBean.starttimeStr + "\n截止日期: " + endtimeStr);
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
