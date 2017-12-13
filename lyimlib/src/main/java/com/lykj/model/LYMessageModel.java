package com.lykj.model;

import com.dlbase.base.DLBaseModel;
import com.dlbase.util.DLJsonUtil;
import com.dlbase.util.DLStringUtil;

import org.json.JSONObject;

/**
 * Created by luyz on 2017/5/11.
 */

public class LYMessageModel extends DLBaseModel {

    private String type;
    private String userId;
    private String message;
    private String topicName;

    private LYChatItemModel chatModel;
    private LYUserModel userModel;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
        if (type!=null) {
            setMessageType(DLStringUtil.strToInt(type));
        }
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public LYChatItemModel getChatModel() {
        return chatModel;
    }

    public void setChatModel(LYChatItemModel chatModel) {
        this.chatModel = chatModel;
    }

    public LYUserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(LYUserModel userModel) {
        this.userModel = userModel;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }

    public class TMQTTMessageType{
        public static final int EMQTTMsg_Default = -1;
        public static final int EMQTTMsg_Publish = 0;//发送消息
        public static final int EMQTTMsg_99999 = 99999;//操作
        public static final int EMQTTMsg_10001 = 10001;//请求加好友
        public static final int EMQTTMsg_10002 = 10002;//通过好友
        public static final int EMQTTMsg_10003 = 10003;//赞
        public static final int EMQTTMsg_10004 = 10004;//评论
    }

    public class TMQTTOperation{
        public static final int EMQTTOper_Default = 0;
        public static final int EMQTTOper_Subscribe = 1;
        public static final int EMQTTOper_Unsubscribe = 2;
        public static final int EMQTTOper_Killout = 3;
    }

    private int messageType;
    private int operation;

    public LYMessageModel(){
        setMessageType(TMQTTMessageType.EMQTTMsg_Default);
        setOperation(TMQTTOperation.EMQTTOper_Default);
    }

    public void parseJson(JSONObject data){
        if (data!=null){
            JSONObject tempData = data;
            if (tempData!=null){

                setUserId(DLJsonUtil.hasAndGetString(tempData,"uid"));
                setType(DLJsonUtil.hasAndGetString(tempData,"type"));

                switch (messageType){
                    case TMQTTMessageType.EMQTTMsg_Publish:
                    {
                        LYChatItemModel tempItem = new LYChatItemModel();
                        tempItem.parseJsonObject(tempData);
                        setChatModel(tempItem);
                    }
                        break;
                    case TMQTTMessageType.EMQTTMsg_10001:
                    case TMQTTMessageType.EMQTTMsg_10002:
                    case TMQTTMessageType.EMQTTMsg_10003:
                    case TMQTTMessageType.EMQTTMsg_10004:
                    {
                        setMessage(DLJsonUtil.hasAndGetString(tempData,"message"));
                        LYUserModel tempUser = new LYUserModel();
                        tempUser.parseJsonDic(DLJsonUtil.hasAndGetJsonObject(tempData,"user"));
                        setUserModel(tempUser);
                    }
                        break;
                    case TMQTTMessageType.EMQTTMsg_99999:
                    {
                        handleMessage(DLJsonUtil.hasAndGetString(tempData,"message"));
                    }
                        break;
                    default:
                        break;
                }
            }
        }
    }

    public void handleMessage(String data){

        setMessage(data);

        String tempDD = "://";

        int tempRange = message.indexOf(tempDD);
        if (tempRange>-1){
            LYChatItemModel tempItem = new LYChatItemModel() ;

            String tempOp = message.substring(0,tempRange);
            String tempTopic = message.substring(tempRange+tempDD.length());

            if (DLStringUtil.notEmpty(tempOp)){
                if (tempOp.equals("subscribe")){
                    setOperation(TMQTTOperation.EMQTTOper_Subscribe);
                }else if (tempOp.equals("unsubscribe")){
                    setOperation(TMQTTOperation.EMQTTOper_Unsubscribe);
                }
            }

            if (DLStringUtil.notEmpty(tempTopic)){
                setTopicName(tempTopic);

                tempItem.setMessageType("0");

                int tempRange2 = tempTopic.indexOf("_");
                if (tempRange2>-1){
                    LYUserModel tempUser = new LYUserModel();
                    tempUser.setUserId(tempTopic.substring(tempRange2+1));
                    tempItem.setFromModel(tempUser);
                }
            }

            setChatModel(tempItem);
        }else{
            if (message.equals("kickout")){
                setOperation(TMQTTOperation.EMQTTOper_Killout);
            }
        }
    }
}
