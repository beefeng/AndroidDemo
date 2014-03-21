package com.example.android.apis.beefeng.widget;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.CheckBox;

/**
 * 修正Checkbox在<4.2和4.2以上版本显示不一致问题
 * @author liuqingfeng
 *
 */
public class CheckBoxWithPaddingFix extends CheckBox{
	public CheckBoxWithPaddingFix(Context context) {
        super(context);
    }

    public CheckBoxWithPaddingFix(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CheckBoxWithPaddingFix(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getCompoundPaddingLeft() {
        final float scale = this.getResources().getDisplayMetrics().density;
        if(Build.VERSION.SDK_INT >= 17){
        	return (super.getCompoundPaddingLeft());
        }else{
        	return (super.getCompoundPaddingLeft() + (int) (20.0f * scale + 0.5f));
        }
    }
}
