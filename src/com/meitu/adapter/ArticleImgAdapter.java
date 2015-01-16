package com.meitu.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.meitu.R;
import com.meitu.data.ArticleImage;
import com.meitu.utils.UniversalImageLoadTool;

public class ArticleImgAdapter extends BaseAdapter {
	private Context mContext;
	private List<ArticleImage> listData;

	public ArticleImgAdapter(Context context, List<ArticleImage> data) {
		this.mContext = context;
		this.listData = data;
	}

	@Override
	public int getCount() {
		return listData.size();
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
		String path = listData.get(position).getImg();
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.grow_img_gridview_item, null);
			holder.img = (ImageView) convertView.findViewById(R.id.img);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.img.setTag(path);
		if (!path.startsWith("http")) {
			path = "file://" + path;
		}
		UniversalImageLoadTool
				.disPlay(path, holder.img, R.drawable.empty_photo);
		return convertView;
	}

	class ViewHolder {
		ImageView img;
	}
}
