package com.timo.gamelife.login;

import com.timo.gamelife.mvp.BasePresenter;
import com.timo.gamelife.mvp.BaseView;

/**
 * MVPPlugin
 *  邮箱 784787081@qq.com
 */

public class LoginContract {
    interface View extends BaseView {
        void loginSuccess(String user);
        void loginFailed(String mes);
    }

    interface  Presenter extends BasePresenter<View> {
        void login(String userName,String password);
    }
}
