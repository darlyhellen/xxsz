package com.xiangxun.xacity.ui.patrol;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiangxun.xacity.R;
import com.xiangxun.xacity.bean.ResultBeans.OrgUserType;


public class OrgListViewAdapter extends BaseAdapter {
	private List<OrgUserType> mNodes;
	private LayoutInflater mInflater;

	/**
	 * @param context
	 * @param datas
	 */
	public OrgListViewAdapter(Context context, List<OrgUserType> datas) {
		this.mNodes = datas;
		this.mInflater = LayoutInflater.from(context);
	}
	
	public void upData(List<OrgUserType> datas){
		this.mNodes = datas;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mNodes.size();
	}

	@Override
	public Object getItem(int position) {
		return mNodes.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		OrgUserType node = mNodes.get(position);
		convertView = getConvertView(node, position, convertView, parent);
		// 设置内边距
		convertView.setPadding(0, 3, 3, 3);
		return convertView;
	}

	public View getConvertView(OrgUserType node, final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.orguser_list_item, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.name = (TextView) convertView.findViewById(R.id.name);
			viewHolder.check = (ImageView) convertView.findViewById(R.id.check);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.check.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (checkBoxClick != null) {
					checkBoxClick.onCheckBoxClick(position);
				}
			}
		});
		viewHolder.name.setText(node.locateRegName);
		if (node.checked) {
			viewHolder.check.setImageResource(R.drawable.checkbox_press);
		} else {
			viewHolder.check.setImageResource(R.drawable.checkbox_normal);
		}
		return convertView;
	}

	private final class ViewHolder {
		TextView name;
		ImageView check;
	}

	private CheckBoxClick checkBoxClick;

	public void setCheckBoxClick(CheckBoxClick checkBoxClick) {
		this.checkBoxClick = checkBoxClick;
	}

	public interface CheckBoxClick {
		public void onCheckBoxClick(int position);
	}
}
