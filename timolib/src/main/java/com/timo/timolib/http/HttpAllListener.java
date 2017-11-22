package com.timo.timolib.http;

/**
 * Created by 蔡永汪 on 2017/8/17.
 */

public interface HttpAllListener<T> {
    void data(T data);
    void error(String error);
}
