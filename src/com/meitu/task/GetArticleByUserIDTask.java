package com.meitu.task;

import com.meitu.data.ArticleList;
import com.meitu.data.enums.RetError;

public class GetArticleByUserIDTask extends
		BaseAsyncTask<ArticleList, Void, RetError> {
	private ArticleList list;

	private int uid;
	private String refushTime = "";

	public GetArticleByUserIDTask(int uid, String refushTime) {
		this.uid = uid;
		this.refushTime = refushTime;
	}

	@Override
	protected RetError doInBackground(ArticleList... params) {
		list = params[0];
		RetError ret = list.getArticlesByUserID(uid, refushTime);
		return ret;
	}

}
