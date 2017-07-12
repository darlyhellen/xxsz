package com.xiangxun.xacity.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;

import com.xiangxun.xacity.R;
import com.xiangxun.xacity.app.BaseFragment;
import com.xiangxun.xacity.app.BaseFragmentActivity;
import com.xiangxun.xacity.utils.Utils;
import com.xiangxun.xacity.view.FragmentFactory;
import com.xiangxun.xacity.view.IndexViewPager;
import com.xiangxun.xacity.view.PagerSlidingTabStrip;
import com.xiangxun.xacity.view.TitleView;

/**
 * @package: com.xiangxun.xacity.ui
 * @ClassName: VideoManageActivity.java
 * @Description: 视频管理
 * @author: HanGJ
 * @date: 2015-12-30 下午5:19:19
 */
public class VideoManageActivity extends BaseFragmentActivity {
	private TitleView titleView;
	private IndexViewPager mPageVp;
	private PagerSlidingTabStrip mPageTabs;
	private FragmentAdapter mFragmentAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_manage_home_layout);
		initView();
		initData();
		initListener();
	}

	@Override
	public void initView() {
		titleView = (TitleView) findViewById(R.id.comment_title);
		mPageVp = (IndexViewPager) this.findViewById(R.id.id_page_vp);
		mPageTabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
	}

	@Override
	public void initData() {
		titleView.setTitle("视频管理");
		mFragmentAdapter = new FragmentAdapter(this.getSupportFragmentManager());
		mPageVp.setAdapter(mFragmentAdapter);
		mPageTabs.setViewPager(mPageVp);
	}

	@Override
	public void initListener() {
		titleView.setLeftBackOneListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (GroupListFragment.mGroupListManager.getRootNode() != null) {
					GroupListFragment.mGroupListManager.setRootNode(null);
				}
				if (Utils.click_play) {
					Utils.click_play = false;
				}
				setResult(RESULT_OK);
				finish();
			}
		});
	}

	public void setCurrentPosition(int currentPosition) {
		mPageVp.setCurrentItem(currentPosition);
	}

	public class FragmentAdapter extends FragmentPagerAdapter implements OnPageChangeListener {

		String[] mTabTitle;
		BaseFragment[] mFragments;
		int mCurrentIndex;

		public FragmentAdapter(FragmentManager fm) {
			super(fm);
			mTabTitle = getResources().getStringArray(R.array.video_tab_names);
			mFragments = new BaseFragment[mTabTitle.length];
		}

		@Override
		public Fragment getItem(int position) {
			if (mFragments[position] == null || !(mFragments[position] instanceof BaseFragment)) {
				mFragments[position] = FragmentFactory.createVideoFragment(position);
			}
			return mFragments[position];
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return mTabTitle[position];
		}

		@Override
		public int getCount() {
			return mTabTitle.length;
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// ViewPager滑动状态改变的回调
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// ViewPager滑动时的回调
		}

		@Override
		public void onPageSelected(int index) {
			// ViewPager页面被选中的回调
			if (index < mFragments.length) {
				mCurrentIndex = index;
				mFragments[index].load();
				mPageVp.setCurrentItem(index);
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (GroupListFragment.mGroupListManager.getRootNode() != null) {
				GroupListFragment.mGroupListManager.setRootNode(null);
			}
			if (Utils.click_play) {
				Utils.click_play = false;
			}
			setResult(RESULT_OK);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

}
