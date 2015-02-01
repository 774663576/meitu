package com.meitu.data;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.meitu.db.Const;

public class Praise extends AbstractData {
	private int user_id;
	private String user_avatar = "";
	private int article_id;

	@Override
	public String toString() {
		return "user_id:" + user_id + "  article_id" + article_id;
	}

	public int getArticle_id() {
		return article_id;
	}

	public void setArticle_id(int article_id) {
		this.article_id = article_id;
	}

	public String getUser_avatar() {
		return user_avatar;
	}

	public void setUser_avatar(String user_avatar) {
		this.user_avatar = user_avatar;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	@Override
	public void write(SQLiteDatabase db) {
		String dbName = Const.PRAISE_TABLE_NAME;
		if (this.status == Status.DEL) {
			int res = db.delete(dbName, "article_id=? and user_id=?",
					new String[] { article_id + "", user_id + "" });
			System.out.println("res:::::::::::::" + res + "     " + article_id
					+ "        " + user_id);
			return;
		}
		ContentValues cv = new ContentValues();
		cv.put("article_id", this.article_id);
		cv.put("user_id", this.user_id);
		cv.put("user_avatar", this.user_avatar);
		db.insert(dbName, null, cv);

	}
}
