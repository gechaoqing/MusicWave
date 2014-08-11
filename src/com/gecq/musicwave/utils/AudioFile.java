package com.gecq.musicwave.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

public class AudioFile {
	    // comma separated list of all file extensions supported by the media scanner
	    public static String sFileExtensions;

	    // Audio file types
	    public static final int FILE_TYPE_MP3     = 1;
	    public static final int FILE_TYPE_M4A     = 2;
	    public static final int FILE_TYPE_WAV     = 3;
	    public static final int FILE_TYPE_AMR     = 4;
	    public static final int FILE_TYPE_AWB     = 5;
	    public static final int FILE_TYPE_WMA     = 6;
	    public static final int FILE_TYPE_OGG     = 7;
	    private static final int FIRST_AUDIO_FILE_TYPE = FILE_TYPE_MP3;
	    private static final int LAST_AUDIO_FILE_TYPE = FILE_TYPE_OGG;

	   
	    
	    static class AudioFileType {
	    
	        int fileType;
	        String mimeType;
	        AudioFileType(int fileType, String mimeType) {
	            this.fileType = fileType;
	            this.mimeType = mimeType;
	        }
	    }
	    
	    private static HashMap<String, AudioFileType> sFileTypeMap 
	            = new HashMap<String, AudioFileType>();
	    private static HashMap<String, Integer> sMimeTypeMap 
	            = new HashMap<String, Integer>();            
	    static void addFileType(String extension, int fileType, String mimeType) {
	        sFileTypeMap.put(extension, new AudioFileType(fileType, mimeType));
	        sMimeTypeMap.put(mimeType, fileType);
	    }
	    static {
	        addFileType("MP3", FILE_TYPE_MP3, "audio/mpeg");
	        addFileType("M4A", FILE_TYPE_M4A, "audio/mp4");
	        addFileType("WAV", FILE_TYPE_WAV, "audio/x-wav");
	        addFileType("AMR", FILE_TYPE_AMR, "audio/amr");
	        addFileType("AWB", FILE_TYPE_AWB, "audio/amr-wb");
	        addFileType("WMA", FILE_TYPE_WMA, "audio/x-ms-wma");    
	        addFileType("OGG", FILE_TYPE_OGG, "application/ogg"); 

	        // compute file extensions list for native Media Scanner
	        StringBuilder builder = new StringBuilder();
	        Iterator<String> iterator = sFileTypeMap.keySet().iterator();
	        
	        while (iterator.hasNext()) {
	            if (builder.length() > 0) {
	                builder.append(',');
	            }
	            builder.append(iterator.next());
	        } 
	        sFileExtensions = builder.toString();
	    }
	    
	    public static final String UNKNOWN_STRING = "<unknown>";
	    
	    public static boolean isAudioFileType(int fileType) {
	        return ((fileType >= FIRST_AUDIO_FILE_TYPE &&
	                fileType <= LAST_AUDIO_FILE_TYPE));
	    }
	    
	    
	    public static AudioFileType getFileType(String path) {
	        int lastDot = path.lastIndexOf(".");
	        if (lastDot < 0)
	            return null;
	        return sFileTypeMap.get(path.substring(lastDot + 1).toUpperCase(Locale.CHINA));
	    }
	    //根据音频文件路径判断文件类型
	    public static boolean isAudioFileType(String path) {  //自己增加
	        AudioFileType type = getFileType(path);
	        if(null != type) {
	            return isAudioFileType(type.fileType);
	        }
	        return false;
	    }
	    //根据mime类型查看文件类型
	    public static int getFileTypeForMimeType(String mimeType) {
	        Integer value = sMimeTypeMap.get(mimeType);
	        return (value == null ? 0 : value.intValue());
	    }
}
