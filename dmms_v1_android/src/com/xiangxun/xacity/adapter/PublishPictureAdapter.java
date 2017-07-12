package com.xiangxun.xacity.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.xiangxun.xacity.R;
import com.xiangxun.xacity.bean.PhotoInfo;
import com.xiangxun.xacity.bean.ResultBeans.UpLoadResule;
import com.xiangxun.xacity.common.DcHttpClient;
import com.xiangxun.xacity.common.LocalNetWorkView;
import com.xiangxun.xacity.utils.ImageCacheLoader;
import com.xiangxun.xacity.utils.ImageCacheLoader.GetLocalCallBack;
import com.xiangxun.xacity.utils.ImageCacheLoader.UpLoadCallBack;
import com.xiangxun.xacity.utils.Logger;
import com.xiangxun.xacity.utils.Utils;
import com.xiangxun.xacity.view.DeletePictureInterface;
import com.xiangxun.xacity.view.NoViewPager;
import com.xiangxun.xacity.view.PublishLookAndDeletePicturePw;
import com.xiangxun.xacity.volley.toolbox.NetworkImageView;

/**
 * @package: com.xiangxun.xacity.adapter
 * @ClassName: PublishPictureAdapter.java
 * @Description: 图片展示适配器
 * @author: HanGJ
 * @date: 2016-2-18 上午11:24:06
 */
public class PublishPictureAdapter extends PagerAdapter {
	private int mChildCount = 0;
	private List<View> mViews = new ArrayList<View>();
	private NoViewPager mNoViewPager;
	private List<PhotoInfo> mPhotos = null;
	List<UpLoadResule> mUpLoadImageUrls;
	private PublishLookAndDeletePicturePw mPublishLookAndDeletePicturePw = null;
	private Context mContext;
	private String fileType;
	
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			List<ImageView> upLoadUI = null;
			switch (msg.what) {
			case PhotoInfo.STATUREADY:
				upLoadUI = (ArrayList<ImageView>) msg.obj;
				upLoadUI.get(0).setVisibility(View.VISIBLE);
				upLoadUI.get(1).setVisibility(View.INVISIBLE);
				break;
			case PhotoInfo.STATUSUCCESS:
				upLoadUI = (ArrayList<ImageView>) msg.obj;
				upLoadUI.get(0).setVisibility(View.INVISIBLE);
				upLoadUI.get(1).setVisibility(View.VISIBLE);
				break;
			case PhotoInfo.STATUFALSE:
				upLoadUI = (ArrayList<ImageView>) msg.obj;
				upLoadUI.get(0).setVisibility(View.INVISIBLE);
				upLoadUI.get(1).setVisibility(View.INVISIBLE);
				upLoadUI.get(2).setVisibility(View.VISIBLE);
				break;
			}
			// 上传loading取消动画
			if (upLoadUI != null) {
				upLoadUI.get(0).clearAnimation();
			}
		}
	};

	public PublishPictureAdapter(Context context, NoViewPager noViewPager, List<PhotoInfo> photos, List<UpLoadResule> upLoadImageUrls, String fileType) {
		super();
		this.mNoViewPager = noViewPager;
		this.mPhotos = photos;
		this.mContext = context;
		this.mUpLoadImageUrls = upLoadImageUrls;
		this.fileType = fileType;
	}

	@Override
	public float getPageWidth(int position) {
		return (float) 0.33;
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
		mChildCount = getCount();
		mNoViewPager.setPagingEnabled(mChildCount > 3);
	}

	@Override
	public int getItemPosition(Object object) {
		if (mChildCount > 0) {
			mChildCount--;
			return POSITION_NONE;
		}
		return super.getItemPosition(object);
	}

	@Override
	public void destroyItem(View container, int item, Object object) {
		View view = (View) object;
		((ViewPager) container).removeView(view);
		mViews.add(view);
	}

	@Override
	public int getCount() {
		return mPhotos == null ? 0 : mPhotos.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@Override
	public Object instantiateItem(final ViewGroup container, final int position) {
		View view = getView(mPhotos.get(position), mPhotos.get(position).filePath, mContext);
		container.addView(view);
		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mPublishLookAndDeletePicturePw = new PublishLookAndDeletePicturePw(mContext, mPhotos);
				mPublishLookAndDeletePicturePw.startFullScreenSlide(position);
				mPublishLookAndDeletePicturePw.setLocationListener((DeletePictureInterface) mContext);
			}
		});
		return view;
	}

	private View getView(final PhotoInfo photoInfo, String photoPath, Context con) {
		LayoutInflater inflater = LayoutInflater.from(con);
		LinearLayout linearLayout = new LinearLayout(mContext);
		View viewLocal = inflater.inflate(R.layout.publish_image, null);
		View viewUrl = inflater.inflate(R.layout.publish_image_download, null);
		final NetworkImageView ivPictureUrl = (NetworkImageView) viewUrl.findViewById(R.id.iv_publish);
		final RelativeLayout rlShowPictureNet = (RelativeLayout) viewUrl.findViewById(R.id.rl_publish_picture_show_item_net);
		
		final RelativeLayout rlShowPictureLocal = (RelativeLayout) viewLocal.findViewById(R.id.rl_publish_picture_show_item_local);
		final LocalNetWorkView ivPicture = (LocalNetWorkView) viewLocal.findViewById(R.id.iv_publish);

		final ImageView iv_loading = (ImageView) viewLocal.findViewById(R.id.iv_loading);
		final ImageView iv_finish = (ImageView) viewLocal.findViewById(R.id.iv_finish);
		final ImageView iv_false = (ImageView) viewLocal.findViewById(R.id.iv_false);
		// 转圈动画
		Animation animation = (AnimationSet) AnimationUtils.loadAnimation(mContext, R.anim.loading_animation);
		iv_loading.setAnimation(animation);

		final List<ImageView> imageViews = new ArrayList<ImageView>();
		imageViews.add(iv_loading);
		imageViews.add(iv_finish);
		imageViews.add(iv_false);
		if (!new File(photoPath).exists()) {
			if (iv_loading != null) {
				iv_loading.clearAnimation();
				iv_loading.setVisibility(View.GONE);
			}
			ivPictureUrl.setScaleType(ImageView.ScaleType.CENTER_CROP);
			DcHttpClient.getInstance().getImageForNIView(Utils.trimImageUrl(photoInfo.result, 0), ivPictureUrl, R.drawable.view_pager_default);
			Logger.e("result=" + photoInfo.result);
			linearLayout.addView(rlShowPictureNet);
		} else {
			ImageCacheLoader.getInstance().getLocalImage(photoPath, ivPicture, new GetLocalCallBack() {
				@Override
				public void localSuccess(Object o) {
					LocalNetWorkView lv = (LocalNetWorkView) o;
					ivPicture.setImageBitmap(DcHttpClient.getInstance().mBitmapCache.getBitmap(lv.filePath));
					// 已经上传成功直接显示成功图片
					if (photoInfo.statu == PhotoInfo.STATUSUCCESS) {
						Message msg = new Message();
						msg.what = PhotoInfo.STATUSUCCESS;
						msg.obj = imageViews;
						mHandler.sendMessage(msg);
					}
				}

				@Override
				public void localFalse(Object o) {
					ivPicture.setImageResource(R.drawable.view_pager_default);
				}
			});
		}
		if (photoInfo.statu != PhotoInfo.STATUSUCCESS) {
			photoInfo.fileType = fileType;
			ImageCacheLoader.getInstance().upLoadImage(photoInfo, new UpLoadCallBack() {
				@Override
				public void upSuccess(Object o) {
					PhotoInfo info = (PhotoInfo) o;
					info.statu = PhotoInfo.STATUSUCCESS;
					// 成功后将图片URL存放起来
					mUpLoadImageUrls.add(info.loadResule);
					Message msg = new Message();
					msg.what = PhotoInfo.STATUSUCCESS;
					msg.obj = imageViews;
					mHandler.sendMessage(msg);
				}

				@Override
				public void upFalse(Object o) {
					PhotoInfo info = (PhotoInfo) o;
					info.statu = PhotoInfo.STATUFALSE;
					Message msg = new Message();
					msg.what = PhotoInfo.STATUFALSE;
					msg.obj = imageViews;
					mHandler.sendMessage(msg);
				}
			});
		}

		ivPicture.setScaleType(ScaleType.CENTER_CROP);
		linearLayout.addView(rlShowPictureLocal);
		return linearLayout;
	}

}
