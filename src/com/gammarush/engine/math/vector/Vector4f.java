package com.gammarush.engine.math.vector;

public class Vector4f {
	
	public float x;
	public float y;
	public float z;
	public float w;

	public Vector4f() {
		x = 0;
		y = 0;
		z = 0;
		w = 0;
	}
	
	public Vector4f(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public Vector4f add(Vector4f v) {
		float x = this.x + v.x;
		float y = this.y + v.y;
		float z = this.z + v.z;
		float w = this.w + v.w;
		return new Vector4f(x, y, z, w);
	}
	
	public Vector4f add(float x, float y, float z, float w) {
		x += this.x;
		y += this.y;
		z += this.z;
		w += this.w;
		return new Vector4f(x, y, z, w);
	}
	
	public Vector4f sub(Vector4f v) {
		float x = this.x - v.x;
		float y = this.y - v.y;
		float z = this.z - v.z;
		float w = this.w - v.w;
		return new Vector4f(x, y, z, w);
	}
	
	public Vector4f sub(float x, float y, float z, float w) {
		x -= this.x;
		y -= this.y;
		z -= this.z;
		w -= this.w;
		return new Vector4f(x, y, z, w);
	}
	
	public float dot(Vector4f v) {
		return x * v.x + y * v.y + z * v.z + w * v.w;
	}
	
	public float dot(float x, float y, float z, float w) {
		return this.x * x + this.y * y + this.z * z + this.w * w;
	}
	
	public Vector4f normalize() {
		float d = length();
		float x = this.x / d;
		float y = this.y / d;
		float z = this.z / d;
		float w = this.w / d;
		return new Vector4f(x, y, z, w);
	}
	
	public float length() {
		return (float) Math.sqrt(x * x + y * y + z * z + w * w);
	}
	
	public void print() {
		System.out.println("X: " + x + ", Y: " + y + ", Z: " + z + ", W: " + w);
	}

}
