package com.timo.timolib.http;

import android.app.Dialog;
import android.content.Context;

import com.google.gson.Gson;
import com.timo.timolib.BaseTools;
import com.timo.timolib.tools.utils.DialogUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import okhttp3.Call;

/**
 * Created by 蔡永汪 on 2017/8/17.
 */

public class Http {
    private Http() {
    }

    private static Gson mGson = new Gson();
    private static Dialog loadingDialog;
    private static boolean isShowing;

    public static void getData(Context context, String url, Class clazz, HttpAllListener listener) {
        getData(context, url, null, clazz, listener);
    }

    public static void getData(Context context, String url, Object object, final Class clazz, final HttpAllListener listener) {
        if (!isShowing) {
            loadingDialog = DialogUtils.getInstance().getLoadingDialog(context);
            loadingDialog.show();
            isShowing = true;
        }
        try {
            if (object == null) {
                OkHttpUtils.get().url(url).build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (isShowing) {
                            loadingDialog.dismiss();
                            isShowing = false;
                        }
                        listener.error("联网失败");
                        BaseTools.printErrorMessage(e);
                        BaseTools.showToast("服务器连接错误，请重启下应用或稍后再尝试!");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (isShowing) {
                            loadingDialog.dismiss();
                            isShowing = false;
                        }
                        try {
                            ApiBase<Object> bean = (ApiBase<Object>) mGson.fromJson(response, clazz);
                            if (bean.getStatus() == 0) {
                                listener.data(mGson.fromJson(response, clazz));
                            } else {
                                listener.error(bean.getMsg());
                            }
                        } catch (Exception e) {
                            BaseTools.printErrorMessage(e);
                            BaseTools.showToast("网络数据出错了，请重启下应用或稍后再尝试!");
                        }
                    }
                });
            } else {
                HashMap<String, String> hashMap = objToHash(object);
                OkHttpUtils.get().params(hashMap).url(url).build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (isShowing) {
                            loadingDialog.dismiss();
                            isShowing = false;
                        }
                        listener.error("联网失败");
                        BaseTools.printErrorMessage(e);
                        BaseTools.showToast("服务器连接错误，请重启下应用或稍后再尝试!");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (isShowing) {
                            loadingDialog.dismiss();
                            isShowing = false;
                        }
                        try {
                            ApiBase<Object> bean = (ApiBase<Object>) mGson.fromJson(response, clazz);
                            if (bean.getStatus() == 0) {
                                listener.data(mGson.fromJson(response, clazz));
                            } else {
                                listener.error(bean.getMsg());
                            }
                        } catch (Exception e) {
                            BaseTools.printErrorMessage(e);
                            BaseTools.showToast("网络数据出错了，请重启下应用或稍后再尝试!");
                        }
                    }
                });
            }
        } catch (Exception e) {
            if (isShowing) {
                loadingDialog.dismiss();
                isShowing = false;
            }
            BaseTools.showToast("网络数据出错了，请重启下应用或稍后再尝试!");
            BaseTools.printErrorMessage(e);
        }
    }

    /**
     * 基类获取数据的总方法
     */
    public static void baseGetData(String url, Object object, final Class clazz, final HttpAllListener listener) {
        try {
            if (object == null) {
                OkHttpUtils.get().url(url).build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        listener.error("联网失败");
                        BaseTools.printErrorMessage(e);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            ApiBase<Object> bean = (ApiBase<Object>) mGson.fromJson(response, clazz);
                            if (bean.getStatus() == 0) {
                                listener.data(mGson.fromJson(response, clazz));
                            } else {
                                listener.error(bean.getMsg());
                            }
                        } catch (Exception e) {
                            BaseTools.printErrorMessage(e);
                        }
                    }
                });
            } else {
                HashMap<String, String> hashMap = objToHash(object);
                OkHttpUtils.get().params(hashMap).url(url).build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        listener.error("联网失败");
                        BaseTools.printErrorMessage(e);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            ApiBase<Object> bean = (ApiBase<Object>) mGson.fromJson(response, clazz);
                            if (bean.getStatus() == 0) {
                                listener.data(mGson.fromJson(response, clazz));
                            } else {
                                listener.error(bean.getMsg());
                            }
                        } catch (Exception e) {
                            BaseTools.printErrorMessage(e);
                        }
                    }
                });
            }
        } catch (Exception e) {
            BaseTools.printErrorMessage(e);
        }
    }

    public void getData(Context context, String url, Class clazz, HttpListener listener) {
        getData(context, url, null, clazz, listener);
    }

    public static void getData(Context context, String url, Object object, final Class clazz, final HttpListener listener) {
        if (!isShowing) {
            loadingDialog = DialogUtils.getInstance().getLoadingDialog(context);
            loadingDialog.show();
            isShowing = true;
        }
        try {
            if (object == null) {
                OkHttpUtils.get().url(url).build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (isShowing) {
                            loadingDialog.dismiss();
                            isShowing = false;
                        }
                        BaseTools.printErrorMessage(e);
                        BaseTools.showToast("服务器连接错误，请重启下应用或稍后再尝试!");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (isShowing) {
                            loadingDialog.dismiss();
                            isShowing = false;
                        }
                        try {
                            ApiBase<Object> bean = (ApiBase<Object>) mGson.fromJson(response, clazz);
                            if (bean.getStatus() == 0) {
                                listener.data(mGson.fromJson(response, clazz));
                            } else {
                                if (BaseTools.isNotEmpty(bean.getMsg())) {
                                    BaseTools.showToast(bean.getMsg());
                                }
                            }
                        } catch (Exception e) {
                            BaseTools.printErrorMessage(e);
                            BaseTools.showToast("网络数据出错了，请重启下应用或稍后再尝试!");
                        }
                    }
                });
            } else {
                HashMap<String, String> hashMap = objToHash(object);
                OkHttpUtils.get().params(hashMap).url(url).build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (isShowing) {
                            loadingDialog.dismiss();
                            isShowing = false;
                        }
                        BaseTools.printErrorMessage(e);
                        BaseTools.showToast("服务器连接错误，请重启下应用或稍后再尝试!");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (isShowing) {
                            loadingDialog.dismiss();
                            isShowing = false;
                        }
                        try {
                            ApiBase<Object> bean = (ApiBase<Object>) mGson.fromJson(response, clazz);
                            if (bean.getStatus() == 0) {
                                listener.data(mGson.fromJson(response, clazz));
                            } else {
                                if (BaseTools.isNotEmpty(bean.getMsg())) {
                                    BaseTools.showToast(bean.getMsg());
                                }
                            }
                        } catch (Exception e) {
                            BaseTools.printErrorMessage(e);
                            BaseTools.showToast("网络数据出错了，请重启下应用或稍后再尝试!");
                        }
                    }
                });
            }
        } catch (Exception e) {
            if (isShowing) {
                loadingDialog.dismiss();
                isShowing = false;
            }
            BaseTools.showToast("网络数据出错了，请重启下应用或稍后再尝试!");
            BaseTools.printErrorMessage(e);
        }
    }

    public static void post(Context context, String url, Class clazz, HttpAllListener listener) {
        post(context, url, null, clazz, listener);
    }

    public static void post(Context context, String url, Object object, final Class clazz, final HttpAllListener listener) {
        if (!isShowing) {
            loadingDialog = DialogUtils.getInstance().getLoadingDialog(context);
            loadingDialog.show();
            isShowing = true;
        }
        try {
            if (object == null) {
                OkHttpUtils.post().url(url).build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (isShowing) {
                            loadingDialog.dismiss();
                            isShowing = false;
                        }
                        BaseTools.printErrorMessage(e);
                        BaseTools.showToast("服务器连接错误，请重启下应用或稍后再尝试!");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (isShowing) {
                            loadingDialog.dismiss();
                            isShowing = false;
                        }
                        try {
                            ApiBase<Object> bean = (ApiBase<Object>) mGson.fromJson(response, clazz);
                            if (bean.getStatus() == 0) {
                                listener.data(mGson.fromJson(response, clazz));
                            } else {
                                listener.error(bean.getMsg());
                            }
                        } catch (Exception e) {
                            BaseTools.printErrorMessage(e);
                            BaseTools.showToast("网络数据出错了，请重启下应用或稍后再尝试!");
                        }
                    }
                });
            } else {
                HashMap<String, String> hashMap = objToHash(object);
                OkHttpUtils.post().params(hashMap).url(url).build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (isShowing) {
                            loadingDialog.dismiss();
                            isShowing = false;
                        }
                        BaseTools.printErrorMessage(e);
                        BaseTools.showToast("服务器连接错误，请重启下应用或稍后再尝试!");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (isShowing) {
                            loadingDialog.dismiss();
                            isShowing = false;
                        }
                        try {
                            ApiBase<Object> bean = (ApiBase<Object>) mGson.fromJson(response, clazz);
                            if (bean.getStatus() == 0) {
                                listener.data(mGson.fromJson(response, clazz));
                            } else {
                                listener.error(bean.getMsg());
                            }
                        } catch (Exception e) {
                            BaseTools.printErrorMessage(e);
                            BaseTools.showToast("网络数据出错了，请重启下应用或稍后再尝试!");
                        }
                    }
                });
            }
        } catch (Exception e) {
            if (isShowing) {
                loadingDialog.dismiss();
                isShowing = false;
            }
            BaseTools.showToast("网络数据出错了，请重启下应用或稍后再尝试!");
            BaseTools.printErrorMessage(e);
        }
    }

    public static void post(Context context, String url, Object object, final Class clazz, final HttpListener listener) {
        if (!isShowing) {
            loadingDialog = DialogUtils.getInstance().getLoadingDialog(context);
            loadingDialog.show();
            isShowing = true;
        }
        try {
            if (object == null) {
                OkHttpUtils.post().url(url).build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (isShowing) {
                            loadingDialog.dismiss();
                            isShowing = false;
                        }
                        BaseTools.printErrorMessage(e);
                        BaseTools.showToast("服务器连接错误，请重启下应用或稍后再尝试!");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (isShowing) {
                            loadingDialog.dismiss();
                            isShowing = false;
                        }
                        try {
                            ApiBase<Object> bean = (ApiBase<Object>) mGson.fromJson(response, clazz);
                            if (bean.getStatus() == 0) {
                                listener.data(mGson.fromJson(response, clazz));
                            } else {
                                BaseTools.showToast(bean.getMsg());
                            }
                        } catch (Exception e) {
                            BaseTools.printErrorMessage(e);
                            BaseTools.showToast("网络数据出错了，请重启下应用或稍后再尝试!");
                        }
                    }
                });
            } else {
                HashMap<String, String> hashMap = objToHash(object);
                OkHttpUtils.post().params(hashMap).url(url).build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (isShowing) {
                            loadingDialog.dismiss();
                            isShowing = false;
                        }
                        BaseTools.printErrorMessage(e);
                        BaseTools.showToast("服务器连接错误，请重启下应用或稍后再尝试!");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (isShowing) {
                            loadingDialog.dismiss();
                            isShowing = false;
                        }
                        try {
                            ApiBase<Object> bean = (ApiBase<Object>) mGson.fromJson(response, clazz);
                            if (bean.getStatus() == 0) {
                                listener.data(mGson.fromJson(response, clazz));
                            } else {
                                BaseTools.showToast(bean.getMsg());
                            }
                        } catch (Exception e) {
                            BaseTools.printErrorMessage(e);
                            BaseTools.showToast("网络数据出错了，请重启下应用或稍后再尝试!");
                        }
                    }
                });
            }
        } catch (Exception e) {
            if (isShowing) {
                loadingDialog.dismiss();
                isShowing = false;
            }
            BaseTools.showToast("网络数据出错了，请重启下应用或稍后再尝试!");
            BaseTools.printErrorMessage(e);
        }
    }

    public static void postFile(Context context, String url, Object object, String fileName, String filePath, final HttpAllListener listener) {
        if (!isShowing) {
            loadingDialog = DialogUtils.getInstance().getLoadingDialog(context);
            loadingDialog.show();
            isShowing = true;
        }
        try {
            if (object == null) {
                OkHttpUtils.post()
                        .addFile(fileName, fileName + ".jpg", new File(filePath))
                        .url(url)
                        .build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        BaseTools.showToast("文件上传失败");
                        if (isShowing) {
                            loadingDialog.dismiss();
                            isShowing = false;
                        }
                        BaseTools.printErrorMessage(e);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        BaseTools.showToast("上传成功");
                        if (isShowing) {
                            loadingDialog.dismiss();
                            isShowing = false;
                        }
                        try {
                            ApiBase<Object> bean = mGson.fromJson(response, ApiObject.class);
                            if (bean.getStatus() == 0) {
                                listener.data(mGson.fromJson(response, ApiObject.class));
                            } else {
                                listener.error(bean.getMsg());
                            }
                        } catch (Exception e) {
                            BaseTools.printErrorMessage(e);
                            BaseTools.showToast("网络数据出错了，请重启下应用或稍后再尝试!");
                        }
                    }
                });
            } else {
                HashMap<String, String> hashMap = objToHash(object);
                OkHttpUtils.post()
                        .addFile(fileName, fileName + ".jpg", new File(filePath))
                        .url(url)
                        .params(hashMap)
                        .build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        BaseTools.showToast("图片上传失败");
                        if (isShowing) {
                            loadingDialog.dismiss();
                            isShowing = false;
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        BaseTools.showToast("提交成功");
                        if (isShowing) {
                            loadingDialog.dismiss();
                            isShowing = false;
                        }
                        try {
                            ApiBase<Object> bean = mGson.fromJson(response, ApiObject.class);
                            if (bean.getStatus() == 0) {
                                listener.data(mGson.fromJson(response, ApiObject.class));
                            } else {
                                listener.error(bean.getMsg());
                            }
                        } catch (Exception e) {
                            BaseTools.printErrorMessage(e);
                            BaseTools.showToast("网络数据出错了，请重启下应用或稍后再尝试!");
                        }
                    }
                });
            }
        } catch (Exception e) {
            if (isShowing) {
                loadingDialog.dismiss();
                isShowing = false;
            }
            BaseTools.showToast("网络数据出错了，请重启下应用或稍后再尝试!");
            BaseTools.printErrorMessage(e);
        }
    }

    public static void postFiles(Context context, String url, Object object, List<FileBean> files, final HttpAllListener listener) {
        if (!isShowing) {
            loadingDialog = DialogUtils.getInstance().getLoadingDialog(context);
            loadingDialog.show();
            isShowing = true;
        }
        try {
            if (object == null) {
                PostFormBuilder post = OkHttpUtils.post();
                for (int i = 0; i < files.size(); i++) {
                    post.addFile(files.get(i).getFileName(), files.get(i).getFileName() + ".jpg", new File(files.get(i).getFilePath()));
                }
                post.url(url)
                        .build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        BaseTools.showToast("文件上传失败");
                        if (isShowing) {
                            loadingDialog.dismiss();
                            isShowing = false;
                        }
                        BaseTools.printErrorMessage(e);
                    }

                    @Override
                    public void onResponse(String response, int id) {

                        if (isShowing) {
                            loadingDialog.dismiss();
                            isShowing = false;
                        }
                        try {
                            ApiBase<Object> bean = mGson.fromJson(response, ApiObject.class);
                            if (bean.getStatus() == 0) {
                                listener.data(mGson.fromJson(response, ApiObject.class));
                                BaseTools.showToast("上传成功");
                            } else {
                                if (BaseTools.isNotEmpty(bean.getMsg())) {
                                    BaseTools.showToast(bean.getMsg());
                                }
                            }
                        } catch (Exception e) {
                            BaseTools.printErrorMessage(e);
                            BaseTools.showToast("网络数据出错了，请重启下应用或稍后再尝试!");
                        }
                    }
                });
            } else {
                HashMap<String, String> hashMap = objToHash(object);
                PostFormBuilder post = OkHttpUtils.post();
                for (int i = 0; i < files.size(); i++) {
                    post.addFile(files.get(i).getFileName(), files.get(i).getFileName() + ".jpg", new File(files.get(i).getFilePath()));
                }
                post
                        .url(url)
                        .params(hashMap)
                        .build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        BaseTools.showToast("图片上传失败");
                        if (isShowing) {
                            loadingDialog.dismiss();
                            isShowing = false;
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (isShowing) {
                            loadingDialog.dismiss();
                            isShowing = false;
                        }
                        try {
                            ApiBase<Object> bean = mGson.fromJson(response, ApiObject.class);
                            if (bean.getStatus() == 0) {
                                listener.data(mGson.fromJson(response, ApiObject.class));
                                BaseTools.showToast("上传成功");
                            } else {
                                if (BaseTools.isNotEmpty(bean.getMsg())) {
                                    BaseTools.showToast(bean.getMsg());
                                }
                            }
                        } catch (Exception e) {
                            BaseTools.printErrorMessage(e);
                            BaseTools.showToast("网络数据出错了，请重启下应用或稍后再尝试!");
                        }
                    }
                });
            }
        } catch (Exception e) {
            if (isShowing) {
                loadingDialog.dismiss();
                isShowing = false;
            }
            BaseTools.showToast("网络数据出错了，请重启下应用或稍后再尝试!");
            BaseTools.printErrorMessage(e);
        }
    }

    public static void httpObject(Context context, String url, MyHttpParams params) {
        httpObject(context, url, params, null, null);
    }

    public static void httpObject(Context context, String url, MyHttpParams params, HttpObjectListener listener) {
        httpObject(context, url, params, null, listener);
    }

    public static void httpObject(Context context, String url, MyHttpParams params, final String description, final HttpObjectListener listener) {
        getData(context, url, params, ApiObject.class, new HttpAllListener<ApiObject>() {
            @Override
            public void data(ApiObject data) {
                if (BaseTools.isNotEmpty(description)) {
                    BaseTools.showToast(description);
                }
                if (listener != null) {
                    listener.data();
                }
            }

            @Override
            public void error(String error) {
                BaseTools.showToast(error);
            }
        });
    }

    public static void postObject(Context context, String url, MyHttpParams params) {
        postObject(context, url, params, null, null);
    }

    public static void postObject(Context context, String url, MyHttpParams params, HttpObjectListener listener) {
        postObject(context, url, params, null, listener);
    }

    public static void postObject(Context context, String url, MyHttpParams params, final String description, final HttpObjectListener listener) {
        post(context, url, params, ApiObject.class, new HttpAllListener<ApiObject>() {
            @Override
            public void data(ApiObject data) {
                if (BaseTools.isNotEmpty(description)) {
                    BaseTools.showToast(description);
                }
                if (listener != null) {
                    listener.data();
                }
            }

            @Override
            public void error(String error) {
                BaseTools.showToast(error);
            }
        });
    }

    @SuppressWarnings("rawtypes")
    public static HashMap<String, String> objToHash(Object obj) {

        LinkedHashMap<String, String> hashMap = new LinkedHashMap<String, String>();
        try {
            Class clazz = obj.getClass();
            List<Class> clazzs = new ArrayList<Class>();

            do {
                clazzs.add(clazz);
                clazz = clazz.getSuperclass();
            } while (!clazz.equals(Object.class));

            for (Class iClazz : clazzs) {
                Field[] fields = iClazz.getDeclaredFields();
                for (Field field : fields) {
                    String objVal = null;
                    field.setAccessible(true);
                    if (field.get(obj) != null) {
                        objVal = String.valueOf(field.get(obj));
                        if (BaseTools.isNotEmpty(objVal)) {
                            hashMap.put(field.getName(), objVal);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hashMap;
    }

    public static FileBean getFile() {
        return new FileBean();
    }

    public static List<FileBean> getFileList() {
        List<FileBean> list = new ArrayList<>();
        return list;
    }

    public static MyHttpParams getHttpParams() {
        return new MyHttpParams();
    }
}
