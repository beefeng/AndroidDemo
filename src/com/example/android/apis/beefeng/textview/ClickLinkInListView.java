package com.example.android.apis.beefeng.textview;

import org.xml.sax.XMLReader;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.Html.TagHandler;
import android.text.Layout;
import android.text.NoCopySpan;
import android.text.Selection;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.MovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.apis.R;

public class ClickLinkInListView extends Activity implements
		OnItemClickListener {

	ListView mListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.beefeng_click_link_in_listview);
		mListView = (ListView) findViewById(R.id.list);

		mListView.setAdapter(new MyAdapter(getApplicationContext()));

		mListView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Toast.makeText(getApplicationContext(),
				"item " + position + " clicked", Toast.LENGTH_LONG).show();
	}

	public class MyAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		public MyAdapter(Context context) {
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return 10;
		}

		@Override
		public Object getItem(int position) {
			return "@刘庆丰" + position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@SuppressLint("NewApi")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = mInflater.inflate(
						 R.layout.beefeng_list_item, null);
			}

			TextView textView = (TextView) convertView
					.findViewById(  R.id.textView1);

			textView.setText(Html.fromHtml(String.format(
					"<body><at_1234>%s</at_1234></body>", getItem(position)),
					null, MyTagHandler.getInstance()));
			textView.setClickable(false);
			textView.setMovementMethod(MyLinkMovementMethod.getInstance());
			textView.setFocusable(false);
			return convertView;
		}
	}

	public static class MyTagHandler implements TagHandler {

		private static MyTagHandler mInstance;

		public static synchronized MyTagHandler getInstance() {
			if (mInstance == null) {
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
			 * This knows that the last returned object from getSpans() will be
			 * the most recently added.
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
			output.setSpan(new UserClickableSpan(id), output.length(),
					output.length(), Spannable.SPAN_MARK_MARK);
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
				Toast.makeText(widget.getContext(), mId, Toast.LENGTH_LONG)
						.show();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	public static class MyLinkMovementMethod extends ScrollingMovementMethod {

		private static final int CLICK = 1;
		private static final int UP = 2;
		private static final int DOWN = 3;
		
		BackgroundColorSpan mSpan = new BackgroundColorSpan(Color.RED);

		@SuppressLint("NewApi")
		@Override
		protected boolean handleMovementKey(TextView widget, Spannable buffer,
				int keyCode, int movementMetaState, KeyEvent event) {
			switch (keyCode) {
			case KeyEvent.KEYCODE_DPAD_CENTER:
			case KeyEvent.KEYCODE_ENTER:
				if (KeyEvent.metaStateHasNoModifiers(movementMetaState)) {
					if (event.getAction() == KeyEvent.ACTION_DOWN
							&& event.getRepeatCount() == 0
							&& action(CLICK, widget, buffer)) {
						return false;
					}
				}
				break;
			}
			return false;
		}

		@Override
		protected boolean up(TextView widget, Spannable buffer) {
			if (action(UP, widget, buffer)) {
				return false;
			}

			return false;
		}

		@Override
		protected boolean down(TextView widget, Spannable buffer) {
			if (action(DOWN, widget, buffer)) {
				return false;
			}

			return false;
		}

		@Override
		protected boolean left(TextView widget, Spannable buffer) {
			if (action(UP, widget, buffer)) {
				return false;
			}

			return false;
		}

		@Override
		protected boolean right(TextView widget, Spannable buffer) {
			if (action(DOWN, widget, buffer)) {
				return false;
			}

			return false;
		}

		private boolean action(int what, TextView widget, Spannable buffer) {
			Layout layout = widget.getLayout();

			int padding = widget.getTotalPaddingTop()
					+ widget.getTotalPaddingBottom();
			int areatop = widget.getScrollY();
			int areabot = areatop + widget.getHeight() - padding;

			int linetop = layout.getLineForVertical(areatop);
			int linebot = layout.getLineForVertical(areabot);

			int first = layout.getLineStart(linetop);
			int last = layout.getLineEnd(linebot);

			ClickableSpan[] candidates = buffer.getSpans(first, last,
					ClickableSpan.class);

			int a = Selection.getSelectionStart(buffer);
			int b = Selection.getSelectionEnd(buffer);

			int selStart = Math.min(a, b);
			int selEnd = Math.max(a, b);

			if (selStart < 0) {
				if (buffer.getSpanStart(FROM_BELOW) >= 0) {
					selStart = selEnd = buffer.length();
				}
			}

			if (selStart > last)
				selStart = selEnd = Integer.MAX_VALUE;
			if (selEnd < first)
				selStart = selEnd = -1;

			switch (what) {
			case CLICK:
				if (selStart == selEnd) {
					return false;
				}

				ClickableSpan[] link = buffer.getSpans(selStart, selEnd,
						ClickableSpan.class);

				if (link.length != 1)
					return false;

				link[0].onClick(widget);
				break;

			case UP:
				int beststart,
				bestend;

				beststart = -1;
				bestend = -1;

				for (int i = 0; i < candidates.length; i++) {
					int end = buffer.getSpanEnd(candidates[i]);

					if (end < selEnd || selStart == selEnd) {
						if (end > bestend) {
							beststart = buffer.getSpanStart(candidates[i]);
							bestend = end;
						}
					}
				}

				if (beststart >= 0) {
					Selection.setSelection(buffer, bestend, beststart);
					return false;
				}

				break;

			case DOWN:
				beststart = Integer.MAX_VALUE;
				bestend = Integer.MAX_VALUE;

				for (int i = 0; i < candidates.length; i++) {
					int start = buffer.getSpanStart(candidates[i]);

					if (start > selStart || selStart == selEnd) {
						if (start < beststart) {
							beststart = start;
							bestend = buffer.getSpanEnd(candidates[i]);
						}
					}
				}

				if (bestend < Integer.MAX_VALUE) {
					Selection.setSelection(buffer, beststart, bestend);
					return false;
				}

				break;
			}

			return false;
		}

		@Override
		public boolean onTouchEvent(TextView widget, Spannable buffer,
				MotionEvent event) {
			int action = event.getAction();

			if (action == MotionEvent.ACTION_UP
					|| action == MotionEvent.ACTION_DOWN) {
				int x = (int) event.getX();
				int y = (int) event.getY();

				x -= widget.getTotalPaddingLeft();
				y -= widget.getTotalPaddingTop();

				x += widget.getScrollX();
				y += widget.getScrollY();

				Layout layout = widget.getLayout();
				int line = layout.getLineForVertical(y);
				int off = layout.getOffsetForHorizontal(line, x);

				ClickableSpan[] link = buffer.getSpans(off, off,
						ClickableSpan.class);

				if (link.length != 0) {
					if (action == MotionEvent.ACTION_UP) {
						link[0].onClick(widget);
						buffer.removeSpan(mSpan);
					} else if (action == MotionEvent.ACTION_DOWN) {
						
						buffer.setSpan(mSpan, buffer.getSpanStart(link[0]), buffer.getSpanEnd(link[0]),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
						
//						Selection.setSelection(buffer,
//								buffer.getSpanStart(link[0]),
//								buffer.getSpanEnd(link[0]));
//						widget.invalidate();
					}

					return false;
				} else {
					Selection.removeSelection(buffer);
				}
			}else if(action == MotionEvent.ACTION_CANCEL){
				buffer.removeSpan(mSpan);
			}

			return false;
		}

		@Override
		public void initialize(TextView widget, Spannable text) {
			Selection.removeSelection(text);
			text.removeSpan(FROM_BELOW);
		}

		@Override
		public void onTakeFocus(TextView view, Spannable text, int dir) {
			Selection.removeSelection(text);

			if ((dir & View.FOCUS_BACKWARD) != 0) {
				text.setSpan(FROM_BELOW, 0, 0, Spannable.SPAN_POINT_POINT);
			} else {
				text.removeSpan(FROM_BELOW);
			}
		}

		public static MovementMethod getInstance() {
			if (sInstance == null)
				sInstance = new MyLinkMovementMethod();

			return sInstance;
		}

		private static MyLinkMovementMethod sInstance;
		private static Object FROM_BELOW = new NoCopySpan.Concrete();
	}
}
