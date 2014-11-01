package de.ludwig.objstreamer;

import java.util.Collection;
import java.util.Iterator;

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
		
		ObjectChunk chunk = os.findChunkByPropertyPath("b.d");
		System.out.println(chunk);
	}
	
	@Test
	public void testTwo(){
		final A a = new A();
		ObjStreamer os = new ObjStreamer(a);
		Collection<ObjStreamer> list = os.list("stringList");
		Assert.assertNotNull(list);
		Assert.assertFalse(list.isEmpty());
		Assert.assertEquals(3, list.size());
		
		list = os.list("bList");
		Assert.assertNotNull(list);
		Assert.assertFalse(list.isEmpty());
		Assert.assertEquals(2, list.size());
		
		Iterator<ObjStreamer> bIter = list.iterator();
		while(bIter.hasNext()){
			ObjStreamer nextB = bIter.next();
			Object objValue = nextB.objValue("d");
			Assert.assertNotNull(objValue);
		}
	}
}
