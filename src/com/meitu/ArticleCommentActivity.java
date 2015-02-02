package com.meitu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.meitu.Interface.AbstractTaskPostCallBack;
import com.meitu.adapter.ArticleImgAdapter;
import com.meitu.adapter.CommentAdapter;
import com.meitu.adapter.PraiseAdapter;
import com.meitu.data.Article;
import com.meitu.data.ArticleImage;
import com.meitu.data.Comment;
import com.meitu.data.enums.RetError;
import com.meitu.popwindow.CommentPopwindow;
import com.meitu.popwindow.CommentPopwindow.OnCommentOnClick;
import com.meitu.showbigpic.ImagePagerActivity;
import com.meitu.task.DeleteCommentTask;
import com.meitu.task.SendCommentTask;
import com.meitu.utils.Constants;
import com.meitu.utils.DateUtils;
import com.meitu.utils.DialogUtil;
import com.meitu.utils.SharedUtils;
import com.meitu.utils.ToastUtil;
import com.meitu.utils.UniversalImageLoadTool;
import com.meitu.utils.Utils;
import com.meitu.view.ExpandGridView;
import com.meitu.view.HorizontalListView;

public class ArticleCommentActivity extends BaseActivity implements
		OnItemClickListener, TextWatcher, OnCommentOnClick {
	private ImageView img_avatar;
	private TextView txt_user_name;
	private TextView txt_time;
	private TextView txt_context;
	private ImageView img;
	private ExpandGridView img_grid_view;
	private ImageView back;
	private TextView txt_title;
	private Button btnComment;
	private EditText edit_comment;
	private ListView mListView;

	private Article article;

	private Dialog dialog;

	private CommentAdapter adapter;
	private List<Comment> comments = new ArrayList<Comment>();

	private int position;

	private boolean isReplaySomeOne = false;

	private int replaySomeOneID = 0;
	private String replaySomeOneName = "";

	private CommentPopwindow pop;

	private LinearLayout parise_layout;
	private HorizontalListView praise_listView;
	private PraiseAdapter praiseAdapter;
	private LinearLayout comment_layout;

	private RelativeLayout layout_title;

	private boolean isTasking = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_article_comment);
		article = (Article) getIntent().getSerializableExtra("article");
		position = getIntent().getIntExtra("position", 0);
		initView();
		setValue();
	}

	private void initView() {
		layout_title = (RelativeLayout) findViewById(R.id.layout_title);
		back = (ImageView) findViewById(R.id.back);
		txt_title = (TextView) findViewById(R.id.title_txt);
		img = (ImageView) findViewById(R.id.img);
		txt_context = (TextView) findViewById(R.id.txt_content);
		txt_time = (TextView) findViewById(R.id.txt_time);
		txt_user_name = (TextView) findViewById(R.id.txt_user_name);
		img_avatar = (ImageView) findViewById(R.id.img_avatar);
		img_grid_view = (ExpandGridView) findViewById(R.id.imgGridview);
		btnComment = (Button) findViewById(R.id.btnComment);
		edit_comment = (EditText) findViewById(R.id.edit_content);
		mListView = (ListView) findViewById(R.id.listView1);
		parise_layout = (LinearLayout) findViewById(R.id.layout_praise);
		praise_listView = (HorizontalListView) findViewById(R.id.praise_listView);
		comment_layout = (LinearLayout) findViewById(R.id.layout_comment);
		setListener();
	}

	private void setListener() {
		back.setOnClickListener(this);
		btnComment.setOnClickListener(this);
		edit_comment.addTextChangedListener(this);
		mListView.setOnItemClickListener(this);
		img_grid_view.setOnItemClickListener(new GridViewOnItemClick());
		Utils.getFocus(layout_title);
	}

	private void setValue() {
		txt_title.setText("∆¿¬€");
		int imageSize = article.getImages().size();
		if (imageSize > 1) {
			if (imageSize > 2) {
				img_grid_view.setNumColumns(3);
			} else {
				img_grid_view.setNumColumns(2);
			}
			img_grid_view.setAdapter(new ArticleImgAdapter(this, article
					.getImages()));
			img.setVisibility(View.GONE);
			img_grid_view.setVisibility(View.VISIBLE);
		} else if (imageSize == 1) {
			String path = article.getImages().get(0).getImg();
			if (!path.startsWith("http")) {
				path = "file://" + path;
			}
			UniversalImageLoadTool.disPlay(path, img, R.drawable.empty_photo);
			img.setVisibility(View.VISIBLE);
			img_grid_view.setVisibility(View.GONE);
		} else {
			img.setVisibility(View.GONE);
			img_grid_view.setVisibility(View.GONE);
		}
		String content = article.getContent();
		if ("".equals(content)) {
			txt_context.setVisibility(View.GONE);
		} else {
			txt_context.setVisibility(View.VISIBLE);
			txt_context.setText(content);

		}
		txt_time.setText(article.getPublished());
		UniversalImageLoadTool.disPlay(article.getPublisher_avatar(),
				img_avatar, R.drawable.default_avatar);
		txt_user_name.setText(article.getPublisher_name());
		comments = article.getComments();
		adapter = new CommentAdapter(this, comments);
		mListView.setAdapter(adapter);
		viewLineVisible();
		praiseAdapter = new PraiseAdapter(this, article.getPraises());
		if (article.getPraises().size() > 0) {
			parise_layout.setVisibility(View.VISIBLE);
			praise_listView.setAdapter(praiseAdapter);
		} else {
			parise_layout.setVisibility(View.GONE);

		}

	}

	class GridViewOnItemClick implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View v, int posit,
				long arg3) {
			intentImagePager(posit);
		}
	}

	private void intentImagePager(int index) {
		List<String> imgUrl = new ArrayList<String>();
		for (ArticleImage img : article.getImages()) {
			imgUrl.add(img.getImg());
		}
		Intent intent = new Intent(this, ImagePagerActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable(Constants.EXTRA_IMAGE_URLS,
				(Serializable) imgUrl);
		intent.putExtras(bundle);
		intent.putExtra(Constants.EXTRA_IMAGE_INDEX, index);
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finishThisActivity();
			break;
		case R.id.btnComment:
			String content = edit_comment.getText().toString().trim();
			if (content.length() == 0) {
				return;
			}
			sendComment(content.replace("@" + replaySomeOneName, ""));
			break;
		default:
			break;
		}
	}

	private void viewLineVisible() {
		if (comments.size() > 0) {
			comment_layout.setVisibility(View.VISIBLE);
		} else {
			comment_layout.setVisibility(View.GONE);
		}

	}

	private void sendComment(String content) {
		dialog = DialogUtil.createLoadingDialog(this, "«Î…‘∫Ú");
		dialog.show();
		final Comment comment = new Comment();
		comment.setComment_content(content);
		if (isReplaySomeOne) {
			comment.setReply_someone_name(replaySomeOneName);
			comment.setReply_someone_id(replaySomeOneID);
		}
		comment.setArticle_id(article.getArticle_id());
		comment.setComment_time(DateUtils.getGrowthShowTime());
		comment.setPublisher_id(SharedUtils.getIntUid());
		comment.setPublisher_avatar(SharedUtils.getAPPUserAvatar());
		comment.setPublisher_name(SharedUtils.getAPPUserName());
		SendCommentTask task = new SendCommentTask(article.getPublisher_id());
		task.setmCallBack(new AbstractTaskPostCallBack<RetError>() {
			@Override
			public void taskFinish(RetError result) {
				if (dialog != null) {
					dialog.dismiss();
				}
				if (result != RetError.NONE) {
					return;
				}
				edit_comment.setText("");
				ToastUtil.showToast("ªÿ∏¥≥…π¶");
				comments.add(0, comment);
				adapter.notifyDataSetChanged();
				viewLineVisible();
				System.out.println("size::::::::;" + comments.size());
				Intent intent = new Intent();
				intent.putExtra("position", position);
				intent.putExtra("comment", comment);
				intent.setAction(Constants.COMMENT_ARTICLE);
				sendBroadcast(intent);
			}
		});
		task.executeParallel(comment);
	}

	private void delReplaySomeOne() {
		isReplaySomeOne = false;
		replaySomeOneID = 0;
		replaySomeOneName = "";
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position,
			long arg3) {
		pop = new CommentPopwindow(this, view, position,
				article.getPublisher_id());
		pop.setmCallBack(this);
		pop.show();

	}

	@Override
	public void afterTextChanged(Editable str) {
		if (isReplaySomeOne) {
			if (replaySomeOneName.equals(str.toString().replace("@", ""))) {
				edit_comment.setText("");
				delReplaySomeOne();
			}
		}
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {

	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

	}

	@Override
	public void onClick(int position, int id) {
		switch (id) {
		case R.id.txt_reply:
			reply(position);
			break;
		case R.id.txt_del:
			del(position);
			break;
		default:
			break;
		}
	}

	private void del(final int position) {
		final Dialog dialog = DialogUtil.createLoadingDialog(this, "«Î…‘∫Ú");
		dialog.show();
		final Comment comment = comments.get(position);
		DeleteCommentTask task = new DeleteCommentTask();
		task.setmCallBack(new AbstractTaskPostCallBack<RetError>() {
			@Override
			public void taskFinish(RetError result) {
				if (dialog != null) {
					dialog.dismiss();
				}
				if (result != RetError.NONE) {
					return;
				}
				sendBroadcast(new Intent(Constants.DEL_COMMENT).putExtra(
						"article_id", article.getArticle_id()).putExtra(
						"comment_id", comment.getComment_id()));
				comments.remove(position);
				adapter.notifyDataSetChanged();
				viewLineVisible();

			}
		});
		task.executeParallel(comment);
	}

	private void reply(int position) {
		Comment comment = comments.get(position);
		edit_comment.setText(Html.fromHtml("<font color=#F06617>@"
				+ comment.getPublisher_name() + "</font> "));
		edit_comment.setSelection(edit_comment.getText().toString().length());
		Utils.getFocus(edit_comment);
		isReplaySomeOne = true;
		replaySomeOneID = comment.getPublisher_id();
		replaySomeOneName = comment.getPublisher_name();
	}
}
