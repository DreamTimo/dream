package com.timo.timolib.tools.utils;

import android.content.Context;
import android.content.Intent;

import com.timo.timolib.Timo_BaseConstancts;
import com.timo.timolib.Timo_Application;
import com.timo.timolib.Timo_Params;

/**
 * Created by 蔡永汪 on 2017/8/24.
 */

public class BaseUtils {
    private Timo_Params setParams;

    public Timo_Params setParams() {
        if (setParams == null) {
            setParams = new Timo_Params();
        }
        return setParams;
    }

    public void startActivity(Context context, Class<?> cls) {
        if (setParams == null) {
            context.startActivity(new Intent(Timo_Application.getInstance().getContext(), cls).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        } else {
            context.startActivity(new Intent(Timo_Application.getInstance().getContext(), cls).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra(Timo_BaseConstancts.BASE_PARAM, setParams));
        }
    }
}
