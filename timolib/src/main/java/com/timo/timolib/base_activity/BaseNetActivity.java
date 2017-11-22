package com.timo.timolib.base_activity;

import android.os.Bundle;
import android.view.View;

import com.timo.timolib.BaseTools;
import com.timo.timolib.http.MyHttpParams;

/**
 * Created by 蔡永汪 on 2017/10/31.
 * 与网络进行联合使用。
 */

public abstract class BaseNetActivity<T> extends BaseActivity implements ShowDataContract<T> {
    private StateContract.StateView mView;
    protected DataBasePresenter<T> groupPresenter;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        try {
            initView();
        } catch (Exception e) {
            BaseTools.printErrorMessage(e);
        }
    }

    @Override
    public void setContentView(final int layoutResID) {
        try {
            mView = new BaseNetView(this) {
                @Override
                public View getChildRootView() {
                    return getChildView(layoutResID);
                }

                @Override
                public void showLoadStatus(int state) {
                    super.showLoadStatus(state);
                    showLoadState(state);
                }
            };
        } catch (Exception e) {
            BaseTools.printErrorMessage(e);
        }
        super.setContentView(mView.getRootView());
    }

    protected void initView() {
        try {
            groupPresenter = new DataBasePresenter<T>(this, getStateView(), this) {
            };
            groupPresenter.getData(getRequest(), getUrl(), getApi());
        } catch (Exception e) {
            BaseTools.printErrorMessage(e);
        }
    }
    @Override
    public void setPresenter(DataBasePresenter presenter) {}
    @Override
    public void showError(String message) {}
    protected abstract Class getApi();

    protected abstract MyHttpParams getRequest();

    protected abstract String getUrl();

    protected void showLoadState(int state) {
    }

    protected View getChildView(int layoutResID) {
        return View.inflate(this, layoutResID, null);
    }

    protected final StateContract.StateView getStateView() {
        return mView;
    }
}
