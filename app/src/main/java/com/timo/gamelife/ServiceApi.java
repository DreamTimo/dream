package com.timo.gamelife;

import com.timo.gamelife.bean.ApiObj;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

/**
 * Author:Gaoyanan
 * Data:2018/6/6
 * Function:
 * project_name:HHS
 */
public interface ServiceApi {
    //显示通讯录的网络请求方式（正确）
    @Multipart
    @POST("/hhsapp/operateLinkman/addLinkman.action")
    Observable<ApiObj> showLinkman(@Part("sessionId") RequestBody description,
                                   @Part MultipartBody.Part file);
}