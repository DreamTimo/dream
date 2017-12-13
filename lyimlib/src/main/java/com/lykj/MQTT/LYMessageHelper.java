package com.lykj.MQTT;

import android.os.Handler;
import android.os.Message;

import com.dlbase.util.DLStringUtil;
import com.luyz.lyimlib.LyImEngine;


/**
 * Created by luyz on 2017/5/26.
 */

public class LYMessageHelper {

    private onMessageHelperListener listener;
    private String unreadsysmsgcount = "0";
    private String userCount = "0";

    public LYMessageHelper(){

    }

    Handler mHandle = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);

            if (msg.what == 700){

                userCount = getUserMsgCount()+"";
                int codd = DLStringUtil.strToInt(userCount);
                if (listener!=null){
                    listener.onUserMessage((codd>99?99:codd)+"");
                }
                if (listener!=null){
                    listener.onMsgCount((codd>99?99:codd)+"");
                }

                mHandle.sendEmptyMessage(701);

            }else if (msg.what == 701){

                getUnreadSystemMsgCount();
            }
        }
    };

    public void getAllMsgCount(){

        mHandle.sendEmptyMessage(700);
    }

    private int  getUserMsgCount(){

        int count = 0;
        try {
            if (LyImEngine.getInstance().getDbHelper()!=null&&LyImEngine.getInstance().getDbHelper().getCharListTable()!=null){
                count = LyImEngine.getInstance().getDbHelper().getCharListTable().queryCharListToNewCounts();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return count;
    }

    private void getUnreadSystemMsgCount(){

        if (LyImEngine.getInstance().getLyimListener()!=null){
            LyImEngine.getInstance().getLyimListener().onUnReadMsgCount(LyImEngine.getInstance().getUserId(), new LyImEngine.onUnReadMsgCountListener() {
                @Override
                public void onUnReadMsgCount(String count) {
                    unreadsysmsgcount = count;

                    int codd = DLStringUtil.strToInt(userCount);
                    int das = DLStringUtil.strToInt(unreadsysmsgcount);
                    if (listener!=null){
                        listener.onSystemMessage((das>99?99:das)+"");
                    }
                    if (listener!=null){
                        listener.onUserMessage((codd>99?99:codd)+"");
                    }
                    int mdhd = codd+das;
                    if (listener!=null){
                        listener.onMsgCount((mdhd>99?99:mdhd)+"");
                    }
                }
            });
        }
    }

    public onMessageHelperListener getListener() {
        return listener;
    }

    public void setListener(onMessageHelperListener listener) {
        this.listener = listener;
    }

    public String getUserCount() {
        return userCount;
    }

    public void setUserCount(String userCount) {
        this.userCount = userCount;
    }

    public interface onMessageHelperListener{
        public void onMsgCount(String num);
        public void onUserMessage(String num);
        public void onSystemMessage(String num);
    }
}
