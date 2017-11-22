package com.timo.timolib.utils;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 * Created by 蔡永汪 on 2017/5/16.
 */

public class PopupUtils {
    private PopupWindow window;
    private static PopupUtils popupInstance;

    private PopupUtils() {
    }

    public static PopupUtils getInstance() {
        if (popupInstance == null) {
            popupInstance = new PopupUtils();
        }
        return popupInstance;
    }

    /**
     * 弹出评论对话框
     */
    public void showPopup(View groupView, View contentView, int gravityLocation) {
        //创建PopupWindow
        if (window == null) {
            window = new PopupWindow(contentView,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        window.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);// 额外的设置(让popupWindow只弹出一个)
        window.setFocusable(true);// 获得焦点
        //window.setBackgroundDrawable(new ColorDrawable());//禁止滑动取消
        window.setTouchable(true);
        //显示PopupWindow
        window.showAtLocation(groupView, gravityLocation, 0, 0);
        window.setTouchInterceptor(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE && !window.isFocusable()) {
                    dismiss();
                    return true;
                }
                return false;
            }
        });
    }

    public void dismiss() {
        if (window != null) {
            window.dismiss();
            window = null;
        }
    }
}
