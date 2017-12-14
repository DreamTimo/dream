package com.timo.gamelife.fragment.plan;

import android.content.Context;

import com.timo.gamelife.mvp.BasePresenter;
import com.timo.gamelife.mvp.BaseView;

import java.util.List;

/**
 * MVPPlugin
 * 邮箱 784787081@qq.com
 */

public class PlanContract {
    interface View extends BaseView {
        void initData(List<String> data);
    }

    interface Presenter extends BasePresenter<View> {
        void initData();
    }
}
