package de.ludwig.objstreamer.testobjects;

public class B {
	private char c = 'a';
	
	private double d = 3.0;
	
	private C co = new C();

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
}
