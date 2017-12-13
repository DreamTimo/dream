package com.lykj.Bean;

import com.dlbase.base.DLBaseModel;
import com.dlbase.util.DLJsonUtil;
import com.lykj.model.LYChatItemModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by luyz on 2017/6/9.
 */

public class LYChatBean extends DLBaseModel {

    private boolean has_more;
    private String limit;
    private String next_max;

    private ArrayList<LYChatItemModel> data ;

    public void parseJson(String json){
        try {
            JSONObject tempO = new JSONObject(json);

            int code = DLJsonUtil.hasAndGetInt(tempO,"code");

            if (code == 0){
                JSONObject tempD = DLJsonUtil.hasAndGetJsonObject(tempO,"data");
                if (tempD!=null){
                    limit = DLJsonUtil.hasAndGetString(tempD,"limit");
                    next_max = DLJsonUtil.hasAndGetString(tempD,"next_max");
                    has_more = DLJsonUtil.hasAndGetBoolean(tempD,"has_more");

                    JSONArray tempData = DLJsonUtil.hasAndGetJsonArray(tempD,"list");
                    if (tempData!=null){
                        data = new ArrayList<LYChatItemModel>();

                        for (int i=0;i<tempData.length();i++){
                            JSONObject tempI = DLJsonUtil.hasAndGetJsonObjectFromJsonArray(tempData,i);
                            if (tempI!=null){
                                LYChatItemModel tempItem = new LYChatItemModel();

                                JSONObject tempC = DLJsonUtil.hasAndGetJsonObject(tempI,"content");
                                if (tempC!=null) {
                                    tempItem.parseJsonObject(tempC);
                                }

                                tempItem.setStatus("2");

                                tempItem.setMsgId(DLJsonUtil.hasAndGetString(tempI,"id"));
                                tempItem.setUid(DLJsonUtil.hasAndGetString(tempI,"uid"));

                                data.add(tempItem);
                            }
                        }

                    }
                }

            }

        }catch (JSONException e){

        }
    }

    public ArrayList<LYChatItemModel> getData(){
        return data;
    }

    public boolean isHas_more() {
        return has_more;
    }

    public void setHas_more(boolean has_more) {
        this.has_more = has_more;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getNext_max() {
        return next_max;
    }

    public void setNext_max(String next_max) {
        this.next_max = next_max;
    }
}
