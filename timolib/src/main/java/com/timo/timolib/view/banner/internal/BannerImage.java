package com.timo.timolib.view.banner.internal;

import com.timo.timolib.view.banner.BannerImageView;

/**
 * Created by zhouwk on 2015/11/2 0002.
 */
public class BannerImage {

    private BannerImageView imageView;
    private String url;

    public BannerImage(BannerImageView imageView, String url) {
        this.imageView = imageView;
        this.url = url;
    }
    public BannerImageView getImageView() {
        return imageView;
    }
    public String getUrl() {
        return url;
    }
}