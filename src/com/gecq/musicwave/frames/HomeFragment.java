package com.gecq.musicwave.frames;

import java.util.ArrayList;
import java.util.List;

import com.gecq.musicwave.R;
import com.gecq.musicwave.activity.MusicWaveActivity;
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
import android.widget.LinearLayout;
import android.widget.TextView;

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
        LinearLayout addPlaylist=(LinearLayout)home.findViewById(R.id.home_add_play_list);
        TextView addIcon=(TextView) addPlaylist.findViewById(R.id.home_add_play_list_icon);
        addIcon.setTypeface(MusicWaveActivity.icon);
        TextView favourite=(TextView)home.findViewById(R.id.home_favourite_icon);
        favourite.setTypeface(MusicWaveActivity.icon);
        return home;
    }
    
    public List<HomeGridItem> getItems() {
		if (items == null) {
			items = new ArrayList<HomeGridItem>();
			items.add(new HomeGridItem("全部歌曲", getString(R.string.icon_all_songs), new AllMusicFragment(this)));
			items.add(new HomeGridItem("最近听过", getString(R.string.icon_recent_listen), null));
			items.add(new HomeGridItem("我的歌单", getString(R.string.icon_play_list_mine), null));
			items.add(new HomeGridItem("音效", getString(R.string.icon_music_effect), null));
			items.add(new HomeGridItem("扫描歌曲", getString(R.string.icon_scan_songs), new ScanFragment(this)));
			items.add(new HomeGridItem("设置", getString(R.string.icon_setting), null));
			
		}
		return items;
	}
}
