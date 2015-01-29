package com.meitu.popwindow;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.meitu.R;
import com.meitu.utils.SharedUtils;

public class CommentPopwindow implements OnClickListener {
	private PopupWindow popupWindow;
	private Context mContext;
	private View v;
	private LinearLayout layout_parent;
	private View view;
	private TextView txt_del;
	private TextView txt_reply;
	private int position;
	private int publisher_id;
	private OnCommentOnClick mCallBack;
	private View line;

	public OnCommentOnClick getmCallBack() {
		return mCallBack;
	}

	public void setmCallBack(OnCommentOnClick mCallBack) {
		this.mCallBack = mCallBack;
	}

	public CommentPopwindow(Context context, View v, int position,
			int publisher_id) {
		this.mContext = context;
		this.v = v;
		this.position = position;
		this.publisher_id = publisher_id;
		LayoutInflater inflater = LayoutInflater.from(mContext);
		view = inflater.inflate(R.layout.comment_popwindow, null);
		initView();
		initPopwindow();

	}

	private void initView() {
		line = (View) view.findViewById(R.id.line);
		layout_parent = (LinearLayout) view.findViewById(R.id.parent);
		layout_parent.setOnClickListener(this);
		txt_del = (TextView) view.findViewById(R.id.txt_del);
		txt_reply = (TextView) view.findViewById(R.id.txt_reply);
		txt_del.setOnClickListener(this);
		txt_reply.setOnClickListener(this);
		if (publisher_id != SharedUtils.getIntUid()) {
			txt_del.setVisibility(View.GONE);
			line.setVisibility(View.GONE);
		}
	}

	/**
	 * 初始化popwindow
	 */
	@SuppressWarnings("deprecation")
	private void initPopwindow() {
		popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		// 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景（很神奇的）
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
	}

	/**
	 * popwindow的显示
	 */
	public void show() {
		popupWindow.showAtLocation(v, Gravity.CENTER
				| Gravity.CENTER_HORIZONTAL, 0, 0);
		// 使其聚集
		popupWindow.setFocusable(true);
		// 设置允许在外点击消失
		popupWindow.setOutsideTouchable(true);
		// 刷新状态
		popupWindow.update();
	}

	// 隐藏
	public void dismiss() {
		popupWindow.dismiss();
	}

	@Override
	public void onClick(View v) {
		dismiss();
		switch (v.getId()) {
		case R.id.txt_del:
			mCallBack.onClick(position, v.getId());
			break;
		case R.id.txt_reply:
			mCallBack.onClick(position, v.getId());
			break;
		default:
			break;
		}

	}

	public interface OnCommentOnClick {
		void onClick(int position, int id);
	}
}
