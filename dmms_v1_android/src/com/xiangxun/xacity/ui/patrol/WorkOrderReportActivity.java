package com.xiangxun.xacity.ui.patrol;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.xiangxun.xacity.R;
import com.xiangxun.xacity.adapter.PublishPictureAdapter;
import com.xiangxun.xacity.app.BaseActivity;
import com.xiangxun.xacity.app.XiangXunApplication;
import com.xiangxun.xacity.bean.InfoCache;
import com.xiangxun.xacity.bean.PhotoInfo;
import com.xiangxun.xacity.bean.ResponseResultBeans.WorkOrderBean;
import com.xiangxun.xacity.bean.ResultBeans.GetPatrolBulderList;
import com.xiangxun.xacity.bean.ResultBeans.GetPatrolTypeList;
import com.xiangxun.xacity.bean.ResultBeans.PublishOrderData;
import com.xiangxun.xacity.bean.ResultBeans.Type;
import com.xiangxun.xacity.bean.ResultBeans.UpLoadResule;
import com.xiangxun.xacity.common.ConstantStatus;
import com.xiangxun.xacity.map.OnLocationUpdate;
import com.xiangxun.xacity.request.DcNetWorkUtils;
import com.xiangxun.xacity.ui.CameraActivity;
import com.xiangxun.xacity.ui.PhotoSelectActivity;
import com.xiangxun.xacity.utils.Logger;
import com.xiangxun.xacity.utils.Tools;
import com.xiangxun.xacity.view.DeletePictureInterface;
import com.xiangxun.xacity.view.LoadDialog;
import com.xiangxun.xacity.view.MsgToast;
import com.xiangxun.xacity.view.NoViewPager;
import com.xiangxun.xacity.view.PublishSelectTypeDialog;
import com.xiangxun.xacity.view.PublishSelectTypeDialog.onSelectItemClick;
import com.xiangxun.xacity.view.TitleView;

/**
 * @package: com.xiangxun.xacity.ui.patrol
 * @ClassName: WorkOrderReportActivity.java
 * @Description: 工单上报页面
 * @author: HanGJ
 * @date: 2016-2-3 上午8:38:39
 */
public class WorkOrderReportActivity extends BaseActivity implements OnClickListener, DeletePictureInterface, OnLocationUpdate, onSelectItemClick {
	private WorkOrderReportActivity mContext = null;
	private TitleView titleView;
	private TextView tvSubmit;
	private TextView tv_order_type;
	private LinearLayout ll_order_type_click;
	private TextView tv_order_org;
	private LinearLayout ll_order_org_click;
	private TextView tv_order_duty;
	private LinearLayout ll_order_duty_click;
	private LoadDialog loadDialog;
	// 添加图片
	private ImageView mIvAddPicture = null;
	private NoViewPager mVpAddPicture = null;
	// 定位位置
	private TextView tvLocation;
	// 描述--工单内容
	private LinearLayout mLlDescription = null;
	private LinearLayout ll_publish_code = null;
	private LinearLayout ll_publish_q = null;
	private LinearLayout ll_publish_desc = null;
	private LinearLayout ll_publish_click_desc = null;
	private LinearLayout ll_order_org = null;
	private LinearLayout add_picture_show = null;
	private LinearLayout ll_publish_click_address = null;
	private TextView tv_publish_desc = null;
	private TextView tv_publish_q = null;
	private TextView tv_publish_code = null;
	private TextView mTvDescription = null;
	private ImageView mIvDescriptionErrorTip = null;
	//
	private List<PhotoInfo> photoPaths;
	private PublishPictureAdapter mViewPagerAdapter = null;
	// 浮层
	private View mVSupernatant = null;
	private PopupWindow mPopupWindow = null;
	// 已经上传result
	private List<UpLoadResule> mUpLoadImageUrls = new ArrayList<UpLoadResule>();
	private String dicTypes;
	private PublishSelectTypeDialog orderType;
	private PublishSelectTypeDialog orderOrgType;
	private PublishSelectTypeDialog orderDutyType;
	private int isFlag = 0;
	private boolean isLocation = false;
	private WorkOrderBean order;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			loadDialog.dismiss();
			switch (msg.what) {
			case ConstantStatus.OccupyTypeSuccess:
				GetPatrolTypeList patrolTypeList = (GetPatrolTypeList) msg.obj;
				if (patrolTypeList != null) {
					List<Type> tourOrderType = patrolTypeList.tourOrderType;
					if (tourOrderType != null && tourOrderType.size() > 0) {
						orderType = new PublishSelectTypeDialog(mContext, tourOrderType, tv_order_type, "请选择工单类型");
					}
					List<Type> tourDutyType = patrolTypeList.tourDutyType;
					if (tourDutyType != null && tourDutyType.size() > 0) {
						orderOrgType = new PublishSelectTypeDialog(mContext, tourDutyType, tv_order_org, "请选择市政内/外");
						orderOrgType.setSelectItemClick(mContext);
					}
				}
				break;
			case ConstantStatus.OccupyStatusSuccess:
				GetPatrolBulderList typeBulders = (GetPatrolBulderList) msg.obj;
				if (typeBulders != null) {
					List<Type> types = typeBulders.type;
					if (types != null && types.size() > 0) {
						orderDutyType = new PublishSelectTypeDialog(mContext, types, tv_order_duty, "请选择责任单位");
					}
				}
				break;
			case ConstantStatus.OccupyTypeFailed:
				MsgToast.geToast().setMsg("数据加载失败~");
				break;
			case ConstantStatus.NetWorkError:
				MsgToast.geToast().setMsg("网络异常,请稍后重试!");
				break;
			case ConstantStatus.PublishOrderSuccess:
				MsgToast.geToast().setMsg("工单数据已更新...");
				finish();
				break;
			case ConstantStatus.PublishOrderFailed:
				MsgToast.geToast().setMsg("工单数据已更新失败,请重新操作~");
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.work_order_report_layout);
		mContext = WorkOrderReportActivity.this;
		initView();
		initData();
		initListener();
	}

	@Override
	public void initView() {
		titleView = (TitleView) findViewById(R.id.comment_title);
		mIvAddPicture = (ImageView) findViewById(R.id.iv_publish_add_picture);
		mVpAddPicture = (NoViewPager) findViewById(R.id.vp_publish_add_pictures);
		tvLocation = (TextView) findViewById(R.id.tv_publish_address);
		tv_publish_code = (TextView) findViewById(R.id.tv_publish_code);
		tv_publish_q = (TextView) findViewById(R.id.tv_publish_q);
		tv_publish_desc = (TextView) findViewById(R.id.tv_publish_desc);
		ll_order_org = (LinearLayout) findViewById(R.id.ll_order_org);
		ll_publish_desc = (LinearLayout) findViewById(R.id.ll_publish_desc);
		ll_publish_code = (LinearLayout) findViewById(R.id.ll_publish_code);
		ll_publish_q = (LinearLayout) findViewById(R.id.ll_publish_q);
		add_picture_show = (LinearLayout) findViewById(R.id.add_picture_show);
		ll_publish_click_desc = (LinearLayout) findViewById(R.id.ll_publish_click_desc);
		mLlDescription = (LinearLayout) findViewById(R.id.ll_publish_click_description);
		ll_publish_click_address = (LinearLayout) findViewById(R.id.ll_publish_click_address);
		mTvDescription = (TextView) findViewById(R.id.tv_publish_description);
		mIvDescriptionErrorTip = (ImageView) findViewById(R.id.iv_publish_description_tip);
		mVSupernatant = (View) findViewById(R.id.v_supernatant_background);
		tv_order_type = (TextView) findViewById(R.id.tv_order_type);
		ll_order_type_click = (LinearLayout) findViewById(R.id.ll_order_type_click);
		tv_order_org = (TextView) findViewById(R.id.tv_order_org);
		ll_order_org_click = (LinearLayout) findViewById(R.id.ll_order_org_click);
		tv_order_duty = (TextView) findViewById(R.id.tv_order_duty);
		ll_order_duty_click = (LinearLayout) findViewById(R.id.ll_order_duty_click);
		tvSubmit = titleView.getTitleViewOperationText();
	}

	@Override
	public void initData() {
		String title = getIntent().getStringExtra("title");
		if (title != null && title.length() > 0) {
			titleView.setTitle(title);
		} else {
			titleView.setTitle("工单上报");
		}
		tvSubmit.setText("提交");
		loadDialog = new LoadDialog(mContext);
		isFlag = getIntent().getIntExtra("isFlag", 0);
		photoPaths = new ArrayList<PhotoInfo>();
		mViewPagerAdapter = new PublishPictureAdapter(this, mVpAddPicture, photoPaths, mUpLoadImageUrls, ConstantStatus.ORDER);
		mVpAddPicture.setAdapter(mViewPagerAdapter);
		if (photoPaths.size() > 0) {
			mVpAddPicture.setVisibility(View.VISIBLE);
		}
		if (isFlag == 0) {// 工单上报
			loadDialog.setTitle("正在加载数据,请稍后...");
			XiangXunApplication.getInstance().addLocationListenner(this);
			XiangXunApplication.getInstance().requestLocation();
			dicTypes = "tourOrderType,tourDutyType";
			loadDialog.show();
			RequestList(dicTypes, 0);
		} else if (isFlag == 1) {// 现场说明
			ll_order_org.setVisibility(View.GONE);
			ll_publish_q.setVisibility(View.VISIBLE);
			ll_publish_click_desc.setVisibility(View.VISIBLE);
			ll_publish_desc.setVisibility(View.VISIBLE);
			order = (WorkOrderBean) getIntent().getSerializableExtra("bean");
			if (order != null) {
				ll_publish_code.setVisibility(View.VISIBLE);
				tv_publish_code.setText(Tools.isEmpty(order.workOrderCode));
				tv_order_type.setText(Tools.isEmpty(order.workEventType));
				tv_order_duty.setText(Tools.isEmpty(order.dutyOrgCode));
				mTvDescription.setText(Tools.isEmpty(order.workOrderText));
				tv_publish_desc.setText(Tools.isEmpty(order.siteDescription));
				tv_publish_q.setText(Tools.isEmpty(order.workDisposeRequest));
				tvLocation.setText(Tools.isEmpty(order.workEventPointExplain));
			}
		} else {
			add_picture_show.setVisibility(View.GONE);
			order = (WorkOrderBean) getIntent().getSerializableExtra("bean");
			if (order != null) {
				ll_publish_code.setVisibility(View.VISIBLE);
				tv_publish_code.setText(Tools.isEmpty(order.workOrderCode));
				tv_order_type.setText(Tools.isEmpty(order.workEventType));
				tv_order_org.setText(Tools.isEmpty(order.dutyOrgType));
				tv_order_duty.setText(Tools.isEmpty(order.dutyOrgCode));
				mTvDescription.setText(Tools.isEmpty(order.workOrderText));
				tv_publish_q.setText(Tools.isEmpty(order.workDisposeRequest));
				tvLocation.setText(Tools.isEmpty(order.workEventPointExplain));
			}
		}
	}

	private void RequestList(String dicTypes, int type) {
		DcNetWorkUtils.getPatrolBaseData(this, handler, type, account, password, dicTypes);
	}

	@Override
	public void initListener() {
		mIvAddPicture.setOnClickListener(this);
		titleView.setRightImageTextFlipper(mContext);
		if (isFlag == 0) {
			mLlDescription.setOnClickListener(mContext);
			ll_order_type_click.setOnClickListener(mContext);
			ll_order_org_click.setOnClickListener(mContext);
			ll_order_duty_click.setOnClickListener(mContext);
			ll_publish_click_address.setOnClickListener(mContext);
		} else if (isFlag == 1) {
			ll_publish_click_desc.setOnClickListener(mContext);
		} else {
			mLlDescription.setOnClickListener(mContext);
		}
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
			intentSpeech.putExtra("title", "工单内容");
			intentSpeech.putExtra("already_description", mTvDescription.getText().toString());
			startActivityForResult(intentSpeech, 999);
			break;
		case R.id.ll_publish_click_desc:
			Intent intentDesc = new Intent();
			intentDesc.setClass(mContext, PublishDescribeActivity.class);
			intentDesc.putExtra("title", "现场说明");
			intentDesc.putExtra("already_description", tv_publish_desc.getText().toString());
			startActivityForResult(intentDesc, 1000);
			break;
		case R.id.ll_order_type_click:
			if (orderType != null) {
				orderType.show();
			} else {
				RequestList(dicTypes, 0);
			}
			break;
		case R.id.ll_order_org_click:
			if (orderOrgType != null) {
				orderOrgType.show();
			} else {
				RequestList(dicTypes, 0);
			}
			break;
		case R.id.ll_order_duty_click:
			String info = tv_order_org.getText().toString();
			if (info.length() > 0) {
				if (orderDutyType != null) {
					orderDutyType.show();
				}
			} else {
				MsgToast.geToast().setMsg("请选择市政内/外类型");
			}
			break;
		case R.id.title_view_right_Flipper01:
			if (isFlag == 0) {
				showErrorTip();
			} else if (isFlag == 1) {
				if (tv_publish_desc.getText().toString().trim().equals("")) {
					MsgToast.geToast().setMsg("请输入工单现场说明");
					return;
				}
				if (order != null) {
					loadDialog.setTitle("正在提交数据,请稍后...");
					loadDialog.show();
					PublishOrderData publishInfo = new PublishOrderData();
					publishInfo.workOrderId = order.workOrderId;
					publishInfo.siteDescription = tv_publish_desc.getText().toString().trim();
					publishInfo.files = mUpLoadImageUrls;
					DcNetWorkUtils.publishOrderUrl(this, handler, publishInfo, account, password);
				} else {
					MsgToast.geToast().setMsg("工单数据异常,请重新处理");
				}
			} else {
				if (mTvDescription.getText().toString().trim().equals("")) {
					MsgToast.geToast().setMsg("请输入工单描述内容");
					return;
				}
				if (order != null) {
					loadDialog.setTitle("正在提交数据,请稍后...");
					loadDialog.show();
					PublishOrderData publishInfo = new PublishOrderData();
					publishInfo.workOrderId = order.workOrderId;
					publishInfo.workOrderText = mTvDescription.getText().toString().trim();
					publishInfo.files = mUpLoadImageUrls;
					DcNetWorkUtils.publishOrderUrl(this, handler, publishInfo, account, password);
				} else {
					MsgToast.geToast().setMsg("工单数据异常,请重新处理");
				}
			}
			break;
		case R.id.ll_publish_click_address:
			if (!isLocation && "定位失败,请点击重试".equals(tvLocation.getText().toString().trim())) {
				MsgToast.geToast().setLongMsg("正在定位...");
				tvLocation.setText("");
				XiangXunApplication.getInstance().addLocationListenner(this);
				XiangXunApplication.getInstance().requestLocation();
			}
			break;
		}
	}

	private void showErrorTip() {
		if (photoPaths.size() <= 0) {
			MsgToast.geToast().setMsg("请选择工单附件(图片)");
			return;
		}
		if (tv_order_type.getText().toString().trim().equals("")) {
			MsgToast.geToast().setMsg("请选择工单类型");
			return;
		}
		if (tv_order_org.getText().toString().trim().equals("")) {
			MsgToast.geToast().setMsg("请选择市政内/外类型");
			return;
		}
		if (tv_order_duty.getText().toString().trim().equals("")) {
			MsgToast.geToast().setMsg("请选择责任单位");
			return;
		}
		// if (tvLocation.getText().toString().trim().equals("")) {
		// MsgToast.geToast().setMsg("请选择责任单位");
		// return;
		// }
		if (mTvDescription.getText().toString().trim().equals("")) {
			MsgToast.geToast().setMsg("请输入工单描述内容");
			return;
		}
		if(mUpLoadImageUrls.size() != photoPaths.size()){
			MsgToast.geToast().setMsg("图片附件正在上传,请稍后...");
			return;
		}
		loadDialog.setTitle("正在提交数据,请稍后...");
		loadDialog.show();
		// 设置信息对象数值
		PublishOrderData publishInfo = publishInfo();
		DcNetWorkUtils.publishOrderUrl(this, handler, publishInfo, account, password);

	}

	private PublishOrderData publishInfo() {
		PublishOrderData orderData = new PublishOrderData();
		Type workEventType = (Type) tv_order_type.getTag();
		Type dutyOrgType = (Type) tv_order_org.getTag();
		Type dutyOrgId = (Type) tv_order_duty.getTag();
		String workOrderText = mTvDescription.getText().toString().trim();
		orderData.workOrderText = workOrderText;
		orderData.workEventType = workEventType.code;
		orderData.dutyOrgCode = dutyOrgId.code;
		orderData.dutyOrgType = dutyOrgType.code;
		orderData.files = mUpLoadImageUrls;
		if (TextUtils.isEmpty(workEventPointLatlon) || TextUtils.isEmpty(workEventPointExplain)) {
			orderData.workEventPointLatlon = "108.940612,34.247593";
			orderData.workEventPointExplain = "陕西省西安市碑林区太白北路156号-b";
		} else {
			orderData.workEventPointLatlon = workEventPointLatlon;
			orderData.workEventPointExplain = workEventPointExplain;
		}
		return orderData;
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
					if(photoPaths.size() < 5){
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
					if(photoPaths.size() < 5){
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
			case 1000:
				String desc = data.getStringExtra("description");
				tv_publish_desc.setText(desc);
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
		if(photoPaths.size() < 5){
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

	private String workEventPointLatlon = "";
	private String workEventPointExplain = "";

	@Override
	public void update(BDLocation location) {
		if (location != null) {
			if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
				isLocation = true;
				Logger.i("gps定位成功");
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
				isLocation = true;
				Logger.i("网络定位成功");
			} else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
				isLocation = true;
				Logger.i("离线定位成功，离线定位结果也是有效的");
			} else if (location.getLocType() == BDLocation.TypeServerError) {
				Logger.i("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
				tvLocation.setText(R.string.city_location_fail);
				tvLocation.setTextColor(Color.RED);
				isLocation = false;
				return;
			} else if (location.getLocType() == BDLocation.TypeNetWorkException) {
				Logger.i("网络不同导致定位失败，请检查网络是否通畅");
				tvLocation.setText(R.string.city_location_fail);
				tvLocation.setTextColor(Color.RED);
				isLocation = false;
				return;
			} else if (location.getLocType() == BDLocation.TypeCriteriaException) {
				Logger.i("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
				tvLocation.setText(R.string.city_location_fail);
				tvLocation.setTextColor(Color.RED);
				isLocation = false;
				return;
			}
			workEventPointExplain = location.getAddrStr();
			XiangXunApplication.getInstance().removeLocationListenner(this);
			if (!TextUtils.isEmpty(XiangXunApplication.getInstance().getmLocationCity())) {
				isLocation = true;
				workEventPointLatlon = location.getLongitude() + "," + location.getLatitude();
				tvLocation.setText(InfoCache.getInstance().getmAddr());
				tvLocation.setTextColor(getResources().getColor(R.color.color333333));
			} else {
				isLocation = false;
				tvLocation.setText(R.string.city_location_fail);
				tvLocation.setTextColor(Color.RED);
			}
		} else {
			isLocation = false;
			tvLocation.setText(R.string.city_location_fail);
			tvLocation.setTextColor(Color.RED);
		}
	}

	@Override
	public void changeState(Type type) {
		tv_order_duty.setText("");
		String dicTypes = type.code;
		RequestList(dicTypes, 1);
	}

}
