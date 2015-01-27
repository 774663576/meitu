package com.meitu.parser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.meitu.data.Article;
import com.meitu.data.ArticleImage;
import com.meitu.data.result.Result;
import com.meitu.utils.SharedUtils;

public class UploadArticleParser implements IParser {

	@Override
	public Result parse(JSONObject jsonObj) throws Exception {
		if (jsonObj == null) {
			return Result.defContentErrorResult();
		}

		if (!jsonObj.has("article_id")) {
			return Result.defContentErrorResult();
		}
		String time = jsonObj.getString("time");
		SharedUtils.setGrowthLastRequestTime(time);
		Article g = new Article();
		List<ArticleImage> imgs = new ArrayList<ArticleImage>();
		int article_id = jsonObj.getInt("article_id");
		JSONArray jsonArr = jsonObj.getJSONArray("images");
		for (int i = 0; i < jsonArr.length(); i++) {
			JSONObject obj = (JSONObject) jsonArr.opt(i);
			int id = obj.getInt("img_id");
			String img = obj.getString("img_url");
			ArticleImage gImage = new ArticleImage(article_id, id, img);
			imgs.add(gImage);
		}
		g.setImages(imgs);
		g.setPublished(time);
		g.setLast_update_time(time);
		Result ret = new Result();
		ret.setData(g);
		return ret;
	}

}
