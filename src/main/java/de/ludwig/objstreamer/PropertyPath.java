package de.ludwig.objstreamer;

import java.util.Iterator;

class PropertyPath implements Iterable<String>, Iterator<String> {

	public static final String PATH_SEPERATOR = ".";

	private String [] pathNodes;
	
	private int arrPnt = 0;
	
	public PropertyPath(String path) {
		super();
		if(path.contains(PATH_SEPERATOR) == false){
			pathNodes = new String[]{path};
		} else {
			pathNodes = path.split("["+ PATH_SEPERATOR +"]");
		}
	}

	@Override
	public Iterator<String> iterator() {
		return this;
	}

	@Override
	public boolean hasNext() {
		return arrPnt >= pathNodes.length == false;
	}

	@Override
	public String next() {
		final String node = pathNodes[arrPnt];
		arrPnt++;
		return node;
	}

	@Override
	public void remove() {
		// NOOP
	}

}
