package de.ludwig.objstreamer;

import org.junit.Assert;
import org.junit.Test;

import de.ludwig.objstreamer.testobjects.A;

public class SimpleTypeTest {
	@Test
	public void testOne(){
		final A a = new A();
		ObjStreamer os = new ObjStreamer(a);
		Object objValue = os.objValue("i");
		Assert.assertNotNull(objValue);
		
		objValue = os.objValue("b.d");
		Assert.assertNotNull(objValue);
	}
}
