package com.meitu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.meitu.Interface.AbstractTaskPostCallBack;
import com.meitu.data.User;
import com.meitu.data.enums.RetError;
import com.meitu.showbigpic.ImagePagerActivity;
import com.meitu.task.GetUserInfoTask;
import com.meitu.utils.Constants;
import com.meitu.utils.DialogUtil;
import com.meitu.utils.SharedUtils;
import com.meitu.utils.UniversalImageLoadTool;
import com.meitu.utils.Utils;
import com.meitu.view.DampView;

public class UserInfoActivity extends BaseActivity {
	private String avatar_path = "";
	private static int user_id;

	private ImageView avatar;
	private DampView view;
	private TextView txt_title;
	private TextView txt_name;
	private TextView txt_gender;
	private TextView txt_birthday;
	private TextView txt_dongtai;

	private Dialog dialog;
	private User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_info);
		avatar_path = getIntent().getStringExtra("avatar_path");
		user_id = getIntent().getIntExtra("user_id", 0);
		user = new User();
		user.setUser_id(user_id);
		initView();
		setValue();
	}

	private void initView() {
		txt_dongtai = (TextView) findViewById(R.id.txt_dongtai);
		txt_birthday = (TextView) findViewById(R.id.txt_birthday);
		txt_gender = (TextView) findViewById(R.id.txt_gender);
		txt_name = (TextView) findViewById(R.id.txt_user_name);
		avatar = (ImageView) findViewById(R.id.img_avatar);
		view = (DampView) findViewById(R.id.scrollView1);
		view.setImageView(avatar);
		txt_title = (TextView) findViewById(R.id.title_txt);
		setListener();
	}

	private void setListener() {
		txt_dongtai.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(UserInfoActivity.this,
						UserArticlesActivity.class)
						.putExtra("user_id", user_id));
				Utils.leftOutRightIn(UserInfoActivity.this);
			}
		});
		avatar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				List<String> imgUrl = new ArrayList<String>();
				imgUrl.add(avatar_path);
				Intent intent = new Intent(UserInfoActivity.this,
						ImagePagerActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable(Constants.EXTRA_IMAGE_URLS,
						(Serializable) imgUrl);
				intent.putExtras(bundle);
				intent.putExtra(Constants.EXTRA_IMAGE_INDEX, 1);
				startActivity(intent);
			}
		});
	}

	private void setValue() {
		txt_title.setText("个人信息");
		UniversalImageLoadTool.disPlay(avatar_path, avatar,
				R.drawable.picture_default_head);
		getUserInfo();
	}

	private void getUserInfo() {
		dialog = DialogUtil.createLoadingDialog(this);
		dialog.show();
		GetUserInfoTask task = new GetUserInfoTask();
		task.setmCallBack(new AbstractTaskPostCallBack<RetError>() {
			@Override
			public void taskFinish(RetError result) {
				if (dialog != null) {
					dialog.dismiss();
				}
				if (result != RetError.NONE) {
					return;
				}
				txt_birthday.setText(user.getUser_birthday());
				txt_gender.setText(user.getUser_gender());
				txt_name.setText(user.getUser_name());
				if ("男".equals(user.getUser_gender())) {
					txt_dongtai.setText("他的动态");
				} else {
					txt_dongtai.setText("她的动态");

				}
			}
		});

		task.executeParallel(user);
	}

}
