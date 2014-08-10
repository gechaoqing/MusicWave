package com.gecq.musicwave.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "gePlayer2014.db";
	private static final int DATABASE_VERSION = 1;
//	public static final String TABLE_PLAY_LIST = "t_play_list";
//	public static final String TABLE_SONG_LISTS = "t_song_lists";
//	public static final String TABLE_SONGS_IN_LIST = "t_songs_lists";
//	public static final String TABLE_ALL_SONGS = "t_all_songs";
//	public static final String TABLE_RECENT_SONGS = "t_recent_songs";

	public DBHelper(Context context) {
		// CursorFactory设置为null,使用默认值
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS " + Tables.PLAY_LIST
				+ "(_id INTEGER PRIMARY KEY)");
		db.execSQL("CREATE TABLE IF NOT EXISTS " + Tables.SONG_LISTS
				+ "(_id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR)");
		db.execSQL("CREATE TABLE IF NOT EXISTS "
				+ Tables.SONGS_IN_LIST
				+ "(song_id INTEGER,list_id INTEGER)");
		db.execSQL("CREATE TABLE IF NOT EXISTS "
				+ Tables.ALL_SONGS
				+ "(_id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR,size VARCHAR"
				+ ",m_path VARCHAR,artist VARCHAR"
				+ ",album VARCHAR,track VARCHAR"
				+ ",year VARCHAR,comment VARCHAR"
				+ ",genre INTEGER,genreDescrib VARCHAR)");
		db.execSQL("CREATE TABLE IF NOT EXISTS " + Tables.RECENET_SONGS
				+ "(_id INTEGER PRIMARY KEY)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
