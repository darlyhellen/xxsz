package com.xiangxun.xacity.view;

import com.xiangxun.xacity.app.BaseFragment;
import com.xiangxun.xacity.ui.GroupListFragment;
import com.xiangxun.xacity.ui.MapViewFragment;

public class FragmentFactory {
	public static final int VIDEO_INFO = 0;
	public static final int MAP_VIEW = 1;
	
	public static BaseFragment createVideoFragment(int subId) {
		BaseFragment fragment = null;
		switch (subId) {
		case VIDEO_INFO:
			fragment = new GroupListFragment();
			break;
		case MAP_VIEW:
			fragment = new MapViewFragment();
			break;
		}
		return fragment;
	}
}
