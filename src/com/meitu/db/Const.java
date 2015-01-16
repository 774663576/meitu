package com.meitu.db;

public class Const {

	public static final String ARTICLE_TABLE_NAME = "article";
	public static final String ARTICLE_TABLE_STRUCTURE = "_id integer PRIMARY KEY AUTOINCREMENT,"
			+ " article_id integer, publisher_id integer, content varchar, time varchar, publisher_name varchar, publisher_avatar varcher, isPraise integer, praise_count integer , last_update_time";

	public static final String ARTICLE_IMAGE_TABLE_NAME = "article_images";
	public static final String ARTICLE_IMAGE_TABLE_STRUCTURE = "_id integer PRIMARY KEY AUTOINCREMENT,"
			+ " article_id integer, img_id integer, img varchar";

	public static final String COMMENT_TABLE_NAME = "comments";
	public static final String COMMENT_TABLE_STRUCTURE = "_id integer PRIMARY KEY AUTOINCREMENT,"
			+ " comment_id integer, publisher_id integer, article_id integer, comment_content varchar, comment_time varchar , publisher_name varchar, publisher_avatar varcher,reply_someone_name varchar, reply_someone_id integer";

	public static final String PRAISE_TABLE_NAME = "praise";
	public static final String PRAISE_TABLE_STRUCTURE = "_id integer PRIMARY KEY AUTOINCREMENT,"
			+ " user_id integer, user_avatar integer ,article_id integer  ";
}
