package com.lykj.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dlbase.util.DLStringUtil;
import com.lykj.model.LYChatItemModel;
import com.lykj.model.LYChatListModel;
import com.lykj.model.LYGroupItemModel;
import com.lykj.model.LYUserModel;

import java.util.ArrayList;

/**
 * Created by luyz on 2017/5/12.
 */

public class LYCharListTable {

    private SQLiteDatabase db;

    //聊天会话表
    public static final String TABLE_CHARLIST = "chatlist_table";
    public static final String CHARLIST_AUTOID = "auto_id";
    public static final String CHARLIST_CHARID = "char_id";
    public static final String CHARLIST_TIME = "char_time";
    public static final String CHARLIST_TYPE = "char_type";
    public static final String CHARLIST_CONTENT = "char_content";
    public static final String CHARLIST_MESSAGETYPE = "message_type";
    public static final String CHARLIST_NEWCOUNT = "newcount";
    public static final String CHARLIST_FROMID = "from_id";
    public static final String CHARLIST_FROMTYPE = "from_type";
    public static final String CHARLIST_FROMNAME = "from_name";
    public static final String CHARLIST_FROMAVATAR = "from_avatar";
    public static final String CHARLIST_UID = "uid";

    public static String getCreateCharListTableSQL(){
        return LYDBHelper.CREATETABLESQL
                + TABLE_CHARLIST
                + LYDBHelper.TABLELEFTSQL
                + CHARLIST_AUTOID       + LYDBHelper.TABLEAUTOSQLKEY
                + CHARLIST_FROMID       + LYDBHelper.TABLECHARNOTNULLSQLKEY
                + CHARLIST_TIME         + LYDBHelper.TABLECHARSQLKEY
                + CHARLIST_CHARID       + LYDBHelper.TABLECHARSQLKEY
                + CHARLIST_TYPE         + LYDBHelper.TABLEINTEGERSQLKEY
                + CHARLIST_CONTENT      + LYDBHelper.TABLECHARSQLKEY
                + CHARLIST_MESSAGETYPE  + LYDBHelper.TABLEINTEGERSQLKEY
                + CHARLIST_NEWCOUNT     + LYDBHelper.TABLEINTEGERSQLKEY
                + CHARLIST_UID          + LYDBHelper.TABLECHARSQLKEY
                + CHARLIST_FROMNAME     + LYDBHelper.TABLECHARNOTNULLSQLKEY
                + CHARLIST_FROMAVATAR   + LYDBHelper.TABLECHARSQLKEY
                + CHARLIST_FROMTYPE     + LYDBHelper.TABELCHARLASTSQLKEY
                + LYDBHelper.TABLERIGHTSQL;
    }

    public  LYCharListTable(SQLiteDatabase sdb){
        db=sdb;
    }

    private String getCharId(LYChatListModel model){

        String fromId = null;

        if (model!=null){

            if(DLStringUtil.notEmpty(model.getMessageType())){
                if(model.getMessageType().equals("0")){
                    if(model.getFromUserModel()!=null){
                        fromId = model.getFromUserModel().getUserId();
                    }
                }else if(model.getMessageType().equals("1")){
                    if(model.getFromGroupModel()!=null){
                        fromId = model.getFromGroupModel().getGroupId();
                    }
                }
            }
        }

        return fromId;
    }

    /**
     * 检查并插入或更新 聊天会话列表数据
     * @param model
     * @return
     */
    public boolean checkAndInsertCharListData(LYChatListModel model){

        if(db == null){
            return false;
        }

        if(queryCharListById(model)){
            return updateCharList(model);
        }else{
            return insertCharList(model);
        }
    }

    private ContentValues getCharDic(LYChatListModel model){

        String fromId = null;
        String fromName = null;
        String fromAvatar = null;
        String fromType = null;

        if(DLStringUtil.notEmpty(model.getMessageType())){
            if(model.getMessageType().equals("0")){
                if(model.getFromUserModel()!=null){
                    fromId = model.getFromUserModel().getUserId();
                    fromType = model.getFromUserModel().getType();
                    fromName = model.getFromUserModel().getUserName();
                    fromAvatar = model.getFromUserModel().getAvatar();
                }
            }else if(model.getMessageType().equals("1")){
                if(model.getFromGroupModel()!=null){
                    fromId = model.getFromGroupModel().getGroupId();
                    fromName = model.getFromGroupModel().getGroupName();
                    fromAvatar = model.getFromGroupModel().getGroupAvatar();
                }
            }
        }

        ContentValues initialValues = new ContentValues();

        initialValues.put(CHARLIST_FROMID, fromId);
        initialValues.put(CHARLIST_FROMNAME, fromName);
        initialValues.put(CHARLIST_FROMAVATAR, fromAvatar);
        initialValues.put(CHARLIST_FROMTYPE, fromType);

        if(DLStringUtil.notEmpty(model.getMessageType())){
            initialValues.put(CHARLIST_MESSAGETYPE, DLStringUtil.strToInt(model.getMessageType()));
        }

        initialValues.put(CHARLIST_NEWCOUNT, model.getNewCount());

        if(model.getLastItemModel()!=null){
            initialValues.put(CHARLIST_CHARID, model.getLastItemModel().getId());
            initialValues.put(CHARLIST_CONTENT, model.getLastItemModel().getChatContent());
            initialValues.put(CHARLIST_TIME, model.getLastItemModel().getChatTimer());
            initialValues.put(CHARLIST_TYPE, DLStringUtil.strToInt(model.getLastItemModel().getContentType()));
        }

//        initialValues.put(CHARLIST_UID,model.getUid());

        return initialValues;
    }

    private boolean insertCharList(LYChatListModel model){

        if(db == null){
            return false;
        }

        return db.insert(TABLE_CHARLIST, null, getCharDic(model)) > 0;
    }

    public boolean updateCharList(LYChatListModel model){

        if(db == null){
            return false;
        }

        String fromId = getCharId(model);

        return db.update(TABLE_CHARLIST, getCharDic(model),
                getWhereStr(model),
                null) > 0;
    }

    /**
     * 删除某人聊天记录
     * @return
     */
    public boolean deleteCharList(LYChatListModel model){
        if(db == null){
            return false;
        }
        return db.delete(TABLE_CHARLIST,
                getWhereStr(model),
                null) > 0;
    }


    private String getWhereStr(LYChatListModel model){
        String fromId = getCharId(model);
        return CHARLIST_FROMID+"='"+fromId+"' and "+ CHARLIST_MESSAGETYPE+"='"+model.getMessageType()+"'";
    }

    /**
     * 删除所有人聊天记录
     * @return
     */
    public boolean deleteAllCharList(){
        if(db == null){
            return false;
        }
        return db.delete(TABLE_CHARLIST, null, null) > 0;
    }

    /**
     * 检索某人是否存在聊天记录
     * @return
     */
    public boolean queryCharListById(LYChatListModel model){
        if(db == null){
            return false;
        }
        boolean result = false;

        Cursor cursor = db.query(TABLE_CHARLIST,
                new String[]{CHARLIST_FROMID, CHARLIST_MESSAGETYPE},
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

    public int queryCharListToNewCounts(){

        if(db == null){
            return 0;
        }

        int count = 0;

        Cursor cursor = db.query(TABLE_CHARLIST,
                new String[]{CHARLIST_FROMID, CHARLIST_MESSAGETYPE, CHARLIST_NEWCOUNT},
                null, null, null, null, null);

        if(cursor!=null){
            while (cursor.moveToNext()){
                count += cursor.getInt(cursor.getColumnIndex(CHARLIST_NEWCOUNT));
            }
        }
        cursor.close();

        return count;
    }

    public int queryCharListToNewCount(String fromId,String messageType){

        if(db == null){
            return 0;
        }
        int count = 0;

        Cursor cursor = db.query(TABLE_CHARLIST,
                new String[]{CHARLIST_FROMID, CHARLIST_MESSAGETYPE, CHARLIST_NEWCOUNT},
                CHARLIST_FROMID+"='"+fromId+"' and "+ CHARLIST_MESSAGETYPE+"='"+messageType+"'",
                null, null, null, null);

        if(cursor!=null){
            if(cursor.moveToFirst()){
                count = cursor.getInt(cursor.getColumnIndex(CHARLIST_NEWCOUNT));
            }
        }
        cursor.close();
        return count;
    }

    /**
     * 查询所有聊天记录数据
     * @return
     */
    public ArrayList<LYChatListModel> queryAllCharList(){

        ArrayList<LYChatListModel> result = new ArrayList<LYChatListModel>();

        if(db == null){
            return result;
        }

        Cursor  cursor = db.query(TABLE_CHARLIST,
                new String[]{CHARLIST_FROMID, CHARLIST_FROMNAME, CHARLIST_FROMAVATAR,
                        CHARLIST_CHARID, CHARLIST_CONTENT, CHARLIST_MESSAGETYPE,
                        CHARLIST_NEWCOUNT, CHARLIST_TIME, CHARLIST_TYPE,
                        CHARLIST_FROMTYPE},
                null, null, null, null, CHARLIST_TIME + " desc");

        if(cursor == null){
            return null;
        }

        while (cursor.moveToNext()) {

            result.add(getModel(cursor));
        }
        cursor.close();

        return result;
    }

    private LYChatListModel getModel(Cursor cursor){

        LYChatListModel tempItem = new LYChatListModel();

        int messageType = cursor.getInt(cursor.getColumnIndex(CHARLIST_MESSAGETYPE));
        if(messageType == 0){
            //个人
            LYUserModel tempUser = new LYUserModel();
            tempUser.setUserId(cursor.getString(cursor.getColumnIndex(CHARLIST_FROMID)));
            tempUser.setType(cursor.getString(cursor.getColumnIndex(CHARLIST_FROMTYPE)));
            tempUser.setAvatar(cursor.getString(cursor.getColumnIndex(CHARLIST_FROMAVATAR)));
            tempUser.setUserName(cursor.getString(cursor.getColumnIndex(CHARLIST_FROMNAME)));
            tempItem.setFromUserModel(tempUser);
        }else if(messageType == 1){
            //群组
            LYGroupItemModel tempGroup = new LYGroupItemModel();
            tempGroup.setGroupId(cursor.getString(cursor.getColumnIndex(CHARLIST_FROMID)));;
            tempGroup.setGroupAvatar(cursor.getString(cursor.getColumnIndex(CHARLIST_FROMAVATAR)));;
            tempGroup.setGroupName(cursor.getString(cursor.getColumnIndex(CHARLIST_FROMNAME)));;
            tempItem.setFromGroupModel(tempGroup);
        }

        LYChatItemModel tempLastitem = new LYChatItemModel();

        tempLastitem.setChatContent(cursor.getString(cursor.getColumnIndex(CHARLIST_CONTENT)));
        tempLastitem.setChatTimer(cursor.getString(cursor.getColumnIndex(CHARLIST_TIME)));
        tempLastitem.setContentType(""+cursor.getInt(cursor.getColumnIndex(CHARLIST_TYPE)));
        tempLastitem.setId(cursor.getString(cursor.getColumnIndex(CHARLIST_CHARID)));

        tempItem.setLastItemModel(tempLastitem);

        tempItem.setNewCount(cursor.getInt(cursor.getColumnIndex(CHARLIST_NEWCOUNT)));

//        tempItem.setUid(cursor.getString(cursor.getColumnIndex(CHARLIST_UID)));

        return tempItem;
    }
}
