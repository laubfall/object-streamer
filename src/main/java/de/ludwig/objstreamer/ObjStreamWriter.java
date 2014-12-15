package de.ludwig.objstreamer;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * objectchunk parent:3214 childs:3443 fieldName:334 fqn:2312 values ....
 * objectchunk
 * 
 * @author Daniel
 * 
 */
public class ObjStreamWriter {

	public void write(ObjStreamer stream, File out) {
		RandomAccessFile raf = null;
		
		try {
			raf = new RandomAccessFile(out, "rw");

			long currentChunkStartPtr = 0l;
			ObjectChunk rootChunk = stream.getObjectGraphRoot();

			raf.writeBytes(FileConstants.CHUNK_START.val);
			raf.writeBytes(FileConstants.PARENT.val);
			raf.writeLong(-1);
			raf.writeBytes(FileConstants.FIELD_NAME.val);
			raf.writeLong(-1);
			// TODO further params

			// write values start
			raf.writeBytes(FileConstants.VALUES_START.val);
			// write values
			long ptr = raf.getFilePointer();
			raf.writeBytes(rootChunk.getFieldName());
			long endPtr = raf.getFilePointer();
			
			// TODO calculate write Position
			long writePos = currentChunkStartPtr
					+ FileConstants.CHUNK_START.byteLength()
					+ FileConstants.PARENT.byteLength() + 8
					+ FileConstants.FIELD_NAME.byteLength();
			raf.seek(writePos);
			raf.writeLong(ptr);
			raf.seek(endPtr);
			// <-- value written and reference made
			
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
	}
}
