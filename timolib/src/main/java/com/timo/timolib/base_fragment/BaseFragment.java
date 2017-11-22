package com.timo.timolib.base_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.timo.timolib.BaseTools;
import com.timo.timolib.R;
import com.timo.timolib.base.BaseConstancts;
import com.timo.timolib.Params;
import com.timo.timolib.http.MyHttpParams;
import com.timo.timolib.view.TitleBar;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by lykj on 2017/9/10.
 */

public  abstract  class BaseFragment extends Fragment {

    private Params setParams;
    private Params getParams;
    private TitleBar baseTitleBar;
    private Unbinder unbinder;

    public void startActivity(Class<?> cls) {
        if (setParams == null) {
            startActivity(new Intent(getActivity(), cls));
        } else {
            startActivity(new Intent(getActivity(), cls).putExtra(BaseConstancts.BASE_PARAM, setParams));
        }
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getContentResId(),container,false);
        unbinder = ButterKnife.bind(this,view);
        setTitle(view);
        initEvent(view);
        return view;

    }
    protected void setTitle(View view) {
        if (view.findViewById(R.id.title) != null && BaseTools.isNotEmpty(setTitleName())) {
            baseTitleBar = (TitleBar) view.findViewById(R.id.title);
            BaseTools.setTitleBar(baseTitleBar, setTitleName());
        }
    }

    protected abstract String setTitleName();

    protected abstract int getContentResId();
    protected abstract void initEvent(View view);
    public Params setParams(){
        if (setParams == null){
            setParams = new Params();
        }
        return setParams;
    }
    public Params getParams(){
        try {
            if (getArguments()!=null && getArguments().getSerializable(BaseConstancts.BASE_PARAM)!=null){
                getParams =(Params)getArguments().getSerializable(BaseConstancts.BASE_PARAM);
            }
            if (getParams == null){
                getParams =  new Params();
            }
        }catch (Exception e){
            e.printStackTrace();

        }
        return getParams;
    }
    public MyHttpParams getHttpParams() {
        return new MyHttpParams();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder!=null){
            unbinder.unbind();
        }
    }
}
