package com.gecq.musicwave.player;

import android.content.Intent;

import com.gecq.musicwave.utils.MusicUtils;

public class ProgressBarThread extends Thread {
	private long duration;
	private PlayerService ps;
	public ProgressBarThread(long dur,PlayerService ps) {
		this.duration=dur;
		this.ps=ps;
	}
	@Override
	public void run() {
		if(duration<=0){
			return;
		}
		
		long position=MusicUtils.position();
		while(MusicUtils.isPlaying()&&position<=duration){
			try {
				Thread.sleep(100);
				position =MusicUtils.position();
			} catch (Exception e) {
				return;
			}
//			final Intent intent = new Intent(PlayerService.UPDATE_PROGRESS);
//			intent.putExtra(PlayerService.UPDATE_PROGRESS_POS, position);
//			ps.sendStickyBroadcast(intent);
		}
		interrupt();
	}

}
