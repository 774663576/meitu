package com.meitu.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.meitu.R;
import com.meitu.Interface.OnAvatarClick;
import com.meitu.data.Comment;
import com.meitu.utils.SharedUtils;
import com.meitu.utils.UniversalImageLoadTool;
import com.meitu.utils.Utils;
import com.meitu.view.RoundAngleImageView;

public class CommentAdapter extends BaseAdapter {
	private List<Comment> list = new ArrayList<Comment>();
	private Context mContext;

	public CommentAdapter(Context context, List<Comment> list) {
		this.mContext = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
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
	public View getView(int position, View contentView, ViewGroup arg2) {
		ViewHolder holder = null;
		Comment comment = list.get(position);
		if (contentView == null) {
			contentView = LayoutInflater.from(mContext).inflate(
					R.layout.comment_item, null);
			holder = new ViewHolder();
			holder.img_user_avatar = (RoundAngleImageView) contentView
					.findViewById(R.id.img_user_avatar);
			holder.txt_user_name = (TextView) contentView
					.findViewById(R.id.txt_user_name);
			holder.txt_comment_content = (TextView) contentView
					.findViewById(R.id.txt_comment_content);
			holder.txt_time = (TextView) contentView
					.findViewById(R.id.txt_time);
			contentView.setTag(holder);
		} else {
			holder = (ViewHolder) contentView.getTag();
		}

		holder.txt_user_name.setText(list.get(position).getPublisher_name());
		UniversalImageLoadTool.disPlay(
				list.get(position).getPublisher_avatar(),
				holder.img_user_avatar, R.drawable.picture_default_head);
		holder.img_user_avatar.setOnClickListener(new OnAvatarClick(list.get(
				position).getPublisher_id(), mContext));
		String content = comment.getComment_content();
		if (!"".equals(comment.getReply_someone_name())
				&& comment.getReply_someone_id() != 0) {
			if (comment.getPublisher_id() == SharedUtils.getIntUid()) {
				holder.txt_comment_content.setText(Html
						.fromHtml("<font color=#F06617>@"
								+ comment.getReply_someone_name()
								+ "</font>  "
								+ Html.fromHtml("<font color=#F06617>"
										+ content + "</font>  ")));
			} else {
				holder.txt_comment_content.setText(Html
						.fromHtml("<font color=#F06617>@"
								+ comment.getReply_someone_name()
								+ "</font>  "
								+ Html.fromHtml("<font color=#000000>"
										+ content + "</font>  ")));
			}

		} else {
			holder.txt_comment_content.setText(content);
			if (comment.getPublisher_id() == SharedUtils.getIntUid()) {
				holder.txt_comment_content.setTextColor(mContext.getResources()
						.getColor(R.color.self_comment_color));
				Utils.print("aaaaaaaaaaaaaaaaaaaaaaa");
			} else {
				holder.txt_comment_content.setTextColor(Color.BLACK);
			}
		}
		holder.txt_time.setText(list.get(position).getComment_time());
		if (comment.getPublisher_id() == SharedUtils.getIntUid()) {
			holder.txt_comment_content.setTextColor(Color.rgb(55, 182, 105));
		} else {
			holder.txt_comment_content.setTextColor(Color.BLACK);
		}
		System.out.println("result::::::::::::;" + content);
		return contentView;
	}

	static class ViewHolder {
		private RoundAngleImageView img_user_avatar;
		private TextView txt_user_name;
		private TextView txt_comment_content;
		private TextView txt_time;

	}
}
