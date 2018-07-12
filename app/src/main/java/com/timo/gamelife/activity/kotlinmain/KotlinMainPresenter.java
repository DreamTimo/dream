package com.timo.gamelife.activity.kotlinmain;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.timo.gamelife.BaseConstances;
import com.timo.gamelife.R;
import com.timo.gamelife.ServiceApi;
import com.timo.gamelife.bean.ApiShowLinkman;
import com.timo.gamelife.fragment.DreamFragment;
import com.timo.gamelife.fragment.HomeFragment;
import com.timo.gamelife.fragment.MineFragment;
import com.timo.gamelife.mvp.BasePresenterImpl;
import com.timo.timolib.BaseTools;
import com.timo.timolib.BaseConstancts;
import com.timo.timolib.network.Http;
import com.timo.timolib.tools.rx.RxSubscriber;
import com.timo.timolib.view.tablayout.CommonTabLayout;
import com.timo.timolib.view.tablayout.listener.OnTabSelectListener;

/**
 * MVPPlugin
 * 邮箱 784787081@qq.com
 */

public class KotlinMainPresenter extends BasePresenterImpl<KotlinMainContract.View> implements KotlinMainContract.Presenter {
    private String[] mTitles = {"首页", "梦想", "我的"};
    private int[] mIconSelectIds = {R.mipmap.icon_home_checked, R.mipmap.icon_dream_checked, R.mipmap.icon_mine_checked};
    private int[] mIconUnselectIds = {R.mipmap.icon_home_no_check, R.mipmap.icon_dream_no_check, R.mipmap.icon_mine_no_check};

    @Override
    public void initTop(CommonTabLayout mTabLayout) {
        BaseTools.setNavigation(mTabLayout, mTitles, mIconUnselectIds, mIconSelectIds, new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mView.showFragment(position);
                BaseTools.postMsg(BaseConstancts.TAG, false);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
    }

    private HomeFragment mHomeFragment;
    private DreamFragment mDreamFragment;
    private MineFragment mMineFragment;

    @Override
    public void initFragment(FragmentManager fragmentManager, CommonTabLayout mTabLayout, int replaceId) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        int currentTabPosition = 0;
        mHomeFragment = new HomeFragment();
        mDreamFragment = new DreamFragment();
        mMineFragment = new MineFragment();
        transaction.add(replaceId, mHomeFragment, BaseConstances.homeFragment);
        transaction.add(replaceId, mDreamFragment, BaseConstances.dreamFragment);
        transaction.add(replaceId, mMineFragment, BaseConstances.mineFragment);
        transaction.commit();
        mView.showFragment(currentTabPosition);
        mTabLayout.setCurrentTab(currentTabPosition);
    }

    @Override
    public void showFragment(int position, FragmentManager fragmentManager) {
        addSubscription(Http.getGsonApi(mView.getContext(), ServiceApi.class).showLinkman("c8192b9d81054e5f820ba9b5055f217a"), new RxSubscriber<ApiShowLinkman>(mView.getContext()) {
            @Override
            protected void _onNext(ApiShowLinkman o) {
                BaseTools.showToast(o.getReturnData().getResult().get(0).getLinkmanId());
            }
        });
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        switch (position) {
            //首页
            case 0:
                transaction.hide(mDreamFragment);
                transaction.hide(mMineFragment);
                transaction.show(mHomeFragment);
                transaction.commitAllowingStateLoss();
                break;
            //美女
            case 1:
                transaction.hide(mMineFragment);
                transaction.hide(mHomeFragment);
                transaction.show(mDreamFragment);
                transaction.commitAllowingStateLoss();
                break;
            //视频
            case 2:
                transaction.hide(mHomeFragment);
                transaction.hide(mDreamFragment);
                transaction.show(mMineFragment);
                transaction.commitAllowingStateLoss();
                break;
            default:
                break;
        }
    }
}
