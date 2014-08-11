package com.gecq.musicwave.frames;

import com.gecq.musicwave.R;
import com.gecq.musicwave.activity.MusicWaveActivity;
import com.gecq.musicwave.models.HomeGridItem;
import com.gecq.musicwave.utils.MediaScanner;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ScanFragment extends MusicWaveFragment {
	private MediaScanner scanner;
	private Handler handl;

	public ScanFragment(HomeFragment home) {
		super(home);
	}

	@SuppressLint({ "InflateParams"})
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		final View all = inflater.inflate(R.layout.scan_frame, null, false);
		final Button backHome = (Button) all.findViewById(R.id.scan_back);
		final Animation show = AnimationUtils.loadAnimation(getActivity(),
				R.animator.view_show);
		final Animation hiden = AnimationUtils.loadAnimation(getActivity(),
				R.animator.view_hiden);
		backHome.setTypeface(MusicWaveActivity.icon);
		backHome.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				final FragmentManager fragmentManager = getActivity()
						.getSupportFragmentManager();
				final FragmentTransaction transaction = fragmentManager
						.beginTransaction();
				transaction.setCustomAnimations(R.animator.side_in_left,
						R.animator.side_out_right);
				transaction.replace(R.id.home_content, home);
				MusicWaveActivity.currentFrame = 0;
				transaction.commit();
			}
		});
		final LinearLayout scanStatus=(LinearLayout)all.findViewById(R.id.scan_current);
		final TextView tv = (TextView) scanStatus.findViewById(R.id.scan_current_path);
		final TextView size=(TextView)scanStatus.findViewById(R.id.scan_current_size);
		size.setText("0");
		handl = new Handler(Looper.myLooper()) {

			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					final FragmentManager fragmentManager = getActivity()
							.getSupportFragmentManager();
					final FragmentTransaction transaction = fragmentManager
							.beginTransaction();
					transaction.setCustomAnimations(R.animator.side_in_left,
							R.animator.side_out_right);
					AllMusicFragment all=(AllMusicFragment) home.getItems().get(0).getTo();
					transaction.replace(R.id.home_content, all);
					MusicWaveActivity.currentFrame = HomeGridItem.HOME_MENU_ALL;
					transaction.commit();
					break;
				case 2:
					size.setText(String.valueOf(msg.arg1));
					break;
				default:
					tv.setText((String) msg.obj);
					break;
				}

			}
		};
		final Button scan = (Button) all.findViewById(R.id.scan_start);
		scan.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				scan.startAnimation(hiden);
				hiden.setAnimationListener(new AnimationListener() {
					@Override
					public void onAnimationStart(Animation animation) {
					}
					@Override
					public void onAnimationRepeat(Animation animation) {
					}
					@Override
					public void onAnimationEnd(Animation animation) {
						scanStatus.setVisibility(View.VISIBLE);
						scanStatus.startAnimation(show);
						show.setAnimationListener(showLis);
						scan.setVisibility(View.GONE);
					}
				});
				
			}
		});
		return all;
	}
	
	private AnimationListener showLis=new AnimationListener() {
		@Override
		public void onAnimationStart(Animation animation) {
		}
		@Override
		public void onAnimationRepeat(Animation animation) {
		}
		@Override
		public void onAnimationEnd(Animation animation) {
			scanner = new MediaScanner(Environment
					.getExternalStorageDirectory(), getActivity(), handl);
			scanner.start();
		}
	};

	@Override
	public int getType() {
		return HomeGridItem.HOME_MENU_SCAN;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (scanner != null) {
			scanner.stop();
			scanner = null;
		}
	}
}
