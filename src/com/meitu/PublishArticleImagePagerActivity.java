package com.meitu;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.meitu.Interface.ConfirmDialog;
import com.meitu.showbigpic.ImageDetailFragment;
import com.meitu.utils.Constants;
import com.meitu.utils.DialogUtil;
import com.meitu.view.HackyViewPager;

import fynn.app.PromptDialog;

public class PublishArticleImagePagerActivity extends FragmentActivity implements
		OnClickListener {
	private static final String STATE_POSITION = "STATE_POSITION";
	private HackyViewPager mPager;
	private int pagerPosition;
	private TextView indicator;
	private List<String> lists = new ArrayList<String>();
	private static DelPic callBack;
	private static ImageView btnDel;
	private int delPosition;
	private int type;
	private ImagePagerAdapter mAdapter;

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_publish_growth_image_pager);
		pagerPosition = getIntent().getIntExtra(Constants.EXTRA_IMAGE_INDEX, 0);
		type = getIntent().getIntExtra("type", 0);
		lists = (List<String>) getIntent().getExtras().getSerializable(
				Constants.EXTRA_IMAGE_URLS);
		mPager = (HackyViewPager) findViewById(R.id.pager);
		mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), lists);
		mPager.setAdapter(mAdapter);
		indicator = (TextView) findViewById(R.id.indicator);
		btnDel = (ImageView) findViewById(R.id.btnDel);
		btnDel.setOnClickListener(this);
		if (type != 0) {
			btnDel.setVisibility(View.VISIBLE);
		}
		CharSequence text = getString(R.string.viewpager_indicator, 1, mPager
				.getAdapter().getCount());
		indicator.setText(text);
		// �����±�
		mPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageSelected(int arg0) {
				CharSequence text = getString(R.string.viewpager_indicator,
						arg0 + 1, mPager.getAdapter().getCount());
				indicator.setText(text);
				delPosition = arg0;
			}
		});
		if (savedInstanceState != null) {
			pagerPosition = savedInstanceState.getInt(STATE_POSITION);
		}

		mPager.setCurrentItem(pagerPosition);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(STATE_POSITION, mPager.getCurrentItem());
	}

	private class ImagePagerAdapter extends FragmentStatePagerAdapter {

		public List<String> fileList;
		private FragmentManager fm;

		public ImagePagerAdapter(FragmentManager fm, List<String> fileList) {
			super(fm);
			this.fileList = fileList;
			this.fm = fm;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			fm.beginTransaction();

		}

		@Override
		public int getCount() {
			return fileList == null ? 0 : fileList.size();
		}

		@Override
		public Fragment getItem(int position) {
			String url = fileList.get(position);
			return ImageDetailFragment.newInstance(url);
		}
	}

	public static void setCallBack(DelPic back) {
		callBack = back;
	}

	public interface DelPic {
		void del(int position);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnDel:
			PromptDialog.Builder dialog = DialogUtil.confirmDialog(this,
					"�Ƿ�ɾ������ͼƬ?", "��", "��", new ConfirmDialog() {

						@Override
						public void onOKClick() {
							callBack.del(delPosition);
							finish();
						}

						public void onCancleClick() {

						}
					});
			dialog.show();
			break;

		default:
			break;
		}

	}
}