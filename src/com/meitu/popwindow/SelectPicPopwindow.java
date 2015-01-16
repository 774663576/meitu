package com.meitu.popwindow;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.meitu.R;

/**
 * ѡ��ͼƬ ���� ѡ���
 * 
 * @author teeker_bin
 * 
 */
public class SelectPicPopwindow implements OnClickListener {
	private PopupWindow popupWindow;
	private Context mContext;
	private View v;
	private Button btn_menu_1;
	private Button btn_menu_2;
	private Button btnCancle;
	private View view;
	private String fileName = "";
	private SelectOnclick mSelectOnclick;

	private RelativeLayout layout_parent;

	public void setmSelectOnclick(SelectOnclick mSelectOnclick) {
		this.mSelectOnclick = mSelectOnclick;
	}

	public SelectPicPopwindow(Context context, View v, String txt_menu_1,
			String txt_menu_2) {
		this.mContext = context;
		this.v = v;
		LayoutInflater inflater = LayoutInflater.from(mContext);
		view = inflater.inflate(R.layout.select_pic_popowinow_layout, null);
		initView();
		initPopwindow();
		btn_menu_1.setText(txt_menu_1);
		btn_menu_2.setText(txt_menu_2);

	}

	private void initView() {
		layout_parent = (RelativeLayout) view.findViewById(R.id.layout_parent);
		layout_parent.getBackground().setAlpha(150);
		btnCancle = (Button) view.findViewById(R.id.btn_cancel);
		btn_menu_1 = (Button) view.findViewById(R.id.btn_menu_1);
		btn_menu_2 = (Button) view.findViewById(R.id.btn_menu_2);
		btnCancle.setOnClickListener(this);
		btn_menu_1.setOnClickListener(this);
		btn_menu_2.setOnClickListener(this);
		layout_parent.setOnClickListener(this);

	}

	/**
	 * ��ʼ��popwindow
	 */
	@SuppressWarnings("deprecation")
	private void initPopwindow() {
		popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		// �����Ϊ�˵��������Back��Ҳ��ʹ����ʧ�����Ҳ�����Ӱ����ı�����������ģ�
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		// popupWindow.setAnimationStyle(R.style.AnimBottom);
	}

	/**
	 * popwindow����ʾ
	 */
	public void show() {
		popupWindow.showAtLocation(v, Gravity.BOTTOM
				| Gravity.CENTER_HORIZONTAL, 0, 0);
		// ʹ��ۼ�
		popupWindow.setFocusable(true);
		// ����������������ʧ
		popupWindow.setOutsideTouchable(true);
		// ˢ��״̬
		popupWindow.update();
	}

	// ����
	public void dismiss() {
		popupWindow.dismiss();
	}

	/**
	 * ��������֮�󱣴�·��
	 */
	public String getTakePhotoPath() {
		return fileName;
	}

	@Override
	public void onClick(View v) {
		dismiss();
		switch (v.getId()) {
		case R.id.btn_cancel:
			break;
		case R.id.btn_menu_1:
			mSelectOnclick.menu1_select();
			break;
		case R.id.btn_menu_2:
			mSelectOnclick.menu2_select();
			break;
		case R.id.layout_parent:
			dismiss();
			break;
		default:
			break;
		}

	}

	public interface SelectOnclick {
		void menu1_select();

		void menu2_select();

	}
}
