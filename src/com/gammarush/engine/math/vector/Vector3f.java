package com.gammarush.engine.math.vector;

public class Vector3f {
	
	public float x;
	public float y;
	public float z;

	public Vector3f() {
		x = 0;
		y = 0;
		z = 0;
	}
	
	public Vector3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector3f add(Vector3f v) {
		float x = this.x + v.x;
		float y = this.y + v.y;
		float z = this.z + v.z;
		return new Vector3f(x, y, z);
	}
	
	public Vector3f add(float x, float y, float z) {
		x += this.x;
		y += this.y;
		z += this.z;
		return new Vector3f(x, y, z);
	}
	
	public Vector3f sub(Vector3f v) {
		float x = this.x - v.x;
		float y = this.y - v.y;
		float z = this.z - v.z;
		return new Vector3f(x, y, z);
	}
	
	public Vector3f sub(float x, float y, float z) {
		x -= this.x;
		y -= this.y;
		z -= this.z;
		return new Vector3f(x, y, z);
	}
	
	public Vector3f mult(Vector3f v) {
		float x = this.x * v.x;
		float y = this.y * v.y;
		float z = this.z * v.z;
		return new Vector3f(x, y, z);
	}
	
	public Vector3f mult(float v) {
		float x = v * this.x;
		float y = v * this.y;
		float z = v * this.z;
		return new Vector3f(x, y, z);
	}
	
	public Vector3f mult(float x, float y, float z) {
		x *= this.x;
		y *= this.y;
		z *= this.z;
		return new Vector3f(x, y, z);
	}
	
	public float dot(Vector3f v) {
		return x * v.x + y * v.y + z * v.z;
	}
	
	public float dot(float x, float y, float z) {
		return this.x * x + this.y * y + this.z * z;
	}
	
	public Vector3f cross(Vector3f v) {
		float x = this.y * v.z - this.z * v.y;
		float y = this.z * v.x - this.x * v.z;
		float z = this.x * v.y - this.y * v.x;
		return new Vector3f(x, y, z);
	}
	
	public Vector3f cross(float x, float y, float z) {
		x = this.y * z - this.z * y;
		y = this.z * x - this.x * z;
		z = this.x * y - this.y * x;
		return new Vector3f(x, y, z);
	}
	
	public Vector3f normalize() {
		float d = magnitude();
		float x = this.x / d;
		float y = this.y / d;
		float z = this.z / d;
		return new Vector3f(x, y, z);
	}
	
	public float magnitude() {
		return (float) Math.sqrt(x * x + y * y + z * z);
	}
	
	public void print() {
		System.out.println("X: " + x + ", Y: " + y + ", Z: " + z);
	}

}
