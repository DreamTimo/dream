package com.timo.gamelife.fragment.plan;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.timo.gamelife.R;
import com.timo.gamelife.detail.DetailActivity;
import com.timo.gamelife.mvp.MVPBaseFragment;
import com.timo.timolib.BaseTools;
import com.timo.timolib.view.CustomScrollView;
import com.timo.timolib.view.InnerScrollView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * MVPPlugin
 * 邮箱 784787081@qq.com
 */

public class PlanFragment extends MVPBaseFragment<PlanContract.View, PlanPresenter> implements PlanContract.View {

    @BindView(R.id.container)
    LinearLayout mContainer;
    @BindView(R.id.scrollview)
    InnerScrollView mScrollview;

    @Override
    protected String setTitleName() {
        return null;
    }

    @Override
    protected int getContentResId() {
        return R.layout.fragment_plan;
    }

    @Override
    protected void initEvent(View view) {
        DetailActivity activity = (DetailActivity) getActivity();
        CustomScrollView scrollView = (CustomScrollView) activity.findViewById(R.id.parent_scrollview);
        mScrollview.setParentScrollView(scrollView);
        mPresenter.initData();
    }

    @Override
    public void initData(List<String> data) {
        for (int i = 0; i < data.size(); i++) {
            View view = BaseTools.getView(getContext(), R.layout.item_plan_test, mContainer);
            LinearLayout root = (LinearLayout) view.findViewById(R.id.root);
            final int finalI = i;
            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseTools.showToast(finalI + "");
                }
            });
            mContainer.addView(view);
        }
    }

}
