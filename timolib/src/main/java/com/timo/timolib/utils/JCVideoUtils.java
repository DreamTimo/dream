package com.timo.timolib.utils;

import cn.jzvd.JZVideoPlayerStandard;

/**
 * 视频加载工具类
 */
public class JCVideoUtils {
    private JCVideoUtils() {
    }

    private static JCVideoUtils instance;

    public static JCVideoUtils getInstance() {
        if (instance == null) {
            instance = new JCVideoUtils();
        }
        return instance;
    }

    public void JCVideoLoad(String url, JZVideoPlayerStandard JCViedo, String title) {
        JCViedo.setAllControlsVisiblity(0,0,0,0,0,0,0);
        JCViedo.setUp(url, JZVideoPlayerStandard.NORMAL_ORIENTATION, title);
        JCViedo.startVideo();
    }
}
