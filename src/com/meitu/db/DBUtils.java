package com.meitu.db;

import android.database.sqlite.SQLiteDatabase;

/**
 * ���ݿ������
 */
public class DBUtils {
	public static DataBaseHelper dbase = DataBaseHelper.getInstance();
	public static SQLiteDatabase dbs = dbase.getReadableDatabase();

	/**
	 * 1 ��ʾ�� 2��ʾд
	 * 
	 * @param flagType
	 * @return
	 */
	public static SQLiteDatabase getDBsa(int flagType) {
		if (dbase == null) {
			dbase = DataBaseHelper.getInstance();
		}
		if (!dbs.isOpen()) {
			if (flagType == 1) {
				dbs = dbase.getReadableDatabase();
			} else {
				dbs = dbase.getWritableDatabase();
			}
		}
		return dbs;
	}

	public static void close() {
		dbase = null;
		if (dbs != null) {
			dbs.close();
		}
	}

}
