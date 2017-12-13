package com.lykj.MQTT;

/**
 * Created by luyz on 2017/4/19.
 */

public interface LYMQTTEngineListener {

    public void messageArrived(String topic,String data);
    public void connectSuccess();
    public void connectFail();
    public void disconnectSuccess();
    public void disconnectFail();
    public void publishSuccess(String mqttMsgId);
    public void publishFail(String mqttMsgId);
    public void subscribeSuccess();
    public void subscribeFail();
    public void unsubscribeSuccess();
    public void unsubscribeFail();
    public void connectLost();
}
