package com.timo.gamelife.activity.kotlinmain


import android.os.Bundle
import android.os.Handler
import android.os.Message
import com.timo.gamelife.R

import com.timo.gamelife.mvp.MVPBaseActivity
import com.timo.timolib.BaseTools
import com.timo.timolib.Timo_BaseConstancts
import com.timo.timolib.tools.rx.RxManager
import kotlinx.android.synthetic.main.activity_test_main.*
import rx.functions.Action1


/**
 * MVPPlugin
 * 邮箱 784787081@qq.com
 */

class KotlinMainActivity : MVPBaseActivity<KotlinMainContract.View, KotlinMainPresenter>(), KotlinMainContract.View {

    override fun getContentResId(): Int = R.layout.activity_test_main

    override fun setTitleName(): String? = getString(R.string.title_1)
    override fun initEvent(savedInstanceState: Bundle?) {
        mPresenter.initTop(tab_layout)
        mPresenter.initFragment(savedInstanceState, supportFragmentManager, tab_layout, R.id.fragment)
        initWave()
        RxManager.getInstance().on(Timo_BaseConstancts.TAG, Action1<Boolean> { hideOrShow ->
            BaseTools.showToast("ceshi" + hideOrShow)
            BaseTools.getHandler().postDelayed({ BaseTools.showToast("55555555") }, 5000)
        })
    }

    override fun showFragment(position: Int) = mPresenter.showFragment(position, supportFragmentManager)

    override fun initWave() = BaseTools.setWave(wave_view, img_logo)

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //奔溃前保存位置
        if (tab_layout != null) {
            outState.putInt(Timo_BaseConstancts.currentPosition, tab_layout.currentTab)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        RxManager.getInstance().clear()
    }
}
