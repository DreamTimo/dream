package com.timo.gamelife.fragment;

import android.os.Environment;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.timo.gamelife.BannerUtils;
import com.timo.gamelife.R;
import com.timo.timolib.BaseTools;
import com.timo.timolib.base.base_fragment.BaseFragment;
import com.timo.timolib.tools.camera.util.FileUtil;
import com.timo.timolib.tools.daynightmodeutils.ChangeModeController;
import com.timo.timolib.view.CommonWebView;
import com.timo.timolib.view.banner.Banner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.jzvd.JZVideoPlayerStandard;

/**
 * Created by lykj on 2017/9/12.
 */

public class MineFragment extends BaseFragment {

    @BindView(R.id.banner)
    Banner mBanner;
    List<String> data;
    @BindView(R.id.webview)
    CommonWebView mWebview;
    @BindView(R.id.textview)
    TextView mTextview;
    @BindView(R.id.jz_video)
    JZVideoPlayerStandard mJzVideo;

    @Override
    protected String setTitleName() {
        return getString(R.string.main_mine);
    }

    @Override
    protected int getContentResId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initEvent(View view) {
        data = new ArrayList<>();
        data.add("111111");
        data.add("111111");
        data.add("111111");
        data.add("111111");
        BannerUtils.getInstance().setHouseProfiltBanner(getActivity(), mBanner, data);
        BaseTools.loadWeb(mWebview, "file:///android_asset/index.html");
        String html = "<font color='#ff0000'>点击-></font><font color='#0000FF'>项目<font>";
        CharSequence charSequence = Html.fromHtml(html);
        mTextview.setText(charSequence);
        mTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeModeController.toggleThemeSetting(getActivity());
            }
        });
//        mTextview.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                startActivityNoFinish(ProjectWebActivity.class);
//                Intent intent = new Intent();
//                intent.putExtra("name", "atsys-android-defaule");
//                getActivity().sendBroadcast(intent);
//            }
//        });
//        JCVideoUtils.getInstance().JCVideoLoad("http://192.168.6.211/atsys-server-resources/device-api/resources/fileDownload?md5=9fbd32b5371f86ce9e42dd5196cea870", mJzVideo, "测试视频");
    }
}
