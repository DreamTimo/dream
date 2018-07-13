package com.timo.gamelife.fragment

import android.view.View

import com.timo.gamelife.R
import com.timo.timolib.BaseTools
import com.timo.timolib.base.base_fragment.BaseFragment
import com.timo.timolib.base.base_fragment.SuperFragment
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * Created by lykj on 2017/9/12.
 */

class DreamFragment : BaseFragment() {

    override fun getContentResId(): Int = R.layout.fragment_home

    override fun initEvent(view: View) = BaseTools.setTitleBar(title_dream, "梦想")

}
