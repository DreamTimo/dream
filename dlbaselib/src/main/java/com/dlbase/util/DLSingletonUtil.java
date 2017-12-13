package com.dlbase.util;

/**
 * @author luyz
 * 单例
 * @param <T>
 */
public abstract class DLSingletonUtil<T> {
	
	private T instance;

    protected abstract T newInstance();

    public final T getInstance() {
        if (instance == null) {
            synchronized (DLSingletonUtil.class) {
                if (instance == null) {
                    instance = newInstance();
                }
            }
        }
        return instance;
    }
}
