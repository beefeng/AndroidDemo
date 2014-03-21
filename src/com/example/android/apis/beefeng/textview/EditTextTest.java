package com.example.android.apis.beefeng.textview;

import com.example.android.apis.beefeng.textview.MyTagHandler.UserClickableSpan;

import android.app.Activity;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.style.ReplacementSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class EditTextTest extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LinearLayout layout = new LinearLayout(getApplicationContext());
		layout.setOrientation(LinearLayout.VERTICAL);
		
		setContentView(layout);
		
		EditText eidEditText = new EditText(getApplicationContext());
		layout.addView(eidEditText,new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		
		Button button = new Button(getApplicationContext());
		button.setText("insert");
		layout.addView(button,new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		
		String content = "<body>test <at_1234>@刘庆锋</at_1234></body>";
		eidEditText.setText(Html.fromHtml(content, null, MyTagHandler.getInstance()));
		
		
		ReplacementSpan span;
		final EditWrapper editWrapper = new EditWrapper(eidEditText);
		
		
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				editWrapper.insertUser("刘庆锋", "1234");
			}
		});
	}
	
	
	public  static class EditWrapper implements TextWatcher{
		
		private EditText mEditText;
		
		public EditWrapper(EditText edit){
			mEditText = edit;
			mEditText.addTextChangedListener(this);
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			Log.d("text", "beforeTextChanged() + start: " + start + " count :" + count + " after:" + after);
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			Log.d("text", "onTextChanged() + start: " + start + " before:" + before + " count :" + count);
			
			Editable editor =	 mEditText.getText();
			
			if(before > 0){
				UserClickableSpan[] spans	= editor.getSpans(start - count, start, UserClickableSpan.class);
				if(spans.length > 0){
					int st = editor.getSpanStart(spans[0]);
					int ed = editor.getSpanEnd(spans[0]);
					editor.replace(st, ed, "");
				}
			}else{
				
			}
		}

		@Override
		public void afterTextChanged(Editable s) {
			Log.d("text", "afterTextChanged() + " + s.toString());
			
			
		}
		
		public void insertUser(String name, String id){
			
			String formatUser = String.format("<at_%s>@%s</at_%s>", id,name,id);
			Editable et = mEditText.getText();// 先获取Edittext中的内容
			int start = mEditText.getSelectionStart();
			et.insert(start, formatUser);
			et.append(' ');
			
			mEditText.setText(Html.fromHtml(et.toString(), null, MyTagHandler.getInstance()));
			mEditText.setSelection(start + name.length()+1);
		}
	}
}
