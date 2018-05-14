package com.timo.gamelife.activity.kotlinmain;

import android.support.v4.app.FragmentManager;

import com.timo.gamelife.mvp.BasePresenter;
import com.timo.gamelife.mvp.BaseView;
import com.timo.timolib.view.tablayout.CommonTabLayout;

/**
 * MVPPlugin
 * 邮箱 784787081@qq.com
 */

public class KotlinMainContract {
    public interface View extends BaseView {
        void showFragment(int position);

        void initWave();
    }

    public interface Presenter extends BasePresenter<View> {
        void initTop(CommonTabLayout mTabLayout);

        void initFragment(FragmentManager fragmentManager, CommonTabLayout mTabLayout, int replaceId);

        void showFragment(int position, FragmentManager fragmentManager);
    }
}
