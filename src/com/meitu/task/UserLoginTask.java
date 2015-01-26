package com.meitu.task;

import com.meitu.data.User;
import com.meitu.data.enums.RetError;

public class UserLoginTask extends BaseAsyncTask<User, Void, RetError> {

	private User user;

	@Override
	protected RetError doInBackground(User... params) {
		user = params[0];
		RetError ret = user.login();
		return ret;
	}

}
