package com.example.android.apis.beefeng.listview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.example.android.apis.R;

public class MyPullToRefreshListViewActivity extends Activity{

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mypulltorefresh);
		MyPullRefreshListView listView = (MyPullRefreshListView)findViewById(R.id.list);
//		listView.setScrollBarFadeDuration(0);
		listView.setScrollbarFadingEnabled(false);
		
		
		listView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, new String[]{"fafa","假按揭啊","1","2","3","4","5","6","7","8","9","10","11"}));
	}
	
	
}
