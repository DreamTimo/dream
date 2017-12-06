package com.timo.gamelife.login;

import com.timo.gamelife.mvp.BasePresenterImpl;

/**
 * MVPPlugin
 *  邮箱 784787081@qq.com
 */

public class LoginPresenter extends BasePresenterImpl<LoginContract.View> implements LoginContract.Presenter{

    @Override
    public void login(String userName, String password) {
        mView.loginFailed(userName);
    }
}
