package com.gecq.musicwave.utils;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.ImageView;

public class ImageWork {
	private Map<String,SoftReference<Bitmap>> imageCache;

	public ImageWork() {
		imageCache = new HashMap<String,SoftReference<Bitmap>>();
	}
	
	public Bitmap loadAlbumImage(final Context context,final ImageView imageView,final long song_id,final long album_id,final ImageCallback imageCallback){
		final String key=getAlbumKey(album_id);
		if (imageCache.containsKey(key)) {
			SoftReference<Bitmap> softReference = imageCache.get(key);
			Bitmap drawable = softReference.get();
			if (drawable != null) {
				imageCallback.imageLoaded(imageView,drawable);
				return drawable;
			}
		}
		final Handler handler = new Handler(Looper.myLooper()) {
			public void handleMessage(Message message) {
				imageCallback.imageLoaded(imageView,(Bitmap) message.obj);
			}
		};
		new Thread() {
			@Override
			public void run() {
				Bitmap drawable = ArtWork.getArtwork(context, song_id, album_id, true);
				imageCache.put(key, new SoftReference<Bitmap>(drawable));
				Message message = handler.obtainMessage(0, drawable);
				handler.sendMessage(message);
			}
		}.start();
		return null;
	}
	
	private String getAlbumKey(long id){
		return "album_"+id;
	}
	
	public interface ImageCallback {
		public void imageLoaded(ImageView imageView, Bitmap bitmap);
	}
}
