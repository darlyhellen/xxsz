package com.xiangxun.xacity.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.ImageColumns;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.xiangxun.xacity.R;
import com.xiangxun.xacity.bean.PhotoInfo;
import com.xiangxun.xacity.request.Api;
import com.xiangxun.xacity.request.ApiUrl;
import com.xiangxun.xacity.utils.MyUtils;
import com.xiangxun.xacity.utils.Tools;
import com.xiangxun.xacity.view.MsgToast;

/**
 * @package: com.xiangxun.xacity.ui
 * @ClassName: CameraActivity.java
 * @Description: 自定义相机拍照页面
 * @author: HanGJ
 * @date: 2016-2-3 上午10:23:46
 */
public class CameraActivity extends Activity implements OnClickListener {
	private SurfaceView sv;
	private ImageView iv_camera;
	private Button btn_cancel;
	private Button btn_ok;
	private Camera camera;
	private ImageView iv_close;
	private ImageView iv_open;
	private Camera.Parameters parameters = null;
	private ViewPager vp;
	private ViewPaperAdapter pAdapter;
	private List<PhotoInfo> arrCamera;
	private SensorEventListener mListener = null;
	private SensorManager mManager = null;
	private Sensor mSensor = null;
	private int direction = 0;
	private int rotata;
	private int mSize = 0;
	private int total = 0;
	private int callbackTimes;
	private ToneGenerator tone;
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			arrCamera.add((PhotoInfo) msg.obj);
			pAdapter.notifyDataSetChanged();
			vp.setCurrentItem(arrCamera.size() - 1);
			btn_ok.setText("完成(" + arrCamera.size() + ")");
			vp.setVisibility(View.VISIBLE);
			iv_open.setVisibility(View.GONE);
			iv_close.setVisibility(View.VISIBLE);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera_activity);
		initView();
		initData();
		initListener();
	}

	private void initView() {
		sv = (SurfaceView) findViewById(R.id.sv);
		iv_camera = (ImageView) findViewById(R.id.iv_camera);
		vp = (ViewPager) findViewById(R.id.vp);
		btn_cancel = (Button) findViewById(R.id.btn_cancel);
		btn_ok = (Button) findViewById(R.id.btn_ok);
		iv_close = (ImageView) findViewById(R.id.iv_close);
		iv_open = (ImageView) findViewById(R.id.iv_open);
	}

	@SuppressWarnings("deprecation")
	private void initData() {
		mSize = getIntent().getIntExtra("size", 0);
		total = getIntent().getIntExtra("total", 0);
		sv.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		// sv.getHolder().setFixedSize(1280, 840);
		sv.getHolder().setKeepScreenOn(true);
		sv.getHolder().addCallback(new Callback() {
			@Override
			public void surfaceDestroyed(SurfaceHolder arg0) {
				if (camera != null) {
					camera.stopPreview();
					camera.setPreviewCallback(null);
					camera.release();
					camera = null;
				}
			}

			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				try {
					camera = Camera.open();
					camera.setPreviewDisplay(holder);
					camera.setDisplayOrientation(getPreviewDegree());
					camera.startPreview();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void surfaceChanged(SurfaceHolder arg0, int arg1, int width, int height) {
				parameters = camera.getParameters(); // 获取各项参数
				parameters.setPictureFormat(ImageFormat.JPEG);// 设置图片格式
				parameters.setPreviewFormat(ImageFormat.NV21);
				List<Size> list = parameters.getSupportedPictureSizes();
				parameters.setPictureSize(list.get(list.size() - 1).width, list.get(list.size() - 1).height);
				list = parameters.getSupportedPreviewSizes();
				parameters.setPreviewSize(list.get(0).width, list.get(0).height);
				// parameters.setPreviewFrameRate(5); // 设置每秒显示4帧
				parameters.setJpegQuality(100); // 设置照片质量
				camera.setDisplayOrientation(getPreviewDegree());
				camera.setParameters(parameters);
				camera.startPreview();
			}
		});
		arrCamera = new ArrayList<PhotoInfo>();
		pAdapter = new ViewPaperAdapter();
		vp.setAdapter(pAdapter);
	}

	private void initListener() {
		iv_camera.setOnClickListener(this);
		btn_cancel.setOnClickListener(this);
		btn_ok.setOnClickListener(this);
		iv_close.setOnClickListener(this);
		iv_open.setOnClickListener(this);
		mListener = new SensorEventListener() {
			public void onAccuracyChanged(Sensor sensor, int accuracy) {
			}

			@SuppressWarnings("deprecation")
			public void onSensorChanged(SensorEvent event) {
				float x = event.values[SensorManager.DATA_X];
				float y = event.values[SensorManager.DATA_Y];
				int temp = getDiection((int) x, (int) y);
				if (temp == -100) {
					return;
				} else {
					rotata(temp);
				}
			}
		};
		mManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mSensor = mManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mManager.registerListener(mListener, mSensor, SensorManager.SENSOR_DELAY_GAME);
	}

	public int getDiection(int x, int y) {
		int tempDirection = -100;
		if (Math.abs(x) < 1) {
			if (y > 8) {
				tempDirection = 0;
			} else if (y < -8) {
				tempDirection = 2;
			}
		} else if (Math.abs(y) < 1) {
			if (x > 8) {
				tempDirection = 3;
			} else if (x < -8) {
				tempDirection = 1;
			}
		}
		return tempDirection;
	}

	private int getPreviewDegree() {
		int rotation = this.getWindowManager().getDefaultDisplay().getRotation();
		int res = 0;
		switch (rotation) {
		case Surface.ROTATION_0:
			res = 90;
			break;
		case Surface.ROTATION_90:
			res = 0;
			break;
		case Surface.ROTATION_180:
			res = 270;
			break;
		case Surface.ROTATION_270:
			res = 180;
			break;
		default:
			break;
		}
		return res;
	}

	private synchronized void rotata(int tempDirection) {
		if (tempDirection == direction) {
			return;
		} else {
			switch (tempDirection) {
			case 0:
				rotata = 0;
				break;
			case 1:
				rotata = -90;
				break;
			case 2:
				rotata = -180;
				break;
			case 3:
				rotata = 90;
				break;

			default:
				break;
			}
			notifyRotata();
			direction = tempDirection;
		}
	}

	public void notifyRotata() {
		pAdapter = new ViewPaperAdapter();
		vp.setAdapter(pAdapter);
	}

	private class ViewPaperAdapter extends PagerAdapter {
		@Override
		public float getPageWidth(int position) {
			return (float) 0.25;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
		}

		@Override
		public int getCount() {
			return arrCamera.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(final ViewGroup container, final int position) {
			View v = getView(arrCamera.get(position).filePath, CameraActivity.this);
			ImageView iv_delete = (ImageView) v.findViewById(R.id.iv_delete);
			iv_delete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					arrCamera.remove(position);
					pAdapter = new ViewPaperAdapter();
					vp.setAdapter(pAdapter);
					btn_ok.setText("完成(" + arrCamera.size() + ")");
				}
			});
			container.addView(v);
			return v;
		}
	}

	/**
	 * @Title: getView
	 * @Description: 用于拍照页面刷新图片
	 * @param: @param rotata
	 * @return: void
	 */
	@SuppressLint("InflateParams")
	public View getView(String filePath, Context con) {
		LayoutInflater inflater = LayoutInflater.from(con);
		View v = inflater.inflate(R.layout.publish_image_camera, null);
		Bitmap bitmap = MyUtils.getSmallImg(filePath, con);
		bitmap = MyUtils.rotataBitmap(bitmap, getPreviewDegree());
		ImageView iv_publish = (ImageView) v.findViewById(R.id.iv_publish);
		iv_publish.setImageBitmap(bitmap);
		iv_publish.setScaleType(ScaleType.CENTER_INSIDE);
		return v;
	}

	private class TakePic implements Runnable {
		private byte[] data;

		public TakePic(byte[] data) {
			this.data = data;
		}

		@Override
		public void run() {
			if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
				String filePath = Api.xaCityPublishPictureDir + Tools.getDateAsFileName() + "-" + Math.round(Math.random() * 8999 + 1000) + ".jpg";
				File picture = new File(Api.xaCityPublishPictureDir);
				if (!picture.exists())
					picture.mkdirs();
				try {
					FileOutputStream fos = new FileOutputStream(filePath);
					fos.write(data);
					fos.close();
					File file = new File(filePath);
					if (file.exists()) {
						ContentResolver resolver = CameraActivity.this.getContentResolver();
						ContentValues contentValues = new ContentValues();
						contentValues.put(ImageColumns.TITLE, file.getName());
						contentValues.put(ImageColumns.DISPLAY_NAME, file.getName());
						contentValues.put(ImageColumns.ORIENTATION, getPreviewDegree());
						contentValues.put(ImageColumns.DATA, filePath);
						resolver.insert(Images.Media.EXTERNAL_CONTENT_URI, contentValues);
						resolver.notifyChange(Images.Media.EXTERNAL_CONTENT_URI, null);
					}
					Message msg = handler.obtainMessage();
					PhotoInfo photoInfo = new PhotoInfo();
					photoInfo.filePath = filePath;
					photoInfo.url = ApiUrl.upLoadImageUrl(CameraActivity.this);
					photoInfo.statu = PhotoInfo.STATUREADY;
					msg.obj = photoInfo;
					handler.sendMessage(msg);
				} catch (Exception e) {
					MsgToast.geToast().setMsg("文件写入失败");
				}
			} else {
				MsgToast.geToast().setMsg("SD卡被拨出，数据无法存储");
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_camera:
			if ((mSize + arrCamera.size()) < total) {
				if (Tools.isFastDoubleClick()) {
					MsgToast.geToast().setMsg("拍照速度太快了~");
					return;
				}
				if (camera != null) {
					callbackTimes = 0;
					camera.autoFocus(new TPcallback());
				}
			} else {
				MsgToast.geToast().setMsg("已选取" + total + "张照片~");
			}
			break;
		case R.id.btn_cancel:
			arrCamera.clear();
			this.finish();
			break;
		case R.id.btn_ok:
			Intent intent = new Intent();
			intent.putExtra("camera_picture", (Serializable) arrCamera);
			setResult(Activity.RESULT_OK, intent);
			finish();
			break;
		case R.id.iv_close:
			vp.setVisibility(View.GONE);
			iv_open.setVisibility(View.VISIBLE);
			iv_close.setVisibility(View.GONE);
			break;
		case R.id.iv_open:
			vp.setVisibility(View.VISIBLE);
			iv_open.setVisibility(View.GONE);
			iv_close.setVisibility(View.VISIBLE);
			break;
		}
	}

	private class TPcallback implements AutoFocusCallback {
		public void onAutoFocus(boolean success, Camera camera) {
			if (success) {
				callbackTimes++;
				if (callbackTimes > 1) {
					return;
				}
				camera.takePicture(new shutterCallback(), null, new MyPictureCallback());
			}
		}
	}

	private class MyPictureCallback implements PictureCallback {
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			try {
				Handler handler = new Handler();
				handler.post(new TakePic(data));
				camera.startPreview();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private class shutterCallback implements ShutterCallback {

		@Override
		public void onShutter() {
			if (tone == null) {
				tone = new ToneGenerator(1, ToneGenerator.MIN_VOLUME);
				tone.startTone(ToneGenerator.TONE_PROP_BEEP);
			}
		}
	}
}
