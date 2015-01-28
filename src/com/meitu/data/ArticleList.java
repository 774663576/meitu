package com.meitu.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.meitu.data.enums.RetError;
import com.meitu.data.enums.RetStatus;
import com.meitu.data.result.ApiRequest;
import com.meitu.data.result.Result;
import com.meitu.db.Const;
import com.meitu.parser.ArticleListParser;
import com.meitu.parser.IParser;

public class ArticleList extends AbstractData {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final String ARTICLE_LIST_API = "/articles.do";
	private final int ARTICLE_COUNT = 20;

	private List<Article> articles = new ArrayList<Article>();
	private List<Article> requestArticles = new ArrayList<Article>();
	private List<Article> writeArticles = new ArrayList<Article>();

	private String refushTime = "0";
	private int refushState = 1;// 1 上拉刷新 2 加载更多

	public List<Article> getRequestArticles() {
		return requestArticles;
	}

	public void setRequestArticles(List<Article> requestArticles) {
		this.requestArticles = requestArticles;
	}

	public int getRefushState() {
		return refushState;
	}

	public void setRefushState(int refushState) {
		this.refushState = refushState;
	}

	public String getRefushTime() {
		return refushTime;
	}

	public void setRefushTime(String refushTime) {
		this.refushTime = refushTime;
	}

	public List<Article> getArticles() {
		sort();
		return articles;
	}

	public void setArticles(List<Article> articles) {
		this.articles = articles;
	}

	public List<Article> getWriteArticles() {
		return writeArticles;
	}

	public void setWriteArticles(List<Article> writeArticles) {
		this.writeArticles = writeArticles;
	}

	private void sort() {
		Collections.sort(articles, new Comparator<Article>() {
			@Override
			public int compare(Article lhs, Article rhs) {
				return rhs.getPublished().compareTo(lhs.getPublished());
			}
		});

	}

	public RetError refushArticles() {
		requestArticles.clear();
		IParser parser = new ArticleListParser();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("refushTime", refushTime);
		params.put("refushState", refushState);
		Result ret = ApiRequest.request(ARTICLE_LIST_API, params, parser);
		if (ret.getStatus() == RetStatus.SUCC) {
			ArticleList lists = (ArticleList) ret.getData();
			requestArticles.addAll(lists.getArticles());
			updateGrowth(lists.getArticles());
			writeArticles.addAll(lists.getArticles());
			return RetError.NONE;
		} else {
			return ret.getErr();
		}
	}

	private void delById(int article_id) {
		for (Iterator<Article> it = articles.iterator(); it.hasNext();) {
			if (it.next().getArticle_id() == article_id) {
				it.remove();
				break;
			}
		}

	}

	private void updateGrowth(List<Article> lists) {
		for (Article article : lists) {
			if (article.getStatus() == Status.UPDATE) {
				delById(article.getArticle_id());
			}
			this.articles.add(article);
		}
	}

	public void writeGrowth(SQLiteDatabase db) {
		List<Article> newArticles = new ArrayList<Article>();
		for (Article article : writeArticles) {
			if (article.getStatus() == Status.UPDATE) {
				db.delete(Const.ARTICLE_TABLE_NAME, "article_id=?",
						new String[] { article.getArticle_id() + "" });
				db.delete(Const.ARTICLE_IMAGE_TABLE_NAME, "article_id=?",
						new String[] { article.getArticle_id() + "" });
				db.delete(Const.COMMENT_TABLE_NAME, "growth_id=?",
						new String[] { article.getArticle_id() + "" });
				db.delete(Const.PRAISE_TABLE_NAME, "growth_id=?",
						new String[] { article.getArticle_id() + "" });
			}
			newArticles.add(article);
		}
		for (Article article : newArticles) {
			article.write(db);
		}
		writeArticles.clear();
		if (refushState == 1) {
			Cursor cursor = db.query(Const.ARTICLE_TABLE_NAME,
					new String[] { "article_id" }, null, null, null, null,
					"article_id desc;");
			if (cursor.getCount() > ARTICLE_COUNT) {
				cursor.move(ARTICLE_COUNT);
				int article_id = cursor.getInt(cursor
						.getColumnIndex("article_id"));
				db.delete(Const.ARTICLE_TABLE_NAME, "article_id< ? ",
						new String[] { article_id + "" });
				db.delete(Const.ARTICLE_IMAGE_TABLE_NAME, "article_id< ? ",
						new String[] { article_id + "" });
				db.delete(Const.PRAISE_TABLE_NAME, "article_id<?",
						new String[] { article_id + "" });
				db.delete(Const.COMMENT_TABLE_NAME, "article_id< ? ",
						new String[] { article_id + "" });
			}
		}
	}

	@Override
	public void read(SQLiteDatabase db) {
		// read growth basic info
		Cursor cursor = db.query(Const.ARTICLE_TABLE_NAME, new String[] {
				"article_id", "content", "publisher_id", "time",
				"publisher_name", "publisher_avatar", "isPraise",
				"praise_count", "last_update_time" }, null, null, null, null,
				null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			for (int j = 0; j < cursor.getCount(); j++) {
				int article_id = cursor.getInt(cursor
						.getColumnIndex("article_id"));
				int publisher = cursor.getInt(cursor
						.getColumnIndex("publisher_id"));
				String content = cursor.getString(cursor
						.getColumnIndex("content"));
				String publisher_name = cursor.getString(cursor
						.getColumnIndex("publisher_name"));
				String publisher_avatar = cursor.getString(cursor
						.getColumnIndex("publisher_avatar"));
				String time = cursor.getString(cursor.getColumnIndex("time"));
				String last_update_time = cursor.getString(cursor
						.getColumnIndex("last_update_time"));
				int isPraise = cursor.getInt(cursor.getColumnIndex("isPraise"));
				int praise_count = cursor.getInt(cursor
						.getColumnIndex("praise_count"));
				Article article = new Article();
				article.setContent(content);
				article.setArticle_id(article_id);
				article.setPublished(time);
				article.setPublisher_id(publisher);
				article.setPublisher_avatar(publisher_avatar);
				article.setPublisher_name(publisher_name);
				article.setPraise(isPraise > 0);
				article.setPraise_count(praise_count);
				article.setLast_update_time(last_update_time);
				// read article images
				List<ArticleImage> images = new ArrayList<ArticleImage>();
				Cursor cursor2 = db.query(Const.ARTICLE_IMAGE_TABLE_NAME,
						new String[] { "img_id", "img" }, "article_id=?",
						new String[] { article_id + "" }, null, null, null);
				if (cursor2.getCount() > 0) {
					cursor2.moveToFirst();
					for (int i = 0; i < cursor2.getCount(); i++) {
						int imgId = cursor2.getInt(cursor2
								.getColumnIndex("img_id"));
						String img = cursor2.getString(cursor2
								.getColumnIndex("img"));
						ArticleImage image = new ArticleImage(article_id,
								imgId, img);
						images.add(image);
						cursor2.moveToNext();
					}
					article.setImages(images);
				}
				cursor2.close();

				// read comment

				List<Comment> comments = new ArrayList<Comment>();

				Cursor cursor3 = db.query(Const.COMMENT_TABLE_NAME,
						new String[] { "comment_id", "comment_time",
								"comment_content", "publisher_id",
								"publisher_name", "publisher_avatar",
								"reply_someone_id", "reply_someone_name" },
						"article_id=?", new String[] { article_id + "" }, null,
						null, null);
				if (cursor3.getCount() > 0) {
					cursor3.moveToFirst();
					for (int i = 0; i < cursor3.getCount(); i++) {
						int comment_id = cursor3.getInt(cursor3
								.getColumnIndex("comment_id"));
						String comment_time = cursor3.getString(cursor3
								.getColumnIndex("comment_time"));
						String comment_content = cursor3.getString(cursor3
								.getColumnIndex("comment_content"));
						int publisher_id = cursor3.getInt(cursor3
								.getColumnIndex("publisher_id"));
						publisher_name = cursor3.getString(cursor3
								.getColumnIndex("publisher_name"));
						publisher_avatar = cursor3.getString(cursor3
								.getColumnIndex("publisher_avatar"));
						int reply_someone_id = cursor3.getInt(cursor3
								.getColumnIndex("reply_someone_id"));
						String reply_someone_name = cursor3.getString(cursor3
								.getColumnIndex("reply_someone_name"));
						Comment comment = new Comment();
						comment.setComment_content(comment_content);
						comment.setComment_id(comment_id);
						comment.setPublisher_name(publisher_name);
						comment.setPublisher_avatar(publisher_avatar);
						comment.setComment_time(comment_time);
						comment.setPublisher_id(publisher_id);
						comment.setReply_someone_name(reply_someone_name);
						comment.setReply_someone_id(reply_someone_id);
						comments.add(comment);
						cursor3.moveToNext();
					}
					sortComment(comments);
					article.setComments(comments);
				}
				cursor3.close();
				List<Praise> praises = new ArrayList<Praise>();
				Cursor cursor4 = db.query(Const.PRAISE_TABLE_NAME,
						new String[] { "user_id", "user_avatar" },
						"article_id=?", new String[] { article_id + "" }, null,
						null, null);
				if (cursor4.getCount() > 0) {
					cursor4.moveToFirst();
					for (int i = 0; i < cursor4.getCount(); i++) {
						int user_id = cursor4.getInt(cursor4
								.getColumnIndex("user_id"));
						String user_avatar = cursor4.getString(cursor4
								.getColumnIndex("user_avatar"));
						Praise praise = new Praise();
						praise.setUser_avatar(user_avatar);
						praise.setUser_id(user_id);
						praises.add(praise);
						cursor4.moveToNext();
					}
					article.setPraises(praises);
				}
				cursor4.close();
				articles.add(article);
				cursor.moveToNext();
			}
			cursor.close();
		}
	}

	public void sortComment(List<Comment> comments) {
		Collections.sort(comments, new Comparator<Comment>() {
			@Override
			public int compare(Comment lhs, Comment rhs) {
				return rhs.getComment_time().compareTo(lhs.getComment_time());
			}
		});

	}
}
