package com.timo.gamelife.activity.kotlinmain;

import com.timo.gamelife.mvp.BasePresenter;
import com.timo.gamelife.mvp.BaseView;

/**
 * MVPPlugin
 * 邮箱 784787081@qq.com
 */

public class KotlinMainContract {
    public interface View extends BaseView {
        void showFragment(int position);
    }

    public interface Presenter extends BasePresenter<View> {
        String[] getTitles();

        int[] getSelect();

        int[] getSelected();
    }
}
