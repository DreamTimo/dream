package com.timo.timolib.tools.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.timo.timolib.R;
import com.timo.timolib.base.base_dialog.DialogListener;

public class DialogUtils {
    private static DialogUtils instance;

    private DialogUtils() {
    }

    public static DialogUtils getInstance() {
        if (instance == null) {
            instance = new DialogUtils();
        }
        return instance;
    }

    /**
     * 加载框
     */
    private Dialog loadingDialog;

    public Dialog getLoadingDialog(Context context) {
        if (loadingDialog == null) {
            loadingDialog = new Dialog(context, R.style.loading_dialog);
        }
        loadingDialog.setCancelable(false);
        loadingDialog.setContentView(R.layout.common_dialog_loading);
        return loadingDialog;
    }

    public Dialog showLoadingDialog(Context context, String loadingMsg, boolean cancelable) {
        if (loadingDialog == null) {
            loadingDialog = new Dialog(context, R.style.loading_dialog);
        }
        loadingDialog.setCancelable(cancelable);
        View mView = LayoutInflater.from(context).inflate(R.layout.common_dialog_loading, null);
        loadingDialog.setContentView(mView);
        if (!TextUtils.isEmpty(loadingMsg)) {
            TextView textView = (TextView) mView.findViewById(R.id.tipTextView);
            textView.setText(loadingMsg);
        }
        loadingDialog.show();
        return loadingDialog;
    }

    /**
     * 确认、取消  --选择框
     */

    public void showTwoChoiceDialog(Context context, String description, final DialogListener listener) {
        try {
            final AlertDialog ad = new AlertDialog.Builder(context, R.style.loading_dialog).create();
            ad.show();
            Window window = ad.getWindow();
            window.setContentView(R.layout.dialog_two_choice);
            if (description != null) {
                TextView tv_title = (TextView) window.findViewById(R.id.tv_title);
                tv_title.setText(description);
            }
            Button bt_cancel = (Button) window.findViewById(R.id.bt_cancel);
            Button bt_confirm = (Button) window.findViewById(R.id.bt_confirm);
            bt_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ad.dismiss();
                    listener.cancel();
                }
            });
            bt_confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ad.dismiss();
                    listener.confirm();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cancelLoadingDialog() {
        if (loadingDialog != null) {
            loadingDialog.cancel();
            loadingDialog = null;
        }
    }
}
