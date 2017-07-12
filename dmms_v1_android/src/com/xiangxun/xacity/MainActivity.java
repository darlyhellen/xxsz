package com.xiangxun.xacity;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.xiangxun.xacity.adapter.HomeAdapter;
import com.xiangxun.xacity.app.BaseFragmentActivity;
import com.xiangxun.xacity.bean.InfoCache;
import com.xiangxun.xacity.bean.LoginData.Menu;
import com.xiangxun.xacity.bean.ResponseResultBeans.LocationConfig;
import com.xiangxun.xacity.common.ConstantStatus;
import com.xiangxun.xacity.gis.LocationService;
import com.xiangxun.xacity.request.DcNetWorkUtils;
import com.xiangxun.xacity.ui.DeviceManageActivity;
import com.xiangxun.xacity.ui.MaterielManageActivity;
import com.xiangxun.xacity.ui.NoticeManagerActivity;
import com.xiangxun.xacity.ui.PatrolManageActivity;
import com.xiangxun.xacity.ui.RoadManageActivity;
import com.xiangxun.xacity.ui.setting.SettingActivity;
import com.xiangxun.xacity.utils.Logger;
import com.xiangxun.xacity.utils.ShareDataUtils;
import com.xiangxun.xacity.utils.SharedPreferencesKeys;
import com.xiangxun.xacity.utils.Tools;
import com.xiangxun.xacity.view.MsgDialog;
import com.xiangxun.xacity.view.MsgToast;
import com.xiangxun.xacity.view.TitleView;
import com.xiangxun.xacity.view.UpdateDialog;

public class MainActivity extends BaseFragmentActivity implements OnItemClickListener {
	private TitleView titleView;
	private GridView gv_home;
	private Map<String, Integer> map = new HashMap<String, Integer>();
	private HomeAdapter adapter;
	/** 更新版本提示框 */
	private UpdateDialog updateDialog;// 更新版本提示框
	private ExitReceiver exitReceiver;
	private MsgDialog msgDialog = null;
	private boolean isLocation = false;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		private MsgDialog mUpdateDialog;

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ConstantStatus.LocationConfigSuccess:
				if (Tools.isServiceRunning(MainActivity.this, "com.xiangxun.xacity.gis.LocationService")) {
					stopService(new Intent(MainActivity.this, LocationService.class));
				}
				LocationConfig locationConfig = (LocationConfig) msg.obj;
				int current = (int) Float.parseFloat(locationConfig.locateSpace);
				int locateSpaceUnit = current * 1000 * 60;
				ShareDataUtils.setSharedIntData(MainActivity.this, SharedPreferencesKeys.LOCATESPACEUNIT, locateSpaceUnit);
				startService(new Intent(MainActivity.this, LocationService.class));
				Logger.i(locationConfig.toString());
				break;
			case ConstantStatus.LocationConfigFailed:
				if (!isLocation) {
					isLocation = true;
					if (msgDialog == null) {
						msgDialog = new MsgDialog(MainActivity.this);
						msgDialog.setTiele("提示");
						msgDialog.setMsg("获取定位设置参数失败,是否重新获取...");
						msgDialog.setButLeftListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								msgDialog.dismiss();

							}
						});
						msgDialog.setButRightListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								DcNetWorkUtils.locationConfig(MainActivity.this, handler, account, password);

							}
						});
					}
					msgDialog.show();
				}
				break;
			case ConstantStatus.NetWorkError:
				break;
			case ConstantStatus.updateSuccess:// 获取版本更新数据成功
				String arrayStr = InfoCache.getInstance().getmData().getRemark();
				if (arrayStr == null || arrayStr.length() <= 0) {
					arrayStr = "发现新版本, 请更新~~~";
				}
				if (updateDialog == null) {
					updateDialog = new UpdateDialog(MainActivity.this, R.style.updateDialog, InfoCache.getInstance().getmData().getVersionName(), arrayStr, InfoCache.getInstance().getmData().getDownloadUrl().trim(),true);
				}
				updateDialog.setCancelable(InfoCache.getInstance().getmData().getForce().equals("0"));
				updateDialog.show();
				break;
			case ConstantStatus.updateFalse:// 版本更新
				if (mUpdateDialog == null) {
					mUpdateDialog = new MsgDialog(MainActivity.this);
					mUpdateDialog.setTiele(Html.fromHtml(getText(R.string.update_tips_html).toString()));
					mUpdateDialog.setMsg(getString(R.string.latest_version_please_look));
					mUpdateDialog.setOnlyOneBut();
				}
				mUpdateDialog.show();
				break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		initData();
		initListener();
	}

	@Override
	public void initView() {
		titleView = (TitleView) findViewById(R.id.comment_title);
		gv_home = (GridView) findViewById(R.id.gv_home);
	}

	@Override
	public void initData() {
		titleView.setTitle(R.string.app_title_name);
		map.put("通知公告", R.drawable.btn_selecter_notice);
		map.put("设备管理", R.drawable.btn_selecter_device);
		map.put("物资管理", R.drawable.btn_selecter_materiel);
		map.put("视频管理", R.drawable.btn_selecter_video);
		map.put("道路挖占", R.drawable.btn_selecter_road);
		map.put("巡视系统", R.drawable.btn_selecter_patrol);
		titleView.setRightBackgroundResource(R.drawable.set);
		exitReceiver = new ExitReceiver();
		@SuppressWarnings("unchecked")
		List<Menu> menus = (List<Menu>) ShareDataUtils.getObject(this, "home_menu");
		if (menus != null && menus.size() > 0) {
			adapter = new HomeAdapter(this, menus, map);
			gv_home.setAdapter(adapter);
		}
		IntentFilter filter = new IntentFilter();
		filter.addAction("finishApp");
		registerReceiver(exitReceiver, filter);
		// 判断今天是否已经取消了版本更新，如果有，则今天不进行请求提示
		// 第一次更新弹出，间隔一周在弹出。
		if (Calendar.getInstance().get(Calendar.DAY_OF_YEAR) >= ShareDataUtils.getSharedIntData(this, SharedPreferencesKeys.NOTUPDATE)) {
			DcNetWorkUtils.getVersion(false, this, handler, account, password);
		}
		DcNetWorkUtils.locationConfig(this, handler, account, password);
	}

	@Override
	public void initListener() {
		gv_home.setOnItemClickListener(this);
		titleView.setRightOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this, SettingActivity.class));
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Menu menu = (Menu) adapter.getItem(position);
		if ("通知公告".equals(menu.getName())) {
			startActivity(new Intent(this, NoticeManagerActivity.class));
		} else if ("设备管理".equals(menu.getName())) {
			startActivity(new Intent(this, DeviceManageActivity.class));
		} else if ("物资管理".equals(menu.getName())) {
			startActivity(new Intent(this, MaterielManageActivity.class));
		} else if ("视频管理".equals(menu.getName())) {
			startActivity(new Intent(this, DpsdkCoreActivity.class));
		} else if ("道路挖占".equals(menu.getName())) {
			startActivity(new Intent(this, RoadManageActivity.class));
		} else if ("巡视系统".equals(menu.getName())) {
			startActivity(new Intent(this, PatrolManageActivity.class));
		}
	}

	private class ExitReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			finish();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// onBackPressed();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onBackPressed() {
		if ((System.currentTimeMillis() - exitTime) > 2000) {
			MsgToast.geToast().setMsg("再按一次退出程序");
			exitTime = System.currentTimeMillis();
		} else {
			finish();
		}
	}

	private long exitTime = 0;
}
