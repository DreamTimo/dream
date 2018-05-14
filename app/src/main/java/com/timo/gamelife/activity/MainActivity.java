package com.timo.gamelife.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.timo.gamelife.R;
import com.timo.timolib.BaseTools;
import com.timo.timolib.base.base_fragment.FragmentFactory;
import com.timo.gamelife.fragment.HomeFragment;
import com.timo.gamelife.fragment.MineFragment;
import com.timo.gamelife.fragment.DreamFragment;
import com.timo.timolib.base.base_activity.BaseActivity;
import com.timo.timolib.tools.utils.PermissionUtils;
import com.timo.timolib.view.CommonWebView;

public class MainActivity extends BaseActivity {
    RadioButton mRbMain;
    RadioButton mRbFind;
    RadioButton mRbMine;
    RadioGroup mRgBottom;
    CommonWebView mWebview;
    private Class[] fragments = new Class[]{
            HomeFragment.class,
            DreamFragment.class,
            MineFragment.class,
    };

    @Override
    protected int getContentResId() {
        return R.layout.activity_main;
    }

    @Override
    protected String setTitleName() {
        return "梦之启航";
    }

    @Override
    protected void initEvent() {
        mRbMain = (RadioButton) findViewById(R.id.rb_main);
        mRbFind = (RadioButton) findViewById(R.id.rb_find);
        mRbMine = (RadioButton) findViewById(R.id.rb_mine);
        mRgBottom = (RadioGroup) findViewById(R.id.rg_bottom);
        mWebview = (CommonWebView) findViewById(R.id.webview);
        mRgBottom.setOnCheckedChangeListener(new MyCheckChangeListener());
        showFragment(1);
        PermissionUtils.getInstance().applyPermission(this);
    }

    class MyCheckChangeListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId == R.id.rb_main) {
                showFragment(1);
            } else if (checkedId == R.id.rb_find) {
                showFragment(2);
            } else if (checkedId == R.id.rb_mine) {
                showFragment(3);
            }
        }
    }

    private int mCurrentPos = -1;

    public void showFragment(int i) {
        if (mCurrentPos != -1 && fragments[i] == fragments[mCurrentPos]) {
            return;
        }
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentByTag(i + "");
        FragmentTransaction ft = fm.beginTransaction();
        if (mCurrentPos != -1) {
            Fragment hideFragment = fm.findFragmentByTag(mCurrentPos + "");
            ft.hide(hideFragment);
        }
        if (fragment == null) {
            //添加
            ft.add(R.id.fl_container, FragmentFactory.getInstance(fragments[i]), i + "");
        } else {
            //显示
            ft.show(fragment);
        }
        ft.commitAllowingStateLoss();
        mCurrentPos = i;
        ((RadioButton) mRgBottom.getChildAt(mCurrentPos)).setChecked(true);
//        if (i == 0) {
//            BaseTools.loadWeb(mWebview, "file:///android_asset/sakura.html");
//        } else
        if (i == 1) {
            BaseTools.loadWeb(mWebview, "file:///android_asset/snow.html");
        } else if (i == 2) {
            BaseTools.loadWeb(mWebview, "file:///android_asset/sakura.html");
        } else if (i == 3) {
            BaseTools.loadWeb(mWebview, "file:///android_asset/snow.html");
        }
    }

    @Override
    protected boolean isMain() {
        return true;
    }

}
