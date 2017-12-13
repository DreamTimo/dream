package com.timo.gamelife.retrofit;

import android.content.Context;

import com.timo.gamelife.bean.Book;
import com.timo.gamelife.bean.CityInfo;

import rx.Observable;

/**
 * Created by win764-1 on 2016/12/12.
 */

public class RetrofitManager {
    private RetrofitService mRetrofitService;

    public RetrofitManager(Context context) {
        this.mRetrofitService = RetrofitHelper.getInstance(context).getServer();
    }

    public Observable<Book> getSearchBooks(String name, String tag, int start, int count) {
        return mRetrofitService.getSearchBooks(name, tag, start, count);
    }
    public Observable<CityInfo> getMyData() {
        return mRetrofitService.getMyData();
    }
}
