package com.xiangxun.xacity.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.xiangxun.xacity.R;
import com.xiangxun.xacity.app.BaseActivity;
import com.xiangxun.xacity.bean.ResponseResultBeans.NoticeBean;
import com.xiangxun.xacity.common.ConstantStatus;
import com.xiangxun.xacity.request.DcNetWorkUtils;
import com.xiangxun.xacity.view.MsgToast;
import com.xiangxun.xacity.view.TitleView;

/**
 * @package: com.xiangxun.xacity.ui
 * @ClassName: NoticeDetialActivity.java
 * @Description:
 * @author: HanGJ
 * @date: 2016-1-18 下午5:00:24
 */
public class NoticeDetialActivity extends BaseActivity {
	private TitleView titleView;
	private TextView nitice_title;
	private TextView nitice_user;
	private TextView nitice_date;
	private TextView nitice_content;
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ConstantStatus.getNoticeBeanListSuccess:
				NoticeBean noticeBean = (NoticeBean) msg.obj;
				if (noticeBean != null) {
					nitice_title.setText(noticeBean.noticeTitle);
					nitice_user.setText(String.format(getString(R.string.nitice_user), noticeBean.releaseUserId));
					nitice_date.setText(String.format(getString(R.string.nitice_date), noticeBean.noticeBegin));
					nitice_content.setText(Html.fromHtml(noticeBean.noticeContent).toString());
				}
				break;
			case ConstantStatus.getNoticeBeanListFailed:
				MsgToast.geToast().setMsg("数据获取失败");
				break;
			case ConstantStatus.NetWorkError:
				MsgToast.geToast().setMsg("数据获取失败");
				break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notice_detial_layout);
		initView();
		initData();
		initListener();
	}

	@Override
	public void initView() {
		titleView = (TitleView) findViewById(R.id.comment_title);
		nitice_title = (TextView) findViewById(R.id.mine_nitice_title);
		nitice_user = (TextView) findViewById(R.id.mine_nitice_user);
		nitice_date = (TextView) findViewById(R.id.mine_nitice_date);
		nitice_content = (TextView) findViewById(R.id.mine_nitice_content);
	}

	@Override
	public void initData() {
		titleView.setTitle("公告详情");
		NoticeBean item = (NoticeBean) getIntent().getSerializableExtra("noticeBean");
		if (item != null) {
			getNoticeDetail(item.noticeId);
		}
	}

	private void getNoticeDetail(String noteId) {
		DcNetWorkUtils.getNoticeDetail(this, account, password, noteId, handler);
	}

	@Override
	public void initListener() {
		titleView.setLeftBackOneListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}

}
