package com.meitu.register;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.meitu.R;
import com.meitu.Interface.MyEditTextWatcher;
import com.meitu.Interface.MyEditTextWatcher.OnTextLengthChange;
import com.meitu.Interface.OnEditFocusChangeListener;
import com.meitu.utils.ToastUtil;
import com.meitu.view.MyEditTextDeleteImg;

public class RegisterUserName extends RegisterStep implements OnClickListener,
		OnTextLengthChange {
	private MyEditTextDeleteImg edit_user_name;
	private Button btn_next;

	public RegisterUserName(RegisterActivity activity, View contentRootView) {
		super(activity, contentRootView);
	}

	@Override
	public void initView() {
		edit_user_name = (MyEditTextDeleteImg) findViewById(R.id.edit_user_name);
		btn_next = (Button) findViewById(R.id.btnNext);
		setListener();
	}

	@Override
	public void setListener() {
		edit_user_name.setOnFocusChangeListener(new OnEditFocusChangeListener(
				edit_user_name, mContext));
		edit_user_name.addTextChangedListener(new MyEditTextWatcher(
				edit_user_name, mContext, this));
		btn_next.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnNext:
			if (edit_user_name.getText().toString().replace(" ", "").length() == 0) {
				ToastUtil.showToast("��������Ϊ��", Toast.LENGTH_SHORT);
				return;
			}
			mActivity.getmRegister().setUser_name(
					edit_user_name.getText().toString());
			mOnNextListener.next();
			break;
		default:
			break;
		}
	}

	@Override
	public void onTextLengthChanged(boolean isBlank) {
		if (!isBlank) {
			if (edit_user_name.getText().toString().length() != 0) {
				btn_next.setEnabled(true);
				btn_next.setBackgroundResource(R.drawable.btn_selector);
				return;
			}
		}
		btn_next.setEnabled(false);
		btn_next.setBackgroundResource(R.drawable.btn_disenable_bg);
	}

}
