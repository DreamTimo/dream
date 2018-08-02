package com.timo.gamelife.activity.kotlinmain

import android.speech.tts.TextToSpeech
import com.timo.gamelife.BaseConstances
import com.timo.gamelife.R
import com.timo.gamelife.fragment.DreamFragment
import com.timo.gamelife.fragment.HomeFragment
import com.timo.gamelife.fragment.MineFragment

import com.timo.gamelife.mvp.MVPBaseActivity
import com.timo.timolib.BaseTools
import com.timo.timolib.view.tablayout.listener.OnTabSelectListener
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

/**
 * MVPPlugin
 * 邮箱 784787081@qq.com
 */

class KotlinMainActivity : MVPBaseActivity<KotlinMainContract.View, KotlinMainPresenter>(), KotlinMainContract.View {
    private var mHomeFragment: HomeFragment? = null
    private var mDreamFragment: DreamFragment? = null
    private var mMineFragment: MineFragment? = null
    internal var textToSpeech: TextToSpeech? = null

    override fun getContentResId(): Int = R.layout.activity_main

    override fun initEvent() {
        val transaction = supportFragmentManager.beginTransaction()
        val currentTabPosition = 0
        mHomeFragment = HomeFragment()
        mDreamFragment = DreamFragment()
        mMineFragment = MineFragment()
        transaction.add(R.id.fragment, mHomeFragment, BaseConstances.homeFragment)
        transaction.add(R.id.fragment, mDreamFragment, BaseConstances.dreamFragment)
        transaction.add(R.id.fragment, mMineFragment, BaseConstances.mineFragment)
        transaction.commit()
        showFragment(currentTabPosition)
        BaseTools.setNavigation(tab_layout, mPresenter.titles, mPresenter.select, mPresenter.selected, object : OnTabSelectListener {
            override fun onTabSelect(position: Int) = showFragment(position)

            override fun onTabReselect(position: Int) = Unit
        })
        tab_layout.currentTab = currentTabPosition

    }

    override fun isMain(): Boolean = true
    override fun showFragment(position: Int) {
        textToSpeech = TextToSpeech(this, TextToSpeech.OnInitListener { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = textToSpeech!!.setLanguage(Locale.ENGLISH)
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    BaseTools.showToast("抱歉!不支持语音播报功能...")
                } else {
                    textToSpeech!!.speak("请对工号1101号服务人员做出评价", TextToSpeech.QUEUE_FLUSH, null)
                }
            }
        })
        val transaction = supportFragmentManager.beginTransaction()
        when (position) {
        //首页
            0 -> {
                transaction.hide(mDreamFragment)
                transaction.hide(mMineFragment)
                transaction.show(mHomeFragment)
                transaction.commitAllowingStateLoss()
                BaseTools.loadWeb(test_webview, "file:///android_asset/snow.html")
            }
        //美女
            1 -> {
                transaction.hide(mMineFragment)
                transaction.hide(mHomeFragment)
                transaction.show(mDreamFragment)
                transaction.commitAllowingStateLoss()
                BaseTools.loadWeb(test_webview, "file:///android_asset/sakura.html")
            }
        //视频
            2 -> {
                transaction.hide(mHomeFragment)
                transaction.hide(mDreamFragment)
                transaction.show(mMineFragment)
                transaction.commitAllowingStateLoss()
                BaseTools.loadWeb(test_webview, "file:///android_asset/snow.html")
            }
            else -> {
            }
        }
    }
}

