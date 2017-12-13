package com.lykj.UI.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;

import com.dlbase.base.DLBaseActivity;
import com.dlbase.util.DLArrayUtil;
import com.luyz.lyimlib.LyImEngine;
import com.luyz.lyimlib.R;
import com.lykj.UI.adapter.LYPinyinAdapter;
import com.lykj.model.LYUserModel;

import java.util.ArrayList;

public class LYFriendsActivity extends DLBaseActivity {

    public static final String PAGEKEY_CARD = "card";

    private ArrayList<LYUserModel> list;
    private ExpandableListView elv_contacts_elistView;
    private LYPinyinAdapter adapter;

    private boolean showCard = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyfriends);

        showCard = getIntent().getBooleanExtra(PAGEKEY_CARD,false);

        initView();
        initData();
    }

    private void initView(){

        initNavView("好友", TTopBackType.ETopBack_Black, !showCard, R.id.top_view);

        if (!showCard){
            topRight.setText("添加好友");
        }

        elv_contacts_elistView = (ExpandableListView)findViewById(R.id.elv_contacts_elistView);

        elv_contacts_elistView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub

                LYUserModel tempItem = (LYUserModel) adapter.getChild(groupPosition, childPosition);
                if(tempItem!=null){

                    if (showCard){
                        toCard(tempItem);
                    }else{
                        toUserHome(tempItem);
                    }
                }

                return true;
            }
        });
    }

    private void toCard(LYUserModel model){

        Intent intent = new Intent();
        intent.putExtra(PAGEKEY_CARD,model);
        setResult(RESULT_OK,intent);
        finish();
    }

    private void toUserHome(LYUserModel model){
        if(model!=null){
            if (LyImEngine.getInstance().getLyimListener()!=null){
                LyImEngine.getInstance().getLyimListener().onToUser(mContext,model);
            }
        }
    }

    private void initData(){

        list = new ArrayList<LYUserModel>();

        getShipList();
    }

    private void updateListData(ArrayList<LYUserModel> users){
        list.clear();
        if(DLArrayUtil.notEmpty(users)){
            list.addAll(users);

        }
        adapter = new LYPinyinAdapter(mContext,list);
        elv_contacts_elistView.setAdapter(adapter);
        for (int i = 0, length = adapter.getGroupCount(); i < length; i++) {
            elv_contacts_elistView.expandGroup(i);
        }
        adapter.notifyDataSetChanged();
    }

    private void getShipList(){

        String tempUserId = LyImEngine.getInstance().getUserId();

        if (LyImEngine.getInstance().getLyimListener()!=null){
            LyImEngine.getInstance().getLyimListener().onGetShipList(tempUserId, new LyImEngine.onShipListListener() {
                @Override
                public void onShipList(ArrayList<LYUserModel> array) {
                    if (array!=null){
                        updateListData(array);
                    }
                }
            });
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        getShipList();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.top_left) {
            finish();
        }else if(v.getId() == R.id.top_right){
            if (!showCard) {
                 startActivity(new Intent(mContext,LYAddFriendActivity.class));
            }
        }
    }

    public void refreshData(){
        getShipList();
    }
}
