package com.meitu;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.meitu.utils.UniversalImageLoadTool;

public class UserInfoActivity extends BaseActivity {
	private String avatar_path = "";
	private int user_id;

	private ImageView avatar;
	// private DampView view;
	private TextView txt_title;
	private UserInfoView user_info_view;
	private UserInfoDongTai info_dongtai_view;
	private int mCurrentIndex = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_info);
		avatar_path = getIntent().getStringExtra("avatar_path");
		user_id = getIntent().getIntExtra("user_id", 0);
		initView();
		setValue();
	}

	private void initView() {
		avatar = (ImageView) findViewById(R.id.img_avatar);
		// view = (DampView) findViewById(R.id.scrollView1);
		// view.setImageView(avatar);
		txt_title = (TextView) findViewById(R.id.title_txt);
		setListener();
	}

	private void setListener() {
	}

	private void setValue() {
		txt_title.setText("个人信息");
		UniversalImageLoadTool.disPlay(avatar_path, avatar,
				R.drawable.picture_default_head);
	}

	public int getUser_id() {
		return user_id;
	}
}
