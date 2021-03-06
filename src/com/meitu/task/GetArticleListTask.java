package com.meitu.task;

import android.database.sqlite.SQLiteDatabase;

import com.meitu.data.ArticleList;
import com.meitu.data.enums.RetError;
import com.meitu.db.DBUtils;

public class GetArticleListTask extends
		BaseAsyncTask<ArticleList, Void, RetError> {
	private ArticleList list;

	@Override
	protected RetError doInBackground(ArticleList... params) {
		list = params[0];
		RetError ret = list.refushArticles();
		if (ret == RetError.NONE) {
			 SQLiteDatabase db = DBUtils.getDBsa(2);
			 db.beginTransaction();
			 list.writeGrowth(db);
			 db.setTransactionSuccessful();
			 db.endTransaction();
		}
		return ret;
	}

}
