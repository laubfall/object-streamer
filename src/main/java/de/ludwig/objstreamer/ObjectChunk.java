package de.ludwig.objstreamer;

import java.io.Serializable;
import java.util.Set;

public class ObjectChunk implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2574633110996204433L;

	/**
	 * java.lang.reflect.Field.getName()...
	 */
	private String fieldName;
	
	/**
	 * simple Type name or something like java.lang.Integer...
	 * 
	 * Used for getting the value of the chunk.
	 */
	private String fieldTypeNameFQN;
	
	/**
	 * The owning chunk, only null if this chunk is the root of the object graph.
	 */
	private ObjectChunk parent;
	
	private Set<ObjectChunk> childs;
	
	private Object fieldValue;

	public final void addChunk(final ObjectChunk child){
		if(child == null){
			throw new ObjectChunkProcessingRtException("child to add is not allowed to be null", this);
		}
		
		childs.add(child);
		child.parent = this;
	}
	
	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldTypeNameFQN() {
		return fieldTypeNameFQN;
	}

	public void setFieldTypeNameFQN(String fieldTypeNameFQN) {
		this.fieldTypeNameFQN = fieldTypeNameFQN;
	}

	public Object getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(Object fieldValue) {
		this.fieldValue = fieldValue;
	}

	public ObjectChunk getParent() {
		return parent;
	}

	public Set<ObjectChunk> getChilds() {
		return childs;
	}

	@Override
	public String toString() {
		return "ObjectChunk [fieldName=" + fieldName + ", fieldTypeNameFQN="
				+ fieldTypeNameFQN + ", parent=" + parent + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((fieldName == null) ? 0 : fieldName.hashCode());
		result = prime * result + ((parent == null) ? 0 : parent.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ObjectChunk other = (ObjectChunk) obj;
		if (fieldName == null) {
			if (other.fieldName != null)
				return false;
		} else if (!fieldName.equals(other.fieldName))
			return false;
		if (parent == null) {
			if (other.parent != null)
				return false;
		} else if (!parent.equals(other.parent))
			return false;
		return true;
	}
}
