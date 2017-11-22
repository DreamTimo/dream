package com.timo.timolib.base_activity;

import com.timo.timolib.base.AppManager;

/**
 * Created by 蔡永汪 on 2017/10/31.
 */

public abstract class BaseNetManagerActivity extends BaseNetActivity implements AppManager.AppListener{

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
