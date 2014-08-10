
package com.gecq.musicwave.formats.mp3;

import java.io.Serializable;
import java.nio.ByteBuffer;

public class ByteBufferUtils implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static String extractNullTerminatedString(ByteBuffer bb) {
        int start = bb.position();

        byte[] buffer = new byte[bb.remaining()];

        bb.get(buffer);

        String s = new String(buffer);
        int nullPos = s.indexOf(0);

        s = s.substring(0, nullPos);

        bb.position(start + s.length() + 1);

        return s;
    }

}
