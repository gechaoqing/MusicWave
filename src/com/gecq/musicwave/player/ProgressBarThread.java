package com.gecq.musicwave.player;

import android.os.Message;

import com.gecq.musicwave.activity.MusicWaveActivity;
import com.gecq.musicwave.utils.MusicUtils;

public class ProgressBarThread extends Thread {
	@Override
	public void run() {
		long duration=MusicUtils.duration();
		if(duration<=0){
			return;
		}
		Message msg=MusicWaveActivity.hand.obtainMessage(MusicWaveActivity.SET_PROGRESS_MAX);
		msg.arg1=(int) duration;
		msg.sendToTarget();
		long position=MusicUtils.position();
		while(MusicUtils.isPlaying()&&position<=duration){
			try {
				Thread.sleep(100);
				position =MusicUtils.position();
			} catch (Exception e) {
				return;
			}
			msg=MusicWaveActivity.hand.obtainMessage(MusicWaveActivity.UPDATE_PROGRESS);
			msg.arg1=(int) position;
			msg.sendToTarget();
		}
	}

}
