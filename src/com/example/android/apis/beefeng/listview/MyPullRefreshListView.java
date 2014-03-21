package com.example.android.apis.beefeng.listview;

import com.example.android.apis.R;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class MyPullRefreshListView extends ListView{

	View mView;
	
	public MyPullRefreshListView(Context context) {
		this(context, null, 0);
		
	}
	
	
	public MyPullRefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	
	public MyPullRefreshListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
		
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
	}
	
	private void init(){
		mView = LayoutInflater.from(getContext()).inflate(R.layout.list_header, null);
		setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//				setPadding(0, 100, 0, 0);
				scrollBy(0, -250); 
			}
		});
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		mView.measure(widthMeasureSpec, heightMeasureSpec);
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t + 130, r, b);
		mView.layout(-50, 0, 200, 30);
	}
	
	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		drawChild(canvas, mView, getDrawingTime());
	}
	
	
	
	protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX,
			int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
		return true;
	}
}
