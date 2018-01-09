package com.timo.gamelife.user;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;

import com.timo.gamelife.R;
import com.timo.gamelife.adapter.DetailFragmentAdapter;
import com.timo.gamelife.bean.Book;
import com.timo.gamelife.bean.CityInfo;
import com.timo.gamelife.mvp.MVPBaseActivity;

import butterknife.BindView;

/**
 * MVPPlugin
 * 邮箱 784787081@qq.com
 */

public class UserActivity extends MVPBaseActivity<UserContract.View, UserPresenter> implements UserContract.View {
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    private FragmentTransaction fragmentTransaction;
    private Fragment fragment;

    @Override
    protected int getContentResId() {
        return R.layout.activity_normal;
    }

    @Override
    protected String setTitleName() {
        return "消息界面";
    }

    @Override
    protected void initEvent() {
//        fragment = new MessageFragment();
//        fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.add(R.id.rl_content, fragment).commit();
        mViewPager.setAdapter(new DetailFragmentAdapter(getSupportFragmentManager()));
        mViewPager.setCurrentItem(1);
    }

    @Override
    public void onSuccess(Book mBook) {

    }

    @Override
    public void onError(String result) {

    }

    @Override
    public void getData(CityInfo info) {

    }
}
