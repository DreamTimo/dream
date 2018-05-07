package com.timo.gamelife.fragment;

import android.view.View;

import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestOptions;
import com.timo.gamelife.R;
import com.timo.timolib.base_fragment.BaseFragment;
import com.timo.timolib.glide.GlideImageView;
import com.timo.timolib.glide.progress.CircleProgressView;
import com.timo.timolib.glide.progress.OnGlideImageViewListener;
import com.timo.timolib.utils.GlideUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by lykj on 2017/9/12.
 */

public class DreamFragment extends BaseFragment {
    @BindView(R.id.image)
    GlideImageView mImage;
    @BindView(R.id.progressBar)
    CircleProgressView mProgressBar;

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
        RequestOptions requestOptions = mImage.requestOptions(R.color.blue).centerCrop();
        mImage.load(url, requestOptions).listener(new OnGlideImageViewListener() {
            @Override
            public void onProgress(int percent, boolean isDone, GlideException exception) {
                mProgressBar.setProgress(percent);
                mProgressBar.setVisibility(isDone ? View.GONE : View.VISIBLE);
            }
        });
        mImage.setCircle(true);
        mImage.setBorderWidth(5);
        mImage.setBorderColor(getResources().getColor(R.color.transparent100));
        GlideUtils.getInstance().load(url, mImage, R.drawable.bg_no_res, mProgressBar);
    }

}
