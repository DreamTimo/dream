package com.lykj.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dlbase.util.DLStringUtil;
import com.lykj.model.LYTopisItemModel;

import java.util.ArrayList;

/**
 * Created by luyz on 2017/5/12.
 */

public class LYMQTTTable {

    private SQLiteDatabase db;

    //MQTT和USER关系表
    public static final String TABLE_MQTTANDUSER = "mqtt_user_table";
    public static final String MQTT_USERID = "userId";
    public static final String MQTT_ID = "id";
    public static final String MQTT_NAME = "name";
    public static final String MQTT_QOS = "qos";
    public static final String MQTT_TARGETID = "target_id";
    public static final String MQTT_CREATETIME = "create_time";
    public static final String MQTT_AUTOID = "auto_id";
    public static final String MQTT_TYPE = "type";

    public LYMQTTTable(SQLiteDatabase sdb){
        db=sdb;
    }


    public static String getCreateMQTTTable(){
        return LYDBHelper.CREATETABLESQL
                + TABLE_MQTTANDUSER
                + LYDBHelper.TABLELEFTSQL
                + MQTT_AUTOID       + LYDBHelper.TABLEAUTOSQLKEY
                + MQTT_ID           + LYDBHelper.TABLECHARSQLKEY
                + MQTT_USERID       + LYDBHelper.TABLECHARNOTNULLSQLKEY
                + MQTT_NAME         + LYDBHelper.TABLECHARNOTNULLSQLKEY
                + MQTT_QOS          + LYDBHelper.TABLEINTEGERSQLKEY
                + MQTT_CREATETIME   + LYDBHelper.TABLEINTEGERSQLKEY
                + MQTT_TYPE         + LYDBHelper.TABLEINTEGERSQLKEY
                + MQTT_TARGETID     + LYDBHelper.TABELCHARLASTSQLKEY
                + LYDBHelper.TABLERIGHTSQL;
    }

    /**
     * 检查并添加或更新MQTT数据
     * @param model
     * @return
     */
    public boolean checkAndInsertMQTT(LYTopisItemModel model){


        if(model==null || db == null){
            return false;
        }

        boolean result = false;

        if(queryMQTTById(model)){
            result = updateMQTT(model);
        }else{
            result = insertMQTT(model);
        }

        return result;
    }

    /**
     * 删除MQTT
     * @return
     */
    public boolean deleteMQTT(String topicName){

        if(db == null){
            return false;
        }

        return db.delete(TABLE_MQTTANDUSER,
                MQTT_NAME + "='" + topicName + "'",
                null) > 0;
    }

    /**
     * 删除所有人MQTT
     * @return
     */
    public boolean deleteAllMQTT(){
        if(db == null){
            return false;
        }
        return db.delete(TABLE_MQTTANDUSER, null, null) > 0;
    }

    private ContentValues getDataDic(LYTopisItemModel model){

        ContentValues initialValues = new ContentValues();
        initialValues.put(MQTT_USERID, model.getUserId());
        initialValues.put(MQTT_CREATETIME, model.getCreatetime());
        initialValues.put(MQTT_ID, model.getId());
        initialValues.put(MQTT_TARGETID, model.getTarget_id());
        initialValues.put(MQTT_NAME, model.getName());
        initialValues.put(MQTT_QOS, DLStringUtil.strToInt(model.getQos()));

        initialValues.put(MQTT_TYPE, DLStringUtil.strToInt(model.getType()));

        return initialValues;
    }

    /**
     * 插入MQTT
     * @return
     */
    private boolean insertMQTT(LYTopisItemModel model){

        if(model==null || db == null){
            return false;
        }

        return db.insert(TABLE_MQTTANDUSER, null, getDataDic(model)) > 0;
    }

    /**
     * 更新MQTT
     * @return
     */
    private boolean updateMQTT(LYTopisItemModel model){

        if(model==null || db == null){
            return false;
        }

        return db.update(TABLE_MQTTANDUSER,
                getDataDic(model),
                getWhereStr(model),
                null)>0;
    }

    /**
     * 检索MQTT记录是否存在
     * @return
     */
    public boolean queryMQTTById(LYTopisItemModel model){

        if(db == null){
            return false;
        }

        boolean result = false;

        Cursor cursor = db.query(TABLE_MQTTANDUSER,
                getQueryDic(),
                getWhereStr(model),
                null, null, null, null);

        if(cursor!=null){
            if(cursor.moveToFirst()){
                result = true;
            }
        }
        cursor.close();

        return result;
    }

    private String getWhereStr(LYTopisItemModel model){
        return  MQTT_NAME   + "='" + model.getName()    + "' and " +
                MQTT_USERID + "='" + model.getUserId()  + "'";
    }


    /**
     * 查询全部MQTT数据
     * @return
     */
    public ArrayList<LYTopisItemModel> queryAllMQTT(){
        ArrayList<LYTopisItemModel> result = new ArrayList<LYTopisItemModel>();

        if(db == null){
            return result;
        }

        Cursor  cursor = db.query(TABLE_MQTTANDUSER,
                getQueryDic(),
                null, null, null, null, null);

        if(cursor == null){
            return null;
        }

        while (cursor.moveToNext()) {

            result.add(getItemModel(cursor));
        }
        cursor.close();
        return result;
    }

    private LYTopisItemModel getItemModel( Cursor cursor){

        LYTopisItemModel tempItem = new LYTopisItemModel();
        tempItem.setId(cursor.getString(cursor.getColumnIndex(MQTT_ID)));
        tempItem.setUserId(cursor.getString(cursor.getColumnIndex(MQTT_USERID)));
        tempItem.setTarget_id(cursor.getString(cursor.getColumnIndex(MQTT_TARGETID)));
        tempItem.setName(cursor.getString(cursor.getColumnIndex(MQTT_NAME)));
        tempItem.setQos(""+cursor.getInt(cursor.getColumnIndex(MQTT_QOS)));
        tempItem.setCreatetime(cursor.getString(cursor.getColumnIndex(MQTT_CREATETIME)));
        tempItem.setType(""+cursor.getInt(cursor.getColumnIndex(MQTT_TYPE)));

        return tempItem;
    }

    /**
     * 根据Name获取MQTT数据
     * @param topicName
     * @return
     */
    public LYTopisItemModel queryMQTTWhereName(String topicName,String userId){

        LYTopisItemModel result= null;

        if(db == null){
            return result;
        }

        Cursor  cursor = db.query(TABLE_MQTTANDUSER,
                getQueryDic(),
                MQTT_NAME + "='" + topicName + "' and " +
                        MQTT_USERID + "='" + userId + "'", null, null, null, null);

        if(cursor == null){
            return null;
        }

        while (cursor.moveToNext()) {
            result = getItemModel(cursor);
        }
        cursor.close();
        return result;
    }

    /**
     * 根据targetId获取MQTT数据
     * @param targetId
     * @return
     */
    public LYTopisItemModel queryMQTTWhereTargetId(String targetId,String userId){

        LYTopisItemModel result= null;

        if(db == null){
            return result;
        }

        Cursor  cursor = db.query(TABLE_MQTTANDUSER,
                getQueryDic(),
                MQTT_TARGETID + "='" + targetId + "' and " +
                        MQTT_USERID + "='" + userId + "'", null, null, null, null);

        if(cursor == null){
            return null;
        }

        while (cursor.moveToNext()) {
            result = getItemModel(cursor);
        }
        cursor.close();

        return result;
    }

    private String[] getQueryDic(){
        return new String[]{MQTT_ID, MQTT_USERID, MQTT_TARGETID,
                MQTT_NAME, MQTT_QOS, MQTT_TYPE, MQTT_CREATETIME};
    }

}
