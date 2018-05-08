package com.timo.timolib.tools.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.timo.timolib.Timo_Application;

public class NetUtil {
    private NetUtil() {
    }

    public static NetUtil instance;

    public static NetUtil getInstance() {
        if (instance == null) {
            instance = new NetUtil();
        }
        return instance;
    }

    public boolean checkNet(Context context) {
        // 获取手机所以连接管理对象（包括wi-fi，net等连接的管理）
        context = Timo_Application.getInstance().getContext();
        if (context != null) {
            ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (conn != null) {
                // 网络管理连接对象
                NetworkInfo info = conn.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {
                    // 判断当前网络是否连接
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
