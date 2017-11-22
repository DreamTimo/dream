package com.timo.timolib;

/**
 * Created by 蔡永汪 on 2017/10/26.
 */

public interface BaseConfig {
    boolean log = true;//log日志开关
    boolean exit_to_back = false;//应用退出时是否进入后台
    int exit_time = 2000;//按返回键应用相应退出的时间-毫秒
    String log_tag = "timo";//log_tag日志标记
    int pageSize = 10;//分页--默认十条数据
    boolean bgService = true;//开启后台服务--默认开启
}
