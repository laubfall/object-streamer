package de.ludwig.objstreamer.testobjects;

import java.util.HashMap;
import java.util.Map;

public class B {
	private char c = 'a';
	
	private double d = 3.0;
	
	private C co = new C();
	
	private Map<String, C> bMap = new HashMap<>();
	
	private Map<C, Integer> bMap2 = new HashMap<>();

	private C [] cArray = {new C()};
	
	public B(){
		bMap.put("c1", new C());
		bMap.put("c2", new C());
		
		bMap2.put(new C(), 1);
	}
	
	public char getC() {
		return c;
	}

	public void setC(char c) {
		this.c = c;
	}

	public double getD() {
		return d;
	}

	public void setD(double d) {
		this.d = d;
	}

	public C getCo() {
		return co;
	}

	public void setCo(C co) {
		this.co = co;
	}

	public Map<String, C> getbMap() {
		return bMap;
	}

	public void setbMap(Map<String, C> bMap) {
		this.bMap = bMap;
	}

	public C[] getcArray() {
		return cArray;
	}

	public void setcArray(C[] cArray) {
		this.cArray = cArray;
	}
}
