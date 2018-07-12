package com.timo.gamelife.activity.kotlinmain


import android.os.Bundle
import com.timo.gamelife.R

import com.timo.gamelife.mvp.MVPBaseActivity
import com.timo.timolib.BaseTools
import com.timo.timolib.BaseConstancts
import com.timo.timolib.tools.rx.RxManager
import kotlinx.android.synthetic.main.activity_test_main.*

/**
 * MVPPlugin
 * 邮箱 784787081@qq.com
 */

class KotlinMainActivity : MVPBaseActivity<KotlinMainContract.View, KotlinMainPresenter>(), KotlinMainContract.View {

    override fun getContentResId(): Int = R.layout.activity_test_main

    override fun initEvent() {
        mPresenter.initTop(tab_layout)
        mPresenter.initFragment(supportFragmentManager, tab_layout, R.id.fragment)
    }

    override fun showFragment(position: Int) {
        mPresenter.showFragment(position, supportFragmentManager)
        when (position) {
            0 -> BaseTools.loadWeb(test_webview, "file:///android_asset/snow.html")
            1 -> BaseTools.loadWeb(test_webview, "file:///android_asset/sakura.html")
            2 -> BaseTools.loadWeb(test_webview, "file:///android_asset/snow.html")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //奔溃前保存位置
        if (tab_layout != null) {
            outState.putInt(BaseConstancts.currentPosition, tab_layout.currentTab)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        RxManager.getInstance().clear()
    }
}

