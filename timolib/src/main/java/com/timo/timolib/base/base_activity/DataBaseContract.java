package com.timo.timolib.base.base_activity;

/**
 * Created by 58 on 2016/8/25.
 */
public class DataBaseContract {
    interface GroupListPresenter extends BasePresenter {
        void getData(Object object, String url, Class<?> cls);
    }
}
