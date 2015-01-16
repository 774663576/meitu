package com.meitu.register;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.meitu.R;
import com.meitu.data.User;
import com.meitu.register.RegisterStep.onNextListener;
import com.meitu.utils.FileUtils;
import com.meitu.utils.PhotoUtils;
import com.meitu.utils.ToastUtil;
import com.meitu.utils.Utils;

public class RegisterActivity extends FragmentActivity implements
		onNextListener, OnClickListener {
	private ViewFlipper mVfFlipper;
	private ImageView back;
	private TextView txt_title;
	private TextView txt_page;

	private RegisterStep reStep;
	private RegisterBasicInfo reBasicInfo;
	private RegisterPhone rePhone;
	private RegisterUserName regName;
	private RegisterSetPassword reSetPasswd;

	private User mRegister;

	private int mCurrentStepIndex = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.register_activity);
		mRegister = new User();
		initView();
	}

	private void initView() {
		back = (ImageView) findViewById(R.id.back);
		txt_title = (TextView) findViewById(R.id.txt_title);
		txt_title.setText("注册");
		txt_page = (TextView) findViewById(R.id.txt_page);
		mVfFlipper = (ViewFlipper) findViewById(R.id.viewflipper);
		mVfFlipper.setDisplayedChild(0);
		reStep = initStep();
		setListener();
	}

	private void setListener() {
		reStep.setmOnNextListener(this);
		back.setOnClickListener(this);
	}

	protected User getmRegister() {
		return mRegister;
	}

	public void setmRegister(User mRegister) {
		this.mRegister = mRegister;
	}

	private RegisterStep initStep() {
		txt_page.setText(mCurrentStepIndex + "/4");
		switch (mCurrentStepIndex) {
		case 1:
			if (rePhone == null) {
				rePhone = new RegisterPhone(this, mVfFlipper.getChildAt(0));
			}
			return rePhone;
		case 2:
			if (regName == null) {
				regName = new RegisterUserName(this, mVfFlipper.getChildAt(1));
			}
			return regName;
		case 3:
			if (reBasicInfo == null) {
				reBasicInfo = new RegisterBasicInfo(this,
						mVfFlipper.getChildAt(2));
			}
			return reBasicInfo;

		case 4:
			if (reSetPasswd == null) {
				reSetPasswd = new RegisterSetPassword(this,
						mVfFlipper.getChildAt(3));
			}
			return reSetPasswd;
		default:
			break;
		}
		return null;
	}

	@Override
	public void next() {
		mCurrentStepIndex++;
		reStep = initStep();
		reStep.setmOnNextListener(this);
		mVfFlipper.showNext();
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case PhotoUtils.INTENT_REQUEST_CODE_ALBUM:
			if (data == null) {
				return;
			}
			if (resultCode == RESULT_OK) {
				if (data.getData() == null) {
					return;
				}
				if (!FileUtils.isSdcardExist()) {
					ToastUtil.showToast("SD卡不可用,请检查", Toast.LENGTH_SHORT);
					return;
				}
				Uri uri = data.getData();
				String[] proj = { MediaStore.Images.Media.DATA };
				Cursor cursor = managedQuery(uri, proj, null, null, null);
				if (cursor != null) {
					int column_index = cursor
							.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					if (cursor.getCount() > 0 && cursor.moveToFirst()) {
						String path = cursor.getString(column_index);
						Bitmap bitmap = BitmapFactory.decodeFile(path);
						if (PhotoUtils.bitmapIsLarge(bitmap)) {
							PhotoUtils.cropPhoto(this, this, path);
						} else {
							reBasicInfo.setUserPhoto(bitmap, path);
						}
					}
				}
			}
			break;

		case PhotoUtils.INTENT_REQUEST_CODE_CAMERA:

			if (resultCode == RESULT_OK) {
				String path = reBasicInfo.getTakePicturePath();
				Bitmap bitmap = BitmapFactory.decodeFile(path);
				if (PhotoUtils.bitmapIsLarge(bitmap)) {
					PhotoUtils.cropPhoto(this, this, path);
				} else {
					reBasicInfo.setUserPhoto(bitmap, path);
				}
			}
			break;

		case PhotoUtils.INTENT_REQUEST_CODE_CROP:
			if (resultCode == RESULT_OK) {
				String path = data.getStringExtra("path");
				if (path != null) {
					Bitmap bitmap = BitmapFactory.decodeFile(path);
					if (bitmap != null) {
						reBasicInfo.setUserPhoto(bitmap, path);
					}
				}
			}
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			back();
		}
		return false;
	}

	private void pre() {
		mCurrentStepIndex--;
		reStep = initStep();
		reStep.setmOnNextListener(this);
		mVfFlipper.showPrevious();
	}

	private void back() {
		if (mCurrentStepIndex == 1) {
			// finishThisActivity();
			finish();
			Utils.rightOut(this);
		} else {
			pre();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			back();
			break;
		default:
			break;
		}
	}

}
