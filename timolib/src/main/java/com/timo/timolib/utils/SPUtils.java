package com.timo.timolib.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.timo.timolib.BaseConstancts;
import com.timo.timolib.MyApplication;

import java.util.Map;

/**
 * SharedPreferences的一个工具类，调用setParam就能保存String, Integer, Boolean, Float, Long类型的参�?
 */
public class SPUtils {
    private SPUtils() {
    }

    private static SPUtils instance;

    public static SPUtils getInstance() {
        if (instance == null) {
            instance = new SPUtils();
        }
        return instance;
    }

    /**
     * 保存数据的方法，我们�?��拿到保存数据的具体类型，然后根据类型调用不同的保存方�?
     */
    public void save(String key, Object object) {

        String type = object.getClass().getSimpleName();
        SharedPreferences sp = MyApplication.getInstance().getContext().getSharedPreferences(BaseConstancts.BASE_SHARE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if ("String".equals(type)) {
            editor.putString(key, (String) object);
        } else if ("Integer".equals(type)) {
            editor.putInt(key, (Integer) object);
        } else if ("Boolean".equals(type)) {
            editor.putBoolean(key, (Boolean) object);
        } else if ("Float".equals(type)) {
            editor.putFloat(key, (Float) object);
        } else if ("Long".equals(type)) {
            editor.putLong(key, (Long) object);
        }
        editor.commit();
    }


    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取�?
     */
    public Object get(String key, Object defaultObject) {
        if (defaultObject != null) {
            String type = defaultObject.getClass().getSimpleName();
            SharedPreferences sp = MyApplication.getInstance().getContext().getSharedPreferences(BaseConstancts.BASE_SHARE_NAME, Context.MODE_PRIVATE);

            if ("String".equals(type)) {
                return sp.getString(key, (String) defaultObject);
            } else if ("Integer".equals(type)) {
                return sp.getInt(key, (Integer) defaultObject);
            } else if ("Boolean".equals(type)) {
                return sp.getBoolean(key, (Boolean) defaultObject);
            } else if ("Float".equals(type)) {
                return sp.getFloat(key, (Float) defaultObject);
            } else if ("Long".equals(type)) {
                return sp.getLong(key, (Long) defaultObject);
            }
        }
        return null;
    }

    public void destroyParam() {
        SharedPreferences sp = MyApplication.getInstance().getContext().getSharedPreferences(BaseConstancts.BASE_SHARE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }
    /**
     * 移除某个key值已经对应的值
     */
    public void remove(String key) {
        SharedPreferences sp = MyApplication.getInstance().getContext().getSharedPreferences(BaseConstancts.BASE_SHARE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.commit();
    }
    /**
     * 返回所有的键值对
     */
    public Map<String, ?> getAll() {
        SharedPreferences sp = MyApplication.getInstance().getContext().getSharedPreferences(BaseConstancts.BASE_SHARE_NAME,
                Context.MODE_PRIVATE);
        return sp.getAll();
    }
    /**
     * 查询某个key是否已经存在
     */
    public static boolean isExist(String key) {
        SharedPreferences sp = MyApplication.getInstance().getContext().getSharedPreferences(BaseConstancts.BASE_SHARE_NAME,
                Context.MODE_PRIVATE);
        return sp.contains(key);
    }

    /**
     * 清除所有数据
     *
     */
    public static void clearAll() {
        SharedPreferences sp = MyApplication.getInstance().getContext().getSharedPreferences(BaseConstancts.BASE_SHARE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }
}
