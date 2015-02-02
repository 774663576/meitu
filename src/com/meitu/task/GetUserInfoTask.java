package com.meitu.task;

import com.meitu.data.User;
import com.meitu.data.enums.RetError;

public class GetUserInfoTask extends BaseAsyncTask<User, Void, RetError> {
	private User user;

	@Override
	protected RetError doInBackground(User... params) {
		user = params[0];
		RetError ret = user.getUserInfo();
		return ret;
	}

}
