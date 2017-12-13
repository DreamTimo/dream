package com.lykj.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dlbase.Model.DLFileModel;

import java.util.ArrayList;

/**
 * Created by luyz on 2017/5/12.
 */

public class LYDownFileTable {

    //文件表
    public static final String TABLE_DOWNFILE = "downfile_table";
    public static final String DOWNFILE_AUTOID = "auto_id";
    public static final String DOWNFILE_URL = "file_url";
    public static final String DOWNFILE_NAME = "file_name";
    public static final String DOWNFILE_SIZE = "file_size";
    public static final String DOWNFILE_PATH = "file_path";
    public static final String DOWNFILE_STATE = "file_state";
    public static final String DOWNFILE_UPDATETIME = "file_timer";
    public static final String DOWNFILE_USERID = "file_user";

    private SQLiteDatabase db;

    public LYDownFileTable(SQLiteDatabase sdb){
        db = sdb;
    }


    public static String getCreateDownFileTable(){
        return LYDBHelper.CREATETABLESQL
                + TABLE_DOWNFILE
                + LYDBHelper.TABLELEFTSQL
                + DOWNFILE_AUTOID + LYDBHelper.TABLEAUTOSQLKEY
                + DOWNFILE_URL + LYDBHelper.TABLECHARSQLKEY
                + DOWNFILE_NAME + LYDBHelper.TABLECHARNOTNULLSQLKEY
                + DOWNFILE_SIZE + LYDBHelper.TABLECHARSQLKEY
                + DOWNFILE_STATE + LYDBHelper.TABLEINTEGERSQLKEY
                + DOWNFILE_UPDATETIME + LYDBHelper.TABLECHARSQLKEY
                + DOWNFILE_USERID + LYDBHelper.TABLECHARNOTNULLSQLKEY
                + DOWNFILE_PATH + LYDBHelper.TABELCHARLASTSQLKEY
                + LYDBHelper.TABLERIGHTSQL;
    }


    /**
     * 检查并添加或更新DownFile数据
     * @param model
     * @return
     */
    public boolean checkAndInsertDownFle(DLFileModel model){
        if(model==null || db == null){
            return false;
        }

        boolean result = false;

        if(queryDownFileByUrl(model)){
            result = updateDownFile(model);
        }else{
            result = insertDownFile(model);
        }

        return result;
    }

    private  ContentValues getDataDic(DLFileModel model){

        ContentValues initialValues = new ContentValues();
        initialValues.put(DOWNFILE_NAME, model.getFileName());
        initialValues.put(DOWNFILE_SIZE, model.getFileSize());
        initialValues.put(DOWNFILE_STATE, model.getDownState());
        initialValues.put(DOWNFILE_UPDATETIME, model.getFileTime());
        initialValues.put(DOWNFILE_URL, model.getFileUrl());
        initialValues.put(DOWNFILE_PATH, model.getFilePath());
        initialValues.put(DOWNFILE_USERID, model.getUserId());

        return initialValues;
    }

    /**
     * 插入DownFile
     * @return
     */
    private boolean insertDownFile(DLFileModel model){

        if(model==null|| db == null){
            return false;
        }

        return db.insert(TABLE_DOWNFILE, null, getDataDic(model)) > 0;
    }

    /**
     * 更新DownFile
     * @return
     */
    private boolean updateDownFile(DLFileModel model){

        if(model==null||db == null){
            return false;
        }

        return db.update(TABLE_DOWNFILE, getDataDic(model),getWhereStr(model), null)>0;
    }

    /**
     * 检索DownFile记录是否存在
     * @return
     */
    public boolean queryDownFileByUrl(DLFileModel model){
        boolean result = false;
        if(db == null){
            return false;
        }
        Cursor cursor = db.query(TABLE_DOWNFILE,
                getQueryStr(),
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

    private String[] getQueryStr(){
        return new String[]{DOWNFILE_AUTOID, DOWNFILE_NAME, DOWNFILE_SIZE,
                DOWNFILE_STATE, DOWNFILE_STATE, DOWNFILE_UPDATETIME,
                DOWNFILE_URL, DOWNFILE_USERID, DOWNFILE_PATH};
    }

    private String getWhereStr(DLFileModel model){
        return DOWNFILE_USERID+"='"+model.getUserId()+"' and "+ DOWNFILE_NAME+"='"+model.getFileName()+"'";
    }


    /**
     * 查询DownFile数据
     * @return
     */
    public DLFileModel queryDownFile(String url,String userId){

        if(db == null){
            return null;
        }

        Cursor  cursor = db.query(TABLE_DOWNFILE,
                getQueryStr(),
                DOWNFILE_USERID+"='"+userId+"' and "+ DOWNFILE_URL +"='"+url+"'",
                null, null, null, null);

        if(cursor == null){
            return null;
        }

        DLFileModel tempItem = null;

        if (cursor.moveToFirst()) {

            tempItem = getDataItemModel(cursor);
        }
        cursor.close();
        return tempItem;
    }

    /**
     * 查询全部DownFile数据
     * @return
     */
    public ArrayList<DLFileModel> queryAllDownFile(String userId){

        ArrayList<DLFileModel> result = new ArrayList<DLFileModel>();

        if(db == null){
            return result;
        }

        Cursor  cursor = db.query(TABLE_DOWNFILE,
                getQueryStr(),
                DOWNFILE_USERID+"='"+userId+"'", null, null, null, null);

        if(cursor == null){
            return null;
        }

        while (cursor.moveToNext()) {

            result.add(getDataItemModel(cursor));
        }
        cursor.close();
        return result;
    }

    private DLFileModel getDataItemModel(Cursor cursor){
        DLFileModel tempItem = new DLFileModel();
        tempItem.setAdd(false);
        tempItem.setCheck(false);
        tempItem.setFileName(cursor.getString(cursor.getColumnIndex(DOWNFILE_NAME)));
        tempItem.setFileSize(cursor.getString(cursor.getColumnIndex(DOWNFILE_SIZE)));
        tempItem.setFileUrl(cursor.getString(cursor.getColumnIndex(DOWNFILE_URL)));
        tempItem.setFileTime(cursor.getString(cursor.getColumnIndex(DOWNFILE_UPDATETIME)));
        tempItem.setDownState(cursor.getInt(cursor.getColumnIndex(DOWNFILE_STATE)));
        tempItem.setUserId(cursor.getString(cursor.getColumnIndex(DOWNFILE_USERID)));
        tempItem.setFilePath(cursor.getString(cursor.getColumnIndex(DOWNFILE_PATH)));

        return tempItem;
    }

    /**
     * 删除DownFile
     * @return
     */
    public boolean deleteDownFile(DLFileModel model){
        if(db == null){
            return false;
        }
        return db.delete(TABLE_DOWNFILE, getWhereStr(model), null) > 0;
    }

    /**
     * 删除所有DownFile
     * @return
     */
    public boolean deleteAllDownFile(){
        if(db == null){
            return false;
        }
        return db.delete(TABLE_DOWNFILE, null, null) > 0;
    }
}
