package com.xiangxun.xacity;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.xiangxun.xacity.app.BaseActivity;
import com.xiangxun.xacity.app.XiangXunApplication;
import com.xiangxun.xacity.bean.InfoCache;
import com.xiangxun.xacity.bean.LoginData;
import com.xiangxun.xacity.bean.LoginData.ChildrenRoot;
import com.xiangxun.xacity.bean.LoginData.Login;
import com.xiangxun.xacity.bean.LoginData.Menu;
import com.xiangxun.xacity.bean.LoginData.UserData;
import com.xiangxun.xacity.common.ConstantStatus;
import com.xiangxun.xacity.request.DcNetWorkUtils;
import com.xiangxun.xacity.utils.ShareDataUtils;
import com.xiangxun.xacity.utils.SharedPreferencesKeys;
import com.xiangxun.xacity.utils.Tools;
import com.xiangxun.xacity.view.ClearEditText;
import com.xiangxun.xacity.view.MsgToast;
import com.xiangxun.xacity.view.TitleView;
import com.xiangxun.xacity.view.XSubButton;

public class LoginActivity extends BaseActivity implements OnClickListener {
	private LoginActivity mContext;
	private TitleView titleView;
	private ClearEditText mEdtAcount = null;
	private ClearEditText mEdtPassWord = null;
	private XSubButton mBtnLogin = null;
	private String password;
	private String acount;
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			mBtnLogin.setNormal();
			switch (msg.what) {
			case ConstantStatus.loadSuccess:
				LoginData loginData = (LoginData) msg.obj;
				Login login = loginData.getLogin();
				if (login != null && "false".equals(login.getRes())) {
					MsgToast.geToast().setMsg("登录失败, 请检查用户名或者密码是否正确~");
					return;
				}
				MsgToast.geToast().setMsg("登录成功");
				if (loginData.getUser() != null && loginData.getUser().size() > 0) {
					UserData userData = loginData.getUser().get(0);
					InfoCache.getInstance().setUserData(userData);
					ShareDataUtils.setSharedStringData(mContext, SharedPreferencesKeys.USERNAME, acount);
					ShareDataUtils.setSharedStringData(mContext, SharedPreferencesKeys.PASSWORD, password);
					ShareDataUtils.setSharedStringData(mContext, SharedPreferencesKeys.USERID, userData.getId());
					ShareDataUtils.setSharedStringData(mContext, SharedPreferencesKeys.NAME, userData.getName());
					ShareDataUtils.setSharedStringData(mContext, SharedPreferencesKeys.DEPTID, userData.getDeptid());
					ShareDataUtils.setSharedStringData(mContext, SharedPreferencesKeys.DUTYORGID, userData.getDutyorgcode());
					ShareDataUtils.setSharedStringData(mContext, SharedPreferencesKeys.USERPHONE, userData.getMobile());
					ShareDataUtils.setSharedStringData(mContext, SharedPreferencesKeys.MOBILEROLES, userData.getMobileRoles());
					XiangXunApplication.getInstance().setIsFirstIn(Tools.getAppVersionName(LoginActivity.this));
					List<Menu> menus = loginData.getMenu();
					if (menus != null && menus.size() > 0) {
						for (int i = 0; i < menus.size(); i++) {
							Menu menu = menus.get(i);
							if (menu != null && "物资管理".equals(menu.getName())) {
								List<ChildrenRoot> children = menu.getChildren();
								if (children != null && children.size() > 0) {
									ShareDataUtils.saveObject(mContext, "menu_materiel", children);
								}
							} else if (menu != null && "设备管理".equals(menu.getName())) {
								List<ChildrenRoot> children = menu.getChildren();
								if (children != null && children.size() > 0) {
									ShareDataUtils.saveObject(mContext, "menu_device", children);
								}
							} else if (menu != null && "道路挖占".equals(menu.getName())) {
								List<ChildrenRoot> children = menu.getChildren();
								if (children != null && children.size() > 0) {
									ShareDataUtils.saveObject(mContext, "menu_occupy", children);
								}
							} else if (menu != null && "巡视系统".equals(menu.getName())) {
								List<ChildrenRoot> children = menu.getChildren();
								if (children != null && children.size() > 0) {
									ShareDataUtils.saveObject(mContext, "menu_patrol", children);
								}
							}
						}
						ShareDataUtils.saveObject(mContext, "home_menu", menus);
					}
					startActivity(new Intent(mContext, MainActivity.class));
					finish();
				} else {
					MsgToast.geToast().setMsg("登录失败, 请重新登录~");
				}
				break;
			case ConstantStatus.loadFailed:
				MsgToast.geToast().setMsg("登录失败");
				break;
			case ConstantStatus.NetWorkError:
				MsgToast.geToast().setMsg("数据异常");
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity_layout);
		this.mContext = this;
		initView();
		initData();
		initListener();
	}

	@Override
	public void initView() {
		titleView = (TitleView) findViewById(R.id.comment_title);
		mEdtAcount = (ClearEditText) findViewById(R.id.edt_user_acount);
		mEdtPassWord = (ClearEditText) findViewById(R.id.edt_login_password);
		mBtnLogin = (XSubButton) findViewById(R.id.btn_login);
	}

	@Override
	public void initData() {
		titleView.setTitle("登录");
		mBtnLogin.setViewInit(R.string.mine_login_login, R.string.mine_login_loginning, mEdtAcount, mEdtPassWord);
		String account = ShareDataUtils.getSharedStringData(LoginActivity.this, "loginName");
		String password = ShareDataUtils.getSharedStringData(LoginActivity.this, "password");
		mEdtAcount.setText(account);
		mEdtPassWord.setText(password);
	}

	@Override
	public void initListener() {
		mBtnLogin.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login:
			acount = mEdtAcount.getText().toString().trim();
			password = mEdtPassWord.getText().toString().trim();
			if (TextUtils.isEmpty(acount)) {
				MsgToast.geToast().setMsg("用户名不能为空~");
				mBtnLogin.setNormal();
				return;
			}
			if (TextUtils.isEmpty(password)) {
				MsgToast.geToast().setMsg("密码不能为空~");
				mBtnLogin.setNormal();
				return;
			}
			DcNetWorkUtils.login(this, acount, password, mHandler);
			// mHandler.sendEmptyMessageDelayed(ConstantStatus.loadSuccess,
			// 2000);
			break;
		}
	}

}
