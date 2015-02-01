package com.meitu.adapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.meitu.ArticleCommentActivity;
import com.meitu.R;
import com.meitu.Interface.AbstractTaskPostCallBack;
import com.meitu.Interface.OnAvatarClick;
import com.meitu.data.AbstractData.Status;
import com.meitu.data.Article;
import com.meitu.data.ArticleImage;
import com.meitu.data.Praise;
import com.meitu.data.enums.RetError;
import com.meitu.db.DBUtils;
import com.meitu.showbigpic.ImagePagerActivity;
import com.meitu.task.CancelPraiseTask;
import com.meitu.task.PraiseTask;
import com.meitu.utils.Constants;
import com.meitu.utils.SharedUtils;
import com.meitu.utils.ToastUtil;
import com.meitu.utils.UniversalImageLoadTool;
import com.meitu.utils.Utils;
import com.meitu.view.ExpandGridView;

public class ArticleAdapter extends BaseAdapter {
	private Context mContext;
	private boolean isTasking = false;
	private List<Article> lists = new ArrayList<Article>();

	public ArticleAdapter(Context context, List<Article> lists) {
		this.mContext = context;
		this.lists = lists;
	}

	@Override
	public int getCount() {
		return lists.size();
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
		return LayoutInflater.from(mContext).inflate(R.layout.article_item,
				null);

	}

	@Override
	public View getView(final int position, View contentView, ViewGroup arg2) {
		ViewHolder holder = null;
		Article article = lists.get(position);
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
		holder.img_grid_view.setOnItemClickListener(new GridViewOnItemClick(
				position));
		if (article.getComments().size() > 0) {
			holder.btn_comment.setText("�ظ�(" + article.getComments().size()
					+ ")");
		} else {
			holder.btn_comment.setText("�ظ�");
		}
		Drawable drawable = mContext.getResources().getDrawable(
				R.drawable.praise_img_nomal);
		if (article.isPraise()) {
			drawable = mContext.getResources().getDrawable(
					R.drawable.praise_img_focus);
		}
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				drawable.getMinimumHeight());
		holder.btn_praise.setCompoundDrawables(drawable, null, null, null);
		if (article.getPraise_count() > 0) {
			holder.btn_praise.setText("��(" + article.getPraise_count() + ")");
		} else {
			holder.btn_praise.setText("��");
		}
		int imageSize = lists.get(position).getImages().size();
		if (imageSize > 1) {
			if (imageSize > 2) {
				holder.img_grid_view.setNumColumns(3);
			} else {
				holder.img_grid_view.setNumColumns(2);
			}
			holder.img_grid_view.setAdapter(new ArticleImgAdapter(mContext,
					lists.get(position).getImages()));
			holder.img.setVisibility(View.GONE);
			holder.img_grid_view.setVisibility(View.VISIBLE);
		} else if (imageSize == 1) {
			String path = lists.get(position).getImages().get(0).getImg();
			if (!path.startsWith("http")) {
				path = "file://" + path;
			}
			UniversalImageLoadTool.disPlay(path, holder.img,
					R.drawable.empty_photo);
			holder.img.setVisibility(View.VISIBLE);
			holder.img_grid_view.setVisibility(View.GONE);
		} else {
			holder.img.setVisibility(View.GONE);
			holder.img_grid_view.setVisibility(View.GONE);
		}

		String content = lists.get(position).getContent();
		if ("".equals(content)) {
			holder.txt_context.setVisibility(View.GONE);
		} else {
			holder.txt_context.setVisibility(View.VISIBLE);
			holder.txt_context.setText(content);

		}
		holder.txt_time.setText(lists.get(position).getPublished());
		UniversalImageLoadTool.disPlay(article.getPublisher_avatar(),
				holder.img_avatar, R.drawable.default_avatar);
		holder.txt_user_name.setText(article.getPublisher_name());
		holder.img.setOnClickListener(new Onclick(position));
		holder.btn_comment.setOnClickListener(new Onclick(position));
		holder.btn_praise.setOnClickListener(new Onclick(position));
		holder.img_avatar.setOnClickListener(new OnAvatarClick(article
				.getPublisher_avatar(), article.getPublisher_id(), mContext));
		return contentView;
	}

	class Onclick implements OnClickListener {
		int position;

		public Onclick(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			if (lists.get(position).isUploading()) {
				ToastUtil.showToast("���ڷ�����,���Ժ�...");
				return;
			}
			switch (v.getId()) {
			case R.id.btn_comment:
				Intent intent = new Intent();
				intent.putExtra("article", lists.get(position));
				intent.putExtra("position", position);
				intent.setClass(mContext, ArticleCommentActivity.class);
				mContext.startActivity(intent);
				Utils.leftOutRightIn(mContext);
				break;
			case R.id.btn_prise:
				if (isTasking) {
					return;
				}
				Article article = lists.get(position);
				if (!article.isPraise()) {
					praise(article, (TextView) v);
				} else {
					cancelPraise(article, (TextView) v);
				}
				break;
			default:
				intentImagePager(position, 1);
				break;
			}

		}
	}

	private void cancelPraise(final Article article, final TextView v) {
		isTasking = true;
		Drawable drawable = mContext.getResources().getDrawable(
				R.drawable.praise_img_nomal);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				drawable.getMinimumHeight());
		v.setCompoundDrawables(drawable, null, null, null);
		int count = article.getPraise_count() - 1;
		if (count > 0) {
			v.setText("��(" + (article.getPraise_count() - 1) + ")");
		} else {
			v.setText("�� ");

		}
		CancelPraiseTask task = new CancelPraiseTask();
		task.setmCallBack(new AbstractTaskPostCallBack<RetError>() {
			@Override
			public void taskFinish(RetError result) {
				isTasking = false;
				if (result == RetError.NONE) {
					for (Praise pr : article.getPraises()) {
						if (pr.getUser_id() == SharedUtils.getIntUid()) {
							pr.setStatus(Status.DEL);
							pr.write(DBUtils.getDBsa(2));
							Utils.print("res:::::::::::::====" + pr);
							article.getPraises().remove(pr);
							break;
						}
					}
				}
			}
		});
		task.executeParallel(article);
	}

	private void praise(final Article article, TextView v) {
		isTasking = true;
		Drawable drawable = mContext.getResources().getDrawable(
				R.drawable.praise_img_focus);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				drawable.getMinimumHeight());
		v.setCompoundDrawables(drawable, null, null, null);
		int count = article.getPraise_count() + 1;
		v.setText("��(" + (count) + ")");
		PraiseTask task = new PraiseTask();
		task.setmCallBack(new AbstractTaskPostCallBack<RetError>() {
			@Override
			public void taskFinish(RetError result) {
				isTasking = false;
				if (result == RetError.NONE) {
					Praise pr = new Praise();
					pr.setArticle_id(article.getArticle_id());
					pr.setUser_avatar(SharedUtils.getAPPUserAvatar());
					pr.setUser_id(SharedUtils.getIntUid());
					pr.write(DBUtils.getDBsa(2));
					article.getPraises().add(pr);

				}

			}
		});
		task.executeParallel(article);
	}

	class GridViewOnItemClick implements OnItemClickListener {
		int position;

		public GridViewOnItemClick(int position) {
			this.position = position;
		}

		@Override
		public void onItemClick(AdapterView<?> arg0, View v, int posit,
				long arg3) {
			intentImagePager(position, posit);
		}
	}

	private void intentImagePager(int position, int index) {
		List<String> imgUrl = new ArrayList<String>();
		for (ArticleImage img : lists.get(position).getImages()) {
			imgUrl.add(img.getImg());
		}
		Intent intent = new Intent(mContext, ImagePagerActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable(Constants.EXTRA_IMAGE_URLS,
				(Serializable) imgUrl);
		intent.putExtras(bundle);
		intent.putExtra(Constants.EXTRA_IMAGE_INDEX, index);
		mContext.startActivity(intent);
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
