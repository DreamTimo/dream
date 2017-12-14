package com.timo.gamelife.fragment.plan;

import android.content.Context;

import com.timo.gamelife.mvp.BasePresenterImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * MVPPlugin
 * 邮箱 784787081@qq.com
 */

public class PlanPresenter extends BasePresenterImpl<PlanContract.View> implements PlanContract.Presenter {

    @Override
    public void initData() {
        List<String> data = new ArrayList<>();
        data.add("111");
        data.add("111");
        data.add("111");
        data.add("111");
        data.add("111");
        mView.initData(data);
    }
}
