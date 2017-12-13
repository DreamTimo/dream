package com.timo.gamelife.recommend;

import android.content.Context;

import com.timo.gamelife.bean.HouseBean;
import com.timo.gamelife.mvp.BasePresenter;
import com.timo.gamelife.mvp.BaseView;

import java.util.List;

/**
 * MVPPlugin
 *  邮箱 784787081@qq.com
 */

public class RecommendContract {
    interface View extends BaseView {
        void setBanner(List<String> bannerData);
        void setLl1(List<HouseBean> data);
        void setLl2(List<HouseBean> data);
        void setLl3(List<HouseBean> data);
    }

    interface  Presenter extends BasePresenter<View> {
        void setBanner();
        void setLl1();
        void setLl2();
        void setLl3();
    }
}
