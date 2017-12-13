package com.lykj.MQTT;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.dlbase.base.DLBaseActivity;
import com.dlbase.util.DLArrayUtil;
import com.dlbase.util.DLDateUtils;
import com.dlbase.util.DLJsonUtil;
import com.dlbase.util.DLLogUtil;
import com.dlbase.util.DLShowDialog;
import com.dlbase.util.DLStringUtil;
import com.luyz.lyimlib.LyImEngine;
import com.lykj.Bean.LYChatBean;
import com.lykj.model.LYChatItemModel;
import com.lykj.model.LYChatListModel;
import com.lykj.model.LYMessageModel;
import com.lykj.model.LYTopisItemModel;
import com.lykj.model.LYUserModel;
import com.lykj.util.LYSharedUtil;
import com.lykj.util.ToolsUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by luyz on 2017/4/19.
 */

public class LYMQTTHelper {

    private LYMQTTEngine mqttEngine;

    private LYMessageHelper messageHelper;

    private onChatPublishListener charPublishListener;
    private onChatConnectListener chatConnectListener;
    private onChatMessageListener chatMessageListener;
    private onChatListListener chatListListener;

    public static LYMQTTHelper instance;
    public static LYMQTTHelper getInstance() {
        if (instance == null) {
            instance = new LYMQTTHelper();
        }
        return instance;
    }

    public LYMQTTHelper(){

        mqttEngine = new LYMQTTEngine(mqttListener);
        messageHelper = new LYMessageHelper();
    }

    public void connect(String clientId,onChatConnectListener listener){
        setChatConnectListener(listener);
        mqttEngine.setDefaultData(clientId);
        mqttEngine.connect();
    }

    public void disconnect()
    {
        mqttEngine.disConnect();
    }

    public Handler mHander = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);

            if(msg.what == 902){

                if (LyImEngine.getInstance().getLyimListener()!=null){
                    LyImEngine.getInstance().getLyimListener().onLogout();
                }
            }else if (msg.what == 800){
                String limit = (String)msg.obj;
                getOffLineMessage(limit);
            }
        }

    };

    public  void sendMessage(final Context context,final LYChatItemModel model, final onChatPublishListener listener){

        if(model!=null){

            String tempStr = publish(model,new onChatPublishListener() {

                @Override
                public void publishSuccess(String msgId) {
                    // TODO Auto-generated method stub
                    //0 未发送 1 正在发送 2 已发送 3 发送失败
                    model.setStatus("2");
                    if(LyImEngine.getInstance().getDbHelper()!=null){
                        LyImEngine.getInstance().getDbHelper().getCharTable().updateCharStatus(model.getToId(), model);
                    }
                    sendMessageFinish(context,listener,true);
                }

                @Override
                public void publishFail(String msgId) {
                    // TODO Auto-generated method stub
                    model.setStatus("3");
                    if(LyImEngine.getInstance().getDbHelper()!=null){
                        LyImEngine.getInstance().getDbHelper().getCharTable().updateCharStatus(model.getToId(), model);
                    }
                    sendMessageFinish(context,listener,false);
                }
            });

            model.setMqttMsgId(tempStr);

            if(LyImEngine.getInstance().getDbHelper()!=null){
                LyImEngine.getInstance().getDbHelper().getCharTable().checkAndInsertCharData(model.getToId(), model);
            }
            insertAndUpdateCharList(model);
        }
    }

    public void sendMessageFinish(Context context, final onChatPublishListener listener, final boolean success){

        ((DLBaseActivity) context).runOnUiThread(new Runnable(){

            @Override
            public void run() {
                // TODO Auto-generated method stub
                if(listener!=null){
                    if(success){
                        listener.publishSuccess(null);
                    }else{
                        listener.publishFail(null);
                    }
                }
            }
        });
    }

    private String publish(LYChatItemModel model,onChatPublishListener listener)
    {
        if(LyImEngine.getInstance().getDbHelper() == null){
            model.setStatus("3");
            if(listener!=null){
                listener.publishFail(null);
            }
            return null;
        }

        LYTopisItemModel tempTopic = LyImEngine.getInstance().getDbHelper().getMqttTable().queryMQTTWhereTargetId(model.getToId(),model.getUid());

        if(tempTopic!=null && mqttEngine!=null){

            charPublishListener = listener ;

            String tempStr = model.getSendMessageJson();

            DLLogUtil.d("MQTT publish:"+tempTopic+"\n--data:"+tempStr);

            byte[] payload = tempStr.getBytes();

            return mqttEngine.publish(tempTopic.getName(), DLStringUtil.strToInt(tempTopic.getQos()), payload);
        }else{
            model.setStatus("3");

            LyImEngine.getInstance().getDbHelper().getCharTable().updateCharStatus(model.getToId(), model);

            if(listener!=null){
                listener.publishFail(null);
            }
        }

        return null;
    }

    public void subscribe()
    {
        ArrayList<LYTopisItemModel> tempArray = LyImEngine.getInstance().getDbHelper().getMqttTable().queryAllMQTT();

        if(DLArrayUtil.notEmpty(tempArray)){

            int count  = tempArray.size();
            String[] topics = new String[count] ;
            int[] qoss = new int[count] ;
            for (int i = 0; i < count; i++) {
                LYTopisItemModel tempItem = tempArray.get(i);
                if(tempItem!=null){
                    topics[i] = tempItem.getName();
                    qoss[i]   = DLStringUtil.strToInt(tempItem.getQos());
                }
            }

            subscribe(topics, qoss);
        }
    }

    private void subscribe(String[] topics,int[] qoss){
        if(topics!=null && qoss!=null && mqttEngine!=null){
            mqttEngine.subscribe(topics, qoss);
        }
    }

    public void unsubscribe(String[] topics){
        if(topics!=null && mqttEngine!=null){
            mqttEngine.unsubscribe(topics);
        }
    }

    public LYMQTTEngineListener mqttListener = new LYMQTTEngineListener() {
        @Override
        public void messageArrived(String topic, String data) {
            handleMessage(topic,data);
        }

        @Override
        public void connectSuccess() {
            if (chatConnectListener!=null){
                chatConnectListener.connectSuccess();
            }

            subscribe();

            Message msg = new Message();
            msg.what = 800;
            msg.obj = "10";
            mHander.sendMessage(msg);
        }

        @Override
        public void connectFail() {
            if (chatConnectListener!=null){
                chatConnectListener.connectFail();
            }
        }

        @Override
        public void disconnectSuccess() {

        }

        @Override
        public void disconnectFail() {

        }

        @Override
        public void publishSuccess(String mqttMsgId) {
            if (charPublishListener!=null){
                charPublishListener.publishSuccess(mqttMsgId);
            }
        }

        @Override
        public void publishFail(String mqttMsgId) {
            if (charPublishListener!=null){
                charPublishListener.publishFail(mqttMsgId);
            }
        }

        @Override
        public void subscribeSuccess() {

        }

        @Override
        public void subscribeFail() {

        }

        @Override
        public void unsubscribeSuccess() {

        }

        @Override
        public void unsubscribeFail() {

        }

        @Override
        public void connectLost() {

        }
    };

    private void handleMessage(String topic,String data){

        if(LyImEngine.getInstance().getDbHelper() == null){
            return;
        }
        try {
            JSONObject tempD = new JSONObject(data);
            if (tempD!=null){
                JSONObject tempData = DLJsonUtil.hasAndGetJsonObject(tempD,"data");
                if (tempData!=null){
                    LYMessageModel tempMsg = new LYMessageModel();
                    tempMsg.parseJson(tempData);

                    if (tempMsg.getMessageType() == LYMessageModel.TMQTTMessageType.EMQTTMsg_Publish){
                        //消息
                        handlerUserMessage(tempMsg);
                    }else{
                        //系统消息
                        handlerSystemMessage(tempMsg);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String getUserId(LYChatItemModel model){
        String tempUserId = null;

        if (model!=null) {

            String userKey = LyImEngine.getInstance().getUserId();
            model.setUid(userKey);

            if (model.getMessageType().equals("0")) {
                if (userKey.equals(model.getFromModel().getUserId())) {
                    tempUserId = model.getToId();
                } else {
                    tempUserId = model.getFromModel().getUserId();
                }
            } else if (model.getMessageType().equals("1")) {

                tempUserId = model.getToId();
            }
        }

        return tempUserId;
    }

    private void newMessageForSetting(){
        ToolsUtil.newMessageForSetting();
    }

    private void handleVoiceMsg(LYChatItemModel model,String userId){

    }

    private void handlerUserMessage(LYMessageModel data){

        LYChatItemModel tempChat =  data.getChatModel();
        if(tempChat!=null){
            //发送成功
            tempChat.setStatus("2");

            boolean hasmsg = false;

            String tempUserId = getUserId(tempChat);

            if(tempChat.getFromModel()!=null){
                if(!LyImEngine.getInstance().getUserId().equals(tempChat.getFromModel().getUserId())){
                    newMessageForSetting();
                }else{
                    hasmsg = true;
                    tempChat.setIsfrom(true);
                    if(tempChat.getChatType() == LYChatItemModel.TChatType.EChat_File){
                        tempChat.getFileModel().setDownState(3);
                    }else if (tempChat.getChatType() == LYChatItemModel.TChatType.EChat_Voice){
                        handleVoiceMsg(tempChat,tempUserId);
                    }
                }
            }

            LYSharedUtil.setOffline_max(LyImEngine.getInstance().getmContext(),LyImEngine.getInstance().getUserId(),tempChat.getMsgId());

            LyImEngine.getInstance().getDbHelper().getCharTable().checkAndInsertCharData(tempUserId,tempChat);

            getLastItemToChatList(tempChat,tempUserId);

            if (hasmsg){
                if (chatMessageListener!=null){
                    chatMessageListener.toSendMessage(tempChat);
                }
            }else{

                if (chatMessageListener!=null){
                    chatMessageListener.toReceiveMessage(tempChat);
                }

                if (chatListListener!=null){
                    chatListListener.toUpdateChatList();
                }
            }

        }
    }

    private void getLastItemToChatList(LYChatItemModel model,String userId){
        LYChatItemModel tempLastItem = LyImEngine.getInstance().getDbHelper().getCharTable().queryLastItem(userId,model.getUid(),model.getMessageType());
        if(tempLastItem!=null){
            insertAndUpdateCharList(tempLastItem);
        }else{
            insertAndUpdateCharList(model);
        }
    }

    private void handlerSystemMessage(LYMessageModel data){
        if (data!=null){
            if (data.getMessageType()== LYMessageModel.TMQTTMessageType.EMQTTMsg_99999){

                if (data.getOperation() == LYMessageModel.TMQTTOperation.EMQTTOper_Subscribe){

                    LYTopisItemModel tempModel = new LYTopisItemModel();
                    tempModel.setName(data.getTopicName());
                    tempModel.setUserId(LyImEngine.getInstance().getUserId());
                    tempModel.setTarget_id(data.getChatModel().getFromModel().getUserId());

                    LyImEngine.getInstance().getDbHelper().getMqttTable().checkAndInsertMQTT(tempModel);

                    subscribe();

                    toCharListSystemData(data.getChatModel().getFromModel().getUserId(),true);

                }else if (data.getOperation() == LYMessageModel.TMQTTOperation.EMQTTOper_Unsubscribe){

                    LYTopisItemModel tempModel = new LYTopisItemModel();
                    tempModel.setName(data.getTopicName());
                    tempModel.setUserId(LyImEngine.getInstance().getUserId());
                    tempModel.setTarget_id(data.getChatModel().getFromModel().getUserId());

                    LyImEngine.getInstance().getDbHelper().getMqttTable().deleteMQTT(data.getTopicName());

                    unsubscribe(new String[]{data.getTopicName()});

                    toCharListSystemData(data.getChatModel().getFromModel().getUserId(),false);

                }else if (data.getOperation() == LYMessageModel.TMQTTOperation.EMQTTOper_Killout){
                    disconnect();
                    toKillout();
                }
            }
        }
    }

    private void toCharListSystemData(String userId, final boolean sub){

        if (LyImEngine.getInstance().getLyimListener()!=null){
            LyImEngine.getInstance().getLyimListener().onGetUserInfo(userId, new LyImEngine.onUserInfoListener() {
                @Override
                public void onGetUserInfo(LYUserModel model) {
                if (model!=null){
                    LYChatItemModel tempMode = new LYChatItemModel();
                    tempMode.setMessageType("0");
                    tempMode.setUid(LyImEngine.getInstance().getUserId());
                    tempMode.setFromModel(model);
                    tempMode.setToUserModel(LyImEngine.getInstance().getUserModel());
                    tempMode.setChatType(LYChatItemModel.TChatType.EChat_System);
                    tempMode.setChatTimer(DLDateUtils.getCurrentTimeInLong()/1000+"");
                    if (sub) {
                        tempMode.setChatContent(model.getUserName() + "已和你成为好友，现在可以开始聊天了。");
                    }else{
                        tempMode.setChatContent("你们已经不是好友了。");
                    }
                    LyImEngine.getInstance().getDbHelper().getCharTable().checkAndInsertCharData(model.getUserId(), tempMode);
                    insertAndUpdateCharList(tempMode);
                }
                }
            });
        }
    }

    private void toKillout(){
        Log.d("killout","killout");

        mHander.sendEmptyMessage(902);
    }

    public void insertAndUpdateCharList(LYChatItemModel model){

        if(model == null || LyImEngine.getInstance().getDbHelper()==null){
            return;
        }

        LYChatListModel tempListItem = new LYChatListModel();
        boolean hasMe = false;
        String fromId = null;
        if(model.getMessageType().equals("0")){
            if(LyImEngine.getInstance().getUserId().equals(model.getFromModel().getUserId())){
                tempListItem.setFromUserModel(model.getToUserModel());
                hasMe = true;
                fromId = model.getToUserModel().getUserId();
            }else{
                tempListItem.setFromUserModel(model.getFromModel());
                fromId = model.getFromModel().getUserId();
            }
        }else if(model.getMessageType().equals("1")){
            if(LyImEngine.getInstance().getUserId().equals(model.getFromModel().getUserId())){
                hasMe = true;
            }
            fromId = model.getToGroupModel().getGroupId();
            tempListItem.setFromGroupModel(model.getToGroupModel());
        }

        tempListItem.setMessageType(model.getMessageType());
        tempListItem.setLastItemModel(model);
        tempListItem.setUid(LyImEngine.getInstance().getUserId());

        if(!hasMe){
            if (LyImEngine.getInstance().getCurrChatActivity()!=null){
                tempListItem.setNewCount(0);
            }else {
                int count = LyImEngine.getInstance().getDbHelper().getCharListTable().queryCharListToNewCount(fromId, model.getMessageType());
                count++;
                tempListItem.setNewCount(count);
            }
        }

        LyImEngine.getInstance().getDbHelper().getCharListTable().checkAndInsertCharListData(tempListItem);
    }

    public void getOffLineMessage(String limit){

        String tempMax = LYSharedUtil.getOffline_max(LyImEngine.getInstance().getmContext(),LyImEngine.getInstance().getUserId());

        if (LyImEngine.getInstance().getLyimListener()!=null){
            LyImEngine.getInstance().getLyimListener().onGetOfflineMessage(LyImEngine.getInstance().getUserId(), limit, tempMax, new LyImEngine.onOffLineMsgListener() {
                @Override
                public void onOffLineMsg(String data) {

                    if (DLStringUtil.notEmpty(data)) {
                        LYChatBean tempBean = new LYChatBean();
                        tempBean.parseJson(data);

                        LYSharedUtil.setOffline_max(LyImEngine.getInstance().getmContext(),LyImEngine.getInstance().getUserId(),tempBean.getNext_max());

                        if (tempBean.getData()!=null) {
                            for (int i = 0; i < tempBean.getData().size(); i++) {
                                LYChatItemModel tempItem = tempBean.getData().get(i);
                                if (tempItem!=null){

                                    String tempUserId = getUserId(tempItem);

                                    LyImEngine.getInstance().getDbHelper().getCharTable().checkAndInsertCharData(tempUserId,tempItem);

                                    getLastItemToChatList(tempItem,tempUserId);
                                }
                            }
                        }

                        if (tempBean.isHas_more()){
                            Message msg = new Message();
                            msg.what = 800;
                            msg.obj = "99999";
                            mHander.sendMessage(msg);
                        }
                    }
                }
            });
        }
    }

    public onChatConnectListener getChatConnectListener() {
        return chatConnectListener;
    }

    public void setChatConnectListener(onChatConnectListener chatConnectListener) {
        this.chatConnectListener = chatConnectListener;
    }

    public onChatPublishListener getCharPublishListener() {
        return charPublishListener;
    }

    public void setCharPublishListener(onChatPublishListener charPublishListener) {
        this.charPublishListener = charPublishListener;
    }

    public onChatMessageListener getChatMessageListener() {
        return chatMessageListener;
    }

    public void setChatMessageListener(onChatMessageListener chatMessageListener) {
        this.chatMessageListener = chatMessageListener;
    }

    public LYMessageHelper getMessageHelper() {
        return messageHelper;
    }

    public void setMessageHelper(LYMessageHelper messageHelper) {
        this.messageHelper = messageHelper;
    }

    public onChatListListener getChatListListener() {
        return chatListListener;
    }

    public void setChatListListener(onChatListListener chatListListener) {
        this.chatListListener = chatListListener;
    }

    public interface onChatPublishListener {
        public void publishSuccess(String msgId);
        public void publishFail(String msgId);
    }

    public interface onChatConnectListener {
        public void connectSuccess();
        public void connectFail();
    }

    public interface onChatMessageListener{
        public void toSendMessage(LYChatItemModel model);
        public void toReceiveMessage(LYChatItemModel model);
    }

    public interface onChatListListener{
        public void toUpdateChatList();
    }
}
