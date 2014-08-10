package com.gecq.musicwave.models;

import com.gecq.musicwave.frames.MusicWaveFragment;


public class HomeGridItem {
	public static final int HOME_MENU_ALL = 1;
	public static final int HOME_MENU_RECENT = 2;
	public static final int HOME_MENU_PLAYLIST = 3;
	public static final int HOME_MENU_EFFECTS = 4;
	public static final int HOME_MENU_SCAN=5;
	public static final int HOME_MENU_SETTING=6;
	private String name;
	private String icon;
	private int action;
	private MusicWaveFragment to;

	public HomeGridItem(String name, String icon,MusicWaveFragment to) {
		this.name = name;
		this.icon = icon;
		this.to=to;
	}

	public MusicWaveFragment getTo() {
		return to;
	}

	public void setTo(MusicWaveFragment to) {
		this.to = to;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}
}
