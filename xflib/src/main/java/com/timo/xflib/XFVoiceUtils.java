package com.timo.xflib;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.iflytek.sunflower.FlowerCollector;

import org.json.JSONException;
import org.json.JSONObject;

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
            if (mSharedPreferences == null) {
                mSharedPreferences = context.getSharedPreferences(XFConfig.sp_setting, context.MODE_PRIVATE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void xfSpeak(final Context context, String message) {
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

    // 语音听写UI
    private static RecognizerDialog mIatDialog;
    private static Context mContext;

    public static void initXFHear(final Context context) {
        try {
            mContext = context;
            // 初始化听写Dialog，如果只使用有UI听写功能，无需创建SpeechRecognizer
            // 使用UI听写功能，请根据sdk文件目录下的notice.txt,放置布局文件和图片资源
            mIatDialog = new RecognizerDialog(context, mInitListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化监听器。
     */
    private static InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {
                Toast.makeText(mContext, "初始化失败，错误码：" + code, Toast.LENGTH_SHORT).show();

            }
        }
    };
    /**
     * 听写UI监听器
     */
    private static RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
        public void onResult(RecognizerResult results, boolean isLast) {
            printResult(mContext, results);
        }

        /**
         * 识别回调错误.
         */
        public void onError(SpeechError error) {
            Toast.makeText(mContext, error.getPlainDescription(true), Toast.LENGTH_SHORT).show();
        }

    };

    // 引擎类型
    public static void xfHear(XFListener listener) {
        try {
            mXFListener=listener;
            // 显示听写对话框
            mIatDialog.setListener(mRecognizerDialogListener);
            mIatDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printResult(Context context, RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());

        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (mXFListener != null && !TextUtils.isEmpty(sn)) {
            mXFListener.listener(sn);
        }
    }

    private static XFListener mXFListener;

    public interface XFListener {
        void listener(String result);
    }

    public static void stopSpeak() {
        if (mSpeak != null && mSpeak.isSpeaking()) {
            mSpeak.stopSpeaking();
        }
    }
}
