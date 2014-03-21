package com.example.android.apis.beefeng.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

public class SwitchRelativeLayout extends RelativeLayout{

	private View mCurrentVisiableChild;
	
	public SwitchRelativeLayout(Context context) {
		super(context);
	}

	public SwitchRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
	
	public SwitchRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if(mCurrentVisiableChild == null){
			showAtIndex(0);
		}
		super.onLayout(changed, l, t, r, b);
	}
	
	public void show(View child){
		for(int i = 0,j = getChildCount(); i < j; i++){
			View view = getChildAt(i);
			if(view == child){
				view.setVisibility(View.VISIBLE);
				if(mCurrentVisiableChild != view){
					onViewSwitch(mCurrentVisiableChild, view);
				}
				mCurrentVisiableChild = view;
			}else{
				view.setVisibility(View.GONE);
			}
		}
	}
	
	public void show(int viewId){
		View child = findViewById(viewId);
		show(child);
	}
	
	public void showAtIndex(int index){
		show(getChildAt(index));
	}
	
	public void onViewSwitch(View lastView, View nextView){
		
	}
}
