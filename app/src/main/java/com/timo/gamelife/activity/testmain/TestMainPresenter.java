package com.timo.gamelife.activity.testmain;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.timo.gamelife.BaseConstances;
import com.timo.gamelife.R;
import com.timo.gamelife.fragment.DreamFragment;
import com.timo.gamelife.fragment.HomeFragment;
import com.timo.gamelife.fragment.MineFragment;
import com.timo.gamelife.mvp.BasePresenterImpl;
import com.timo.timolib.BaseTools;
import com.timo.timolib.Timo_BaseConstancts;
import com.timo.timolib.view.tablayout.CommonTabLayout;
import com.timo.timolib.view.tablayout.listener.OnTabSelectListener;

/**
 * MVPPlugin
 * 邮箱 784787081@qq.com
 */

public class TestMainPresenter extends BasePresenterImpl<TestMainContract.View> implements TestMainContract.Presenter {
    private String[] mTitles = {"首页", "梦想", "我的"};
    private int[] mIconSelectIds = {R.mipmap.icon_home_checked, R.mipmap.icon_dream_checked, R.mipmap.icon_mine_checked};
    private int[] mIconUnselectIds = {R.mipmap.icon_home_no_check, R.mipmap.icon_dream_no_check, R.mipmap.icon_mine_no_check};


    @Override
    public void initTop(CommonTabLayout mTabLayout) {
        BaseTools.setNavigation(mTabLayout, mTitles, mIconUnselectIds, mIconSelectIds, new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mView.showFragment(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
    }

    private HomeFragment mHomeFragment;
    private DreamFragment mDreamFragment;
    private MineFragment mMineFragment;

    @Override
    public void initFragment(Bundle savedInstanceState, FragmentManager fragmentManager, CommonTabLayout mTabLayout, int replaceId) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        int currentTabPosition = 0;
        if (savedInstanceState != null) {
            mHomeFragment = (HomeFragment) fragmentManager.findFragmentByTag(BaseConstances.homeFragment);
            mDreamFragment = (DreamFragment) fragmentManager.findFragmentByTag(BaseConstances.dreamFragment);
            mMineFragment = (MineFragment) fragmentManager.findFragmentByTag(BaseConstances.mineFragment);
            currentTabPosition = savedInstanceState.getInt(Timo_BaseConstancts.currentPosition);
        } else {
            mHomeFragment = new HomeFragment();
            mDreamFragment = new DreamFragment();
            mMineFragment = new MineFragment();

            transaction.add(replaceId, mHomeFragment, BaseConstances.homeFragment);
            transaction.add(replaceId, mDreamFragment, BaseConstances.dreamFragment);
            transaction.add(replaceId, mMineFragment, BaseConstances.mineFragment);
        }
        transaction.commit();
        mView.showFragment(currentTabPosition);
        mTabLayout.setCurrentTab(currentTabPosition);
    }

    @Override
    public void showFragment(int position, FragmentManager fragmentManager) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        switch (position) {
            //首页
            case 0:
                transaction.hide(mDreamFragment);
                transaction.hide(mMineFragment);
                transaction.show(mHomeFragment);
                transaction.commitAllowingStateLoss();
                break;
            //美女
            case 1:
                transaction.hide(mMineFragment);
                transaction.hide(mHomeFragment);
                transaction.show(mDreamFragment);
                transaction.commitAllowingStateLoss();
                break;
            //视频
            case 2:
                transaction.hide(mHomeFragment);
                transaction.hide(mDreamFragment);
                transaction.show(mMineFragment);
                transaction.commitAllowingStateLoss();
                break;
            default:
                break;
        }
    }
}
