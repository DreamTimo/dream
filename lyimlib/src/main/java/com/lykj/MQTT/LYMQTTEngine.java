package com.lykj.MQTT;

import android.os.Handler;
import android.os.Message;

import com.dlbase.util.DLMD5Util;
import com.dlbase.util.DLStringUtil;
import com.luyz.lyimlib.LYIMConfig;
import com.luyz.lyimlib.LyImEngine;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.HashMap;

/**
 * Created by luyz on 2017/4/19.
 */

public class LYMQTTEngine {

    private MqttAsyncClient mqttClient;
    private MqttConnectOptions mqttOptions;
    private LYMQTTEngineListener listener;

    private final static String CONNECTION_STRING = "tcp://"+LYIMConfig.KHost+":"+LYIMConfig.KPort;
//    private final static String CONNECTION_STRING = "tcp://"+ LYIMConfig.KHost;
//    private final static String CONNECTION_STRING = "https://"+ LYIMConfig.KHost+":"+LYIMConfig.KPort;

    private final static String LISTENER_CONNECT = "connect";
    private final static String LISTENER_DISCONNECT = "disconnect";
    private final static String LISTENER_PUBLISH = "publish";
    private final static String LISTENER_SUBSCRIBE = "subscribe";
    private final static String LISTENER_UNSUBSCRIBE = "unsubscribe";

    private final static int HANDLE_WART_ONSUCCESS = 0x400;
    private final static int HANDLE_WART_ONFAIL = 0x401;
    private final static int HANDLE_WART_MESSAGEARRIVED = 0x402;
    private final static int HANDLE_WART_CONNECTIONLOST = 0x403;

    private final static String MESSAGE_KEY_TOPIC = "topic";
    private final static String MESSAGE_KEY_DATA = "data";
    private final static String MESSAGE_KEY_MESSAGEID = "msgId";
    private final static String MESSAGE_KEY_USER = "user";

    public LYMQTTEngine(LYMQTTEngineListener mqttEngineListener){

        this.listener = mqttEngineListener;

    }

    public void setDefaultData(String clientId)
    {
        String userName = LyImEngine.getInstance().getUserName();
        String userPassword = LyImEngine.getInstance().getUserPwd();

        init(userName,userPassword, clientId);
    }

    private void init(String name,String password,String clientId){

        try {
            mqttClient = new MqttAsyncClient(CONNECTION_STRING, clientId,new MemoryPersistence());
        } catch (MqttException e) {
            e.printStackTrace();
        }

        mqttOptions = new MqttConnectOptions();

        mqttOptions.setUserName(name);
        mqttOptions.setPassword(password.toCharArray());

        mqttClient.setCallback(new MqttCallback() {

            @Override
            public void messageArrived(String arg0, MqttMessage arg1) throws Exception {
                // TODO Auto-generated method stub
                //subscribe后得到的消息会执行到这里
                System.out.println("messageArrived："+arg0+"\n"+arg1.toString());

                HashMap<String, String> msgObj = new HashMap<String, String>();
                msgObj.put(MESSAGE_KEY_TOPIC, arg0+"");
                msgObj.put(MESSAGE_KEY_DATA, arg1.toString()+"");

                Message msg = new Message();
                msg.what = HANDLE_WART_MESSAGEARRIVED;
                msg.obj = msgObj;
                mMQTTHandle.sendMessage(msg);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken arg0) {
                // TODO Auto-generated method stub
                //public 后执行这里
                System.out.println("deliveryComplete："+arg0.isComplete());

            }

            @Override
            public void connectionLost(Throwable arg0) {
                // TODO Auto-generated method stub
                //连接后丢失 处理重新连接
                System.out.println("connectionLost");

                Message msg = new Message();
                msg.what = HANDLE_WART_CONNECTIONLOST;
                mMQTTHandle.sendMessage(msg);
            }
        });

    }

    private IMqttActionListener actionListener = new IMqttActionListener() {

        @Override
        public void onSuccess(IMqttToken arg0) {
            // TODO Auto-generated method stub
            System.out.println(arg0.getUserContext()+":success--"+arg0.getMessageId());
            if(arg0.getUserContext()!=null){

                HashMap<String, String> msgObj = new HashMap<String, String>();
                msgObj.put(MESSAGE_KEY_MESSAGEID, arg0.getMessageId()+"");
                msgObj.put(MESSAGE_KEY_USER, arg0.getUserContext()+"");

                Message msg = new Message();
                msg.what = HANDLE_WART_ONSUCCESS;
                msg.obj  = msgObj;
                mMQTTHandle.sendMessage(msg);
            }
        }

        @Override
        public void onFailure(IMqttToken arg0, Throwable arg1) {
            // TODO Auto-generated method stub
            System.out.println(arg0.getUserContext()+":fail--"+arg1.getMessage());

            if(arg0.getUserContext()!=null){

                HashMap<String, String> msgObj = new HashMap<String, String>();
                msgObj.put(MESSAGE_KEY_MESSAGEID, arg0.getMessageId()+"");
                msgObj.put(MESSAGE_KEY_USER, arg0.getUserContext()+"");

                Message msg = new Message();
                msg.what = HANDLE_WART_ONFAIL;
                msg.obj  = msgObj;
                mMQTTHandle.sendMessage(msg);
            }
        }
    };

    private Handler mMQTTHandle = new Handler(){
        @SuppressWarnings("unchecked")
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case HANDLE_WART_ONSUCCESS:{
                    HashMap<String, String> tempData = (HashMap<String, String>)msg.obj;
                    if(tempData!=null){
                        String tempUser = tempData.get(MESSAGE_KEY_USER);
                        String tempMsgId = tempData.get(MESSAGE_KEY_MESSAGEID);
                        if(DLStringUtil.notEmpty(tempUser)){
                            if(tempUser.equals(LISTENER_CONNECT)){
                                if(listener!=null){
                                    listener.connectSuccess();
                                }
                            }else if(tempUser.equals(LISTENER_DISCONNECT)){
                                if(listener!=null){
                                    listener.disconnectSuccess();
                                }
                            }else if(tempUser.equals(LISTENER_SUBSCRIBE)){
                                if(listener!=null){
                                    listener.subscribeSuccess();
                                }
                            }else if(tempUser.equals(LISTENER_UNSUBSCRIBE)){
                                if(listener!=null){
                                    listener.unsubscribeSuccess();
                                }
                            }else if(tempUser.equals(LISTENER_PUBLISH)){
                                if(listener!=null){
                                    listener.publishSuccess(tempMsgId);
                                }
                            }
                        }
                    }
                }
                break;
                case HANDLE_WART_ONFAIL:{
                    HashMap<String, String> tempData = (HashMap<String, String>)msg.obj;
                    if(tempData!=null){
                        String tempUser = tempData.get(MESSAGE_KEY_USER);
                        String tempMsgId = tempData.get(MESSAGE_KEY_MESSAGEID);
                        if(DLStringUtil.notEmpty(tempUser)){
                            if(tempUser.equals(LISTENER_CONNECT)){
                                if(listener!=null){
                                    listener.connectFail();
                                }
                            }else if(tempUser.equals(LISTENER_DISCONNECT)){
                                if(listener!=null){
                                    listener.disconnectFail();
                                }
                            }else if(tempUser.equals(LISTENER_SUBSCRIBE)){
                                if(listener!=null){
                                    listener.subscribeFail();
                                }
                            }else if(tempUser.equals(LISTENER_UNSUBSCRIBE)){
                                if(listener!=null){
                                    listener.unsubscribeFail();
                                }
                            }else if(tempUser.equals(LISTENER_PUBLISH)){
                                if(listener!=null){
                                    listener.publishFail(tempMsgId);
                                }
                            }
                        }
                    }
                }
                break;
                case HANDLE_WART_MESSAGEARRIVED:{
                    HashMap<String, String> tempData = (HashMap<String, String>)msg.obj;
                    if(tempData!=null){
                        if(listener!=null){
                            listener.messageArrived(tempData.get(MESSAGE_KEY_TOPIC), tempData.get(MESSAGE_KEY_DATA));
                        }
                    }
                }
                break;
                case HANDLE_WART_CONNECTIONLOST:{
                    connect();
                }
                break;
                default:
                    break;
            }

            super.handleMessage(msg);
        }
    };

    public void connect(){
        try {
            mqttClient.connect(mqttOptions,LISTENER_CONNECT,actionListener);
        } catch (MqttSecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (MqttException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void disConnect(){
        try {
            mqttClient.disconnect(LISTENER_DISCONNECT,actionListener);
        } catch (MqttException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
        }
    }

    public void subscribe(String[] topicName, int[] qos){

        if(mqttClient.isConnected()){

            try {
                //DLLogUtil.d("subscribe:"+topicName+"--"+qos);

                mqttClient.subscribe(topicName, qos,LISTENER_SUBSCRIBE,actionListener);
            } catch (MqttException e) {
                // TODO Auto-generated catch block
                //e.printStackTrace();
            }
        }
    }

    public void unsubscribe(String[] topicFilter){
        if(mqttClient.isConnected()){
            try {
                mqttClient.unsubscribe(topicFilter,LISTENER_UNSUBSCRIBE,actionListener);
            } catch (MqttException e) {
                // TODO Auto-generated catch block
                //e.printStackTrace();
            }
        }
    }

    public String publish(String topicName, int qos, byte[] payload){

        String mqttMsgId = null;

        if(mqttClient.isConnected()){
            try {
                MqttMessage message = new MqttMessage(payload);
                message.setQos(qos);
                IMqttDeliveryToken token = mqttClient.publish(topicName, message,LISTENER_PUBLISH,actionListener);
                mqttMsgId = token.getMessageId()+"";
            } catch (MqttPersistenceException e) {
                // TODO Auto-generated catch block
                //e.printStackTrace();
            } catch (MqttException e) {
                // TODO Auto-generated catch block
                //e.printStackTrace();
            }
        }
        return mqttMsgId;
    }

    /**
     * 是否连接
     * @return
     */
    public boolean isConnected(){
        if(mqttClient == null){
            return false;
        }
        return mqttClient.isConnected();
    }
}
