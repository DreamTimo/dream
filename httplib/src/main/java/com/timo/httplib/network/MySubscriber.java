package com.timo.httplib.network;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import com.timo.httplib.R;
import com.timo.httplib.network.base.NetUtil;

import rx.Subscriber;

public abstract class MySubscriber<T> extends Subscriber<T> {

    private Context mContext;
    private String msg;
    private boolean showDialog = true;

    public MySubscriber(Context context, String msg, boolean showDialog) {
        this.mContext = context;
        this.msg = msg;
        this.showDialog = showDialog;
    }

    public MySubscriber(Context context) {
        this(context, context.getString(R.string.hint_loading), true);
    }

    public MySubscriber(Context context, boolean showDialog) {
        this(context, context.getString(R.string.hint_loading), showDialog);
    }

    @Override
    public void onCompleted() {
        if (showDialog) {
            MyHttp.dissmissLoadingDialog();
        }
    }

    @Override
    public void onStart() {
        if (showDialog) {
            try {
                MyHttp.showLoadingDialog(mContext);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onNext(T t) {
        _onNext(t);
    }

    @Override
    public void onError(Throwable e) {
        if (showDialog) {
            MyHttp.dissmissLoadingDialog();
        }
        MyHttp.logE(e);
        if (!NetUtil.getInstance().isNetConnected(mContext)) {
            _onError(mContext.getString(R.string.error_no_network));
        } else {
            _onError(mContext.getString(R.string.error_network));
        }
    }

    protected abstract void _onNext(T t);

    @SuppressLint("WrongConstant")
    protected void _onError(String message) {
        if (!TextUtils.isEmpty(message)) {
            MyHttp.showToast(message);
        }
    }

}
