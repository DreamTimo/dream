package com.timo.timolib.base.base_fragment;

import com.timo.timolib.base.base_manager.AppManager;

/**
 * Created by 蔡永汪 on 2017/10/31.
 */

public abstract class BaseManagerFragment extends BaseFragment implements AppManager.AppListener {

    @Override
    public void onStart() {
        super.onStart();
        AppManager.getInstance().addListener(this);
    }

    @Override
    public void onDestroy() {
        AppManager.getInstance().removeListener(this);
        super.onDestroy();
    }
}
