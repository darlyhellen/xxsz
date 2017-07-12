package com.xiangxun.xacity.ui;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dh.DpsdkCore.IDpsdkCore;
import com.xiangxun.xacity.DpsdkCoreActivity;
import com.xiangxun.xacity.R;
import com.xiangxun.xacity.adapter.GroupListAdapter;
import com.xiangxun.xacity.adapter.GroupListAdapter.IOnCheckBoxClick;
import com.xiangxun.xacity.adapter.GroupListAdapter.IOnItemClickListener;
import com.xiangxun.xacity.app.BaseFragment;
import com.xiangxun.xacity.app.XiangXunApplication;
import com.xiangxun.xacity.bean.TreeNode;
import com.xiangxun.xacity.groupTree.GroupListGetTask.IOnSuccessListener;
import com.xiangxun.xacity.groupTree.GroupListManager;
import com.xiangxun.xacity.view.MsgToast;
import com.xiangxun.xacity.view.xlistView.XListView;
import com.xiangxun.xacity.view.xlistView.XListView.IXListViewListener;

public class GroupListFragment extends BaseFragment implements OnClickListener, IOnItemClickListener, IOnCheckBoxClick, IXListViewListener {
	// 组织树控件
	private XListView mXListView;
	// 搜索框adapter
	private GroupListAdapter mGroupListAdapter = null;
	// 获取实例
	public static GroupListManager mGroupListManager = null;
	// 选中的nodes
	private List<TreeNode> selectNnodes = null;
	// 获取的树信息
	private TreeNode root = null;
	// 消息对象
	private Handler mHandler = null;
	// 等待对话框
	private ProgressBar mWattingPb = null;
	// 显示选中个数
	private TextView mSeletedChannelsNumTv = null;
	/** 更新列表消息(const value:1000) */
	public static final int MSG_GROUPLIST_UPDATELIST = 1000;
	/** 点击进入回放消息 */
	public static final int MSG_GROUP_TO_PLAYBACK = 1005;
	private String[] dialogList;
	private LinearLayout layLogout;
	private Context mContext;
	private VideoManageActivity mActivity;

	@Override
	public void onAttach(Activity activity) {
		mActivity = (VideoManageActivity) activity;
		mContext = activity;
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_group_info, container, false);
		initView(view); // 查找控件
		initData(); // 设置监听器
		initListener(); // 初始化数据
		return view;
	}

	@Override
	public void initView(View view) {
		layLogout = (LinearLayout) view.findViewById(R.id.title_lay);
		mXListView = (XListView) view.findViewById(R.id.group_list);
		mSeletedChannelsNumTv = (TextView) view.findViewById(R.id.selected_channels_num_tv);
		// 等待对话框布局
		mWattingPb = (ProgressBar) view.findViewById(R.id.grouplist_waitting_pb);
	}

	@Override
	public void initData() {
		mHandler = createHandler();
		mGroupListManager = GroupListManager.getInstance();
		mGroupListAdapter = new GroupListAdapter(mContext);
		mGroupListAdapter.setListner(this, this);
		mWattingPb.setClickable(true);
		updateSelectChannels();
		mXListView.setAdapter(mGroupListAdapter);
		getGroupList();
	}

	@Override
	public void initListener() {
		mXListView.setPullLoadEnable(true);
		mXListView.setXListViewListener(this);
		layLogout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				int nPDLLHandle = DpsdkCoreActivity.getDpsdkHandle();
				int nRet = IDpsdkCore.DPSDK_Logout(nPDLLHandle, 30000);
				if (nRet == 0) {
					MsgToast.geToast().setText(R.string.logout);
				} else {
					MsgToast.geToast().setText("登陆失败");
				}
				Intent intent = new Intent();
				intent.setClass(mContext.getApplicationContext(), DpsdkCoreActivity.class);
				startActivity(intent);
				mActivity.finish();
			}
		});
	}

	/**
	 * <p>
	 * 获取组织列表
	 * </p>
	 */
	private void getGroupList() {
		root = mGroupListManager.getRootNode();
		if (root == null) {
			mWattingPb.setVisibility(View.VISIBLE);
		}
		if (mGroupListManager.getTask() != null) {
			mGroupListManager.setGroupListGetListener(mIOnSuccessListener);
		}
		if (mGroupListManager.isFinish() && root != null) {
			if (root.getChildren().size() == 0) {
				mGroupListManager.startGroupListGetTask();
			}
			Message message = mHandler.obtainMessage();
			message.what = MSG_GROUPLIST_UPDATELIST;
			message.arg1 = 0;
			message.arg2 = 0;
			mHandler.sendMessage(message);
			return;
		} else if (root == null) {
			if (mGroupListManager.getTask() == null) {
				// 获取组织树任务
				mGroupListManager.startGroupListGetTask();
				mGroupListManager.setGroupListGetListener(mIOnSuccessListener);
			}
		}
	}

	/**
	 * 创建消息对象
	 */
	private Handler createHandler() {
		Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case MSG_GROUPLIST_UPDATELIST:
					// 处理更新列表
					handleUpdateList();
					break;
				case MSG_GROUP_TO_PLAYBACK:
					// handleClickPlayback(msg.obj, msg.arg1); //处理点击回放
				default:
					break;
				}
			}

			/**
			 * 处理更新列表
			 */
			private void handleUpdateList() {
				root = mGroupListManager.getRootNode();
				mGroupListManager.setOnSuccessListener(mIOnSuccessListener);
				if (mWattingPb != null) {
					mWattingPb.setVisibility(View.GONE);
				}
				mGroupListAdapter.clearDate();
				mGroupListAdapter.addNode(root);
				// 设置默认展开级别
				mGroupListAdapter.setExpandLevel(1);
				mGroupListAdapter.notifyDataSetChanged();
			}
		};
		return handler;
	}

	IOnSuccessListener mIOnSuccessListener = new IOnSuccessListener() {
		@Override
		public void onSuccess(final boolean success, final int errCode) {
			stopXListView();
			mHandler.post(new Runnable() {

				@Override
				public void run() {
					// 清空任务
					mGroupListManager.setTask(null);
					if (mWattingPb != null) {
						mWattingPb.setVisibility(View.GONE);
					}
					if (success) {
						root = mGroupListManager.getRootNode();
						if (root != null) {
							mGroupListAdapter.clearDate();
							mGroupListAdapter.addNode(root);
							// 设置默认展开级别
							mGroupListAdapter.setExpandLevel(1);
							mGroupListAdapter.notifyDataSetChanged();
						} else {
							mGroupListAdapter.clearDate();
							mGroupListAdapter.notifyDataSetChanged();
						}
						// updateSelectChannels();
					} else {
						MsgToast.geToast().setMsg("获取组织列表失败,异常代码：" + errCode);
					}
				}
			});
		}
	};

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mGroupListManager.getRootNode() != null) {
			mGroupListManager.setRootNode(null);
		}
		mGroupListAdapter.setAllUnExpanded();
		mGroupListManager.setTask(null);
		mActivity.finish();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void updateSelectChannels() {
		selectNnodes = mGroupListAdapter.getSeletedNodes();
		if (selectNnodes != null && selectNnodes.size() > 0) {
			mSeletedChannelsNumTv.setVisibility(View.VISIBLE);
			mSeletedChannelsNumTv.setText("" + selectNnodes.size());
		} else {
			mSeletedChannelsNumTv.setVisibility(View.GONE);
		}
		if (selectNnodes.size() > 32) {
			MsgToast.geToast().setMsg("打开通道上限为32路");
		}
	}

	@Override
	public void onItemClick(TreeNode treeNode, boolean isChecked, final int position) {
		if (treeNode.getType() == 1 && !treeNode.isExpanded()) {
			XiangXunApplication.CHANNEL_TYPE = 1;
			XiangXunApplication.firsTreeNodes = treeNode.getChildren();
		}
		if (treeNode.getType() == 2 && !treeNode.isExpanded()) {
			XiangXunApplication.CHANNEL_TYPE = 2;
			XiangXunApplication.secondTreeNodes = treeNode.getChildren();
		}
		if (treeNode.getType() == 2) {
			// 判断设备类型是否是报警主机
			int devType = treeNode.getDeviceInfo().getdeviceType();
			if (devType == 601) { // 报警主机类型601
				dialogList = new String[] { "实时", "回放", "布控报警", "报警主机" };
			} else {
				dialogList = new String[] { "实时", "回放", "布控报警", "语音对讲" };
			}
		}
		if (treeNode.getType() == 3) { // 通道
			if (dialogList == null) { // 没有设备的业务树
				dialogList = new String[] { "实时", "回放", "布控报警", "语音对讲" };
			}
		} else {
			if (mGroupListAdapter.ExpandOrCollapse(position)) {
				mXListView.setSelection(0);
			}
		}
	}

	@Override
	public void onCheckBoxClick(TreeNode treeNode, boolean isChecked, int position) {
	}

	@Override
	public void onClick(View arg0) {
	}

	@Override
	public void onRefresh(View v) {
		getGroupList();

	}

	@Override
	public void onLoadMore(View v) {
	}

	@Override
	public void load() {

	}

	// xLisView 停止
	private void stopXListView() {
		mXListView.stopRefresh();
		mXListView.stopLoadMore();
	}
}
