package com.example.android.apis.beefeng.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.apis.R;

public class SimpleGridLayout extends ViewGroup {

	private int mColumCount = 3;

	private int mRowCount = 2;
	
	private int mDivderWidth = 1;
	
	private int mCellWidth;
	
	private int mCellHeight;
	
	public SimpleGridLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SimpleGridLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		if (attrs != null) {
			TypedArray a = getContext().obtainStyledAttributes(attrs,
					R.styleable.SimpleGridLayout_Layout);
			mColumCount = a.getInteger(R.styleable.SimpleGridLayout_columnCount, 3);
			mRowCount = (int) a.getInteger(R.styleable.SimpleGridLayout_rowCount, 2);
			a.recycle();
		}
		
		
	}

	@Override
	protected void onMeasure(int widthSpec, int heightSpec) {
		final int widthMode = MeasureSpec.getMode(widthSpec);
        final int heightMode = MeasureSpec.getMode(heightSpec);
        
        if(widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY){
        	int width = MeasureSpec.getSize(widthSpec);
        	int height = MeasureSpec.getSize(heightSpec);
        	
        	mCellWidth = (width - (mColumCount - 1) * mDivderWidth)/mColumCount;
        	mCellHeight = (height - (mRowCount - 1) * mDivderWidth)/mRowCount;
        	
        	for(int i = 0, childCount = getChildCount(); i < childCount; i++){
        		View child = getChildAt(i);
        		LayoutParams lp = getLayoutParams(child);
        		
        		int childWidth = mCellWidth * lp.colSpan + (lp.colSpan - 1) * mDivderWidth;
        		int childHeight = mCellHeight * lp.rowSpan + (lp.rowSpan - 1) * mDivderWidth;
        		child.measure(MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY));
        	}
        	setMeasuredDimension(width, height);
        }else{
        	setMeasuredDimension(widthSpec, heightSpec);
        }
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		for(int i = 0, childCount = getChildCount(); i < childCount; i++){
    		View child = getChildAt(i);
    		LayoutParams lp = getLayoutParams(child);
    		
    		int left = l +  lp.column * (mCellWidth + mDivderWidth);
    		int top = t + lp.row * (mCellHeight + mDivderWidth);
    		int right = left + child.getMeasuredWidth();
    		int bottom = top + child.getMeasuredHeight();
    		child.layout(left,top,right,bottom);
    	}
	}

	final LayoutParams getLayoutParams(View c) {
        return (LayoutParams) c.getLayoutParams();
    }
	
	@Override
	public LayoutParams generateLayoutParams(AttributeSet attrs) {
		return new LayoutParams(getContext(), attrs);
	}

	@Override
	protected LayoutParams generateDefaultLayoutParams() {
		return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	}

	@Override
	protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams lp) {
		return new LayoutParams(lp);
	}

	public static class LayoutParams extends ViewGroup.MarginLayoutParams {

		private static final int DEFAULT_WIDTH = WRAP_CONTENT;
		private static final int DEFAULT_HEIGHT = WRAP_CONTENT;

		private static final int COLUMN = R.styleable.SimpleGridLayout_Layout_layout_column;
		private static final int COLUMN_SPAN = R.styleable.SimpleGridLayout_Layout_layout_columnSpan;

		private static final int ROW = R.styleable.SimpleGridLayout_Layout_layout_row;
		private static final int ROW_SPAN = R.styleable.SimpleGridLayout_Layout_layout_rowSpan;

		public int column;
		public int colSpan;

		public int row;
		public int rowSpan;

		public LayoutParams(Context arg0, AttributeSet arg1) {
			super(arg0, arg1);
			init(arg0, arg1);
		}

		public LayoutParams(int w, int h) {
			super(w, h);
		}

		public LayoutParams(ViewGroup.LayoutParams lp) {
			super(lp);
		}

		private void init(Context context, AttributeSet attrs) {
			TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SimpleGridLayout_Layout);
			try {

				this.column = a.getInt(COLUMN, 0);
				this.colSpan = a.getInt(COLUMN_SPAN, 1);

				this.row = a.getInt(ROW, 0);
				this.rowSpan = a.getInt(ROW_SPAN, 1);
			} finally {
				a.recycle();
			}
		}

	}
}
