package com.meitu.data;

import java.io.File;
import java.util.HashMap;

import com.meitu.data.enums.RetError;
import com.meitu.data.enums.RetStatus;
import com.meitu.data.result.ApiRequest;
import com.meitu.data.result.Result;
import com.meitu.parser.IParser;
import com.meitu.parser.SimpleParser;
import com.meitu.parser.UserLoginPaser;
import com.meitu.utils.BitmapUtils;
import com.meitu.utils.SharedUtils;

public class User {
	private static final String USER_REGISTER_API = "/register.do";
	private static final String VERIFY_CELLPHONE_API = "/checkoutMobilNum.do";
	private static final String USER_LOGIN_API = "/login.do";

	private String user_phone = "";// �û�ע��绰
	private String user_name = "";// �û�ע������
	private String user_avatar = "";// �û�ע��ͷ��
	private String user_gender = "";// �û�ע���Ա�
	private String user_birthday = "";// �û�ע������
	private String user_password = "";// �û�ע������
	private int user_id = 0;

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getUser_name() {
		return user_name;
	}

	public String getUser_phone() {
		return user_phone;
	}

	public void setUser_phone(String user_phone) {
		this.user_phone = user_phone;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getUser_avatar() {
		return user_avatar;
	}

	public void setUser_avatar(String user_avatar) {
		this.user_avatar = user_avatar;
	}

	public String getUser_gender() {
		return user_gender;
	}

	public void setUser_gender(String user_gender) {
		this.user_gender = user_gender;
	}

	public String getUser_birthday() {
		return user_birthday;
	}

	public void setUser_birthday(String user_birthday) {
		this.user_birthday = user_birthday;
	}

	public String getUser_password() {
		return user_password;
	}

	public void setUser_password(String user_password) {
		this.user_password = user_password;
	}

	/**
	 * ��֤�ֻ����ֻ����Ƿ��ѱ�ע��
	 * 
	 * @return
	 */
	public RetError vefifyCellPhone() {
		IParser parser = new SimpleParser();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("user_phone", user_phone);
		Result ret = ApiRequest.request(VERIFY_CELLPHONE_API, params, parser);
		if (ret.getStatus() == RetStatus.SUCC) {
			return RetError.NONE;
		} else {
			return ret.getErr();
		}
	}

	/**
	 * �û�ע��
	 * 
	 * @return
	 */
	public RetError userRegister() {
		IParser parser = new SimpleParser();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("user_name", user_name);
		params.put("user_phone", user_phone);
		params.put("user_password", user_password);
		params.put("user_gender", user_gender);
		params.put("user_birthday", user_birthday);
		File file = BitmapUtils.getImageFile(user_avatar);
		Result ret = ApiRequest.requestWithFile(USER_REGISTER_API, params,
				file, parser);
		if (ret.getStatus() == RetStatus.SUCC) {
			return RetError.NONE;
		} else {
			return ret.getErr();
		}

	}

	public RetError login() {
		IParser parser = new UserLoginPaser();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("user_phone", user_phone);
		params.put("user_password", user_password);
		Result<?> ret = ApiRequest.request(USER_LOGIN_API, params, parser);
		if (ret.getStatus() == RetStatus.SUCC) {
			User user = (User) ret.getData();
			SharedUtils.setUid(user.getUser_id() + "");
			SharedUtils.setAPPUserAvatar(user.getUser_avatar());
			SharedUtils.setAPPUserBirthday(user.getUser_birthday());
			SharedUtils.setAPPUserGender(user.getUser_gender());
			SharedUtils.setAPPUserName(user.getUser_name());
			return RetError.NONE;
		} else {
			return ret.getErr();
		}
	}
}
