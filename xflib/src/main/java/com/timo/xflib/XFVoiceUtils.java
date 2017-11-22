package com.timo.xflib;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.sunflower.FlowerCollector;

/**
 * Created by 蔡永汪 on 2017/11/22.
 */

public class XFVoiceUtils {
    private XFVoiceUtils() {
    }

    private static XFVoiceUtils instance;
    private static String mEngineType = SpeechConstant.TYPE_CLOUD;
    // 默认发音人
    private static String voicer = XFConfig.voicer;
    private static SharedPreferences mSharedPreferences;
    // 语音合成对象
    private static SpeechSynthesizer mSpeak;

    public static XFVoiceUtils getInstance() {
        if (instance == null) {
            instance = new XFVoiceUtils();
        }
        return instance;
    }

    public static void initXFVoice(final Context context) {
        try {
            mSpeak = SpeechSynthesizer.createSynthesizer(context, new InitListener() {
                @Override
                public void onInit(int i) {
                    if (i != ErrorCode.SUCCESS) {
                        Toast.makeText(context, XFConfig.error_play + i, Toast.LENGTH_SHORT).show();
                    } else {
                        // 初始化成功，之后可以调用startSpeaking方法
                        // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
                        // 正确的做法是将onCreate中的startSpeaking调用移至这里
                    }
                }
            });
            mSharedPreferences = context.getSharedPreferences(XFConfig.sp_setting, context.MODE_PRIVATE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void speakMessage(final Context context, String message) {
        try {
            if (TextUtils.isEmpty(message)) {
                return;
            }
            // 移动数据分析，收集开始合成事件
            FlowerCollector.onEvent(context, XFConfig.play);
            // 设置参数
            // 清空参数
            if (mSpeak == null) {
                return;
            }
            mSpeak.setParameter(SpeechConstant.PARAMS, null);
            // 根据合成引擎设置相应参数
            if (mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
                mSpeak.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
                // 设置在线合成发音人
                mSpeak.setParameter(SpeechConstant.VOICE_NAME, voicer);
                //设置合成语速
                mSpeak.setParameter(SpeechConstant.SPEED, mSharedPreferences.getString("speed_preference", "50"));
                //设置合成音调
                mSpeak.setParameter(SpeechConstant.PITCH, mSharedPreferences.getString("pitch_preference", "50"));
                //设置合成音量
                mSpeak.setParameter(SpeechConstant.VOLUME, mSharedPreferences.getString("volume_preference", "50"));
            } else {
                mSpeak.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
                // 设置本地合成发音人 voicer为空，默认通过语记界面指定发音人。
                mSpeak.setParameter(SpeechConstant.VOICE_NAME, "");
                /**
                 * TODO 本地合成不设置语速、音调、音量，默认使用语记设置
                 * 开发者如需自定义参数，请参考在线合成参数设置
                 */
            }
            //设置播放器音频流类型
            mSpeak.setParameter(SpeechConstant.STREAM_TYPE, mSharedPreferences.getString("stream_preference", "3"));
            // 设置播放合成音频打断音乐播放，默认为true
            mSpeak.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");

            // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
            // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
            mSpeak.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
            mSpeak.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/tts.wav");
            int code = mSpeak.startSpeaking(message, new SynthesizerListener() {
                @Override
                public void onSpeakBegin() {

                }

                @Override
                public void onBufferProgress(int i, int i1, int i2, String s) {

                }

                @Override
                public void onSpeakPaused() {

                }

                @Override
                public void onSpeakResumed() {

                }

                @Override
                public void onSpeakProgress(int i, int i1, int i2) {

                }

                @Override
                public void onCompleted(SpeechError speechError) {
                    if (speechError == null) {

                    } else if (speechError != null) {
                        Toast.makeText(context, speechError.getPlainDescription(true), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onEvent(int i, int i1, int i2, Bundle bundle) {

                }
            });
            if (code != ErrorCode.SUCCESS) {
                Toast.makeText(context, XFConfig.error_play + code, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stopSpeak() {
        if (mSpeak != null && mSpeak.isSpeaking()) {
            mSpeak.stopSpeaking();
        }
    }
}
