package com.lykj.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dlbase.Model.DLFileModel;
import com.dlbase.Model.DLImageModel;
import com.dlbase.Model.DLLocationModel;
import com.dlbase.Model.DLVideoModel;
import com.dlbase.Model.DLVoiceModel;
import com.dlbase.util.DLStringUtil;
import com.luyz.lyimlib.LyImEngine;
import com.lykj.model.LYChatItemModel;
import com.lykj.model.LYGroupItemModel;
import com.lykj.model.LYProjectModel;
import com.lykj.model.LYUserModel;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by luyz on 2017/5/12.
 */

public class LYCharTable {

    private SQLiteDatabase db;
    private LYDBHelper fashionLYDBHelper;


    //聊天记录表
    public static final String TABLE_CHAR = "char_table";
    public static final String CHAR_TIME = "char_time";
    public static final String CHAR_TYPE = "char_type";
    public static final String CHAR_CONTENT = "char_content";
    public static final String CHAR_TOID = "to_id";
    public static final String CHAR_TOAVATAR = "to_avatar";
    public static final String CHAR_TONAME = "to_name";
    public static final String CHAR_CHARID = "char_id";
    public static final String CHAR_AUTOID = "auto_id";
    public static final String CHAR_MESSAGETYPE = "message_type";
    public static final String CHAR_STATUS = "char_status";
    public static final String CHAR_FROMID = "from_id";
    public static final String CHAR_FROMAVATAR = "from_avatar";
    public static final String CHAR_FROMNAME = "from_name";
    public static final String CHAR_MQTTMSGID = "mqtt_msgid";
    public static final String CHAR_TOTYPE = "to_type";
    public static final String CHAR_FROMTYPE = "from_type";
    public static final String CHAR_VOICELENGHT = "voicelenght";
    public static final String CHAR_FILEURL = "fileurl";
    public static final String CHAR_FILEPATH = "file_path";
    public static final String CHAR_FILESIZE = "filesie";
    public static final String CHAR_FILENAME = "filename";
    public static final String CHAR_FILESTATE = "filestate";
    public static final String CHAR_LATANDLON = "lat_lng";
    public static final String CHAR_CARDID = "card_id";
    public static final String CHAR_CARDNAME = "card_name";
    public static final String CHAR_CARDTYPE = "card_type";
    public static final String CHAR_CARDAVATAR = "card_avatar";
    public static final String CHAR_UID = "char_uid";
    public static final String CHAR_MSGID = "char_msgid";

    public LYCharTable(LYDBHelper sdb){
        fashionLYDBHelper=sdb;
        db = fashionLYDBHelper.getReadableDatabase();
    }


    public static String getCreateCharTable(String tableName){
        return LYDBHelper.CREATETABLESQL
                + tableName
                + LYDBHelper.TABLELEFTSQL
                + CHAR_AUTOID       + LYDBHelper.TABLEAUTOSQLKEY
                + CHAR_FROMID       + LYDBHelper.TABLECHARNOTNULLSQLKEY
                + CHAR_FROMNAME     + LYDBHelper.TABLECHARSQLKEY
                + CHAR_FROMAVATAR   + LYDBHelper.TABLECHARSQLKEY
                + CHAR_TOID         + LYDBHelper.TABLECHARNOTNULLSQLKEY
                + CHAR_TONAME       + LYDBHelper.TABLECHARSQLKEY
                + CHAR_TOAVATAR     + LYDBHelper.TABLECHARSQLKEY
                + CHAR_TIME         + LYDBHelper.TABLECHARSQLKEY
                + CHAR_CHARID       + LYDBHelper.TABLECHARSQLKEY
                + CHAR_TYPE         + LYDBHelper.TABLEINTEGERSQLKEY
                + CHAR_CONTENT      + LYDBHelper.TABLECHARSQLKEY
                + CHAR_MESSAGETYPE  + LYDBHelper.TABLEINTEGERSQLKEY
                + CHAR_STATUS       + LYDBHelper.TABLEINTEGERSQLKEY
                + CHAR_FILEPATH     + LYDBHelper.TABLECHARSQLKEY
                + CHAR_VOICELENGHT  + LYDBHelper.TABLECHARSQLKEY
                + CHAR_FILEURL      + LYDBHelper.TABLECHARSQLKEY
                + CHAR_FILENAME     + LYDBHelper.TABLECHARSQLKEY
                + CHAR_FILESIZE     + LYDBHelper.TABLECHARSQLKEY
                + CHAR_CARDID       + LYDBHelper.TABLECHARSQLKEY
                + CHAR_CARDNAME     + LYDBHelper.TABLECHARSQLKEY
                + CHAR_CARDAVATAR   + LYDBHelper.TABLECHARSQLKEY
                + CHAR_LATANDLON    + LYDBHelper.TABLECHARSQLKEY
                + CHAR_MQTTMSGID    + LYDBHelper.TABLECHARSQLKEY
                + CHAR_TOTYPE       + LYDBHelper.TABLECHARSQLKEY
                + CHAR_FROMTYPE     + LYDBHelper.TABLECHARSQLKEY
                + CHAR_CARDTYPE     + LYDBHelper.TABLECHARSQLKEY
                + CHAR_FILESTATE    + LYDBHelper.TABLEINTEGERSQLKEY
                + CHAR_MSGID        + LYDBHelper.TABLECHARSQLKEY
                + CHAR_UID          + LYDBHelper.TABELCHARLASTSQLKEY
                + LYDBHelper.TABLERIGHTSQL;
    }

    /**
     * 创建表
     * @param userId
     */
    public void createCharTable(String userId,LYChatItemModel model){
        if(fashionLYDBHelper !=null && db!=null){
            fashionLYDBHelper.execSQL(db, getCreateCharTable(handleCharTableName(model,userId)));
        }
    }

    /**
     * 插入数据，检查数据是否创建，没创建，创建数据库，检查数据库是否存在记录，不存在插入数据，存在更新数据
     * @param userId
     * @return
     */
    public boolean checkAndInsertCharData(String userId,LYChatItemModel model){

        if(db == null){
            return false;
        }

        if(checkAndCreateCharTable(userId,model))
        {
            if(queryCharById(userId, model)){
                return updateChar(userId,model);
            }else{
                return insertChar(userId,model);
            }
        }
        return false;
    }

    /**
     * 检查并创建聊天记录表
     * @param userId
     * @return
     */
    private boolean checkAndCreateCharTable(String userId,LYChatItemModel model){

        if(db == null || fashionLYDBHelper == null){
            return false;
        }

        if(!fashionLYDBHelper.tabIsExist(db, handleCharTableName(model,userId))){
            createCharTable(userId,model);
        }
        return true;
    }

    private  ContentValues getDataDic(LYChatItemModel model){

        ContentValues initialValues = new ContentValues();
        initialValues.put(CHAR_CHARID, model.getId());
        initialValues.put(CHAR_CONTENT, model.getChatContent());
        initialValues.put(CHAR_TIME, model.getChatTimer());
        initialValues.put(CHAR_FROMID, model.getFromModel().getUserId());
        initialValues.put(CHAR_FROMTYPE, model.getFromModel().getType());
        initialValues.put(CHAR_FROMNAME, model.getFromModel().getUserName());
        initialValues.put(CHAR_FROMAVATAR, model.getFromModel().getAvatar());
        initialValues.put(CHAR_TOID, model.getToId());
        initialValues.put(CHAR_TOTYPE, model.getToType());
        initialValues.put(CHAR_TONAME, model.getToName());
        initialValues.put(CHAR_TOAVATAR, model.getToAvatar());
        initialValues.put(CHAR_TYPE, DLStringUtil.strToInt(model.getContentType()));
        initialValues.put(CHAR_MESSAGETYPE, DLStringUtil.strToInt(model.getMessageType()));
        initialValues.put(CHAR_STATUS, DLStringUtil.strToInt(model.getStatus()));
        initialValues.put(CHAR_MQTTMSGID,model.getMqttMsgId());

        initialValues.put(CHAR_UID,model.getUid());
        initialValues.put(CHAR_MSGID,model.getMsgId());

        if(model.getImageModel()!=null){
            initialValues.put(CHAR_FILEURL, model.getImageModel().getImageUrl());
            initialValues.put(CHAR_FILEPATH, model.getImageModel().getImagePath());
        }
        if(model.getVoiceModel()!=null){
            initialValues.put(CHAR_FILEURL, model.getVoiceModel().getVoiceUrl());
            initialValues.put(CHAR_FILEPATH, model.getVoiceModel().getVoicePath());
            initialValues.put(CHAR_VOICELENGHT, model.getVoiceModel().getVoiceLength());
            initialValues.put(CHAR_FILESTATE,model.getVoiceModel().isRead()+"");
        }
        if(model.getCardModel()!=null){
            initialValues.put(CHAR_CARDID, model.getCardModel().getUserId());
            initialValues.put(CHAR_CARDTYPE, model.getCardModel().getType());
            initialValues.put(CHAR_CARDAVATAR, model.getCardModel().getAvatar());
            initialValues.put(CHAR_CARDNAME, model.getCardModel().getUserName());
        }
        if(model.getLocationModel()!=null){
            initialValues.put(CHAR_LATANDLON, model.getLocationModel().getLat()+","+model.getLocationModel().getLng());
            initialValues.put(CHAR_FILEURL, model.getLocationModel().getImageUrl());
            initialValues.put(CHAR_FILEPATH, model.getLocationModel().getImagePath());
        }
        if(model.getFileModel()!=null){
            initialValues.put(CHAR_FILENAME, model.getFileModel().getFileName());
            initialValues.put(CHAR_FILESIZE, model.getFileModel().getFileSize());
            initialValues.put(CHAR_FILEURL, model.getFileModel().getFileUrl());
            initialValues.put(CHAR_FILESTATE, model.getFileModel().getDownState());
        }

        if(model.getVideoModel()!=null){
            initialValues.put(CHAR_FILEURL, model.getVideoModel().getVideoUrl());
            initialValues.put(CHAR_FILEPATH, model.getVideoModel().getVideoPath());
            initialValues.put(CHAR_VOICELENGHT, model.getVideoModel().getVideoLength());
            initialValues.put(CHAR_CARDAVATAR, model.getVideoModel().getVideoLogoUrl());
            initialValues.put(CHAR_CARDNAME, model.getVideoModel().getVideoLogoPath());
        }
        if(model.getProjectModel()!=null){
            initialValues.put(CHAR_CARDID, model.getProjectModel().getProjectId());
            initialValues.put(CHAR_CARDAVATAR, model.getProjectModel().getProjectLogo());
            initialValues.put(CHAR_CARDNAME, model.getProjectModel().getProjectName());
        }

        return initialValues;
    }

    /**
     * 插入某人聊天记录
     * @param userId
     * @return
     */
    private boolean insertChar(String userId,LYChatItemModel model){

        if(db == null){
            return false;
        }

        if (!checkAndCreateCharTable(userId,model)){
            return false;
        }

        return db.insert(handleCharTableName(model,userId), null, getDataDic(model)) > 0;
    }

    /**
     * 更新聊天记录
     * @param userId
     * @return
     */
    public boolean updateChar(String userId,LYChatItemModel model){

        if(db == null){
            return false;
        }

        if (!checkAndCreateCharTable(userId,model)){
            return false;
        }

        return db.update(handleCharTableName(model,userId), getDataDic(model), getWhereStr(model), null)>0;
    }



    /**
     * 删除聊天记录
     * @param userId
     * @return
     */
    public boolean deleteChar(String userId,LYChatItemModel model){

        if(db == null){
            return false;
        }

        if (!checkAndCreateCharTable(userId,model)){
            return false;
        }

        return db.delete(handleCharTableName(model,userId), getWhereStr(model), null) > 0;
    }

    private String handleCharTableName(LYChatItemModel model,String userId){

        String tableName = TABLE_CHAR;

        if(DLStringUtil.notEmpty(model.getMessageType())){
            if(model.getMessageType().equals("0")){
                tableName+="_"+model.getUid()+"_user_"+userId;
            }else if(model.getMessageType().equals("1")){
                tableName+="_"+model.getUid()+"_group_"+userId;
            }
        }

        return tableName;
    }

    /**
     * 删除某用户所有聊天记录
     * @param userId
     * @return
     */
    public boolean deleteAllChar(String userId,LYChatItemModel model){

        if(db == null){
            return false;
        }

        if (!checkAndCreateCharTable(userId,model)){
            return false;
        }

        dropTable(handleCharTableName(model,userId));

        return true;
    }

    /**
     * 删除表
     * @param taleName
     */
    public void dropTable(String taleName) {
        if(fashionLYDBHelper !=null && db!=null){
            fashionLYDBHelper.dropTable(db, taleName);
        }
    }

    private String getWhereStr(LYChatItemModel model){
        return CHAR_CHARID+"='"+model.getId()+"'";//+"' and "+ CHAR_MSGID+"='"+model.getMsgId()
    }

    /**
     * 检索某条记录是否存在
     * @param userId
     * @return
     */
    public boolean queryCharById(String userId,LYChatItemModel model){

        if(db == null){
            return false;
        }

        if (!checkAndCreateCharTable(userId,model)){
            return false;
        }

        boolean result = false;

        Cursor cursor = db.query(handleCharTableName(model,userId), getQueryStr(), getWhereStr(model), null, null, null, null);

        if(cursor!=null){
            if(cursor.moveToFirst()){
                result = true;
            }
        }
        cursor.close();

        return result;
    }

    /**
     * 更新CHAR状态
     * @param userId
     * @param model
     * @return
     */
    public boolean updateCharStatus(String userId,LYChatItemModel model){

        if(db == null){
            return false;
        }

        if (!checkAndCreateCharTable(userId,model)){
            return false;
        }

        ContentValues initialValues = new ContentValues();
        initialValues.put(CHAR_CHARID, model.getId());
        initialValues.put(CHAR_CONTENT, model.getChatContent());
        initialValues.put(CHAR_TIME, model.getChatTimer());
        initialValues.put(CHAR_TYPE, DLStringUtil.strToInt(model.getContentType()));
        initialValues.put(CHAR_MESSAGETYPE, DLStringUtil.strToInt(model.getMessageType()));
        initialValues.put(CHAR_STATUS, DLStringUtil.strToInt(model.getStatus()));
        initialValues.put(CHAR_MSGID,model.getMsgId());

        return db.update(handleCharTableName(model,userId), initialValues, getWhereStr(model), null)>0;
    }

    /**
     * 获取最新的聊天记录
     * @param userId
     * @return
     */
    public LYChatItemModel queryLastItem(String userId,String uid,String messageType){
        LYChatItemModel result = null;

        if(db == null){
            return result;
        }

        LYChatItemModel tempNee = new LYChatItemModel();
        tempNee.setUid(uid);
        tempNee.setMessageType(messageType);

        if (!checkAndCreateCharTable(userId,tempNee)){
            return result;
        }

        Cursor  cursor = db.query(handleCharTableName(tempNee,userId),
                getQueryStr(),
                null, null, null, null, CHAR_TIME + " desc","0,1");

        if(cursor == null){
            return null;
        }

        while (cursor.moveToNext()) {
            result = getDataModel(cursor);
        }
        cursor.close();

        return result ;
    }

    public int queryAllCharTotalCount(String userId,String uid,String messageType){

        if(db == null){
            return 0;
        }

        LYChatItemModel tempNee = new LYChatItemModel();
        tempNee.setUid(uid);
        tempNee.setMessageType(messageType);

        if (!checkAndCreateCharTable(userId,tempNee)){
            return 0;
        }

        Cursor  cursor = db.query(handleCharTableName(tempNee,userId), getQueryStr(),
                null, null, null, null, CHAR_TIME + " desc",null);//desc

        if(cursor == null){
            return 0;
        }

        return cursor.getCount();
    }

    /**
     * 查询某人聊天记录数据 分页显示
     * @return
     */
    public ArrayList<LYChatItemModel> queryAllChar(String userId, int limit, int max, String uid, String messageType){
        ArrayList<LYChatItemModel> result = new ArrayList<LYChatItemModel>();

        if(db == null){
            return result;
        }

        LYChatItemModel tempNee = new LYChatItemModel();
        tempNee.setUid(uid);
        tempNee.setMessageType(messageType);

        if (!checkAndCreateCharTable(userId,tempNee)){
            return result;
        }

        Cursor  cursor = db.query(handleCharTableName(tempNee,userId),
                getQueryStr(),
                null, null, null, null, CHAR_TIME + " desc",max+","+limit);//desc

        if(cursor == null){
            return null;
        }

        while (cursor.moveToNext()) {

            result.add(getDataModel(cursor));
        }
        Collections.reverse(result);
        cursor.close();
        return result;
    }

    private String[] getQueryStr(){
        return  new String[]{CHAR_CHARID, CHAR_CONTENT, CHAR_TIME,
                CHAR_FROMID, CHAR_FROMAVATAR, CHAR_MESSAGETYPE,
                CHAR_STATUS, CHAR_TOAVATAR, CHAR_TOID,
                CHAR_TYPE, CHAR_FROMNAME, CHAR_TONAME,
                CHAR_VOICELENGHT, CHAR_FILEPATH, CHAR_MQTTMSGID,
                CHAR_CARDAVATAR, CHAR_CARDID, CHAR_CARDNAME,
                CHAR_FILENAME, CHAR_FILESIZE, CHAR_FILEURL,
                CHAR_LATANDLON, CHAR_AUTOID, CHAR_TOTYPE,
                CHAR_FROMTYPE, CHAR_CARDTYPE, CHAR_FILESTATE,CHAR_UID,CHAR_MSGID};
    }

    private LYChatItemModel getDataModel(Cursor cursor){

        LYChatItemModel tempItem = new LYChatItemModel();
        tempItem.setAutoId(cursor.getInt(cursor.getColumnIndex(CHAR_AUTOID))+"");
        tempItem.setChatTimer(cursor.getString(cursor.getColumnIndex(CHAR_TIME)));
        tempItem.setUid(cursor.getString(cursor.getColumnIndex(CHAR_UID)));
        tempItem.setContentType(""+cursor.getInt(cursor.getColumnIndex(CHAR_TYPE)));
        tempItem.setId(cursor.getString(cursor.getColumnIndex(CHAR_CHARID)));
        tempItem.setMessageType(""+cursor.getInt(cursor.getColumnIndex(CHAR_MESSAGETYPE)));
        tempItem.setStatus(cursor.getString(cursor.getColumnIndex(CHAR_STATUS)));
        tempItem.setMqttMsgId(cursor.getString(cursor.getColumnIndex(CHAR_MQTTMSGID)));

        if(tempItem.getStatus().equals("0")){
            tempItem.setStatus("3");
        }

        tempItem.setMsgId(cursor.getString(cursor.getColumnIndex(CHAR_MSGID)));

        switch (tempItem.getChatType()) {
            case EChat_Text:
            case EChat_System:
                tempItem.setChatContent(cursor.getString(cursor.getColumnIndex(CHAR_CONTENT)));
                break;
            case EChat_Image:{
                DLImageModel tempImage = new DLImageModel();
                tempImage.setImageUrl(cursor.getString(cursor.getColumnIndex(CHAR_FILEURL)));
                tempImage.setImagePath(cursor.getString(cursor.getColumnIndex(CHAR_FILEPATH)));
                tempItem.setImageModel(tempImage);
            }
            break;
            case EChat_Voice:	{
                DLVoiceModel tempVoice = new DLVoiceModel();
                tempVoice.setVoiceUrl(cursor.getString(cursor.getColumnIndex(CHAR_FILEURL)));
                tempVoice.setVoicePath(cursor.getString(cursor.getColumnIndex(CHAR_FILEPATH)));
                tempVoice.setVoiceLength(cursor.getString(cursor.getColumnIndex(CHAR_VOICELENGHT)));
                tempVoice.setRead(DLStringUtil.strToBoolean(cursor.getString(cursor.getColumnIndex(CHAR_FILESTATE))));
                tempItem.setVoiceModel(tempVoice);
            }
            break;
            case EChat_Video:{
                DLVideoModel tempVideo = new DLVideoModel();
                tempVideo.setVideoUrl(cursor.getString(cursor.getColumnIndex(CHAR_FILEURL)));
                tempVideo.setVideoPath(cursor.getString(cursor.getColumnIndex(CHAR_FILEPATH)));
                tempVideo.setVideoLength(cursor.getString(cursor.getColumnIndex(CHAR_VOICELENGHT)));
                tempVideo.setVideoLogoUrl(cursor.getString(cursor.getColumnIndex(CHAR_CARDAVATAR)));
                tempVideo.setVideoLogoPath(cursor.getString(cursor.getColumnIndex(CHAR_CARDNAME)));
                tempItem.setVideoModel(tempVideo);
            }
            break;
            case EChat_Location:	{
                DLLocationModel tempLocation = new DLLocationModel();
                tempLocation.setImageUrl(cursor.getString(cursor.getColumnIndex(CHAR_FILEURL)));
                tempLocation.setImagePath(cursor.getString(cursor.getColumnIndex(CHAR_FILEPATH)));
                String tempLocationStr = cursor.getString(cursor.getColumnIndex(CHAR_LATANDLON));
                if(DLStringUtil.notEmpty(tempLocationStr)){
                    String[] tempLatAndLng = tempLocationStr.split(",");
                    if(tempLatAndLng!=null){
                        tempLocation.setLat(tempLatAndLng[0]);
                        tempLocation.setLng(tempLatAndLng[1]);
                    }
                }
                tempItem.setLocationModel(tempLocation);
            }
            break;
            case EChat_File:	{
                DLFileModel tempFile = new DLFileModel();
                tempFile.setFileUrl(cursor.getString(cursor.getColumnIndex(CHAR_FILEURL)));
                tempFile.setFileName(cursor.getString(cursor.getColumnIndex(CHAR_FILENAME)));
                tempFile.setFileSize(cursor.getString(cursor.getColumnIndex(CHAR_FILESIZE)));
                tempFile.setDownState(cursor.getInt(cursor.getColumnIndex(CHAR_FILESTATE)));
                tempItem.setFileModel(tempFile);
            }
            break;
            case EChat_Card:	{
                LYUserModel tempCard = new LYUserModel();
                tempCard.setUserId(cursor.getString(cursor.getColumnIndex(CHAR_CARDID)));
                tempCard.setType(cursor.getString(cursor.getColumnIndex(CHAR_CARDTYPE)));
                tempCard.setAvatar(cursor.getString(cursor.getColumnIndex(CHAR_CARDAVATAR)));
                tempCard.setUserName(cursor.getString(cursor.getColumnIndex(CHAR_CARDNAME)));
                tempItem.setCardModel(tempCard);
            }
            break;
            case EChat_Project:{
                LYProjectModel tempProject = new LYProjectModel();
                tempProject.setProjectId(cursor.getString(cursor.getColumnIndex(CHAR_CARDID)));
                tempProject.setProjectLogo(cursor.getString(cursor.getColumnIndex(CHAR_CARDAVATAR)));
                tempProject.setProjectName(cursor.getString(cursor.getColumnIndex(CHAR_CARDNAME)));
                tempItem.setProjectModel(tempProject);
            }
            break;
            default:
                break;
        }

        LYUserModel tempFrom = new LYUserModel();
        tempFrom.setUserId(cursor.getString(cursor.getColumnIndex(CHAR_FROMID)));
        tempFrom.setType(cursor.getString(cursor.getColumnIndex(CHAR_FROMTYPE)));
        tempFrom.setUserName(cursor.getString(cursor.getColumnIndex(CHAR_FROMNAME)));;
        tempFrom.setAvatar(cursor.getString(cursor.getColumnIndex(CHAR_FROMAVATAR)));
        tempItem.setFromModel(tempFrom);

        boolean from = LyImEngine.getInstance().getUserId().equals(tempFrom.getUserId());
        tempItem.setIsfrom(!from);

        if(DLStringUtil.notEmpty(tempItem.getMessageType())){
            if(tempItem.getMessageType().equals("0")){
                LYUserModel tempTo = new LYUserModel();
                tempTo.setUserId(cursor.getString(cursor.getColumnIndex(CHAR_TOID)));
                tempTo.setType(cursor.getString(cursor.getColumnIndex(CHAR_TOTYPE)));
                tempTo.setUserName(cursor.getString(cursor.getColumnIndex(CHAR_TONAME)));
                tempTo.setAvatar(cursor.getString(cursor.getColumnIndex(CHAR_TOAVATAR)));
                tempItem.setToUserModel(tempTo);
            }else if(tempItem.getMessageType().equals("1")){
                LYGroupItemModel tempTo = new LYGroupItemModel();
                tempTo.setGroupId(cursor.getString(cursor.getColumnIndex(CHAR_TOID)));
                tempTo.setGroupName(cursor.getString(cursor.getColumnIndex(CHAR_TONAME)));
                tempTo.setGroupAvatar(cursor.getString(cursor.getColumnIndex(CHAR_TOAVATAR)));
                tempItem.setToGroupModel(tempTo);
            }
        }

        return tempItem;
    }
}
