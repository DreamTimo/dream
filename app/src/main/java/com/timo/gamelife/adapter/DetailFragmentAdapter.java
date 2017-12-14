package com.timo.gamelife.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.timo.gamelife.fragment.DreamFragment;
import com.timo.gamelife.fragment.HomeFragment;
import com.timo.gamelife.fragment.plan.PlanFragment;
import com.timo.gamelife.fragment.projectintroduction.ProjectIntroductionFragment;

/**
 * Created by 蔡永汪 on 2017/12/14.
 */

public class DetailFragmentAdapter extends FragmentPagerAdapter {
    public DetailFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new ProjectIntroductionFragment();
        }
        if (position == 1) {
            return new PlanFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    /**
     * 将 标题的数组文字 添加到title上面
     *
     * @param position
     * @return
     */
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "项目介绍";
        }
        if (position == 1) {
            return "行程安排";
        }
        return null;
    }
}
