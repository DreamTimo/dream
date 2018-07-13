package com.timo.timolib.base.base_activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;

import com.timo.timolib.R;
import com.timo.timolib.BaseConstancts;
import com.timo.timolib.BaseTools;
import com.timo.timolib.BaseApplication;
import com.timo.timolib.tools.permissions.PermissionUtils;

import java.io.Serializable;

/**
 * activity基础类：一般不做变动
 */

public abstract class SuperActivity extends FragmentActivity implements PermissionUtils.PermissionCallbacks {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //夜间模式：暂不使用
        //ChangeModeController.getInstance().init(this, R.attr.class);
        super.onCreate(savedInstanceState);
        setContentView(getContentResId());
        initEvent();
        initEvent(savedInstanceState);
    }

    protected abstract int getContentResId();

    protected abstract void initEvent();

    protected void initEvent(Bundle savedInstanceState) {
    }

    public void startActivityNoFinish(Class<?> cls) {
        startActivity(new Intent(BaseApplication.getInstance().getContext(), cls));
    }

    public void startActivityNoFinish(Class<?> cls, Serializable params) {
        startActivity(new Intent(BaseApplication.getInstance().getContext(), cls).putExtra(BaseConstancts.Params, params));
    }

    public void startActivityAddFinish(Class<?> cls) {
        startActivity(new Intent(BaseApplication.getInstance().getContext(), cls));
        finish();
    }

    public void startActivityAddFinish(Class<?> cls, Serializable params) {
        startActivity(new Intent(BaseApplication.getInstance().getContext(), cls).putExtra(BaseConstancts.Params, params));
        finish();
    }


    /**
     * 返回键监听，主页退出监听
     *
     * @param keyCode
     * @param event
     * @return
     */
    private long mExitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && isMain()) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if ((System.currentTimeMillis() - mExitTime) > BaseConstancts.exit_time) {
                    BaseTools.showToast(getString(R.string.hint_exit));
                    mExitTime = System.currentTimeMillis();
                } else {
                    if (BaseConstancts.exit_to_back) {
                        moveTaskToBack(false);
                    } else {
                        stopAndFinish();
                    }
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 停止程序
     */
    protected void stopAndFinish() {
        finish();
        Process.killProcess(Process.myPid());
        System.exit(0);
    }

    /**
     * 判断是否是首页
     */
    protected boolean isMain() {
        return false;
    }

    /**
     * 请求权限的返回值：不操作
     */
    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
