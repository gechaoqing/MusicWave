package com.gecq.musicwave.database;

import java.util.ArrayList;
import java.util.List;

import com.gecq.musicwave.formats.Mp3;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {
    private DBHelper helper;
    private SQLiteDatabase db;

    public DBManager(Context context) {
        helper = new DBHelper(context);
        // 因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0,
        // mFactory);
        // 所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        db = helper.getWritableDatabase();
    }

    public boolean isAllExist() {
        Cursor c = db.rawQuery("select count(0) from " + Tables.ALL_SONGS, null);
        int s = 0;
        while (c.moveToNext()) {
            s = c.getInt(0);
        }
        c.close();
        db.close();
        return s > 0;
    }

    public boolean addMp3ToAll(List<Mp3> mp3s) {
        db=helper.getWritableDatabase();
        db.beginTransaction();
        try {
            for (Mp3 mp3 : mp3s) {
                if (isExsit(mp3.getFileName())) {
                    continue;
                }
                insertToAll(mp3);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
        return false;
    }

    private void insertToAll(Mp3 mp3) {
        db.execSQL(
                "insert into "
                        + Tables.ALL_SONGS
                        + "(name,size,album,m_path,artist,year,comment,genre,genreDescrib,track)"
                        + " values(?,?,?,?,?,?,?,?,?,?)",
                new Object[]{mp3.getName(),mp3.getSize(), mp3.getAlbum(),
                        mp3.getFileName(), mp3.getArtist(),
                        mp3.getYear(), mp3.getComment(), mp3.getGenre(),
                        mp3.getGenreDescrib(), mp3.getTrack()});
    }

    public boolean addMp3ToAll(Mp3 mp3) {
        if (isMp3Exsit(mp3.getFileName())) {
            return false;
        }
        db.beginTransaction();
        try {
            insertToAll(mp3);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            return false;
        } finally {
            db.endTransaction();
            db.close();
        }
        return true;
    }

    public boolean addMp3To(Tables table, Mp3 mp3) {
        if (table == Tables.ALL_SONGS) {
            return addMp3ToAll(mp3);
        }
        String id = String.valueOf(mp3.getId());
        Cursor c = db.rawQuery("select count(0) from " + table + " where _id=?",
                new String[]{id});
        boolean has = c.moveToNext();
        c.close();
        if (!has) {
            db.beginTransaction();
            try {
                db.execSQL("insert into " + table + " values(?)",
                        new Object[]{id});
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
                db.close();
            }
        }
        return !has;
    }

    public boolean addMp3ToList(Integer mid, Integer lid) {
        Cursor c = db.rawQuery("select count(0) from " + Tables.SONGS_IN_LIST
                + " where song_id=? and list_id=?",
                new String[]{String.valueOf(mid), String.valueOf(lid)});
        boolean has = c.moveToNext();
        c.close();
        if (!has) {
            db.beginTransaction();
            try {
                db.execSQL("insert into " + Tables.SONGS_IN_LIST
                        + "(song_id,list_id) values(?,?)", new Object[]{mid,
                        lid});
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
                db.close();
            }
        }
        return !has;
    }

    public List<Mp3> getAll() {
        if(!db.isOpen()){
            db=helper.getReadableDatabase();
        }
        List<Mp3> list = new ArrayList<Mp3>();
        Cursor c = db.rawQuery("select * from " + Tables.ALL_SONGS, null);
        while (c.moveToNext()) {
            Mp3 mp3 = new Mp3(c.getString(c.getColumnIndex("m_path")));
            mp3.setAlbum(c.getString(c.getColumnIndex("album")));
            mp3.setArtist(c.getString(c.getColumnIndex("artist")));
            mp3.setComment(c.getString(c.getColumnIndex("comment")));
            mp3.setGenre(c.getInt(c.getColumnIndex("genre")));
            mp3.setGenreDescrib(c.getString(c.getColumnIndex("genreDescrib")));
            mp3.setYear(c.getString(c.getColumnIndex("year")));
            mp3.setTrack(c.getString(c.getColumnIndex("track")));
            mp3.setSize(c.getDouble(c.getColumnIndex("size")));
            mp3.setName(c.getString(c.getColumnIndex("name")));
            list.add(mp3);
        }
        c.close();
        db.close();
        return list;
    }

    public List<Mp3> getSongsInList(Integer lid) {
        List<Mp3> list = new ArrayList<Mp3>();
        Cursor c = db.rawQuery("select as.* ,(select sl.song_id from " + Tables.SONGS_IN_LIST + " sl , " + Tables.SONG_LISTS + " l  where sl.list_id=l._id and l._id=?) sid from "
                + Tables.ALL_SONGS + " as where as._id=sid",
                new String[]{String.valueOf(lid)});
        while (c.moveToNext()) {
            Mp3 mp3 = new Mp3(c.getString(c.getColumnIndex("m_path")));
            mp3.setAlbum(c.getString(c.getColumnIndex("album")));
            mp3.setArtist(c.getString(c.getColumnIndex("artist")));
            mp3.setComment(c.getString(c.getColumnIndex("comment")));
            mp3.setGenre(c.getInt(c.getColumnIndex("genre")));
            mp3.setGenreDescrib(c.getString(c.getColumnIndex("genreDescrib")));
            mp3.setYear(c.getString(c.getColumnIndex("year")));
            mp3.setTrack(c.getString(c.getColumnIndex("track")));
            mp3.setSize(c.getDouble(c.getColumnIndex("size")));
            mp3.setName(c.getString(c.getColumnIndex("name")));
            list.add(mp3);
        }
        c.close();
        db.close();
        return list;
    }

    public int delFrom(Tables table, Integer id) {
        int d = db.delete(table.toString(), "_id = ?", new String[]{String.valueOf(id)});
        db.close();
        return d;
    }

    public boolean isMp3Exsit(Tables table, Integer id) {
        Cursor c = db.rawQuery("select _id from " + table + " where _id=?", new String[]{String.valueOf(id)});
        boolean has = c.moveToNext();
        c.close();
        db.close();
        return has;
    }

    public boolean isMp3Exsit(String path) {
        if (path == null) {
            return true;
        }
        Cursor c = db.rawQuery("select _id from " + Tables.ALL_SONGS + " where m_path=?", new String[]{path});
        boolean has = c.moveToNext();
        c.close();
        db.close();
        return has;
    }

    private boolean isExsit(String path){
        Cursor c = db.rawQuery("select _id from " + Tables.ALL_SONGS + " where m_path=?", new String[]{path});
        boolean has = c.moveToNext();
        c.close();
        return has;
    }
    
    


}
