package com.timo.timolib.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.net.URL;

/**
 * 图片加载工具类
 */
public class GlideUtils {
    private static GlideUtils instance;

    private GlideUtils() {
    }

    public static GlideUtils getInstance() {
        if (instance == null) {
            instance = new GlideUtils();
        }
        return instance;
    }

    public void load(Context context, String url, ImageView view) {
        if (url.startsWith("http")) {
            Glide
                    .with(context)
                    .load(url)
                    .into(view);
        } else {
            Glide
                    .with(context)
                    .load("http://118.26.142.132:8080/ms-appWeb/" + url)
                    .into(view);
        }

    }

    public void load(Context context, URL url, ImageView view) {
        Glide
                .with(context)
                .load(url)
                .into(view);
    }

    public void load(Context context, File file, ImageView view) {
        Glide
                .with(context)
                .load(file)
                .into(view);
    }

    public void load(Context context, int resourceId, ImageView view) {
        Glide
                .with(context)
                .load(resourceId)
                .into(view);
    }
} 
