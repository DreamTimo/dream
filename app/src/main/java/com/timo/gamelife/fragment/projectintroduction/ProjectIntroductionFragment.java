package com.timo.gamelife.fragment.projectintroduction;


import android.view.View;

import com.timo.gamelife.R;
import com.timo.gamelife.mvp.MVPBaseFragment;

/**
 * MVPPlugin
 *  邮箱 784787081@qq.com
 */

public class ProjectIntroductionFragment extends MVPBaseFragment<ProjectIntroductionContract.View, ProjectIntroductionPresenter> implements ProjectIntroductionContract.View {

    @Override
    protected String setTitleName() {
        return null;
    }

    @Override
    protected int getContentResId() {
        return R.layout.fragment_project_introduction;
    }

    @Override
    protected void initEvent(View view) {

    }
}
