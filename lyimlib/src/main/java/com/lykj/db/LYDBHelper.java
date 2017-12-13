package com.lykj.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * 数据库定义类
 * @author luyz
 *
 */
public class LYDBHelper extends SQLiteOpenHelper {
	
	//DB名称
	private static final String DB_FILENAME = "Im";
	//版本号
	private static final int DB_VERSION = 2;

	public static final String CREATETABLESQL = "CREATE TABLE IF NOT EXISTS ";
    public static final String DROPTABLESQL = "DROP TABLE IF EXISTS ";

	public static final String TABLELEFTSQL = " (";
	public static final String TABLERIGHTSQL = ");";

	public static final String TABLEAUTOSQLKEY = " INTEGER PRIMARY KEY AUTOINCREMENT, ";
	public static final String TABLECHARNOTNULLSQLKEY = " VARCHAR NOT NULL, ";
	public static final String TABLECHARSQLKEY = " VARCHAR, ";
	public static final String TABELCHARLASTSQLKEY = " VARCHAR";
	public static final String TABLEINTEGERSQLKEY = " INTEGER, ";

	public LYDBHelper(Context context, String userKey) {
		super(context, DB_FILENAME+userKey+".db", null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		execSQL(db, LYCharListTable.getCreateCharListTableSQL());
		execSQL(db, LYMQTTTable.getCreateMQTTTable());
		execSQL(db, LYDownFileTable.getCreateDownFileTable());
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		dropTable(db,LYCharListTable.TABLE_CHARLIST);
		dropTable(db,LYMQTTTable.TABLE_MQTTANDUSER);
		dropTable(db,LYDownFileTable.TABLE_DOWNFILE);
		onCreate(db);
	}

	public void execSQL(SQLiteDatabase db,String sql){
		db.execSQL(sql);
	}
	
	public void dropTable(SQLiteDatabase db,String taleName) {
		db.execSQL(LYDBHelper.DROPTABLESQL + taleName);
    }
	
	public void closeDB(SQLiteDatabase db) {
		db.close();
		this.close();
	}
	
	public boolean tabIsExist(SQLiteDatabase db,String tabName){
        boolean result = false;
        if(tabName == null){
            return false;
        }
        Cursor cursor = null;
        try {
               
                String sql = "select count(*) as c from sqlite_master where type ='table' and name ='"+tabName.trim()+"' ";
                cursor = db.rawQuery(sql, null);
                if(cursor.moveToNext()){
                        int count = cursor.getInt(0);
                        if(count>0){
                                result = true;
                        }
                }
                
        } catch (Exception e) {
                // TODO: handle exception
        }                
        return result;
	}
}
