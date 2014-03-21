package com.example.android.apis.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

public class DrawableCenterTextView extends TextView{

	Rect mTextBounds;
	
	public DrawableCenterTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mTextBounds = new Rect();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Drawable[] drawables = getCompoundDrawables();
		
		if(drawables[0] != null){
			float textWidth = getPaint().measureText(getText().toString());
            int drawablePadding = getCompoundDrawablePadding();
            int drawableWidth = drawables[0].getIntrinsicWidth();
            float bodyWidth = textWidth + drawableWidth + drawablePadding;
            
            canvas.save();
            canvas.translate((getWidth() - bodyWidth) / 2,(getWidth() - drawables[0].getIntrinsicHeight()) / 2);
            drawables[0].draw(canvas);
            canvas.restore();
            getPaint().setTextAlign(Align.CENTER);
            canvas.drawText(getText().toString(),getWidth()/2,getHeight()/2, getPaint());
            
		}else if(drawables[1] != null){
			getPaint().getTextBounds(getText().toString(), 0, getText().length(), mTextBounds);
			float textHeight = mTextBounds.height();
            int drawablePadding = getCompoundDrawablePadding();
            int drawableHeight = drawables[0].getIntrinsicHeight();
            float bodyHeight = textHeight + drawableHeight + drawablePadding;
            canvas.save();
            canvas.translate((getWidth() - drawables[1].getIntrinsicWidth())/ 2, (getHeight() - bodyHeight)/2);
            drawables[1].draw(canvas);
            canvas.restore();
            canvas.drawText(getText().toString(),getWidth()/2,getHeight()/2, getPaint());
		}
	}
}
