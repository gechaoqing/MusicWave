package com.gecq.musicwave.formats;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

import com.gecq.musicwave.formats.jmp.BuffRandAcceFile;
import com.gecq.musicwave.formats.jmp.ID3Tag;
import com.gecq.musicwave.formats.mp3.ID3v1;
import com.gecq.musicwave.formats.mp3.ID3v2;
import com.gecq.musicwave.formats.mp3.InvalidDataException;
import com.gecq.musicwave.formats.mp3.Mp3File;
import com.gecq.musicwave.formats.mp3.UnsupportedTagException;
import com.gecq.musicwave.utils.CharUtils;


public class Mp3 implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean hasDecodeAlbum=false;
	private Integer id;
	private final String UNKNOWN = "未知";
	private double size;
	private String name;// 歌曲名
	private String album;// 专辑
	private String artist;// 歌手
	private String year;// 年代
	private String comment;
	private String track;// 轨道
	private int genre;// 流派（体裁）
	private String genreDescrib;
	private byte[] albumImage;
	private String fileName;

	public Mp3(String fileName, boolean decode) {
		this(fileName);
		if (decode) {
			decodeAll();
		}
	}

	public Mp3(String fileName) {
		this.fileName = fileName;
	}

	public void decodeJustName() {
		try {
			BuffRandAcceFile buf = new BuffRandAcceFile(fileName);
			ID3Tag tag = buf.readID3Tag();
			setName(tag.getTitle());
			setArtist(tag.getArtist());
			setAlbum(tag.getAlbum());
			setYear(tag.getYear());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void decodeAlbum(){
		try {
			Mp3File mp3File = new Mp3File(this.fileName);
			if (mp3File.hasId3v2Tag()) {
				ID3v2 tag = mp3File.getId3v2Tag();
				setAlbumImage(tag.getAlbumImage());
			}
		} catch (UnsupportedTagException e) {
			e.printStackTrace();
		} catch (InvalidDataException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		setHasDecodeAlbum(true);
	}

	public void decodeAll() {
		try {
			Mp3File mp3File = new Mp3File(this.fileName);
			size = mp3File.getLength() / (1024 * 1024);
			BigDecimal bg = new BigDecimal(size).setScale(2,
					RoundingMode.HALF_UP);
			size = bg.doubleValue();
			ID3v1 tag = null;
			if (mp3File.hasId3v1Tag()) {
				tag = mp3File.getId3v1Tag();
			}
			if (mp3File.hasId3v2Tag()) {
				tag = mp3File.getId3v2Tag();
				setAlbumImage(((ID3v2) tag).getAlbumImage());
			}
			setTags(tag);
		} catch (UnsupportedTagException e) {
			e.printStackTrace();
		} catch (InvalidDataException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void setTags(ID3v1 tag) {
		if (tag == null) {
			return;
		}
		setAlbum(tag.getAlbum());
		setArtist(tag.getArtist());
		setComment(tag.getComment());
		setYear(tag.getYear());
		setTrack(tag.getTrack());
		setName(tag.getTitle());
		setGenre(tag.getGenre());
		setGenreDescrib(tag.getGenreDescription());
	}

	public double getSize() {
		return size;
	}

	public void setSize(double size) {
		this.size = size;
	}

	public String getName() {
		if(name==null||CharUtils.isMessyCode(name))
		{
			name=fileName.substring(fileName.lastIndexOf("/")+1,fileName.lastIndexOf("."));
		}
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlbum() {
		return album == null ? UNKNOWN : album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public String getArtist() {
		if(artist==null||CharUtils.isMessyCode(artist)){
			artist=UNKNOWN;
		}
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getComment() {
		return comment == null ? "" : comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getTrack() {
		return track;
	}

	public void setTrack(String track) {
		this.track = track;
	}

	public int getGenre() {
		return genre;
	}

	public void setGenre(int genre) {
		this.genre = genre;
	}

	public String getGenreDescrib() {
		return genreDescrib;
	}

	public void setGenreDescrib(String genreDescrib) {
		this.genreDescrib = genreDescrib;
	}

	public byte[] getAlbumImage() {
		return albumImage;
	}

	public void setAlbumImage(byte[] albumImage) {
		this.albumImage = albumImage;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Mp3) {
			Mp3 om = (Mp3) o;
			return om.getFileName().equals(getFileName());
		}
		return super.equals(o);
	}

	public boolean isHasDecodeAlbum() {
		return hasDecodeAlbum;
	}

	public void setHasDecodeAlbum(boolean hasDecodeAlbum) {
		this.hasDecodeAlbum = hasDecodeAlbum;
	}
}
