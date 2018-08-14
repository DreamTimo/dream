package com.timo.gamelife.bean;

import com.timo.httplib.network.basebean.BaseBean;

/**
 * Created by 45590 on 2018/7/16.
 */

public class UserBean extends BaseBean {

    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
