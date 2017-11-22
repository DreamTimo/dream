package com.timo.timolib.view.banner.internal;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * 版权所有：XXX有限公司
 *
 * LoopHandler
 *
 * @author zhou.wenkai ,Created on 2015-11-2 17:58:21
 * Major Function：<b>LoopHandler</b>
 *
 * 注:如果您修改了本类请填写以下内容作为记录，如非本人操作劳烦通知，谢谢！！！
 * @author mender，Modified Date Modify Content:
 */
public class BannerHandler extends Handler {

    private final WeakReference<Activity> mActivity;
    private final WeakReference<BaseBanner> mLoopView;

    public BannerHandler(BaseBanner loopView, Activity activity) {
        this.mLoopView = new WeakReference<BaseBanner>(loopView);
        this.mActivity = new WeakReference<Activity>(activity);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);

        Activity activity = mActivity.get();
        BaseBanner loopView = mLoopView.get();
        if (activity != null && loopView != null) {
            if (!loopView.isAutoScroll()) return;

            switch (msg.what) {
                case 0: // 自动跳转
                    int change = (loopView.getDirection() == BaseBanner.LEFT) ? -1 : 1;
                    loopView.getViewPager().setCurrentItem(loopView.getViewPager().getCurrentItem() + change, true);
                    loopView.sendScrollMessage(loopView.getPeriod());
                    break;
                case 1: // 由不可见到可见跳转一次
                    loopView.getViewPager().setCurrentItem(loopView.getViewPager().getCurrentItem() - 1, false);
                    loopView.getViewPager().setCurrentItem(loopView.getViewPager().getCurrentItem() + 1, false);
                    loopView.sendScrollMessage(loopView.getPeriod());
                    break;
            }

        } else {
            removeMessages(0);
            removeMessages(1);
            if (loopView != null) {
                loopView.releaseResources();
            }
        }
    }

}