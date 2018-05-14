package com.timo.timolib.tools.rx;
import android.content.Context;

import com.timo.timolib.R;
import com.timo.timolib.Timo_Application;
import com.timo.timolib.tools.utils.DialogUtils;
import com.timo.timolib.tools.utils.NetUtil;

import rx.Subscriber;

/**
 * des:订阅封装
 * Created by xsf
 * on 2016.09.10:16
 */

/********************使用例子********************/
/*_apiService.login(mobile, verifyCode)
        .//省略
        .subscribe(new RxSubscriber<User user>(mContext,false) {
@Override
public void _onNext(User user) {
        // 处理user
        }

@Override
public void _onError(String msg) {
        ToastUtil.showShort(mActivity, msg);
        });*/
public abstract class RxSubscriber<T> extends Subscriber<T> {

    private Context mContext;
    private String msg;
    private boolean showDialog = true;

    /**
     * 是否显示浮动dialog
     */
    public void showDialog() {
        this.showDialog = true;
    }

    public void hideDialog() {
        this.showDialog = true;
    }

    public RxSubscriber(Context context, String msg, boolean showDialog) {
        this.mContext = context;
        this.msg = msg;
        this.showDialog = showDialog;
    }

    public RxSubscriber(Context context) {
        this(context, Timo_Application.getInstance().getContext().getString(R.string.loading), true);
    }

    public RxSubscriber(Context context, boolean showDialog) {
        this(context, Timo_Application.getInstance().getContext().getString(R.string.loading), showDialog);
    }

    @Override
    public void onCompleted() {
        if (showDialog)
            DialogUtils.getInstance().cancelLoadingDialog();
    }

    @Override
    public void onStart() {
        super.onStart();
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
        if (showDialog)
            DialogUtils.getInstance().cancelLoadingDialog();
        e.printStackTrace();
        //网络
        if (!NetUtil.getInstance().isNetConnected(Timo_Application.getInstance().getContext())) {
            _onError(Timo_Application.getInstance().getContext().getString(R.string.no_net));
        }
        //服务器
        else if (e instanceof ServerException) {
            _onError(e.getMessage());
        }
        //其它
        else {
            _onError(Timo_Application.getInstance().getContext().getString(R.string.net_error));
        }
    }

    protected abstract void _onNext(T t);

    protected abstract void _onError(String message);

}
