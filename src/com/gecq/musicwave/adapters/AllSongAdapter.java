package com.gecq.musicwave.adapters;

import com.gecq.musicwave.R;
import com.gecq.musicwave.models.Song;
import com.gecq.musicwave.ui.MusicHolder;
import com.gecq.musicwave.ui.MusicHolder.DataHolder;
import com.gecq.musicwave.utils.MusicUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;

public class AllSongAdapter extends ArrayAdapter<Song> implements OnItemClickListener {
	
	/**
     * Number of views (TextView)
     */
    private static final int VIEW_TYPE_COUNT = 1;

    /**
     * The resource Id of the layout to inflate
     */
    private final int mLayoutId;

    /**
     * Used to cache the song info
     */
    private DataHolder[] mData;
    
    /* The song id array */
    private long[] ids;

	public AllSongAdapter(Context context, int resource) {
		super(context, 0);
		this.mLayoutId=resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		MusicHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(mLayoutId, parent, false);
            holder = new MusicHolder(convertView);
            // Hide the third line of text
            holder.mLineThree.get().setVisibility(View.GONE);
            convertView.setTag(R.id.view_holder_tag,holder);
            convertView.setTag(R.id.view_position_tag, position);
        } else {
            holder = (MusicHolder)convertView.getTag(R.id.view_holder_tag);
        }
        
        if(MusicUtils.getCurrentAudioId()==getItemId(position)){
        	holder.root.get().setBackgroundResource(R.color.current_playing_background);
        }else{
        	holder.root.get().setBackgroundResource(R.color.color_transparent);
        }

        // Retrieve the data holder
        final DataHolder dataHolder = mData[position];

        // Set each song name (line one)
        holder.mLineOne.get().setText(dataHolder.mLineOne);
        // Set the song duration (line one, right)
        holder.mLineOneRight.get().setText(dataHolder.mLineOneRight);
        // Set the album name (line two)
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
        ids=new long[getCount()];
        for (int i = 0; i < getCount(); i++) {
            // Build the song
            final Song song = getItem(i);

            // Build the data holder
            mData[i] = new DataHolder();
            // Song Id
            mData[i].mItemId = song.mSongId;
            // Song names (line one)
            mData[i].mLineOne = song.mSongName;
            // Song duration (line one, right)
            mData[i].mLineOneRight = MusicUtils.makeTimeString(getContext(), song.mDuration);
            // Album names (line two)
            mData[i].mLineTwo = song.mAlbumName;
            ids[i]=song.mSongId;
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
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if(MusicUtils.getCurrentAudioId()!=getItemId(position)){
			int count = parent.getChildCount();
			for (int i = 0; i < count; i++) {
				View v = parent.getChildAt(i);
				if ((Integer) v.getTag(R.id.view_position_tag) != position) {
					v.setBackgroundResource(R.color.color_transparent);
				}
			}
			view.setBackgroundResource(
					R.color.current_playing_background);
		}
		MusicUtils.playAll(getContext(), ids, position, false);
	}

	@Override
	public long getItemId(int position) {
		return getItem(position).mSongId;
	}
	
	
	
	

}
