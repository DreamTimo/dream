package com.timo.timolib.utils;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;

import com.timo.timolib.MyApplication;

/**
 * 此类主要是用来放一些系统过时方法的处理
 */
public class XOutdatedUtils {

    private XOutdatedUtils() {}
    private static XOutdatedUtils instance;
    public static XOutdatedUtils getInstance(){
        if (instance==null){
            instance=new XOutdatedUtils();
        }
        return instance;
    }

    /**
     * setBackgroundDrawable过时方法处理
     *
     * @param view
     * @param drawable
     */
    public void setBackground(@NonNull View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            view.setBackground(drawable);
        else
            view.setBackgroundDrawable(drawable);
    }

    /**
     * getDrawable过时方法处理
     *
     * @param id
     * @return
     */
    public Drawable getDrawable(@DrawableRes int id) {
        return ContextCompat.getDrawable(MyApplication.getInstance().getContext(), id);
    }

    /**
     * getDrawable过时方法处理
     *
     * @param id 资源id
     * @param theme 指定主题
     * @return
     */
    public Drawable getDrawable(@DrawableRes int id,
                                       @Nullable Resources.Theme theme) {
        return ResourcesCompat.getDrawable(MyApplication.getInstance().getResources(), id, theme);
    }

    /**
     * getColor过时方法处理
     *
     * @param id
     * @return
     */
    public int getColor(@ColorRes int id) {
        return ContextCompat.getColor(MyApplication.getInstance().getContext(), id);
    }

    /**
     * getColor过时方法处理
     *
     * @param id 资源id
     * @param theme 指定主题
     * @return
     */
    public int getColor(@ColorRes int id, @Nullable Resources.Theme theme) {
        return ResourcesCompat.getColor(MyApplication.getInstance().getResources(), id, theme);
    }

    /**
     * getColorStateList过时方法处理
     *
     * @param id 资源id
     * @return
     */
    public ColorStateList getColorStateList(@ColorRes int id) {
        return ContextCompat.getColorStateList(MyApplication.getInstance().getContext(), id);
    }

    /**
     * getColorStateList过时方法处理
     *
     * @param id 资源id
     * @param theme 指定主题
     * @return
     */
    public ColorStateList getColorStateList(@ColorRes int id, @Nullable Resources.Theme theme) {
        return ResourcesCompat.getColorStateList(MyApplication.getInstance().getResources(), id, theme);
    }
}
