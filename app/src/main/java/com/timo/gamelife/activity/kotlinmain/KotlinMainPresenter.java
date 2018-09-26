package com.timo.gamelife.activity.kotlinmain;

import android.os.Environment;

import com.timo.gamelife.R;
import com.timo.gamelife.ServiceApi;
import com.timo.gamelife.bean.ApiObj;
import com.timo.gamelife.bean.ApiShowLinkman;
import com.timo.gamelife.mvp.BasePresenterImpl;
import com.timo.httplib.network.MyHttp;
import com.timo.httplib.network.MySubscriber;
import com.timo.timolib.BaseTools;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

/**
 * MVPPlugin
 * 邮箱 784787081@qq.com
 */

public class KotlinMainPresenter extends BasePresenterImpl<KotlinMainContract.View> implements KotlinMainContract.Presenter {
    private String[] mTitles = {"首页", "梦想", "我的"};
    private int[] mIconSelectIds = {R.mipmap.icon_home_checked, R.mipmap.icon_dream_checked, R.mipmap.icon_mine_checked};
    private int[] mIconUnselectIds = {R.mipmap.icon_home_no_check, R.mipmap.icon_dream_no_check, R.mipmap.icon_mine_no_check};

    @Override
    public String[] getTitles() {
        return mTitles;
    }

    @Override
    public int[] getSelect() {
        return mIconUnselectIds;
    }

    @Override
    public int[] getSelected() {
        return mIconSelectIds;
    }

    public void getData() {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/tencent/MicroMsg/WeiXin/wx_camera_1534998696139.jpg");
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName());
        RequestBody session = RequestBody.create(MediaType.parse("text/plain"), "0aca277faf7b4518b6024d4f38ccf756");
        addSubscription(MyHttp.getGsonApi(mView.getContext(), ServiceApi.class).showLinkman(session, body), new MySubscriber<ApiObj>(mView.getContext()) {
            @Override
            protected void _onNext(ApiObj o) {
                BaseTools.showToast(o.getMsg());
            }
        });
    }
}
