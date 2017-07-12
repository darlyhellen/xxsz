package com.xiangxun.xacity.view;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ViewFlipper;

import com.xiangxun.xacity.R;
import com.xiangxun.xacity.bean.PhotoInfo;
import com.xiangxun.xacity.utils.MyUtils;
import com.xiangxun.xacity.view.photoview.PhotoView;

/**
 * @package: com.xiangxun.xacity.view
 * @ClassName: PhoneViewDeleteAdapter.java
 * @Description: PhoneView适配器
 * @author: HanGJ
 * @date: 2016-2-18 上午11:27:21
 */
public class PhoneViewDeleteAdapter extends PagerAdapter {
	private Context mContext = null;
	private List<PhotoInfo> mPictures = null;

	public PhoneViewDeleteAdapter(Context context, List<PhotoInfo> pictures) {
		super();
		this.mContext = context;
		this.mPictures = pictures;
	}

	@Override
	public int getCount() {
		return mPictures.size();
	}

	@Override
	public View instantiateItem(final ViewGroup container, int position) {
		View phoneViewLayout = View.inflate(mContext, R.layout.publish_delete_picture_layout, null);
		ViewFlipper vfPhoneView = (ViewFlipper) phoneViewLayout.findViewById(R.id.vf_phone_view);
		vfPhoneView.setDisplayedChild(0);
		PhotoView photoView = (PhotoView) phoneViewLayout.findViewById(R.id.pv_deitail_picture);

		PhotoInfo photoInfo = mPictures.get(position);
		if (new File(photoInfo.filePath).exists()) {
			if (!TextUtils.isEmpty(photoInfo.filePath)) {
				Bitmap bitmap = MyUtils.getSmallImg(photoInfo.filePath, mContext);
//				bitmap = MyUtils.rotataBitmap(bitmap, MyUtils.getPreviewDegree((Activity) mContext));
				photoView.setImageBitmap(bitmap);
			}
		}

		container.addView(phoneViewLayout, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		return phoneViewLayout;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

}