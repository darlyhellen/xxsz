package com.xiangxun.xacity.ui.patrol;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.xiangxun.xacity.R;
import com.xiangxun.xacity.adapter.PersonnelLocationAdapter;
import com.xiangxun.xacity.app.BaseActivity;
import com.xiangxun.xacity.bean.ResponseResultBeans.GetNoticeList;
import com.xiangxun.xacity.bean.ResponseResultBeans.WorkOrderBean;
import com.xiangxun.xacity.bean.ResultBeans.ContactGroupType;
import com.xiangxun.xacity.bean.ResultBeans.TelBookType;
import com.xiangxun.xacity.common.ConstantStatus;
import com.xiangxun.xacity.request.DcNetWorkUtils;
import com.xiangxun.xacity.view.MsgToast;
import com.xiangxun.xacity.view.TitleView;
import com.xiangxun.xacity.view.itemexpandable.XActionSlideExpandableListView;
import com.xiangxun.xacity.view.itemexpandable.ActionSlideExpandableListView.OnActionClickListener;
import com.xiangxun.xacity.view.treelist.ListViewAdapter;
import com.xiangxun.xacity.view.treelist.TreeFileBean;
import com.xiangxun.xacity.view.treelist.TreeListViewAdapter;
import com.xiangxun.xacity.view.treelist.TreeNode;
import com.xiangxun.xacity.view.treelist.TreeListViewAdapter.OnTreeNodeClickListener;


/**
 * @package: com.xiangxun.xacity.ui.patrol
 * @ClassName: ContactActivity.java
 * @Description: 通讯录页面
 * @author: ZhangJP
 * @date: 2016-4-5 上午11:10:00
 */
public class ContactActivity extends BaseActivity{
	private static int 	listState 	= -1;

	private static int 	TREE_LIST 	= 0;
	private static int 	LOAD_DATA 	= 1;
	private static int 	NO_DATA 	= 2;
	private static int 	SIGNLE_LIST = 3;
	
	private TitleView 	titleView;
	private ListView 	mListViewTree;
	private ListView 	mListViewList;
	private ViewFlipper mVF;
	
	private ImageView 	btnSearch;
	private EditText 	edtSearch;
	
	//通讯录
	List<ContactGroupType>  	contact;
	List<TelBookType>  			telbook;
	private List<TreeFileBean>	mTreeDatas = new ArrayList<TreeFileBean>();

	private TreeListViewAdapter mTreeAdapter;
	private ListViewAdapter 	mListAdapter;
	
	
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case ConstantStatus.ContactGroupSuccess:
				contact = (List<ContactGroupType>) msg.obj;
				if (contact != null && contact.size() > 0) {
					//转换通讯录数据到list
					convertDataToTree(contact, 1);
					initListTreeAdapter();
					mVF.setDisplayedChild(TREE_LIST);
					//不再抓取数据
					listState = ConstantStatus.listStateRefresh;
				} else {
					if (contact.size() <= 0) {
						mVF.setDisplayedChild(NO_DATA);
					}
				}
				break;
			case ConstantStatus.ContactGroupBookSuccess:
				telbook = (List<TelBookType>) msg.obj;
				if (telbook != null && telbook.size() > 0) {
					//转换通讯录数据到list
					initListAdapter(telbook);
					mVF.setDisplayedChild(SIGNLE_LIST);
				} else {
					if (telbook.size() <= 0) {
						mVF.setDisplayedChild(NO_DATA);
					}
				}
				break;
			case ConstantStatus.ContactSearchSuccess:
				telbook = (List<TelBookType>) msg.obj;
				if (telbook != null && telbook.size() > 0) {
					//转换通讯录数据到list
					initListAdapter(telbook);
					mVF.setDisplayedChild(SIGNLE_LIST);
				} else {
					if (telbook.size() <= 0) {
						MsgToast.geToast().setMsg(R.string.search_nofind);
					}
				}
				break;				
			case ConstantStatus.ContactSearchFailed:
				MsgToast.geToast().setMsg(R.string.search_nofind);
				break;			
			case ConstantStatus.ContactFailed:
				mVF.setDisplayedChild(NO_DATA);
				break;
			case ConstantStatus.NetWorkError:
				MsgToast.geToast().setMsg(R.string.networkerror);
				break;
			}
		}
	};

	/*
	 * 转换通讯录数据到Tree
	 */
	private void convertDataToTree(List<ContactGroupType> data, int parent) {
		int num = mTreeDatas.size() + 2;
		int fa = parent;
		int faChild;
		
		ContactGroupType item;
		
		if (data != null && data.size() > 0) {
			for (int i = 0; i < data.size(); i ++) {
				item = data.get(i);
				
				mTreeDatas.add(new TreeFileBean(num, fa, item.groupName, item.groupId));
				faChild = num ++;

				//child
				if (item.child != null && item.child.size() > 0)
					convertDataToTree(item.child, faChild);

				num = mTreeDatas.size() + 2;
			}
		}
	}
	
	public void initListAdapter(List<TelBookType> data) {
		try {
			mListAdapter = new ListViewAdapter(mListViewList, this, data) {
			};
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		mListViewList.setAdapter(mListAdapter);
	}
	
	private void initListTreeAdapter () {

		try	{
			mTreeAdapter = new TreeListViewAdapter<TreeFileBean>(mListViewTree, this, mTreeDatas, 10);
			mTreeAdapter.setOnTreeNodeClickListener(new OnTreeNodeClickListener()
			{
				@Override
				public void onClick(TreeNode node, int position)
				{
					hideIMEWindows();

					//switch to telbook; send groupID
					RequestBookList((String)node.getData());
					mVF.setDisplayedChild(LOAD_DATA);
				}

			});
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		mListViewTree.setAdapter(mTreeAdapter);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.patrol_contact_layout);
		initView();
		initData();
		initListener();		
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		titleView = (TitleView) findViewById(R.id.comment_title);
		mListViewTree = (ListView) findViewById(R.id.elv_mine_list);
		mListViewList = (ListView) findViewById(R.id.xlistview);
		mVF = (ViewFlipper) findViewById(R.id.vf_comment);
		
		edtSearch = (EditText)findViewById(R.id.toolbar_search_text);
		btnSearch = (ImageView)findViewById(R.id.toolbar_search_btn);
		btnSearch.setClickable(true);
		btnSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				hideIMEWindows();
				
				String key = edtSearch.getText().toString();
				if ( key == null || key.equals(""))	{
					Toast.makeText(getApplicationContext(), R.string.search_key_null, Toast.LENGTH_SHORT).show();
				}else{
					if (mVF.getDisplayedChild() == TREE_LIST) {
						RequestPersonList(key);
					}
					else {
						List<TelBookType> person = searchKey(key);
						if (person.size() > 0) {
							initListAdapter(person);
							mVF.setDisplayedChild(SIGNLE_LIST);
						} else{
							Toast.makeText(getApplicationContext(), R.string.search_nofind, Toast.LENGTH_SHORT).show();	
						}
					}
				}
			}
		});
	}

	/**
	 * 隐藏输入法窗口
	 */
	private void hideIMEWindows() {
		InputMethodManager imm =(InputMethodManager)getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(edtSearch.getWindowToken(), 0);
	}
	
	private List<TelBookType> searchKey(String key) {
		List<TelBookType> person = new ArrayList<TelBookType>();
		int length = telbook.size();

		if (telbook != null) {
			for (int j = 0; j < length; j ++){
				TelBookType item = telbook.get(j);
				if (item.memberName.contains(key)){
					person.add(item);
				}
			}
		}
		
		return person;
	}
	
	
	@Override
	public void initData() {
		// TODO Auto-generated method stub
		titleView.setTitle("通讯录");
		listState = ConstantStatus.listStateFirst;
		RequestTreeList();
	}

	/**
	 * 获取群组信息
	 */
	private void RequestTreeList() {
		switch (listState) {
		case ConstantStatus.listStateFirst:
			if (contact != null) {
				contact.clear();
				contact = null;
			}
			if (mTreeDatas != null) {
				mTreeDatas.clear();
			}
			
			mVF.setDisplayedChild(1);
			DcNetWorkUtils.getPatrolContactGroup(this, handler, account, password);
			break;
		case ConstantStatus.listStateRefresh:
			mVF.setDisplayedChild(0);
			break;
		}
	}

	/**
	 * 获取群组通讯录
	 * @param groupID
	 */
	private void RequestBookList(String groupID) {
		if (telbook != null) {
			telbook.clear();
			telbook = null;
		}
		DcNetWorkUtils.getPatrolContactGroupBook(this, handler, account, password, groupID);
	}

	/**
	 * 查找通讯录
	 * @param groupID
	 */
	private void RequestPersonList(String groupID, String key) {
		if (telbook != null) {
			telbook.clear();
			telbook = null;
		}
		DcNetWorkUtils.getPatrolContactSearchGroupPerson(this, handler, account, password, groupID, key);
	}

	/**
	 * 查找群组通讯录
	 * @param groupID
	 */
	private void RequestPersonList(String key) {
		if (telbook != null) {
			telbook.clear();
			telbook = null;
		}
		DcNetWorkUtils.getPatrolContactSearchPerson(this, handler, account, password, key);
	}	
	
	@Override
	public void initListener() {
		// TODO Auto-generated method stub
		titleView.setLeftBackOneListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mVF.getDisplayedChild() == SIGNLE_LIST || mVF.getDisplayedChild() == NO_DATA)
					mVF.setDisplayedChild(TREE_LIST);
				else
					onBackPressed();
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mVF.getDisplayedChild() == SIGNLE_LIST || mVF.getDisplayedChild() == NO_DATA) {
				mVF.setDisplayedChild(TREE_LIST);
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		contact = null;
		mTreeDatas = null;
	}

	
}
