package com.gecq.musicwave.frames;

import com.gecq.musicwave.R;
import com.gecq.musicwave.activity.MusicWaveActivity;
import com.gecq.musicwave.adapters.HomeItemGridAdapter;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by chaoqing on 14-8-9.
 */
public class HomeFragment extends Fragment {
    @SuppressLint("InflateParams")
	@Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View home=inflater.inflate(R.layout.home_frame,null,false);
        GridView gv = (GridView)home.findViewById(R.id.home_grid_menu);
        HomeItemGridAdapter adap = new HomeItemGridAdapter(HomeFragment.this);
        gv.setAdapter(adap);
        gv.setOnItemClickListener(adap);
        LinearLayout addPlaylist=(LinearLayout)home.findViewById(R.id.home_add_play_list);
        TextView addIcon=(TextView) addPlaylist.findViewById(R.id.home_add_play_list_icon);
        addIcon.setTypeface(MusicWaveActivity.icon);
        TextView favourite=(TextView)home.findViewById(R.id.home_favourite_icon);
        favourite.setTypeface(MusicWaveActivity.icon);
        return home;
    }
    
    
}
