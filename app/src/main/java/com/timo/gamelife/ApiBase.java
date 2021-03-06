package com.timo.gamelife;

import com.timo.httplib.network.basebean.BaseBean;

public class ApiBase<T> extends BaseBean {
    private T returnData;
    private int status;
    private String Msg;

    public T getReturnData() {
        return returnData;
    }

    public void setReturnData(T returnData) {
        this.returnData = returnData;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String msg) {
        this.Msg = msg;
    }
}
