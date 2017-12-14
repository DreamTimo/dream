package com.timo.gamelife.detail;

import android.support.v4.app.FragmentManager;

import com.timo.gamelife.adapter.DetailFragmentAdapter;
import com.timo.gamelife.mvp.BasePresenterImpl;

/**
 * MVPPlugin
 * 邮箱 784787081@qq.com
 */

public class DetailPresenter extends BasePresenterImpl<DetailContract.View> implements DetailContract.Presenter {

    @Override
    public void setIndicator(FragmentManager fm) {
        mView.setIndicator(new DetailFragmentAdapter(fm));
    }
}
