package com.timo.timolib.base.base_fragment;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.timo.timolib.BaseApplication;
import com.timo.timolib.BaseConstancts;
import com.timo.timolib.tools.permissions.PermissionUtils;

import java.io.Serializable;


/**
 * Created by lykj on 2017/9/10.
 */

public abstract class SuperFragment extends Fragment implements PermissionUtils.PermissionCallbacks {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getContentResId(), container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initEvent(view);
    }

    protected abstract int getContentResId();

    protected abstract void initEvent(View view);

    public void startActivityNoFinish(Class<?> cls) {
        startActivity(new Intent(BaseApplication.getInstance().getContext(), cls));
    }

    public void startActivityNoFinish(Class<?> cls, Serializable params) {
        startActivity(new Intent(BaseApplication.getInstance().getContext(), cls).putExtra(BaseConstancts.Params, params));
    }

    /**
     * 请求权限回调--不操作
     */
    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

}
