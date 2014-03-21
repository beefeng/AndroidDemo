package com.example.android.apis.beefeng.listview;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.android.apis.R;

public class ListHeaderFooter extends Activity{

	LoadingHeaderFooterListAdapter<BaseAdapter> mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);
		
		ListView listView = (ListView)findViewById(R.id.list);
		
		TextView text = new TextView(getApplicationContext());
		text.setText("header");
		listView.addHeaderView(text);
		
		TextView text2 = new TextView(getApplicationContext());
		text2.setText("footer");
		listView.addFooterView(text2);
		
		mAdapter = new LoadingHeaderFooterListAdapter<BaseAdapter>(listView, new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, new String[]{"item1", "item2","item3"}));
		listView.setAdapter(mAdapter);
		mAdapter.setLoadingHeaderVisibility(true);
		mAdapter.setLoadingFooterStatus(LoadingHeaderFooterListAdapter.STATUS_HAS_MORE);
	}
	
}
