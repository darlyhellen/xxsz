package com.xiangxun.xacity.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xiangxun.xacity.R;
import com.xiangxun.xacity.bean.ResponseResultBeans.OccupyArchiveBean;
import com.xiangxun.xacity.utils.Tools;
import com.xiangxun.xacity.view.xlistView.XListView;

/**
 * @package: com.xiangxun.xacity.adapter
 * @ClassName: OccupyArchiveAdapter.java
 * @Description: 档案管理数据适配器
 * @author: HanGJ
 * @date: 2016-3-3 上午9:48:43
 */
public class OccupyArchiveAdapter extends BaseAdapter {
	private List<OccupyArchiveBean> itemList;
	private int childAt;
	private XListView mListView;
	private LayoutInflater inflater;
	private int allsize = 0;

	public OccupyArchiveAdapter(Context context, int childAt, XListView mXListView) {
		super();
		this.childAt = childAt;
		this.mListView = mXListView;
		inflater = LayoutInflater.from(context);
	}

	public void setData(List<OccupyArchiveBean> itemList, int size) {
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
		OccupyArchiveBean searchBean = itemList.get(position);
		if (childAt == 0) {
			holder.supplier_title.setText("单位名称: "+Tools.isEmpty(searchBean.name).trim());
			holder.supplier_contact.setText("单位类型: " + searchBean.unitAttribute);
			holder.supplier_product.setText("单位编号: " + searchBean.id);
			holder.appraise_man.setText(Tools.isEmpty(searchBean.levelCode));
			holder.appraise_date.setText("录入时间: " + searchBean.insertTime);
		} else if (childAt == 1) {
			holder.supplier_title.setText("单位名称: "+Tools.isEmpty(searchBean.name).trim());
			holder.supplier_contact.setText("负责人: " + Tools.isEmpty(searchBean.leadingOfficial));
			holder.supplier_product.setText("单位编号: " + searchBean.id);
			holder.appraise_man.setText(Tools.isEmpty(searchBean.levelCode));
			holder.appraise_date.setText("录入时间: " + searchBean.insertTime);
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
