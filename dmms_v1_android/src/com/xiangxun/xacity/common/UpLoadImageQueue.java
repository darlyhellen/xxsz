package com.xiangxun.xacity.common;

import java.util.HashMap;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.xiangxun.xacity.bean.PhotoInfo;
import com.xiangxun.xacity.utils.Logger;
import com.xiangxun.xacity.utils.MyUtils;

/**
 * @package: com.xiangxun.xacity.common
 * @ClassName: UpLoadImageQueue.java
 * @Description: 上传队列图片发布时使用
 * @author: HanGJ
 * @date: 2016-3-16 上午11:30:20
 */

public class UpLoadImageQueue {
	// 存放文件路径的队列
	@SuppressLint("NewApi")
	private BlockingDeque<PhotoInfo> queue = new LinkedBlockingDeque<PhotoInfo>();
	private UpLoadThread thread;
	private boolean isRun = false;
	LoadUpLoadMachine machine;
	Context con;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ConstantStatus.UpLoadSuccess:
				PhotoInfo info = (PhotoInfo) msg.obj;
				info.uCallBack.upSuccess(info);
				break;
			case ConstantStatus.UpLoadFalse:
				PhotoInfo info1 = (PhotoInfo) msg.obj;
				info1.uCallBack.upFalse(info1);
				break;
			default:
				break;
			}
		};
	};

	public UpLoadImageQueue(Context con) {
		this.con = con;
	}

	/**
	 * @Title:
	 * @Description: 开启队列
	 * @param:
	 * @return: void
	 * @throws
	 */
	public void start() {
		if (thread != null && thread.isAlive()) {
			isRun = false;
			thread.interrupt();
			thread = null;
		}
		thread = new UpLoadThread();
		isRun = true;
		thread.start();
	}

	/**
	 * @Title:
	 * @Description: 停止队列
	 * @param:
	 * @return: void
	 * @throws
	 */
	public void stop() {
		isRun = false;
		if (thread != null) {
			thread.interrupt();
		}
		thread = null;
	}

	/**
	 * @Title:
	 * @Description: 放入一个任务到队列
	 * @param: @param url
	 * @return: void
	 * @throws
	 */
	@SuppressLint("NewApi")
	public void put(PhotoInfo lv) {
		try {
			queue.put(lv);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@SuppressLint("NewApi")
	public void removeAll() {
		queue.clear();
		if (machine != null) {
			// 强制断开联网请求
			machine.close();
		}
	}

	private class UpLoadThread extends Thread {
		@SuppressLint("NewApi")
		@Override
		public void run() {
			while (isRun) {
				try {
					PhotoInfo pi = queue.take();
					machine = new LoadUpLoadMachine();
					try {
						HashMap<String, String> hs = new HashMap<String, String>();
//						hs.put("json", "true");
						String fileType = pi.fileType;
						String filePath = pi.filePath;
						pi.filePath = MyUtils.compressImage(filePath, con);
						pi = machine.requestPost(pi, hs, pi.url, pi.filePath, fileType);
						Logger.e("result = " + pi.result);
						Message msg = new Message();
						msg.what = ConstantStatus.UpLoadSuccess;
						msg.obj = pi;
						handler.sendMessage(msg);
					} catch (Exception e) {
						e.printStackTrace();
						Logger.e("upload fail");
						Message msg = new Message();
						msg.what = ConstantStatus.UpLoadFalse;
						msg.obj = pi;
						handler.sendMessage(msg);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
					return;
				}
			}
		}
	}

}
