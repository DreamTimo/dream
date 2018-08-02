package com.timo.gamelife.activity;

import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.timo.gamelife.R;
import com.timo.gamelife.activity.kotlinmain.KotlinMainActivity;
import com.timo.gamelife.bean.UserBean;
import com.timo.timolib.base.base_manager.AppInfo;
import com.timo.timolib.base.base_activity.BaseActivity;

import java.util.ArrayList;

public class GuideActivity extends BaseActivity {

    private static final int WHAT_JUMP_TO_MAIN = 2;
    private static final int REFRESH_TIME = 3;
    private ViewPager viewPager;
    private TextView description;
    //添加小点的
    private LinearLayout pointGroup;
    private ArrayList<ImageView> imageList;
    protected int lastPosition;
    // 引导图片资源
    private static final int[] pics = {R.drawable.guide_a, R.drawable.guide_b, R.drawable.guide_c, R.drawable.guide_d, R.drawable.welcome_image};
    private Button bt_skip;

    @Override
    protected int getContentResId() {
        if (AppInfo.isFirst()) {
            return R.layout.activity_guide;
        } else {
            return R.layout.activity_welcome;
        }
    }

    @Override
    protected void initEvent() {
        if (AppInfo.isFirst()) {
            initGuide();
        } else {
            initWelcome();
        }
    }

    public void initWelcome() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                goToMain();
            }
        }, 2000);
    }

    public void initGuide() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        description = (TextView) findViewById(R.id.description);
        pointGroup = (LinearLayout) findViewById(R.id.point_group);
        bt_skip = (Button) findViewById(R.id.bt_skip);
        bt_skip.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                AppInfo.setIsFirst();
                goToMain();
            }
        });
        setViewPagerData();
    }

    private void setViewPagerData() {
        try {
            imageList = new ArrayList<ImageView>();
            for (int i = 0; i < pics.length; i++) {
                //加五个图  加五个排好版的圆点
                //初始化图片资源
                ImageView image = new ImageView(this);
                LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                image.setLayoutParams(layoutParams);
                image.setScaleType(ScaleType.FIT_XY);
                image.setImageResource(pics[i]);

                imageList.add(image);
                //添加指示点
                ImageView point = new ImageView(this);
                //设置点的属性  例如点的大小以及点的隔离位置（margin）
                //因为点是放在LinearLayout里面，所以这里就new一个LinearLayout
                //如果不是放在LinearLayout里面，例如是相对布局，这里就new相对布局
                //注意LayoutParams的使用  看做是设置布局里面ImageView这个View的参数属性即可
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        //参数也可以直接指定大小  例如 5，5
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                //用代码代替xml布局属性
                params.rightMargin = 20;
                point.setLayoutParams(params);

                //选择器
                point.setBackgroundResource(R.drawable.point_bg);
                if (i == 0) {
                    point.setEnabled(true);
                } else {
                    point.setEnabled(false);
                }
                pointGroup.addView(point);
            }

            viewPager.setAdapter(new MyPagerAdapter());

            //setCurrentItem是设置当前显示的图片的位置
            //getCurrentItem是获取当前显示的图片的位置

            //加上这个可以使得向左滑动也能和向右一样无限循环
            //viewPager.setCurrentItem(Integer.MAX_VALUE/2 - (Integer.MAX_VALUE/2%imageList.size())) ;
            //监听页面切换  切换时就改变textView的文字
            viewPager.setOnPageChangeListener(new MyPageChanageListener());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class MyPageChanageListener implements OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int position) {
            position = position % imageList.size();
            pointGroup.getChildAt(position).setEnabled(true);
            //把上一个点设为false
            pointGroup.getChildAt(lastPosition).setEnabled(false);
            lastPosition = position;
            if (position == imageList.size() - 1) {
                bt_skip.setVisibility(View.VISIBLE);
            } else {
                bt_skip.setVisibility(View.GONE);
            }
            if (position == 0) {
                description.setText(getString(R.string.description_a));
            } else if (position == 1) {
                description.setText(getString(R.string.description_b));
            } else if (position == 2) {
                description.setText(getString(R.string.description_c));
            } else if (position == 3) {
                description.setText(getString(R.string.description_d));
            } else if (position == 4) {
                description.setText(getString(R.string.description_e));
            }
        }

    }

    private class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return pics.length;

        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // 给 container 添加一个view
            container.addView(imageList.get(position % imageList.size()));
            //返回一个和该view相对的object
            return imageList.get(position % imageList.size());
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            object = null;
        }
    }

    public void goToMain() {
        UserBean userBean = new UserBean();
        userBean.setName("蔡永汪");
        AppInfo.saveUser(userBean);
        startActivityAddFinish(KotlinMainActivity.class);
    }

}
