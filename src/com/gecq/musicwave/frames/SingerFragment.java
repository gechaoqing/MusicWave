package com.gecq.musicwave.frames;

import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.gecq.musicwave.R;
import com.gecq.musicwave.adapters.SingerFrameAdapter;
import com.gecq.musicwave.loaders.ArtistLoader;
import com.gecq.musicwave.models.Artist;
import com.gecq.musicwave.models.HomeGridItem;
import com.gecq.musicwave.utils.MusicUtils;

public class SingerFragment extends MusicWaveFragment implements
		LoaderCallbacks<List<Artist>>, OnItemClickListener {

	private SingerFrameAdapter mAdapter;
	/**
	 * LoaderCallbacks identifier
	 */
	private static final int LOADER = 0;
	/**
	 * The list view
	 */
	private ListView mListView;

	/**
	 * Fragment UI
	 */
	private View mRootView;

	public SingerFragment(HomeFragment home) {
		super(home);
	}

	@Override
	public int getType() {
		return HomeGridItem.HOME_MENU_SINGER;
	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.singer_frame, null, false);
		mListView = (ListView)mRootView.findViewById(R.id.singers_list);
		mListView.setAdapter(mAdapter);
		return mRootView;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// Enable the options menu
		setHasOptionsMenu(false);
		// Start the loader
		getLoaderManager().initLoader(LOADER, null, this);
	}

	@Override
	public Loader<List<Artist>> onCreateLoader(int arg0, Bundle arg1) {
		return new ArtistLoader(getActivity());
	}

	@Override
	public void onLoadFinished(Loader<List<Artist>> loader, List<Artist> data) {
		// Check for any errors
		if (data.isEmpty()) {
			// Set the empty text
			final TextView empty = (TextView) mRootView
					.findViewById(R.id.empty);
			empty.setText(getString(R.string.empty_song));
			mListView.setEmptyView(empty);
			return;
		}

		// Start fresh
		mAdapter.unload();
		// Add the data to the adpater
		for (final Artist artist : data) {
			mAdapter.add(artist);
		}
		// Build the cache
		mAdapter.buildCache();
	}

	@Override
	public void onLoaderReset(Loader<List<Artist>> arg0) {

	}

	/**
	 * Scrolls the list to the currently playing artist when the user touches
	 * the header in the {@link TitlePageIndicator}.
	 */
	public void scrollToCurrentArtist() {
		final int currentArtistPosition = getItemPositionByArtist();
		if (currentArtistPosition != 0) {
			mListView.setSelection(currentArtistPosition);
		}
	}

	/**
	 * @return The position of an item in the list or grid based on the name of
	 *         the currently playing artist.
	 */
	private int getItemPositionByArtist() {
		final long artistId = MusicUtils.getCurrentArtistId();
		if (mAdapter == null) {
			return 0;
		}
		for (int i = 0; i < mAdapter.getCount(); i++) {
			if (mAdapter.getItem(i).mArtistId == artistId) {
				return i;
			}
		}
		return 0;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

	}

	/**
	 * Restarts the loader.
	 */
	public void refresh() {
		// Wait a moment for the preference to change.
		SystemClock.sleep(10);
		getLoaderManager().restartLoader(LOADER, null, this);
	}

}
