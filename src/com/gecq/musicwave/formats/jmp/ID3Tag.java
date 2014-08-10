package com.gecq.musicwave.formats.jmp;

/*
* ID3Tag.java -- 解析MP3文件的ID3 v1/v2 tag
* Copyright (C) 2010
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*
* If you would like to negotiate alternate licensing terms, you may do
* so by contacting the author: <http://jmp123.sourceforge.net/>
*/

import java.io.UnsupportedEncodingException;

/*
 ID3v1:
 [0-2]    3  bytes: ID3 v1标识 -- 'TAG'
 [3—32]   30 bytes: 标题
 [33—62]  30 bytes: 艺术家
 [63—92]  30 bytes: 专辑名
 [93—96]  4  bytes: 发行年份
 [97—126] 30 bytes: v1.0 -- 注释/附加/备注信息
 					v1.1 -- 前29 bytes注释/附加/备注信息，最后1 byte音轨信息
 [127]    1  byte : 流派
*/

public final class ID3Tag {
	// ID3v1 & ID3v2
	private String strTitle;
	private String strArtist;
	private String strAlbum;
	private String strYear;

	// ID3v2
	//private String strLyrics;		// (内嵌)歌词
	private int intVersion;
	private int intExHeaderSize;
	private boolean boolID3v2Footer;
	//TEXT_ENCODING[0]应由 "ISO-8859-1" 改为 "GBK". ??
	private static String[] TEXT_ENCODING = {"GB2312","UTF-16", "UTF-16BE", "UTF-8"};

	//--------------------------------------------------------------------
	// ID3v1 & ID3v2

	public void printTag() {
		//if (strLyrics != null)
		//	System.out.println("\r" + strLyrics + "\n");
		if (strTitle != null)
			System.out.println("\r        标题: " + strTitle);
		if (strArtist != null)
			System.out.println("\r      艺术家: " + strArtist);
		if (strAlbum != null)
			System.out.println("\r      唱片集: " + strAlbum);
		if (strYear != null)
			System.out.println("\r      发行年: " + strYear);
	}

	public void destroy() {
		strTitle = strArtist = strAlbum = strYear = null;
		//strLyrics = null;
		intVersion = intExHeaderSize = 0;
		boolID3v2Footer = false;
	}

	public String getTitle() {
		return strTitle;
	}

	public String getArtist() {
		return strArtist;
	}

	public String getAlbum() {
		return strAlbum;
	}

	public String getYear() {
		return strYear;
	}

	//--------------------------------------------------------------------
	// ID3v1

	public boolean checkID3V1(byte[] b) {
		return b[0] == 'T' && b[1] == 'A' && b[2] == 'G';
	}

	public void parseID3V1(byte[] b) {
		int i;
		if (b.length < 128 || checkID3V1(b) == false)
			return;

		byte[] buf = new byte[125];
		System.arraycopy(b, 3, buf, 0, 125);

		for (i = 0; i < 30 && buf[i] != 0; i++);
		if (strTitle == null)
			strTitle = new String(buf, 0, i).trim();
		if (strTitle.length() == 0)
			strTitle = null;

		for (i = 30; i < 60 && buf[i] != 0; i++);
		if (strArtist == null)
			strArtist = new String(buf, 30, i-30).trim();
		if (strArtist.length() == 0)
			strArtist = null;

		for (i = 60; i < 90 && buf[i] != 0; i++);
		if (strAlbum == null)
			strAlbum = new String(buf, 60, i-60).trim();
		if (strAlbum.length() == 0)
			strAlbum = null;

		for (i = 90; i < 94 && buf[i] != 0; i++);
		if (strYear == null)
			strYear = new String(buf, 90, i-90).trim();
		if (strYear.length() == 0)
			strYear = null;

		buf = null;
	}

	//--------------------------------------------------------------------
	// ID3v2

	public int checkID3V2(byte[] b, int off) {
		if(b.length - off < 10)
			return 0;
		if(b[off] != 'I' || b[off+1] != 'D' || b[off+2] != '3')
			return 0;

		intVersion = b[off+3] & 0xff;

		if(intVersion > 2 && (b[off+5] & 0x40) != 0)
			intExHeaderSize = 1;		//设置为1表示有扩展头

		boolID3v2Footer = (b[off+5] & 0x10) != 0;
		int size = synchSafeInt(b, off+6);
		size += 10;					// ID3 header:10bytes 
		return size;
	}

	//b[off..]不含ID3v2 头(10 bytes)
	public void parseID3V2(byte[] b, int off) {
		int max_size = b.length;
		int pos = off;
		if(intExHeaderSize == 1) {
			intExHeaderSize = synchSafeInt(b, off);
			pos += intExHeaderSize;
		}
		max_size -= 10;		//1 frame header: 10 bytes
		if(boolID3v2Footer)
			max_size -= 10;

		//System.out.println("ID3 v2." + intVersion);
		while(pos < max_size)
			pos += getText(b, pos, max_size);
	}

	public static int synchSafeInt(byte[] b, int off) {
		int i = (b[off] & 0x7f) << 21;
		i |= (b[off+1] & 0x7f) << 14;
		i |= (b[off+2] & 0x7f) << 7;
		i |=  b[off+3] & 0x7f;
		return i;
	}

	private int makeInt(byte[] b, int off, int len) {
		int i, ret = b[off] & 0xff;
		for (i = 1; i < len; i++) {
			ret <<= 8;
			ret |= b[off + i] & 0xff;
		}
		return ret;
	}

	private int getText(byte[] b, int off, int max_size)  {
		int id_part = 4, frame_header = 10;
		if(intVersion == 2) {
			id_part = 3;
			frame_header = 6;
		}
		String id = new String(b, off, id_part);
		off += id_part;

		int fsize, len;
		fsize = len = makeInt(b, off, id_part);
		off += id_part;		// frame size = frame id bytes
		if (intVersion > 2)
			off += 2;		// flag: 2 bytes

		int enc = b[off];
		len--;				// Text encoding: 1 byte
		off++;				// Text encoding: 1 byte
		if (len <= 0 || off + len > max_size || enc < 0 || enc >= TEXT_ENCODING.length)
			return fsize + frame_header;
		try {
			switch(id.hashCode()) {
			case 83378:		//TT2 v2.2
			case 2575251:	//TIT2  标题
				if (strTitle == null)
					strTitle = new String(b, off, len, TEXT_ENCODING[enc]).trim();
				break;
			case 83552:
			case 2590194:	//TYER  发行年
				if (strYear == null)
					strYear = new String(b, off, len, TEXT_ENCODING[enc]).trim();
				break;
			case 2569358:	//TCON  流派
				break;
			case 82815:
			case 2567331:	//TALB  唱片集
				if (strAlbum == null)
					strAlbum = new String(b, off, len, TEXT_ENCODING[enc]).trim();
				break;
			case 83253:
			case 2581512:	//TPE1  艺术家
				if (strArtist == null)
					strArtist = new String(b, off, len, TEXT_ENCODING[enc]).trim();
				break;
			case 2583398:	//TRCK  音轨
				break;
			/*case 2614438:	//USLT  歌词
				off += 4;	//Languge: 4 bytes
				len -= 4;
				strLyrics = new String(b, off, len, TEXT_ENCODING[enc]);
				break;*/
			}
		} catch (UnsupportedEncodingException e) {
			return fsize + frame_header;
		} finally {
			id = null;
		}
		return fsize + frame_header;
	}
}