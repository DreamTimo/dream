package com.timo.timolib.base.base_fragment;

import android.support.v4.app.Fragment;

/**
 * Created by Apple on 2016/12/1.
 * Fragment的工厂类
 */

public final class FragmentFactory {
    private FragmentFactory() {
    }

    //获取实例
    public static synchronized Fragment getInstance(Class<? extends Fragment> clazz) {
        Fragment fragment = null;
        try {
            fragment = clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return fragment;
    }
}
