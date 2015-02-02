package com.meitu.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.meitu.R;
import com.meitu.Interface.OnAvatarClick;
import com.meitu.data.Praise;
import com.meitu.utils.UniversalImageLoadTool;
import com.meitu.view.RoundAngleImageView;

public class PraiseAdapter extends BaseAdapter {
	private List<Praise> praises;
	private Context mContext;

	public PraiseAdapter(Context context, List<Praise> praises) {
		this.mContext = context;
		this.praises = praises;
	}

	@Override
	public int getCount() {
		return praises.size();
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
	public View getView(int position, View convertView, ViewGroup arg2) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.praise_item, null);
			holder = new ViewHolder();
			holder.user_avatar = (RoundAngleImageView) convertView
					.findViewById(R.id.img_user_avatar);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		UniversalImageLoadTool.disPlay(praises.get(position).getUser_avatar(),
				holder.user_avatar, R.drawable.picture_default_head);
		holder.user_avatar.setOnClickListener(new OnAvatarClick(praises.get(
				position).getUser_avatar(), praises.get(position).getUser_id(),
				mContext));
		return convertView;
	}

	class ViewHolder {
		RoundAngleImageView user_avatar;
	}
}
