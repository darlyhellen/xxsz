package com.xiangxun.xacity.ui.setting;

import java.io.File;
import java.text.DecimalFormat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiangxun.xacity.LoginActivity;
import com.xiangxun.xacity.R;
import com.xiangxun.xacity.app.BaseActivity;
import com.xiangxun.xacity.bean.InfoCache;
import com.xiangxun.xacity.common.ConstantStatus;
import com.xiangxun.xacity.common.DcHttpClient;
import com.xiangxun.xacity.gis.LocationService;
import com.xiangxun.xacity.request.Api;
import com.xiangxun.xacity.request.DcNetWorkUtils;
import com.xiangxun.xacity.utils.ShareDataUtils;
import com.xiangxun.xacity.utils.Tools;
import com.xiangxun.xacity.view.MsgDialog;
import com.xiangxun.xacity.view.MsgToast;
import com.xiangxun.xacity.view.TitleView;
import com.xiangxun.xacity.view.UpdateDialog;

public class SettingActivity extends BaseActivity implements OnClickListener {
	private TitleView titleView;
	/** 缓存大小 */
	private TextView mTvCache;
	/** 缓存Layout */
	private RelativeLayout mRlCache;
	/** 退出登录 */
	private RelativeLayout mBtnExit;
	/** 版本更新Layout */
	private RelativeLayout mRlUpdate;
	/** 版本号 */
	private TextView mTvUpdate;
	/** 更新版本提示框 */
	private UpdateDialog updateDialog;// 更新版本提示框
	/** 提示框 */
	private MsgDialog mDialog;// 提示框
	/** 缓存文件框 */
	private File imageCacheFile;// 缓存文件
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		private MsgDialog mUpdateDialog;

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ConstantStatus.updateSuccess:// 获取版本更新数据成功
				String arrayStr = InfoCache.getInstance().getmData().getRemark();
				if (arrayStr == null || arrayStr.length() <= 0) {
					arrayStr = "发现新版本, 请更新~~~";
				}
				if (updateDialog == null) {
					updateDialog = new UpdateDialog(SettingActivity.this, R.style.updateDialog, InfoCache.getInstance().getmData().getVersionName(), arrayStr, InfoCache.getInstance().getmData().getDownloadUrl().trim(), false);
				}
				updateDialog.setCancelable(InfoCache.getInstance().getmData().getForce().equals("0"));
				updateDialog.show();
				showNew();
				break;
			case ConstantStatus.updateFalse:// 版本更新
				if (mUpdateDialog == null) {
					mUpdateDialog = new MsgDialog(SettingActivity.this);
					mUpdateDialog.setTiele(Html.fromHtml(getText(R.string.update_tips_html).toString()));
					mUpdateDialog.setMsg(getString(R.string.latest_version_please_look));
					mUpdateDialog.setOnlyOneBut();
				}
				mUpdateDialog.show();
				break;
			case ConstantStatus.getCacheSize:// 获取缓存数据成功
				DecimalFormat df = new DecimalFormat("0.00");
				mTvCache.setText(new StringBuffer(df.format((Long) msg.obj / (1024.00 * 1024.00))).append("M"));
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_layout);
		initView();
		initData();
		initListener();
	}

	@Override
	public void initView() {
		titleView = (TitleView) findViewById(R.id.comment_title);
		mRlUpdate = (RelativeLayout) this.findViewById(R.id.rl_more_update);
		mTvUpdate = (TextView) this.findViewById(R.id.tv_more_version);
		mBtnExit = (RelativeLayout) this.findViewById(R.id.btn_more_exit);
		mRlCache = (RelativeLayout) this.findViewById(R.id.rl_more_clear);
		mTvCache = (TextView) this.findViewById(R.id.tv_more_cache);
	}

	@Override
	public void initData() {
		titleView.setTitle("设置");
		mTvUpdate.setText(new StringBuilder(getString(R.string.curr_ver)).append(InfoCache.getInstance().getAppVersionName(this)));
		// DcNetWorkUtils.getVersion(false, this, this.handler, super.account,
		// super.password);
		showNew();
		new Thread(new GetCacheSize()).start();
	}

	@Override
	public void initListener() {
		mRlUpdate.setOnClickListener(this);
		mBtnExit.setOnClickListener(this);
		mRlCache.setOnClickListener(this);
		titleView.setLeftBackOneListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}

	private void showNew() {
		if (InfoCache.getInstance().isNewVer()) {
			mTvUpdate.setText(null);
			mTvUpdate.setBackgroundResource(R.drawable.more_update);
		} else {
			mTvUpdate.setText(new StringBuilder(getString(R.string.curr_ver)).append(InfoCache.getInstance().getAppVersionName(this)));
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_more_exit:// 退出登录
			showExitLogin();
			break;
		case R.id.rl_more_update:// 版本更新:
			MsgToast.geToast().setMsg(R.string.updating);
			DcNetWorkUtils.getVersion(true, this, handler, account, password);
			break;
		case R.id.rl_more_clear:// 清理缓存
			if (mTvCache.getText().toString().equals("0.00M")) {
				MsgToast.geToast().setMsg(R.string.no_clear);
				return;
			}
			mDialog = new MsgDialog(this);
			mDialog.setTiele(getResources().getString(R.string.clear_com));
			mDialog.setButRightListener(this);
			mDialog.show();
			break;
		case R.id.dia_tv_but2:// 确定 清楚缓存
			mTvCache.setText("0.00M");
			Tools.deleteFile(imageCacheFile);
			DcHttpClient.getInstance().cleanDiskCache();
			MsgToast.geToast().setMsg(R.string.cache_realy_cleat);
			mDialog.dismiss();
			break;
		}
	}

	/**
	 * @Title:
	 * @Description:
	 * @param:
	 * @return: void
	 * @throws
	 */

	private void showExitLogin() {
		mDialog = new MsgDialog(this);
		mDialog.setTiele(getString(R.string.exit_login));
		mDialog.setButRightListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mDialog.dismiss();
				InfoCache.getInstance().setUserData(null);
				ShareDataUtils.clearn(SettingActivity.this);
				stopService(new Intent(SettingActivity.this, LocationService.class));
				mBtnExit.setVisibility(View.GONE);
				overridePendingTransition(R.anim.right_in, R.anim.activity_nothing);
				Intent intent = new Intent("finishApp");
				sendBroadcast(intent);
				startActivity(new Intent(SettingActivity.this, LoginActivity.class));
				finish();
			}
		});
		mDialog.show();
	}

	private final class GetCacheSize implements Runnable {
		public void run() {
			imageCacheFile = new File(Api.XaCity);
			if (imageCacheFile.exists()) {
				try {
					Message.obtain(handler, ConstantStatus.getCacheSize, Tools.getFileSize(imageCacheFile)).sendToTarget();
				} catch (Exception e) {
					Message.obtain(handler, ConstantStatus.getCacheSize, 0L).sendToTarget();
				}
			} else {
				Message.obtain(handler, ConstantStatus.getCacheSize, 0L).sendToTarget();
			}
		}
	}

}
