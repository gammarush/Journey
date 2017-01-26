package com.gammarush.engine.math.vector;

public class Vector3i {

	public int x;
	public int y;
	public int z;

	public Vector3i() {
		x = 0;
		y = 0;
		z = 0;
	}
	
	public Vector3i(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector3i add(Vector3i v) {
		int x = this.x + v.x;
		int y = this.y + v.y;
		int z = this.z + v.z;
		return new Vector3i(x, y, z);
	}
	
	public Vector3i add(int x, int y, int z) {
		x += this.x;
		y += this.y;
		z += this.z;
		return new Vector3i(x, y, z);
	}
	
	public Vector3i sub(Vector3i v) {
		int x = this.x - v.x;
		int y = this.y - v.y;
		int z = this.z - v.z;
		return new Vector3i(x, y, z);
	}
	
	public Vector3i sub(int x, int y, int z) {
		x -= this.x;
		y -= this.y;
		z -= this.z;
		return new Vector3i(x, y, z);
	}
	
	public int dot(Vector3i v) {
		return x * v.x + y * v.y + z * v.z;
	}
	
	public int dot(int x, int y, int z) {
		return this.x * x + this.y * y + this.z * z;
	}
	
	public Vector3i cross(Vector3i v) {
		int x = this.y * v.z - this.z * v.y;
		int y = this.z * v.x - this.x * v.z;
		int z = this.x * v.y - this.y * v.x;
		return new Vector3i(x, y, z);
	}
	
	public Vector3i cross(int x, int y, int z) {
		x = this.y * z - this.z * y;
		y = this.z * x - this.x * z;
		z = this.x * y - this.y * x;
		return new Vector3i(x, y, z);
	}
	
	public Vector3f normalize() {
		float d = length();
		float x = this.x / d;
		float y = this.y / d;
		float z = this.z / d;
		return new Vector3f(x, y, z);
	}
	
	public float length() {
		return (float) Math.sqrt(x * x + y * y + z * z);
	}
	
	public void print() {
		System.out.println("X: " + x + ", Y: " + y + ", Z: " + z);
	}

}
