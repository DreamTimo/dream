package com.timo.gamelife.login;


import android.widget.Button;

import com.timo.gamelife.R;
import com.timo.gamelife.mvp.MVPBaseActivity;
import com.timo.timolib.BaseTools;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * MVPPlugin
 * 邮箱 784787081@qq.com
 */

public class LoginActivity extends MVPBaseActivity<LoginContract.View, LoginPresenter> implements LoginContract.View {

    @BindView(R.id.login)
    Button mLogin;

    @Override
    protected int getContentResId() {
        return R.layout.activity_login;
    }

    @Override
    protected String setTitleName() {
        return null;
    }

    @Override
    protected void initEvent() {

    }

    @Override
    public void loginSuccess(String user) {

    }

    @Override
    public void loginFailed(String mes) {
        BaseTools.showToast(mes);
    }

    @OnClick(R.id.login)
    public void onViewClicked() {
        mPresenter.login("user", "password");
    }
}
