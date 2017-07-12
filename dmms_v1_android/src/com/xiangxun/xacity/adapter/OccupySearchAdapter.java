package com.xiangxun.xacity.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xiangxun.xacity.R;
import com.xiangxun.xacity.bean.ResponseResultBeans.OccupySearchBean;
import com.xiangxun.xacity.utils.Tools;
import com.xiangxun.xacity.view.xlistView.XListView;

/**
 * @package: com.xiangxun.xacity.adapter
 * @ClassName: OccupySearchAdapter.java
 * @Description: 查询管理数据适配器
 * @author: HanGJ
 * @date: 2016-3-2 下午3:42:40
 */
public class OccupySearchAdapter extends BaseAdapter {
	private List<OccupySearchBean> itemList;
	private int childAt;
	private XListView mListView;
	private LayoutInflater inflater;
	private int allsize = 0;

	public OccupySearchAdapter(Context context, int childAt, XListView mXListView) {
		super();
		this.childAt = childAt;
		this.mListView = mXListView;
		inflater = LayoutInflater.from(context);
	}

	public void setData(List<OccupySearchBean> itemList, int size) {
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
		OccupySearchBean searchBean = itemList.get(position);
		if (childAt == 0) {
			holder.supplier_title.setText(Tools.isEmpty(searchBean.pname).trim());
			holder.supplier_contact.setText("建设单位: " + searchBean.applicantName);
			holder.supplier_product.setText("施工地点: " + searchBean.roadId);
			holder.appraise_man.setText(Tools.isEmpty(searchBean.occupyType));
			holder.appraise_date.setText("开始时间: " + searchBean.begintimeStr + "\n截止时间: " + searchBean.deadtimeStr);
		} else if (childAt == 1) {
			holder.supplier_title.setText(Tools.isEmpty(searchBean.pname).trim());
			holder.supplier_contact.setText("建设单位: " + searchBean.applicantName);
			holder.supplier_product.setText("施工地点: " + searchBean.roadId);
			holder.appraise_man.setText(Tools.isEmpty(searchBean.occupyType));
			holder.appraise_date.setText("勘察时间: " + searchBean.inspectTimeStr);
		} else if (childAt == 2) {
			holder.supplier_title.setText(Tools.isEmpty(searchBean.pname).trim());
			holder.supplier_contact.setText("建设单位: " + searchBean.applicantName);
			holder.supplier_product.setText("施工地点: " + searchBean.roadname);
			holder.appraise_man.setText(Tools.isEmpty(searchBean.type));
			holder.appraise_date.setText("更改前状态: " + searchBean.beforeStatus + "\n更改后状态: " + searchBean.afterStatus);
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
