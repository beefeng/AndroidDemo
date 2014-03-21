package com.example.android.apis.beefeng.listview;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.example.android.apis.R;

public class WebViewInListVIew extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);
		
		ListView list = (ListView)findViewById(R.id.list);
		
		list.setAdapter(new BaseAdapter() {
			
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				WebView webview = null;
				if(convertView == null){
					webview = new WebView(WebViewInListVIew.this);
					webview.setWebViewClient(new WebViewClient() {
						@Override
						public boolean shouldOverrideUrlLoading(WebView view, String url) {
							return false;
						}
					});
					
					webview.getSettings().setDefaultTextEncodingName("utf-8");
					convertView = webview;
				}
				webview = (WebView)convertView;
				webview.loadUrl("http://baidu.com");
				return convertView;
			}
			
			@Override
			public long getItemId(int position) {
				return 0;
			}
			
			@Override
			public Object getItem(int position) {
				return null;
			}
			
			@Override
			public int getCount() {
				return 3;
			}
		});
	}
}
