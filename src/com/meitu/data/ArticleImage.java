package com.meitu.data;

import java.io.Serializable;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.meitu.db.Const;

/**
  * 
 */
public class ArticleImage extends AbstractData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int article_id = 0;
	private int imgId = 0;
	private String img = "";

	public ArticleImage() {
	}

	public ArticleImage(int cid, int gid, int imgId) {
		this(gid, imgId, "");
	}

	public ArticleImage(int aritcle_id, int imgId, String img) {
		this.article_id = aritcle_id;
		this.imgId = imgId;
		this.img = img;
	}

	public int getAritcle_id() {
		return article_id;
	}

	public void setAritcle_id(int aritcle_id) {
		this.article_id = aritcle_id;
	}

	public int getImgId() {
		return imgId;
	}

	public void setImgId(int imgId) {
		this.imgId = imgId;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	@Override
	public String toString() {
		return "AritcleImage [  aritcle_id=" + article_id + ", imgId=" + imgId
				+ ", img=" + img + "]";
	}

	@Override
	public void write(SQLiteDatabase db) {
		String dbName = Const.ARTICLE_IMAGE_TABLE_NAME;
		if (status == Status.DEL) {
			db.delete(dbName, "article_id=?", new String[] { this.article_id
					+ "" });
			return;
		}
		ContentValues cv = new ContentValues();
		cv.put("article_id", article_id);
		cv.put("img_id", imgId);
		cv.put("img", img);
		db.insert(dbName, null, cv);
	}

}
