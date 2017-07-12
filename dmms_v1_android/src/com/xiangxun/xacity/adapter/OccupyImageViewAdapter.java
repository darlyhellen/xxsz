package com.xiangxun.xacity.adapter;

import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ViewFlipper;

import com.xiangxun.xacity.R;
import com.xiangxun.xacity.bean.ResponseResultBeans.OccupyImageBean;
import com.xiangxun.xacity.common.DcHttpClient;
import com.xiangxun.xacity.common.DcHttpClient.ResponseImageListener;
import com.xiangxun.xacity.request.Api;
import com.xiangxun.xacity.view.photoview.PhotoView;
import com.xiangxun.xacity.volley.VolleyError;
import com.xiangxun.xacity.volley.toolbox.ImageLoader.ImageContainer;

/**
 * @package: com.xiangxun.xacity.adapter
 * @ClassName: OccupyImageViewAdapter.java
 * @Description: PhoneView适配器
 * @author: HanGJ
 * @date: 2016-3-24 上午10:07:48
 */
public class OccupyImageViewAdapter extends PagerAdapter {
	private Context mContext = null;
	private List<OccupyImageBean> mPictures = null;

	public OccupyImageViewAdapter(Context context, List<OccupyImageBean> pictures) {
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
		View phoneViewLayout = View.inflate(mContext, R.layout.phone_view_layout, null);
		final ViewFlipper vfPhoneView = (ViewFlipper) phoneViewLayout.findViewById(R.id.vf_phone_view);
		vfPhoneView.setDisplayedChild(1);
		PhotoView photoView = (PhotoView) phoneViewLayout.findViewById(R.id.pv_deitail_picture);
		DcHttpClient.getInstance().requestImage(photoView, mPictures.get(position).path.equals("null") ? "" : Api.urlImage + mPictures.get(position).path, R.drawable.view_pager_default, new ResponseImageListener() {
			@Override
			public void onLoadingStarted() {
			}

			@Override
			public void onLoadingFailed(VolleyError error) {
			}

			@Override
			public void onLoadingComplete(ImageContainer response, boolean isImmediate) {
				vfPhoneView.setDisplayedChild(0);
			}
		});
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