package de.ludwig.objstreamer;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import de.ludwig.objstreamer.testobjects.C;

public class WriteReadTest {
	@Test
	public void w1() throws IOException{
		final ObjStreamer os = new ObjStreamer(new C());
		final ObjStreamWriter osw = new ObjStreamWriter();
		final File out = File.createTempFile("writeTest1", ".osw");
		System.out.println(out.getAbsolutePath());
		osw.write(os, out);
	}
}
