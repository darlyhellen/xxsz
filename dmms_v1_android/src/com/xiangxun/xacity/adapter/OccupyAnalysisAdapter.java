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
import com.xiangxun.xacity.utils.Tools;
import com.xiangxun.xacity.view.xlistView.XListView;

/**
 * @package: com.xiangxun.xacity.adapter
 * @ClassName: OccupyAnalysisAdapter.java
 * @Description: 挖占统计数据适配器
 * @author: HanGJ
 * @date: 2016-3-4 下午3:58:47
 */
public class OccupyAnalysisAdapter extends BaseAdapter {
	private List<OccupyManageBean> itemList;
	private XListView mListView;
	private LayoutInflater inflater;
	private int allsize = 0;

	public OccupyAnalysisAdapter(Context context, XListView mXListView) {
		super();
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
		holder.supplier_title.setText("项目名称: " + Tools.isEmpty(manageBean.projectName).trim());
		holder.supplier_contact.setText("道路名称: " + manageBean.roadname);
		holder.supplier_product.setText("辖区: " + manageBean.managearea);
		holder.appraise_man.setText(Tools.isEmpty(manageBean.typeName));
		holder.appraise_date.setText("施工日期: " + manageBean.starttimeStr + " 至 " + manageBean.endtimeStr);
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
