package com.timo.gamelife;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.timo.timolib.MyApplication;
import com.timo.xflib.XFVoiceUtils;

/**
 * Created by 蔡永汪 on 2017/11/8.
 */

public class App extends MyApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        SpeechUtility.createUtility(App.this, SpeechConstant.APPID + "=5a14e3a0");
//        XFVoiceUtils.getInstance().initXFVoice(App.this);//初始化讯飞语音
    }
}
