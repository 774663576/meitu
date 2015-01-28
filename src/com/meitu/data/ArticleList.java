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
	private List<Article> writeArticles = new ArrayList<Article>();

	private String refushTime = "0";
	private int refushState = 1;// 1 上拉刷新 2 加载更多

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
		IParser parser = new ArticleListParser();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("refushTime", refushTime);
		params.put("refushState", refushState);
		Result ret = ApiRequest.request(ARTICLE_LIST_API, params, parser);
		if (ret.getStatus() == RetStatus.SUCC) {
			ArticleList lists = (ArticleList) ret.getData();
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
		// if (refushState == 1) {
		// Cursor cursor = db.query(Const.ARTICLE_TABLE_NAME,
		// new String[] { "_id" }, "cid=?", new String[] { cid + "" },
		// null, null, null);
		// if (cursor.getCount() > ARTICLE_COUNT) {
		// cursor.move(GROUTH_COUNT);
		// int id = cursor.getInt(cursor.getColumnIndex("_id"));
		// db.delete(Const.GROWTHS_TABLE_NAME, "_id> ? and cid=?",
		// new String[] { id + "", cid + "" });
		// db.delete(Const.GROWTH_IMAGE_TABLE_NAME, "_id> ? and cid=?",
		// new String[] { id + "", cid + "" });
		// db.delete(Const.PRAISE_TABLE_NAME, "_id> ?", new String[] { id
		// + "" });
		// db.delete(Const.COMMENT_TABLE_NAME, "_id> ? ",
		// new String[] { id + "" });
		// }
		// }
	}
}
