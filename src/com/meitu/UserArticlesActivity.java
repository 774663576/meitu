package com.meitu;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.meitu.Interface.AbstractTaskPostCallBack;
import com.meitu.adapter.ArticleAdapter;
import com.meitu.data.Article;
import com.meitu.data.ArticleList;
import com.meitu.data.enums.RetError;
import com.meitu.task.GetArticleByUserIDTask;
import com.meitu.utils.DialogUtil;

public class UserArticlesActivity extends BaseActivity {
	private ListView mlistView;
	private TextView txt_title;
	private ImageView back;

	private ArticleAdapter adapter;
	private List<Article> lists = new ArrayList<Article>();
	private Dialog dialog;

	private ArticleList alist = new ArticleList();

	private int user_id;

	private boolean isLoading = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_articles);
		user_id = getIntent().getIntExtra("user_id", 0);
		initView();
		setValue();
		getArticleList(user_id, "");

	}

	public void initView() {
		back = (ImageView) findViewById(R.id.back);
		txt_title = (TextView) findViewById(R.id.title_txt);
		mlistView = (ListView) findViewById(R.id.listView);
		setListener();
	}

	public void setListener() {
		back.setOnClickListener(this);
		mlistView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// 当不滚动时
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					// 判断是否滚动到底部
					if (view.getLastVisiblePosition() == view.getCount() - 1) {
						// 加载更多功能的代码
						if (isLoading) {
							return;
						}
						getArticleList(user_id, lists.get(lists.size() - 1)
								.getLast_update_time());
					}
				}
			}

			@Override
			public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {

			}
		});
	}

	private void setValue() {
		dialog = DialogUtil.createLoadingDialog(this);
		dialog.show();
		adapter = new ArticleAdapter(this, lists, -1);
		mlistView.setAdapter(adapter);
	}

	private void getArticleList(int uid, String refushTime) {
		isLoading = true;
		GetArticleByUserIDTask task = new GetArticleByUserIDTask(uid,
				refushTime);
		task.setmCallBack(new AbstractTaskPostCallBack<RetError>() {
			@Override
			public void taskFinish(RetError result) {
				isLoading = false;
				if (dialog != null) {
					dialog.dismiss();
				}
				if (result != RetError.NONE) {
					return;
				}
				lists.addAll(alist.getArticles());
				adapter.notifyDataSetChanged();
			}
		});
		task.executeParallel(alist);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finishThisActivity();
			break;

		default:
			break;
		}
	}
}
