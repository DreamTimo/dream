package com.timo.timolib.view.banner.internal;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.timo.timolib.view.banner.BannerImageView;

public class BannerAdapter extends BaseBannerAdapter {

    public BannerAdapter(Context context, BannerData loopData,
                         ViewPager viewPager) {
        super(context, loopData, viewPager);
    }

    /**
     * 控件操作
     * @param imageUrl
     */
    public View instantiateItemView(String imageUrl, int position) {
        BannerImageView mImageView = new BannerImageView(mContext);
        mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.MATCH_PARENT;
        mImageView.setLayoutParams(new ViewGroup.LayoutParams(width, height));
        if(!TextUtils.isEmpty(imageUrl)) {
            mImageView.setImageUrl(imageUrl);
        }
        return mImageView;
    }
}