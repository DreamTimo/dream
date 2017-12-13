package com.timo.gamelife;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.timo.timolib.MyApplication;
import com.timo.timolib.utils.GlideUtils;
import com.timo.timolib.view.ninegridview.NineGridView;
import com.timo.xflib.XFVoiceUtils;

/**
 * Created by 蔡永汪 on 2017/11/8.
 */

public class App extends MyApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        SpeechUtility.createUtility(App.this, SpeechConstant.APPID + "=5a14e3a0");
        XFVoiceUtils.getInstance().initXFVoice(App.this);//初始化讯飞语音
        NineGridView.setImageLoader(new PicassoImageLoader());
    }

    /**
     * Picasso 加载
     */
    private class PicassoImageLoader implements NineGridView.ImageLoader {

        @Override
        public void onDisplayImage(Context context, ImageView imageView, String url) {
            GlideUtils.getInstance().load(context, url, imageView);
        }

        @Override
        public Bitmap getCacheImage(String url) {
            return null;
        }
    }
}
