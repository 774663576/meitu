package com.meitu.task;

import com.meitu.data.Comment;
import com.meitu.data.enums.RetError;
import com.meitu.db.DBUtils;

public class SendCommentTask extends BaseAsyncTask<Comment, Void, RetError> {
	private Comment comment;
	private int article_publisher_id;

	public SendCommentTask(int article_publisher_id) {
		this.article_publisher_id = article_publisher_id;
	}

	@Override
	protected RetError doInBackground(Comment... params) {
		comment = params[0];
		RetError ret = comment.sendComment(article_publisher_id);
		if (ret == RetError.NONE) {
			comment.write(DBUtils.getDBsa(2));
		}
		return ret;
	}

}
