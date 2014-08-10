package com.gecq.musicwave.adapters;

import java.util.ArrayList;
import java.util.List;

import com.gecq.musicwave.R;
import com.gecq.musicwave.activity.MusicWaveActivity;
import com.gecq.musicwave.frames.AllMusicFragment;
import com.gecq.musicwave.frames.HomeFragment;
import com.gecq.musicwave.models.HomeGridItem;

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

public class HomeItemGridAdapter extends BaseAdapter implements OnItemClickListener {
	List<HomeGridItem> items;
    HomeFragment home;
	public HomeItemGridAdapter(HomeFragment home) {
		this.home=home;
		getItems();
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
		if(cv==null){
			cv=LayoutInflater.from(home.getActivity()).inflate(R.layout.home_grid_menu_item, null);
		}
		TextView icon=(TextView)cv.findViewById(R.id.home_item_icon);
		HomeGridItem item=items.get(position);
		icon.setTypeface(MusicWaveActivity.icon);
		icon.setText(item.getIcon());
		TextView descrip=(TextView)cv.findViewById(R.id.home_item_name);
		descrip.setText(item.getName());
		return cv;
	}
	
	private List<HomeGridItem> getItems(){
		if(items==null){
			items=new ArrayList<HomeGridItem>();
		}
		items.add(new HomeGridItem("全部歌曲", "a", HomeGridItem.HOME_MENU_ALL));
		items.add(new HomeGridItem("最近听过", "r", HomeGridItem.HOME_MENU_RECENT));
		items.add(new HomeGridItem("我的歌单", "m", HomeGridItem.HOME_MENU_PLAYLIST));
		items.add(new HomeGridItem("音效", "e", HomeGridItem.HOME_MENU_EFFECTS));
		items.add(new HomeGridItem("扫描歌曲", "c", HomeGridItem.HOME_MENU_SCAN));
		items.add(new HomeGridItem("设置", "d", HomeGridItem.HOME_MENU_SETTING));
		return items;
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		HomeGridItem item=items.get(position);
        final FragmentManager fragmentManager = home.getActivity().getSupportFragmentManager();
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        switch (item.getAction()){
            case HomeGridItem.HOME_MENU_ALL:
                toAllFrame(transaction);
                break;
            case HomeGridItem.HOME_MENU_SCAN:
            	break;
        }

	}

    private void toAllFrame(final FragmentTransaction transaction){
        AllMusicFragment amf=new AllMusicFragment(home);
        transaction.setCustomAnimations(R.animator.side_in_left, R.animator.side_out_right);
        transaction.replace(R.id.home_content,amf);
        transaction.commit();
    }
    
    private void toScanFrame(){
    	
    }

}
