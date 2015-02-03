//package com.meitu;
//
//import android.app.Dialog;
//import android.view.View;
//import android.widget.TextView;
//
//import com.meitu.Interface.AbstractTaskPostCallBack;
//import com.meitu.data.User;
//import com.meitu.data.enums.RetError;
//import com.meitu.task.GetUserInfoTask;
//import com.meitu.utils.DialogUtil;
//
//public class UserInfoView extends UserInfo {
//	private TextView txt_name;
//	private TextView txt_gender;
//	private TextView txt_birthday;
//
//	private User user;
//
//	private Dialog dialog;
//
//	public UserInfoView(UserInfoActivity activity, View contentRootView) {
//		super(activity, contentRootView);
//		user = new User();
//		user.setUser_id(mActivity.getUser_id());
//		getUserInfo();
//	}
//
//	@Override
//	public void initView() {
//		txt_birthday = (TextView) findViewById(R.id.txt_birthday);
//		txt_gender = (TextView) findViewById(R.id.txt_gender);
//		txt_name = (TextView) findViewById(R.id.txt_user_name);
//	}
//
//	@Override
//	public void setListener() {
//
//	}
//
//	private void getUserInfo() {
//		dialog = DialogUtil.createLoadingDialog(mContext);
//		dialog.show();
//		GetUserInfoTask task = new GetUserInfoTask();
//		task.setmCallBack(new AbstractTaskPostCallBack<RetError>() {
//			@Override
//			public void taskFinish(RetError result) {
//				if (dialog != null) {
//					dialog.dismiss();
//				}
//				if (result != RetError.NONE) {
//					return;
//				}
//				txt_birthday.setText(user.getUser_birthday());
//				txt_gender.setText(user.getUser_gender());
//				txt_name.setText(user.getUser_name());
//			}
//		});
//
//		task.executeParallel(user);
//	}
// }
