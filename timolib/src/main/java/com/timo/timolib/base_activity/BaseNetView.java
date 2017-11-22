package com.timo.timolib.base_activity;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import com.timo.timolib.R;
import com.timo.timolib.view.ProgressWheel;

/**
 * Created by chenmeng on 2016/7/4.
 */
public abstract class BaseNetView implements StateContract.StateView {

    private boolean bOnInterceptDisplay;
    private FrameLayout loaded_data_container;

    private ProgressWheel progressBar;
    private View loaded_error_view;
    private Context mContext;
    private StateContract.StatePresenter presenter;
    private FrameLayout mContainerView;

    public BaseNetView(Context mContext) {
        this.mContext = mContext;
        setRootView();
    }

    public void setRootView() {
        mContainerView = (FrameLayout) View.inflate(mContext, R.layout.fragment_network_base, null);
        loaded_data_container = (FrameLayout) mContainerView.findViewById(R.id.flyt_fragment_container);
        progressBar = (ProgressWheel) mContainerView.findViewById(R.id.progressBar);
        loaded_error_view = mContainerView.findViewById(R.id.ll_network_error);
        loaded_error_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.updateData();
            }
        });
        loaded_data_container.addView((getChildRootView()));
    }

    @Override
    public View getRootView() {
        return mContainerView;
    }

    @Override
    public void setOnInterceptDisplay(boolean OnInter) {
        bOnInterceptDisplay = OnInter;
    }

    @Override
    public void showLoadStatus(int state) {
        if (bOnInterceptDisplay) {
            return;
        }
        switch (state) {
            case NetworkLoadStatus.LOAD_IDEL:
            case NetworkLoadStatus.LOAD_START:
                progressBar.setVisibility(bOnInterceptDisplay ? View.GONE : View.VISIBLE);
                loaded_error_view.setVisibility(View.GONE);
                showAndHideContainer(bOnInterceptDisplay ? 1 : 0);
                break;
            case NetworkLoadStatus.LOAD_FAIL:
            case NetworkLoadStatus.LOAD_NETWORK_ERROR:
                showAndHideContainer(0);
                progressBar.setVisibility(View.GONE);
                loaded_error_view.setVisibility(View.VISIBLE);
                break;
            case NetworkLoadStatus.LOAD_FINISH:
                showAndHideContainer(1);
                progressBar.setVisibility(View.GONE);
                loaded_error_view.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    @Override
    public void setPresenter(StateContract.StatePresenter presenter) {
        this.presenter = presenter;
    }

    /**
     * 显示或者隐藏 container
     *
     * @param type 1显示
     */
    private void showAndHideContainer(int type) {// public_title
        View inner_container = loaded_data_container.findViewById(R.id.root);
        if (inner_container == null) {
            throw new RuntimeException("你没有在布局中添加要隐藏和现实的container主布局");
        }
        if (type == 1) {
            inner_container.setVisibility(View.VISIBLE);
        } else {
            inner_container.setVisibility(View.GONE);
        }
    }

    public static final class NetworkLoadStatus {

        public static final int LOAD_IDEL = 0;
        public static final int LOAD_START = 1;
        public static final int LOAD_FINISH = 2;
        public static final int LOAD_FAIL = 3;
        //网络异常
        public static final int LOAD_NETWORK_ERROR = 4;
    }
}
