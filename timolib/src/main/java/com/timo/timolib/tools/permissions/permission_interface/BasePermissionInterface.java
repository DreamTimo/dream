package com.timo.timolib.tools.permissions.permission_interface;

/**
 * Created by 45590 on 2018/7/13.
 */

public interface BasePermissionInterface {
    /**
     * 基类方法定义接口
     */

    /**
     * 打开相机
     */
    void toOpenCamera();

    /**
     * 打电话
     *
     * @param phone
     */
    void toOpenCallPhone(String phone);

    /**
     * 发短信
     *
     * @param phone
     */
    void toOpenSendSms(String phone);

    /**
     * 打开外置sd卡权限
     */
    void toOpenStorage(PermissionGrantedListener deniedListener);

    void toOpenStorage();

    /**
     * 查看日历权限
     */
    void toOpenCalendar(PermissionGrantedListener deniedListener);

    void toOpenCalendar();

    /**
     * 打开联系人权限
     */
    void toOpenContacts(PermissionGrantedListener deniedListener);

    void toOpenContacts();

    /**
     * 打开获取位置权限
     */
    void toOpenLocation(PermissionGrantedListener deniedListener);

    void toOpenLocation();

    /**
     * 打开获取声音的权限
     */
    void toOpenAudio(PermissionGrantedListener deniedListener);

    void toOpenAudio();

    /**
     * 打开获取传感器权限
     */
    void toOpenSensors(PermissionGrantedListener deniedListener);

    void toOpenSensors();
}
