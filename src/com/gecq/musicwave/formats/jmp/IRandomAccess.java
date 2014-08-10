package com.gecq.musicwave.formats.jmp;

/*
* IrandomAccess.java -- 随机访问文件接口
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
* along with this program.&nbsp; If not, see <http://www.gnu.org/licenses/>.
*/

public interface IRandomAccess {
	public int read() throws Exception;
	public int read(byte b[]) throws Exception;
	public int read(byte b[], int off, int len) throws Exception;
	public int dump(int src_off, byte b[], int dst_off, int len) throws Exception;
	public void seek(long pos) throws Exception;
	public long length();
	public long getFilePointer();
	public void close();
}

