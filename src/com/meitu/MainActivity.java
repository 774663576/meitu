package com.meitu;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.meitu.adapter.GrowthAdapter;
import com.meitu.view.PullDownView;
import com.meitu.view.PullDownView.OnPullDownListener;

public class MainActivity extends BaseActivity implements DrawerListener,
		OnPullDownListener {
	private TextView txt_title;
	private ImageView img_menu;
	private PullDownView mPullDownView;
	private ListView mListView;
	public DrawerLayout drawerLayout;// 侧边栏布局

	private GrowthAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		setValue();
	}

	private void initView() {
		img_menu = (ImageView) findViewById(R.id.back);
		img_menu.setImageResource(R.drawable.menu_nomal);
		drawerLayout = (DrawerLayout) findViewById(R.id.main_layout);
		txt_title = (TextView) findViewById(R.id.title_txt);
		txt_title.setText("美图美句");
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
		adapter = new GrowthAdapter(this);
		mListView.setAdapter(adapter);
		mPullDownView.addFooterView();

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

	}

	@Override
	public void onMore() {

	}
}
