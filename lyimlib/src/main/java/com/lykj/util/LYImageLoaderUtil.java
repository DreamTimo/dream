package com.lykj.util;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class LYImageLoaderUtil {
    public static void picassoLoad(final Context context, final String url, final ImageView view,int errorId){
        if (url==null||view==null||url.length()<=0){
            return;
        }
        Picasso.with(context).load(url).error(errorId).into(view
//                , new com.squareup.picasso.Callback() {
//                    @Override
//                    public void onSuccess() {
//                    }
//
//                    @Override
//                    public void onError() {
//                        Picasso.with(context).load(url)
//                                .resize(LYDeviceInfoUtils.fromDipToPx(context,160),LYDeviceInfoUtils.fromDipToPx(context,160))
//                                .into(view ,new com.squareup.picasso.Callback() {
//                            @Override
//                            public void onSuccess() {
//                            }
//
//                            @Override
//                            public void onError() {
//                                Picasso.with(context).load(url)
//                                        .resize(LYDeviceInfoUtils.fromDipToPx(context,80),LYDeviceInfoUtils.fromDipToPx(context,80))
//                                        .into(view);
//                            }
//                        });
//                    }
//                }
                );
    }
}
