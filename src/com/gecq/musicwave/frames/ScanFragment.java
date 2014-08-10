package com.gecq.musicwave.frames;

import com.gecq.musicwave.R;
import com.gecq.musicwave.activity.MusicWaveActivity;
import com.gecq.musicwave.models.HomeGridItem;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class ScanFragment extends MusicWaveFragment {
    public ScanFragment(HomeFragment home){
        super(home);
    }
    @SuppressLint("InflateParams")
	@Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View all= inflater.inflate(R.layout.scan_frame,null,false);
        Button backHome=(Button)all.findViewById(R.id.scan_back);
        backHome.setTypeface(MusicWaveActivity.icon);
        backHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                final FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(R.animator.side_in_left,R.animator.side_out_right);
                transaction.replace(R.id.home_content,home);
                MusicWaveActivity.currentFrame=0;
                transaction.commit();
            }
        });
        return all;
    }
	@Override
	public int getType() {
		return HomeGridItem.HOME_MENU_SCAN;
	}
    
}
