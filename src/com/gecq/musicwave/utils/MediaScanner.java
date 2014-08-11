package com.gecq.musicwave.utils;

import java.io.File;
import android.content.Context;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

public class MediaScanner implements MediaScannerConnectionClient {

	private File mScanDir;
	private MediaScannerConnection mScanner;
	private Context context;
	private boolean scanning;
	private Handler handl;
	private int size;

	public MediaScanner(File dir,Context context,Handler handl) {
		mScanDir = dir;
		this.context=context;
		this.handl=handl;
	}

	@Override
	public void onMediaScannerConnected() {
		new AsyncTask<Integer, Integer, Void>() {
			@Override
			protected Void doInBackground(Integer... params) {
				scanFolder(mScanDir);
				return null;
			}
		}.execute(0);
	}
	
	private void scanFolder(File dir){
		if(!scanning){
			return;
		}
		String path=dir.getAbsolutePath();
		Message msg=handl.obtainMessage();
		msg.obj=path;
		msg.sendToTarget();
		if(dir.isDirectory()){
			File[] fs=dir.listFiles();
			if(fs==null){
				return;
			}
			for(File f:fs){
				scanFolder(f);
			}
		}else{
			if(AudioFile.isAudioFileType(path)){
				size++;
				msg=handl.obtainMessage(2);
				msg.arg1=size;
				msg.sendToTarget();
				mScanner.scanFile(path, "audio/*");
			}
			return;
		}
	}

	@Override
	public void onScanCompleted(String path, Uri uri) {
        mScanner.disconnect();
        mScanner = null;
        Message msg=handl.obtainMessage(1);
		msg.sendToTarget();
	}
	
	public void start(){
		mScanner = new MediaScannerConnection(context, MediaScanner.this);
		scanning=true;
		mScanner.connect();
	}
	
	public void stop(){
		scanning=false;
		if(mScanner!=null){
			mScanner.disconnect();
			mScanner=null;
		}
	}

}
