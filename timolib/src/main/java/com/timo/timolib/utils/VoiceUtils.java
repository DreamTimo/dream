package com.timo.timolib.utils;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by 蔡永汪 on 2017/7/24.
 */

public class VoiceUtils {
    private VoiceUtils() {}
    private static VoiceUtils instance;
    public static VoiceUtils getInstance(){
        if (instance==null){
            instance=new VoiceUtils();
        }
        return instance;
    }
    private MediaPlayer mPlayer = null;
    public void ring(Context context,int resourceId){
        mPlayer = MediaPlayer.create(context,resourceId);
        mPlayer.start();
    }
}
