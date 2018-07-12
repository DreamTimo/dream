package com.timo.timolib.tools.updata_app;

import android.support.annotation.NonNull;

import java.util.Map;


/**
 * Created by 蔡永汪 on 2017/11/9.
 */

public class UpdateAppHttpManager implements HttpManager {

    @Override
    public void asyncGet(@NonNull String url, @NonNull Map<String, String> params, @NonNull final Callback callBack) {

    }

    @Override
    public void asyncPost(@NonNull String url, @NonNull Map<String, String> params, @NonNull final Callback callBack) {
    }

    @Override
    public void download(@NonNull String url, @NonNull String path, @NonNull String fileName, @NonNull final FileCallback callback) {
    }
}
