package com.timo.timolib.network.rx;

import android.content.Context;

import com.timo.timolib.BaseTools;
import com.timo.timolib.R;
import com.timo.timolib.BaseApplication;
import com.timo.timolib.tools.utils.DialogUtils;
import com.timo.timolib.tools.utils.NetUtil;

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
        this(context, BaseApplication.getInstance().getContext().getString(R.string.loading), true);
    }

    public MySubscriber(Context context, boolean showDialog) {
        this(context, BaseApplication.getInstance().getContext().getString(R.string.loading), showDialog);
    }

    @Override
    public void onCompleted() {
        if (showDialog) {
            DialogUtils.getInstance().cancelLoadingDialog();
        }
    }

    @Override
    public void onStart() {
        if (showDialog) {
            try {
                DialogUtils.getInstance().showLoadingDialog(mContext, msg, false);
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
            DialogUtils.getInstance().cancelLoadingDialog();
        }
        BaseTools.e(e);
        if (!NetUtil.getInstance().isNetConnected(BaseApplication.getInstance().getContext())) {
            _onError(BaseApplication.getInstance().getContext().getString(R.string.error_no_network));
        } else {
            _onError(BaseApplication.getInstance().getContext().getString(R.string.error_network));
        }
    }

    protected abstract void _onNext(T t);

    protected void _onError(String message) {
        BaseTools.showToast(message);
    }

}
