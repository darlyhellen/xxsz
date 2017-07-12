package com.xiangxun.xacity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.dh.DpsdkCore.IDpsdkCore;
import com.dh.DpsdkCore.Login_Info_t;
import com.dh.DpsdkCore.Return_Value_Info_t;
import com.dh.DpsdkCore.fDPSDKStatusCallback;
import com.xiangxun.xacity.app.BaseActivity;
import com.xiangxun.xacity.ui.VideoManageActivity;
import com.xiangxun.xacity.view.LoadDialog;
import com.xiangxun.xacity.view.MsgToast;

public class DpsdkCoreActivity extends BaseActivity {
	static IDpsdkCore dpsdkcore = new IDpsdkCore();
	// 标记是否第一次登入
	static long m_loginHandle = 0;
	static int m_nLastError = 0;
	static Return_Value_Info_t m_ReValue = new Return_Value_Info_t();
	private String szIp, nPort, szUsername, szPassword;
	private LoadDialog loadDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_activity);
		initView();
		initData();
		initListener();
	}

	@Override
	public void initView() {
	}

	@Override
	public void initData() {
		int nType = 1;
		m_nLastError = IDpsdkCore.DPSDK_Create(nType, m_ReValue);
		IDpsdkCore.DPSDK_SetDPSDKStatusCallback(m_ReValue.nReturnValue, new fDPSDKStatusCallback() {
			@Override
			public void invoke(int nPDLLHandle, int nStatus) {
			}
		});
		szIp = "124.114.151.2";
		nPort = "9000";
		szUsername = "1";
		szPassword = "1";
		if (loadDialog == null) {
			loadDialog = new LoadDialog(this);
		}
		loadDialog.setTitle("正在登录");
		loadDialog.show();
		new LoginTask2().execute();
	}

	@Override
	public void initListener() {
	}

	class LoginTask2 extends AsyncTask<Void, Integer, Integer> {

		@Override
		protected Integer doInBackground(Void... arg0) { // 在此处处理UI会导致异常
			if (m_loginHandle != 0) {
				IDpsdkCore.DPSDK_Logout(m_ReValue.nReturnValue, 30000);
				m_loginHandle = 0;
			}
			Login_Info_t loginInfo = new Login_Info_t();
			loginInfo.szIp = szIp.getBytes();
			loginInfo.nPort = Integer.parseInt(nPort);
			loginInfo.szUsername = szUsername.getBytes();
			loginInfo.szPassword = szPassword.getBytes();
			loginInfo.nProtocol = 2;
			int nRet = IDpsdkCore.DPSDK_Login(m_ReValue.nReturnValue, loginInfo, 30000);
			return nRet;
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			loadDialog.dismiss();
			if (result == 0) {
				IDpsdkCore.DPSDK_SetCompressType(m_ReValue.nReturnValue, 0);
				m_loginHandle = 1;
				jumpToListFragment();
			} else {
				MsgToast.geToast().setMsg("登录视频服务失败，异常代码: " + result);
				m_loginHandle = 0;
				finish();
			}
		}
	}

	static public int getDpsdkHandle() {
		if (m_loginHandle == 1)
			return m_ReValue.nReturnValue;
		else
			return 0;
	}

	public void jumpToListFragment() {
		Intent intent = new Intent();
		intent.setClass(this, VideoManageActivity.class);
		startActivityForResult(intent, 2);
	}

	public void Logout() {
		IDpsdkCore.DPSDK_Logout(m_ReValue.nReturnValue, 30000);
		m_loginHandle = 0;
	}

	@Override
	protected void onDestroy() {
		IDpsdkCore.DPSDK_Destroy(m_ReValue.nReturnValue);
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			Logout();
			finish();
		}
	}

}