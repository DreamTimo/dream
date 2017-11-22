package com.timo.timolib.base;

import java.util.ArrayList;
import java.util.WeakHashMap;

/**
 * AppManager: 应用管理/消息传递
 * --实现AppManager.AppListener接口,加入到管理集合:AppManager.getInstance().addListener(this);
 * --重写updata方法处理接收到的消息.
 * --发送消息:AppManager.getInstance().update(photoPath, Constancts.image_update);
 */
public class AppManager {
    public interface AppListener {
        void update(Object obj, String tag);
    }

    private static AppManager manager;
    private WeakHashMap<AppListener, Void> mListeners = new WeakHashMap<>();
    private Object obj;
    private String tag;

    public static AppManager getInstance() {
        if (manager == null) {
            manager = new AppManager();
        }
        return manager;
    }

    private AppManager() {
        super();
    }

    public void update(Object object, String tag) {
        this.obj = object;
        this.tag = tag;
        update();
    }

    public void addListener(AppListener listener) {
        mListeners.put(listener, null);
    }

    public void removeListener(AppListener listener) {
        mListeners.remove(listener);
        listener = null;
    }

    private void update() {
        ArrayList<AppListener> allListeners = new ArrayList<>(
                mListeners.keySet());
        for (AppListener aListener : allListeners) {
            aListener.update(obj, tag);
        }
    }
}
