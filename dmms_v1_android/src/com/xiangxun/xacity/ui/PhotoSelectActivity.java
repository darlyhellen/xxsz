package com.xiangxun.xacity.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import com.xiangxun.xacity.R;
import com.xiangxun.xacity.app.BaseActivity;
import com.xiangxun.xacity.bean.PhotoInfo;
import com.xiangxun.xacity.common.LocalNetWorkView;
import com.xiangxun.xacity.request.ApiUrl;
import com.xiangxun.xacity.utils.ImageCacheLoader;
import com.xiangxun.xacity.utils.ImageCacheLoader.GetLocalCallBack;
import com.xiangxun.xacity.view.MsgToast;
import com.xiangxun.xacity.view.TitleView;

/**
 * @package: com.xiangxun.xacity.ui
 * @ClassName: PhotoSelectActivity.java
 * @Description: 图片选择
 * @author: HanGJ
 * @date: 2016-2-3 下午4:23:17
 */
public class PhotoSelectActivity extends BaseActivity implements OnClickListener {
	private TitleView titleView;
	private GridView mGvPhotos = null;
	private MyGridAdapter mMyGridAdapter = null;
	private Button mBtnFinish = null;

	private ArrayList<PhotoInfo> mAllLocalPhotos = new ArrayList<PhotoInfo>();
	private List<PhotoInfo> mSelectedPhotos = new ArrayList<PhotoInfo>();
	private int mCount = 0;
	private int mSize;
	private int total = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo_select_activity);
		initView();
		initData();
		initListener();
	}

	@Override
	public void initView() {
		titleView = (TitleView) findViewById(R.id.tv_comm_title);
		titleView.setTitle("我的相册");
		mGvPhotos = (GridView) findViewById(R.id.gv_photos);
		mBtnFinish = (Button) findViewById(R.id.btn_finish);
	}

	@Override
	public void initData() {
		mSize = getIntent().getIntExtra("size", 0);
		total = getIntent().getIntExtra("total", 0);
		mMyGridAdapter = new MyGridAdapter();
		query();
		mGvPhotos.setAdapter(mMyGridAdapter);
	}

	@Override
	public void initListener() {
		titleView.setLeftBackOneListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		mBtnFinish.setOnClickListener(this);
		mGvPhotos.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ImageView ivSelectChecked = (ImageView) view.findViewById(R.id.iv_photo_select);
				if (ivSelectChecked.getVisibility() == View.GONE) {
					if (mSize + mCount >= total) {
						MsgToast.geToast().setMsg("已经选取" + total + "张照片~");
						return;
					}
					ivSelectChecked.setVisibility(View.VISIBLE);
					mSelectedPhotos.add(mAllLocalPhotos.get(position));
					mCount++;
				} else {
					ivSelectChecked.setVisibility(View.GONE);
					mSelectedPhotos.remove(mAllLocalPhotos.get(position));
					mCount--;
				}
				mBtnFinish.setText("完成(" + mCount + ")");
			}
		});
	}

	/**
	 * 
	 * @Title:
	 * @Description: 查询最新照片
	 * @param:
	 * @return: void
	 * @throws
	 */
	public void query() {
		String[] filePathColumn = { MediaStore.Images.Media.DATA };
		Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, filePathColumn, null, null, MediaStore.Images.Media.DATE_ADDED.concat(" desc"));
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			String filepath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
			PhotoInfo photoInfo = new PhotoInfo();
			photoInfo.filePath = filepath;
			photoInfo.url = ApiUrl.upLoadImageUrl(this);
			photoInfo.statu = PhotoInfo.STATUREADY;
			mAllLocalPhotos.add(photoInfo);
		}
	}

	private class MyGridAdapter extends BaseAdapter {
		private LayoutInflater mInflater = null;

		public MyGridAdapter() {
			mInflater = LayoutInflater.from(PhotoSelectActivity.this);
		}

		@Override
		public int getCount() {
			return mAllLocalPhotos.size();
		}

		@Override
		public Object getItem(int arg0) {
			return mAllLocalPhotos.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		@SuppressLint("InflateParams")
		public View getView(final int position, View convertView, ViewGroup arg2) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.photo_select_item, null);
				holder.ivPhoto = (LocalNetWorkView) convertView.findViewById(R.id.iv_photo);
				holder.ivSelectedLogo = (ImageView) convertView.findViewById(R.id.iv_photo_select);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			Object o = holder.ivPhoto.getTag();
			if (o != null && !((String) o).equals(mAllLocalPhotos.get(position).filePath)) {
				holder.ivPhoto.setImageBitmap(null);
			}

			holder.ivPhoto.setTag(mAllLocalPhotos.get(position).filePath);
			holder.ivSelectedLogo.setVisibility(mSelectedPhotos.contains(mAllLocalPhotos.get(position)) ? View.VISIBLE : View.GONE);
			try {
				ImageCacheLoader.getInstance().getLocalImage(mAllLocalPhotos.get(position).filePath, holder.ivPhoto, new GetLocalCallBack() {
					@Override
					public void localSuccess(Object o) {
						LocalNetWorkView lv = (LocalNetWorkView) o;
						if (lv.getTag().equals(lv.filePath)) {
							lv.setImageBitmap(lv.bm);
						}
					}

					@Override
					public void localFalse(Object o) {
						// ToDo
					}
				});
			} catch (Exception e) {
				// holder.ivPhoto.setBackgroundResource(R.drawable.ic_error);
			}
			return convertView;
		}
	}

	private class ViewHolder {
		LocalNetWorkView ivPhoto = null;
		ImageView ivSelectedLogo = null;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_finish:
			Intent intent = new Intent();
			intent.putExtra("album_picture", (Serializable) mSelectedPhotos);
			setResult(Activity.RESULT_OK, intent);
			finish();
			break;
		}
	}

}
