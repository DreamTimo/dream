package com.timo.gamelife.detail;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.timo.gamelife.R;
import com.timo.gamelife.mvp.MVPBaseActivity;
import com.timo.timolib.BaseTools;
import com.timo.timolib.view.SlidingTabLayout;
import com.timo.timolib.view.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * MVPPlugin
 * 邮箱 784787081@qq.com
 */

public class DetailActivity extends MVPBaseActivity<DetailContract.View, DetailPresenter> implements DetailContract.View {

    @BindView(R.id.title)
    TitleBar mTitle;
    @BindView(R.id.image)
    ImageView mImage;
    @BindView(R.id.introduction)
    TextView mIntroduction;
    @BindView(R.id.tag_price)
    TextView mTagPrice;
    @BindView(R.id.price)
    TextView mPrice;
    @BindView(R.id.slide_layout)
    SlidingTabLayout mSlideLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    @Override
    protected int getContentResId() {
        return R.layout.activity_detail;
    }

    @Override
    protected String setTitleName() {
        return "路线详情";
    }

    @Override
    protected void initEvent() {
        BaseTools.setTitleBar(mTitle, "线路详情", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTitle.addAction(new TitleBar.ImageAction(R.drawable.ic_back) {
            @Override
            public void performAction(View view) {
                BaseTools.showToast("收藏");
            }
        });
        mPresenter.setIndicator(getSupportFragmentManager());
    }

    @Override
    public void setIndicator(PagerAdapter pagerAdapter) {
        mViewPager.setAdapter(pagerAdapter);
        mSlideLayout.setViewPager(mViewPager);
    }
}
