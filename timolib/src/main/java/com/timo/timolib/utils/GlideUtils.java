package com.timo.timolib.utils;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestOptions;
import com.timo.timolib.BaseTools;
import com.timo.timolib.glide.GlideImageView;
import com.timo.timolib.glide.progress.CircleProgressView;
import com.timo.timolib.glide.progress.OnGlideImageViewListener;

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
        if (BaseTools.isEmpty(url) || view == null) return;

        if (url.startsWith("http")) {
            Glide
                    .with(context)
                    .load(url)
                    .into(view);
        } else {
            Glide
                    .with(context)
                    .load("http://" + url)
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

    public void load(Context context, int resourceId, ImageView view, int defaultResId) {
        RequestOptions options = new RequestOptions()
                .placeholder(defaultResId)
                .error(defaultResId)
                .priority(Priority.HIGH);
        Glide
                .with(context)
                .setDefaultRequestOptions(options)
                .load(resourceId)
                .into(view);
    }

    public void load(Context context, int resourceId, ImageView view, int defaultResId, int errorResId) {
        RequestOptions options = new RequestOptions()
                .placeholder(defaultResId)
                .error(errorResId)
                .priority(Priority.HIGH);
        Glide
                .with(context)
                .setDefaultRequestOptions(options)
                .load(resourceId)
                .into(view);
    }

    public void load(Context context, int resourceId, ImageView view, RequestOptions options) {
        Glide
                .with(context)
                .setDefaultRequestOptions(options)
                .load(resourceId)
                .into(view);
    }

    public void load(String url, GlideImageView view, int defaultResId) {
        view.loadImage(url, defaultResId);
    }

    public void load(String url, GlideImageView view, int defaultResId, boolean setCircle, int broderWidth, int broderColor) {
        view.setCircle(setCircle);
        view.setBorderWidth(broderWidth);
        view.setBorderColor(broderColor);
        view.loadImage(url, defaultResId);
    }

    public void load(int resourceId, GlideImageView view, int defaultResId) {
        view.load(resourceId, view.requestOptions(defaultResId));
    }

    public void load(int resourceId, GlideImageView view, int defaultResId, boolean setCircle, int broderWidth, int broderColor) {
        view.setCircle(setCircle);
        view.setBorderWidth(broderWidth);
        view.setBorderColor(broderColor);
        view.load(resourceId, view.requestOptions(defaultResId));
    }

    public void load(String url, GlideImageView view, int defaultResId, final CircleProgressView progressBar) {
        view.loadImage(url, defaultResId).listener(new OnGlideImageViewListener() {
            @Override
            public void onProgress(int percent, boolean isDone, GlideException exception) {
                progressBar.setProgress(percent);
                progressBar.setVisibility(isDone ? View.GONE : View.VISIBLE);
            }
        });
    }

    public void load(String url, GlideImageView view, int defaultResId, final CircleProgressView progressBar, boolean setCircle, int broderWidth, int broderColor) {
        view.setCircle(setCircle);
        view.setBorderWidth(broderWidth);
        view.setBorderColor(broderColor);
        view.loadImage(url, defaultResId).listener(new OnGlideImageViewListener() {
            @Override
            public void onProgress(int percent, boolean isDone, GlideException exception) {
                progressBar.setProgress(percent);
                progressBar.setVisibility(isDone ? View.GONE : View.VISIBLE);
            }
        });
    }

    public void load(int resourceId, GlideImageView view, int defaultResId, final CircleProgressView progressBar) {
        view.load(resourceId, view.requestOptions(defaultResId)).listener(new OnGlideImageViewListener() {
            @Override
            public void onProgress(int percent, boolean isDone, GlideException exception) {
                progressBar.setProgress(percent);
                progressBar.setVisibility(isDone ? View.GONE : View.VISIBLE);
            }
        });
    }

    public void load(int resourceId, GlideImageView view, int defaultResId, final CircleProgressView progressBar, boolean setCircle, int broderWidth, int broderColor) {
        view.setCircle(setCircle);
        view.setBorderWidth(broderWidth);
        view.setBorderColor(broderColor);
        view.load(resourceId, view.requestOptions(defaultResId)).listener(new OnGlideImageViewListener() {
            @Override
            public void onProgress(int percent, boolean isDone, GlideException exception) {
                progressBar.setProgress(percent);
                progressBar.setVisibility(isDone ? View.GONE : View.VISIBLE);
            }
        });
    }

}


