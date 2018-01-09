package com.timo.gamelife.fragment;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.timo.gamelife.BannerUtils;
import com.timo.gamelife.R;
import com.timo.timolib.BaseTools;
import com.timo.timolib.base_fragment.BaseFragment;
import com.timo.timolib.view.CommonWebView;
import com.timo.timolib.view.banner.Banner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

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

    @Override
    protected String setTitleName() {
        return "我的页面";
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
        String html = "<font color='#ff0000'>颜色1</font><font color='#0000FF'>颜色2<font>";
        CharSequence charSequence = Html.fromHtml(html);
        mTextview.setText(charSequence);
        mTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseTools.showToast("11111");
            }
        });
    }

}
