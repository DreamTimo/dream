package com.timo.timolib.network;

import android.content.Context;
import android.text.TextUtils;

import com.timo.timolib.BaseConstancts;
import com.timo.timolib.BaseTools;
import com.timo.timolib.R;
import com.timo.timolib.network.base.CaheInterceptor;
import com.timo.timolib.network.base.NovateCookieManger;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Http {
    private static volatile Retrofit retrofit_string;
    private static volatile Retrofit retrofit_gson;
    private static OkHttpClient httpClient;
    private static String baseUrl;

    public static void init(String url, OkHttpClient client) {
        baseUrl = url;
        httpClient = client;
    }

    public static void init(String url) {
        init(url, null);
    }

    /**
     * 获取Gson类型数据
     *
     * @param context
     * @return
     */
    public static <T> T getGsonApi(Context context, Class<T> service) {
        if (TextUtils.isEmpty(baseUrl)) {
            BaseTools.showToast(context.getString(R.string.http_error_not_init));
            return null;
        }
        if (retrofit_gson == null) {
            synchronized (Http.class) {
                if (retrofit_gson == null) {
                    if (httpClient == null) {
                        httpClient = getUnsafeOkHttpClient();
                    }
                    retrofit_gson = new Retrofit.Builder().baseUrl(baseUrl)
                            .client(httpClient)
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .client(new OkHttpClient.Builder()
                                    .cookieJar(new NovateCookieManger(context))
                                    .cache(new Cache(new File(context.getExternalCacheDir(), BaseConstancts.http_cache), 10 * 1024 * 1024))
                                    .addInterceptor(new CaheInterceptor(context))
                                    .addNetworkInterceptor(new CaheInterceptor(context))
                                    .connectTimeout(30, TimeUnit.SECONDS)
                                    .build())
                            .build();
                }
            }
        }
        return retrofit_gson.create(service);
    }

    /**
     * 获取String类型数据
     *
     * @param context
     * @return
     */
    public static <T> T getStringApi(Context context, Class<T> service) {
        if (TextUtils.isEmpty(baseUrl)) {
            BaseTools.showToast(context.getString(R.string.http_error_not_init));
            return null;
        }
        if (retrofit_string == null) {
            synchronized (Http.class) {
                if (retrofit_string == null) {
                    if (httpClient == null) {
                        httpClient = getUnsafeOkHttpClient();
                    }
                    retrofit_string = new Retrofit.Builder().baseUrl(baseUrl)
                            .client(httpClient)
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .client(new OkHttpClient.Builder()
                                    .cookieJar(new NovateCookieManger(context))
                                    .cache(new Cache(new File(context.getExternalCacheDir(), BaseConstancts.http_cache), 10 * 1024 * 1024))
                                    .addInterceptor(new CaheInterceptor(context))
                                    .addNetworkInterceptor(new CaheInterceptor(context))
                                    .connectTimeout(30, TimeUnit.SECONDS)
                                    .build())
                            .build();
                }
            }
        }
        return retrofit_string.create(service);
    }

    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final javax.net.ssl.SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            OkHttpClient okHttpClient = builder.build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
