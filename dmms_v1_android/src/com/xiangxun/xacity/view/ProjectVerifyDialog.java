package com.xiangxun.xacity.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.EditText;
import android.widget.TextView;

import com.xiangxun.xacity.R;

public class ProjectVerifyDialog extends Dialog {
	private Context mContext;
	private TextView tv_msg_title;
	private TextView tv_msg_prompt;
	private TextView mTvDiaBut1;
	private TextView mTvDiaBut2;
	private EditText mEtContent;
	private String title;
	private String prompt;

	public ProjectVerifyDialog(Context context, String title, String prompt) {
		super(context, R.style.msg_dalog);
		this.title = title;
		this.prompt = prompt;
		mContext = context;
		init();
		initLinster();
	}

	private void init() {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.project_verify_dialog, null);
		tv_msg_title = (TextView) v.findViewById(R.id.tv_msg_title);
		tv_msg_prompt = (TextView) v.findViewById(R.id.tv_msg_prompt);
		mTvDiaBut1 = (TextView) v.findViewById(R.id.dia_tv_but1);
		mTvDiaBut2 = (TextView) v.findViewById(R.id.dia_tv_but2);
		mEtContent = (EditText) v.findViewById(R.id.edt_content);
		tv_msg_title.setText(title);
		tv_msg_prompt.setText(prompt);
		mEtContent.setHint(prompt);
		this.setContentView(v);
		this.setCanceledOnTouchOutside(false);
		// WindowManager m = ((Activity) mContext).getWindowManager();
		// Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高

		DisplayMetrics dm = new DisplayMetrics();
		((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
		LayoutParams p = getWindow().getAttributes(); // 获取对话框当前的参数值
		// p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
		p.width = (int) (dm.widthPixels * 0.80); // 宽度设置为屏幕的0.95

		getWindow().setAttributes(p); // 设置生效
	}

	private void initLinster() {
		mTvDiaBut2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String reason = mEtContent.getText().toString().trim();
				if (reason.length() > 0) {
					if (selectItemClick != null) {
						ProjectVerifyDialog.this.dismiss();
						mEtContent.setText("");
						selectItemClick.itemOnClick(reason);
					}
				} else {
					MsgToast.geToast().setMsg("内容不能为空~");
				}
			}
		});
		mTvDiaBut1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				mEtContent.setText("");
				ProjectVerifyDialog.this.dismiss();
			}
		});
	}
	
	private SelectItemClick selectItemClick;

	public void setSelectItemClick(SelectItemClick selectItemClick) {
		this.selectItemClick = selectItemClick;
	}

	public interface SelectItemClick {
		public void itemOnClick(String item);
	}

}
