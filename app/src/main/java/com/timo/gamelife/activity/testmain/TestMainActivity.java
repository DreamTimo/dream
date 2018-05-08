package com.timo.gamelife.activity.testmain;

import android.widget.FrameLayout;

import com.timo.gamelife.R;
import com.timo.gamelife.mvp.MVPBaseActivity;
import com.timo.timolib.BaseTools;
import com.timo.timolib.view.tablayout.CommonTabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * MVPPlugin
 * 邮箱 784787081@qq.com
 */

public class TestMainActivity extends MVPBaseActivity<TestMainContract.View, TestMainPresenter> implements TestMainContract.View {

    @BindView(R.id.fl_body)
    FrameLayout mFlBody;
    @BindView(R.id.tab_layout)
    CommonTabLayout mTabLayout;

    @Override
    protected int getContentResId() {
        return R.layout.activity_test_main;
    }

    @Override
    protected String setTitleName() {
        return null;
    }

    @Override
    protected void initEvent() {
        mPresenter.initTop(mTabLayout);
    }

    @Override
    public void showFragment(int position) {
        BaseTools.showToast(position + "");
    }
}
