package com.timo.gamelife.user;

import com.timo.gamelife.bean.CityInfo;
import com.timo.gamelife.mvp.BasePresenter;
import com.timo.gamelife.mvp.BaseView;
import com.timo.gamelife.bean.Book;

/**
 * MVPPlugin
 * 邮箱 784787081@qq.com
 */

public class UserContract {
    interface View extends BaseView {
        void onSuccess(Book mBook);

        void onError(String result);
        void getData(CityInfo info);
    }

    interface Presenter extends BasePresenter<View> {
        void onStop();
        void getData();
    }
}
