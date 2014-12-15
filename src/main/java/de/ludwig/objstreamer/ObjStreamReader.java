package de.ludwig.objstreamer;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class ObjStreamReader {
	public ObjStreamer read(File in){
		RandomAccessFile raf = null;
		try {
			raf = new RandomAccessFile(in, "r");
			byte[] b = new byte[FileConstants.CHUNK_START.byteLength()];
			raf.read(b);
			
			final String strVal = new String(b);
			if(FileConstants.CHUNK_START.val.equals(strVal) == false){
				throw new RuntimeException("invalid file");
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(raf != null){
				try {
					raf.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
		return null;
	}
}
