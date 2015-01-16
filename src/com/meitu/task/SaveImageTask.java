package com.meitu.task;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.meitu.MyApplation;
import com.meitu.utils.BitmapUtils;
import com.meitu.utils.FileUtils;
import com.meitu.utils.Utils;

public class SaveImageTask extends AsyncTask<Void, Void, Void> {
	private SaveImge callBack;
	private Bitmap bmp;

	public void setCallBack(SaveImge callBack) {
		this.callBack = callBack;
	}

	public SaveImageTask(Bitmap bmp) {
		this.bmp = bmp;
	}

	@Override
	protected Void doInBackground(Void... params) {
		String name = FileUtils.getFileName() + ".jpg";
		String fileName = FileUtils.getMeiTuImgSavePath() + name;
		BitmapUtils.createImgToFile(bmp, fileName);
		bmp.recycle();
		Utils.fileScan(MyApplation.getInstance(), fileName);
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		callBack.saveFinish();
	}

	public interface SaveImge {
		void saveFinish();
	}
}
