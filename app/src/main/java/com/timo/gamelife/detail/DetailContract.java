package com.timo.gamelife.detail;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;

import com.timo.gamelife.mvp.BasePresenter;
import com.timo.gamelife.mvp.BaseView;

/**
 * MVPPlugin
 *  邮箱 784787081@qq.com
 */

public class DetailContract {
    interface View extends BaseView {
        void setIndicator(PagerAdapter pagerAdapter);
    }

    interface  Presenter extends BasePresenter<View> {
        void setIndicator(FragmentManager fm);
    }
}
