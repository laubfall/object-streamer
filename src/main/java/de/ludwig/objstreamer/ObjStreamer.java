package de.ludwig.objstreamer;

import java.lang.reflect.Field;
import java.util.List;


public class ObjStreamer {

	private ObjectChunk objectGraphRoot;
	
	public ObjStreamer(final Object obj){
		// start parsing the object and create chunks of it.
		processField(obj, null);
	}
	
	public Object objValue(final String propertyPath){
		final ObjectChunk chunk = findChunkByPropertyPath(propertyPath);
		if(chunk == null){
			return null;
		}
		
		return chunk.getFieldValue();
	}
	
	public int intValue(final String property){
		
		return 0;
	}
	
	public List<ObjectChunk> list(final String property){
		return null;
	}
	
	private final ObjectChunk findChunkByPropertyPath(final String path){
		final PropertyPath pp = new PropertyPath(path);
		return findChunkByPropertyPath(pp.next(), pp, objectGraphRoot);
	}
	
	private final ObjectChunk findChunkByPropertyPath(final String propName, final PropertyPath propPath, final ObjectChunk chunk){

			for(ObjectChunk oc : chunk.getChilds()){
				if(oc.getFieldName().equals(propName)){
					if(propPath.hasNext() == false){
						return oc;
					}
					
					return findChunkByPropertyPath(propPath.next(), propPath, oc);
				}
			}
	
		return null;
	}
	
	private final void processField(Object source, ObjectChunk parent){		
		ObjectChunk currParent = parent;
		if(parent == null){
			objectGraphRoot = new ObjectChunk();
			currParent = objectGraphRoot;
		}
		final Field[] declaredFields = source.getClass().getDeclaredFields();
		for(Field f : declaredFields){
			processField(f, source, currParent);
		}
	}
	
	private final void processField(Field field, Object source, ObjectChunk parent){
		try {
			field.setAccessible(true);
			final Object fieldValue = field.get(source);
			final ObjectChunk childChunk = new ObjectChunk();
			parent.addChunk(childChunk);
			childChunk.setFieldName(field.getName());
			childChunk.setFieldTypeNameFQN(field.getDeclaringClass().getCanonicalName());
			
			if(fieldValue == null){
				return;
			}
			
			if(isSimpleType(fieldValue)){
				// leaf node, stop traversal
				childChunk.setFieldValue(fieldValue);
			} else {
				// process further in object graph, continue traversal
				processField(fieldValue, childChunk);
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new ObjectChunkProcessingRtException(e, parent);
		}
	}
	
	private boolean isSimpleType(Object obj){
		final Class<? extends Object> class1 = obj.getClass();
		if(class1.isPrimitive()){
			return true;
		}
		
		if(Integer.class.isAssignableFrom(class1) ||
			String.class.isAssignableFrom(class1) ||
			Double.class.isAssignableFrom(class1) ||
			Long.class.isAssignableFrom(class1) ||
			Character.class.isAssignableFrom(class1) ||
			Byte.class.isAssignableFrom(class1)){
			return true;
		}
		
		return false;
	}
}
