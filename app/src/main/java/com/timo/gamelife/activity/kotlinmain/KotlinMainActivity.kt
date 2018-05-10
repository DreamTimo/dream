package com.timo.gamelife.activity.kotlinmain


import android.os.Bundle
import com.timo.gamelife.R

import com.timo.gamelife.mvp.MVPBaseActivity
import com.timo.timolib.BaseTools
import com.timo.timolib.Timo_BaseConstancts
import kotlinx.android.synthetic.main.activity_test_main.*


/**
 * MVPPlugin
 * 邮箱 784787081@qq.com
 */

class KotlinMainActivity : MVPBaseActivity<KotlinMainContract.View, KotlinMainPresenter>(), KotlinMainContract.View {

    override fun getContentResId(): Int {
        return R.layout.activity_test_main
    }

    override fun setTitleName(): String? {
        return null
    }

    override fun initEvent(savedInstanceState: Bundle?) {
        mPresenter.initTop(tab_layout)
        mPresenter.initFragment(savedInstanceState, supportFragmentManager, tab_layout, R.id.fragment)
        initWave()
    }

    override fun showFragment(position: Int) {
        mPresenter.showFragment(position, supportFragmentManager)
    }

    override fun initWave() {
        BaseTools.setWave(wave_view, img_logo)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //奔溃前保存位置
        if (tab_layout != null) {
            outState.putInt(Timo_BaseConstancts.currentPosition, tab_layout.getCurrentTab())
        }
    }
}
