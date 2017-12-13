package com.timo.gamelife.mvp;

import com.timo.gamelife.retrofit.RetrofitManager;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * MVPPlugin
 * 邮箱 784787081@qq.com
 */

public class BasePresenterImpl<V extends BaseView> implements BasePresenter<V> {
    protected V mView;
    protected RetrofitManager manager;
    protected CompositeSubscription mCompositeSubscription;

    @Override
    public void attachView(V view) {
        mView = view;
        if (manager == null) {
            manager = new RetrofitManager(mView.getContext());
        }
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public void onStop() {
        if (mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.unsubscribe();
        }
    }

    public void addSubscription(Observable observable, Observer observer) {
        mCompositeSubscription.add(observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)
        );
    }
}
