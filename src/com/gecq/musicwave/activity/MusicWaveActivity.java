package com.gecq.musicwave.activity;

import com.gecq.musicwave.R;
import com.gecq.musicwave.database.DBManager;
import com.gecq.musicwave.formats.Mp3;
import com.gecq.musicwave.frames.HomeFragment;
import com.gecq.musicwave.player.PlayerManager;
import com.gecq.musicwave.utils.ArtWork;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.*;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Window;

public class MusicWaveActivity extends FragmentActivity {
	public static Typeface icon;
	private Button play,next;
    private TextView psong, psinger;
    public static Handler hand;
    public static DBManager dbm;
    public static final int PLAY_NEW=1;
    public static final int PAUSE=0;
    public static final int PAUSE_PLAY=2;
    public static final int UPDATE_PROGRESS=3;
    public static final int PLAY_COMPLETE=4;
    public static final int UPDATE_ALBUM=5;
    private ImageView album;
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
		play=(Button)findViewById(R.id.home_play);
		play.setTypeface(icon);
		next=(Button)findViewById(R.id.home_play_next);
		next.setTypeface(icon);
        psong=(TextView)findViewById(R.id.home_song_name);
        psinger=(TextView)findViewById(R.id.home_song_singer);
        pbar=(ProgressBar)findViewById(R.id.home_play_progress);
        album=(ImageView)findViewById(R.id.home_album);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlayerManager.getInstance().play();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlayerManager.getInstance().playNext();
            }
        });
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
    
    
	private void newHandler(){
        hand=new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case PAUSE:
                        play.setText(R.string.icon_play);
                        break;
                    case PAUSE_PLAY:
                        play.setText(R.string.icon_pause);
                        break;
                    case PLAY_NEW:
                        Mp3 mp3=(Mp3)msg.obj;
                        play.setText(R.string.icon_pause);
                        psong.setText(mp3.getName());
                        psinger.setText(mp3.getArtist());
                        pbar.setMax(msg.arg1);
                        pbar.setProgress(0);
                        if(mp3.getAlbumArt()==null)
                        {
                            new ArtWork.AsyncWork(MusicWaveActivity.this,mp3).execute(0);
                        }else{
                            album.setImageBitmap(mp3.getAlbumArt());
                        }
                        break;
                    case UPDATE_PROGRESS:
                        pbar.setProgress(msg.arg1);
                        break;
                    case PLAY_COMPLETE:
                        play.setText(R.string.icon_play);
                        psong.setText("我的音乐");
                        psinger.setText("乐浪");
                        pbar.setMax(100);
                        pbar.setProgress(0);
                        break;
                    case UPDATE_ALBUM:
                    	Bitmap data=(Bitmap)msg.obj;
                        if(data!=null){
                            album.setImageBitmap(data);
                        }else{
                        	album.setImageResource(R.drawable.home_play_bar_default_album);
                        }
                        break;
                }
            }
        };
    }

}
