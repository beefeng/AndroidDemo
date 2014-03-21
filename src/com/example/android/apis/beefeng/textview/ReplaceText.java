package com.example.android.apis.beefeng.textview;

import org.apache.http.util.ByteArrayBuffer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ReplacementSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android.apis.R;

public class ReplaceText extends Activity{

	EditText mEditText;
	TextView mTextView;
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.replace_text);
		
		
		mEditText = (EditText)findViewById(R.id.edit);
		mTextView = (TextView)findViewById(R.id.text);
		findViewById(R.id.btn_insert).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				insertIntoEditText(getInsertContent());
			}
		});
		
		mEditText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				mTextView.setText(s.toString());
			}
		});
		
		ByteArrayBuffer buffer = new ByteArrayBuffer((int) 1024);
		mTextView.setText(new String(buffer.buffer()));
	}
	
	private SpannableString getInsertContent(){
		SpannableString string = new SpannableString("@刘庆锋1231313134113131313131313131313131313131313131313131[428816]");
 		string.setSpan(new MyReplacementSpan("@刘庆锋"), 0, string.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//		string.setSpan(new ImageSpan(textAsBitmap("@刘庆锋", mEditText),DynamicDrawableSpan.ALIGN_BOTTOM), 0, string.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return string;
	}
	
	private void insertIntoEditText(SpannableString ss) {
	    Editable et = mEditText.getText();// 先获取Edittext中的内容
	    int start = mEditText.getSelectionStart();
	    et.insert(start, ss);
	    mEditText.setText(et);// 把et添加到Edittext中
	    mEditText.setSelection(start + ss.length());
	}
	
	public class  MyReplacementSpan extends ReplacementSpan{

		 protected final int mVerticalAlignment;

		 public static final int ALIGN_BOTTOM = 0;
		    
		    public static final int ALIGN_BASELINE = 1;
		    
		String mText;
		
		Rect mRect;
		public MyReplacementSpan(String text){
			this.mText = text;
			mVerticalAlignment = ALIGN_BOTTOM;
			mRect = new Rect();
		}
		
		@Override
		public int getSize(Paint paint, CharSequence text, int start, int end,
				FontMetricsInt fm) {
			paint.getTextBounds(mText, 0, mText.length(), mRect);
			
			if (fm != null) {
                //We need to make sure the layout allots enough space for the view
                int height = mRect.height();
                int need = height - (fm.descent - fm.ascent);
                if (need > 0) {
                    int ascent = need / 2;
                    //This makes sure the text drawing area will be tall enough for the view
                    fm.descent += need - ascent;
                    fm.ascent -= ascent;
                    fm.bottom += need - ascent;
                    fm.top -= need / 2;
                }
            }
            
			return mRect.right;
		}

		@Override
		public void draw(Canvas canvas, CharSequence text, int start, int end,
				float x, int top, int y, int bottom, Paint paint) {
			
			canvas.save();
			int padding = 0;
			canvas.translate(x, bottom - mRect.bottom - padding);
			canvas.drawText(mText, 0, 0, paint);
			canvas.restore();
			
		}
		
	}
}
