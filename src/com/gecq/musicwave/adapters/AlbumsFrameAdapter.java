package com.gecq.musicwave.adapters;

import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.gecq.musicwave.cache.ImageFetcher;
import com.gecq.musicwave.models.Album;
import com.gecq.musicwave.ui.MusicHolder;
import com.gecq.musicwave.ui.MusicHolder.DataHolder;
import com.gecq.musicwave.utils.CommonUtils;

public class AlbumsFrameAdapter extends ArrayAdapter<Album>{
	/**
     * Number of views (ImageView and TextView)
     */
    private static final int VIEW_TYPE_COUNT = 2;
	/**
	 * The resource Id of the layout to inflate
	 */
	private final int mLayoutId;
	/**
	 * Used to cache the album info
	 */
	private DataHolder[] mData;
	
	private final ImageFetcher mImageFetcher;
	public AlbumsFrameAdapter(final FragmentActivity activity, int resource) {
		super(activity, resource);
		mLayoutId=resource;
		mImageFetcher=CommonUtils.getImageFetcher(activity);
	}
	
	private Handler handler=null;
		
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final MusicHolder holder;
		if(handler==null){
			handler=new Handler();
		}
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
		mImageFetcher.loadAlbumImage(dataHolder.mLineTwo, dataHolder.mLineOne, dataHolder.mItemId,
                holder.mImage.get());
		// Set each album name (line one)
		holder.mLineOne.get().setText(dataHolder.mLineOne);
		// Set the number of albums (line two)
		holder.mLineTwo.get().setText(dataHolder.mLineTwo);
		
//		holder.mLineThree.get().setText(dataHolder.mLineThree);
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
            // Build the album
            final Album album = getItem(i);

            // Build the data holder
            mData[i] = new DataHolder();
            // Artist Id
            mData[i].mItemId = album.mAlbumId;
            // Artist names (line one)
            mData[i].mLineOne = album.mAlbumName;
//            // Number of albums (line two)
            mData[i].mLineTwo = album.mArtistName;
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
		return getItem(position).mAlbumId;
	}

}
