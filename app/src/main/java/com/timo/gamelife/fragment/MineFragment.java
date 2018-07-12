package com.timo.gamelife.fragment;

import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.timo.gamelife.BannerUtils;
import com.timo.gamelife.R;
import com.timo.gamelife.activity.projectweb.ProjectWebActivity;
import com.timo.timolib.BaseTools;
import com.timo.timolib.base.base_fragment.BaseFragment;
import com.timo.timolib.view.CommonWebView;
import com.timo.timolib.view.banner.Banner;

import java.util.ArrayList;
import java.util.List;

import cn.jzvd.JZVideoPlayerStandard;

/**
 * Created by lykj on 2017/9/12.
 */

public class MineFragment extends BaseFragment {

    Banner mBanner;
    CommonWebView mWebview;
    TextView mTextview;
    JZVideoPlayerStandard mJzVideo;
    List<String> data;

    @Override
    protected int getContentResId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initEvent(View view) {
        mBanner = (Banner) view.findViewById(R.id.banner);
        mWebview = (CommonWebView) view.findViewById(R.id.webview);
        mTextview = (TextView) view.findViewById(R.id.textview);
        mJzVideo = (JZVideoPlayerStandard) view.findViewById(R.id.jz_video);
        data = new ArrayList<>();
        data.add("http://aiwolvju.b0.upaiyun.com/uploads/20171211/fbfrebpymwaadvb9uckzmyb1iw81axet.tmp");
        data.add("http://aiwolvju.b0.upaiyun.com/uploads/20171211/fbfrebpymwaadvb9uckzmyb1iw81axet.tmp");
        data.add("http://aiwolvju.b0.upaiyun.com/uploads/20171211/fbfrebpymwaadvb9uckzmyb1iw81axet.tmp");
        data.add("http://aiwolvju.b0.upaiyun.com/uploads/20171211/fbfrebpymwaadvb9uckzmyb1iw81axet.tmp");
        BannerUtils.getInstance().setHouseProfiltBanner(getActivity(), mBanner, data);
        mJzVideo.setUp("http://jzvd.nathen.cn/c6e3dc12a1154626b3476d9bf3bd7266/6b56c5f0dc31428083757a45764763b0-5287d2089db37e62345123a1be272f8b.mp4",
                JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL,
                "测试视频");
        mJzVideo.thumbImageView.setImageResource(R.mipmap.ic_launcher);
        BaseTools.loadWeb(mWebview, "file:///android_asset/index.html");
        String html = "<font color='#ff0000'>点击-></font><font color='#0000FF'>项目<font>";
        CharSequence charSequence = Html.fromHtml(html);
        mTextview.setText(charSequence);
        mTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityNoFinish(ProjectWebActivity.class);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        mJzVideo.releaseAllVideos();
    }
}
