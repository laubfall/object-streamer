package de.ludwig.objstreamer;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.EnumSet;

/**
 * objectchunk parent:3214 childs:3443 fieldName:334 fqn:2312 values ....
 * objectchunk
 * 
 * Every ObjectChunk is serialized as a Head-Section and a Value-Section. The
 * Head-Section holds the references to the values stored in the Value-Section.
 * 
 * The Head-Section starts with {@link FileConstants#CHUNK_START}. The
 * Value-Section starts with {@link FileConstants#VALUES_START}.
 * 
 * @author Daniel
 * 
 */
public class ObjStreamWriter {

	private final static EnumSet<FileConstants> FIELDS = EnumSet.of(
			FileConstants.PARENT, FileConstants.FIELD_NAME);

	public void write(ObjStreamer stream, File out) {
		RandomAccessFile raf = null;

		try {
			raf = new RandomAccessFile(out, "rw");

			long currentChunkStartPtr = 0l;
			ObjectChunk rootChunk = stream.getObjectGraphRoot();

			write(raf, currentChunkStartPtr, rootChunk);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (raf != null) {
				try {
					raf.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	private void write(RandomAccessFile raf, long currentChunkStartPtr,
			ObjectChunk chunk) throws IOException {
		raf.writeBytes(FileConstants.CHUNK_START.val);
		raf.writeBytes(FileConstants.PARENT.val);
		raf.writeLong(-1);
		raf.writeBytes(FileConstants.FIELD_NAME.val);
		raf.writeLong(-1);
		// TODO further params

		// write values start
		raf.writeBytes(FileConstants.VALUES_START.val);

		// write values, store value of the position where we write the value to
		long ptr = raf.getFilePointer();
		String fieldName = chunk.getFieldName();
		long endPtr = raf.getFilePointer();
		if (fieldName != null) {
			raf.writeBytes(fieldName);

			// calculate write Position for reference write of the field
			// "field name".
			long writePos = currentChunkStartPtr
					+ FileConstants.CHUNK_START.byteLength()
					+ FileConstants.PARENT.byteLength() + 8
					+ FileConstants.FIELD_NAME.byteLength();
			// reference position
			raf.seek(writePos);
			// finally write the reference, overwrite the default -1
			raf.writeLong(ptr);
			raf.seek(endPtr);
		}

		// <-- value written and reference made

		// process childs
		for (ObjectChunk oc : chunk.getChilds()) {
			write(raf, endPtr, oc);
		}
	}

	/**
	 * Write the given field and the reference to the head section.
	 * 
	 * @param raf
	 * @param chunk
	 * @param currentChunkStartPtr
	 * @param fieldNum
	 * @throws IOException
	 */
	private void write(RandomAccessFile raf, ObjectChunk chunk,long currentChunkStartPtr) throws IOException {
		// write values, store value of the position where we write the value to
//		long ptr = raf.getFilePointer();
		
		for(FileConstants fc : FIELDS){
			writeFieldValueAndRef(raf, chunk, fc, currentChunkStartPtr);
		}

	}

	private void writeFieldValueAndRef(RandomAccessFile raf, ObjectChunk chunk,
			FileConstants field, long currentChunkStartPtr) throws IOException {
		switch (field) {
		case FIELD_NAME:
			String fieldName = chunk.getFieldName();
			if (fieldName != null) {
				long ptr = raf.getFilePointer();
				raf.writeBytes(fieldName);
				writeFieldRef(raf, currentChunkStartPtr, field, ptr);
			}
			break;

		case PARENT:
			chunk.getParent();
			break;
		default:
			break;
		}
	}
	
	private void writeFieldRef(RandomAccessFile raf, long currentChunkStartPtr, FileConstants field, long valuePtr) throws IOException{
		final long endPtr = raf.getFilePointer();
		final long computeWriteRefOffset = computeWriteRefOffset(currentChunkStartPtr, field);
		
		// reference position
		raf.seek(computeWriteRefOffset);
		// finally write the reference, overwrite the default -1
		raf.writeLong(valuePtr);
		raf.seek(endPtr);
	}
	
	private long computeWriteRefOffset(long currentChunkStartPtr, FileConstants field){
		long writePos = currentChunkStartPtr
				+ FileConstants.CHUNK_START.byteLength();
		
		for(FileConstants fc : FIELDS){
			writePos += fc.byteLength();
			if(fc.equals(field)){
				break;
			}
			writePos += 8;
		}
		
		return writePos;
	}
}
