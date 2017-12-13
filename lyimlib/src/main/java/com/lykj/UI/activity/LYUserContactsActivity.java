package com.lykj.UI.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Contacts;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.dlbase.base.DLBaseActivity;
import com.dlbase.pinyin.DLAssortView;
import com.dlbase.util.DLLogUtil;
import com.dlbase.util.DLStringUtil;
import com.luyz.lyimlib.LyImEngine;
import com.luyz.lyimlib.R;
import com.lykj.UI.adapter.LYUserContactsAdapter;
import com.lykj.model.LYUserModel;
import com.lykj.util.ToolsUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class LYUserContactsActivity extends DLBaseActivity {


    private TextView et_phContacts_search;

    private TextView tv_clew_notdata;
    private ExpandableListView lv_listView;
    private DLAssortView assortView;
    private ArrayList<LYUserModel> list;
    private LYUserContactsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyuser_contacts);

        initView();
        initData();
    }

    private void initView(){

        initNavView("手机联系人", TTopBackType.ETopBack_Black, false, R.id.top_view);

        tv_clew_notdata = (TextView) findViewById(R.id.tv_clew_notdata);
        lv_listView = (ExpandableListView) findViewById(R.id.lv_listView);
        assortView = (DLAssortView) findViewById(R.id.assortview);

        tv_clew_notdata.setVisibility(View.GONE);

        assortView.setOnTouchAssortListener(new DLAssortView.OnTouchAssortListener() {

            View layoutView= LayoutInflater.from(LYUserContactsActivity.this)
                    .inflate(R.layout.alert_dialog_menu_layout, null);
            TextView textalphabet =(TextView) layoutView.findViewById(R.id.alphabet);
            PopupWindow popupWindow ;

            public void onTouchAssortListener(String str) {
                int index=adapter.getAssort().getHashList().indexOfKey(str);
                if(index!=-1)
                {
                    lv_listView.setSelectedGroup(index);;
                }
                if(popupWindow!=null){
                    textalphabet.setText(str);
                }
                else
                {
                    popupWindow = new PopupWindow(layoutView,150,150,false);
                    popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
                }
                textalphabet.setText(str);
            }

            public void onTouchAssortUP() {
                if(popupWindow!=null)
                    popupWindow.dismiss();
                popupWindow=null;
            }
        });
    }

    private void initData(){

        list = new ArrayList<LYUserModel>();

        getContactsInfoListFromPhone();

    }
    /* 读取手机中的contacts内容 */
    @SuppressWarnings("deprecation")
    private void getContactsInfoListFromPhone() {

        Message msg = new Message();
        msg.what = 991;
        mHandler.sendMessage(msg);
    }

    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);

            if(msg.what == 991){

                list.clear();

                JSONArray tempData = ToolsUtil.getContast(mContext,list);

                Message msg2 = new Message();
                msg2.what = 992;
                msg2.obj = tempData;
                mHandler.sendMessage(msg2);
            }else if(msg.what == 992){

                JSONArray tempData = (JSONArray)msg.obj;

                adapter = new LYUserContactsAdapter(mContext, list,false,btnlistener);
                adapter.setShowPhone(true);
                lv_listView.setAdapter(adapter);

                for (int i = 0, length = adapter.getGroupCount(); i < length; i++) {
                    lv_listView.expandGroup(i);
                }

                adapter.notifyDataSetChanged();

                updateContent(tempData.toString());
            }
        }

    };

    private void updateContent(String data){

        LyImEngine.getInstance().showWaitDialog(mContext);

        if (LyImEngine.getInstance().getLyimListener()!=null){
            LyImEngine.getInstance().getLyimListener().onGetUserContants(LyImEngine.getInstance().getUserId(), new LyImEngine.onUserContastListener() {
                @Override
                public void onUserContast(ArrayList<LYUserModel> data) {
                    list.clear();
                    list.addAll(data);
                    adapter = new LYUserContactsAdapter(mContext, list,false,btnlistener);
                    adapter.setShowPhone(true);
                    lv_listView.setAdapter(adapter);

                    for (int i = 0, length = adapter.getGroupCount(); i < length; i++) {
                        lv_listView.expandGroup(i);
                    }
                    adapter.notifyDataSetChanged();

                    LyImEngine.getInstance().hideWaitDialog();
                }
            });
        }
    }

    private void send(String number, String message){
        Uri uri = Uri.parse("smsto:" + number);
        Intent sendIntent = new Intent(Intent.ACTION_VIEW, uri);
        sendIntent.putExtra("sms_body", message);
        startActivity(sendIntent);
    }

    private View.OnClickListener btnlistener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub

            @SuppressWarnings("unchecked")
            HashMap<String, Object> tempMap = (HashMap<String, Object>) v.getTag();

            if(tempMap!=null){
                int group = (int)tempMap.get("group");
                int child = (int)tempMap.get("child");

                LYUserModel tempItem = (LYUserModel) adapter.getChild(group, child);
                if(tempItem!=null){
                    send(tempItem.getPhone(), "你的好友"+LyImEngine.getInstance().getUserModel().getUserName()+"("+LyImEngine.getInstance().getUserId()+")"+"邀请您安装\"催化剂\"APP,下载地址：");
                }
            }
        }
    };
    @Override
    public void onClick(View arg0) {

        if(arg0.getId() == R.id.top_left){
            finish();
        }
    }
}
