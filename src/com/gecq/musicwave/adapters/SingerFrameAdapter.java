package com.gecq.musicwave.adapters;

import com.gecq.musicwave.R;
import com.gecq.musicwave.models.Artist;
import com.gecq.musicwave.ui.MusicHolder;
import com.gecq.musicwave.ui.MusicHolder.DataHolder;
import com.gecq.musicwave.utils.MusicUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class SingerFrameAdapter extends ArrayAdapter<Artist> {
	/**
     * Number of views (ImageView and TextView)
     */
    private static final int VIEW_TYPE_COUNT = 2;
	/**
	 * The resource Id of the layout to inflate
	 */
	private final int mLayoutId;
	/**
	 * Used to cache the artist info
	 */
	private DataHolder[] mData;

	public SingerFrameAdapter(Context context, int resource) {
		super(context, resource);
		mLayoutId = resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		MusicHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(mLayoutId,
					parent, false);
			holder = new MusicHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (MusicHolder) convertView.getTag();
		}
		// Retrieve the data holder
		final DataHolder dataHolder = mData[position];
		// Set each artist name (line one)
		holder.mLineOne.get().setText(dataHolder.mLineOne);
		// Set the number of albums (line two)
		holder.mLineTwo.get().setText(dataHolder.mLineTwo);
		return convertView;
	}
	
	
	
	/**
     * {@inheritDoc}
     */
    @Override
    public boolean hasStableIds() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    /**
     * Method used to cache the data used to populate the list or grid. The idea
     * is to cache everything before {@code #getView(int, View, ViewGroup)} is
     * called.
     */
    public void buildCache() {
        mData = new DataHolder[getCount()];
        for (int i = 0; i < getCount(); i++) {
            // Build the artist
            final Artist artist = getItem(i);

            // Build the data holder
            mData[i] = new DataHolder();
            // Artist Id
            mData[i].mItemId = artist.mArtistId;
            // Artist names (line one)
            mData[i].mLineOne = artist.mArtistName;
            // Number of albums (line two)
            mData[i].mLineTwo = MusicUtils.makeLabel(getContext(),
                    R.plurals.Nalbums, artist.mAlbumNumber);
            // Number of songs (line three)
            mData[i].mLineThree = MusicUtils.makeLabel(getContext(),
                    R.plurals.Nsongs, artist.mSongNumber);
        }
    }

    /**
     * Method that unloads and clears the items in the adapter
     */
    public void unload() {
        clear();
        mData = null;
    }

	@Override
	public long getItemId(int position) {
		return getItem(position).mArtistId;
	}

}
