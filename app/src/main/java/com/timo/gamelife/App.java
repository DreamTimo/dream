package com.timo.gamelife;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.timo.timolib.BaseTools;
import com.timo.timolib.BaseApplication;
import com.timo.timolib.view.ninegridview.NineGridView;

/**
 * Created by 蔡永汪 on 2017/11/8.
 */

public class App extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        NineGridView.setImageLoader(new PicassoImageLoader());
    }

    /**
     * Picasso 加载
     */
    private class PicassoImageLoader implements NineGridView.ImageLoader {

        @Override
        public void onDisplayImage(Context context, ImageView imageView, String url) {
            BaseTools.load(context,url,imageView);
        }

        @Override
        public Bitmap getCacheImage(String url) {
            return null;
        }
    }
}
