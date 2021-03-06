package com.gecq.musicwave.activity;

import static com.gecq.musicwave.utils.MusicUtils.mService;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import com.gecq.musicwave.R;
import com.gecq.musicwave.frames.HomeFragment;
import com.gecq.musicwave.player.IMusicWaveService;
import com.gecq.musicwave.player.PlayerService;
import com.gecq.musicwave.ui.PlayNextButton;
import com.gecq.musicwave.ui.PlayPauseButton;
import com.gecq.musicwave.ui.RepeatButton;
import com.gecq.musicwave.utils.ArtWork;
import com.gecq.musicwave.utils.Lists;
import com.gecq.musicwave.utils.MusicUtils;
import com.gecq.musicwave.utils.MusicUtils.ServiceToken;

import android.media.AudioManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.widget.*;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Window;

public class MusicWaveActivity extends FragmentActivity implements
		ServiceConnection {
	public static Typeface icon;
	// public static Handler hand;
	public static final int PLAY_NEW = 1;
	public static final int PAUSE = 0;
	public static final int PAUSE_PLAY = 2;
	public static final int UPDATE_PROGRESS = 3;
	public static final int PLAY_COMPLETE = 4;
	public static final int UPDATE_ALBUM = 5;
	public static final int SET_PROGRESS_MAX = 6;
	private ProgressBar pbar;
	private HomeFragment home;
	public static int currentFrame;

	private void readFont(Context context) {
		if (icon == null) {
			try {
				icon = Typeface.createFromAsset(context.getAssets(),
						"gePlayer.ttf");
			} catch (Exception e) {
				icon = Typeface.DEFAULT;
			}
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.home);
		readFont(MusicWaveActivity.this);

		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		mToken = MusicUtils.bindToService(this, this);
		// Initialize the broadcast receiver
		mPlaybackStatus = new PlaybackStatus(this);
		mTimeHandler = new TimeHandler(this);
		initViews();
	}

	private void setHomeFrame() {
		home = new HomeFragment();
		currentFrame = 0;
		final FragmentManager fragmentManager = getSupportFragmentManager();
		final FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.add(R.id.home_content, home);
		fragmentTransaction.commit();
	}

	private void initViews() {
		mPlayPauseButton = (PlayPauseButton) findViewById(R.id.home_play);
		mPlayPauseButton.setTypeface(icon);
		next = (PlayNextButton) findViewById(R.id.home_play_next);
		next.setTypeface(icon);
		mTrackName = (TextView) findViewById(R.id.home_song_name);
		mArtistName = (TextView) findViewById(R.id.home_song_singer);
		pbar = (ProgressBar) findViewById(R.id.home_play_progress);
		mAlbumArt = (ImageView) findViewById(R.id.home_album);
		mRepeatButton = (RepeatButton) findViewById(R.id.home_play_repeat);
		mRepeatButton.setTypeface(icon);
		setHomeFrame();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if (currentFrame != 0) {
				final FragmentManager fragmentManager = getSupportFragmentManager();
				final FragmentTransaction transaction = fragmentManager
						.beginTransaction();
				transaction.setCustomAnimations(R.animator.side_in_left,
						R.animator.side_out_right);
				transaction.replace(R.id.home_content, home);
				currentFrame = 0;
				transaction.commit();
			} else {
				onBackPressed();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onBackPressed() {
		if (MusicUtils.isPlaying()) {
			moveTaskToBack(false);
		} else {
			finish();
		}
	}

	/**
	 * Playstate and meta change listener
	 */
	private final ArrayList<MusicStateListener> mMusicStateListener = Lists
			.newArrayList();

	/**
	 * The service token
	 */
	private ServiceToken mToken;

	private PlayNextButton next;

	/**
	 * Play and pause button (BAB)
	 */
	private PlayPauseButton mPlayPauseButton;

	/**
	 * Repeat button (BAB)
	 */
	private RepeatButton mRepeatButton;

	/**
	 * Shuffle button (BAB) 随机播放按钮
	 */
	// private ShuffleButton mShuffleButton;

	/**
	 * Track name (BAB)
	 */
	private TextView mTrackName;

	/**
	 * Artist name (BAB)
	 */
	private TextView mArtistName;

	/**
	 * Album art (BAB)
	 */
	private ImageView mAlbumArt;

	/**
	 * Broadcast receiver
	 */
	private PlaybackStatus mPlaybackStatus;

	/**
	 * Used to monitor the state of playback
	 */
	private final static class PlaybackStatus extends BroadcastReceiver {

		private final WeakReference<MusicWaveActivity> mReference;

		/**
		 * Constructor of <code>PlaybackStatus</code>
		 */
		public PlaybackStatus(final MusicWaveActivity activity) {
			mReference = new WeakReference<MusicWaveActivity>(activity);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void onReceive(final Context context, final Intent intent) {
			final String action = intent.getAction();
			if (action.equals(PlayerService.META_CHANGED)) {
				// Current info
				mReference.get().updateBottomActionBarInfo();
				// Update the favorites icon
				// mReference.get().invalidateOptionsMenu();
				// Let the listener know to the meta chnaged
				for (final MusicStateListener listener : mReference.get().mMusicStateListener) {
					if (listener != null) {
						listener.onMetaChanged();
					}
				}
//				int max = (int) intent.getLongExtra(
//						PlayerService.UPDATE_PROGRESS_MAX, 0);
//				mReference.get().pbar.setMax(max);
			} else if (action.equals(PlayerService.PLAYSTATE_CHANGED)) {
				// Set the play and pause image
				mReference.get().mPlayPauseButton.updateState();
			} else if (action.equals(PlayerService.REPEATMODE_CHANGED)
					|| action.equals(PlayerService.SHUFFLEMODE_CHANGED)) {
				// Set the repeat image
				mReference.get().mRepeatButton.updateRepeatState();

				// Set the shuffle image
				// mReference.get().mShuffleButton.updateShuffleState();
			} else if (action.equals(PlayerService.REFRESH)) {
				// Let the listener know to update a list
				for (final MusicStateListener listener : mReference.get().mMusicStateListener) {
					if (listener != null) {
						listener.restartLoader();
					}
				}
			} 
//			else if (action.equals(PlayerService.UPDATE_PROGRESS)) {
//				int pos = intent.getIntExtra(PlayerService.UPDATE_PROGRESS_POS,
//						0);
//				mReference.get().pbar.setProgress(pos);
//			}
		}
	}

	/**
	 * Sets the track name, album name, and album art.
	 */
	private void updateBottomActionBarInfo() {
		// Set the track name
		String tn = MusicUtils.getTrackName();
		if (tn != null) {
			mTrackName.setText(tn);
		} else {
			mTrackName.setText(R.string.app_string);
		}
		// Set the artist name
		String an = MusicUtils.getArtistName();
		if (an != null) {
			mArtistName.setText(an);
		} else {
			mArtistName.setText(R.string.app_name);
		}
		// Set the album art
		Bitmap art = ArtWork.getArtwork(MusicWaveActivity.this,
				MusicUtils.getCurrentAudioId(), MusicUtils.getCurrentAlbumId(),
				true);
		if (art != null) {
			mAlbumArt.setImageBitmap(art);
		} else {
			mAlbumArt.setImageResource(R.drawable.home_play_bar_default_album);
		}
	}

	/**
	 * Sets the correct drawable states for the playback controls.
	 */
	private void updatePlaybackControls() {
		// Set the play and pause image
		mPlayPauseButton.updateState();
		// Set the shuffle image
		// mShuffleButton.updateShuffleState();
		// Set the repeat image
		mRepeatButton.updateRepeatState();
	}

	/**
	 * {@inheritDoc}
	 */
	// @SuppressLint("NewApi")
	@Override
	public void onServiceConnected(final ComponentName name,
			final IBinder service) {
		mService = IMusicWaveService.Stub.asInterface(service);
		updatePlaybackControls();
		// Current info
		updateBottomActionBarInfo();
		// Update the favorites icon
		// invalidateOptionsMenu();
		queueNextRefresh(1);
	}

	@Override
	public void onServiceDisconnected(final ComponentName name) {
		mService = null;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// Unbind from the service
		if (mService != null) {
			MusicUtils.unbindFromService(mToken);
			mToken = null;
		}

		// Unregister the receiver
		try {
			unregisterReceiver(mPlaybackStatus);
		} catch (final Throwable e) {
			//$FALL-THROUGH$
		}

		// Remove any music status listeners
		mMusicStateListener.clear();
		mIsPaused = false;
        mTimeHandler.removeMessages(REFRESH_TIME);
	}

	@Override
	protected void onStop() {
		super.onStop();
		MusicUtils.notifyForegroundStateChanged(this, false);
	}

	@Override
	protected void onStart() {
		super.onStart();
		final IntentFilter filter = new IntentFilter();
		// Play and pause changes
		filter.addAction(PlayerService.PLAYSTATE_CHANGED);
		// Shuffle and repeat changes
		filter.addAction(PlayerService.SHUFFLEMODE_CHANGED);
		filter.addAction(PlayerService.REPEATMODE_CHANGED);
		// Track changes
		filter.addAction(PlayerService.META_CHANGED);
		// Update a list, probably the playlist fragment's
		filter.addAction(PlayerService.REFRESH);
		filter.addAction(PlayerService.UPDATE_PROGRESS);
		registerReceiver(mPlaybackStatus, filter);
		MusicUtils.notifyForegroundStateChanged(this, true);
	}

	@Override
	protected void onResume() {
		super.onResume();
		updatePlaybackControls();
		// Current info
		updateBottomActionBarInfo();
		queueNextRefresh(1);
	}

	private static final int REFRESH_TIME = 1;
	private long mPosOverride = -1;
	// Handler used to update the current time
	private TimeHandler mTimeHandler;
	private boolean mIsPaused = false;

	private long refreshCurrentTime() {
		if (mService == null) {
			return 500;
		}
		try {
			final long pos = mPosOverride < 0 ? MusicUtils.position()
					: mPosOverride;
			if (pos >= 0 && MusicUtils.duration() > 0) {
				// refreshCurrentTimeText(pos);
				final int progress = (int) (1000 * pos / MusicUtils.duration());
				pbar.setProgress(progress);
			} else {
				pbar.setProgress(1000);
			}
			// calculate the number of milliseconds until the next full second,
			// so
			// the counter can be updated at just the right time
			final long remaining = 1000 - pos % 1000;
			// approximate how often we would need to refresh the slider to
			// move it smoothly
			int width = pbar.getWidth();
			if (width == 0) {
				width = 320;
			}
			final long smoothrefreshtime = MusicUtils.duration() / width;
			if (smoothrefreshtime > remaining) {
				return remaining;
			}
			if (smoothrefreshtime < 20) {
				return 20;
			}
			return smoothrefreshtime;
		} catch (final Exception ignored) {

		}
		return 500;
	}

	/**
	 * @param delay
	 *            When to update
	 */
	private void queueNextRefresh(final long delay) {
		if (!mIsPaused) {
			final Message message = mTimeHandler.obtainMessage(REFRESH_TIME);
			mTimeHandler.removeMessages(REFRESH_TIME);
			mTimeHandler.sendMessageDelayed(message, delay);
		}
	}

	/**
	 * Used to update the current time string
	 */
	private static final class TimeHandler extends Handler {

		private final WeakReference<MusicWaveActivity> mAudioPlayer;

		/**
		 * Constructor of <code>TimeHandler</code>
		 */
		public TimeHandler(final MusicWaveActivity player) {
			mAudioPlayer = new WeakReference<MusicWaveActivity>(player);
		}

		@Override
		public void handleMessage(final Message msg) {
			switch (msg.what) {
			case REFRESH_TIME:
				final long next = mAudioPlayer.get().refreshCurrentTime();
				mAudioPlayer.get().queueNextRefresh(next);
				break;
			default:
				break;
			}
		}
	};
	
	 /**
     * @param status The {@link MusicStateListener} to use
     */
    public void setMusicStateListenerListener(final MusicStateListener status) {
        if (status != null) {
            mMusicStateListener.add(status);
        }
    }

}
