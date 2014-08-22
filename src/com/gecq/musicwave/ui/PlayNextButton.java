package com.gecq.musicwave.ui;

import com.gecq.musicwave.R;
import com.gecq.musicwave.utils.CommonUtils;
import com.gecq.musicwave.utils.MusicUtils;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;

public class PlayNextButton extends Button implements OnClickListener, OnLongClickListener {


	public PlayNextButton(Context context, AttributeSet attrs) {
		super(context, attrs);
        setOnClickListener(this);
        setOnLongClickListener(this);
        setContentDescription(getResources().getString(R.string.accessibility_next));
	}

	@Override
	public boolean onLongClick(View v) {
		if (TextUtils.isEmpty(v.getContentDescription())) {
            return false;
        } else {
            CommonUtils.showCheatSheet(v);
            return true;
        }
	}

	@Override
	public void onClick(View v) {
		MusicUtils.next();
	}
	

}
