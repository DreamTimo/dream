package com.timo.gamelife.user;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.timo.gamelife.R;
import com.timo.gamelife.bean.CityInfo;
import com.timo.gamelife.message.MessageFragment;
import com.timo.gamelife.mvp.MVPBaseActivity;
import com.timo.gamelife.bean.Book;
import com.timo.timolib.BaseTools;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * MVPPlugin
 * 邮箱 784787081@qq.com
 */

public class UserActivity extends MVPBaseActivity<UserContract.View, UserPresenter> implements UserContract.View {
    private FragmentTransaction fragmentTransaction;
    private Fragment fragment;

    @Override
    protected int getContentResId() {
        return R.layout.activity_normal;
    }

    @Override
    protected String setTitleName() {
        return "消息界面";
    }

    @Override
    protected void initEvent() {
        fragment = new MessageFragment();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.rl_content, fragment).commit();
    }

    @Override
    public void onSuccess(Book mBook) {

    }

    @Override
    public void onError(String result) {

    }

    @Override
    public void getData(CityInfo info) {

    }
}
