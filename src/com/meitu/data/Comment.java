package com.meitu.data;

import java.util.HashMap;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.meitu.data.enums.RetError;
import com.meitu.data.enums.RetStatus;
import com.meitu.data.result.ApiRequest;
import com.meitu.data.result.Result;
import com.meitu.data.result.StringResult;
import com.meitu.db.Const;
import com.meitu.parser.IParser;
import com.meitu.parser.SimpleParser;
import com.meitu.parser.StringParser;
import com.meitu.utils.SharedUtils;

public class Comment extends AbstractData {
	private final String COMMENT_API = "CommentServlet";
	private final String DELETE_COMMENT_API = "DeleteCommentServlet";

	private int comment_id;
	private int article_id;
	private int publisher_id;
	private String comment_content = "";
	private String comment_time = "";
	private String publisher_name = "";
	private String publisher_avatar = "";
	private String reply_someone_name = "";
	private int reply_someone_id = 0;

	public String getReply_someone_name() {
		return reply_someone_name;
	}

	public void setReply_someone_name(String reply_someone_name) {
		this.reply_someone_name = reply_someone_name;
	}

	public int getReply_someone_id() {
		return reply_someone_id;
	}

	public void setReply_someone_id(int reply_someone_id) {
		this.reply_someone_id = reply_someone_id;
	}

	public String getPublisher_name() {
		return publisher_name;
	}

	public void setPublisher_name(String publisher_name) {
		this.publisher_name = publisher_name;
	}

	public String getPublisher_avatar() {
		return publisher_avatar;
	}

	public void setPublisher_avatar(String publisher_avatar) {
		this.publisher_avatar = publisher_avatar;
	}

	public int getPublisher_id() {
		return publisher_id;
	}

	public void setPublisher_id(int publisher_id) {
		this.publisher_id = publisher_id;
	}

	public int getComment_id() {
		return comment_id;
	}

	public void setComment_id(int comment_id) {
		this.comment_id = comment_id;
	}

	public int getArticle_id() {
		return article_id;
	}

	public void setArticle_id(int article_id) {
		this.article_id = article_id;
	}

	public String getComment_content() {
		return comment_content;
	}

	public void setComment_content(String comment_content) {
		this.comment_content = comment_content;
	}

	public String getComment_time() {
		return comment_time;
	}

	public void setComment_time(String comment_time) {
		this.comment_time = comment_time;
	}

	@Override
	public String toString() {
		return "comment_content:" + this.comment_content;
	}

	public RetError sendComment(int growth_publisher_id) {
		IParser parser = new StringParser("comment_id");
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("comment_content", comment_content);
		params.put("article_id", article_id);
		params.put("reply_someone_name", reply_someone_name);
		params.put("reply_someone_id", reply_someone_id);
		params.put("growth_publisher_id", growth_publisher_id);
		params.put("user_name", SharedUtils.getAPPUserName());
		Result ret = ApiRequest.request(COMMENT_API, params, parser);
		if (ret.getStatus() == RetStatus.SUCC) {
			StringResult sr = (StringResult) ret;
			this.comment_id = Integer.valueOf(sr.getStr());
			return RetError.NONE;
		} else {
			return ret.getErr();
		}

	}

	public RetError deleteComment() {
		IParser parser = new SimpleParser();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("comment_id", comment_id);
		Result ret = ApiRequest.request(DELETE_COMMENT_API, params, parser);
		if (ret.getStatus() == RetStatus.SUCC) {
			this.status = Status.DEL;
			return RetError.NONE;
		} else {
			return ret.getErr();
		}
	}

	@Override
	public void write(SQLiteDatabase db) {
		String dbName = Const.COMMENT_TABLE_NAME;
		if (this.status == Status.DEL) {
			db.delete(dbName, "comment_id=?", new String[] { comment_id + "" });
			return;
		}
		ContentValues cv = new ContentValues();
		cv.put("article_id", this.article_id);
		cv.put("comment_id", this.comment_id);
		cv.put("publisher_id", this.publisher_id);
		cv.put("comment_time", this.comment_time);
		cv.put("comment_content", this.comment_content);
		cv.put("publisher_name", this.publisher_name);
		cv.put("publisher_avatar", this.publisher_avatar);
		cv.put("reply_someone_id", this.reply_someone_id);
		cv.put("reply_someone_name", this.reply_someone_name);
		db.insert(dbName, null, cv);

	}
}
