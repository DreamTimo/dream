package com.timo.gamelife.message;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.dlbase.util.DLShowDialog;
import com.luyz.lyimlib.LyImEngine;
import com.lykj.MQTT.LYMQTTHelper;
import com.lykj.UI.activity.LYChatActivity;
import com.lykj.UI.adapter.LYUserMessageAdapter;
import com.lykj.model.LYChatListModel;
import com.timo.timolib.BaseTools;

import java.util.ArrayList;

/**
 * Created by luyz on 2017/5/26.
 */

public class LYUserMessageFragment extends Fragment {

    private View v;
    private Context mContext;

    private ListView lv_listview;
    private ArrayList<LYChatListModel> list;
    private LYUserMessageAdapter adapter;

    private boolean islogn;
    public void fresh(){
        try{
            getData();
        }catch (Exception e){
            BaseTools.printErrorMessage(e);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(com.luyz.lyimlib.R.layout.fragment_lyusermessage, null);

        mContext = getActivity();

        initView();

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getData();
    }

    private void initView(){

        lv_listview = (ListView)v.findViewById(com.luyz.lyimlib.R.id.lv_listview);

        lv_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (islogn){
                    return;
                }

            LYChatListModel tempItem = list.get(i);
            if (tempItem!=null){
                tempItem.setNewCount(0);

                LyImEngine.getInstance().getDbHelper().getCharListTable().checkAndInsertCharListData(tempItem);

                adapter.notifyDataSetChanged();

                Intent intent = new Intent(mContext, LYChatActivity.class);
                intent.putExtra(LYChatActivity.PAGE_MESSAGETYPE,"0");
                intent.putExtra(LYChatActivity.PAGE_USERMODEL,tempItem.getFromUserModel());
                startActivityForResult(intent,309);
            }
            }
        });

        lv_listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {

                islogn = true;

                final LYChatListModel tempItem = list.get(i);
                if (tempItem!=null){

                    DLShowDialog.getInstance().showDialog("确认删除？",mContext, new DLShowDialog.DialogListener() {
                        @Override
                        public void onSubmitClick(Object obj) {
                            list.remove(i);

                            LyImEngine.getInstance().getDbHelper().getCharListTable().deleteCharList(tempItem);

                            adapter.notifyDataSetChanged();
                            islogn = false;
                        }

                        @Override
                        public void onCancleClick(Object obj) {

                        }
                    });
                }

                return true;
            }
        });

        list = new ArrayList<>();
        adapter = new LYUserMessageAdapter(mContext,list);
        lv_listview.setAdapter(adapter);

        LYMQTTHelper.getInstance().setChatListListener(new LYMQTTHelper.onChatListListener() {
            @Override
            public void toUpdateChatList() {
                getData();
            }
        });
    }

    private void getData(){
        try{
            if (LyImEngine.getInstance().getDbHelper()!=null){
                ArrayList<LYChatListModel> tempArray = LyImEngine.getInstance().getDbHelper().getCharListTable().queryAllCharList();
                if (tempArray!=null && tempArray.size()>0){
                    if (list==null){
                        list=new ArrayList<>();
                    }
                    list.clear();
                    list.addAll(tempArray);
                    if(adapter!=null){
                        adapter.refresh();
                    }
                }
            }
        }catch (Exception e){
            BaseTools.printErrorMessage(e);
        }
    }
}
