package com.timo.gamelife.activity.projectweb;


import android.view.View;

import com.timo.gamelife.R;
import com.timo.gamelife.mvp.MVPBaseActivity;
import com.timo.timolib.BaseTools;
import com.timo.timolib.view.CommonWebView;
import com.timo.timolib.view.TitleBar;

/**
 * MVPPlugin
 * 邮箱 784787081@qq.com
 */

public class ProjectWebActivity extends MVPBaseActivity<ProjectWebContract.View, ProjectWebPresenter> implements ProjectWebContract.View {

    CommonWebView mWebview;
    TitleBar mTitle;

    @Override
    protected int getContentResId() {
        return R.layout.activity_project;
    }

    @Override
    protected void initEvent() {
        mWebview= (CommonWebView) findViewById(R.id.webview);
        mTitle= (TitleBar) findViewById(R.id.title);
        BaseTools.setTitleBar(mTitle, getString(R.string.title_project), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        BaseTools.loadWeb(mWebview, "https://fir.im");
    }

}
