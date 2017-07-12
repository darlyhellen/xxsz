package com.xiangxun.xacity.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xiangxun.xacity.R;
import com.xiangxun.xacity.bean.ResponseResultBeans.NoticeBean;
import com.xiangxun.xacity.view.xlistView.XListView;

/**
 * @package: com.xiangxun.xacity.adapter
 * @ClassName: NiticeAdapter.java
 * @Description: 通知公告列表适配器
 * @author: HanGJ
 * @date: 2016-1-18 下午4:23:39
 */
public class NoticeAdapter extends BaseAdapter {
	private Context m_act;
	private XListView mListView;
	private List<NoticeBean> itemList;
	private int allsize = 0;

	public NoticeAdapter(Context act, XListView mXListView) {
		m_act = act;
		this.mListView = mXListView;
	}

	public void setData(List<NoticeBean> itemList, int size) {
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
	public NoticeBean getItem(int position) {
		if (itemList != null) {
			return itemList.get(position);
		}
		return null;
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
			convertView = LayoutInflater.from(m_act).inflate(R.layout.nitice_list_item_layout, null);
			holder.nitice_title = (TextView) convertView.findViewById(R.id.mine_nitice_title);
			holder.nitice_user = (TextView) convertView.findViewById(R.id.mine_nitice_user);
			holder.nitice_date = (TextView) convertView.findViewById(R.id.mine_nitice_date);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		NoticeBean item = itemList.get(position);
		holder.nitice_title.setText(item.noticeTitle);
		holder.nitice_user.setText(String.format(m_act.getString(R.string.nitice_user), item.releaseUserId));
		holder.nitice_date.setText(String.format(m_act.getString(R.string.nitice_date), item.noticeBegin));
		return convertView;
	}

	private static class ViewHolder {
		TextView nitice_title;
		TextView nitice_user;
		TextView nitice_date;
	}

}
