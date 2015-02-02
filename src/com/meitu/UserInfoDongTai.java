package com.meitu;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.view.View;
import android.widget.ListView;

import com.meitu.Interface.AbstractTaskPostCallBack;
import com.meitu.adapter.ArticleAdapter;
import com.meitu.data.Article;
import com.meitu.data.ArticleList;
import com.meitu.data.enums.RetError;
import com.meitu.task.GetArticleByUserIDTask;
import com.meitu.utils.DialogUtil;
import com.meitu.utils.Utils;

public class UserInfoDongTai extends UserInfo {
	private ListView mlistView;

	private ArticleAdapter adapter;
	private List<Article> lists = new ArrayList<Article>();
	private Dialog dialog;

	private ArticleList alist = new ArticleList();

	private int uid;

	public UserInfoDongTai(UserInfoActivity activity, View contentRootView) {
		super(activity, contentRootView);
		uid = activity.getUser_id();
		setValue();
		dialog = DialogUtil.createLoadingDialog(mContext);
		dialog.show();
		getArticleList(uid, "");
	}

	@Override
	public void initView() {
		mlistView = (ListView) findViewById(R.id.listView);
	}

	@Override
	public void setListener() {

	}

	private void setValue() {
		adapter = new ArticleAdapter(mContext, lists);
		mlistView.setAdapter(adapter);

	}

	private void getArticleList(int uid, String refushTime) {
		GetArticleByUserIDTask task = new GetArticleByUserIDTask(uid,
				refushTime);
		task.setmCallBack(new AbstractTaskPostCallBack<RetError>() {
			@Override
			public void taskFinish(RetError result) {
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

}
