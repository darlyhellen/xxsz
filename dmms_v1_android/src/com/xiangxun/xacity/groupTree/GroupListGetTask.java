package com.xiangxun.xacity.groupTree;

import android.os.AsyncTask;
import android.util.Log;

import com.xiangxun.xacity.bean.TreeNode;


/**
 * @package: com.xiangxun.xacity.groupTree
 * @ClassName: GroupListGetTask.java
 * @Description: 获取组织树任务
 * @author: HanGJ
 * @date: 2016-1-30 上午9:58:23
 */
public class GroupListGetTask extends AsyncTask<Void,Void,TreeNode> {
    private GroupListManager mGroupListManager = null;

    // 组织树的头信息
    private byte[] szCoding = null;

    private IOnSuccessListener mOnSuccessListener;

    public interface IOnSuccessListener {
        public void onSuccess(boolean success, int errCode);
    }

    public GroupListGetTask() {
        mGroupListManager = GroupListManager.getInstance();
    }

    public void setListener(IOnSuccessListener onSuccessListener) {
        mOnSuccessListener = onSuccessListener;
    }

    @Override
	protected void onPreExecute() {
		super.onPreExecute();
	}
    
	@Override
	protected TreeNode doInBackground(Void... arg0) {	
		mGroupListManager.setFinish(false);
	    szCoding = mGroupListManager.loadDGroupInfoLayered();
	    mGroupListManager.getGroupList(szCoding, mGroupListManager.getRootNode());
	    mGroupListManager.setFinish(true);
	    if (mOnSuccessListener != null) {
			mOnSuccessListener.onSuccess(true, 0);
		}
	    Log.i("GroupListGetTask", "get data");
	    return mGroupListManager.getRootNode();
	   
	}

	/*@Override
	protected void onPostExecute(TreeNode result) {
		super.onPostExecute(result);
		mGroupListManager.setFinish(false);
	}*/
	
}
