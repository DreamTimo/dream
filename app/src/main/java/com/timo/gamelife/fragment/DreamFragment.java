package com.timo.gamelife.fragment;

import android.view.View;

import com.timo.gamelife.R;
import com.timo.timolib.base_fragment.BaseFragment;

/**
 * Created by lykj on 2017/9/12.
 */

public class DreamFragment extends BaseFragment {
    @Override
    protected String setTitleName() {
        return "梦想";
    }

    @Override
    protected int getContentResId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initEvent(View view) {
    }
}
