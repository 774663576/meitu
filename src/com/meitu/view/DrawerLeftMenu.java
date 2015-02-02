package com.meitu.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.meitu.MainActivity;
import com.meitu.MyApplation;
import com.meitu.PublicshActivity;
import com.meitu.R;
import com.meitu.Interface.ConfirmDialog;
import com.meitu.db.DBUtils;
import com.meitu.db.DataBaseHelper;
import com.meitu.showbigpic.ImagePagerActivity;
import com.meitu.task.UpDateNewVersionTask;
import com.meitu.task.UpDateNewVersionTask.UpDateVersion;
import com.meitu.utils.Constants;
import com.meitu.utils.DialogUtil;
import com.meitu.utils.SharedUtils;
import com.meitu.utils.ToastUtil;
import com.meitu.utils.UniversalImageLoadTool;
import com.meitu.utils.Utils;

import fynn.app.PromptDialog;

public class DrawerLeftMenu extends FrameLayout implements OnClickListener {
	private Context mContext;
	private View rootView;
	private ImageView img_avatar;
	private TextView txt_user_name;
	private LinearLayout layotu_parent;
	private TextView txt_publish;
	private TextView txt_user_info;
	private TextView txt_new_version;
	private TextView txt_exit;

	public DrawerLeftMenu(Context context, MainActivity mActivity) {
		super(context);
		mContext = context;
		initView();
		setValue();
	}

	public DrawerLeftMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initView();
		setValue();

	}

	private void setValue() {
		checkVersion();
		String user_avatar = SharedUtils.getAPPUserAvatar();
		setName(SharedUtils.getAPPUserName());
		UniversalImageLoadTool.disPlay(user_avatar, img_avatar,
				R.drawable.picture_default_head);
	}

	public void setName(String name) {
		txt_user_name.setText(name);

	}

	private void initView() {
		rootView = LayoutInflater.from(mContext).inflate(
				R.layout.drawer_left_view_layout, null);
		img_avatar = (ImageView) rootView.findViewById(R.id.img_avatar);
		txt_user_name = (TextView) rootView.findViewById(R.id.txt_user_name);
		txt_user_name.getBackground().setAlpha(120);
		layotu_parent = (LinearLayout) rootView
				.findViewById(R.id.layout_parent);
		txt_publish = (TextView) rootView.findViewById(R.id.txt_publish);
		txt_user_info = (TextView) rootView.findViewById(R.id.txt_user_info);
		txt_new_version = (TextView) rootView
				.findViewById(R.id.txt_new_version);
		txt_exit = (TextView) rootView.findViewById(R.id.txt_exit);
		layotu_parent.setOnClickListener(this);
		txt_publish.setOnClickListener(this);
		txt_user_info.setOnClickListener(this);
		txt_new_version.setOnClickListener(this);
		img_avatar.setOnClickListener(this);
		txt_exit.setOnClickListener(this);
		addView(rootView);
	}

	private void setNewVersionPrompt() {
		if (SharedUtils.getNewVersion()) {
			Drawable prompt = getResources().getDrawable(R.drawable.prompt);
			prompt.setBounds(0, 0, prompt.getMinimumWidth(),
					prompt.getMinimumHeight());
			txt_new_version.setCompoundDrawables(null, null, prompt, null);
		}
	}

	// public void setMessagePrompt(boolean visible) {
	// if (visible) {
	// Drawable prompt = getResources().getDrawable(R.drawable.prompt);
	// prompt.setBounds(0, 0, prompt.getMinimumWidth(),
	// prompt.getMinimumHeight());
	// Drawable message = getResources().getDrawable(R.drawable.message);
	// message.setBounds(0, 0, message.getMinimumWidth(),
	// message.getMinimumHeight());
	// txt_message.setCompoundDrawables(message, null, prompt, null);
	// } else {
	// Drawable message = getResources().getDrawable(R.drawable.message);
	// message.setBounds(0, 0, message.getMinimumWidth(),
	// message.getMinimumHeight());
	// txt_message.setCompoundDrawables(message, null, null, null);
	// }
	// }

	@SuppressLint("NewApi")
	private void checkVersion() {
		if (!Utils.isNetworkAvailable()) {
			return;
		}
		UpDateNewVersionTask task = new UpDateNewVersionTask(mContext, false);
		task.setCallBack(new UpDateVersion() {
			@Override
			public void getNewVersion(int rt, String versionCode, String link) {
				if (rt == 0) {
					SharedUtils.settingNewVersion(false);
					return;
				}
				SharedUtils.settingNewVersion(true);
				setNewVersionPrompt();
			}
		});
		int sdk = android.os.Build.VERSION.SDK_INT;
		if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
			task.execute();
		} else {
			task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		}
	}

	private void getNewVersion() {
		final Dialog dialog = DialogUtil.createLoadingDialog(mContext);
		dialog.show();
		UpDateNewVersionTask task = new UpDateNewVersionTask(mContext, true);
		task.setCallBack(new UpDateVersion() {
			@Override
			public void getNewVersion(int rt, String versionCode, String link) {
				if (dialog != null) {
					dialog.dismiss();
				}
				if (rt == 0) {
					return;
				}
				DialogUtil.newVewsionDialog(mContext, versionCode, link);
			}
		});
		task.execute();
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.txt_publish:
			if (MainActivity.isUpLoading()) {
				ToastUtil.showToast("正在发布中,请稍候...");
				return;
			}
			((Activity) mContext).startActivityForResult(new Intent(mContext,
					PublicshActivity.class), 200);
			break;
		case R.id.txt_user_info:
			// intent = new Intent();
			// intent.putExtra("circle_member", MyApplation.getMemberSelf());
			// intent.setClass(mContext, CircleMemberOfSelfInfoActivity.class);
			// mContext.startActivity(intent);
			break;
		case R.id.txt_new_version:
			if (!Utils.isNetworkAvailable()) {
				ToastUtil.showToast("网络错误,请检查网络");
				return;
			}
			getNewVersion();
			break;
		case R.id.img_avatar:
			List<String> imgUrl = new ArrayList<String>();
			imgUrl.add(SharedUtils.getAPPUserAvatar());
			intent = new Intent(mContext, ImagePagerActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable(Constants.EXTRA_IMAGE_URLS,
					(Serializable) imgUrl);
			intent.putExtras(bundle);
			intent.putExtra(Constants.EXTRA_IMAGE_INDEX, 1);
			mContext.startActivity(intent);
			break;
		case R.id.txt_exit:
			quitPrompt();
			break;
		default:
			break;
		}
		Utils.leftOutRightIn(mContext);
	}

	private void quitPrompt() {
		PromptDialog.Builder dialog = DialogUtil.confirmDialog(mContext,
				"确定要退出吗?", "确定", "取消", new ConfirmDialog() {
					@Override
					public void onOKClick() {
						MyApplation.exit(true);
						Utils.cleanDatabaseByName(mContext);
						SharedUtils.setUid(0 + "");
						DataBaseHelper.setIinstanceNull();
						SharedUtils.setCircleLastRequestTime(0);
						SharedUtils.clearData();
						DBUtils.dbase = null;
						DBUtils.close();
					}

					@Override
					public void onCancleClick() {
					}
				});
		dialog.show();
	}
}
