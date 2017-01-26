package com.gammarush.engine.math.vector;

public class Vector2f {

	public float x;
	public float y;
	
	public Vector2f() {
		this.x = 0;
		this.y = 0;
	}
	
	public Vector2f(Vector2f v) {
		this.x = v.x;
		this.y = v.y;
	}
	
	public Vector2f(Vector2i v) {
		this.x = v.x;
		this.y = v.y;
	}
	
	public Vector2f(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2f add(Vector2f v) {
		float x = this.x + v.x;
		float y = this.y + v.y;
		return new Vector2f(x, y);
	}
	
	public Vector2f add(float x, float y) {
		x += this.x;
		y += this.y;
		return new Vector2f(x, y);
	}
	
	public Vector2f sub(Vector2f v) {
		float x = this.x - v.x;
		float y = this.y - v.y;
		return new Vector2f(x, y);
	}
	
	public Vector2f sub(float x, float y) {
		x = this.x - x;
		y = this.y - y;
		return new Vector2f(x, y);
	}
	
	public Vector2f mult(Vector2f v) {
		float x = this.x * v.x;
		float y = this.y * v.y;
		return new Vector2f(x, y);
	}
	
	public Vector2f mult(float v) {
		float x = v * this.x;
		float y = v * this.y;
		return new Vector2f(x, y);
	}
	
	public Vector2f mult(float x, float y) {
		x *= this.x;
		y *= this.y;
		return new Vector2f(x, y);
	}
	
	public float dot(Vector2f v) {
		return x * v.x + y * v.y;
	}
	
	public float dot(float x, float y) {
		return this.x * x + this.y * y;
	}
	
	public Vector2f reflect(Vector2f v) {
		float dot = this.dot(v);
		float x = this.x - 2 * dot * v.x;
		float y = this.y - 2 * dot * v.y;
		return new Vector2f(x, y);
	}
	
	public Vector2f normalize() {
		float d = magnitude();
		float x = this.x / d;
		float y = this.y / d;
		return new Vector2f(x, y);
	}
	
	public float magnitude() {
		return (float) Math.sqrt(x * x + y * y);
	}
	
	public Vector2i toInt() {
		return new Vector2i((int) x, (int) y);
	}
	
	public void print() {
		System.out.println("X: " + x + ", Y: " + y);
	}
	
}
