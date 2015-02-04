//package com.meitu;
//
//import android.app.Dialog;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import com.meitu.Interface.AbstractTaskPostCallBack;
//import com.meitu.data.User;
//import com.meitu.data.enums.RetError;
//import com.meitu.task.GetUserInfoTask;
//import com.meitu.utils.DialogUtil;
//
//public class UserInfoFragment extends Fragment {
//	private TextView txt_name;
//	private TextView txt_gender;
//	private TextView txt_birthday;
//
//	private User user;
//
//	private Dialog dialog;
//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//		return inflater.inflate(R.layout.user_info_layout, null);
//	}
//
//	@Override
//	public void onActivityCreated(Bundle savedInstanceState) {
//		super.onActivityCreated(savedInstanceState);
//		user = new User();
//		user.setUser_id(UserInfoActivity.getUser_id());
//		getUserInfo();
//		initView();
//	}
//
//	public void initView() {
//		txt_birthday = (TextView) getView().findViewById(R.id.txt_birthday);
//		txt_gender = (TextView) getView().findViewById(R.id.txt_gender);
//		txt_name = (TextView) getView().findViewById(R.id.txt_user_name);
//	}
//
//	public void setListener() {
//
//	}
//
//	private void getUserInfo() {
//		dialog = DialogUtil.createLoadingDialog(getActivity());
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
//}
