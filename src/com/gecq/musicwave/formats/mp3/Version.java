package com.gecq.musicwave.formats.mp3;

import java.io.Serializable;

public class Version implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String VERSION;
	private static final String URL = "http://github.com/mpatric/mp3agic";
	
	static { // get version from JAR manifest
		String implementationVersion =  Version.class.getPackage().getImplementationVersion();
		VERSION = implementationVersion != null ? implementationVersion : "UNKNOWN-SNAPSHOT";
	}
	
	public static String asString() {
		return getVersion() + " - " + getUrl(); 
	}	

	public static String getVersion() {
		return VERSION;
	}
	
	public static String getUrl() {
		return URL;
	}
}
