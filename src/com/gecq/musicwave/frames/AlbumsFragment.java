package com.gecq.musicwave.frames;

import java.util.List;

import com.gecq.musicwave.R;
import com.gecq.musicwave.activity.MusicWaveActivity;
import com.gecq.musicwave.adapters.AlbumsFrameAdapter;
import com.gecq.musicwave.loaders.AlbumLoader;
import com.gecq.musicwave.models.Album;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class AlbumsFragment extends MusicWaveFragment implements
		LoaderCallbacks<List<Album>>, OnItemClickListener {
	private AlbumsFrameAdapter mAdapter;
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

	public AlbumsFragment(HomeFragment home) {
		super(home);
	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.albums_frame, null, false);
		mListView = (ListView) mRootView.findViewById(R.id.albums_list);
		mListView.setAdapter(mAdapter);
		Button backHome = (Button) mRootView.findViewById(R.id.back_home);
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
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Create the adpater
		mAdapter = new AlbumsFrameAdapter(getActivity(),
				R.layout.list_item_normal);
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
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

	}

	@Override
	public Loader<List<Album>> onCreateLoader(int arg0, Bundle arg1) {
		return new AlbumLoader(getActivity());
	}

	@Override
	public void onLoadFinished(Loader<List<Album>> arg0, List<Album> data) {
		// Check for any errors
		if (data.isEmpty()) {
			// Set the empty text
			final TextView empty = (TextView) mRootView
					.findViewById(R.id.empty);
			empty.setText(getString(R.string.empty_album));
			mListView.setEmptyView(empty);
			return;
		}

		// Start fresh
		mAdapter.unload();
		// Add the data to the adpater
		for (final Album album : data) {
			mAdapter.add(album);
		}
		// Build the cache
		mAdapter.buildCache();
	}

	@Override
	public void onLoaderReset(Loader<List<Album>> arg0) {

	}

	@Override
	public int getType() {
		return R.string.albums;
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
