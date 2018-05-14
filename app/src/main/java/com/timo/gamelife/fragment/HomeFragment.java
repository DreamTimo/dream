package com.timo.gamelife.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.timo.gamelife.R;
import com.timo.timolib.BaseTools;
import com.timo.timolib.base.base_fragment.BaseFragment;
import com.timo.timolib.view.CommonWebView;
import com.timo.timolib.view.FloatTouchView;
import com.timo.timolib.view.TitleBar;

/**
 * Created by lykj on 2017/9/12.
 */

public class HomeFragment extends BaseFragment {

    FloatTouchView mBtIcon;
    DrawerLayout mDrawlayout;
    CommonWebView mWebMine;
    TitleBar mWebTitle;

    @Override
    protected String setTitleName() {
        return getString(R.string.main_home);
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
        mBtIcon = (FloatTouchView) view.findViewById(R.id.bt_icon);
        mDrawlayout = (DrawerLayout) view.findViewById(R.id.drawlayout);
        mWebMine = (CommonWebView) view.findViewById(R.id.web_mine);
        mWebTitle = (TitleBar) view.findViewById(R.id.web_title);
        mBtIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawlayout.openDrawer(Gravity.START);
                mDrawlayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                mDrawlayout.setScrimColor(Color.TRANSPARENT);
            }
        });
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
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        mWebMine.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }
        });
    }

}
