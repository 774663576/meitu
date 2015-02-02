package com.meitu.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.meitu.data.Article;
import com.meitu.data.ArticleImage;
import com.meitu.data.ArticleList;
import com.meitu.data.Comment;
import com.meitu.data.Praise;
import com.meitu.data.result.Result;
import com.meitu.utils.SharedUtils;

public class ArticleListParser implements IParser {

	@Override
	public Result parse(JSONObject jsonObj) throws Exception {
		if (jsonObj == null) {
			return Result.defContentErrorResult();
		}
		JSONArray jsonArr = jsonObj.getJSONArray("articles");
		String last_req_time = jsonObj.getString("lastReqTime");
		SharedUtils.setGrowthLastRequestTime(last_req_time);
		if (jsonArr == null) {
			return Result.defContentErrorResult();
		}
		List<Article> articles = new ArrayList<Article>();
		for (int i = 0; i < jsonArr.length(); i++) {
			JSONObject obj = (JSONObject) jsonArr.opt(i);
			// articles info
			int article_id = obj.getInt("article_id");
			int publisher = obj.getInt("publisher_id");
			String content = obj.getString("content");
			String published = obj.getString("time");
			String publisher_name = obj.getString("publisher_name");
			String publisher_avatar = obj.getString("publisher_avatar");
			int isPraise = obj.getInt("isPraise");
			int praise_count = obj.getInt("praise_count");
			String state = obj.getString("state");
			String last_update_time = obj.getString("last_update_time");

			// articles images
			JSONArray jsonImages = obj.getJSONArray("images");
			List<ArticleImage> images = new ArrayList<ArticleImage>();
			for (int j = 0; j < jsonImages.length(); j++) {
				JSONObject obj2 = (JSONObject) jsonImages.opt(j);
				int imgId = obj2.getInt("img_id");
				String img = obj2.getString("img_url");
				ArticleImage gimg = new ArticleImage(article_id, imgId, img);
				images.add(gimg);
			}
			// comments
			JSONArray commentsJson = obj.getJSONArray("comments");
			List<Comment> comments = new ArrayList<Comment>();
			for (int j = 0; j < commentsJson.length(); j++) {
				JSONObject obj2 = (JSONObject) commentsJson.opt(j);
				int comment_id = obj2.getInt("comment_id");
				int publisher_id = obj2.getInt("publisher_id");
				String comment_time = obj2.getString("comment_time");
				String comment_content = obj2.getString("comment_content");
				String comm_publisher_name = obj2.getString("publisher_name");
				String comm_publisher_avatar = obj2
						.getString("publisher_avatar");
				String reply_someone_name = obj2
						.getString("reply_someone_name");
				int reply_someone_id = obj2.getInt("reply_someone_id");
				Comment comment = new Comment();
				comment.setComment_content(comment_content);
				comment.setComment_id(comment_id);
				comment.setComment_time(comment_time);
				comment.setPublisher_id(publisher_id);
				comment.setArticle_id(article_id);
				comment.setPublisher_avatar(comm_publisher_avatar);
				comment.setPublisher_name(comm_publisher_name);
				comment.setReply_someone_name(reply_someone_name);
				comment.setReply_someone_id(reply_someone_id);
				comments.add(comment);
			}
			sortComment(comments);
<<<<<<< HEAD
			// JSONArray jsonPraise = obj.getJSONArray("praises");
			// List<Praise> praises = new ArrayList<Praise>();
			// for (int k = 0; k < jsonPraise.length(); k++) {
			// JSONObject obj2 = (JSONObject) jsonPraise.opt(k);
			// int user_id = obj2.getInt("user_id");
			// String user_avatar = obj2.getString("user_avatar");
			// Praise praise = new Praise();
			// praise.setUser_avatar(user_avatar);
			// praise.setUser_id(user_id);
			// praise.setGrowth_id(growth_id);
			// praises.add(praise);
			// }
=======
			JSONArray jsonPraise = obj.getJSONArray("praises");
			List<Praise> praises = new ArrayList<Praise>();
			for (int k = 0; k < jsonPraise.length(); k++) {
				JSONObject obj2 = (JSONObject) jsonPraise.opt(k);
				int user_id = obj2.getInt("user_id");
				String user_avatar = obj2.getString("user_avatar");
				Praise praise = new Praise();
				praise.setUser_avatar(user_avatar);
				praise.setUser_id(user_id);
				praise.setArticle_id(article_id);
				praises.add(praise);
			}
>>>>>>> 22a3b7a07d3ace935c4bc0b848969f1b65c59187

			Article article = new Article();
			article.setContent(content);
			article.setArticle_id(article_id);
			article.setImages(images);
			article.setComments(comments);
			article.setPublished(published);
			article.setPublisher_id(publisher);
			article.setPublisher_avatar(publisher_avatar);
			article.setPublisher_name(publisher_name);
			article.setPraise(isPraise > 0);
			article.setPraise_count(praise_count);
			article.setPraises(praises);
			// article.setStatus(AbstractData.convert(state));
			article.setLast_update_time(last_update_time);
			articles.add(article);

		}
		ArticleList al = new ArticleList();
		al.setArticles(articles);
		Result ret = new Result();
		ret.setData(al);
		return ret;
	}

	private void sortComment(List<Comment> comments) {
		Collections.sort(comments, new Comparator<Comment>() {
			@Override
			public int compare(Comment lhs, Comment rhs) {
				return rhs.getComment_time().compareTo(lhs.getComment_time());
			}
		});

	}
}
