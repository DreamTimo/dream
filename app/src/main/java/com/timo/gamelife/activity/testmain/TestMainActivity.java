package com.timo.gamelife.activity.testmain;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.timo.gamelife.R;
import com.timo.gamelife.mvp.MVPBaseActivity;
import com.timo.timolib.BaseTools;
import com.timo.timolib.view.WaveView;
import com.timo.timolib.view.tablayout.CommonTabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * MVPPlugin
 * 邮箱 784787081@qq.com
 */

public class TestMainActivity extends MVPBaseActivity<TestMainContract.View, TestMainPresenter> implements TestMainContract.View {

    @BindView(R.id.tab_layout)
    CommonTabLayout mTabLayout;
    @BindView(R.id.wave_view)
    WaveView mWaveView;
    @BindView(R.id.img_logo)
    ImageView mImgLogo;

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
        initWave();
    }

    @Override
    public void showFragment(int position) {
        BaseTools.showToast(position + "");
    }

    @Override
    public void initWave() {
        final FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(-2, -2);
        lp.gravity = Gravity.CENTER;
        mWaveView.setOnWaveAnimationListener(new WaveView.OnWaveAnimationListener() {
            @Override
            public void OnWaveAnimation(float y) {
                lp.setMargins(0, 0, 0, (int) y + 2);
                mImgLogo.setLayoutParams(lp);
            }
        });
    }

}
