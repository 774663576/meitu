package com.meitu;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.meitu.PublishArticleImagePagerActivity.DelPic;
import com.meitu.chooseimage.RotateImageViewAware;
import com.meitu.chooseimage.SelectPhotoActivity;
import com.meitu.data.Article;
import com.meitu.data.ArticleImage;
import com.meitu.popwindow.SelectPicPopwindow;
import com.meitu.popwindow.SelectPicPopwindow.SelectOnclick;
import com.meitu.utils.Constants;
import com.meitu.utils.FileUtils;
import com.meitu.utils.ToastUtil;
import com.meitu.utils.UniversalImageLoadTool;
import com.meitu.view.ExpandGridView;
import com.meitu.view.RoundAngleImageView;

public class PublicshActivity extends BaseActivity implements
		OnItemClickListener, OnClickListener, SelectOnclick, DelPic {
	private Button btnPublish;
	private EditText content;// 内容输入框
	private ExpandGridView mGridView;
	private TextView txt_title;
	private ImageView back;

	private List<String> photoPathLists = new ArrayList<String>();

	private MyAdapter adapter;

	private SelectPicPopwindow pop;

	private String cameraPath = "";

	private Article article;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.publish_activity);
		initView();
	}

	private void initView() {
		back = (ImageView) findViewById(R.id.back);
		txt_title = (TextView) findViewById(R.id.title_txt);
		txt_title.setText("发布成长");
		btnPublish = (Button) findViewById(R.id.btnUpload);
		content = (EditText) findViewById(R.id.txt_content);
		photoPathLists.add("");
		mGridView = (ExpandGridView) findViewById(R.id.imgGridview);
		adapter = new MyAdapter();
		mGridView.setAdapter(adapter);
		setListener();
	}

	private void setListener() {
		mGridView.setOnItemClickListener(this);
		btnPublish.setOnClickListener(this);
		back.setOnClickListener(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == Constants.REQUEST_CODE_GETIMAGE_BYSDCARD_MORE) {
			if (resultCode == RESULT_OK) {
				Bundle bundle = data.getExtras();
				@SuppressWarnings("unchecked")
				List<String> list = (List<String>) bundle
						.getSerializable("imgPath");
				for (String m : list) {
					photoPathLists.add(photoPathLists.size() - 1, m);
				}
			}
		}
		// 拍摄图片
		else if (requestCode == Constants.REQUEST_CODE_GETIMAGE_BYCAMERA) {
			if (resultCode != RESULT_OK) {
				return;
			}
			File file = new File(cameraPath);
			if (!file.exists()) {
				ToastUtil.showToast("图片获取失败，请重新获取");
				return;
			}

			photoPathLists.add(photoPathLists.size() - 1, cameraPath);
		}
		adapter.notifyDataSetChanged();
	}

	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return photoPathLists.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(PublicshActivity.this)
						.inflate(R.layout.publish_item, null);
				holder.img = (RoundAngleImageView) convertView
						.findViewById(R.id.img);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if (position == photoPathLists.size() - 1) {
				UniversalImageLoadTool.disPlay("file://" + "",
						new RotateImageViewAware(holder.img, ""),
						R.drawable.add_pic);
			} else {
				String path = photoPathLists.get(position);
				UniversalImageLoadTool.disPlay("file://" + path,
						new RotateImageViewAware(holder.img, path),
						R.drawable.empty_photo);
			}
			return convertView;
		}
	}

	class ViewHolder {
		RoundAngleImageView img;
	}

	private void getGrowth(String content) {
		article = new Article();
		List<ArticleImage> imglists = new ArrayList<ArticleImage>();
		for (String imgPath : photoPathLists) {
			if (!"".equals(imgPath)) {
				ArticleImage img = new ArticleImage();
				img.setImg(imgPath);
				imglists.add(img);
			}
		}
		article.setImages(imglists);
		article.setContent(content);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnUpload:
			String str_content = content.getText().toString().trim();
			if (photoPathLists.size() == 1 && str_content.length() == 0) {
				return;
			}
			btnPublish.setEnabled(false);
			getGrowth(str_content);
			Intent intent = new Intent();
			intent.putExtra("article", article);
			setResult(200, intent);
			finish();
			break;
		case R.id.back:
			finishThisActivity();
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
		if (position == photoPathLists.size() - 1) {
			if (photoPathLists.size() == 10) {
				ToastUtil.showToast("一次最多允许发布9张图片", Toast.LENGTH_SHORT);
				return;
			}
			pop = new SelectPicPopwindow(this, v, "拍照", "从相册选择");
			pop.setmSelectOnclick(this);
			pop.show();
			return;
		}
		List<String> imgUrl = new ArrayList<String>();
		for (int i = 0; i < photoPathLists.size() - 1; i++) {
			imgUrl.add(photoPathLists.get(i));
		}
		Intent intent = new Intent(this, PublishArticleImagePagerActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable(Constants.EXTRA_IMAGE_URLS,
				(Serializable) imgUrl);
		intent.putExtras(bundle);
		intent.putExtra(Constants.EXTRA_IMAGE_INDEX, position);
		intent.putExtra("type", 1);
		startActivity(intent);
		PublishArticleImagePagerActivity.setCallBack(this);
	}

	@Override
	public void menu1_select() {
		String name = FileUtils.getFileName() + ".jpg";
		cameraPath = FileUtils.getCameraPath() + File.separator + name;
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// 下面这句指定调用相机拍照后的照片存储的路径
		intent.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(new File(cameraPath)));
		startActivityForResult(intent, Constants.REQUEST_CODE_GETIMAGE_BYCAMERA);
	}

	@Override
	public void menu2_select() {

		Intent intent = new Intent();
		intent.putExtra("count", photoPathLists.size() - 1);
		intent.setClass(this, SelectPhotoActivity.class);
		startActivityForResult(intent,
				Constants.REQUEST_CODE_GETIMAGE_BYSDCARD_MORE);
	}

	@Override
	public void del(int position) {
		photoPathLists.remove(position);
		adapter.notifyDataSetChanged();
	}

}
