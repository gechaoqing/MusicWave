package com.gecq.musicwave.database;

public enum Tables {
	PLAY_LIST("t_play_list"),
	SONG_LISTS("t_song_lists"),
	SONGS_IN_LIST("t_songs_lists"),
	ALL_SONGS("t_all_songs"),
	RECENET_SONGS("t_recent_songs");
	private String name;
	Tables(String name){
		this.name=name;
	}
	@Override
	public String toString() {
		return name;
	}
	
	
}
