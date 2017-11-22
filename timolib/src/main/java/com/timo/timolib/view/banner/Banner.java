package com.timo.timolib.view.banner;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.timo.timolib.R;
import com.timo.timolib.view.banner.internal.BannerAdapter;
import com.timo.timolib.view.banner.internal.BaseBanner;
import com.timo.timolib.view.banner.internal.BaseBannerAdapter;

/**
 * 版权所有：XXX有限公司
 *
 * AdLoopView
 *
 * @author zhou.wenkai ,Created on 2015-1-14 19:30:18
 * Major Function：<b>自定义控件可以自动跳动的ViewPager</b>
 *
 * 注:如果您修改了本类请填写以下内容作为记录，如非本人操作劳烦通知，谢谢！！！
 * @author mender，Modified Date Modify Content:
 */
public class Banner extends BaseBanner {

    public Banner(Context context) {
        this(context, null);
    }

    public Banner(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public Banner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * Set the custom layout to be inflated for the loop views.
     *
     * @param layoutResId Layout id to be inflated
     */
    public void setLoopLayout(int layoutResId) {
        mLoopLayoutId = layoutResId;
    }

    @Override
    @SuppressWarnings("deprecation")
    protected void initRealView() {
        View view = null;

        if (mLoopLayoutId != 0) {
            // If there is a custom loop view layout id set, try and inflate it
            view = LayoutInflater.from(getContext()).inflate(mLoopLayoutId, null);
            // ViewPager
            mViewPager = (ViewPager) view.findViewById(R.id.loop_view_pager);
            // 指示点父控件
            dotsView = (LinearLayout) view.findViewById(R.id.loop_view_dots);
     
        }

        if(view == null) {
            view = createDefaultView();
        }

       
        this.addView(view);
    }
    public void setScrollDuration(long time){
    	 super.setScrollDuration(time);	// 设置页面切换时间
    }
    private View createDefaultView() {
        RelativeLayout contentView = new RelativeLayout(getContext());
        int viewWidth = ViewGroup.LayoutParams.MATCH_PARENT;
        int viewHeight = ViewGroup.LayoutParams.MATCH_PARENT;
        ViewGroup.LayoutParams viewParams = new ViewGroup.LayoutParams(viewWidth, viewHeight);
        contentView.setLayoutParams(viewParams);
        //初始化ViewPager
        mViewPager = new ViewPager(getContext());
        mViewPager.setId(R.id.loop_view_pager);
        int viewPagerWidth = RelativeLayout.LayoutParams.MATCH_PARENT;
        int viewPagerHeight = RelativeLayout.LayoutParams.MATCH_PARENT;
        RelativeLayout.LayoutParams viewPagerParams = new RelativeLayout.LayoutParams(viewPagerWidth, viewPagerHeight);
        this.addView(mViewPager, viewPagerParams);
        //初始化下方指示条
        RelativeLayout bottomLayout = new RelativeLayout(getContext());
        int bottomLayoutWidth =  RelativeLayout.LayoutParams.MATCH_PARENT;
        int bottomLayoutHeight =  RelativeLayout.LayoutParams.WRAP_CONTENT;
        RelativeLayout.LayoutParams bottomLayoutParams = new RelativeLayout.LayoutParams(bottomLayoutWidth, bottomLayoutHeight);
        bottomLayoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, mViewPager.getId());
//        Drawable mBackground = new ColorDrawable(Color.DKGRAY);   //指示器条目背景
//        mBackground.setAlpha((int) (0.3 * 255));
//        bottomLayout.setBackgroundDrawable(mBackground);
        bottomLayout.setGravity(Gravity.CENTER_VERTICAL);
        this.addView(bottomLayout, bottomLayoutParams);
        //初始化指示点父控件
        dotsView = new LinearLayout(getContext());
        dotsView.setId(R.id.loop_view_dots);
        int dotsViewWidth = RelativeLayout.LayoutParams.WRAP_CONTENT;
        int dotsViewHeight = RelativeLayout.LayoutParams.WRAP_CONTENT;
        RelativeLayout.LayoutParams dotsViewParams = new RelativeLayout.LayoutParams(dotsViewWidth, dotsViewHeight);
        dotsView.setOrientation(LinearLayout.HORIZONTAL);
        dotsViewParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        dotsViewParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        dotsViewParams.setMargins(0, 0, 0, 30);
        bottomLayout.addView(dotsView, dotsViewParams);

        int descTextWidth = RelativeLayout.LayoutParams.MATCH_PARENT;
        int descTextHeight = RelativeLayout.LayoutParams.WRAP_CONTENT;
        RelativeLayout.LayoutParams descTextParams = new RelativeLayout.LayoutParams(descTextWidth, descTextHeight);
        descTextParams.addRule(RelativeLayout.LEFT_OF, dotsView.getId());
        return contentView;
    }

    @Override
    protected BaseBannerAdapter initAdapter() {
        return new BannerAdapter(getContext(), mLoopData, mViewPager);
    }

    /** 初始化指示点 */
    @Override
    protected void initDots(int size) {
        if(null != dotsView) {
            dotsView.removeAllViews();
            for(int i=0; i<size; i++){
                ImageView dot = new ImageView(getContext());
                dot.setBackgroundResource(mDotSelector);
                int dotWidth = LinearLayout.LayoutParams.WRAP_CONTENT;
                int dotHeight = LinearLayout.LayoutParams.WRAP_CONTENT;
                LinearLayout.LayoutParams dotParams = new LinearLayout.LayoutParams(dotWidth, dotHeight);
                dotParams.setMargins(0, (int)mDotMargin, (int)mDotMargin, (int)mDotMargin);
                if(i == 0){
                    dot.setEnabled(true);
                }else{
                    dot.setEnabled(false);
                }
                dotsView.addView(dot, dotParams);
            }
        }
    }

    @Override
    protected void setOnPageChangeListener() {
        // 数据适配器滑动监听
        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                int i = position % mLoopData.getDatas().size();
                if (mOnBannerChangeListener!=null){
                    mOnBannerChangeListener.onBannerChange(i);
                }
                if(null != dotsView) {
                    dotsView.getChildAt(i).setEnabled(true);
                }
                if(null != dotsView && currentPosition != -1) {
                    dotsView.getChildAt(currentPosition).setEnabled(false);
                }
                currentPosition = i;
               

                // 跳转到头部尾部的监听回调
                if(mOnLoopListener != null) {
                    if(i == 0) {
                        mOnLoopListener.onLoopToStart(position);
                    } else if(i == mLoopData.getDatas().size() -1) {
                        mOnLoopListener.onLoopToEnd(position);
                    }
                }
            }
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {}
            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }
}