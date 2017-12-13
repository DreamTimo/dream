package com.timo.gamelife.recommend;

import android.content.Context;

import com.timo.gamelife.bean.HouseBean;
import com.timo.gamelife.mvp.BasePresenterImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * MVPPlugin
 *  邮箱 784787081@qq.com
 */

public class RecommendPresenter extends BasePresenterImpl<RecommendContract.View> implements RecommendContract.Presenter{

    @Override
    public void setBanner() {
        List<String> bannerData =new ArrayList<>();
        bannerData.add("1111");
        bannerData.add("1111");
        bannerData.add("1111");
        bannerData.add("1111");
        mView.setBanner(bannerData);
    }

    @Override
    public void setLl1() {
        List<HouseBean> houseBeen=new ArrayList<>();
        houseBeen.add(new HouseBean());
        houseBeen.add(new HouseBean());
        houseBeen.add(new HouseBean());
        mView.setLl1(houseBeen);
    }

    @Override
    public void setLl2() {
        List<HouseBean> houseBeen=new ArrayList<>();
        houseBeen.add(new HouseBean());
        houseBeen.add(new HouseBean());
        houseBeen.add(new HouseBean());
        mView.setLl2(houseBeen);
    }

    @Override
    public void setLl3() {
        List<HouseBean> houseBeen=new ArrayList<>();
        houseBeen.add(new HouseBean());
        houseBeen.add(new HouseBean());
        houseBeen.add(new HouseBean());
        mView.setLl3(houseBeen);
    }
}
