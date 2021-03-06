/*
 * Copyright (C) 2012 Andrew Neal Licensed under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package com.gecq.musicwave.ui;

import com.gecq.musicwave.R;
import com.gecq.musicwave.activity.MusicWaveActivity;
import com.gecq.musicwave.utils.CommonUtils;
import com.gecq.musicwave.utils.MusicUtils;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;


/**
 * A custom {@link ImageButton} that represents the "play and pause" button.
 * 
 * @author Andrew Neal (andrewdneal@gmail.com)
 */
public class PlayPauseButton extends Button implements OnClickListener, OnLongClickListener {

	
    public PlayPauseButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setTypeface(MusicWaveActivity.icon);
	}

	public PlayPauseButton(Context context) {
		super(context);
		setTypeface(MusicWaveActivity.icon);
	}

	/**
     * @param context The {@link Context} to use
     * @param attrs The attributes of the XML tag that is inflating the view.
     */
    public PlayPauseButton(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        // Control playback (play/pause)
        setOnClickListener(this);
        // Show the cheat sheet
        setOnLongClickListener(this);
        setTypeface(MusicWaveActivity.icon);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onClick(final View v) {
        MusicUtils.playOrPause();
        updateState();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onLongClick(final View view) {
        if (TextUtils.isEmpty(view.getContentDescription())) {
            return false;
        } else {
            CommonUtils.showCheatSheet(view);
            return true;
        }
    }

    /**
     * Sets the correct drawable for playback.
     */
    public void updateState() {
        if (MusicUtils.isPlaying()) {
            setContentDescription(getResources().getString(R.string.accessibility_pause));
            setText(R.string.icon_pause);
        } else {
            setContentDescription(getResources().getString(R.string.accessibility_play));
            setText(R.string.icon_play);
        }
    }

}
