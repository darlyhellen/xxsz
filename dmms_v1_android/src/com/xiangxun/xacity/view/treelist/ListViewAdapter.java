package com.xiangxun.xacity.view.treelist;

import java.util.List;

import com.xiangxun.xacity.R;
import com.xiangxun.xacity.bean.ResultBeans.TelBookType;
import com.xiangxun.xacity.view.MsgToast;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public abstract class ListViewAdapter extends BaseAdapter {

	protected Context mContext;
	/**
	 * 存储所有可见的Node
	 */
	protected List<TelBookType> mNodes;
	protected LayoutInflater mInflater;

	/**
	 * 
	 * @param mTree
	 * @param context
	 * @param datas
	 * @param defaultExpandLevel
	 *            默认展开几级树
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public ListViewAdapter(ListView mTree, Context context, List<TelBookType> datas) throws IllegalArgumentException, IllegalAccessException {
		mContext = context;
		/**
		 * 过滤出可见的Node
		 */
		mNodes = (List<TelBookType>) datas;
		mInflater = LayoutInflater.from(context);
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
		TelBookType node = mNodes.get(position);
		convertView = getConvertView(node, position, convertView, parent);
		// 设置内边距
		convertView.setPadding(0, 3, 3, 3);
		return convertView;
	}

	public View getConvertView(final TelBookType node, int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.contact_list_item, parent, false);
			viewHolder = new ViewHolder();

			viewHolder.name = (TextView) convertView.findViewById(R.id.name);
			viewHolder.phone = (TextView) convertView.findViewById(R.id.phone);

			viewHolder.call = (ImageView) convertView.findViewById(R.id.call);
			viewHolder.sms = (ImageView) convertView.findViewById(R.id.sms);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.call.setClickable(true);
		viewHolder.call.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (node.memberMsidn != null && node.memberMsidn.length() > 0) {
					Uri callUri = Uri.parse("tel:" + node.memberMsidn);
					mContext.startActivity(new Intent(Intent.ACTION_CALL, callUri));
				} else {
					MsgToast.geToast().setMsg("无电话号码~");
				}
			}
		});

		viewHolder.sms.setClickable(true);
		viewHolder.sms.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (node.memberMsidn != null && node.memberMsidn.length() > 0) {
					Uri smsUri = Uri.parse("smsto:" + node.memberMsidn);
					mContext.startActivity(new Intent(Intent.ACTION_SENDTO, smsUri));
				} else {
					MsgToast.geToast().setMsg("无电话号码~");
				}
			}
		});

		viewHolder.name.setText(node.memberName);
		viewHolder.phone.setText(node.memberMsidn);

		return convertView;
	}

	private final class ViewHolder {
		// ImageView icon;
		TextView name;
		TextView phone;
		ImageView call;
		ImageView sms;
	}
}
