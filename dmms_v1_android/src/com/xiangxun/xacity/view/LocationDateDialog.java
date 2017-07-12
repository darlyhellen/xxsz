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
import com.xiangxun.xacity.utils.DateTimePickSelctUtil;
import com.xiangxun.xacity.utils.Tools;

/**
 * @package: com.xiangxun.xacity.view
 * @ClassName: LocationDateDialog.java
 * @Description: 自定义查询历史轨迹dialog
 * @author: HanGJ
 * @date: 2016-4-20 下午2:26:05
 */
public class LocationDateDialog extends Dialog implements android.view.View.OnClickListener {

	private Context mContext;
	private TextView mTvDiaTitle;
	private EditText start_location_date;
	private EditText end_location_date;
	private TextView mTvDiaBut1;
	private TextView mTvDiaBut2;

	public LocationDateDialog(Context context) {
		super(context, R.style.msg_dalog);
		mContext = context;
		init();
		initLinster();
	}

	/**
	 * 
	 * @Title:
	 * @Description:
	 * @param:
	 * @return: void
	 * @throws
	 */
	private void initLinster() {
		View.OnClickListener listener = new View.OnClickListener() {
			public void onClick(View v) {
				LocationDateDialog.this.dismiss();
			}
		};
		mTvDiaBut1.setOnClickListener(listener);
		mTvDiaBut2.setOnClickListener(this);
		start_location_date.setOnClickListener(this);
		end_location_date.setOnClickListener(this);
	}

	/**
	 * 
	 * @Title:
	 * @Description:
	 * @param:
	 * @return: void
	 * @throws
	 */
	private void init() {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.location_date_dialog, null);
		mTvDiaTitle = (TextView) v.findViewById(R.id.diag_title);
		start_location_date = (EditText) v.findViewById(R.id.start_location_date);
		end_location_date = (EditText) v.findViewById(R.id.end_location_date);
		mTvDiaBut1 = (TextView) v.findViewById(R.id.dia_tv_but1);
		mTvDiaBut2 = (TextView) v.findViewById(R.id.dia_tv_but2);
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

	/**
	 * @Description:
	 * @param: text
	 * @return: void
	 */
	public void setTiele(CharSequence text) {
		mTvDiaTitle.setText(text);
	}

	@Override
	public void show() {
		try {
			if (mContext == null || ((Activity) mContext).isFinishing()) {
				return;
			}
			super.show();
		} catch (Exception e) {
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.start_location_date:
			new DateTimePickSelctUtil(mContext).dateTimePicKDialog(start_location_date);
			break;
		case R.id.end_location_date:
			new DateTimePickSelctUtil(mContext).dateTimePicKDialog(end_location_date);
			break;
		case R.id.dia_tv_but2:
			String beginTime = start_location_date.getText().toString().trim();
			String endTime = end_location_date.getText().toString().trim();
			if (beginTime.length() <= 0) {
				MsgToast.geToast().setMsg("请选择开始时间~");
				return;
			}
			if (endTime.length() <= 0) {
				MsgToast.geToast().setMsg("请选择结束时间~");
				return;
			}

			if (Tools.computeTime(beginTime, endTime).equals("-1")) {
				MsgToast.geToast().setMsg("开始时间必须小于于结束时间~");
				return;
			}
			if(locationDateClick != null){
				locationDateClick.locationDate(beginTime, endTime);
			}
			dismiss();
			break;
		}
	}

	private LocationDateClick locationDateClick;
	public void setLocationDateClick(LocationDateClick locationDateClick) {
		this.locationDateClick = locationDateClick;
	}

	public interface LocationDateClick {
		public void locationDate(String beginTime, String endTime);
	}
}
