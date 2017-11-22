package com.timo.timolib.view.banner.internal;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.timo.timolib.BaseTools;
import com.timo.timolib.R;
import com.timo.timolib.view.banner.pagerstyle.BasePageTransformer;

/**
 * 版权所有：XXX有限公司
 *
 * BaseLoopView
 *
 * @author zhou.wenkai ,Created on 2015-1-14 19:30:18
 * @author mender，Modified Date Modify Content:
 * Major Function：<b>自定义控件可以自动跳动的ViewPager</b>
 *
 * 注:如果您修改了本类请填写以下内容作为记录，如非本人操作劳烦通知，谢谢！！！
 */
public abstract class BaseBanner extends RelativeLayout {

    private int paddingVertical=10;
    private int paddingHorizontial =30;
    private int pageMargin=3;

    /** ViewPager */
    public ViewPager mViewPager;
    /** 设置的自定义布局id */
    protected int mLoopLayoutId;
    /** ViewPager数据适配器 */
    private BaseBannerAdapter adapter;
    /** 底部指示点父控件 */
    protected LinearLayout dotsView;
  
    /** 指示点的位置 */
    protected int currentPosition = -1;
    /** 指示点距离 */
    protected float mDotMargin;
    /** 自动跳转的时间间隔 */
    protected int period;
    /** 指示点选择器 */
    protected int mDotSelector;
    /** 默认图片 */
    protected int mDefaultImgId;
    /** 是否自动跳转 */
    private boolean autoLoop = false;

    /** 触摸时是否停止自动跳转 */
    private boolean stopScrollWhenTouch = true;
    /** 当前状态是否是由于触摸而停止 */
    private boolean isStoppedByTouch = false;
    /** 当前状态是否是由于不可见而停止 */
    private boolean isStoppedByInvisible = false;
    /** 当前状态是否为自动跳转 */
    private boolean isAutoScroll = true;

    /** 自动跳转的方向为自右向左 */
    public static final int LEFT = 0;
    /** 自动跳转的方向为自左向右 */
    public static final int RIGHT = 1;
    /** 自动跳转方向,默认自左向右 */
    protected int direction = RIGHT;

    /** 数据实体对象 */
    protected BannerData mLoopData;

    private Handler mHandler;
    /** 条目点击的接口回调 */
    protected BaseBannerAdapter.OnItemClickListener mOnItemClickListener;
    /** 自动跳转状态的接口回调 */
    protected OnBannerListener mOnLoopListener;
    /** 轮播图跳转的接口回调 */
    protected OnBannerChangeListener mOnBannerChangeListener;
    /** 滑动控制器 */
    private BannerScroller mScroller;

    private float mDownX;
    private float mDownY;

    public BaseBanner(Context context) {
        this(context, null);
    }

    public BaseBanner(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public BaseBanner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        // 设置默认属性
        final float defaultDotMargin = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics());
        final int defaultInterval = 3000;

        // 设置样式属性
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LoopView);

        mDotMargin = a.getDimension(R.styleable.LoopView_loop_dotMargin, defaultDotMargin);
        period = a.getInt(R.styleable.LoopView_loop_interval, defaultInterval);
        autoLoop = a.getBoolean(R.styleable.LoopView_loop_autoLoop, false);
        mDotSelector = a.getResourceId(R.styleable.LoopView_loop_dotSelector, R.drawable.loop_view_dots_selector);
        mDefaultImgId = a.getResourceId(R.styleable.LoopView_loop_defaultImg, 0);
        mLoopLayoutId = a.getResourceId(R.styleable.LoopView_loop_layout, 0);

        a.recycle();

        initRealView();
    }

    /**
     * 设置监听
     */
    protected void setViewListener() {

        // 设置viewPager监听
        setOnPageChangeListener();

        // 数据适配器点击事件监听回调
        adapter.setOnItemClickListener(new BaseBannerAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(PagerAdapter parent, View view, int position, int realPosition) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(parent, view, position, realPosition);
                }
            }
        });

    }

    /**
     * 设置页面切换过渡时间
     *
     * @param duration
     */
    public void setScrollDuration(long duration) {
        mScroller = new BannerScroller(getContext());
        mScroller.setScrollDuration(duration);
        mScroller.initViewPagerScroll(mViewPager);
    }

    /**
     * 设置页面切换时间间隔
     */
    public void setPeriod(int period) {
        this.period = period;
    }

    /**
     * 获取页面切换时间间隔
     *
     * @return
     */
    public int getPeriod() {
        return period;
    }

    /**
     * 获取ViewPager
     *
     * @return
     */
    public ViewPager getViewPager() {
        return mViewPager;
    }

    /**
     * 获取封装数据
     *
     */
    public BannerData getLoopData() {
        return mLoopData;
    }

    /**
     * 获取当前指示位置
     *
     */
    public int getCurrentPosition() {
        return currentPosition;
    }

    /**
     * @param loopData 设置数据,默认轮播间隔3秒
     */
    public void setData(BannerData loopData) {
        if (null == loopData||loopData.getDatas().size()==0) return;
        stopAutoLoop();
        removeAllViews();
        initRealView();
        mLoopData = null;
        mLoopData = loopData;
        initLoopViewPager();
        invalidate();
        startAutoLoop();
        setScrollDuration(1000);
        setPeriod(3000);
    }
    /**
     * 设置数据
     * @param loopData  数据
     * @param period 轮播间隔/单位秒
     */
    public void setData(BannerData loopData, int period) {
        if (null == loopData||loopData.getDatas().size()==0) return;
        stopAutoLoop();
        removeAllViews();
        initRealView();
        mLoopData = null;
        mLoopData = loopData;
        initLoopViewPager();
        invalidate();
        startAutoLoop();
        setScrollDuration(1000);
        setPeriod(period*1000);

    }

    /**
     * 设置数据,默认轮播间隔3秒
     * 设置样式
     */
    public void setData(BannerData loopData, BasePageTransformer transformer) {
        if (null == loopData||transformer==null) return;
        stopAutoLoop();
        removeAllViews();
        initRealView();
        mLoopData = null;
        mLoopData = loopData;
        initLoopViewPager(transformer);
        invalidate();
        startAutoLoop();
        setScrollDuration(1000);
        setPeriod(3000);
    }

    /**
     * 普通样式的轮播图
     */
    private void initLoopViewPager() {
        adapter = initAdapter();
        adapter.setDefaultImgId(mDefaultImgId);
        mViewPager.setAdapter(adapter);
        initDots(mLoopData.getDatas().size());                     // 初始化指示点
        setViewListener();                                    // 初始化点击监听事件
        int startPosition = Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % mLoopData.getDatas().size();
        mViewPager.setCurrentItem(startPosition, false);      // 设置当前显示的位置
        if (mHandler == null) {
            mHandler = new BannerHandler(this, (Activity)getContext());
        }
        if (autoLoop) {
            startAutoLoop();
        }
    }

    /**
     * @param paddingVertical 设置上下距离
     */
    public void setPaddingVertical(int paddingVertical){
        this.paddingVertical =paddingVertical;
    }
    /**
     * @param paddingHorizontial 设置左右距离
     */
    public void setPaddingHorizontial(int paddingHorizontial){
        this.paddingHorizontial =paddingHorizontial;
    }
    /**
     * @param pageMargin 设置页面距离
     */
    public void setPageMargin(int pageMargin){
        this.pageMargin =pageMargin;
    }
    /**
     * 设置背景图/缩略图
     */
    private void initLoopViewPager(BasePageTransformer transformer) {
        this.setClipChildren(false);
        adapter = initAdapter();
        adapter.setDefaultImgId(mDefaultImgId);
        mViewPager.setAdapter(adapter);
        initDots(mLoopData.getDatas().size());                     // 初始化指示点
        setViewListener();                                    // 初始化点击监听事件
        int startPosition = Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % mLoopData.getDatas().size();
        mViewPager.setCurrentItem(startPosition, false);      // 设置当前显示的位置
        mViewPager.setClipChildren(false);
        mViewPager.setPageTransformer(true,transformer);
        LayoutParams params =new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins(BaseTools.dp2px(paddingHorizontial), BaseTools.dp2px(paddingVertical), BaseTools.dp2px(paddingHorizontial), BaseTools.dp2px(paddingVertical));
        params.alignWithParent =true;
        mViewPager.setLayoutParams(params);
        mViewPager.setPageMargin(BaseTools.dp2px(pageMargin));
        mViewPager.setOffscreenPageLimit(3);//缓存
        if (mHandler == null) {
            mHandler = new BannerHandler(this, (Activity)getContext());
        }
        if (autoLoop) {
            startAutoLoop();
        }
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        // 当Y方向滑动距离大于X方向滑动距离时不获取滚动事件
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = ev.getX();
                mDownY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if(Math.abs(ev.getY() - mDownY) > Math.abs(ev.getX() - mDownX)) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                } else {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                mDownX = ev.getX();
                mDownY = ev.getY();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }

        return super.onInterceptTouchEvent(ev);
    }

    /**
     * stopScrollWhenTouch为TRUE时
     * 按下操作停止轮转
     * 抬起操作继续轮转
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if(stopScrollWhenTouch) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if(isAutoScroll) {
                        stopAutoLoop();
                        isStoppedByTouch = true;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    if(isStoppedByTouch) {
                        startAutoLoop(period);
                        isStoppedByTouch = false;
                    }
                    break;
            }
        }

        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        // 当不可见的时候停止自动跳转
        switch (visibility) {
            case VISIBLE:
                if(isStoppedByInvisible) {
                    startCurrentAutoLoop();
                    isStoppedByInvisible = false;
                }
                break;
            case INVISIBLE:
            case GONE:
                if(isAutoScroll) {
                    stopAutoLoop();
                    isStoppedByInvisible = true;
                }
                break;
        }
    }

    /**
     * 开始自动跳转
     */
    public void startAutoLoop() {
        startAutoLoop(period);
    }

    /**
     * 开始自动跳转
     *
     * @param delayTimeInMills 延时
     */
    public void startAutoLoop(long delayTimeInMills) {
        if (null == mLoopData || mLoopData.getDatas().size() <= 1) return;
        isAutoScroll = true;
        sendScrollMessage(delayTimeInMills);
    }

    /**
     * 发送跳转消息
     *
     * @param delayTimeInMills 延时
     */
    public void sendScrollMessage(long delayTimeInMills) {
        /** 先移除消息,保证最多只有一个消息 */
        removeAllMessages();
        mHandler.sendEmptyMessageDelayed(0, delayTimeInMills);
    }

    /**
     * 开始自动跳转
     * 由于控件嵌套入RecyclerView时由不可见到可见出现页面切换时间间隔为0现象
     */
    public void startCurrentAutoLoop() {
        if (null == mLoopData || mLoopData.getDatas().size() <= 1) return;
        isAutoScroll = true;
        /** 先移除消息,保证最多只有一个消息 */
        removeAllMessages();
        mHandler.sendEmptyMessage(1);
    }

    /**
     * 移除所有消息
     */
    public void removeAllMessages() {
        if(null != mHandler) {
            mHandler.removeMessages(0);
            mHandler.removeMessages(1);
        }
    }

    /**
     * 停止自动跳转
     */
    public void stopAutoLoop() {
        isAutoScroll = false;
        if (mHandler != null) {
            removeAllMessages();
        }
    }

    /**
     * 判断是否在自动轮播
     *
     * @return
     */
    public boolean isAutoScroll() {
        return isAutoScroll;
    }

    /**
     * 获取自动跳转方向
     *
     * @return
     */
    public int getDirection() {
        return direction;
    }

    /**
     * 注册点击监听的方法
     * @param l
     */
    public void setOnClickListener(BaseBannerAdapter.OnItemClickListener l) {
        this.mOnItemClickListener = l;
    }

    /**
     * 设置跳转监听
     * @param l
     */
    public void setOnLoopListener(OnBannerListener l) {
        this.mOnLoopListener = l;
    }
    /**
     * 设置跳转监听
     */
    public void setOnBannerChangeListener(OnBannerChangeListener listener) {
        this.mOnBannerChangeListener =listener;
    }

    /**
     * 初始化View
     */
    protected abstract void initRealView();

    /**
     * 初始化轮转ViewPager
     */
    protected abstract BaseBannerAdapter initAdapter();

    /**
     * 初始化指示器
     *
     * @param size
     */
    protected abstract void initDots(int size);

    /**
     * 设置viewPager监听
     */
    protected abstract void setOnPageChangeListener();

    /**
     * 释放资源
     */
    public void releaseResources() {
        if(adapter != null) {
            adapter.releaseResources();
        }
        if (mHandler != null) {
            removeAllMessages();
            mHandler = null;
        }
    }

    /**
     * OnLoopListener
     *
     * <b>定义一个接口,当Adapter被点击的时候作为回调被调用</b>
     */
    public interface OnBannerListener {

        /**
         * LoopView 跳转到第一个时候会被调用
         *
         * @param realPosition	当前的绝对位置
         */
        void onLoopToStart(int realPosition);

        /**
         * LoopView 跳转到最后一个时候会被调用
         *
         * @param realPosition	当前的绝对位置
         */
        void onLoopToEnd(int realPosition);
    }
    /**
     * OnLoopListener
     *
     * <b>定义一个接口,当Adapter被点击的时候作为回调被调用</b>
     */
    public interface OnBannerChangeListener {

        /**
         *轮播图变化时回调
         */
        void onBannerChange(int position);
    }

}