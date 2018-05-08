package com.timo.timolib.base.base_activity;

/**
 * Created by chenmeng on 2016/5/12.
 */
public interface BaseMvpModel<P,T> {
        void updateDatas(P params, UpdateListener<T> listener);
        interface UpdateListener<T> {
            void finishUpdate(T result);
            void onError(String msg);
        }
}
