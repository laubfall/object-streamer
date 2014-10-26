package de.ludwig.objstreamer.testobjects;

public class A {
	private int i = 123;
			
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

	private B b = new B();
}
