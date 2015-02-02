package com.meitu.Interface;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import com.meitu.UserInfoActivity;
import com.meitu.utils.Utils;

public class OnAvatarClick implements OnClickListener {
	private int user_id;
	private Context mContext;
	private String avatar_path = "";

	public OnAvatarClick(String avatar_path, int user_id, Context context) {
		this.user_id = user_id;
		this.mContext = context;
		this.avatar_path = avatar_path;
	}

	@Override
	public void onClick(View v) {
		if (user_id < 0) {
			return;
		}
		mContext.startActivity(new Intent(mContext, UserInfoActivity.class)
				.putExtra("avatar_path", avatar_path).putExtra("user_id",
						user_id));
		Utils.leftOutRightIn(mContext);
	}
}
