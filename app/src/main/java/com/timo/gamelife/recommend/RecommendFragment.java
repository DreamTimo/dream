package com.timo.gamelife.recommend;


import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.timo.gamelife.BannerUtils;
import com.timo.gamelife.R;
import com.timo.gamelife.bean.HouseBean;
import com.timo.gamelife.mvp.MVPBaseFragment;
import com.timo.timolib.BaseTools;
import com.timo.timolib.view.CustomScrollView;
import com.timo.timolib.view.FloatTouchView;
import com.timo.timolib.view.banner.Banner;
import com.timo.timolib.view.banner.internal.BaseBannerAdapter;
import com.timo.timolib.view.ninegridview.ImageInfo;
import com.timo.timolib.view.ninegridview.NineGridView;
import com.timo.timolib.view.ninegridview.NineGridViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * MVPPlugin
 * 邮箱 784787081@qq.com
 */

public class RecommendFragment extends MVPBaseFragment<RecommendContract.View, RecommendPresenter> implements RecommendContract.View {

    @BindView(R.id.banner_recommend)
    Banner mBannerRecommend;
    @BindView(R.id.recommend_classfy_1)
    TextView mRecommendClassfy1;
    @BindView(R.id.recommend_classfy_2)
    TextView mRecommendClassfy2;
    @BindView(R.id.recommend_classfy_3)
    TextView mRecommendClassfy3;
    @BindView(R.id.recommend_ll_1)
    LinearLayout mRecommendLl1;
    @BindView(R.id.recommend_1_more)
    TextView mRecommend1More;
    @BindView(R.id.recommend_ll_2)
    LinearLayout mRecommendLl2;
    @BindView(R.id.recommend_2_more)
    TextView mRecommend2More;
    @BindView(R.id.recommend_ll_3)
    LinearLayout mRecommendLl3;
    @BindView(R.id.recommend_3_more)
    TextView mRecommend3More;
    @BindView(R.id.home_root)
    CustomScrollView mHomeRoot;
    @BindView(R.id.recommend_search)
    TextView mRecommendSearch;
    @BindView(R.id.recommend_touch_view)
    FloatTouchView mRecommendTouchView;

    @Override
    protected String setTitleName() {
        return null;
    }

    @Override
    protected int getContentResId() {
        return R.layout.fragment_recommend;
    }

    @Override
    protected void initEvent(View view) {
        mBannerRecommend.setOnClickListener(new BaseBannerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(PagerAdapter parent, View view, int position, int realPosition) {
                BaseTools.showToast(position + "");
            }
        });
        mPresenter.setBanner();
        mPresenter.setLl1();
        mPresenter.setLl2();
        mPresenter.setLl3();
    }

    @OnClick({R.id.recommend_classfy_1, R.id.recommend_classfy_2, R.id.recommend_classfy_3, R.id.recommend_1_more, R.id.recommend_2_more, R.id.recommend_3_more, R.id.recommend_search, R.id.recommend_touch_view})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.recommend_classfy_1:
                BaseTools.showToast("1");
                break;
            case R.id.recommend_classfy_2:
                BaseTools.showToast("2");
                break;
            case R.id.recommend_classfy_3:
                BaseTools.showToast("3");
                break;
            case R.id.recommend_1_more:
                BaseTools.showToast("4");
                break;
            case R.id.recommend_2_more:
                BaseTools.showToast("5");
                break;
            case R.id.recommend_3_more:
                BaseTools.showToast("6");
                break;
            case R.id.recommend_search:
                BaseTools.showToast("8");
                break;
            case R.id.recommend_touch_view:
                BaseTools.showToast("9");
                break;
        }
    }

    @Override
    public void setBanner(List<String> bannerData) {
        BannerUtils.getInstance().setHouseProfiltBanner(getActivity(), mBannerRecommend, bannerData);
    }

    @Override
    public void setLl1(List<HouseBean> data) {
        for (int i = 0; i < data.size(); i++) {
            View view = BaseTools.getView(getContext(), R.layout.recommend_item_1, mHomeRoot);
            mRecommendLl1.addView(view);
        }
    }

    @Override
    public void setLl2(List<HouseBean> data) {
        for (int i = 0; i < data.size(); i++) {
            View view = BaseTools.getView(getContext(), R.layout.recommend_item_2, mHomeRoot);
            mRecommendLl2.addView(view);
        }
    }

    private NineGridView mNineGridView;

    @Override
    public void setLl3(List<HouseBean> data) {
        for (int i = 0; i < data.size(); i++) {
            View view = BaseTools.getView(getContext(), R.layout.recommend_item_3, mHomeRoot);
            mNineGridView = (NineGridView) view.findViewById(R.id.nine_gridview);
            ArrayList<ImageInfo> imageInfo = new ArrayList<>();
            imageInfo.add(new ImageInfo());
            imageInfo.add(new ImageInfo());
            imageInfo.add(new ImageInfo());
            mNineGridView.setAdapter(new NineGridViewAdapter(getContext(), imageInfo) {
            });
            mRecommendLl3.addView(view);
        }
    }
}
