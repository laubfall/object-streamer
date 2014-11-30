package de.ludwig.objstreamer.testobjects;

import java.util.ArrayList;
import java.util.List;

public class A {
	private int i = 123;
	
	private B b = new B();
	
	private List<String> stringList = new ArrayList<>();
	
	private List<B> bList = new ArrayList<>();
	
	private B [] bArray = {new B()};
	
	public A(){
		stringList.add("a");
		stringList.add("b");
		stringList.add("c");
		
		bList.add(new B());
		bList.add(new B());
	}
	
	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}

	public B getB() {
		return b;
	}

	public void setB(B b) {
		this.b = b;
	}

	public List<String> getStringList() {
		return stringList;
	}

	public void setStringList(List<String> stringList) {
		this.stringList = stringList;
	}

	public List<B> getbList() {
		return bList;
	}

	public void setbList(List<B> bList) {
		this.bList = bList;
	}

	public B[] getbArray() {
		return bArray;
	}

	public void setbArray(B[] bArray) {
		this.bArray = bArray;
	}
}
