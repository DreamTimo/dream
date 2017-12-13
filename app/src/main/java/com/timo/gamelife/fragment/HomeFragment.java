package com.timo.gamelife.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.timo.gamelife.R;
import com.timo.gamelife.user.UserActivity;
import com.timo.timolib.BaseTools;
import com.timo.timolib.base_fragment.BaseFragment;
import com.timo.timolib.view.CommonWebView;
import com.timo.timolib.view.FloatTouchView;
import com.timo.timolib.view.TitleBar;
import com.timo.xflib.XFVoiceUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by lykj on 2017/9/12.
 */

public class HomeFragment extends BaseFragment {

    @BindView(R.id.bt_icon)
    FloatTouchView mBtIcon;
    @BindView(R.id.drawlayout)
    DrawerLayout mDrawlayout;
    @BindView(R.id.web_mine)
    CommonWebView mWebMine;
    @BindView(R.id.web_title)
    TitleBar mWebTitle;

    @Override
    protected String setTitleName() {
        return "首页";
    }

    @Override
    protected void setTitle(View view) {
        super.setTitle(view);
    }

    @Override
    protected int getContentResId() {
        return R.layout.fragment_dream;
    }

    @Override
    protected void initEvent(View view) {
        mDrawlayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        BaseTools.setTitleBar(mWebTitle, "个人介绍", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWebMine.canGoBack()) {
                    mWebMine.goBack();
                } else {
                    mDrawlayout.closeDrawers();
                }
            }
        });

        mDrawlayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                BaseTools.loadWeb(mWebMine, "file:///android_asset/my.html");
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                XFVoiceUtils.getInstance().stopSpeak();
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        mWebMine.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (url.contains("my.html")) {
                    XFVoiceUtils.getInstance().xfSpeak(getContext(), getString(R.string.introduction_a));
                } else if (url.contains("resume.html")) {
                    XFVoiceUtils.getInstance().xfSpeak(getContext(), getString(R.string.introduction_b));
                }
            }
        });

        XFVoiceUtils.getInstance().initXFHear(getActivity());
        mBtIcon.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
//                XFVoiceUtils.getInstance().xfHear(mXFListener);
                Intent intent = new Intent(getContext(), UserActivity.class);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    ActivityOptions options = ActivityOptions
//                            .makeSceneTransitionAnimation((Activity) getActivity(), mBtIcon, "电影图片");
//                    getActivity().startActivity(intent, options.toBundle());
//                } else {
//                    //让新的Activity从一个小的范围扩大到全屏
                    ActivityOptionsCompat options = ActivityOptionsCompat
                            .makeScaleUpAnimation(mBtIcon, mBtIcon.getWidth() / 2, mBtIcon.getHeight() / 2, 0, 0);
                    ActivityCompat.startActivity((Activity) getActivity(), intent, options.toBundle());
//                }
                startActivityNoFinish(UserActivity.class);
                return false;
            }
        });

    }

    private XFVoiceUtils.XFListener mXFListener = new XFVoiceUtils.XFListener() {
        @Override
        public void listener(String result) {
            BaseTools.showToast(result);
        }
    };

    @OnClick({R.id.bt_icon})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_icon:
                mDrawlayout.openDrawer(Gravity.START);
                mDrawlayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                mDrawlayout.setScrimColor(Color.TRANSPARENT);
                break;
        }
    }
}
