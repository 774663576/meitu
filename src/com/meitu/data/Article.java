package com.meitu.data;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Comment;

import com.meitu.data.enums.RetError;
import com.meitu.data.enums.RetStatus;
import com.meitu.data.result.ApiRequest;
import com.meitu.data.result.Result;
import com.meitu.parser.IParser;
import com.meitu.parser.UploadArticleParser;
import com.meitu.utils.BitmapUtils;

public class Article extends AbstractData {
	private static final String UPLOAD_ARTICLE_API = "/addarticle.do";
	private int article_id = 0;// 成长id
	private int publisher_id = 0;// 发布者id
	private String content = "";// 内容
	private String published = ""; // 发布时间
	private String publisher_name = "";
	private String publisher_avatar = "";
	private List<ArticleImage> images = new ArrayList<ArticleImage>();
	private List<Comment> comments = new ArrayList<Comment>();
	private String last_update_time = "";
	private List<Praise> praises = new ArrayList<Praise>();
	private boolean isPraise;
	private int praise_count;
	private boolean isUploading = false;

	public boolean isUploading() {
		return isUploading;
	}

	public void setUploading(boolean isUploading) {
		this.isUploading = isUploading;
	}

	public int getPraise_count() {
		return praise_count;
	}

	public void setPraise_count(int praise_count) {
		this.praise_count = praise_count;
	}

	public boolean isPraise() {
		return isPraise;
	}

	public void setPraise(boolean isPraise) {
		this.isPraise = isPraise;
	}

	public int getArticle_id() {
		return article_id;
	}

	public void setArticle_id(int article_id) {
		this.article_id = article_id;
	}

	public int getPublisher_id() {
		return publisher_id;
	}

	public void setPublisher_id(int publisher_id) {
		this.publisher_id = publisher_id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPublished() {
		return published;
	}

	public void setPublished(String published) {
		this.published = published;
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

	public List<ArticleImage> getImages() {
		return images;
	}

	public void setImages(List<ArticleImage> images) {
		this.images = images;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public String getLast_update_time() {
		return last_update_time;
	}

	public void setLast_update_time(String last_update_time) {
		this.last_update_time = last_update_time;
	}

	public List<Praise> getPraises() {
		return praises;
	}

	public void setPraises(List<Praise> praises) {
		this.praises = praises;
	}

	// public RetError praiseGrowth() {
	// IParser parser = new StringParser("praise_count");
	// Map<String, Object> params = new HashMap<String, Object>();
	// params.put("article_id", article_id);
	// params.put("publisher_id", publisher_id);
	// params.put("user_name", SharedUtils.getAPPUserName());
	// Result ret = ApiRequest.request(PRAISE_GROWTH_API, params, parser);
	// if (ret.getStatus() == RetStatus.SUCC) {
	// StringResult sr = (StringResult) ret;
	// this.praise_count = Integer.valueOf(sr.getStr());
	// return RetError.NONE;
	// } else {
	// return ret.getErr();
	// }
	// }

	// public RetError getGrowthByGrwothID() {
	// IParser parser = new GrowthPaser();
	// Map<String, Object> params = new HashMap<String, Object>();
	// params.put("circle_id", cid);
	// params.put("growth_id", growth_id);
	// Result ret = ApiRequest.request(GET_GROWTH_API, params, parser);
	// if (ret.getStatus() == RetStatus.SUCC) {
	// GrowthList lists = (GrowthList) ret.getData();
	// Growth g = lists.getGrowths().get(0);
	// publisher_id = g.getPublisher_id();
	// content = g.getContent();
	// location = g.getLocation();
	// published = g.getPublished();
	// this.cid = g.getCid();
	// publisher_name = g.getPublisher_name();
	// publisher_avatar = g.getPublisher_avatar();
	// images = g.getImages();
	// comments = g.getComments();
	// praises = g.getPraises();
	// this.last_update_time = g.getLast_update_time();
	// this.praise_count = g.getPraise_count();
	// return RetError.NONE;
	// } else {
	// return ret.getErr();
	// }
	// }

	// public RetError cancelpraiseGrowth() {
	// IParser parser = new StringParser("praise_count");
	// Map<String, Object> params = new HashMap<String, Object>();
	// params.put("growth_id", growth_id);
	// Result ret = ApiRequest.request(CANCEL_PRAISE_GROWTH_API, params,
	// parser);
	// if (ret.getStatus() == RetStatus.SUCC) {
	// StringResult sr = (StringResult) ret;
	// this.praise_count = Integer.valueOf(sr.getStr());
	// return RetError.NONE;
	// } else {
	// return ret.getErr();
	// }
	// }

	public RetError uploadForAdd() {
		List<File> bytesimg = new ArrayList<File>();
		for (ArticleImage img : this.images) {
			File file = BitmapUtils.getImageFile(img.getImg());
			if (file == null) {
			}
			bytesimg.add(file);
		}
		IParser parser = new UploadArticleParser();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("content", content);
		params.put("time", published);
		Result ret = ApiRequest.uploadFileArrayWithToken(UPLOAD_ARTICLE_API,
				params, bytesimg, parser);
		delGorwthImgFile(bytesimg);
		if (ret.getStatus() == RetStatus.SUCC) {
			Article g = (Article) ret.getData();
			this.images.clear();
			this.images = g.getImages();
			this.published = g.getPublished();
			this.last_update_time = g.getPublished();
			return RetError.NONE;
		} else {
			return ret.getErr();
		}
	}

	private void delGorwthImgFile(List<File> files) {
		for (File file : files) {
			if (file.exists()) {
				file.delete();
			}
		}
	}

	// @Override
	// public void write(SQLiteDatabase db) {
	// String dbName = Const.GROWTHS_TABLE_NAME;
	// ContentValues cv = new ContentValues();
	// cv.put("growth_id", this.growth_id);
	// cv.put("cid", this.cid);
	// cv.put("publisher_id", this.publisher_id);
	// cv.put("content", this.content);
	// cv.put("time", this.published);
	// cv.put("publisher_name", this.publisher_name);
	// cv.put("publisher_avatar", this.publisher_avatar);
	// cv.put("isPraise", this.isPraise);
	// cv.put("praise_count", this.praise_count);
	// cv.put("last_update_time", this.last_update_time);
	// if (state == GrowthState.DEL) {
	// db.delete(dbName, "growth_id=? ", new String[] { this.growth_id
	// + "" });
	// for (GrowthImage img : this.images) {
	// img.setStatus(Status.DEL);
	// img.write(db);
	// }
	// for (Comment comment : this.comments) {
	// comment.setStatus(Status.DEL);
	// comment.write(db);
	// }
	// for (Praise praise : this.praises) {
	// praise.setStatus(Status.DEL);
	// praise.write(db);
	// }
	// return;
	// }
	// if (state == GrowthState.UPDATE) {
	// db.update(dbName, cv, "growth_id=? ", new String[] { this.growth_id
	// + "" });
	// return;
	// }
	// db.insert(dbName, null, cv);
	// for (GrowthImage img : this.images) {
	// img.write(db);
	// }
	// for (Comment comment : this.comments) {
	// comment.write(db);
	// }
	// for (Praise praise : this.praises) {
	// praise.write(db);
	// }
	// }
}
