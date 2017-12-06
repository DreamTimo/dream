package com.timo.gamelife.user;

import com.timo.gamelife.mvp.BasePresenterImpl;
import com.timo.gamelife.bean.Book;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * MVPPlugin
 * 邮箱 784787081@qq.com
 */

public class UserPresenter extends BasePresenterImpl<UserContract.View> implements UserContract.Presenter {
    private Book mBook;

    public void getSearchBooks(String name, String tag, int start, int count) {
        mCompositeSubscription.add(manager.getSearchBooks(name, tag, start, count)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Book>() {
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
                    public void onNext(Book book) {
                        mBook = book;
                    }
                })
        );
    }
}
