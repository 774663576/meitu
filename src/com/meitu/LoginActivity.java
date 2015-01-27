package com.meitu;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.meitu.Interface.AbstractTaskPostCallBack;
import com.meitu.Interface.MyEditTextWatcher;
import com.meitu.Interface.MyEditTextWatcher.OnTextLengthChange;
import com.meitu.Interface.OnEditFocusChangeListener;
import com.meitu.data.User;
import com.meitu.data.enums.RetError;
import com.meitu.task.UserLoginTask;
import com.meitu.utils.DialogUtil;
import com.meitu.utils.SharedUtils;
import com.meitu.utils.ToastUtil;
import com.meitu.utils.Utils;
import com.meitu.view.MyEditTextDeleteImg;

public class LoginActivity extends BaseActivity implements OnTextLengthChange {
	private MyEditTextDeleteImg edit_telphone;
	private MyEditTextDeleteImg edit_password;
	private Button btn_login;
	private Button btn_find_password;
	private ImageView back;
	private TextView txt_title;

	private Dialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		if (SharedUtils.getIntUid() != 0) {
			startActivity(new Intent(LoginActivity.this, MainActivity.class));
			finish();
			return;
		}
		initView();
	}

	private void initView() {
		back = (ImageView) findViewById(R.id.back);
		txt_title = (TextView) findViewById(R.id.title_txt);
		txt_title.setText("登录");
		edit_password = (MyEditTextDeleteImg) findViewById(R.id.edit_password);
		edit_telphone = (MyEditTextDeleteImg) findViewById(R.id.edit_telphone);
		edit_telphone.setTag("phone_num");
		btn_login = (Button) findViewById(R.id.btn_login);
		btn_find_password = (Button) findViewById(R.id.btn_findPasswrod);
		setListener();
	}

	private void setListener() {
		edit_telphone.setOnFocusChangeListener(new OnEditFocusChangeListener(
				edit_telphone, this));
		edit_telphone.addTextChangedListener(new MyEditTextWatcher(
				edit_telphone, this, this));
		edit_password.addTextChangedListener(new MyEditTextWatcher(
				edit_password, this, this));
		edit_password.setOnFocusChangeListener(new OnEditFocusChangeListener(
				edit_password, this));
		btn_login.setOnClickListener(this);
		btn_find_password.setOnClickListener(this);
		back.setOnClickListener(this);
	}

	@Override
	public void onTextLengthChanged(boolean isBlank) {
		if (!isBlank) {
			if (edit_password.getText().toString().length() != 0
					&& edit_telphone.getText().toString().length() != 0) {
				btn_login.setEnabled(true);
				btn_login.setBackgroundResource(R.drawable.btn_selector);
				return;
			}
		}
		btn_login.setEnabled(false);
		btn_login.setBackgroundResource(R.drawable.btn_disenable_bg);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login:
			login();
			break;
		case R.id.btn_findPasswrod:
			// startActivity(new Intent(this, FindPasswordActivity.class));
			// Utils.leftOutRightIn(this);
			break;
		case R.id.back:
			finishThisActivity();
		default:
			break;
		}
	}

	private void login() {
		String user_cellphone = edit_telphone.getText().toString()
				.replaceAll(" ", "");
		String user_password = edit_password.getText().toString();
		if (!Utils.isPhoneNum(user_cellphone)) {
			ToastUtil.showToast("手机号格式不正确", Toast.LENGTH_SHORT);
			return;
		}
		if (!Utils.isNetworkAvailable()) {
			ToastUtil.showToast("网络错误,请检查网络", Toast.LENGTH_SHORT);
			return;
		}
		User user = new User();
		user.setUser_phone(user_cellphone);
		user.setUser_password(user_password);
		dialog = DialogUtil.createLoadingDialog(this);
		dialog.show();
		UserLoginTask task = new UserLoginTask();
		task.setmCallBack(new AbstractTaskPostCallBack<RetError>() {
			@Override
			public void taskFinish(RetError result) {
				if (dialog != null) {
					dialog.dismiss();
				}
				if (result != RetError.NONE) {
					return;
				}
				startActivity(new Intent(LoginActivity.this, MainActivity.class));
				Utils.leftOutRightIn(LoginActivity.this);
			}
		});
		task.executeParallel(user);
	}

}
