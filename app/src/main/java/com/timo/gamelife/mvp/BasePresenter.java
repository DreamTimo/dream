package com.timo.gamelife.mvp;

/**
 * MVPPlugin
 *  邮箱 784787081@qq.com
 */

public interface  BasePresenter <V extends BaseView>{
    void attachView(V view);
    void onStop();
    void detachView();
}
