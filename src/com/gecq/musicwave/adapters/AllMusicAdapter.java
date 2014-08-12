package com.gecq.musicwave.adapters;

import java.util.List;

import com.gecq.musicwave.R;
import com.gecq.musicwave.formats.Mp3;
import com.gecq.musicwave.frames.AllMusicFragment;
import com.gecq.musicwave.player.PlayerManager;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by chaoqing on 14-8-9.
 */
public class AllMusicAdapter extends BaseAdapter implements OnItemClickListener {
	private List<Mp3> mp3s;
	private AllMusicFragment frame;
	private int selected=-1;

	public AllMusicAdapter(final AllMusicFragment frame, final List<Mp3> mp3s) {
		this.frame = frame;
		this.mp3s = mp3s;
	}

	@Override
	public int getCount() {
		return mp3s.size();
	}

	@Override
	public Object getItem(int i) {
		return mp3s.get(i);
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {
		if (view == null) {
			view = LayoutInflater.from(frame.getActivity()).inflate(
					R.layout.all_music_item, null);
		}
		TextView num = (TextView) view.findViewById(R.id.all_music_num);
		num.setText(String.valueOf(i + 1));
		if (i == selected) {
			num.setBackgroundResource(R.color.all_music_selected_num_bg);
		} else {
			num.setBackgroundResource(R.color.all_music_num_bg);
		}
		TextView name = (TextView) view.findViewById(R.id.all_music_song_name);
		Mp3 mp3 = mp3s.get(i);
		name.setText(mp3.getName());
		TextView singer = (TextView) view
				.findViewById(R.id.all_music_song_singer);
		singer.setText(mp3.getArtist());
		view.setTag(i);
		return view;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Mp3 mp3 = mp3s.get(position);
		PlayerManager.getInstance().playNew(mp3);
		int count = parent.getChildCount();
		for (int i = 0; i < count; i++) {
			View v = parent.getChildAt(i);
			TextView num = (TextView) v.findViewById(R.id.all_music_num);
			if ((Integer) v.getTag() != position) {
				num.setBackgroundResource(R.color.all_music_num_bg);
			}
		}
		view.findViewById(R.id.all_music_num).setBackgroundResource(R.color.all_music_selected_num_bg);
		selected = position;
	}
}
