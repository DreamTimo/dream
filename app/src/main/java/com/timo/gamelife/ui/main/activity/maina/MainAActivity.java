package com.timo.gamelife.ui.main.activity.maina;


import android.os.Bundle;
import android.support.annotation.IdRes;

import com.timo.gamelife.R;
import com.timo.gamelife.mvp.MVPBaseActivity;
import com.timo.timolib.BaseTools;
import com.timo.timolib.view.navigation.BottomBar;
import com.timo.timolib.view.navigation.OnTabSelectListener;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * MVPPlugin
 * 邮箱 784787081@qq.com
 */

public class MainAActivity extends MVPBaseActivity<MainAContract.View, MainAPresenter> implements MainAContract.View {

    @BindView(R.id.bottomBar)
    BottomBar mBottomBar;

    @Override
    protected int getContentResId() {
        return R.layout.activity_main_a;
    }

    @Override
    protected String setTitleName() {
        return null;
    }

    @Override
    protected void initEvent() {
        mBottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {

            }
        });
    }

}
