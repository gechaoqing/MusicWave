package com.gecq.musicwave.frames;

import android.support.v4.app.Fragment;

public abstract class MusicWaveFragment extends Fragment {
	HomeFragment home;
	public abstract int getType();
	public MusicWaveFragment(HomeFragment home){
		this.home=home;
	}
}
