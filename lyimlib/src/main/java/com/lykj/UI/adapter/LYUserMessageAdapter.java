package com.lykj.UI.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dlbase.base.DLBaseAdapter;
import com.dlbase.util.DLStringUtil;
import com.dlbase.util.DLTimeUtil;
import com.dlbase.util.ViewHolder;
import com.dlbase.view.CircleImageView;
import com.luyz.lyimlib.LYIMConfig;
import com.luyz.lyimlib.LyImEngine;
import com.luyz.lyimlib.R;
import com.lykj.emojicon.EmojiconTextView;
import com.lykj.model.LYChatListModel;

import java.util.ArrayList;

/**
 * Created by luyz on 2017/5/26.
 */

public class LYUserMessageAdapter extends DLBaseAdapter {

    private Context mContext;
    private ArrayList<LYChatListModel> list;

    public LYUserMessageAdapter(Context context, ArrayList<LYChatListModel> data){
        mContext = context;
        list = data;
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return this.list.size()==0?0:this.list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return this.list.get(position);
    }


    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    /**
     * 刷新页面
     */
    public void refresh() {
        notifyDataSetChanged();
    }

    @SuppressWarnings("unused")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {



        ViewHolder tempView = ViewHolder.get(mContext, convertView, parent, R.layout.adapter_lyusermessage, position);

        CircleImageView tempHeaderView = tempView.getView(R.id.headerview);
        ImageView iv_chatflag = tempView.getView(R.id.iv_chatflag);
        TextView tempName = tempView.getView(R.id.tv_name);
        EmojiconTextView tempDes = tempView.getView(R.id.tv_des);
        TextView tempTiemr = tempView.getView(R.id.tv_timer);
        TextView newfcount = tempView.getView(R.id.newfcount);

        LYChatListModel tempItem = list.get(position);

        if(tempItem!=null){

            tempName.setText("");
            tempTiemr.setText("");
            tempDes.setText("");
            newfcount.setVisibility(View.GONE);
            newfcount.setText("0");

            if(tempItem.getNewCount()>0){
                newfcount.setVisibility(View.VISIBLE);
                newfcount.setText(tempItem.getNewCount()+"");
            }

            if(tempItem.getFromUserModel()!=null){

                tempHeaderView.setImageResource(R.drawable.image_default);
                if(DLStringUtil.notEmpty(tempItem.getFromUserModel().getAvatar())){
                    LyImEngine.getInstance().DownImage(tempItem.getFromUserModel().getAvatar()+ LYIMConfig.THUMBNAIL100,tempHeaderView,R.drawable.image_default);
                }
                if(DLStringUtil.notEmpty(tempItem.getFromUserModel().getUserName())){
                    tempName.setText(tempItem.getFromUserModel().getUserName());
                }
            }

            if(tempItem.getLastItemModel()!=null){
                if(DLStringUtil.notEmpty(tempItem.getLastItemModel().getChatTimer())){
                    tempTiemr.setText(DLTimeUtil.ToTime(DLStringUtil.strToLong(tempItem.getLastItemModel().getChatTimer())));
                }

                switch (tempItem.getLastItemModel().getChatType()) {
                    case EChat_Text:
                    case EChat_System:
                    {
                        if(DLStringUtil.notEmpty(tempItem.getLastItemModel().getChatContent())){
                            tempDes.setText(tempItem.getLastItemModel().getChatContent());
                        }
                    }
                    break;
                    case EChat_Image:{
                        tempDes.setText("[图片]");
                    }
                    break;
                    case EChat_Voice:{
                        tempDes.setText("[语音]");
                    }
                    break;
                    case EChat_Video:{
                        tempDes.setText("[视频]");
                    }
                    break;
                    case EChat_Card:{
                        tempDes.setText("[名片]");
                    }
                    break;
                    case EChat_Location:{
                        tempDes.setText("[位置]");
                    }
                    break;
                    case EChat_File:{
                        tempDes.setText("[文件]");
                    }
                    break;
                    case EChat_Project:{
                        tempDes.setText("[项目]");
                    }
                    break;
                    default:
                        break;
                }
            }
        }

        return tempView.getConvertView();
    }
}
