package com.timo.gamelife.activity.testmain;

import android.os.Bundle;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.timo.gamelife.R;
import com.timo.gamelife.mvp.MVPBaseActivity;
import com.timo.timolib.BaseTools;
import com.timo.timolib.Timo_BaseConstancts;
import com.timo.timolib.UserBean;
import com.timo.timolib.tools.logger.Logger;
import com.timo.timolib.view.WaveView;
import com.timo.timolib.view.tablayout.CommonTabLayout;

import butterknife.BindView;

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
    protected void initEvent(Bundle savedInstanceState) {
        mPresenter.initTop(mTabLayout);
        mPresenter.initFragment(savedInstanceState, getSupportFragmentManager(), mTabLayout, R.id.fragment);
        initWave();
    }

    @Override
    public void showFragment(int position) {
        mPresenter.showFragment(position, getSupportFragmentManager());
    }

    @Override
    public void initWave() {
        BaseTools.setWave(mWaveView, mImgLogo);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //奔溃前保存位置
        if (mTabLayout != null) {
            outState.putInt(Timo_BaseConstancts.currentPosition, mTabLayout.getCurrentTab());
        }
    }

}
