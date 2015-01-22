package com.meitu.register;

import java.util.Calendar;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener;
import com.meitu.R;
import com.meitu.popwindow.SelectPicPopwindow;
import com.meitu.popwindow.SelectPicPopwindow.SelectOnclick;
import com.meitu.utils.PhotoUtils;
import com.meitu.utils.ToastUtil;
import com.meitu.utils.Utils;

public class RegisterBasicInfo extends RegisterStep implements OnClickListener,
		OnDateSetListener, SelectOnclick {

	private static final String DATEPICKER_TAG = "datepicker";
	private String mTakePicturePath;

	private RadioButton rMale;
	private RadioButton rFemale;
	private TextView txtBirthday;
	private ImageView imgAvater;
	private Button btn_next;
	private RelativeLayout layoutMale;
	private RelativeLayout layoutoFemal;

	private DatePickerDialog datePickerDialog;
	private SelectPicPopwindow pop;
	private String photoPath = "";

	public RegisterBasicInfo(RegisterActivity activity, View contentRootView) {
		super(activity, contentRootView);
		Utils.hideSoftInput(mContext);

	}

	@Override
	public void initView() {
		Calendar calendar = Calendar.getInstance();
		datePickerDialog = DatePickerDialog.newInstance(this,
				calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH), true);
		rMale = (RadioButton) findViewById(R.id.rg_radiobutton_male);
		rFemale = (RadioButton) findViewById(R.id.rg_radiobutton_female);
		layoutMale = (RelativeLayout) findViewById(R.id.reg_layout_male);
		layoutoFemal = (RelativeLayout) findViewById(R.id.reg_layout_female);
		txtBirthday = (TextView) findViewById(R.id.txt_birthday);
		imgAvater = (ImageView) findViewById(R.id.img_avatar);
		btn_next = (Button) findViewById(R.id.btnNext);
		setListener();
	}

	@Override
	public void setListener() {
		layoutMale.setOnClickListener(this);
		layoutoFemal.setOnClickListener(this);
		txtBirthday.setOnClickListener(this);
		imgAvater.setOnClickListener(this);
		btn_next.setOnClickListener(this);
	}

	public void setUserPhoto(Bitmap bitmap, String path) {
		if (bitmap != null) {
			imgAvater.setImageBitmap(PhotoUtils.toRoundCorner(bitmap, 30));
			photoPath = path;
			setNextEnbale();
			return;
		}
		ToastUtil.showToast("δ��ȡ��ͼƬ", Toast.LENGTH_SHORT);
		imgAvater.setImageResource(R.drawable.picture_default_head);
	}

	public String getTakePicturePath() {
		return mTakePicturePath;
	}

	private void setNextEnbale() {
		if ((!"".equals(photoPath) && !"".equals(txtBirthday.getText()
				.toString())) && (rFemale.isChecked() || rMale.isChecked())) {
			btn_next.setEnabled(true);
			btn_next.setBackgroundResource(R.drawable.btn_selector);
		} else {
			btn_next.setEnabled(false);
			btn_next.setBackgroundResource(R.drawable.btn_disenable_bg);
		}
	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.reg_layout_male:
			rMale.setChecked(true);
			rFemale.setChecked(false);
			setNextEnbale();

			break;
		case R.id.reg_layout_female:
			rMale.setChecked(false);
			rFemale.setChecked(true);
			setNextEnbale();

			break;
		case R.id.txt_birthday:
			datePickerDialog.setVibrate(true);
			datePickerDialog.setYearRange(1985, 2028);
			datePickerDialog.show(mActivity.getSupportFragmentManager(),
					DATEPICKER_TAG);
			break;
		case R.id.img_avatar:
			pop = new SelectPicPopwindow(mContext, v, "����", "�����ѡ��");
			pop.setmSelectOnclick(this);
			pop.show();
			break;
		case R.id.btnNext:
			mActivity.getmRegister().setUser_avatar(photoPath);
			mActivity.getmRegister().setUser_birthday(
					txtBirthday.getText().toString());
			if (rFemale.isChecked()) {
				mActivity.getmRegister().setUser_gender("Ů");
			} else {
				mActivity.getmRegister().setUser_gender("��");
			}
			mOnNextListener.next();
			break;
		default:
			break;
		}
	}

	@Override
	public void onDateSet(DatePickerDialog datePickerDialog, int year,
			int month, int day) {
		txtBirthday.setText(year + "-" + (month + 1) + "-" + day);
		setNextEnbale();

	}

	@Override
	public void menu1_select() {
		mTakePicturePath = PhotoUtils.takePicture(mActivity);

	}

	@Override
	public void menu2_select() {
		PhotoUtils.selectPhoto(mActivity);

	}

}
