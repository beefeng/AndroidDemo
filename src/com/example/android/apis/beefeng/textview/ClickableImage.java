package com.example.android.apis.beefeng.textview;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android.apis.R;
import com.example.android.apis.graphics.Typefaces;
import com.example.android.apis.utils.AppUtils;

public class ClickableImage extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.text_clickable_image);
		final TextView textView = (TextView)findViewById(R.id.content);
		
		final EditText editText = (EditText)findViewById(R.id.input);
		findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				textView.setText(editText.getText());
				LinkifyUtil.applyLink(textView);
			}
		});
		
		String content = "百度一下，你就知道：http://www.baidu.com";
		textView.setText(content);
		LinkifyUtil.applyLink(textView);
	}
	

	public static  class LinkifyUtil {

		private static Pattern hyperLinksPattern = Pattern
				.compile("((([Ff]|[Hh][Tt])[Tt][Pp][Ss]?://)(\\.?[a-zA-Z0-9][-a-zA-Z0-9]*)+(\\.[a-zA-Z]{2,})[-a-zA-Z0-9@:%_+~#.,;!?&/\\|=]*)(-|\\+|/|\\$|\\?|!|,|\\b|$|)");

		public static void applyLink(TextView view) {
			final SpannableStringBuilder linkableText = new SpannableStringBuilder(view.getText());

			Matcher m = hyperLinksPattern.matcher(linkableText);
			List<Integer>  ends = new ArrayList<Integer>();
			while (m.find()) {
				final int start = m.start();
				final int end = m.end();
				String url = linkableText.subSequence(start, end).toString();
				linkableText.setSpan(new ClickableImageSpan(view.getContext(),url,R.drawable.weblin_normal,R.drawable.weblink_press), start, end,
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				ends.add(end);
			}
			
			try {
				//每个链接之后插入空格
				for(int i = ends.size() - 1; i >=0; i--){
					linkableText.insert(ends.get(i), " ");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			

			view.setFocusable(false);
			view.setClickable(false);
			view.setText(linkableText);
			
			view.setOnTouchListener(new OnTouchListener() {
	            @Override
	            public boolean onTouch(View v, MotionEvent event) {
	                boolean ret = false;
	                CharSequence text = ((TextView) v).getText();
	                Spannable stext = Spannable.Factory.getInstance().newSpannable(text);
	                TextView widget = (TextView) v;
	                int action = event.getAction();

	                if (action == MotionEvent.ACTION_UP ||
	                        action == MotionEvent.ACTION_DOWN) {
	                    int x = (int) event.getX();
	                    int y = (int) event.getY();

	                    x -= widget.getTotalPaddingLeft();
	                    y -= widget.getTotalPaddingTop();

	                    x += widget.getScrollX();
	                    y += widget.getScrollY();

	                    Layout layout = widget.getLayout();
	                    int line = layout.getLineForVertical(y);
	                    int off = layout.getOffsetForHorizontal(line, x);

	                    ClickableImageSpan[] link = stext.getSpans(off, off, ClickableImageSpan.class);

	                    if (link.length != 0) {
	                        if (action == MotionEvent.ACTION_UP) {
	                            link[0].onClick(widget);
	                            link[0].setPressed(false);
	                        }else if(action == MotionEvent.ACTION_DOWN){
	                        	link[0].setPressed(true);
	                        }
	                        widget.invalidate();
	                        ret = true;
	                    }else{
	                    	ClickableSpan[] clickLink = stext.getSpans(off, off, ClickableSpan.class);

	                        if (clickLink.length != 0) {
	                            if (action == MotionEvent.ACTION_UP) {
	                            	clickLink[0].onClick(widget);
	                            } else if (action == MotionEvent.ACTION_DOWN) {
	                                Selection.setSelection(stext,
	                                		stext.getSpanStart(clickLink[0]),
	                                		stext.getSpanEnd(clickLink[0]));
	                            }
	                            ret = true;
	                        } else {
	                            Selection.removeSelection(stext);
	                        }
	                    }
	                }
	                return ret;
	            }
	        });
		}

		
		public static class ClickableImageSpan extends ImageSpan {

			String url;

			Context mContext;

			Drawable mNormalDrawable;
			
			Drawable mPressDrawable;

			boolean mPressed;
			
			public ClickableImageSpan(Context context, String url,int normalId, int pressId) {
				super(context, normalId);
				
				mNormalDrawable = context.getResources().getDrawable(normalId);
				mPressDrawable = context.getResources().getDrawable(pressId);
				
				//注意长宽比与图片实际大小要完全一致，否则显示会变模糊
				int width = AppUtils.dip2px(context,75);
				int height = AppUtils.dip2px(context,21);
				mNormalDrawable.setBounds(0, 0, width, height);
				mPressDrawable.setBounds(0, 0, width, height);

				
				this.url = url;
			}

			public void onClick(TextView view) {
				try {
					url = url.replaceAll("^[Hh][Tt][Tt][Pp]", "http");
					url = url.replaceAll("^[Hh][Tt][Tt][Pp][Ss]", "https");
					url = url.replaceAll("^[Ff][Tt][Pp]", "ftp");
					Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(url));
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					view.getContext().startActivity(intent);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public Drawable getDrawable() {
				if(mPressed){
					return mPressDrawable;
				}else{
					return mNormalDrawable;
				}
			}

			@Override
			public void draw(Canvas canvas, CharSequence text, int start, int end,
					float x, int top, int y, int bottom, Paint paint) {
				Drawable b = getDrawable();
		        canvas.save();

		        int transY = bottom - b.getBounds().bottom;
		        if (mVerticalAlignment == ALIGN_BASELINE) {
		            transY -= paint.getFontMetricsInt().descent;
		        }

		        canvas.translate(x, transY);
		        b.draw(canvas);
		        canvas.restore();
			}
			
			public void setPressed(boolean flag){
				this.mPressed = flag;
			}
		}
	}
}
