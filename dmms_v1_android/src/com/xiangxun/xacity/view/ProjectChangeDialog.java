package com.xiangxun.xacity.view;

import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiangxun.xacity.R;
import com.xiangxun.xacity.bean.ResultBeans.Type;

public class ProjectChangeDialog extends Dialog implements android.view.View.OnClickListener {
	private Context mContext;
	private TextView mTvDiaBut1;
	private TextView mTvDiaBut2;
	private TextView tv_msg_title;
	private TextView tv_title;
	private TextView tv_occupy_change;
	private LinearLayout ll_occupy_change_click;
	private PublishSelectTypeDialog typeDialog;
	private List<Type> types = null;
	private String title;
	private String prompt;

	public ProjectChangeDialog(Context context, List<Type> types, String title, String prompt) {
		super(context, R.style.msg_dalog);
		mContext = context;
		this.title = title;
		this.prompt = prompt;
		this.types = types;
		init();
		initLinster();
	}

	private void init() {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.project_change_dialog, null);
		tv_msg_title = (TextView) v.findViewById(R.id.tv_msg_title);
		tv_title = (TextView) v.findViewById(R.id.tv_title);
		tv_occupy_change = (TextView) v.findViewById(R.id.tv_occupy_change);
		ll_occupy_change_click = (LinearLayout) v.findViewById(R.id.ll_occupy_change_click);
		mTvDiaBut1 = (TextView) v.findViewById(R.id.dia_tv_but1);
		mTvDiaBut2 = (TextView) v.findViewById(R.id.dia_tv_but2);
		tv_msg_title.setText(title);
		tv_msg_title.setText(prompt);
		this.setContentView(v);
		this.setCanceledOnTouchOutside(false);
		// WindowManager m = ((Activity) mContext).getWindowManager();
		// Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
		typeDialog = new PublishSelectTypeDialog(mContext, types, tv_occupy_change, "请选择施工进度");
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
		LayoutParams p = getWindow().getAttributes(); // 获取对话框当前的参数值
		// p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
		p.width = (int) (dm.widthPixels * 0.80); // 宽度设置为屏幕的0.95
		getWindow().setAttributes(p); // 设置生效
	}

	private void initLinster() {
		ll_occupy_change_click.setOnClickListener(this);
		mTvDiaBut2.setOnClickListener(this);
		mTvDiaBut1.setOnClickListener(this);
	}
	
	private onSelectItemClick selectItemClick;

	public void setSelectItemClick(onSelectItemClick selectItemClick) {
		this.selectItemClick = selectItemClick;
	}
	
	public interface onSelectItemClick {
		public void changeState(Type type);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dia_tv_but1:
			tv_occupy_change.setText("");
			ProjectChangeDialog.this.dismiss();
			break;

		case R.id.dia_tv_but2:
			String reason = tv_occupy_change.getText().toString().trim();
			if (reason.length() > 0) {
				Type type = (Type) tv_occupy_change.getTag();
				if (selectItemClick != null) {
					ProjectChangeDialog.this.dismiss();
					tv_occupy_change.setText("");
					selectItemClick.changeState(type);
				}
			} else {
				MsgToast.geToast().setMsg("内容不能为空~");
			}
			break;
		case R.id.ll_occupy_change_click:
			typeDialog.show();
			break;
		}
	}

}
