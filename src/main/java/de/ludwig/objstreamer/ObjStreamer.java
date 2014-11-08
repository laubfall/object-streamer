package de.ludwig.objstreamer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static de.ludwig.objstreamer.ObjectChunk.genFieldTypeNameFQN;

/**
 * 
 * 
 * @author Daniel
 * 
 */
public class ObjStreamer {

	public static final String MAP_KEY_PROPERTY = "_INTERNAL_OBJSTREAMER_MAP_KEY";

	/**
	 * Synthetic field name for map values. Chunks get this name
	 * if they represent a map value. 
	 */
	public static final String MAP_VALUE_PROPERTY = "_INTERNAL_OBJSTREAMER_MAP_VALUE";
	
	/**
	 * Synthetic field name. Used to operate on the root chunk of the current
	 * ObjStreamer instance. Use this for example if you get a list of String or Integers
	 * and you want to get the values out of the list of ObjStreamers.
	 */
	public static final String ROOT_PROPERTY = "_INTERNAL_OBJSTREAMER_ROOT_VALUE";
	
	/**
	 * The root Object from where you can start querying other objects.
	 */
	private ObjectChunk objectGraphRoot;

	public ObjStreamer(final Object obj) {
		// start parsing the object and create chunks of it.
		processField(obj, null);
	}

	/**
	 * Used for Objects that are Collections. Every Element of the Collection is
	 * handled as an ObjStreamer.
	 * 
	 * @param chunk
	 *            part of a Collection.
	 */
	private ObjStreamer(final ObjectChunk chunk) {
		objectGraphRoot = chunk;
	}

	public Object objValue(final String propertyPath) {
		final ObjectChunk chunk = findChunkByPropertyPath(propertyPath);
		if (chunk == null) {
			// TODO not a good idea to return null. If the value is really null
			// then we cannot distinguish if the value is null or if there no
			// chunk.
			return null;
		}

		return chunk.getFieldValue();
	}

	public int intValue(final String property) {
		Object objValue = objValue(property);
		return 0;
	}
	
	public ObjStreamer[] array(final String property){
		final ObjectChunk array = findChunkByPropertyPath(property);
		Set<ObjectChunk> childs = array.getChilds();
		final ObjStreamer[] streams = new ObjStreamer[childs.size()];
		int cnt = 0;
		final Iterator<ObjectChunk> iterator = childs.iterator();
		while(iterator.hasNext()){
			streams[cnt] = new ObjStreamer(iterator.next());
			cnt++;
		}
		
		return streams;
	}

	public Collection<ObjStreamer> list(final String property) {
		final ObjectChunk collection = findChunkByPropertyPath(property);
		final List<ObjStreamer> collectionParts = new ArrayList<>();
		for (ObjectChunk oc : collection.getChilds()) {
			collectionParts.add(new ObjStreamer(oc));
		}
		return collectionParts;
	}

	public Collection<ObjStreamer> keySet(final String property){
		final ObjectChunk mapChunk = findChunkByPropertyPath(property);
		final List<ObjStreamer> collectionParts = new ArrayList<>();
		for (ObjectChunk oc : mapChunk.getChilds()) {
			if(oc.getFieldName().equals(MAP_KEY_PROPERTY)){
				collectionParts.add(new ObjStreamer(oc));
			}
		}
		return collectionParts;
	}
	
	public final ObjectChunk findChunkByPropertyPath(final String path) {
		if(ROOT_PROPERTY.equals(path)){
			return objectGraphRoot;
		}
		final PropertyPath pp = new PropertyPath(path);
		return findChunkByPropertyPath(pp.next(), pp, objectGraphRoot);
	}

	private final ObjectChunk findChunkByPropertyPath(final String propName,
			final PropertyPath propPath, final ObjectChunk chunk) {

		for (ObjectChunk oc : chunk.getChilds()) {
			if (oc.getFieldName().equals(propName)) {
				if (propPath.hasNext() == false) {
					return oc;
				}

				return findChunkByPropertyPath(propPath.next(), propPath, oc);
			}
		}

		return null;
	}

	private final void processField(Object source, ObjectChunk parent) {
		ObjectChunk currParent = parent;
		if (parent == null) {
			objectGraphRoot = new ObjectChunk();
			currParent = objectGraphRoot;
		}
		final Field[] declaredFields = source.getClass().getDeclaredFields();
		for (Field f : declaredFields) {
			processField(f, source, currParent);
		}
	}

	private final void processField(Field field, Object source,
			ObjectChunk parent) {
		try {
			field.setAccessible(true);
			final Object fieldValue = field.get(source);
			processField(fieldValue, field.getName(), genFieldTypeNameFQN(field.getDeclaringClass()), parent);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new ObjectChunkProcessingRtException(e, parent);
		}
	}

	private final ObjectChunk processField(Object fieldValue, String fieldName,
			String fieldTypeClassName, ObjectChunk parent) {
		final ObjectChunk childChunk = new ObjectChunk();
		parent.addChunk(childChunk);
		childChunk.setFieldName(fieldName);
		childChunk.setFieldTypeNameFQN(fieldTypeClassName);

		if (fieldValue == null) {
			return childChunk;
		}

		if (isSimpleType(fieldValue)) {
			// leaf node, stop traversal
			childChunk.setFieldValue(fieldValue);
		} else if (isCollection(fieldValue)) {
			final Collection<?> coll = (Collection<?>) fieldValue;
			final Iterator<?> iterator = coll.iterator();
			while (iterator.hasNext()) {
				Object next = iterator.next();
				// the child chunk is the collection
				processField(next, "", genFieldTypeNameFQN(next.getClass()),
						childChunk);
			}
		} else if(isArray(fieldValue) && isSimpleType(fieldValue.getClass().getComponentType()) == false){
			final Object[] array = (Object[]) fieldValue;
			for(int i = 0; i < array.length; i++){
				Object next = array[i];
				processField(next, "", genFieldTypeNameFQN(next.getClass()),
						childChunk);
			}
		} else if(isArray(fieldValue) && isSimpleType(fieldValue.getClass().getComponentType())){
			childChunk.setFieldValue(fieldValue);
		} else if (isMap(fieldValue)) {
			final Map<?, ?> map = (Map<?, ?>) fieldValue;
			Set<?> keySet = map.keySet();
			for(Object key : keySet){
				final ObjectChunk keyChunk = processField(key, MAP_KEY_PROPERTY, genFieldTypeNameFQN(key.getClass()), childChunk);
				// Assign child (key chunk) to a var and process the map value
				// then use the child as the parent for the map values.
				Object mapValue = map.get(key);
				processField(mapValue, MAP_VALUE_PROPERTY, genFieldTypeNameFQN(mapValue.getClass()), keyChunk);
			}
		} else {
			// process further in object graph, continue traversal
			processField(fieldValue, childChunk);
		}
		
		return childChunk;
	}

	private boolean isCollection(Object obj) {
		Class<? extends Object> class1 = obj.getClass();
		if (Collection.class.isAssignableFrom(class1)) {
			return true;
		}
		return false;
	}
	
	private boolean isArray(Object obj){
		return obj.getClass().isArray();
	}

	private boolean isMap(Object obj) {
		Class<? extends Object> class1 = obj.getClass();
		if (Map.class.isAssignableFrom(class1)) {
			return true;
		}
		return false;
	}

	private boolean isSimpleType(Object obj) {
		return isSimpleType(obj.getClass());
	}

	private boolean isSimpleType(final Class<? extends Object> class1) {
		if (class1.isPrimitive()) {
			return true;
		}

		if (Integer.class.isAssignableFrom(class1)
				|| String.class.isAssignableFrom(class1)
				|| Double.class.isAssignableFrom(class1)
				|| Long.class.isAssignableFrom(class1)
				|| Character.class.isAssignableFrom(class1)
				|| Byte.class.isAssignableFrom(class1)) {
			return true;
		}

		return false;
	}
}
