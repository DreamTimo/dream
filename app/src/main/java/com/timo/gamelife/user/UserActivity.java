package com.timo.gamelife.user;

import android.widget.TextView;

import com.timo.gamelife.R;
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

    @BindView(R.id.description)
    TextView mDescription;

    @Override
    protected int getContentResId() {
        return R.layout.activity_welcome;
    }

    @Override
    protected String setTitleName() {
        return "用户";
    }

    @Override
    protected void initEvent() {

    }

    @Override
    public void onSuccess(Book mBook) {
        BaseTools.showToast("title" + mBook.getBooks().get(0).getAlt_title());
    }

    @Override
    public void onError(String result) {
        BaseTools.showToast(result);
    }

    @OnClick(R.id.description)
    public void onViewClicked() {
        mPresenter.getSearchBooks("金瓶梅", null, 0, 1);
    }
}
