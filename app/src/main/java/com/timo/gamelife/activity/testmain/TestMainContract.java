package com.timo.gamelife.activity.testmain;

import android.content.Context;

import com.timo.gamelife.mvp.BasePresenter;
import com.timo.gamelife.mvp.BaseView;
import com.timo.timolib.view.tablayout.CommonTabLayout;

/**
 * MVPPlugin
 *  邮箱 784787081@qq.com
 */

public class TestMainContract {
    interface View extends BaseView {
        void showFragment(int position);
        void initWave();

    }

    interface  Presenter extends BasePresenter<View> {
        void initTop(CommonTabLayout mTabLayout);
    }
}
