package com.gecq.musicwave.adapters;

import java.util.List;

import com.gecq.musicwave.R;
import com.gecq.musicwave.activity.MusicWaveActivity;
import com.gecq.musicwave.frames.HomeFragment;
import com.gecq.musicwave.frames.MusicWaveFragment;
import com.gecq.musicwave.models.HomeGridItem;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class HomeItemGridAdapter extends BaseAdapter implements
		OnItemClickListener {
	private List<HomeGridItem> items;
	private HomeFragment home;

	public HomeItemGridAdapter(HomeFragment home) {
		this.home = home;
		this.items=home.getItems();
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View cv, ViewGroup parent) {
		if (cv == null) {
			cv = LayoutInflater.from(home.getActivity()).inflate(
					R.layout.home_grid_menu_item, null);
		}
		TextView icon = (TextView) cv.findViewById(R.id.home_item_icon);
		HomeGridItem item = items.get(position);
		icon.setTypeface(MusicWaveActivity.icon);
		icon.setText(item.getIcon());
		TextView descrip = (TextView) cv.findViewById(R.id.home_item_name);
		descrip.setText(item.getName());
		return cv;
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		HomeGridItem item = items.get(position);
		Fragment to = item.getTo();
		if (to != null) {
			final FragmentManager fragmentManager = home.getActivity()
					.getSupportFragmentManager();
			final FragmentTransaction transaction = fragmentManager
					.beginTransaction();
			transaction.setCustomAnimations(R.animator.side_in_left,
					R.animator.side_out_right);
			MusicWaveFragment mwf=item.getTo();
			transaction.replace(R.id.home_content, mwf);
			MusicWaveActivity.currentFrame=mwf.getType();
			transaction.commit();
		}
	}
}
