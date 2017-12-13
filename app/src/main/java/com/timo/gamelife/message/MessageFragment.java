package com.timo.gamelife.message;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lykj.MQTT.LYMQTTHelper;
import com.lykj.MQTT.LYMessageHelper;
import com.timo.gamelife.R;
import com.timo.gamelife.fragment.DreamFragment;
import com.timo.timolib.BaseTools;
import com.timo.timolib.base.AppInfo;
import com.timo.timolib.base_fragment.BaseFragment;
import com.timo.timolib.view.LineTabIndicator;

public class MessageFragment extends BaseFragment {
    @Override
    protected String setTitleName() {
        return null;
    }

    @Override
    protected int getContentResId() {
        return R.layout.fragment_contacts;
    }


    private ViewPager pager;
    private LineTabIndicator indicator;
    private RelativeLayout rl_title;
    private TextView redPoint1;
    private TextView redPoint2;

    @Override
    protected void initEvent(View v) {
        try {
            setMessage(v);
        } catch (Exception e) {
            BaseTools.printErrorMessage(e);
        }
        try {
            LYMQTTHelper.getInstance().getMessageHelper().setListener(new LYMessageHelper.onMessageHelperListener() {
                @Override
                public void onMsgCount(String num) {
                }

                @Override
                public void onUserMessage(String num) {
                    if (num != null && Integer.parseInt(num) > 0) {
                        redPoint1.setVisibility(View.VISIBLE);
                        redPoint1.setText(num);
                    } else {
                        redPoint1.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onSystemMessage(String num) {
                    if (num != null && Integer.parseInt(num) > 0) {
                        redPoint2.setVisibility(View.VISIBLE);
                        redPoint2.setText(num);
                    } else {
                        redPoint2.setVisibility(View.GONE);
                    }
                }
            });
        } catch (Exception e) {
            BaseTools.printErrorMessage(e);
        }

        try {
            if (AppInfo.isLogin()) {
                LYMQTTHelper.getInstance().getMessageHelper().getAllMsgCount();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private LYUserMessageFragment fragment;

    private void setMessage(View v) {
        pager = (ViewPager) v.findViewById(R.id.vp_home);
        indicator = (LineTabIndicator) v.findViewById(R.id.lt_title);
        rl_title = (RelativeLayout) v.findViewById(R.id.rl_title);
        HomePagerAdapter homePagerAdapter = new HomePagerAdapter(getActivity(), pager, getChildFragmentManager());
        indicator.setVisibility(View.VISIBLE);
        rl_title.setVisibility(View.GONE);
        fragment = new LYUserMessageFragment();
        homePagerAdapter.addTab(getString(R.string.MessageFragment_mine), fragment);
        homePagerAdapter.addTab(getString(R.string.MessageFragment_sys), new DreamFragment());
        pager.setAdapter(homePagerAdapter);
        indicator.setViewPager(pager);
        redPoint1 = (TextView) v.findViewById(R.id.message_red_point1);
        redPoint2 = (TextView) v.findViewById(R.id.message_red_point2);
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            if (AppInfo.isLogin()) {
                LYMQTTHelper.getInstance().getMessageHelper().getAllMsgCount();
            }
            if (AppInfo.isLogin()) {
                if (fragment != null) {
                    fragment.fresh();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
