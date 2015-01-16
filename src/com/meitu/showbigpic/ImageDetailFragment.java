package com.meitu.showbigpic;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.meitu.MyApplation;
import com.meitu.R;
import com.meitu.photoview.PhotoView;
import com.meitu.photoview.PhotoViewAttacher.OnPhotoTapListener;
import com.meitu.utils.ToastUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

/**
 * 打图预览界面
 * 
 * @author teeker_bin
 * 
 */
public class ImageDetailFragment extends Fragment {
	private String mImageUrl;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private ProgressBar progressBar;
	private OnBack callBack;
	private PhotoView photoView;

	public static ImageDetailFragment newInstance(String imageUrl) {
		final ImageDetailFragment f = new ImageDetailFragment();
		final Bundle args = new Bundle();
		args.putString("url", imageUrl);
		f.setArguments(args);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mImageUrl = getArguments() != null ? getArguments().getString("url")
				: null;
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(MyApplation
				.getInstance()));
		options = new DisplayImageOptions.Builder().cacheOnDisc(true)
				.bitmapConfig(Bitmap.Config.ARGB_8888).build();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.image_detail_fragment,
				container, false);
		photoView = (PhotoView) v.findViewById(R.id.image);

		photoView.setOnPhotoTapListener(new OnPhotoTapListener() {

			@Override
			public void onPhotoTap(View view, float x, float y) {
				if (callBack != null) {
					callBack.onBackClick();
				} else {
					getActivity().finish();

				}
			}
		});
		progressBar = (ProgressBar) v.findViewById(R.id.loading);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (!mImageUrl.startsWith("http")) {
			mImageUrl = "file://" + mImageUrl;
		}
		imageLoader.displayImage(mImageUrl, photoView, options,
				new SimpleImageLoadingListener() {
					@Override
					public void onLoadingStarted(String imageUri, View view) {
						progressBar.setVisibility(View.VISIBLE);
					}

					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {
						String message = null;
						switch (failReason.getType()) {
						case IO_ERROR:
							message = "图片无法加载，请检查网络是否正常";
							break;
						case DECODING_ERROR:
							message = "图片无法显示";
							break;
						case NETWORK_DENIED:
							message = "网络有问题，无法下载";
							break;
						case OUT_OF_MEMORY:
							message = "图片太大无法显示";
							break;
						case UNKNOWN:
							message = "未知错误";
							break;
						}
						ToastUtil.showToast(message);
						progressBar.setVisibility(View.GONE);
					}

					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						progressBar.setVisibility(View.GONE);

					}
				});

	}

	public void setCallBack(OnBack callBack) {
		this.callBack = callBack;
	}

	public interface OnBack {
		void onBackClick();
	}
}
