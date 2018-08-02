package com.timo.gamelife;

import com.timo.gamelife.bean.ApiShowLinkman;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Author:Gaoyanan
 * Data:2018/6/6
 * Function:
 * project_name:HHS
 */

public interface ServiceApi {
    //显示通讯录的网络请求方式（正确）
    @FormUrlEncoded
    @POST("/hhsapp/operateLinkman/showLinkman.action")
    Observable<ApiShowLinkman> showLinkman(@Field("sessionId") String sessionId);
}
