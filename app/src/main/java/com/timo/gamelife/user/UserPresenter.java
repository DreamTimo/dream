package com.timo.gamelife.user;

import com.timo.gamelife.bean.CityInfo;
import com.timo.gamelife.mvp.BasePresenterImpl;
import com.timo.gamelife.bean.Book;

import rx.Observer;

/**
 * MVPPlugin
 * 邮箱 784787081@qq.com
 */

public class UserPresenter extends BasePresenterImpl<UserContract.View> implements UserContract.Presenter {
    private Book mBook;

    public void getSearchBooks(String name, String tag, int start, int count) {
        addSubscription(manager.getSearchBooks(name, tag, start, count), new Observer<Book>() {
            @Override
            public void onCompleted() {
                if (mBook != null) {
                    mView.onSuccess(mBook);
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                mView.onError("请求失败！！");
            }

            @Override
            public void onNext(Book o) {
                mBook = o;
            }
        });
    }

    private CityInfo mCityInfo;

    @Override
    public void getData() {
        addSubscription(manager.getMyData(), new Observer<CityInfo>() {
            @Override
            public void onCompleted() {
                if (mCityInfo != null) {
                    mView.getData(mCityInfo);
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                mView.onError("请求失败！！");
            }

            @Override
            public void onNext(CityInfo o) {
                mCityInfo = o;
            }
        });
    }
}
