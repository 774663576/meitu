package com.meitu;

import android.content.Context;
import android.view.View;

public abstract class UserInfo {
	private View contentRootView;
	protected UserInfoActivity mActivity;
	protected Context mContext;

	public UserInfo(UserInfoActivity activity, View contentRootView) {
		this.contentRootView = contentRootView;
		this.mActivity = activity;
		this.mContext = (Context) activity;
		initView();
		setListener();
	}

	public abstract void initView();

	public abstract void setListener();

	public View findViewById(int id) {
		return contentRootView.findViewById(id);

	}

}
