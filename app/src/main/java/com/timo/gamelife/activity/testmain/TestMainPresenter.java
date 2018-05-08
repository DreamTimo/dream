package com.timo.gamelife.activity.testmain;

import com.timo.gamelife.R;
import com.timo.gamelife.mvp.BasePresenterImpl;
import com.timo.timolib.view.tablayout.CommonTabLayout;
import com.timo.timolib.view.tablayout.TabEntity;
import com.timo.timolib.view.tablayout.listener.CustomTabEntity;
import com.timo.timolib.view.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;

/**
 * MVPPlugin
 * 邮箱 784787081@qq.com
 */

public class TestMainPresenter extends BasePresenterImpl<TestMainContract.View> implements TestMainContract.Presenter {
    private String[] mTitles = {"首页", "梦想", "我的"};
    private int[] mIconSelectIds = {
            R.mipmap.icon_home_checked, R.mipmap.icon_dream_checked, R.mipmap.icon_mine_checked};
    private int[] mIconUnselectIds = {
            R.mipmap.icon_home_no_check, R.mipmap.icon_dream_no_check, R.mipmap.icon_mine_no_check};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    @Override
    public void initTop(CommonTabLayout mTabLayout) {
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        mTabLayout.setTabData(mTabEntities);
        //点击监听
        mTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mView.showFragment(position);
            }

            @Override
            public void onTabReselect(int position) {
            }
        });
    }
}
