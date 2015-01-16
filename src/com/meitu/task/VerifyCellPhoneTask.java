package com.meitu.task;

import com.meitu.data.User;
import com.meitu.data.enums.RetError;

public class VerifyCellPhoneTask extends BaseAsyncTask<User, Void, RetError> {
	private User register;

	@Override
	protected RetError doInBackground(User... params) {
		register = params[0];
		RetError ret = register.vefifyCellPhone();
		return ret;
	}
}
