package com.meitu.parser;

import org.json.JSONObject;

import com.meitu.data.User;
import com.meitu.data.result.Result;

public class UserLoginPaser implements IParser {

	@Override
	public Result parse(JSONObject jsonObj) throws Exception {
		if (jsonObj == null) {
			return Result.defContentErrorResult();
		}

		String jsonArr = jsonObj.getString("user");
		if (jsonArr == null) {
			return Result.defContentErrorResult();
		}
		JSONObject obj = new JSONObject(jsonArr);
		int userID = obj.getInt("user_id");
		String userName = obj.getString("user_name");
		String userAvatar = obj.getString("user_avatar");
		String userGender = obj.getString("user_gender");
		String userBirthday = obj.getString("user_birthday");
		User user = new User();
		user.setUser_avatar(userAvatar);
		user.setUser_birthday(userBirthday);
		user.setUser_gender(userGender);
		user.setUser_id(userID);
		user.setUser_name(userName);
		Result<User> ret = new Result<User>();
		ret.setData(user);
		return ret;
	}
}
