package de.ludwig.objstreamer;

import java.lang.reflect.Field;
import java.util.List;


public class ObjStreamer {

	private ObjectChunk objectGraphRoot;
	
	public ObjStreamer(final Object obj){
		// start parsing the object and create chunks of it.
		processField(obj, null);
	}
	
	public int intValue(final String property){
		
		return 0;
	}
	
	public List<ObjectChunk> list(final String property){
		return null;
	}
	
	private final ObjectChunk findChunkByPropertyPath(final String path){
		final String[] propertyNames = path.split("[.]");
		return findChunkByPropertyPath(propertyNames[0], "", objectGraphRoot);
	}
	
	private final ObjectChunk findChunkByPropertyPath(final String propName, final String subPath, final ObjectChunk chunk){

			for(ObjectChunk oc : chunk.getChilds()){
				if(oc.getFieldName().equals(propName)){
					
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
			final Object fieldValue = field.get(source);
			final ObjectChunk childChunk = new ObjectChunk();
			parent.addChunk(childChunk);
			childChunk.setFieldName(field.getName());
			childChunk.setFieldTypeNameFQN(field.getDeclaringClass().getCanonicalName());
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
		return true;
	}
}
