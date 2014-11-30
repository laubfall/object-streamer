package de.ludwig.objstreamer;

import java.util.Collection;
import java.util.Iterator;

import org.junit.Assert;
import org.junit.Test;

import de.ludwig.objstreamer.testobjects.A;
import de.ludwig.objstreamer.testobjects.B;
import de.ludwig.objstreamer.testobjects.C;

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
		Iterator<ObjStreamer> stringListIter = list.iterator();
		while(stringListIter.hasNext()){
			ObjStreamer osNext = stringListIter.next();
			Object objValue = osNext.objValue(ObjStreamer.ROOT_PROPERTY);
			Assert.assertNotNull(objValue);
		}
		
		
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
	
	@Test
	public void testThree(){
		B b = new B();
		ObjStreamer os = new ObjStreamer(b);
		Collection<ObjStreamer> keySet = os.keySet("bMap");
		Assert.assertNotNull(keySet);
		Assert.assertFalse(keySet.isEmpty());
		Assert.assertEquals(2, keySet.size());
		
		for(ObjStreamer cOs : keySet){
			ObjectChunk mapValueChunk = cOs.findChunkByPropertyPath(ObjStreamer.MAP_VALUE_PROPERTY);
			Assert.assertNotNull(mapValueChunk);
		}
		
		keySet = os.keySet("bMap2");
		Assert.assertNotNull(keySet);
		Assert.assertFalse(keySet.isEmpty());
		Assert.assertEquals(1, keySet.size());
	}
	
	/**
	 * Array Test
	 */
	@Test
	public void testFour(){
		A a = new A();
		ObjStreamer os = new ObjStreamer(a);
		ObjStreamer[] bArray = os.array("bArray");
		Assert.assertNotNull(bArray);
		Assert.assertEquals(1, bArray.length);
		final ObjectChunk col = bArray[0].findChunkByPropertyPath("co.l");
		Assert.assertNotNull(col);
		Assert.assertEquals("l", col.getFieldName());
		Assert.assertEquals("long", col.getFieldTypeNameFQN());
		Object fieldValue = col.getFieldValue();
		Assert.assertTrue(fieldValue instanceof Long);
		
		C c = new C();
		os = new ObjStreamer(c);
		ObjectChunk arrayChunk = os.findChunkByPropertyPath("charArray");
		Assert.assertNotNull(arrayChunk);
		Assert.assertNotNull(arrayChunk.getFieldValue());
		Assert.assertTrue(arrayChunk.getFieldValue() instanceof char[]);
		
		B b = new B();
		os = new ObjStreamer(b);
		arrayChunk = os.findChunkByPropertyPath("cArray");
		Assert.assertNotNull(arrayChunk);
		
		ObjStreamer[] array = os.array("cArray");
		Assert.assertNotNull(array);
		Assert.assertEquals(1, array.length);
		ObjectChunk lChunk = array[0].findChunkByPropertyPath("l");
		Assert.assertNotNull(lChunk);
	}
}
