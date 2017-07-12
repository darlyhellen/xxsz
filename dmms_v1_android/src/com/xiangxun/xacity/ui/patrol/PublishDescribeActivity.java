package com.xiangxun.xacity.ui.patrol;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.xiangxun.xacity.R;
import com.xiangxun.xacity.app.BaseActivity;
import com.xiangxun.xacity.utils.MyUtils;
import com.xiangxun.xacity.view.MsgDialog;
import com.xiangxun.xacity.view.MsgToast;
import com.xiangxun.xacity.view.TitleView;

/**
 * @package: com.xiangxun.xacity.ui.patrol
 * @ClassName: PublishDescribeActivity.java
 * @Description: 描述页面
 * @author: HanGJ
 * @date: 2016-2-18 下午3:33:04
 */
public class PublishDescribeActivity extends BaseActivity implements OnClickListener {
	private TitleView titleView;
	private TextView mTvPubCount;
	private TextView mTvPubClear;
	private EditText mEdtPubDes;
	private MsgDialog mDialog;
	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activiyt_publish_describe);
		initView();
		intent = getIntent();
		initData();
		initListener();
	}

	@Override
	public void initView() {
		titleView = (TitleView) findViewById(R.id.comment_title);
		mTvPubCount = (TextView) findViewById(R.id.tv_publish_count);
		mTvPubClear = (TextView) findViewById(R.id.tv_publish_clear);
		mEdtPubDes = (EditText) findViewById(R.id.edit_publish_des);
	}

	@Override
	public void initData() {
		String title = getIntent().getStringExtra("title");
		if(TextUtils.isEmpty(title)){
			titleView.setTitle("工单内容");
		} else {
			titleView.setTitle(title);
		}
		mEdtPubDes.setText(intent.getStringExtra("already_description"));
		mEdtPubDes.setSelection(mEdtPubDes.getText().toString().length());
		titleView.getTitleViewOperationText().setText("完成");
		MyUtils.lengthFilterDesc(mEdtPubDes, mTvPubCount, 400, "");
	}

	@Override
	public void initListener() {
		mTvPubClear.setOnClickListener(this);
		titleView.setRightImageTextFlipper(this);
		titleView.setLeftBackOneListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_publish_clear:
			confirmationDia();
			break;
		case R.id.dia_tv_but2:// 提示框左按钮
			mEdtPubDes.getText().clear();
			mDialog.dismiss();
			break;
		case R.id.title_view_right_Flipper01:
			String content = mEdtPubDes.getText().toString().trim();
			if (content.length() < 5) {
				MsgToast.geToast().setMsg("描述/备注不能小于5个字");
				return;
			}
			Intent intent = new Intent();
			intent.putExtra("description", mEdtPubDes.getText().toString());
			setResult(Activity.RESULT_OK, intent);
			finish();
			break;
		}
	}

	/**
	 * @Title:
	 * @Description:删除确认框
	 * @param:
	 * @return: void
	 * @throws
	 */
	private void confirmationDia() {
		if (mEdtPubDes.getText().toString().trim().length() == 0) {
			return;
		}
		if (mDialog == null) {
			mDialog = new MsgDialog(this);
			mDialog.setTiele("提示");
			mDialog.setMsg("是否要清空全部内容？");
			mDialog.setButRightListener(this);

		}
		mDialog.show();
	}

}
