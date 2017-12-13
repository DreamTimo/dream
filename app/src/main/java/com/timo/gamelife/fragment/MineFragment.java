package com.timo.gamelife.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.timo.gamelife.BannerUtils;
import com.timo.gamelife.R;
import com.timo.timolib.BaseTools;
import com.timo.timolib.base_fragment.BaseFragment;
import com.timo.timolib.view.banner.Banner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by lykj on 2017/9/12.
 */

public class MineFragment extends BaseFragment {

    @BindView(R.id.banner)
    Banner mBanner;
    List<String> data;
    @Override
    protected String setTitleName() {
        return "我的页面";
    }

    @Override
    protected int getContentResId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initEvent(View view) {
        data=new ArrayList<>();
        data.add("111111");
        data.add("111111");
        data.add("111111");
        data.add("111111");
        BannerUtils.getInstance().setHouseProfiltBanner(getActivity(),mBanner,data);
    }
}
