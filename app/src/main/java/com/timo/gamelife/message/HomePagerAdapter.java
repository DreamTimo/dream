package com.timo.gamelife.message;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class HomePagerAdapter extends PagerAdapter implements ViewPager.OnPageChangeListener{

    private final Context mContext;
    private final ViewPager mViewPager;
    private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();
    private FragmentManager fragmentManager;
    static final class TabInfo {
        private final Fragment fragment;
        private final Bundle args;
		public Fragment getFragment() {
			return fragment;
		}
		public Bundle getArgs() {
			return args;
		}
		public TabInfo(Fragment fragment, Bundle args) {
			super();
			this.fragment = fragment;
			this.args = args;
		}
       
    }

    public void addTab(String title, Fragment fragment){
        Bundle bundle = new Bundle();
        bundle.putString("title",title);
        TabInfo info = new TabInfo(fragment,bundle);
        mTabs.add(info);
        notifyDataSetChanged();
    }

    public HomePagerAdapter(FragmentActivity activity, ViewPager pager, FragmentManager fragmentManager) {
        mContext = activity;
        mViewPager = pager;
        this.fragmentManager=fragmentManager;
        mViewPager.setAdapter(this);
        mViewPager.setOnPageChangeListener(this);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabs.get(position).args.getString("title");
    }

    @Override
	public int getCount() {
		return mTabs.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object o) {
		return view == o;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(mTabs.get(position).getFragment().getView()); // 移出viewpager两边之外的page布局
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		Fragment fragment = mTabs.get(position).getFragment();
		if (!fragment.isAdded()) { // 如果fragment还没有added
			FragmentTransaction ft = fragmentManager.beginTransaction();
			ft.add(fragment, fragment.getClass().getSimpleName());
			ft.commit();
			fragmentManager.executePendingTransactions();
		}
		if (fragment.getView().getParent() == null) {
			container.addView(fragment.getView()); // 为viewpager增加布局
		}
		return fragment.getView();
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		
	}

}
