package com.meitu.task;

import com.meitu.data.Article;
import com.meitu.data.enums.ArticleState;
import com.meitu.data.enums.RetError;
import com.meitu.db.DBUtils;

public class PraiseTask extends BaseAsyncTask<Article, Void, RetError> {
	private Article article;

	@Override
	protected RetError doInBackground(Article... params) {
		article = params[0];
		RetError ret = article.praise();
		if (ret == RetError.NONE) {
			article.setPraise(!article.isPraise());
			article.setState(ArticleState.UPDATE);
			article.write(DBUtils.getDBsa(2));
		}
		return ret;
	}

}
