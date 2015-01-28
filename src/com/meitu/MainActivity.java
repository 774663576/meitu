package com.meitu;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.meitu.Interface.AbstractTaskPostCallBack;
import com.meitu.adapter.ArticleAdapter;
import com.meitu.data.Article;
import com.meitu.data.ArticleList;
import com.meitu.data.enums.RetError;
import com.meitu.task.GetArticleListTask;
import com.meitu.task.GetArticlesFormDBTask;
import com.meitu.task.UpLoadArticleTask;
import com.meitu.utils.DateUtils;
import com.meitu.utils.DialogUtil;
import com.meitu.utils.SharedUtils;
import com.meitu.utils.ToastUtil;
import com.meitu.view.PullDownView;
import com.meitu.view.PullDownView.OnPullDownListener;

public class MainActivity extends BaseActivity implements DrawerListener,
		OnPullDownListener {
	private TextView txt_title;
	private ImageView img_menu;
	private PullDownView mPullDownView;
	private ListView mListView;
	public DrawerLayout drawerLayout;// 侧边栏布局

	private ArticleAdapter adapter;

	private List<Article> lists = new ArrayList<Article>();
	private Dialog dialog;

	private ArticleList alist = new ArticleList();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		setValue();
		getGrowthFromDB();
	}

	private void initView() {
		img_menu = (ImageView) findViewById(R.id.back);
		img_menu.setImageResource(R.drawable.menu_nomal);
		drawerLayout = (DrawerLayout) findViewById(R.id.main_layout);
		txt_title = (TextView) findViewById(R.id.title_txt);
		txt_title.setText("心灵鸡汤");
		mPullDownView = (PullDownView) findViewById(R.id.PullDownlistView);
		mListView = mPullDownView.getListView();
		mListView.setVerticalScrollBarEnabled(false);
		mListView.setCacheColorHint(0);
		mListView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		setListener();
	}

	private void setListener() {
		drawerLayout.setDrawerListener(this);
		mPullDownView.setOnPullDownListener(this);
		mPullDownView.notifyDidMore();
		mPullDownView.setFooterVisible(false);
	}

	private void setValue() {
		adapter = new ArticleAdapter(this, lists);
		mListView.setAdapter(adapter);
		mPullDownView.addFooterView();

	}

	private void getGrowthFromDB() {
		dialog = DialogUtil.createLoadingDialog(this);
		dialog.show();
		GetArticlesFormDBTask task = new GetArticlesFormDBTask();
		task.setmCallBack(new AbstractTaskPostCallBack<RetError>() {
			@Override
			public void taskFinish(RetError result) {
				lists.addAll(alist.getArticles());
				adapter.notifyDataSetChanged();
				if (lists.size() == 0) {
					getArticleList();
				} else {
					if (dialog != null) {
						dialog.dismiss();
					}
					if (lists.size() > 19) {
						mPullDownView.setFooterVisible(true);
					} else {
						mPullDownView.setFooterVisible(false);

					}
					alist.setRefushState(1);
					alist.setRefushTime(lists.get(0).getLast_update_time());
					getArticleList();
				}
			}
		});
		task.executeParallel(alist);
	}

	private void getArticleList() {
		GetArticleListTask task = new GetArticleListTask();
		task.setmCallBack(new AbstractTaskPostCallBack<RetError>() {
			@Override
			public void taskFinish(RetError result) {

				mPullDownView.RefreshComplete();
				mPullDownView.notifyDidMore();
				if (dialog != null) {
					dialog.dismiss();
				}
				if (result != RetError.NONE) {
					return;
				}
				lists.clear();
				lists.addAll(alist.getArticles());
				adapter.notifyDataSetChanged();
				if (alist.getRequestArticles().size() > 19) {
					mPullDownView.setFooterVisible(true);
				} else {
					mPullDownView.setFooterVisible(false);
					if (lists.size() > 19
							&& alist.getRequestArticles().size() == 0) {
						mPullDownView.setFooterVisible(true);

					}
				}
			}
		});
		task.executeParallel(alist);
	}

	@Override
	public void onDrawerClosed(View arg0) {
		img_menu.setImageResource(R.drawable.menu_nomal);

	}

	@Override
	public void onDrawerOpened(View arg0) {
		img_menu.setImageResource(R.drawable.menu_select);
	}

	@Override
	public void onDrawerSlide(View arg0, float arg1) {

	}

	@Override
	public void onDrawerStateChanged(int arg0) {

	}

	@Override
	public void onRefresh() {
		if (lists.size() == 0) {
			mPullDownView.RefreshComplete();
			return;
		}
		alist.setRefushState(1);
		alist.setRefushTime(lists.get(0).getLast_update_time());
		getArticleList();
	}

	@Override
	public void onMore() {
		alist.setRefushState(2);
		alist.setRefushTime(lists.get(lists.size() - 1).getPublished());
		getArticleList();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		drawerLayout.closeDrawers();
		if (requestCode == 200) {
			if (data == null) {
				return;
			}
			Article article = (Article) data.getSerializableExtra("article");
			article.setPublisher_id(SharedUtils.getIntUid());
			article.setPublished(DateUtils.getGrowthShowTime());
			article.setPublisher_avatar(SharedUtils.getAPPUserAvatar());
			article.setPublisher_name(SharedUtils.getAPPUserName());
			lists.add(0, article);
			adapter.notifyDataSetChanged();
			mListView.setSelection(0);
			upLoadGrowth(article);

		}
	}

	private boolean isUpLoading = false;

	public boolean isUpLoading() {
		return isUpLoading;
	}

	private void upLoadGrowth(Article article) {
		isUpLoading = true;
		UpLoadArticleTask task = new UpLoadArticleTask();
		task.setmCallBack(new AbstractTaskPostCallBack<RetError>() {
			@Override
			public void taskFinish(RetError result) {
				isUpLoading = false;
				if (result != RetError.NONE) {
					return;
				}
				ToastUtil.showToast("发布成功", Toast.LENGTH_SHORT);
				adapter.notifyDataSetChanged();
				for (Article g : lists) {
					if (g.isUploading()) {
						g.setUploading(false);
						break;
					}
				}
			}
		});
		task.executeParallel(article);
	}
}
