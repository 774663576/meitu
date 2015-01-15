package com.meitu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.meitu.R;
import com.meitu.view.ExpandGridView;

public class GrowthAdapter extends BaseAdapter {

	private Context mContext;

	private LayoutInflater inflater;
	private boolean isTasking = false;

	public GrowthAdapter(Context context) {
		this.mContext = context;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return 5;
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	private View createView() {
		return inflater.inflate(R.layout.article_item, null);

	}

	@Override
	public View getView(final int position, View contentView, ViewGroup arg2) {
		ViewHolder holder = null;

		if (contentView == null) {
			holder = new ViewHolder();
			contentView = createView();
			holder.img = (ImageView) contentView.findViewById(R.id.img);
			holder.txt_context = (TextView) contentView
					.findViewById(R.id.txt_content);
			holder.txt_time = (TextView) contentView
					.findViewById(R.id.txt_time);
			holder.txt_user_name = (TextView) contentView
					.findViewById(R.id.txt_user_name);
			holder.img_avatar = (ImageView) contentView
					.findViewById(R.id.img_avatar);
			holder.img_grid_view = (ExpandGridView) contentView
					.findViewById(R.id.imgGridview);
			holder.btn_comment = (TextView) contentView
					.findViewById(R.id.btn_comment);
			holder.line2 = (View) contentView.findViewById(R.id.line2);
			holder.btn_praise = (TextView) contentView
					.findViewById(R.id.btn_prise);
			contentView.setTag(holder);
		} else {
			holder = (ViewHolder) contentView.getTag();
		}

		return contentView;
	}

	class ViewHolder {
		ImageView img_avatar;
		TextView txt_user_name;
		TextView txt_time;
		TextView txt_context;
		TextView btn_comment;
		ImageView img;
		ExpandGridView img_grid_view;
		View line2;
		TextView btn_praise;
	}
}
