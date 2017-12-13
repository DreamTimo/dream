package com.luyz.lyimlib;

import com.dlbase.util.DLScreenUtil;

/**
 * Created by luyz on 2017/4/19.
 */

public class LYIMConfig {
    public static final String KHost = "118.26.142.133";
    public static final int KPort = 2000;
//    public static final String KHost = "112.124.119.104:2000";//测试服务器
    //public static final int KPort = 8081;//测试端口号

    public static final String THUMBNAIL100 = "!/fw/100";
    public static final String THUMBNAIL300 = "!/fw/300";
    public static final String THUMBNAIL200 = "!/fw/200";
    public static final String THUMBNAILScreen = "!/fw/"+ DLScreenUtil.getScreenWidth(LyImEngine.getInstance().getmContext());
    public static final String THUMBNAILHalfScreen = "!/fw/"+ DLScreenUtil.getScreenWidth(LyImEngine.getInstance().getmContext())/2;
    public static final String THUMBNAILThreeScreen = "!/fw/"+ DLScreenUtil.getScreenWidth(LyImEngine.getInstance().getmContext())/3;
}
