/*package com.gecq.musicwave.player;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.gecq.musicwave.activity.MusicWaveActivity;
import com.gecq.musicwave.formats.Mp3;

import android.media.audiofx.EnvironmentalReverb;
import android.os.Message;

*//**
 * Created by chaoqing on 14-7-16.
 *//*
public class PlayerManager implements Runnable {
	private static PlayerManager pm;
	private MediaPlayer player;
	private List<Mp3> playList = new ArrayList<Mp3>();;
	private PlayMode playMode = PlayMode.SEQUENCE;
	private EnvironmentalReverb reverb;
	private int status;// 0 stop 1 play 2 pause
	private int position;
	private int playPosition;
	@SuppressWarnings("unused")
	private String currentSource;

	private Thread progress;

	private PlayerManager() {
		reverb = new EnvironmentalReverb(1, 0);
		reverb.setEnabled(true);
		player = new MediaPlayer();
	}

	public static PlayerManager getInstance() {
		if (pm == null) {
			pm = new PlayerManager();
		}
		if (pm.player == null)
			pm.player = new MediaPlayer();
		return pm;
	}

	public boolean playNew(final Mp3 mp3) {
		status = 1;
		if (!playList.contains(mp3)) {
			playList.add(mp3);
			position = playList.size();
		} else {
			position = playList.indexOf(mp3);
		}
		if(player.isPlaying())
		{
			player.stop();
		}
		boolean p = play(mp3.getFileName(), new Prepared() {
			@Override
			public void onPrepared(MediaPlayer mp) {
				Message msg = MusicWaveActivity.hand
						.obtainMessage(MusicWaveActivity.PLAY_NEW);
				msg.obj = mp3;
				System.out.println(mp.getDuration()+" =================");
				msg.arg1 = mp.getDuration();
				msg.sendToTarget();
			}
		});
		return p;
	}

	private boolean play2Pause() {
		status = 2;
		player.pause();
		playPosition = player.getCurrentPosition();
		MusicWaveActivity.hand.obtainMessage(MusicWaveActivity.PAUSE)
				.sendToTarget();
		stopProgress();
		return true;
	}

	private boolean pause2Play() {
		status = 1;
		startProgress();
		player.start();
		player.seekTo(playPosition);
		Message msg = MusicWaveActivity.hand
				.obtainMessage(MusicWaveActivity.PAUSE_PLAY);
		msg.sendToTarget();
		return true;
	}

	public boolean play(Mp3 mp3) {
		return play(mp3.getFileName(), null);
	}

	public void play() {
		if (status == 2) {
			pause2Play();
		} else if (status == 1) {
			play2Pause();
		} else {
			playNext_();
		}
	}

	private void startProgress() {
		stopProgress();
		progress = new Thread(this);
		progress.start();
	}

	private void stopProgress() {
		if (progress != null) {
			try {
				progress.interrupt();
				progress.join();
			} catch (Exception e) {
				e.printStackTrace();
			}
			progress = null;
		}
	}

	public boolean play(String fileName, final Prepared pre) {
		try {
			currentSource = fileName;
			player.setAudioStreamType(AudioManager.STREAM_MUSIC);
			player.reset();
			player.setDataSource(fileName);
			player.prepareAsync();
			player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mediaPlayer) {
					Message msg = MusicWaveActivity.hand
							.obtainMessage(MusicWaveActivity.PLAY_COMPLETE);
					msg.sendToTarget();
					playNext();
				}
			});
			player.setOnErrorListener(new OnErrorListener() {
				@Override
				public boolean onError(MediaPlayer mp, int what, int extra) {
					System.out.println("what:"+what+" extra:"+extra+" !!!!!!!!!!!!!!!!!!");
					return false;
				}
			});
			player.setOnPreparedListener(new OnPreparedListener() {
				@Override
				public void onPrepared(MediaPlayer mp) {
					mp.start();
					startProgress();
					if (pre != null)
					{
					   pre.onPrepared(mp);
					}
				}
			});
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	interface Prepared {
		void onPrepared(MediaPlayer mp);
	}

	public void playNext() {
		position++;
		playNext_();
	}

	private void playNext_() {
		if (playMode == PlayMode.RANDOM) {
			int max = playList.size() - 1;
			Random random = new Random();
			int s = random.nextInt(max) % (max + 1);
			play(playList.get(s));
		} else if (playMode == PlayMode.CYCLE_SEQUENCE) {
			if (position >= playList.size()) {
				position = 0;
			}
			play(playList.get(position));
		} else if (playMode == PlayMode.SEQUENCE) {
			if (position >= playList.size()) {
				playStop();
			} else {
				play(playList.get(position));
			}
		} else if (playMode == PlayMode.CYCLE_SINGLE) {
			position--;
			play(playList.get(position));
		}
	}

	private void playStop() {
		status = 0;
		player.stop();
		player.reset();
		player.release();
//		player = null;
	}

	@Override
	public void run() {
		int total = player.getDuration();
		while (status == 1 && player != null && playPosition <= total) {
			try {
				Thread.sleep(100);
				playPosition = player.getCurrentPosition();
			} catch (InterruptedException e) {
				return;
			} catch (Exception e) {
				return;
			}
			Message msg = MusicWaveActivity.hand
					.obtainMessage(MusicWaveActivity.UPDATE_PROGRESS);
			msg.arg1 = playPosition;
			msg.sendToTarget();
		}
		if (progress != null) {
			progress.interrupt();
			progress = null;
		}
	}

	public void setEffect(EnvironmentalReverb.Settings settings) {
		reverb.setProperties(settings);
		reverb.setEnabled(true);
		player.attachAuxEffect(reverb.getId());
		player.setAuxEffectSendLevel(1.0f);
	}

	public MediaPlayer getPlayer() {
		if (player == null) {
			player = new MediaPlayer();
		}
		return player;
	}
}
*/