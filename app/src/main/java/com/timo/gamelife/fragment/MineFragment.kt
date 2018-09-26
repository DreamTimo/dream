package com.timo.gamelife.fragment

import android.text.Html
import android.view.View
import com.timo.gamelife.R
import com.timo.timolib.BaseTools
import com.timo.timolib.view.banner.pagerstyle.ScaleInTransformer

import java.util.ArrayList

import com.timo.cameralib.camera.CameraActivity
import com.timo.timolib.base.base_fragment.BaseFragment
import kotlinx.android.synthetic.main.fragment_mine.*

/**
 * Created by lykj on 2017/9/12.
 */

class MineFragment : BaseFragment() {

    lateinit var data: ArrayList<String>

    override fun getContentResId(): Int = R.layout.fragment_mine

    override fun initEvent(view: View) {
        BaseTools.setTitleBar(title, "我的")
        data = ArrayList()
        data.add("http://aiwolvju.b0.upaiyun.com/uploads/20171211/fbfrebpymwaadvb9uckzmyb1iw81axet.tmp")
        data.add("http://aiwolvju.b0.upaiyun.com/uploads/20171211/fbfrebpymwaadvb9uckzmyb1iw81axet.tmp")
        data.add("http://aiwolvju.b0.upaiyun.com/uploads/20171211/fbfrebpymwaadvb9uckzmyb1iw81axet.tmp")
        data.add("http://aiwolvju.b0.upaiyun.com/uploads/20171211/fbfrebpymwaadvb9uckzmyb1iw81axet.tmp")
        BaseTools.setBanner(banner, data, ScaleInTransformer())
        BaseTools.loadWeb(webview, "file:///android_asset/index.html")
        val html = "<font color='#ff0000'>点击-></font><font color='#0000FF'>项目<font>"
        val charSequence = Html.fromHtml(html)
        textview.text = charSequence
        textview.setOnClickListener {
            toOpenCamera(CameraActivity::class.java)
        }
    }
}
