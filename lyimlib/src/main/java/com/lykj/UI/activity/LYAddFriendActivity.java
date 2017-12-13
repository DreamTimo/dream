package com.lykj.UI.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.dlbase.base.DLBaseActivity;
import com.dlbase.util.DLStringUtil;
import com.dlbase.util.DLToastUtil;
import com.luyz.lyimlib.LyImEngine;
import com.luyz.lyimlib.R;

public class LYAddFriendActivity extends DLBaseActivity {

    private EditText et_phContacts_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyadd_friend);

        initView();

        initData();
    }
    private void initView(){

        initNavView("添加好友", TTopBackType.ETopBack_Black, true,R.id.top_view);

        topRight.setText("保存");

        et_phContacts_search = (EditText) findViewById(R.id.et_phContacts_search);
    }

    private void initData(){

    }

    @Override
    public void onClick(View arg0) {
        if(arg0.getId() == R.id.top_left){
            finish();
        }else if(arg0.getId() == R.id.top_right){
            save();
        }
    }

    private void save(){

        String tempPhone = et_phContacts_search.getText().toString();

        if (DLStringUtil.isEmpty(tempPhone)){
            DLToastUtil.showToastShort(mContext,"请输入手机号码");
            return;
        }

        if (!DLStringUtil.isPhoneNumbers(tempPhone)){
            DLToastUtil.showToastShort(mContext,"请输入手机号码");
            return;
        }

        LyImEngine.getInstance().showWaitDialog(mContext);

        if (LyImEngine.getInstance().getLyimListener()!=null){
            LyImEngine.getInstance().getLyimListener().onPostAddShip(LyImEngine.getInstance().getUserId(), tempPhone, null, new LyImEngine.onAddShipListener() {
                @Override
                public void onSuccess() {
                    LyImEngine.getInstance().hideWaitDialog();
                    finish();
                }
            });
        }
    }
}
