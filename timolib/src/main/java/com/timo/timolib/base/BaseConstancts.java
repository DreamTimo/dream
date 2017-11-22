package com.timo.timolib.base;

import android.graphics.Color;
import android.support.annotation.ColorInt;

/**
 * 常量类
 */
public interface BaseConstancts {

    String BASE_SHARE_NAME = "base_share_name";//sp保存名称
    String BASE_PARAM = "base_param";
    String BASE_PASSWORD = "base_password"; //密码

    @ColorInt
    int COLOR_DEFAULT_TEXT = Color.parseColor("#FFFFFF");
    @ColorInt
    int COLOR_ERROR = Color.parseColor("#D8524E");
    @ColorInt
    int COLOR_INFO = Color.parseColor("#3278B5");
    @ColorInt
    int COLOR_SUCCESS = Color.parseColor("#5BB75B");
    @ColorInt
    int COLOR_WARNING = Color.parseColor("#FB9B4D");
    @ColorInt
    int COLOR_NORMAL = Color.parseColor("#444344");
}

