package com.timo.timolib.base.base_activity;
import com.timo.timolib.base.AppManager;

/**
 * activity管理
 */
public abstract class BaseManagerActivity extends BaseActivity implements AppManager.AppListener{

    @Override
    protected void onStart() {
        super.onStart();
        AppManager.getInstance().addListener(this);
    }

    @Override
    protected void onDestroy() {
        AppManager.getInstance().removeListener(this);
        super.onDestroy();
    }
}