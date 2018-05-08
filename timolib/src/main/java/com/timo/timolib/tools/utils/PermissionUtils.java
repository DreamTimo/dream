package com.timo.timolib.tools.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.timo.timolib.BaseTools;
import com.timo.timolib.Timo_Application;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Apple on 2016/12/3.
 */

public final class PermissionUtils {
    private PermissionUtils() {
    }

    private static PermissionUtils instance;

    public int getPermissionRequestCode() {
        return permissionRequestCode;
    }

    public void setPermissionRequestCode(int permissionRequestCode) {
        this.permissionRequestCode = permissionRequestCode;
    }

    private int permissionRequestCode = 110;

    public int getRequestCodeCallPhone() {
        return requestCodeCallPhone;
    }

    public void setRequestCodeCallPhone(int requestCodeCallPhone) {
        this.requestCodeCallPhone = requestCodeCallPhone;
    }

    private int requestCodeCallPhone = 111;

    public static PermissionUtils getInstance() {
        if (instance == null) {
            instance = new PermissionUtils();
        }
        return instance;
    }

    //无底洞  狮驼  五庄  龙宫  化生

    //需要申请的权限
    private String[] permissions = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.RECORD_AUDIO
    };

    //检测权限
    public String[] checkPermission() {
        List<String> data = new ArrayList<>();//存储未申请的权限
        for (String permission : permissions) {
            int checkSelfPermission = ContextCompat.checkSelfPermission(Timo_Application.getInstance().getContext(), permission);
            if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {//未申请
                data.add(permission);
            }
        }
        return data.toArray(new String[data.size()]);
    }

    //访问位置权限
    private String[] locationPermission = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
    };

    //检测权限
    public String[] checkLocationPermission() {
        List<String> data = new ArrayList<>();//存储未申请的权限
        for (String permission : locationPermission) {
            int checkSelfPermission = ContextCompat.checkSelfPermission(Timo_Application.getInstance().getContext(), permission);
            if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {//未申请
                data.add(permission);
            }
        }
        return data.toArray(new String[data.size()]);
    }

    public void applyPermission(Activity activity) {
        String[] permissions = checkPermission();
        if (permissions.length != 0) {
            ActivityCompat.requestPermissions(activity, permissions, permissionRequestCode);
        }
    }

    //需要申请的权限
    private String[] callPhonePermissions = new String[]{
            Manifest.permission.CALL_PHONE
    };

    /**
     * 检测打电话权限
     */
    public String[] checkCallPhonePermission() {
        List<String> data = new ArrayList<>();//存储未申请的权限
        for (String permission : callPhonePermissions) {
            int checkSelfPermission = ContextCompat.checkSelfPermission(Timo_Application.getInstance().getContext(), permission);
            if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {//未申请
                data.add(permission);
            }
        }
        return data.toArray(new String[data.size()]);
    }

    /**
     * 申请打电话权限
     */
    public void applyCallPhonePermission(Activity activity) {
        String[] permissions = checkCallPhonePermission();
        if (permissions.length != 0) {
            ActivityCompat.requestPermissions(activity, permissions, requestCodeCallPhone);
        }
    }


    /**
     * 获取通知栏权限是否开启
     */
    private String CHECK_OP_NO_THROW = "checkOpNoThrow";
    private String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

    @SuppressLint("NewApi")
    public boolean isNotificationEnabled() {
        AppOpsManager mAppOps = (AppOpsManager) Timo_Application.getInstance().getContext().getSystemService(Context.APP_OPS_SERVICE);
        ApplicationInfo appInfo = Timo_Application.getInstance().getContext().getApplicationInfo();
        String pkg = Timo_Application.getInstance().getContext().getApplicationContext().getPackageName();
        int uid = appInfo.uid;

        Class appOpsClass = null;
      /* Context.APP_OPS_MANAGER */
        try {
            appOpsClass = Class.forName(AppOpsManager.class.getName());
            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE,
                    String.class);
            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);

            int value = (Integer) opPostNotificationValue.get(Integer.class);
            return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);

        } catch (ClassNotFoundException e) {
            BaseTools.printErrorMessage(e);
        } catch (NoSuchMethodException e) {
            BaseTools.printErrorMessage(e);
        } catch (NoSuchFieldException e) {
            BaseTools.printErrorMessage(e);
        } catch (InvocationTargetException e) {
            BaseTools.printErrorMessage(e);
        } catch (IllegalAccessException e) {
            BaseTools.printErrorMessage(e);
        }
        return false;
    }
}