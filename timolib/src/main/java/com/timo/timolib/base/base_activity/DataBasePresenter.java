package com.timo.timolib.base.base_activity;

import android.content.Context;

import com.timo.timolib.BaseTools;

/**
 * Created by 58 on 2016/8/26.
 */
public abstract class DataBasePresenter<T> extends BaseNetPresenter<T> implements DataBaseContract.GroupListPresenter {
    private ShowDataContract<T> mView;
    private boolean isHandleLoading = false;
    private StateListener<T> stateListener;

    public DataBasePresenter(Context mContext, StateContract.StateView mSView, ShowDataContract<T> mView) {
        super(mContext, mSView);
        mView.setPresenter(this);
        this.mView = mView;
    }

    @Override
    public void getData(Object object, String url, Class<?> cls) {
        try {
            stateListener = new StateListener<T>() {
                @Override
                public void finishUpdate(T result) {
                    if (!isHandleLoading) {
                        super.finishUpdate(result);
                    }
                    mView.showData(result);
                }

                @Override
                public void onError(String msg) {
                    mView.showError(msg);
                    super.onError(msg);
                }
            };
            updateData(stateListener, url, object, cls);
        } catch (Exception e) {
            BaseTools.printErrorMessage(e);
        }
    }

    public void loadingFinish() {
        if (isHandleLoading) {
            stateListener.loadingFinish();
        }
    }


    public boolean isHandleLoading() {
        return isHandleLoading;
    }

    public void setHandleLoading(boolean handleLoading) {
        isHandleLoading = handleLoading;
    }


}
