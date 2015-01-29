package com.meitu.Interface;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;

public class OnAvatarClick implements OnClickListener {
	private int user_id;
	private Context mContext;

	public OnAvatarClick(int user_id, Context context) {
		this.user_id = user_id;
		this.mContext = context;
	}

	@Override
	public void onClick(View v) {
		if (user_id < 0) {
			return;
		}

	}
}
