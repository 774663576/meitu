package com.meitu.task;

import com.meitu.data.Article;
import com.meitu.data.enums.RetError;

public class UpLoadArticleTask extends BaseAsyncTask<Article, Void, RetError> {
	private Article article;

	@Override
	protected RetError doInBackground(Article... params) {
		article = params[0];
		RetError ret = article.uploadForAdd();
		// if (ret == RetError.NONE) {
		// article.write(DBUtils.getDBsa(2));
		// }
		return ret;
	}
}
