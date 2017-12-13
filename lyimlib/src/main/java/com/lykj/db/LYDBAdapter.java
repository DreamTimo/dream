package com.lykj.db;

import java.util.ArrayList;
import java.util.Collections;

import com.dlbase.Model.DLFileModel;
import com.dlbase.Model.DLImageModel;
import com.dlbase.Model.DLLocationModel;
import com.dlbase.Model.DLVideoModel;
import com.dlbase.Model.DLVoiceModel;
import com.dlbase.util.DLStringUtil;
import com.luyz.lyimlib.LyImEngine;
import com.lykj.model.LYChatItemModel;
import com.lykj.model.LYChatListModel;
import com.lykj.model.LYGroupItemModel;
import com.lykj.model.LYProjectModel;
import com.lykj.model.LYTopisItemModel;
import com.lykj.model.LYUserModel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 数据库工具类
 * @author luyz
 *
 */
public class LYDBAdapter {

	private Context mContext;
	private static LYDBAdapter LYDBAdapter;
	private LYDBHelper fashionLYDBHelper;
	private SQLiteDatabase db;

	private LYCharListTable charListTable;
	private LYCharTable charTable;
	private LYMQTTTable mqttTable;
	private LYDownFileTable downFileTable;

	/**
	 * 创建数据库
	 * @param context
	 * @return
	 */
	public static LYDBAdapter createDBAdapter(Context context, String userKey) {
		if(LYDBAdapter == null) {
			LYDBAdapter = new LYDBAdapter(context,userKey);
		}
		return LYDBAdapter;
	}
	
	/**
	 * 关闭数据库
	 */
	public static void close() {
		if(LYDBAdapter != null) {
			LYDBAdapter.closeDB();
			LYDBAdapter = null;
		}
	}
	
	private LYDBAdapter(Context context, String userKey) {
		this.mContext = context;
		fashionLYDBHelper = new LYDBHelper(mContext,userKey);
		db = fashionLYDBHelper.getWritableDatabase();

		charListTable = new LYCharListTable(db);
		charTable = new LYCharTable(fashionLYDBHelper);
		mqttTable = new LYMQTTTable(db);
		downFileTable = new LYDownFileTable(db);
	}
	
	/**
	 * 关闭数据库
	 */
	private void closeDB() {
		if(fashionLYDBHelper !=null){
			fashionLYDBHelper.closeDB(db);
			fashionLYDBHelper = null;
		}
	}

	public LYCharListTable getCharListTable() {
		return charListTable;
	}

	public void setCharListTable(LYCharListTable charListTable) {
		this.charListTable = charListTable;
	}

	public LYCharTable getCharTable() {
		return charTable;
	}

	public void setCharTable(LYCharTable charTable) {
		this.charTable = charTable;
	}

	public LYMQTTTable getMqttTable() {
		return mqttTable;
	}

	public void setMqttTable(LYMQTTTable mqttTable) {
		this.mqttTable = mqttTable;
	}

	public LYDownFileTable getDownFileTable() {
		return downFileTable;
	}

	public void setDownFileTable(LYDownFileTable downFileTable) {
		this.downFileTable = downFileTable;
	}
}
