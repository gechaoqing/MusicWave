package com.gecq.musicwave.frames;

import java.util.List;

import com.gecq.musicwave.R;
import com.gecq.musicwave.activity.MusicWaveActivity;
import com.gecq.musicwave.adapters.AllSongAdapter;
import com.gecq.musicwave.loaders.SongLoader;
import com.gecq.musicwave.models.HomeGridItem;
import com.gecq.musicwave.models.Song;
import com.gecq.musicwave.utils.MusicUtils;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by chaoqing on 14-8-8.
 */
public class AllMusicFragment extends MusicWaveFragment implements
		LoaderCallbacks<List<Song>> {
	private ListView allMusic;
	private AllSongAdapter adapter;
	private View mRootView;
	private static final int LOADER = 0;

	public AllMusicFragment(HomeFragment home) {
		super(home);
	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.all_music_main, null, false);
		Button backHome = (Button) mRootView.findViewById(R.id.all_music_back);
		allMusic = (ListView) mRootView.findViewById(R.id.all_music_list);
		allMusic.setAdapter(adapter);
		allMusic.setOnItemClickListener(adapter);
		allMusic.setEmptyView(mRootView.findViewById(R.id.all_music_empty));
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
		return mRootView;
	}

	@Override
	public int getType() {
		return HomeGridItem.HOME_MENU_ALL;
	}

	@Override
	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// Enable the options menu
		setHasOptionsMenu(false);
		// Start the loader
		getLoaderManager().initLoader(LOADER, null, this);
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Create the adpater
		adapter = new AllSongAdapter(getActivity(), R.layout.list_item_simple);
	}

	@Override
	public Loader<List<Song>> onCreateLoader(int arg0, Bundle arg1) {
		return new SongLoader(getActivity());
	}
	
	public void scrollToCurrentSong() {
		final int currentArtistPosition = getItemPositionBySong();
		if (currentArtistPosition != 0) {
			allMusic.setSelection(currentArtistPosition);
		}
	}
	
	private int getItemPositionBySong() {
		final long songid = MusicUtils.getCurrentArtistId();
		if (adapter == null) {
			return 0;
		}
		for (int i = 0; i < adapter.getCount(); i++) {
			if (adapter.getItem(i).mSongId == songid) {
				return i;
			}
		}
		return 0;
	}

	@Override
	public void onLoadFinished(Loader<List<Song>> loader, List<Song> data) {
		// Check for any errors
		if (data.isEmpty()) {
			// Set the empty text
			final TextView empty = (TextView) mRootView
					.findViewById(R.id.all_music_empty);
			empty.setText(getString(R.string.empty_song));
			allMusic.setEmptyView(empty);
			return;
		}

		// Start fresh
		adapter.unload();
		// Add the data to the adpater
		for (final Song song : data) {
			adapter.add(song);
		}
		// Build the cache
		adapter.buildCache();
	}

	@Override
	public void onLoaderReset(Loader<List<Song>> arg0) {

	}

}
