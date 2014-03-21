package com.example.android.apis.beefeng.textview;



	import org.xml.sax.XMLReader;

import android.graphics.Color;
import android.text.Editable;
import android.text.Html.TagHandler;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Toast;

	public class MyTagHandler implements TagHandler {

		
		private static MyTagHandler mInstance;
		
		public static synchronized MyTagHandler getInstance(){
			if(mInstance == null){
				mInstance = new MyTagHandler();
			}
			return mInstance;
		}
		
		@Override
		public void handleTag(boolean opening, String tag, Editable output,
				XMLReader xmlReader) {
			if (opening) {
				if (tag.startsWith("at_") || tag.startsWith("us_")) {
					startUser(tag, output);
				}
			} else {
				if (tag.startsWith("at_") || tag.startsWith("us_")) {
					endUser(tag, output);
				}
			}
		}

		private static Object getLast(Spanned text, Class kind) {
			/*
			 * This knows that the last returned object from getSpans() will be the
			 * most recently added.
			 */
			Object[] objs = text.getSpans(0, text.length(), kind);

			if (objs.length == 0) {
				return null;
			} else {
				return objs[objs.length - 1];
			}
		}

		private static void startUser(String tag, Editable output) {
			String id = tag.substring(tag.indexOf("_") + 1);
			output.setSpan(new UserClickableSpan(id), output.length(), output.length(),
					Spannable.SPAN_MARK_MARK);
		}

		private static void endUser(String tag, Editable output) {

			int len = output.length();
			Object obj = getLast(output, UserClickableSpan.class);
			int where = output.getSpanStart(obj);

			output.removeSpan(obj);

			if (where != len) {
				UserClickableSpan h = (UserClickableSpan) obj;
				output.setSpan(new UserClickableSpan(h.mId), where, len,
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				output.insert(len, " ");
			}
		}
		
		public static class UserClickableSpan extends ClickableSpan {

			String mId;

			int color;

			public UserClickableSpan(String id) {
				this.mId = id;
				color = Color.parseColor("#2672ae");
			}

			@Override
			public void updateDrawState(TextPaint ds) {
				ds.setColor(color);
				ds.setUnderlineText(false);
			}

			@Override
			public void onClick(View widget) {
				try {
					Toast.makeText(widget.getContext(), mId + "", Toast.LENGTH_LONG).show();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		}
	}

