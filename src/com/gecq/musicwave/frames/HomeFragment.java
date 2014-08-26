package com.gecq.musicwave.frames;

import java.util.ArrayList;
import java.util.List;

import com.gecq.musicwave.R;
import com.gecq.musicwave.adapters.HomeItemGridAdapter;
import com.gecq.musicwave.models.HomeGridItem;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

/**
 * Created by chaoqing on 14-8-9.
 */
public class HomeFragment extends Fragment {
	private List<HomeGridItem> items;
    @SuppressLint("InflateParams")
	@Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View home=inflater.inflate(R.layout.home_frame,null,false);
        GridView gv = (GridView)home.findViewById(R.id.home_grid_menu);
        HomeItemGridAdapter adap = new HomeItemGridAdapter(HomeFragment.this);
        gv.setAdapter(adap);
        gv.setOnItemClickListener(adap);
        return home;
    }
    
    public List<HomeGridItem> getItems() {
		if (items == null) {
			items = new ArrayList<HomeGridItem>();
			items.add(new HomeGridItem(getString(R.string.all_songs), getString(R.string.icon_all_songs), new AllMusicFragment(this)));
			items.add(new HomeGridItem(getString(R.string.singers), getString(R.string.icon_singers), new SingerFragment(this)));
			items.add(new HomeGridItem(getString(R.string.albums), getString(R.string.icon_albums), new AlbumsFragment(this)));
			items.add(new HomeGridItem(getString(R.string.favourite_str), getString(R.string.icon_favourite), null));
			items.add(new HomeGridItem(getString(R.string.recent_listen), getString(R.string.icon_recent_listen), null));
			items.add(new HomeGridItem(getString(R.string.play_list_mine), getString(R.string.icon_play_list_mine), null));
			items.add(new HomeGridItem(getString(R.string.music_effect), getString(R.string.icon_music_effect), null));
			items.add(new HomeGridItem(getString(R.string.scan_songs), getString(R.string.icon_scan_songs), new ScanFragment(this)));
			items.add(new HomeGridItem(getString(R.string.setting), getString(R.string.icon_setting), null));
			/*items.add(new HomeGridItem(getString(R.string.add_play_list), getString(R.string.icon_add_play_list), null));*/
			
		}
		return items;
	}
}
