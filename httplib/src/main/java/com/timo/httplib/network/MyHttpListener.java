package com.timo.httplib.network;

import android.content.Context;

/**
 * Created by 45590 on 2018/8/14.
 */

public interface MyHttpListener {
    void showLoadingDialog(Context context);

    void dissmissLoadingDialog();

    void showToast(String message);

    void logE(Throwable e);
}
