package com.timo.timolib;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.multidex.MultiDex;

import com.timo.timolib.tools.logger.AndroidLogAdapter;
import com.timo.timolib.tools.logger.Logger;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.PersistentCookieStore;
import com.zhy.http.okhttp.https.HttpsUtils;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.OkHttpClient;

/**
 * 全局单例Application
 */
public class Timo_Application extends Application {

    private Context mContext;    //全局上下文
    private static Timo_Application instance;      //全局应用对象

    /**
     * 程序的入口方法
     */
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mContext = this;
        MultiDex.install(this);
        initOkHttp();
        initLog();
    }

    public static Handler appHandler = new Handler();

    private void initLog() {
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    private void initOkHttp() {
        CookieJarImpl cookieJar = new CookieJarImpl(new PersistentCookieStore(getApplicationContext()));
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(100000L, TimeUnit.MILLISECONDS)
                .writeTimeout(100000L, TimeUnit.MILLISECONDS)//设置写的超时时间
                .addInterceptor(new LoggerInterceptor("TAG"))
                .cookieJar(cookieJar)
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                })
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .build();
        OkHttpUtils.initClient(okHttpClient);
    }

    /**
     * 单例模式中获取唯一的MyApplication实例
     */
    public static Timo_Application getInstance() {
        if (null == instance) {
            instance = new Timo_Application();
        }
        return instance;
    }

    /**
     * 得到上下文
     */
    public Context getContext() {
        return mContext;
    }

    /**
     * 获取当前应用的版本名称
     * getPackageName()是你当前类的包名，0代表是获取版本信息
     */
    public String getVersionName() {
        PackageManager packageManager = getPackageManager();
        PackageInfo packInfo = null;
        String version = "";
        try {
            packInfo = packageManager.getPackageInfo(getPackageName(), 0);
            version = packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

}
