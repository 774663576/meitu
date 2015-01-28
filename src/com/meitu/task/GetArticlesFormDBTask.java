package com.meitu.task;

import com.meitu.data.ArticleList;
import com.meitu.data.enums.RetError;
import com.meitu.db.DBUtils;

public class GetArticlesFormDBTask extends
		BaseAsyncTask<ArticleList, Void, RetError> {
	private ArticleList list;

	@Override
	protected RetError doInBackground(ArticleList... params) {
		list = params[0];
		list.read(DBUtils.getDBsa(1));
		return RetError.NONE;
	}

}
