package com.xiangxun.xacity.ui.patrol;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ViewFlipper;

import com.xiangxun.xacity.R;
import com.xiangxun.xacity.app.BaseActivity;
import com.xiangxun.xacity.bean.ResponseResultBeans.WorkOrderBean;
import com.xiangxun.xacity.bean.ResultBeans.GPSData;
import com.xiangxun.xacity.bean.ResultBeans.OrgType;
import com.xiangxun.xacity.bean.ResultBeans.OrgUserType;
import com.xiangxun.xacity.bean.ResultBeans.PublishOrderData;
import com.xiangxun.xacity.bean.ResultBeans.UpLoadResule;
import com.xiangxun.xacity.common.ConstantStatus;
import com.xiangxun.xacity.request.DcNetWorkUtils;
import com.xiangxun.xacity.ui.patrol.OrgListViewAdapter.CheckBoxClick;
import com.xiangxun.xacity.utils.MyUtils;
import com.xiangxun.xacity.view.LoadDialog;
import com.xiangxun.xacity.view.LocationDateDialog;
import com.xiangxun.xacity.view.LocationDateDialog.LocationDateClick;
import com.xiangxun.xacity.view.MsgToast;
import com.xiangxun.xacity.view.TitleView;
import com.xiangxun.xacity.view.treelist.TreeFileBean;
import com.xiangxun.xacity.view.treelist.TreeListViewAdapter;
import com.xiangxun.xacity.view.treelist.TreeListViewAdapter.OnTreeNodeClickListener;
import com.xiangxun.xacity.view.treelist.TreeNode;

/**
 * @package: com.xiangxun.xacity.ui.patrol
 * @ClassName: PersonnelLocationActivity.java
 * @Description: 通讯录页面
 * @author: ZhangJP
 * @date: 2016-4-5 上午11:10:00
 */
public class PersonnelLocationActivity extends BaseActivity implements CheckBoxClick, OnClickListener, LocationDateClick {
	private static int listState = -1;

	private static int TREE_LIST = 0;
	private static int LOAD_DATA = 1;
	private static int NO_DATA = 2;
	private static int SIGNLE_LIST = 3;

	private TitleView titleView;
	private ListView mListViewTree;
	private ListView mListViewList;
	private ViewFlipper mVF;
	private ImageView btnSearch;
	private EditText edtSearch;
	// 定位、轨迹记录
	private LinearLayout mLocation;
	// 通讯录
	private OrgType org;
	private List<OrgUserType> user;
	private List<TreeFileBean> mTreeDatas = new ArrayList<TreeFileBean>();
	private TreeListViewAdapter<TreeFileBean> mTreeAdapter;
	private OrgListViewAdapter mListAdapter;
	private Button mBtnLocation;
	private Button mBtnPath;
	private int isFlag = 0;
	private LoadDialog loadDialog;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			loadDialog.dismiss();
			switch (msg.what) {
			case ConstantStatus.OrgSuccess:
				org = (OrgType) msg.obj;
				if (org != null) {
					// 转换通讯录数据到list
					convertDataToTree(org, 0);
					initListTreeAdapter();
					mVF.setDisplayedChild(TREE_LIST);
					// 不再抓取数据
					listState = ConstantStatus.listStateRefresh;
				} else {
					mVF.setDisplayedChild(NO_DATA);
				}
				break;
			case ConstantStatus.OrgUserSuccess:
				user = (List<OrgUserType>) msg.obj;
				if (user != null && user.size() > 0) {
					// 转换通讯录数据到list
					initListAdapter(user);
					mVF.setDisplayedChild(SIGNLE_LIST);
				} else {
					if (user.size() <= 0) {
						mVF.setDisplayedChild(NO_DATA);
					}
				}
				break;
			case ConstantStatus.OrgSearchSuccess:
				user = (List<OrgUserType>) msg.obj;
				if (user != null && user.size() > 0) {
					// 转换通讯录数据到list
					initListAdapter(user);
					mVF.setDisplayedChild(SIGNLE_LIST);
				} else {
					if (user.size() <= 0) {
						MsgToast.geToast().setMsg(R.string.search_nofind);
					}
				}
				break;
			case ConstantStatus.OrgSearchFailed:
				MsgToast.geToast().setMsg(R.string.search_nofind);
				break;
			case ConstantStatus.OrgFailed:
				mVF.setDisplayedChild(NO_DATA);
				break;
			case ConstantStatus.NetWorkError:
				MsgToast.geToast().setMsg(R.string.networkerror);
				break;
			case ConstantStatus.PublishOrderSuccess:
				MsgToast.geToast().setMsg("工单已下发");
				finish();
				break;
			case ConstantStatus.PublishOrderFailed:
				MsgToast.geToast().setMsg("工单下发失败,请重新下发~");
				break;
			case ConstantStatus.LocationConfigSuccess:
				GPSData data = (GPSData) msg.obj;
				if (data != null) {
					String longitude = data.longitude;
					String latitude = data.latitude;
					if (longitude != null && longitude.length() > 0 && latitude != null && latitude.length() > 0) {
						OrgUserType orgUserType = user.get(userNum);
						Intent intent = new Intent();
						intent.putExtra("data", (Serializable) data);
						intent.putExtra("name", orgUserType.locateRegName);
						setResult(200, intent);
						finish();
					} else {
						MsgToast.geToast().setMsg("无定位数据");
					}
				} else {
					MsgToast.geToast().setMsg("无定位数据");
				}
				break;
			case ConstantStatus.LocationConfigFailed:
				MsgToast.geToast().setMsg("人员定位失败,请重新定位~");
				break;
			case ConstantStatus.LocationListSuccess:
				List<GPSData> datas = (List<GPSData>) msg.obj;
				if (datas != null && datas.size() > 0) {
					Intent intent = new Intent();
					OrgUserType orgUserType = user.get(userNum);
					intent.putExtra("name", orgUserType.locateRegName);
					intent.putExtra("datas", (Serializable) datas);
					setResult(201, intent);
					finish();
				} else {
					MsgToast.geToast().setMsg("无历史轨迹数据");
				}
				break;
			case ConstantStatus.LocationListFailed:
				MsgToast.geToast().setMsg("获取轨迹数据失败,请重新获取~");
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.patrol_contact_layout);
		initView();
		initData();
		initListener();
	}

	@Override
	public void initView() {
		titleView = (TitleView) findViewById(R.id.comment_title);
		mListViewTree = (ListView) findViewById(R.id.elv_mine_list);
		mListViewList = (ListView) findViewById(R.id.xlistview);
		mVF = (ViewFlipper) findViewById(R.id.vf_comment);

		mLocation = (LinearLayout) findViewById(R.id.location);
		mBtnLocation = (Button) findViewById(R.id.btn_location);
		mBtnPath = (Button) findViewById(R.id.btn_path);
		edtSearch = (EditText) findViewById(R.id.toolbar_search_text);
		btnSearch = (ImageView) findViewById(R.id.toolbar_search_btn);
		btnSearch.setClickable(true);
		btnSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MyUtils.closeInputMethodWindow(getApplicationContext(), edtSearch);
				String key = edtSearch.getText().toString();
				if (key == null || key.equals("")) {
					MsgToast.geToast().setMsg(R.string.search_key_null);
				} else {
					if (mVF.getDisplayedChild() == TREE_LIST) {
						RequestSearchPerson(key);
					} else {
						List<OrgUserType> person = searchKey(key);
						if (person.size() > 0) {
							initListAdapter(person);
							mVF.setDisplayedChild(SIGNLE_LIST);
						} else {
							MsgToast.geToast().setMsg(R.string.search_nofind);
						}
					}
				}
			}
		});
	}

	@Override
	public void initData() {
		String title = getIntent().getStringExtra("title");
		if (title != null && title.length() > 0) {
			titleView.setTitle(title);
		} else {
			titleView.setTitle("人员定位");
		}

		listState = ConstantStatus.listStateFirst;
		loadDialog = new LoadDialog(this);
		isFlag = getIntent().getIntExtra("isFlag", 0);
		if (isFlag == 1) {
			mBtnLocation.setText("确定");
			mBtnPath.setText("取消");
		}
		RequestTreeList();
	}

	/**
	 * 获取群组信息
	 */
	private void RequestTreeList() {
		switch (listState) {
		case ConstantStatus.listStateFirst:
			if (org != null) {
				org = null;
			}
			if (mTreeDatas != null) {
				mTreeDatas.clear();
			}

			mVF.setDisplayedChild(1);
			DcNetWorkUtils.getPatrolOrgData(this, handler, account, password);
			break;
		case ConstantStatus.listStateRefresh:
			mVF.setDisplayedChild(0);
			break;
		}
	}

	/**
	 * 获取群组通讯录
	 * 
	 * @param groupID
	 */
	private void RequestUserList(String groupID) {
		if (user != null) {
			user.clear();
			user = null;
		}
		DcNetWorkUtils.getPatrolOrgUserData(this, handler, account, password, groupID);
	}

	/**
	 * 查找群组通讯录
	 * 
	 * @param groupID
	 */
	private void RequestSearchPerson(String key) {
		if (user != null) {
			user.clear();
			user = null;
		}
		DcNetWorkUtils.getPatrolSearchOrgUser(this, handler, account, password, key);
	}

	@Override
	public void initListener() {
		mBtnLocation.setOnClickListener(this);
		mBtnPath.setOnClickListener(this);
		titleView.setLeftBackOneListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mVF.getDisplayedChild() == SIGNLE_LIST || mVF.getDisplayedChild() == NO_DATA) {
					mVF.setDisplayedChild(TREE_LIST);
					mLocation.setVisibility(View.GONE);
				} else
					onBackPressed();
			}
		});
	}

	/*
	 * 转换通讯录数据到Tree
	 */
	private void convertDataToTree(OrgType data, int parent) {
		int num = mTreeDatas.size() + 1;
		int fa = parent;
		int faChild;

		OrgType item = data;

		if (data != null) {
			mTreeDatas.add(new TreeFileBean(num, fa, item.orgName, item.orgId));
			faChild = num++;

			// child
			if (item.child != null && item.child.size() > 0) {
				int length = item.child.size();
				for (int i = 0; i < length; i++) {
					convertDataToTree(item.child.get(i), faChild);
					num = mTreeDatas.size() + 1;
				}
			}
		}
	}

	public void initListAdapter(List<OrgUserType> data) {
		mListAdapter = new OrgListViewAdapter(this, data);
		mListViewList.setAdapter(mListAdapter);
		mListAdapter.setCheckBoxClick(this);
	}

	private List<OrgUserType> searchKey(String key) {
		List<OrgUserType> person = new ArrayList<OrgUserType>();
		int length = user.size();
		if (user != null) {
			for (int j = 0; j < length; j++) {
				OrgUserType item = user.get(j);
				if (item.locateRegName.contains(key)) {
					person.add(item);
				}
			}
		}

		return person;
	}

	private void initListTreeAdapter() {

		try {
			mTreeAdapter = new TreeListViewAdapter<TreeFileBean>(mListViewTree, this, mTreeDatas, 10);
			mTreeAdapter.setOnTreeNodeClickListener(new OnTreeNodeClickListener() {
				@Override
				public void onClick(TreeNode node, int position) {
					MyUtils.closeInputMethodWindow(getApplicationContext(), edtSearch);
					RequestUserList((String) node.getData());
					mVF.setDisplayedChild(LOAD_DATA);
				}

			});

		} catch (Exception e) {
			e.printStackTrace();
		}
		mListViewTree.setAdapter(mTreeAdapter);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mVF.getDisplayedChild() == SIGNLE_LIST || mVF.getDisplayedChild() == NO_DATA) {
				mVF.setDisplayedChild(TREE_LIST);
				mLocation.setVisibility(View.GONE);
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		org = null;
		mTreeDatas = null;
	}

	int userNum;

	@Override
	public void onCheckBoxClick(int position) {
		userNum = position;
		for (int i = 0; i < user.size(); i++) {
			if (i == position) {
				if (user.get(position).checked == false) {
					user.get(position).checked = true;
					mLocation.setVisibility(View.VISIBLE);
					continue;
				}
			} else {
				user.get(i).checked = false;
			}
		}
		mListAdapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_location:
			OrgUserType orgUserType = user.get(userNum);
			if (isFlag == 1) {
				WorkOrderBean order = (WorkOrderBean) getIntent().getSerializableExtra("bean");
				if (order != null) {
					loadDialog.setTitle("正在提交数据,请稍后...");
					loadDialog.show();
					PublishOrderData publishInfo = new PublishOrderData();
					publishInfo.workOrderId = order.workOrderId;
					publishInfo.issuedRegId = orgUserType.locateRegId;
					publishInfo.workIsIssued = "1";
					publishInfo.files = new ArrayList<UpLoadResule>();
					DcNetWorkUtils.publishOrderUrl(this, handler, publishInfo, account, password);
				} else {
					MsgToast.geToast().setMsg("工单数据异常,请重新处理");
				}
			} else {
				loadDialog.setTitle("正在获取人员定位数据,请稍后...");
				loadDialog.show();
				DcNetWorkUtils.getUserLocation(this, handler, account, password, orgUserType.locateRegId);
			}
			break;
		case R.id.btn_path:
			if (isFlag == 1) {
				onBackPressed();
			} else {
				LocationDateDialog dateDialog = new LocationDateDialog(this);
				dateDialog.setLocationDateClick(this);
				dateDialog.show();
			}
			break;
		}

	}

	@Override
	public void locationDate(String beginTime, String endTime) {
		loadDialog.setTitle("正在获取历史轨迹数据,请稍后...");
		loadDialog.show();
		OrgUserType orgUserType = user.get(userNum);
		DcNetWorkUtils.getUserLocationList(this, handler, account, password, orgUserType.locateRegId, beginTime, endTime);
	}
}
