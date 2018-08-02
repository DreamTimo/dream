package com.timo.timolib.tools.utils;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.timo.timolib.BaseApplication;
import com.timo.timolib.R;

/**
 * Toast
 */
public class ToastUtils {
    private ToastUtils() {
    }

    private int color_default = Color.parseColor("#FFFFFF");
    private int color_error = Color.parseColor("#D8524E");
    private int color_info = Color.parseColor("#3278B5");
    private int color_success = Color.parseColor("#5BB75B");
    private int color_warning = Color.parseColor("#FB9B4D");
    private int color_normal = Color.parseColor("#444344");
    private int color_press_mask = Color.parseColor("#33000000");

    private Context context = BaseApplication.getInstance().getContext();
    private static ToastUtils instance;

    public static ToastUtils getInstance() {
        if (instance == null) {
            instance = new ToastUtils();
        }
        return instance;
    }

    public Toast normal(@NonNull String message) {
        return normal(message, Toast.LENGTH_SHORT, null);
    }

    public Toast normal(@NonNull String message, Drawable icon) {
        return normal(message, Toast.LENGTH_SHORT, icon);
    }

    public Toast normal(@NonNull String message, int duration) {
        return normal(message, duration);
    }

    public Toast normal(@NonNull String message, int duration, Drawable icon) {
        return custom(message, icon,color_normal, duration);
    }

    public Toast warning(@NonNull String message) {
        return warning(message, Toast.LENGTH_SHORT, true);
    }

    public Toast warning(@NonNull String message, int duration) {
        return warning(message, duration, true);
    }

    public Toast warning(@NonNull String message, int duration, boolean withIcon) {
        Drawable icon = null;
        if (withIcon) {
            icon = XOutdatedUtils.getInstance().getDrawable(R.drawable.toast_warning);
        }
        return custom(message, icon,color_warning, duration);
    }

    public Toast info(@NonNull String message) {
        return info(message, Toast.LENGTH_SHORT, true);
    }

    public Toast info(@NonNull String message, int duration) {
        return info(message, duration, true);
    }

    public Toast info(@NonNull String message, int duration, boolean withIcon) {
        Drawable icon = null;
        if (withIcon) {
            icon = XOutdatedUtils.getInstance().getDrawable(R.drawable.toast_info);
        }
        return custom(message, icon,color_info, duration);
    }

    public Toast info(@NonNull String message, int duration, boolean withIcon, boolean newToast) {
        Drawable icon = null;
        if (withIcon) {
            icon = XOutdatedUtils.getInstance().getDrawable(R.drawable.toast_info);
        }
        return custom(message, icon, color_info, duration, newToast);
    }

    public Toast success(@NonNull String message) {
        return success(message, Toast.LENGTH_SHORT, true);
    }

    public Toast success(@NonNull String message, int duration) {
        return success(message, duration, true);
    }

    public Toast success(@NonNull String message, int duration, boolean withIcon) {
        Drawable icon = null;
        if (withIcon) {
            icon = XOutdatedUtils.getInstance().getDrawable(R.drawable.toast_success);
        }
        return custom(message, icon, color_success, duration);
    }

    public Toast success(@NonNull String message, int duration, boolean withIcon, boolean newToast) {
        Drawable icon = null;
        if (withIcon) {
            icon = XOutdatedUtils.getInstance().getDrawable(R.drawable.toast_success);
        }
        return custom(message, icon, color_success, duration, newToast);
    }

    public Toast error(@NonNull String message) {
        return error(message, Toast.LENGTH_SHORT, true);
    }

    public Toast error(@NonNull String message, int duration) {
        return error(message, duration, true);
    }

    public Toast error(@NonNull String message, int duration, boolean withIcon) {
        Drawable icon = null;
        if (withIcon) {
            icon = XOutdatedUtils.getInstance().getDrawable(R.drawable.toast_error);
        }
        return custom(message, icon,color_error, duration);
    }

    public Toast error(@NonNull String message, int duration, boolean withIcon, boolean newToast) {
        Drawable icon = null;
        if (withIcon) {
            icon = XOutdatedUtils.getInstance().getDrawable(R.drawable.toast_error);
        }
        return custom(message, icon, color_error, duration, newToast);
    }

    public Toast custom(@NonNull String message, @ColorInt int tintColor) {
        return custom(message, null, color_default, tintColor, Toast.LENGTH_SHORT);
    }

    public Toast custom(@NonNull String message, @ColorInt int tintColor, boolean newToast) {
        return custom(message, null, color_default, tintColor, Toast.LENGTH_SHORT, newToast);
    }

    public Toast custom(@NonNull String message, Drawable icon, @ColorInt int tintColor) {
        return custom(message, icon,color_default, tintColor, Toast.LENGTH_SHORT);
    }

    public Toast custom(@NonNull String message, Drawable icon, @ColorInt int tintColor, boolean newToast) {
        return custom(message, icon, color_default, tintColor, Toast.LENGTH_SHORT, newToast);
    }

    public Toast custom(@NonNull String message, @ColorInt int tintColor, int duration) {
        return custom(message, null, color_default, tintColor, duration);
    }

    public Toast custom(@NonNull String message, @ColorInt int tintColor, int duration, boolean newToast) {
        return custom(message, null,color_default, tintColor, duration, newToast);
    }

    public Toast custom(@NonNull String message, Drawable icon, @ColorInt int tintColor, int duration) {
        return custom(message, icon, color_default, tintColor, duration);
    }

    public Toast custom(@NonNull String message, Drawable icon, @ColorInt int tintColor, int duration, boolean newToast) {
        return custom(message, icon,color_default, tintColor, duration, newToast);
    }

    public Toast custom(@NonNull String message, @DrawableRes int iconRes, @ColorInt int textColor, @ColorInt int tintColor, int duration) {
        return custom(message, XOutdatedUtils.getInstance().getDrawable(iconRes), textColor, tintColor, duration);
    }

    public Toast custom(@NonNull String message, @DrawableRes int iconRes, @ColorInt int textColor, @ColorInt int tintColor, int duration, boolean newToast) {
        return custom(message, XOutdatedUtils.getInstance().getDrawable(iconRes), textColor, tintColor, duration, newToast);
    }

    /**
     * 自定义toast方法
     *
     * @param message   提示消息文本
     * @param icon      提示消息的icon,传入null代表不显示
     * @param textColor 提示消息文本颜色
     * @param tintColor 提示背景颜色
     * @param duration  显示时长
     * @return
     */

    public Toast custom(@NonNull String message, Drawable icon, @ColorInt int textColor, @ColorInt int tintColor, int duration) {
        Toast currentToast = new Toast(context);
        View toastLayout = LayoutInflater.from(context).inflate(R.layout.toast_view, null);
        ImageView toastIcon = (ImageView) toastLayout.findViewById(R.id.xtoast_icon);
        TextView toastText = (TextView) toastLayout.findViewById(R.id.xtoast_text);
        Drawable drawableFrame = XOutdatedUtils.getInstance().getDrawable(R.drawable.toast_frame);
        drawableFrame.setColorFilter(new PorterDuffColorFilter(tintColor, PorterDuff.Mode.SRC_IN));
        XOutdatedUtils.getInstance().setBackground(toastLayout, drawableFrame);
        if (icon == null) {
            toastIcon.setVisibility(View.GONE);
        } else {
            XOutdatedUtils.getInstance().setBackground(toastIcon, icon);
        }
        toastText.setTextColor(textColor);
        toastText.setText(message);
        toastText.setTypeface(Typeface.SANS_SERIF);

        currentToast.setView(toastLayout);
        currentToast.setDuration(duration);
        currentToast.show();
        return currentToast;
    }

    private Toast currentToast;

    public Toast custom(@NonNull String message, Drawable icon, @ColorInt int textColor, @ColorInt int tintColor, int duration, boolean newToast) {
        if (newToast) {
            Toast currentToast = new Toast(context);
            View toastLayout = LayoutInflater.from(context).inflate(R.layout.toast_view, null);
            ImageView toastIcon = (ImageView) toastLayout.findViewById(R.id.xtoast_icon);
            TextView toastText = (TextView) toastLayout.findViewById(R.id.xtoast_text);
            Drawable drawableFrame = XOutdatedUtils.getInstance().getDrawable(R.drawable.toast_frame);
            drawableFrame.setColorFilter(new PorterDuffColorFilter(tintColor, PorterDuff.Mode.SRC_IN));
            XOutdatedUtils.getInstance().setBackground(toastLayout, drawableFrame);
            if (icon == null) {
                toastIcon.setVisibility(View.GONE);
            } else {
                XOutdatedUtils.getInstance().setBackground(toastIcon, icon);
            }
            toastText.setTextColor(textColor);
            toastText.setText(message);
            toastText.setTypeface(Typeface.SANS_SERIF);

            currentToast.setView(toastLayout);
            currentToast.setDuration(duration);
            currentToast.show();
            return currentToast;
        } else {
            if (currentToast == null) {
                currentToast = new Toast(context);
            }
            View toastLayout = LayoutInflater.from(context).inflate(R.layout.toast_view, null);
            ImageView toastIcon = (ImageView) toastLayout.findViewById(R.id.xtoast_icon);
            TextView toastText = (TextView) toastLayout.findViewById(R.id.xtoast_text);
            Drawable drawableFrame = XOutdatedUtils.getInstance().getDrawable(R.drawable.toast_frame);
            drawableFrame.setColorFilter(new PorterDuffColorFilter(tintColor, PorterDuff.Mode.SRC_IN));
            XOutdatedUtils.getInstance().setBackground(toastLayout, drawableFrame);
            if (icon == null) {
                toastIcon.setVisibility(View.GONE);
            } else {
                XOutdatedUtils.getInstance().setBackground(toastIcon, icon);
            }
            toastText.setTextColor(textColor);
            toastText.setText(message);
            toastText.setTypeface(Typeface.SANS_SERIF);

            currentToast.setView(toastLayout);
            currentToast.setDuration(duration);
            currentToast.show();
            return currentToast;
        }
    }
}
