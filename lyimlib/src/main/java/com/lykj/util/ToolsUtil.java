package com.lykj.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Vibrator;
import android.provider.Contacts;

import com.dlbase.util.DLAudioUtil;
import com.dlbase.util.DLDateUtils;
import com.dlbase.util.DLLogUtil;
import com.dlbase.util.DLMD5Util;
import com.dlbase.util.DLStringUtil;
import com.luyz.lyimlib.LyImEngine;
import com.lykj.model.LYUserModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by luyz on 2017/4/21.
 */

public class ToolsUtil {

    public static JSONArray getContast(Context mContext, ArrayList<LYUserModel> list){

        JSONArray tempData = new JSONArray();

        /* 取得ContentResolver */
        ContentResolver content = mContext.getContentResolver();
		        /* 取得通讯录的Phones表的cursor */
        Cursor contactcursor = content.query(Contacts.Phones.CONTENT_URI, null,
                null, null, Contacts.People.DEFAULT_SORT_ORDER);
		        /* 在LogCat里打印所有关于的列名 */
        for (int i = 0; i < contactcursor.getColumnCount(); i++) {
            String columnName = contactcursor.getColumnName(i);
            DLLogUtil.d("readTXT", "column name:" + columnName);
        }
		        /* 逐条读取记录信息 */
        int Num = contactcursor.getCount();
        DLLogUtil.v("readTXT", "recNum=" + Num);
        String name, number;

        for (int i = 0; i < Num; i++) {

            contactcursor.moveToPosition(i);
            String type = contactcursor.getString(contactcursor
                    .getColumnIndexOrThrow(Contacts.Phones.TYPE));
            DLLogUtil.v("readTXT", "type=" + type);
            String person_id = contactcursor.getString(contactcursor
                    .getColumnIndexOrThrow(Contacts.Phones.PERSON_ID));
            DLLogUtil.v("readTXT", "person_id=" + person_id);
            name = contactcursor.getString(contactcursor
                    .getColumnIndexOrThrow(Contacts.Phones.NAME));
            number = contactcursor.getString(contactcursor
                    .getColumnIndexOrThrow(Contacts.Phones.NUMBER));

            if(DLStringUtil.notEmpty(type)){
                if(type.equals("2")){
                    if(DLStringUtil.notEmpty(number) && DLStringUtil.notEmpty(name)){
                        number = number.replace(" ", "");
                        if(DLStringUtil.isPhoneNumbers(number)){
                            if (list!=null) {
                                LYUserModel tempItem = new LYUserModel();
                                tempItem.setNickName(name);
                                tempItem.setStatus("-3");
                                tempItem.setPhone(number);
                                list.add(tempItem);
                            }
                            JSONObject tempO = new JSONObject();
                            try {
                                tempO.put("phone", number);
                                tempO.put("name", name);
                                tempData.put(tempO);
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }

            DLLogUtil.v("readTXT", "name=" + name);
            DLLogUtil.v("readTXT", "*****number=" + number);
        }

        return tempData;
    }

    //java 获取可变UUID
    public static String getMyUUID(){
        UUID uuid = UUID.randomUUID();
        String uniqueId = uuid.toString();
        return uniqueId;
    }

    public static String getCharId(){
        String result = null;

        result = DLMD5Util.md5To32("android"+ LyImEngine.getInstance().getUserId()+ToolsUtil.getMyUUID()+ DLDateUtils.getCurrentTimeInLong());

        return result;
    }

    //调用系统手机振动
    public static void vibrator(Context context){
        @SuppressWarnings("static-access")
        Vibrator vibrator = (Vibrator)context.getSystemService(context.VIBRATOR_SERVICE);
        vibrator.vibrate(500);
    }

    public static void newMessageForSetting(){

        boolean isVibrator = LyImEngine.getInstance().isVibrator();
        if(isVibrator){
            vibrator(LyImEngine.getInstance().getmContext());
        }

        boolean isAudio = LyImEngine.getInstance().isAudio();
        if(isAudio){
            String tempVoice = LyImEngine.getInstance().getAudioName();
            if(DLStringUtil.notEmpty(tempVoice)){
                DLAudioUtil.playAudio("voice"+tempVoice+".mp3");
            }
        }
    }
}
