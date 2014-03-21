package com.example.android.apis.beefeng.listview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class TabIndicator extends LinearLayout  implements OnClickListener{

	private TabChangedListener mListener;
	
	public TabIndicator(Context context) {
		super(context);
	}

	public TabIndicator(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	@SuppressLint("NewApi")
	public TabIndicator(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		addChildClickListener();
	}

	private void addChildClickListener(){
		for(int i = 0,j = getChildCount(); i < j; i++){
			getChildAt(i).setOnClickListener(this);
		}
	}
	
	@Override
	public void onClick(View v) {
		boolean changed = false;
		View lastSelected = null;
		for(int i = 0,j = getChildCount(); i < j; i++){
			View child = getChildAt(i);
			boolean selectedBefore = child.isSelected();
			
			if(v == child){
				if(!selectedBefore){
					changed = true;
				}
				v.setSelected(true);
			}else{
				if(selectedBefore){
					lastSelected = child;
					changed = true;
				}
				child.setSelected(false);
			}
		}
		
		if(changed){
			onTabChanged(lastSelected, v);
		}
	}
	
	private void onTabChanged(View last, View next){
		if(mListener != null){
			mListener.onTabChanged(last, next);
		}
	}
	
	public void setOnTabChangedListener(TabChangedListener listener){
		mListener = listener;
	}
	
	public static interface TabChangedListener{
		public void onTabChanged(View preTab, View nextTab);
	}
}
