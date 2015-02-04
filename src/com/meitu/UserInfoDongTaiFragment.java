//package com.meitu;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import com.meitu.Interface.AbstractTaskPostCallBack;
//import com.meitu.adapter.ArticleAdapter;
//import com.meitu.data.Article;
//import com.meitu.data.ArticleList;
//import com.meitu.data.enums.RetError;
//import com.meitu.task.GetArticleByUserIDTask;
//import com.meitu.utils.DialogUtil;
//
//import android.app.Dialog;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ListView;
//
//public class UserInfoDongTaiFragment extends Fragment {
//	private ListView mlistView;
//
//	private ArticleAdapter adapter;
//	private List<Article> lists = new ArrayList<Article>();
//	private Dialog dialog;
//
//	private ArticleList alist = new ArticleList();
//
//	private int uid;
//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//		return inflater.inflate(R.layout.dongtai_layout, null);
//	}
//
//	@Override
//	public void onActivityCreated(Bundle savedInstanceState) {
//		super.onActivityCreated(savedInstanceState);
//		initView();
//		setValue();
//	}
//
//	public void initView() {
//		mlistView = (ListView) getView().findViewById(R.id.listView);
//
//	}
//
//	public void setListener() {
//
//	}
//
//	private void setValue() {
//		uid = UserInfoActivity.getUser_id();
//		dialog = DialogUtil.createLoadingDialog(getActivity());
//		dialog.show();
//		getArticleList(uid, "");
//		adapter = new ArticleAdapter(getActivity(), lists);
//		mlistView.setAdapter(adapter);
//	}
//
//	private void getArticleList(int uid, String refushTime) {
//		GetArticleByUserIDTask task = new GetArticleByUserIDTask(uid,
//				refushTime);
//		task.setmCallBack(new AbstractTaskPostCallBack<RetError>() {
//			@Override
//			public void taskFinish(RetError result) {
//				if (dialog != null) {
//					dialog.dismiss();
//				}
//				if (result != RetError.NONE) {
//					return;
//				}
//				lists.addAll(alist.getArticles());
//				adapter.notifyDataSetChanged();
//			}
//		});
//		task.executeParallel(alist);
//	}
//}
