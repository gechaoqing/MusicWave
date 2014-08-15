package com.gecq.musicwave.activity;

import static com.gecq.musicwave.utils.MusicUtils.mService;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import com.gecq.musicwave.R;
import com.gecq.musicwave.database.DBManager;
import com.gecq.musicwave.formats.Mp3;
import com.gecq.musicwave.frames.HomeFragment;
import com.gecq.musicwave.player.IMusicWaveService;
import com.gecq.musicwave.player.PlayerManager;
import com.gecq.musicwave.player.PlayerService;
import com.gecq.musicwave.ui.PlayNextButton;
import com.gecq.musicwave.ui.PlayPauseButton;
import com.gecq.musicwave.ui.RepeatButton;
import com.gecq.musicwave.ui.ShuffleButton;
import com.gecq.musicwave.utils.ArtWork;
import com.gecq.musicwave.utils.Lists;
import com.gecq.musicwave.utils.MusicUtils;
import com.gecq.musicwave.utils.MusicUtils.ServiceToken;

import android.media.AudioManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.widget.*;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Window;

public class MusicWaveActivity extends FragmentActivity  implements ServiceConnection{
	public static Typeface icon;
    public static Handler hand;
    public static final int PLAY_NEW=1;
    public static final int PAUSE=0;
    public static final int PAUSE_PLAY=2;
    public static final int UPDATE_PROGRESS=3;
    public static final int PLAY_COMPLETE=4;
    public static final int UPDATE_ALBUM=5;
    public static final int SET_PROGRESS_MAX=6;
    private ProgressBar pbar;
    private HomeFragment home;
    public static int currentFrame;
    private void readFont(Context context)
	{
		if(icon == null){
			try {
			icon = Typeface.createFromAsset(context.getAssets(), "gePlayer.ttf");
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
        
		initViews();
        newHandler();
    }

    private void setHomeFrame(){
        home=new HomeFragment();
        currentFrame=0;
        final FragmentManager fragmentManager = getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.home_content, home);
        fragmentTransaction.commit();
    }
	
	private void initViews(){
		mPlayPauseButton=(PlayPauseButton)findViewById(R.id.home_play);
		next=(PlayNextButton)findViewById(R.id.home_play_next);
		next.setTypeface(icon);
		mTrackName=(TextView)findViewById(R.id.home_song_name);
		mArtistName=(TextView)findViewById(R.id.home_song_singer);
        pbar=(ProgressBar)findViewById(R.id.home_play_progress);
        mAlbumArt=(ImageView)findViewById(R.id.home_album);
        setHomeFrame();
	}

    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if(keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0){
    		if(currentFrame!=0){
    			 final FragmentManager fragmentManager =getSupportFragmentManager();
                 final FragmentTransaction transaction = fragmentManager.beginTransaction();
                 transaction.setCustomAnimations(R.animator.side_in_left,R.animator.side_out_right);
                 transaction.replace(R.id.home_content,home);
                 currentFrame=0;
                 transaction.commit();
    		}else{
    			finish();
    		}
    	}
		return false;
	}
    
    /**
     * Playstate and meta change listener
     */
    private final ArrayList<MusicStateListener> mMusicStateListener = Lists.newArrayList();

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
     * Shuffle button (BAB)
     */
    private ShuffleButton mShuffleButton;

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
        @SuppressLint("NewApi")
		@Override
        public void onReceive(final Context context, final Intent intent) {
            final String action = intent.getAction();
            if (action.equals(PlayerService.META_CHANGED)) {
                // Current info
                mReference.get().updateBottomActionBarInfo();
                // Update the favorites icon
                mReference.get().invalidateOptionsMenu();
                // Let the listener know to the meta chnaged
                for (final MusicStateListener listener : mReference.get().mMusicStateListener) {
                    if (listener != null) {
                        listener.onMetaChanged();
                    }
                }
            } else if (action.equals(PlayerService.PLAYSTATE_CHANGED)) {
                // Set the play and pause image
                mReference.get().mPlayPauseButton.updateState();
            } else if (action.equals(PlayerService.REPEATMODE_CHANGED)
                    || action.equals(PlayerService.SHUFFLEMODE_CHANGED)) {
                // Set the repeat image
                mReference.get().mRepeatButton.updateRepeatState();
                // Set the shuffle image
                mReference.get().mShuffleButton.updateShuffleState();
            } else if (action.equals(PlayerService.REFRESH)) {
                // Let the listener know to update a list
                for (final MusicStateListener listener : mReference.get().mMusicStateListener) {
                    if (listener != null) {
                        listener.restartLoader();
                    }
                }
            }
        }
    }
    
    /**
     * Sets the track name, album name, and album art.
     */
    private void updateBottomActionBarInfo() {
        // Set the track name
        mTrackName.setText(MusicUtils.getTrackName());
        // Set the artist name
        mArtistName.setText(MusicUtils.getArtistName());
        // Set the album art
        mAlbumArt.setImageBitmap(ArtWork.getArtwork(this, MusicUtils.getCurrentAudioId(), MusicUtils.getCurrentAlbumId(), true));
    }

    /**
     * Sets the correct drawable states for the playback controls.
     */
    private void updatePlaybackControls() {
        // Set the play and pause image
        mPlayPauseButton.updateState();
        // Set the shuffle image
        mShuffleButton.updateShuffleState();
        // Set the repeat image
        mRepeatButton.updateRepeatState();
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressLint("NewApi")
	@Override
    public void onServiceConnected(final ComponentName name, final IBinder service) {
        mService = IMusicWaveService.Stub.asInterface(service);
        updatePlaybackControls();
        // Current info
        updateBottomActionBarInfo();
        // Update the favorites icon
        invalidateOptionsMenu();
    }
    
    @Override
    public void onServiceDisconnected(final ComponentName name) {
        mService = null;
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unbind from the service
        if (mToken != null) {
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
    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
        registerReceiver(mPlaybackStatus, filter);
        MusicUtils.notifyForegroundStateChanged(this, true);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        updatePlaybackControls();
        // Current info
        updateBottomActionBarInfo();
    }

    
    
	private void newHandler(){
        hand=new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case UPDATE_PROGRESS:
                        pbar.setProgress(msg.arg1);
                        break;
                    case SET_PROGRESS_MAX:
                    	pbar.setMax(msg.arg1);
                    	break;
                }
            }
        };
    }

}
