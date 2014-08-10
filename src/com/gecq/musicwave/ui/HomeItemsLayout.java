package com.gecq.musicwave.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class HomeItemsLayout extends RelativeLayout {
	public HomeItemsLayout(Context context) {
		super(context);
	}

	public HomeItemsLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public HomeItemsLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(getDefaultSize(0, widthMeasureSpec),
				getDefaultSize(0, heightMeasureSpec));
		// Children are just made to fill our space.
		int childWidthSize = getMeasuredWidth();
		// 高度和宽度一样
		heightMeasureSpec = widthMeasureSpec = MeasureSpec.makeMeasureSpec(
				childWidthSize, MeasureSpec.EXACTLY);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

}
