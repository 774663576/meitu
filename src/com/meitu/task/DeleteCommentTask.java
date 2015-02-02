package com.meitu.task;

import com.meitu.data.Comment;
import com.meitu.data.enums.RetError;
import com.meitu.db.DBUtils;

public class DeleteCommentTask extends BaseAsyncTask<Comment, Void, RetError> {
	private Comment comment;

	@Override
	protected RetError doInBackground(Comment... params) {
		comment = params[0];
		RetError ret = comment.deleteComment();
		if (ret == RetError.NONE) {
			comment.write(DBUtils.getDBsa(2));
		}
		return ret;
	}
}
