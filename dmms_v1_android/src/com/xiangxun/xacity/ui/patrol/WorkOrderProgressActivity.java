package com.xiangxun.xacity.ui.patrol;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xiangxun.xacity.R;
import com.xiangxun.xacity.adapter.PublishPictureAdapter;
import com.xiangxun.xacity.app.BaseActivity;
import com.xiangxun.xacity.bean.PhotoInfo;
import com.xiangxun.xacity.bean.ResponseResultBeans.WorkOrderBean;
import com.xiangxun.xacity.bean.ResultBeans.OrderProgressData;
import com.xiangxun.xacity.bean.ResultBeans.UpLoadResule;
import com.xiangxun.xacity.common.ConstantStatus;
import com.xiangxun.xacity.request.DcNetWorkUtils;
import com.xiangxun.xacity.ui.CameraActivity;
import com.xiangxun.xacity.ui.PhotoSelectActivity;
import com.xiangxun.xacity.utils.Logger;
import com.xiangxun.xacity.view.DeletePictureInterface;
import com.xiangxun.xacity.view.LoadDialog;
import com.xiangxun.xacity.view.MsgToast;
import com.xiangxun.xacity.view.NoViewPager;
import com.xiangxun.xacity.view.TitleView;

/**
 * @package: com.xiangxun.xacity.ui.patrol
 * @ClassName: WorkOrderProgressActivity.java
 * @Description: 工单进展描述页面
 * @author: HanGJ
 * @date: 2016-4-15 下午3:35:29
 */
public class WorkOrderProgressActivity extends BaseActivity implements OnClickListener, DeletePictureInterface {
	private WorkOrderProgressActivity mContext = null;
	private TitleView titleView;
	private TextView tvSubmit;
	private EditText mETUserInfo = null;
	private TextView mTvDescription = null;
	// 描述--工单内容
	private LinearLayout mLlDescription = null;
	// 添加图片
	private ImageView mIvAddPicture = null;
	private NoViewPager mVpAddPicture = null;
	//
	private List<PhotoInfo> photoPaths;
	private PublishPictureAdapter mViewPagerAdapter = null;
	// 浮层
	private View mVSupernatant = null;
	private PopupWindow mPopupWindow = null;
	// 已经上传result
	private List<UpLoadResule> mUpLoadImageUrls = new ArrayList<UpLoadResule>();
	private LoadDialog loadDialog;
	private WorkOrderBean order;
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			loadDialog.dismiss();
			switch (msg.what) {
			case ConstantStatus.NetWorkError:
				MsgToast.geToast().setMsg("网络异常,请稍后重试!");
				break;
			case ConstantStatus.PublishOrderSuccess:
				MsgToast.geToast().setMsg("工单进展数据已上传");
				finish();
				break;
			case ConstantStatus.PublishOrderFailed:
				MsgToast.geToast().setMsg("工单进展数据上传失败,请重新上传~");
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.patrol_order_progress_layout);
		mContext = this;
		initView();
		initData();
		initListener();
	}

	@Override
	public void initView() {
		titleView = (TitleView) findViewById(R.id.comment_title);
		tvSubmit = titleView.getTitleViewOperationText();
		mIvAddPicture = (ImageView) findViewById(R.id.iv_publish_add_picture);
		mVpAddPicture = (NoViewPager) findViewById(R.id.vp_publish_add_pictures);
		mVSupernatant = (View) findViewById(R.id.v_supernatant_background);
		mETUserInfo = (EditText) findViewById(R.id.tv_publish_user);
		mLlDescription = (LinearLayout) findViewById(R.id.ll_publish_click_description);
		mTvDescription = (TextView) findViewById(R.id.tv_publish_description);
	}

	@Override
	public void initData() {
		titleView.setTitle("工单进展");
		tvSubmit.setText("提交");
		photoPaths = new ArrayList<PhotoInfo>();
		order = (WorkOrderBean) getIntent().getSerializableExtra("bean");
		mViewPagerAdapter = new PublishPictureAdapter(this, mVpAddPicture, photoPaths, mUpLoadImageUrls, ConstantStatus.ORDER);
		mVpAddPicture.setAdapter(mViewPagerAdapter);
		if (photoPaths.size() > 0) {
			mVpAddPicture.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void initListener() {
		mIvAddPicture.setOnClickListener(this);
		mLlDescription.setOnClickListener(this);
		titleView.setRightImageTextFlipper(mContext);
		titleView.setLeftBackOneListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_publish_add_picture:
			showSelectAddPath(); // 选择添加图片方式
			mVSupernatant.setVisibility(View.VISIBLE);
			break;
		case R.id.tv_add_picture_from_camera:
			Intent intentCamera = new Intent(mContext, CameraActivity.class);
			intentCamera.putExtra("size", photoPaths.size());
			intentCamera.putExtra("total", 5);
			intentCamera.setAction("publishFourPhotos");
			startActivityForResult(intentCamera, 0);
			mPopupWindow.dismiss();
			mVSupernatant.setVisibility(View.GONE);
			break;
		case R.id.tv_add_picture_from_album:
			Intent intentAlbum = new Intent();
			intentAlbum.putExtra("size", photoPaths.size());
			intentAlbum.putExtra("total", 5);
			intentAlbum.setAction("publishFourPhotos");
			intentAlbum.setClass(mContext, PhotoSelectActivity.class);
			startActivityForResult(intentAlbum, 99);
			mPopupWindow.dismiss();
			mVSupernatant.setVisibility(View.GONE);
			break;
		case R.id.tv_add_picture_cancle:
			mPopupWindow.dismiss();
			mVSupernatant.setVisibility(View.GONE);
			break;
		case R.id.ll_publish_click_description:
			Intent intentSpeech = new Intent();
			intentSpeech.setClass(mContext, PublishDescribeActivity.class);
			intentSpeech.putExtra("title", "工单进展");
			intentSpeech.putExtra("already_description", mTvDescription.getText().toString());
			startActivityForResult(intentSpeech, 999);
			break;
		case R.id.title_view_right_Flipper01:
			if (mTvDescription.getText().toString().trim().equals("")) {
				MsgToast.geToast().setMsg("请输入工单处理结果内容");
				return;
			}
			if (order != null) {
				loadDialog = new LoadDialog(mContext);
				loadDialog.show();
				OrderProgressData progressData = new OrderProgressData();
				progressData.files = mUpLoadImageUrls;
				progressData.workOrderId = order.workOrderId;
				progressData.disposeText = mTvDescription.getText().toString().trim();
				progressData.disposeType = "处理反馈";
				progressData.acceptName = TextUtils.isEmpty(mETUserInfo.getText().toString()) ? "" : mETUserInfo.getText().toString().trim();
				DcNetWorkUtils.publishOrderProgressUrl(this, handler, progressData, account, password);
			} else {
				MsgToast.geToast().setMsg("工单数据异常,请重新处理");
			}
			break;
		}
	}

	@SuppressWarnings("deprecation")
	private void showSelectAddPath() {
		LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
		View popupwindowView = layoutInflater.inflate(R.layout.add_picture_popup_window, null);
		TextView tvAddFromCamera = (TextView) popupwindowView.findViewById(R.id.tv_add_picture_from_camera);
		TextView tvAddFromAlbum = (TextView) popupwindowView.findViewById(R.id.tv_add_picture_from_album);
		TextView tvAddCancle = (TextView) popupwindowView.findViewById(R.id.tv_add_picture_cancle);
		tvAddFromCamera.setOnClickListener(mContext);
		tvAddFromAlbum.setOnClickListener(mContext);
		tvAddCancle.setOnClickListener(mContext);
		mPopupWindow = new PopupWindow(popupwindowView, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		mPopupWindow.setContentView(popupwindowView);
		mPopupWindow.setFocusable(true);
		mPopupWindow.setOutsideTouchable(false);
		mPopupWindow.setWidth(LayoutParams.MATCH_PARENT);
		mPopupWindow.setHeight(LayoutParams.WRAP_CONTENT);
		mPopupWindow.setAnimationStyle(R.style.Add_Picture_AnimationFade);
		mPopupWindow.showAtLocation(popupwindowView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case 0:
				if (data != null) {
					@SuppressWarnings("unchecked")
					List<PhotoInfo> photos = (List<PhotoInfo>) data.getSerializableExtra("camera_picture");
					photoPaths.addAll(photos);
					// 只取前5张图片
					if (photoPaths.size() > 5) {
						photoPaths = photoPaths.subList(0, 5);
						MsgToast.geToast().setMsg("最多添加5照片噢");
					}
					if (photoPaths.size() > 0) {
						mVpAddPicture.setVisibility(View.VISIBLE);
					}
					if (photoPaths.size() < 5) {
						mIvAddPicture.setVisibility(View.VISIBLE);
					} else {
						mIvAddPicture.setVisibility(View.GONE);
					}
					mViewPagerAdapter.notifyDataSetChanged();
				}
				break;
			case 99:
				if (data != null) {
					@SuppressWarnings("unchecked")
					List<PhotoInfo> photos = (List<PhotoInfo>) data.getSerializableExtra("album_picture");
					photoPaths.addAll(photos);
					// 只取前5张图片
					if (photoPaths.size() > 5) {
						photoPaths = photoPaths.subList(0, 5);
						MsgToast.geToast().setMsg("最多添加5照片噢");
					}
					if (photoPaths.size() > 0) {
						mVpAddPicture.setVisibility(View.VISIBLE);
					}
					if (photoPaths.size() < 5) {
						mIvAddPicture.setVisibility(View.VISIBLE);
					} else {
						mIvAddPicture.setVisibility(View.GONE);
					}
					mViewPagerAdapter.notifyDataSetChanged();
				}
				break;
			case 999:
				String description = data.getStringExtra("description");
				mTvDescription.setText(description);
				break;
			}
		}
	}

	@Override
	public void getPictures(List<PhotoInfo> photos) {
		photoPaths = photos;
		mUpLoadImageUrls.clear();
		if (photoPaths.size() == 0) {
			mVpAddPicture.setVisibility(View.GONE);
		}
		if (photoPaths.size() < 5) {
			mIvAddPicture.setVisibility(View.VISIBLE);
		} else {
			mIvAddPicture.setVisibility(View.GONE);
		}

		for (int i = 0, size = photoPaths.size(); i < size; i++) {
			if (photoPaths.get(i).statu == PhotoInfo.STATUSUCCESS) {
				mUpLoadImageUrls.add(photoPaths.get(i).loadResule);
			}
			Logger.e("全屏返回后图片=" + photoPaths.get(i).result);
		}
		mViewPagerAdapter.notifyDataSetChanged();
	}

}
