package com.xiangxun.xacity.ui.video;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;

import com.company.PlaySDK.IPlaySDK;
import com.dh.DpsdkCore.Get_RecordStream_File_Info_t;
import com.dh.DpsdkCore.Get_RecordStream_Time_Info_t;
import com.dh.DpsdkCore.IDpsdkCore;
import com.dh.DpsdkCore.Query_Record_Info_t;
import com.dh.DpsdkCore.Record_Info_t;
import com.dh.DpsdkCore.Return_Value_Info_t;
import com.dh.DpsdkCore.fMediaDataCallback;
import com.xiangxun.xacity.R;
import com.xiangxun.xacity.DpsdkCoreActivity;
import com.xiangxun.xacity.app.BaseActivity;
import com.xiangxun.xacity.player.Err;
import com.xiangxun.xacity.utils.Logger;
import com.xiangxun.xacity.view.PlayBackControlBar;
import com.xiangxun.xacity.view.RemoteControlBar;
import com.xiangxun.xacity.view.RemotePortaitControlBar;
import com.xiangxun.xacity.view.TitleView;
/**
 * @package: com.xiangxun.xacity.ui.video
 * @ClassName: BackPlayActivity.java
 * @Description: 视频回放页面
 * @author: HanGJ
 * @date: 2016-2-1 下午3:46:13
 */
public class BackPlayActivity extends BaseActivity {
	private TitleView titleView;
	// 根view
	private LinearLayout mRootView;
	private Button bt_query_video;
	private Button bt_playback_file;
	private Button bt_playback_time;
	private Button bt_playback_close;
	// 抓图按钮
	private Button btnCaptureImg;
	public final static String IMAGE_PATH = Environment.getExternalStorageDirectory().getPath() + "/snapshot/";
	public final static String PHOTO_END = ".jpg";

	private TextView chooseStartTxt;
	private TextView chooseEndTxt;
	private Button chooseStartBtn;
	private Button chooseEndBtn;
	private DatePicker dp;
	private TimePicker tp;
	private Calendar startCalendar;
	private Calendar endCalendar;

	private Get_RecordStream_File_Info_t m_RecordFileInfo = new Get_RecordStream_File_Info_t();
	private Return_Value_Info_t m_nPlaybackSeq = new Return_Value_Info_t();
	private SurfaceView m_svPlayer = null;
	private int m_nRecordSource = 3;
	private int m_pDLLHandle = 0;
	private int m_nPort = 0;

	// 媒体数据回调
	private fMediaDataCallback fm;
	private String channelId;
	private static final int PicFormat_JPEG = 1;
	private RemotePortaitControlBar timeBar;
	// private Button btnSeekPlay;
	private PlayBackControlBar mPortCtrl;
	private Record_Info_t records;
	// 保存时间控件临时变量
	private Calendar tempCalendar;
	private int nTimeOut = 30 * 1000;
	private Query_Record_Info_t queryRecordInfo;
	private static final int STATE_STOP = 1;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case STATE_STOP:
				Calendar seekCalendar = Calendar.getInstance();
				Calendar tempCalendar = Calendar.getInstance();
				if (RemoteControlBar.scrollStopCalendar == null) {
					seekCalendar = startCalendar;
					Logger.i("RemoteControlBar.scrollStopCalendar is null");
				} else {
					seekCalendar = RemoteControlBar.scrollStopCalendar;
					Logger.i("hour" + seekCalendar.get(Calendar.HOUR_OF_DAY) + "minute" + seekCalendar.get(Calendar.MINUTE) + "second" + seekCalendar.get(Calendar.SECOND));

					tempCalendar.set(startCalendar.get(Calendar.YEAR), startCalendar.get(Calendar.MONTH), startCalendar.get(Calendar.DAY_OF_MONTH), seekCalendar.get(Calendar.HOUR_OF_DAY), seekCalendar.get(Calendar.MINUTE), seekCalendar.get(Calendar.SECOND));
				}
				int videoNum = getVideoNumByTime(tempCalendar);
				Logger.i("videoNum" + videoNum);
				try {
					m_RecordFileInfo.szCameraId = channelId.getBytes("utf-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				m_RecordFileInfo.nFileIndex = records.pSingleRecord[videoNum].nFileIndex;
				m_RecordFileInfo.nMode = 1;
				m_RecordFileInfo.nRight = 0;
				m_RecordFileInfo.uBeginTime = records.pSingleRecord[videoNum].uBeginTime;
				m_RecordFileInfo.uEndTime = records.pSingleRecord[videoNum].uEndTime;
				playBackByFile();
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_back_play_layout);
		initView();
		initData();
		initListener();
	}

	@Override
	public void initView() {
		titleView = (TitleView) findViewById(R.id.comment_title);
		mRootView = (LinearLayout) findViewById(R.id.main_fragment_playback);
		timeBar = (RemotePortaitControlBar) findViewById(R.id.portait_control);
		bt_query_video = (Button) findViewById(R.id.bt_query_video);
		bt_playback_file = (Button) findViewById(R.id.bt_playback_by_file);
		bt_playback_time = (Button) findViewById(R.id.bt_playback_by_time);
		bt_playback_close = (Button) findViewById(R.id.bt_close_playback);
		// 选择时间
		chooseEndTxt = (TextView) findViewById(R.id.choose_end_time_txt);
		chooseStartTxt = (TextView) findViewById(R.id.choose_start_time_txt);
		chooseEndBtn = (Button) findViewById(R.id.choose_end_time_btn);
		chooseStartBtn = (Button) findViewById(R.id.choose_start_time_btn);
		m_svPlayer = (SurfaceView) findViewById(R.id.sv_player);
		btnCaptureImg = (Button) findViewById(R.id.capture_bitmap);
	}

	@Override
	public void initData() {
		startCalendar = Calendar.getInstance();
		endCalendar = Calendar.getInstance();
		tempCalendar = Calendar.getInstance();
		m_pDLLHandle = DpsdkCoreActivity.getDpsdkHandle();
		titleView.setTitle(getIntent().getStringExtra("channelName"));
		channelId = getIntent().getStringExtra("channelId");
		m_nPort = IPlaySDK.PLAYGetFreePort();
		SurfaceHolder holder = m_svPlayer.getHolder();
		holder.addCallback(new Callback() {
			public void surfaceCreated(SurfaceHolder holder) {
				IPlaySDK.InitSurface(m_nPort, m_svPlayer);
			}

			public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
			}

			public void surfaceDestroyed(SurfaceHolder holder) {
			}
		});
		fm = new fMediaDataCallback() {

			@Override
			public void invoke(int nPDLLHandle, int nSeq, int nMediaType, byte[] szNodeId, int nParamVal, byte[] szData, int nDataLen) {
				IPlaySDK.PLAYInputData(m_nPort, szData, nDataLen);
			}
		};
		setTextDate();
	}

	@Override
	public void initListener() {
		titleView.setLeftBackOneListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		// 选择日期控件
		chooseStartBtn.setOnClickListener(new chooseTimeListener());
		chooseEndBtn.setOnClickListener(new chooseTimeListener());
		btnCaptureImg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				captureBitmap();
			}
		});

		bt_query_video.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				queryVideo(startCalendar);
			}
		});

		bt_playback_file.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				playBackByFile();

			}
		});

		bt_playback_time.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				playBackByTime(startCalendar);
			}
		});

		bt_playback_close.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				closeVideo();
			}
		});
	}

	private void setTextDate() {
		Calendar c = Calendar.getInstance();
		chooseStartTxt.setText(String.valueOf(c.get(Calendar.YEAR)) + "年" + String.valueOf(c.get(Calendar.MONTH) + 1) + "月" + String.valueOf(c.get(Calendar.DAY_OF_MONTH)) + "日" + "00" + "时" + "00" + "分");
		chooseEndTxt.setText(String.valueOf(c.get(Calendar.YEAR)) + "年" + String.valueOf(c.get(Calendar.MONTH) + 1) + "月" + String.valueOf(c.get(Calendar.DAY_OF_MONTH)) + "日" + "23" + "时" + "59" + "分");
		startCalendar.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		endCalendar.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), 23, 59, 0);

	}

	class chooseTimeListener implements OnClickListener {

		@Override
		public void onClick(final View v) {

			LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final View vPopWindow = inflater.inflate(R.layout.popwindow_choose_time, null, false);
			final PopupWindow popWindow = new PopupWindow(vPopWindow, android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT);
			dp = (DatePicker) vPopWindow.findViewById(R.id.datePicker);
			tp = (TimePicker) vPopWindow.findViewById(R.id.timePicker);
			dp.setMaxDate(tempCalendar.getTimeInMillis());
			vPopWindow.findViewById(R.id.finish_btn).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {

					String year = String.valueOf(dp.getYear());
					String month = String.valueOf(dp.getMonth() + 1);
					String dayofmonth = String.valueOf(dp.getDayOfMonth());
					String hour = String.valueOf(tp.getCurrentHour());
					String minute = String.valueOf(tp.getCurrentMinute());
					// 判断点击的是开始还是结束按钮
					if (v.getId() == chooseStartBtn.getId()) {
						chooseStartTxt.setText(year + "年" + month + "月" + dayofmonth + "日" + hour + "时" + minute + "分");
						startCalendar.set(dp.getYear(), dp.getMonth(), dp.getDayOfMonth(), tp.getCurrentHour(), tp.getCurrentMinute(), 0);
					} else {
						chooseEndTxt.setText(year + "年" + month + "月" + dayofmonth + "日" + hour + "时" + minute + "分");
						endCalendar.set(dp.getYear(), dp.getMonth(), dp.getDayOfMonth(), tp.getCurrentHour(), tp.getCurrentMinute(), 0);
					}
					tempCalendar.set(dp.getYear(), dp.getMonth(), dp.getDayOfMonth(), tp.getCurrentHour(), tp.getCurrentMinute(), 0);
					popWindow.dismiss();
				}
			});
			popWindow.showAtLocation(mRootView, Gravity.CENTER, 0, 0);
		}

	}

	private void closeVideo() {
		int nPlaybackReq = m_nPlaybackSeq.nReturnValue;
		int ret = IDpsdkCore.DPSDK_CloseRecordStreamBySeq(m_pDLLHandle, nPlaybackReq, nTimeOut);
		if (ret == 0) {
			Logger.e("DPSDK_CloseRecordStreamBySeq success!");
		} else {
			Logger.e("DPSDK_CloseRecordStreamBySeq failed!");
		}
		StopRealPlay();
	}

	private int getVideoNumByTime(Calendar calendar) {
		int num = 0;
		for (int i = 0; i < records.nCount; i++) {
			long beginTime = records.pSingleRecord[i].uBeginTime * 1000;
			long endTime = records.pSingleRecord[i].uEndTime * 1000;

			Calendar cBeCalendar = Calendar.getInstance();
			cBeCalendar.setTimeInMillis(beginTime);
			Logger.i("cBeCalendar" + cBeCalendar.get(Calendar.YEAR) + cBeCalendar.get(Calendar.MONTH) + cBeCalendar.get(Calendar.DAY_OF_MONTH) + "hour" + cBeCalendar.get(Calendar.HOUR_OF_DAY) + "minute" + cBeCalendar.get(Calendar.MINUTE) + "second" + cBeCalendar.get(Calendar.SECOND));
			Calendar cEndCalendar = Calendar.getInstance();
			cEndCalendar.setTimeInMillis(endTime);
			Logger.i("cEndCalendar" + cEndCalendar.get(Calendar.YEAR) + cEndCalendar.get(Calendar.MONTH) + cEndCalendar.get(Calendar.DAY_OF_MONTH) + "hour" + cEndCalendar.get(Calendar.HOUR_OF_DAY) + "minute" + cEndCalendar.get(Calendar.MINUTE) + "second" + cEndCalendar.get(Calendar.SECOND));
			if (beginTime < calendar.getTimeInMillis() && calendar.getTimeInMillis() < endTime) {
				num = i;
			}
		}
		Logger.i("tempCalendar" + calendar.get(Calendar.YEAR) + calendar.get(Calendar.MONTH) + calendar.get(Calendar.DAY_OF_MONTH) + "hour" + calendar.get(Calendar.HOUR_OF_DAY) + "minute" + calendar.get(Calendar.MINUTE) + "second" + calendar.get(Calendar.SECOND) + "time in millis" + calendar.getTimeInMillis());
		return num;
	}

	private void queryVideo(Calendar startCalendar) {
		byte[] szCameraId = null;
		try {
			szCameraId = channelId.getBytes("utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		int nRet = 0;
		Return_Value_Info_t nRecordCount = new Return_Value_Info_t();
		queryRecordInfo = new Query_Record_Info_t();
		System.arraycopy(szCameraId, 0, queryRecordInfo.szCameraId, 0, szCameraId.length);
		queryRecordInfo.nRight = 0;
		queryRecordInfo.nRecordType = 0;
		queryRecordInfo.nSource = m_nRecordSource;
		if (startCalendar != null) {
			queryRecordInfo.uBeginTime = startCalendar.getTimeInMillis() / 1000;
		}
		if (endCalendar != null) {
			queryRecordInfo.uEndTime = endCalendar.getTimeInMillis() / 1000;
		}
		// IndexOutOfBoundsException 需判断下起止时间
		if (startCalendar.compareTo(endCalendar) == 1) {
			Log.e("BackPlayActivity", "please reset time");
		} else {
			nRet = IDpsdkCore.DPSDK_QueryRecord(m_pDLLHandle, queryRecordInfo, nRecordCount, nTimeOut);
			int nCount = nRecordCount.nReturnValue;
			if (nRet == 0) {
				records = new Record_Info_t(nCount);
				try {
					records.szCameraId = channelId.getBytes("utf-8");
					m_RecordFileInfo.szCameraId = channelId.getBytes("utf-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				records.nBegin = 0;
				IDpsdkCore.DPSDK_GetRecordInfo(m_pDLLHandle, records);
				if (records.nCount > 0) {
					m_RecordFileInfo.nFileIndex = records.pSingleRecord[0].nFileIndex;
					m_RecordFileInfo.nMode = 1;
					m_RecordFileInfo.nRight = 0;
					m_RecordFileInfo.uBeginTime = records.pSingleRecord[0].uBeginTime;
					m_RecordFileInfo.uEndTime = records.pSingleRecord[0].uEndTime;
				}
				Logger.i("[startCalendar]" + startCalendar + "[startCalendar.minute]" + startCalendar.get(Calendar.MINUTE) + "[startCalendar.HOUR]" + startCalendar.get(Calendar.HOUR) + "[records.nCount]" + records.nCount + "[m_RecordFileInfo.nFileIndex]" + m_RecordFileInfo.nFileIndex);
			}
			// 设置有录像的时间段为绿色
			ArrayList<Pair<Calendar, Calendar>> timeArray = getTimeSlices(records);
			setTimeSlices(timeArray);
		}
	}

	public ArrayList<Pair<Calendar, Calendar>> getTimeSlices(Record_Info_t records) {
		if (records == null/* records.nRetCount */) {
			return null;
		}
		int retCount = records.nCount;// records.nRetCount;
		ArrayList<Pair<Calendar, Calendar>> result = new ArrayList<Pair<Calendar, Calendar>>();
		for (int i = 0; i < retCount; i++) {
			long begin = records.pSingleRecord[i].uBeginTime;
			long end = records.pSingleRecord[i].uEndTime;
			Calendar bc = Millis2Calendar(begin * 1000);
			Calendar ec = Millis2Calendar(end * 1000);
			result.add(new Pair<Calendar, Calendar>(bc, ec));
		}
		return result;
	}

	protected void setTimeSlices(ArrayList<Pair<Calendar, Calendar>> timeArray) {
		if (timeArray != null) {
			mPortCtrl.setTimeSlices(timeArray);
		}
	}

	/**
	 * 毫秒转日历
	 * 
	 * @param time
	 * @return
	 */
	private Calendar Millis2Calendar(long millis) {
		Calendar caledar = Calendar.getInstance();
		caledar.setTimeInMillis(millis);
		return caledar;
	}

	private void playBackByFile() {

		if (!StartRealPlay()) {
			Logger.e("StartRealPlay failed!");
			return;
		}
		// Return_Value_Info_t nPlaybackSeq = new Return_Value_Info_t();
		int nRet = IDpsdkCore.DPSDK_GetRecordStreamByFile(m_pDLLHandle, m_nPlaybackSeq, m_RecordFileInfo, fm, 20 * 1000);
		Logger.i("[m_pDLLHandle]" + m_pDLLHandle + "[m_nPlaybackSeq]" + m_nPlaybackSeq.nReturnValue + "[nRet]" + nRet);
		if (nRet == 0) {
			Logger.e("DPSDK_GetRecordStreamByFile success!");
		} else {
			Logger.e("DPSDK_GetRecordStreamByFile failed!");
		}
	}

	private void playBackByTime(Calendar calendar) {
		if (!StartRealPlay()) {
			Logger.e("StartRealPlay failed!");
			return;
		}
		int nRet = 0;
		Get_RecordStream_Time_Info_t getRecordStreamTimeInfo = new Get_RecordStream_Time_Info_t();
		try {
			getRecordStreamTimeInfo.szCameraId = channelId.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		getRecordStreamTimeInfo.nMode = 1;
		getRecordStreamTimeInfo.nRight = 0;
		getRecordStreamTimeInfo.nSource = m_nRecordSource;
		getRecordStreamTimeInfo.uBeginTime = calendar.getTimeInMillis() / 1000;
		getRecordStreamTimeInfo.uEndTime = endCalendar.getTimeInMillis() / 1000;
		nRet = IDpsdkCore.DPSDK_GetRecordStreamByTime(m_pDLLHandle, m_nPlaybackSeq, getRecordStreamTimeInfo, fm, 40 * 1000);
		if (nRet == 0) {
			Logger.e("DPSDK_GetRecordStreamByTime success!");
		} else {
			Logger.e("DPSDK_GetRecordStreamByTime failed!" + nRet);
		}
	}

	/**
	 * 截图
	 */
	private void captureBitmap() {
		String IMGSTR = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + PHOTO_END;
		// 先创建一个文件夹
		File dir = new File(IMAGE_PATH);
		File file = new File(IMAGE_PATH, IMGSTR);
		if (!dir.exists()) {
			dir.mkdir();
		} else {
			if (file.exists()) {
				file.delete();
			}
		}

		int result = IPlaySDK.PLAYCatchPicEx(m_nPort, IMAGE_PATH, PicFormat_JPEG);
		if (result == Err.OK) {
			// showToast(R.string.capture_success);
		} else {
			// showToast(R.string.capture_fail);
		}
	}

	public void StopRealPlay() {
		try {
			IPlaySDK.PLAYStopSoundShare(m_nPort);
			IPlaySDK.PLAYStop(m_nPort);
			IPlaySDK.PLAYCloseStream(m_nPort);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean StartRealPlay() {
		if (m_svPlayer == null)
			return false;

		boolean bOpenRet = IPlaySDK.PLAYOpenStream(m_nPort, null, 0, 1500 * 1024) == 0 ? false : true;
		if (bOpenRet) {
			boolean bPlayRet = IPlaySDK.PLAYPlay(m_nPort, m_svPlayer) == 0 ? false : true;
			if (bPlayRet) {
				boolean bSuccess = IPlaySDK.PLAYPlaySoundShare(m_nPort) == 0 ? false : true;
				if (!bSuccess) {
					IPlaySDK.PLAYStop(m_nPort);
					IPlaySDK.PLAYCloseStream(m_nPort);
					return false;
				}
			} else {
				IPlaySDK.PLAYCloseStream(m_nPort);
				return false;
			}
		} else {
			return false;
		}

		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		setTextDate();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		SharedPreferences sp = getSharedPreferences("LOGININFO", 0);
		Editor ed = sp.edit();
		ed.putString("ISFINISH", "false");
		ed.commit();
	}
}
