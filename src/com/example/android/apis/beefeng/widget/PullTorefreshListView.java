package com.example.android.apis.beefeng.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class PullTorefreshListView extends ListView implements IPullToRefresh{

	public PullTorefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}


	@Override
	public boolean isOverScrollEnable() {
		return false;
	}
}
