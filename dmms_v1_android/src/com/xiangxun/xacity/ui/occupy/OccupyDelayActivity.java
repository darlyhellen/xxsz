package com.xiangxun.xacity.ui.occupy;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xiangxun.xacity.R;
import com.xiangxun.xacity.adapter.PublishPictureAdapter;
import com.xiangxun.xacity.adapter.SurveyTypeAdapter;
import com.xiangxun.xacity.app.BaseActivity;
import com.xiangxun.xacity.bean.PhotoInfo;
import com.xiangxun.xacity.bean.ResultBeans.Type;
import com.xiangxun.xacity.bean.ResultBeans.UpLoadResule;
import com.xiangxun.xacity.common.ConstantStatus;
import com.xiangxun.xacity.request.DcNetWorkUtils;
import com.xiangxun.xacity.ui.CameraActivity;
import com.xiangxun.xacity.ui.PhotoSelectActivity;
import com.xiangxun.xacity.ui.patrol.PublishDescribeActivity;
import com.xiangxun.xacity.utils.DateTimePickDialogUtil;
import com.xiangxun.xacity.utils.Logger;
import com.xiangxun.xacity.utils.Tools;
import com.xiangxun.xacity.view.DeletePictureInterface;
import com.xiangxun.xacity.view.LoadDialog;
import com.xiangxun.xacity.view.MsgToast;
import com.xiangxun.xacity.view.NoViewPager;
import com.xiangxun.xacity.view.TitleView;

/**
 * @package: com.xiangxun.xacity.ui.occupy
 * @ClassName: OccupyDelayActivity.java
 * @Description: 项目延期页面
 * @author: HanGJ
 * @date: 2016-3-15 下午5:03:29
 */
public class OccupyDelayActivity extends BaseActivity implements OnClickListener, DeletePictureInterface {
	private OccupyDelayActivity mContext = null;
	private TitleView titleView;
	private String id = "";
	private ListView xlistview = null;
	private EditText startDelayTime;
	private EditText endDelayTime;
	// 添加图片
	private ImageView mIvAddPicture = null;
	private NoViewPager mVpAddPicture = null;
	// 描述--备注
	private LinearLayout mLlDescription = null;
	private TextView mTvDescription = null;
	//
	private List<PhotoInfo> photoPaths;
	private PublishPictureAdapter mViewPagerAdapter = null;
	// 浮层
	private View mVSupernatant = null;
	private PopupWindow mPopupWindow = null;
	private LoadDialog loadDialog;
	private SurveyTypeAdapter adapter;
	private TextView tvSubmit;
	private String startTime;
	private String endTime;
	private String endtimeStr;
	private List<Type> datas = new ArrayList<Type>();
	// 已经上传result_Url
	private List<UpLoadResule> mUpLoadImageUrls = new ArrayList<UpLoadResule>();

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ConstantStatus.VerifyResultSuccess:
				MsgToast.geToast().setMsg("项目延期信息上报成功");
				mContext.finish();
				break;
			case ConstantStatus.VerifyResultFailed:
				loadDialog.dismiss();
				MsgToast.geToast().setMsg("项目延期信息上报失败,请重试");
				break;
			case ConstantStatus.SurveySuccess:
				loadDialog.dismiss();
				List<Type> surveyTypes = (List<Type>) msg.obj;
				if (surveyTypes != null && surveyTypes.size() > 0) {
					datas.addAll(surveyTypes);
					adapter.setData(datas);
					xlistview.setAdapter(adapter);
				}
				break;
			case ConstantStatus.NetWorkError:
				loadDialog.dismiss();
				MsgToast.geToast().setMsg("网络异常,请稍后重试!");
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.occupy_delay_layout);
		mContext = OccupyDelayActivity.this;
		initView();
		initData();
		initListener();
	}

	@Override
	public void initView() {
		titleView = (TitleView) findViewById(R.id.comment_title);
		mIvAddPicture = (ImageView) findViewById(R.id.iv_publish_add_picture);
		mVpAddPicture = (NoViewPager) findViewById(R.id.vp_publish_add_pictures);
		mLlDescription = (LinearLayout) findViewById(R.id.ll_publish_click_description);
		mTvDescription = (TextView) findViewById(R.id.tv_publish_description);
		startDelayTime = (EditText) findViewById(R.id.start_delay_time);
		endDelayTime = (EditText) findViewById(R.id.end_delay_time);
		xlistview = (ListView) findViewById(R.id.xlistview);
		mVSupernatant = (View) findViewById(R.id.v_supernatant_background);
		tvSubmit = titleView.getTitleViewOperationText();
	}

	@Override
	public void initData() {
		titleView.setTitle("项目延期");
		id = getIntent().getStringExtra("id");
		endtimeStr = getIntent().getStringExtra("endtimeStr");
		photoPaths = new ArrayList<PhotoInfo>();
		tvSubmit.setText("提交");
		mViewPagerAdapter = new PublishPictureAdapter(this, mVpAddPicture, photoPaths, mUpLoadImageUrls, ConstantStatus.DELAY);
		mVpAddPicture.setAdapter(mViewPagerAdapter);
		if (photoPaths.size() > 0) {
			mVpAddPicture.setVisibility(View.VISIBLE);
		}
		adapter = new SurveyTypeAdapter(mContext);
		loadDialog = new LoadDialog(mContext);
		loadDialog.setTitle("正在加载数据,请稍后...");
		startDelayTime.setText(endtimeStr);
		RequestList();
	}

	@Override
	public void initListener() {
		mIvAddPicture.setOnClickListener(mContext);
		mLlDescription.setOnClickListener(mContext);
		startDelayTime.setOnClickListener(mContext);
		endDelayTime.setOnClickListener(mContext);
		titleView.setRightImageTextFlipper(mContext);
		titleView.setLeftBackOneListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}

	private void RequestList() {
		loadDialog.show();
		DcNetWorkUtils.getOccupyDelayDescript(mContext, handler, account, password);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.start_delay_time:
			new DateTimePickDialogUtil(this).dateTimePicKDialog(startDelayTime);
			break;
		case R.id.end_delay_time:
			new DateTimePickDialogUtil(this).dateTimePicKDialog(endDelayTime);
			break;
		case R.id.iv_publish_add_picture:
			showSelectAddPath(); // 选择添加图片方式
			mVSupernatant.setVisibility(View.VISIBLE);
			break;
		case R.id.tv_add_picture_from_camera:
			Intent intentCamera = new Intent(mContext, CameraActivity.class);
			intentCamera.putExtra("size", photoPaths.size());
			intentCamera.putExtra("total", 1);
			intentCamera.setAction("publishFourPhotos");
			startActivityForResult(intentCamera, 0);
			mPopupWindow.dismiss();
			mVSupernatant.setVisibility(View.GONE);
			break;
		case R.id.tv_add_picture_from_album:
			Intent intentAlbum = new Intent();
			intentAlbum.putExtra("size", photoPaths.size());
			intentAlbum.putExtra("total", 1);
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
			intentSpeech.putExtra("already_description", mTvDescription.getText().toString());
			startActivityForResult(intentSpeech, 999);
			break;
		case R.id.title_view_right_Flipper01:
			if (isDataInfo()) {
				loadDialog.setTitle("正在提交数据,请稍后...");
				loadDialog.show();
				List<String> codes = adapter.getCodes();
				StringBuffer buffer = new StringBuffer();
				if (codes.size() > 1) {
					for (int i = 0; i < codes.size(); i++) {
						if ((codes.size() - 1) == i) {
							buffer.append(codes.get(i));
						} else {
							buffer.append(codes.get(i)).append(",");
						}
					}
				} else {
					buffer.append(codes.get(0));
				}
				String descript = mTvDescription.getText().toString().trim();
				DcNetWorkUtils.getOccupyDelayUpload(mContext, handler, mUpLoadImageUrls, account, password, id, startTime, endTime, buffer.toString(), descript);
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

	private boolean isDataInfo() {
		if (photoPaths.size() <= 0) {
			MsgToast.geToast().setMsg("请选择图片,至少一张");
			return false;
		}
		startTime = startDelayTime.getText().toString().trim();
		if (startTime.length() <= 0) {
			MsgToast.geToast().setMsg("请输入延期开始时间");
			return false;
		}
		if(Tools.computeTimeStr(endtimeStr + " 00:00", startTime + " 00:00").equals("-1")){
			MsgToast.geToast().setMsg("延期开始时间必须大于等于项目截止时间~");
			return false;
		}
		endTime = endDelayTime.getText().toString().trim();
		if (endTime.length() <= 0) {
			MsgToast.geToast().setMsg("请输入延期结束时间");
			return false;
		}
		if(Tools.computeTime(startTime + " 00:00", endTime + " 00:00").equals("-1")){
			MsgToast.geToast().setMsg("延期结束时间必须大于延期开始时间~");
			return false;
		}
		if (adapter.getCodes().size() <= 0) {
			MsgToast.geToast().setMsg("请选择延期原因");
			return false;
		}
		return true;
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
					// 只取前1张图片
					if (photoPaths.size() > 1) {
						photoPaths = photoPaths.subList(0, 1);
						MsgToast.geToast().setMsg("最多添加1张照片噢");
					}
					if (photoPaths.size() > 0) {
						mVpAddPicture.setVisibility(View.VISIBLE);
					}
					if (photoPaths.size() < 1) {
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
					// 只取前1张图片
					if (photoPaths.size() > 1) {
						photoPaths = photoPaths.subList(0, 1);
						MsgToast.geToast().setMsg("最多添加1张照片噢");
					}
					if (photoPaths.size() > 0) {
						mVpAddPicture.setVisibility(View.VISIBLE);
					}
					if (photoPaths.size() < 1) {
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
		if (photoPaths.size() < 1) {
			mIvAddPicture.setVisibility(View.VISIBLE);
		} else {
			mIvAddPicture.setVisibility(View.GONE);
		}
		for (int i = 0, size = photoPaths.size(); i < size; i++) {
			if (photoPaths.get(i).statu == PhotoInfo.STATUSUCCESS) {
				mUpLoadImageUrls.add(photoPaths.get(i).loadResule);
			}
			Logger.e("全屏返回后图片=" + photoPaths.get(i));
		}
		mViewPagerAdapter.notifyDataSetChanged();
	}

}
