package com.timo.gamelife.fragment;

import android.view.View;

import com.timo.gamelife.R;
import com.timo.timolib.base.base_fragment.BaseFragment;

/**
 * Created by lykj on 2017/9/12.
 */

public class DreamFragment extends BaseFragment {

    @Override
    protected String setTitleName() {
        return getString(R.string.mian_dream);
    }

    @Override
    protected int getContentResId() {
        return R.layout.fragment_home;
    }

    String url = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1497688355699&di=ea69a930b82ce88561c635089995e124&imgtype=0&src=http%3A%2F%2Fcms-bucket.nosdn.127.net%2Ff84e566bcf654b3698363409fbd676ef20161119091503.jpg";

    @Override
    protected void initEvent(View view) {
    }

}
