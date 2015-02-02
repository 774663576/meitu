package com.meitu;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.meitu.utils.UniversalImageLoadTool;
import com.meitu.view.DampView;

public class UserInfoActivity extends BaseActivity {
	private String avatar_path = "";
	private int user_id;

	private ImageView avatar;
	private ViewFlipper mVfFlipper;
	private DampView view;
	private TextView txt_title;
	private TextView txt_info;
	private TextView txt_dongtai;
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
		mVfFlipper = (ViewFlipper) findViewById(R.id.viewflipper);
		mVfFlipper.setDisplayedChild(0);
		avatar = (ImageView) findViewById(R.id.img_avatar);
		view = (DampView) findViewById(R.id.scrollView1);
		view.setImageView(avatar);
		txt_title = (TextView) findViewById(R.id.title_txt);
		txt_dongtai = (TextView) findViewById(R.id.txt_dongtai);
		txt_info = (TextView) findViewById(R.id.txt_info);
		setListener();
		initCurrentView();
	}

	private void setListener() {
		txt_dongtai.setOnClickListener(this);
		txt_info.setOnClickListener(this);
	}

	private void setValue() {
		txt_title.setText("个人信息");
		UniversalImageLoadTool.disPlay(avatar_path, avatar,
				R.drawable.picture_default_head);
	}

	private UserInfo initCurrentView() {
		switch (mCurrentIndex) {
		case 1:
			if (user_info_view == null) {
				user_info_view = new UserInfoView(this,
						mVfFlipper.getChildAt(0));
			}
			return user_info_view;
		case 2:
			if (info_dongtai_view == null) {
				info_dongtai_view = new UserInfoDongTai(this,
						mVfFlipper.getChildAt(1));
			}
			return info_dongtai_view;
		default:
			break;
		}
		return null;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.txt_info:
			initCurrentView();
			mVfFlipper.setDisplayedChild(0);
			break;
		case R.id.txt_dongtai:
			initCurrentView();
			mVfFlipper.setDisplayedChild(1);
			break;

		default:
			break;
		}
	}

	public int getUser_id() {
		return user_id;
	}
}
