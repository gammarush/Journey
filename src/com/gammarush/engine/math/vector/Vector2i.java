package com.gammarush.engine.math.vector;

public class Vector2i {

	public int x;
	public int y;
	
	public Vector2i() {
		this.x = 0;
		this.y = 0;
	}
	
	public Vector2i(Vector2i v) {
		this.x = v.x;
		this.y = v.y;
	}
	
	public Vector2i(Vector2f v) {
		this.x = (int) v.x;
		this.y = (int) v.y;
	}
	
	public Vector2i(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2i add(Vector2i v) {
		int x = this.x + v.x;
		int y = this.y + v.y;
		return new Vector2i(x, y);
	}
	
	public Vector2i add(int x, int y) {
		x += this.x;
		y += this.y;
		return new Vector2i(x, y);
	}
	
	public Vector2i sub(Vector2i v) {
		int x = this.x - v.x;
		int y = this.y - v.y;
		return new Vector2i(x, y);
	}
	
	public Vector2i sub(int x, int y) {
		x -= this.x;
		y -= this.y;
		return new Vector2i(x, y);
	}
	
	public Vector2i mult(Vector2i v) {
		int x = this.x * v.x;
		int y = this.y * v.y;
		return new Vector2i(x, y);
	}
	
	public Vector2i mult(int v) {
		int x = v * this.x;
		int y = v * this.y;
		return new Vector2i(x, y);
	}
	
	public Vector2i mult(int x, int y) {
		x *= this.x;
		y *= this.y;
		return new Vector2i(x, y);
	}
	
	public int dot(Vector2i v) {
		return x * v.x + y * v.y;
	}
	
	public int dot(int x, int y) {
		return this.x * x + this.y * y;
	}
	
	public Vector2f normalize() {
		float d = length();
		float x = this.x / d;
		float y = this.y / d;
		return new Vector2f(x, y);
	}
	
	public float length() {
		return (float) Math.sqrt(x * x + y * y);
	}
	
	public Vector2f toFloat() {
		return new Vector2f(x, y);
	}
	
	public void print() {
		System.out.println("X: " + x + ", Y: " + y);
	}

}
