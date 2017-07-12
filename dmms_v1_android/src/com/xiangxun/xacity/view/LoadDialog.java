package com.xiangxun.xacity.view;

import com.xiangxun.xacity.R;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * @package: com.xiangxun.xacity.view
 * @ClassName: LoadDialog.java
 * @Description: 加载等待框
 * @author: HanGJ
 * @date: 2016-2-1 下午3:48:00
 */
public class LoadDialog extends Dialog {
	private Context mContext;
	private TextView mTitle;

	public LoadDialog(Context context) {
		super(context, R.style.msg_dalog);
		mContext = context;
		init();
	}

	public LoadDialog(Context context, int theme) {
		super(context, theme);
		mContext = context;
		init();
	}

	private void init() {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.load_dialog, null);
		mTitle = (TextView) v.findViewById(R.id.tv_loading_title);
		this.setCancelable(false);
		this.setContentView(v);
	}

	public void setTitle(String title) {
		mTitle.setText(title);
	}

}
