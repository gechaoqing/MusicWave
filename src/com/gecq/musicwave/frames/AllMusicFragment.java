package com.gecq.musicwave.frames;

import java.util.ArrayList;
import java.util.List;

import com.gecq.musicwave.R;
import com.gecq.musicwave.activity.MusicWaveActivity;
import com.gecq.musicwave.adapters.AllMusicAdapter;
import com.gecq.musicwave.formats.Mp3;
import com.gecq.musicwave.models.HomeGridItem;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

/**
 * Created by chaoqing on 14-8-8.
 */
public class AllMusicFragment extends MusicWaveFragment {
	private List<Mp3> list;

	public AllMusicFragment(HomeFragment home) {
		super(home);
	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View all = inflater.inflate(R.layout.all_music_main, null, false);
		Button backHome = (Button) all.findViewById(R.id.all_music_back);
		queryAll();
		ListView allMusic = (ListView) all.findViewById(R.id.all_music_list);
		AllMusicAdapter adapter = new AllMusicAdapter(this, list);
		allMusic.setAdapter(adapter);
		allMusic.setOnItemClickListener(adapter);
		allMusic.setEmptyView(all.findViewById(R.id.all_music_empty));
		backHome.setTypeface(MusicWaveActivity.icon);
		backHome.setOnClickListener(new View.OnClickListener() {
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
		return all;
	}

	@Override
	public int getType() {
		return HomeGridItem.HOME_MENU_ALL;
	}

	private void queryAll() {
		if (list == null) {
			list = new ArrayList<Mp3>();
			Cursor mAudioCursor = getActivity().getContentResolver().query(
					MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null,
					null, MediaStore.Audio.AudioColumns.TITLE);
			for (int i = 0; i < mAudioCursor.getCount(); i++) {
				mAudioCursor.moveToNext();
				int indexTitle = mAudioCursor
						.getColumnIndex(MediaStore.Audio.AudioColumns.TITLE);
				int indexARTIST = mAudioCursor
						.getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST);
				int indexALBUM = mAudioCursor
						.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM);
				int indexPath = mAudioCursor
						.getColumnIndex(MediaStore.Audio.AudioColumns.DATA);
				int indexDuration = mAudioCursor
						.getColumnIndex(MediaStore.Audio.AudioColumns.DURATION);
				int duration = mAudioCursor.getInt(indexDuration);
				int song_id=mAudioCursor
						.getColumnIndex(MediaStore.Audio.AudioColumns._ID);
				int albumIndex=mAudioCursor.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM_ID);
				String strTitle = mAudioCursor.getString(indexTitle);
				String strARTIST = mAudioCursor.getString(indexARTIST);
				String strALBUM = mAudioCursor.getString(indexALBUM);
				String path = mAudioCursor.getString(indexPath);
				long id=mAudioCursor.getLong(song_id);
				long albumId=mAudioCursor.getLong(albumIndex);
				if (duration > 60000) {
					Mp3 mp3 = new Mp3(path);
					mp3.setId(id);
					mp3.setAlbumId(albumId);
					mp3.setAlbum(strALBUM);
					mp3.setName(strTitle);
					mp3.setArtist(strARTIST);
					list.add(mp3);
				}
			}
		}
	}
}
