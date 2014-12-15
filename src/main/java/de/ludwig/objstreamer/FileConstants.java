package de.ludwig.objstreamer;

enum FileConstants {
	CHUNK_START("oc"), PARENT("parent"), FIELD_NAME("fieldName"), VALUES_START(
			"values");
	String val;

	private FileConstants(String val) {
		this.val = val;
	}

	public int byteLength() {
		return val.getBytes().length;
	}
}