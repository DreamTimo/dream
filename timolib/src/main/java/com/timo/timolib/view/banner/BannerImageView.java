package com.timo.timolib.view.banner;

import android.content.Context;
import android.util.AttributeSet;

import com.timo.timolib.R;
import com.timo.timolib.utils.GlideUtils;
import com.timo.timolib.view.banner.internal.BannerImage;

public class BannerImageView extends android.support.v7.widget.AppCompatImageView {

    public BannerImageView(Context context) {
        super(context);
    }

    public BannerImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BannerImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setImageUrl(String url) {
        setImage(new BannerImage(this, url));
    }

    public void setImage(BannerImage image) {
        GlideUtils.getInstance().load(getContext(), image.getUrl(), image.getImageView());
    }
}