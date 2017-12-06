package com.timo.timolib.utils;

import android.content.Context;
import android.content.Intent;

import com.timo.timolib.BaseConstancts;
import com.timo.timolib.MyApplication;
import com.timo.timolib.Params;

/**
 * Created by 蔡永汪 on 2017/8/24.
 */

public class BaseUtils {
    private Params setParams;

    public Params setParams() {
        if (setParams == null) {
            setParams = new Params();
        }
        return setParams;
    }

    public void startActivity(Context context, Class<?> cls) {
        if (setParams == null) {
            context.startActivity(new Intent(MyApplication.getInstance().getContext(), cls).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        } else {
            context.startActivity(new Intent(MyApplication.getInstance().getContext(), cls).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra(BaseConstancts.BASE_PARAM, setParams));
        }
    }
}
