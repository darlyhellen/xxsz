package com.xiangxun.xacity.ui.video;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.company.PlaySDK.IPlaySDK;
import com.dh.DpsdkCore.Enc_Channel_Info_Ex_t;
import com.dh.DpsdkCore.Get_RealStream_Info_t;
import com.dh.DpsdkCore.IDpsdkCore;
import com.dh.DpsdkCore.Ptz_Direct_Info_t;
import com.dh.DpsdkCore.Ptz_Operation_Info_t;
import com.dh.DpsdkCore.Return_Value_Info_t;
import com.dh.DpsdkCore.fMediaDataCallback;
import com.xiangxun.xacity.DpsdkCoreActivity;
import com.xiangxun.xacity.R;
import com.xiangxun.xacity.app.BaseActivity;
import com.xiangxun.xacity.utils.Logger;
import com.xiangxun.xacity.utils.Utils;
import com.xiangxun.xacity.view.MsgToast;
import com.xiangxun.xacity.view.TitleView;

/**
 * @package: com.xiangxun.xacity.ui.video
 * @ClassName: RealPlayActivity.java
 * @Description: 实施流播放页面
 * @author: HanGJ
 * @date: 2016-2-1 上午10:46:38
 */
@SuppressLint({ "InflateParams", "SimpleDateFormat" })
public class RealPlayActivity extends BaseActivity {
	private TitleView titleView;
	private ImageButton btLeft;
	private ImageButton btRight;
	private ImageButton btTop;
	private ImageButton btBottom;
	private ImageButton btAddZoom;
	private ImageButton btReduceZoom;
	private ImageButton btAddFocus;
	private ImageButton btReduceFocus;
	private ImageButton btAddAperture;
	private ImageButton btReduceAperture;
	private ImageView img_shezhi;// 设置
	private ImageView img_bofang;// 播放
	private ImageView img_steering;// 方向
	private boolean isBoFang = true;
	private PopupWindow sheZhiPop;// 设置pop
	private PopupWindow steeringPop;// 方向pop
	public final static String IMAGE_PATH = Environment.getExternalStorageDirectory().getPath() + "/snapshot/";
	public final static String IMGSTR = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".jpg";

	private byte[] m_szCameraId = null;
	private int m_pDLLHandle = 0;
	private SurfaceView m_svPlayer = null;
	private int m_nPort = 0;
	private int m_nSeq = 0;
	private int mTimeOut = 30 * 1000;
	private fMediaDataCallback fm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_real_play_layout);
		m_pDLLHandle = DpsdkCoreActivity.getDpsdkHandle();
		initView();
		initData();
		initListener();
	}

	@Override
	public void initView() {
		titleView = (TitleView) findViewById(R.id.comment_title);
		m_svPlayer = (SurfaceView) findViewById(R.id.sv_player);
		img_shezhi = (ImageView) findViewById(R.id.img_shezhi);
		img_bofang = (ImageView) findViewById(R.id.img_bofang);
		img_steering = (ImageView) findViewById(R.id.img_steering);
	}

	@Override
	public void initData() {
		titleView.setTitle("视频");
		m_szCameraId = getIntent().getStringExtra("channelId").getBytes();
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
	}

	@Override
	public void initListener() {
		img_bofang.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isBoFang) {
					img_bofang.setImageResource(R.drawable.iconfont_zanting);
					isBoFang = false;
					if (!StartRealPlay()) {
						return;
					}
					try {
						Utils.click_play = true;
						Return_Value_Info_t retVal = new Return_Value_Info_t();
						Get_RealStream_Info_t getRealStreamInfo = new Get_RealStream_Info_t();
						System.arraycopy(m_szCameraId, 0, getRealStreamInfo.szCameraId, 0, m_szCameraId.length);
						getRealStreamInfo.nMediaType = 1;
						getRealStreamInfo.nRight = 0;
						getRealStreamInfo.nStreamType = 1;
						getRealStreamInfo.nTransType = 1;
						Enc_Channel_Info_Ex_t ChannelInfo = new Enc_Channel_Info_Ex_t();
						IDpsdkCore.DPSDK_GetChannelInfoById(m_pDLLHandle, m_szCameraId, ChannelInfo);
						int ret = IDpsdkCore.DPSDK_GetRealStream(m_pDLLHandle, retVal, getRealStreamInfo, fm, mTimeOut);
						if (ret == 0) {
							m_nSeq = retVal.nReturnValue;
						} else {
							StopRealPlay();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					try {
						IDpsdkCore.DPSDK_CloseRealStreamBySeq(m_pDLLHandle, m_nSeq, mTimeOut);
					} catch (Exception e) {
						e.printStackTrace();
					}
					StopRealPlay();
					img_bofang.setImageResource(R.drawable.iconfont_bofang);
					isBoFang = true;
				}
			}
		});
		titleView.setLeftBackOneListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!isBoFang) {
					try {
						IDpsdkCore.DPSDK_CloseRealStreamBySeq(m_pDLLHandle, m_nSeq, mTimeOut);
					} catch (Exception e) {
						e.printStackTrace();
					}
					StopRealPlay();
					img_bofang.setImageResource(R.drawable.iconfont_bofang);
					isBoFang = true;
				}
				onBackPressed();
			}
		});
		img_shezhi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getSheZhiPop();
				int[] location = new int[2];
				v.getLocationOnScreen(location);
				sheZhiPop.showAtLocation(v, Gravity.NO_GRAVITY, 20, location[1] - 430);
			}
		});

		img_steering.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getSteeringPop();
				int[] location = new int[2];
				v.getLocationOnScreen(location);
				steeringPop.showAtLocation(v, Gravity.NO_GRAVITY, location[0] - 20, location[1] - 380);
			}
		});
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
			Logger.i("StartRealPlay1");
			if (bPlayRet) {
				boolean bSuccess = IPlaySDK.PLAYPlaySoundShare(m_nPort) == 0 ? false : true;
				Logger.i("StartRealPlay2");
				if (!bSuccess) {
					IPlaySDK.PLAYStop(m_nPort);
					IPlaySDK.PLAYCloseStream(m_nPort);
					Logger.i("StartRealPlay3");
					return false;
				}
			} else {
				IPlaySDK.PLAYCloseStream(m_nPort);
				Logger.i("StartRealPlay4");
				return false;
			}
		} else {
			Logger.i("StartRealPlay5");
			return false;
		}
		return true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	/**
	 * 创建设置的pop
	 */
	@SuppressWarnings("deprecation")
	@SuppressLint("ClickableViewAccessibility")
	private void getSheZhiPop() {
		if (null != sheZhiPop) {
			sheZhiPop.dismiss();
			return;
		} else {
			View sheZhiPopView = getLayoutInflater().inflate(R.layout.pop_shezhi, null, false);
			sheZhiPop = new PopupWindow(sheZhiPopView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
			sheZhiPop.setFocusable(true);
			// 这句是为了防止弹出菜单获取焦点之后，点击activity的其他组件没有响应
			sheZhiPop.setBackgroundDrawable(new BitmapDrawable());
			sheZhiPop.setOutsideTouchable(true);
			btAddZoom = (ImageButton) sheZhiPopView.findViewById(R.id.button_add_zoom);
			btReduceZoom = (ImageButton) sheZhiPopView.findViewById(R.id.button_reduce_zoom);
			btAddFocus = (ImageButton) sheZhiPopView.findViewById(R.id.button_add_focus);
			btReduceFocus = (ImageButton) sheZhiPopView.findViewById(R.id.button_reduce_focus);
			btAddAperture = (ImageButton) sheZhiPopView.findViewById(R.id.button_add_aperture);
			btReduceAperture = (ImageButton) sheZhiPopView.findViewById(R.id.button_reduce_aperture);
			btAddZoom.setOnTouchListener(new View.OnTouchListener() {

				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
						Ptz_Operation_Info_t ptzOperationInfo = new Ptz_Operation_Info_t();
						System.arraycopy(m_szCameraId, 0, ptzOperationInfo.szCameraId, 0, m_szCameraId.length);
						ptzOperationInfo.bStop = false;
						ptzOperationInfo.nOperation = 0;
						ptzOperationInfo.nStep = 4;

						int ret = IDpsdkCore.DPSDK_PtzCameraOperation(m_pDLLHandle, ptzOperationInfo, mTimeOut);
						if (ret == 0) {
							Logger.e("DPSDK_PtzCameraOperation success!");
						} else {
							Logger.e("DPSDK_PtzCameraOperation failed!");
						}
					} else if (arg1.getAction() == MotionEvent.ACTION_UP) {
						Ptz_Operation_Info_t ptzOperationInfo = new Ptz_Operation_Info_t();
						System.arraycopy(m_szCameraId, 0, ptzOperationInfo.szCameraId, 0, m_szCameraId.length);
						ptzOperationInfo.bStop = true;
						ptzOperationInfo.nOperation = 0;
						ptzOperationInfo.nStep = 4;

						int ret = IDpsdkCore.DPSDK_PtzCameraOperation(m_pDLLHandle, ptzOperationInfo, mTimeOut);
						if (ret == 0) {
							Logger.e("DPSDK_PtzCameraOperation success!");
						} else {
							Logger.e("DPSDK_PtzCameraOperation failed!");
						}
					}
					return false;
				}
			});

			btReduceZoom.setOnTouchListener(new View.OnTouchListener() {

				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
						Ptz_Operation_Info_t ptzOperationInfo = new Ptz_Operation_Info_t();
						System.arraycopy(m_szCameraId, 0, ptzOperationInfo.szCameraId, 0, m_szCameraId.length);
						ptzOperationInfo.bStop = false;
						ptzOperationInfo.nOperation = 3;
						ptzOperationInfo.nStep = 4;
						int ret = IDpsdkCore.DPSDK_PtzCameraOperation(m_pDLLHandle, ptzOperationInfo, mTimeOut);
						if (ret == 0) {
							Logger.e("DPSDK_PtzCameraOperation success!");
						} else {
							Logger.e("DPSDK_PtzCameraOperation failed!");
						}
					} else if (arg1.getAction() == MotionEvent.ACTION_UP) {
						Ptz_Operation_Info_t ptzOperationInfo = new Ptz_Operation_Info_t();
						System.arraycopy(m_szCameraId, 0, ptzOperationInfo.szCameraId, 0, m_szCameraId.length);
						ptzOperationInfo.bStop = true;
						ptzOperationInfo.nOperation = 3;
						ptzOperationInfo.nStep = 4;
						int ret = IDpsdkCore.DPSDK_PtzCameraOperation(m_pDLLHandle, ptzOperationInfo, mTimeOut);
						if (ret == 0) {
							Logger.e("DPSDK_PtzCameraOperation success!");
						} else {
							Logger.e("DPSDK_PtzCameraOperation failed!");
						}
					}
					return false;
				}
			});

			btAddFocus.setOnTouchListener(new View.OnTouchListener() {

				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
						Ptz_Operation_Info_t ptzOperationInfo = new Ptz_Operation_Info_t();
						System.arraycopy(m_szCameraId, 0, ptzOperationInfo.szCameraId, 0, m_szCameraId.length);
						ptzOperationInfo.bStop = false;
						ptzOperationInfo.nOperation = 1;
						ptzOperationInfo.nStep = 4;
						int ret = IDpsdkCore.DPSDK_PtzCameraOperation(m_pDLLHandle, ptzOperationInfo, mTimeOut);
						if (ret == 0) {
							Logger.e("DPSDK_PtzCameraOperation success!");
						} else {
							Logger.e("DPSDK_PtzCameraOperation failed!");
						}
					} else if (arg1.getAction() == MotionEvent.ACTION_UP) {
						Ptz_Operation_Info_t ptzOperationInfo = new Ptz_Operation_Info_t();
						System.arraycopy(m_szCameraId, 0, ptzOperationInfo.szCameraId, 0, m_szCameraId.length);
						ptzOperationInfo.bStop = true;
						ptzOperationInfo.nOperation = 1;
						ptzOperationInfo.nStep = 4;
						int ret = IDpsdkCore.DPSDK_PtzCameraOperation(m_pDLLHandle, ptzOperationInfo, mTimeOut);
						if (ret == 0) {
							Logger.e("DPSDK_PtzCameraOperation success!");
						} else {
							Logger.e("DPSDK_PtzCameraOperation failed!");
						}
					}
					return false;
				}
			});

			btReduceFocus.setOnTouchListener(new View.OnTouchListener() {

				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {

					if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
						Ptz_Operation_Info_t ptzOperationInfo = new Ptz_Operation_Info_t();
						System.arraycopy(m_szCameraId, 0, ptzOperationInfo.szCameraId, 0, m_szCameraId.length);
						ptzOperationInfo.bStop = false;
						ptzOperationInfo.nOperation = 4;
						ptzOperationInfo.nStep = 4;

						int ret = IDpsdkCore.DPSDK_PtzCameraOperation(m_pDLLHandle, ptzOperationInfo, mTimeOut);
						if (ret == 0) {
							Logger.e("DPSDK_PtzCameraOperation success!");
						} else {
							Logger.e("DPSDK_PtzCameraOperation failed!");
						}
					} else if (arg1.getAction() == MotionEvent.ACTION_UP) {
						Ptz_Operation_Info_t ptzOperationInfo = new Ptz_Operation_Info_t();
						System.arraycopy(m_szCameraId, 0, ptzOperationInfo.szCameraId, 0, m_szCameraId.length);
						ptzOperationInfo.bStop = true;
						ptzOperationInfo.nOperation = 4;
						ptzOperationInfo.nStep = 4;

						int ret = IDpsdkCore.DPSDK_PtzCameraOperation(m_pDLLHandle, ptzOperationInfo, mTimeOut);
						if (ret == 0) {
							Logger.e("DPSDK_PtzCameraOperation success!");
						} else {
							Logger.e("DPSDK_PtzCameraOperation failed!");
						}
					}

					return false;
				}
			});

			btAddAperture.setOnTouchListener(new View.OnTouchListener() {

				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
						Ptz_Operation_Info_t ptzOperationInfo = new Ptz_Operation_Info_t();
						System.arraycopy(m_szCameraId, 0, ptzOperationInfo.szCameraId, 0, m_szCameraId.length);
						ptzOperationInfo.bStop = false;
						ptzOperationInfo.nOperation = 2;
						ptzOperationInfo.nStep = 4;

						int ret = IDpsdkCore.DPSDK_PtzCameraOperation(m_pDLLHandle, ptzOperationInfo, mTimeOut);
						if (ret == 0) {
							Logger.e("DPSDK_PtzCameraOperation success!");
						} else {
							Logger.e("DPSDK_PtzCameraOperation failed!");
						}
					} else if (arg1.getAction() == MotionEvent.ACTION_UP) {
						Ptz_Operation_Info_t ptzOperationInfo = new Ptz_Operation_Info_t();
						System.arraycopy(m_szCameraId, 0, ptzOperationInfo.szCameraId, 0, m_szCameraId.length);
						ptzOperationInfo.bStop = true;
						ptzOperationInfo.nOperation = 2;
						ptzOperationInfo.nStep = 4;

						int ret = IDpsdkCore.DPSDK_PtzCameraOperation(m_pDLLHandle, ptzOperationInfo, mTimeOut);
						if (ret == 0) {
							Logger.e("DPSDK_PtzCameraOperation success!");
						} else {
							Logger.e("DPSDK_PtzCameraOperation failed!");
						}
					}

					return false;
				}
			});

			btReduceAperture.setOnTouchListener(new View.OnTouchListener() {

				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
						Ptz_Operation_Info_t ptzOperationInfo = new Ptz_Operation_Info_t();
						System.arraycopy(m_szCameraId, 0, ptzOperationInfo.szCameraId, 0, m_szCameraId.length);
						ptzOperationInfo.bStop = false;
						ptzOperationInfo.nOperation = 5;
						ptzOperationInfo.nStep = 4;

						int ret = IDpsdkCore.DPSDK_PtzCameraOperation(m_pDLLHandle, ptzOperationInfo, mTimeOut);
						if (ret == 0) {
							Logger.e("DPSDK_PtzCameraOperation success!");
						} else {
							Logger.e("DPSDK_PtzCameraOperation failed!");
						}
					} else if (arg1.getAction() == MotionEvent.ACTION_UP) {
						Ptz_Operation_Info_t ptzOperationInfo = new Ptz_Operation_Info_t();
						System.arraycopy(m_szCameraId, 0, ptzOperationInfo.szCameraId, 0, m_szCameraId.length);
						ptzOperationInfo.bStop = true;
						ptzOperationInfo.nOperation = 5;
						ptzOperationInfo.nStep = 4;

						int ret = IDpsdkCore.DPSDK_PtzCameraOperation(m_pDLLHandle, ptzOperationInfo, mTimeOut);
						if (ret == 0) {
							Logger.e("DPSDK_PtzCameraOperation success!");
						} else {
							Logger.e("DPSDK_PtzCameraOperation failed!");
						}
					}
					return false;
				}
			});
		}
	}

	/**
	 * 创建方向选择pop
	 */
	@SuppressWarnings("deprecation")
	@SuppressLint("ClickableViewAccessibility")
	private void getSteeringPop() {
		if (null != steeringPop) {
			steeringPop.dismiss();
			return;
		} else {
			View steeringpop_view = getLayoutInflater().inflate(R.layout.pop_steering, null, false);
			steeringPop = new PopupWindow(steeringpop_view, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
			steeringPop.setFocusable(true);
			// 这句是为了防止弹出菜单获取焦点之后，点击activity的其他组件没有响应
			steeringPop.setBackgroundDrawable(new BitmapDrawable());

			steeringPop.setOutsideTouchable(true);

			btLeft = (ImageButton) steeringpop_view.findViewById(R.id.button_ptz_left);
			btRight = (ImageButton) steeringpop_view.findViewById(R.id.button_right);
			btTop = (ImageButton) steeringpop_view.findViewById(R.id.button_top);
			btBottom = (ImageButton) steeringpop_view.findViewById(R.id.button_bottom);

			btLeft.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
						Ptz_Direct_Info_t ptzDirectInfo = new Ptz_Direct_Info_t();
						System.arraycopy(m_szCameraId, 0, ptzDirectInfo.szCameraId, 0, m_szCameraId.length);
						ptzDirectInfo.bStop = false;
						ptzDirectInfo.nDirect = 3;
						ptzDirectInfo.nStep = 4;

						int ret = IDpsdkCore.DPSDK_PtzDirection(m_pDLLHandle, ptzDirectInfo, mTimeOut);
						if (ret == 0) {
							Logger.e("DPSDK_PtzDirection success!");
						} else {
							Logger.e("DPSDK_PtzDirection failed!");
						}
					} else if (arg1.getAction() == MotionEvent.ACTION_UP) {
						Ptz_Direct_Info_t ptzDirectInfo = new Ptz_Direct_Info_t();
						System.arraycopy(m_szCameraId, 0, ptzDirectInfo.szCameraId, 0, m_szCameraId.length);
						ptzDirectInfo.bStop = true;
						ptzDirectInfo.nDirect = 3;
						ptzDirectInfo.nStep = 4;

						int ret = IDpsdkCore.DPSDK_PtzDirection(m_pDLLHandle, ptzDirectInfo, mTimeOut);
						if (ret == 0) {
							Logger.e("DPSDK_PtzDirection success!");
						} else {
							Logger.e("DPSDK_PtzDirection failed!");
						}
					}
					return false;
				}
			});
			btRight.setOnTouchListener(new View.OnTouchListener() {

				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
						Ptz_Direct_Info_t ptzDirectInfo = new Ptz_Direct_Info_t();
						System.arraycopy(m_szCameraId, 0, ptzDirectInfo.szCameraId, 0, m_szCameraId.length);
						ptzDirectInfo.bStop = false;
						ptzDirectInfo.nDirect = 4;
						ptzDirectInfo.nStep = 4;

						int ret = IDpsdkCore.DPSDK_PtzDirection(m_pDLLHandle, ptzDirectInfo, mTimeOut);
						if (ret == 0) {
							Logger.e("DPSDK_PtzDirection success!");
						} else {
							Logger.e("DPSDK_PtzDirection failed!");
						}
					} else if (arg1.getAction() == MotionEvent.ACTION_UP) {
						Ptz_Direct_Info_t ptzDirectInfo = new Ptz_Direct_Info_t();
						System.arraycopy(m_szCameraId, 0, ptzDirectInfo.szCameraId, 0, m_szCameraId.length);
						ptzDirectInfo.bStop = true;
						ptzDirectInfo.nDirect = 4;
						ptzDirectInfo.nStep = 4;

						int ret = IDpsdkCore.DPSDK_PtzDirection(m_pDLLHandle, ptzDirectInfo, mTimeOut);
						if (ret == 0) {
							Logger.e("DPSDK_PtzDirection success!");
						} else {
							Logger.e("DPSDK_PtzDirection failed!");
						}
					}
					return false;
				}
			});

			btTop.setOnTouchListener(new View.OnTouchListener() {

				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
						Ptz_Direct_Info_t ptzDirectInfo = new Ptz_Direct_Info_t();
						System.arraycopy(m_szCameraId, 0, ptzDirectInfo.szCameraId, 0, m_szCameraId.length);
						ptzDirectInfo.bStop = false;
						ptzDirectInfo.nDirect = 1;
						ptzDirectInfo.nStep = 4;

						int ret = IDpsdkCore.DPSDK_PtzDirection(m_pDLLHandle, ptzDirectInfo, mTimeOut);
						if (ret == 0) {
							Logger.e("DPSDK_PtzDirection success!");
						} else {
							Logger.e("DPSDK_PtzDirection failed!");
						}
					} else if (arg1.getAction() == MotionEvent.ACTION_UP) {
						Ptz_Direct_Info_t ptzDirectInfo = new Ptz_Direct_Info_t();
						System.arraycopy(m_szCameraId, 0, ptzDirectInfo.szCameraId, 0, m_szCameraId.length);
						ptzDirectInfo.bStop = true;
						ptzDirectInfo.nDirect = 1;
						ptzDirectInfo.nStep = 4;
						int ret = IDpsdkCore.DPSDK_PtzDirection(m_pDLLHandle, ptzDirectInfo, mTimeOut);
						if (ret == 0) {
							Logger.e("DPSDK_PtzDirection success!");
						} else {
							Logger.e("DPSDK_PtzDirection failed!");
						}
					}
					return false;
				}
			});

			btBottom.setOnTouchListener(new View.OnTouchListener() {

				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
						Ptz_Direct_Info_t ptzDirectInfo = new Ptz_Direct_Info_t();
						System.arraycopy(m_szCameraId, 0, ptzDirectInfo.szCameraId, 0, m_szCameraId.length);
						ptzDirectInfo.bStop = false;
						ptzDirectInfo.nDirect = 2;
						ptzDirectInfo.nStep = 4;

						int ret = IDpsdkCore.DPSDK_PtzDirection(m_pDLLHandle, ptzDirectInfo, mTimeOut);
						if (ret == 0) {
							Logger.e("DPSDK_PtzDirection success!");
						} else {
							Logger.e("DPSDK_PtzDirection failed!");
						}
					} else if (arg1.getAction() == MotionEvent.ACTION_UP) {
						Ptz_Direct_Info_t ptzDirectInfo = new Ptz_Direct_Info_t();
						System.arraycopy(m_szCameraId, 0, ptzDirectInfo.szCameraId, 0, m_szCameraId.length);
						ptzDirectInfo.bStop = true;
						ptzDirectInfo.nDirect = 2;
						ptzDirectInfo.nStep = 4;
						int ret = IDpsdkCore.DPSDK_PtzDirection(m_pDLLHandle, ptzDirectInfo, mTimeOut);
						if (ret == 0) {
							Logger.e("DPSDK_PtzDirection success!");
						} else {
							Logger.e("DPSDK_PtzDirection failed!");
						}
					}
					return false;
				}
			});
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			stopVideo();
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	private void stopVideo() {
		if (!isBoFang) {
			int ret = -1;
			try {
				ret = IDpsdkCore.DPSDK_CloseRealStreamBySeq(m_pDLLHandle, m_nSeq, mTimeOut);
			} catch (Exception e) {
			}
			if (ret == 0) {
				// btOpenVideo.setEnabled(true);
				// btCloseVideo.setEnabled(false);
				Logger.e("DPSDK_CloseRealStreamByCameraId success!");
				MsgToast.geToast().setMsg("Close video success!");
			} else {
				Logger.e("DPSDK_CloseRealStreamByCameraId failed! ret = " + ret);
				MsgToast.geToast().setMsg("Close video failed!");
			}
			StopRealPlay();
			img_bofang.setImageResource(R.drawable.iconfont_bofang);
			isBoFang = true;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		registerHomeKeyReceiver(this);
	}

	@Override
	protected void onPause() {
		unregisterHomeKeyReceiver(this);
		super.onPause();
	}

	private static HomeWatcherReceiver mHomeKeyReceiver = null;

	private void registerHomeKeyReceiver(Context context) {
		mHomeKeyReceiver = new HomeWatcherReceiver();
		final IntentFilter homeFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
		context.registerReceiver(mHomeKeyReceiver, homeFilter);
	}

	private void unregisterHomeKeyReceiver(Context context) {
		if (null != mHomeKeyReceiver) {
			context.unregisterReceiver(mHomeKeyReceiver);
		}
	}

	public class HomeWatcherReceiver extends BroadcastReceiver {
		private static final String SYSTEM_DIALOG_REASON_KEY = "reason";
		private static final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
				String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
				if (SYSTEM_DIALOG_REASON_HOME_KEY.equals(reason)) {
					// 短按Home键
					stopVideo();
				}
			}
		}
	}

}
