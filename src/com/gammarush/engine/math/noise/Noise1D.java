package com.gammarush.engine.math.noise;

import java.util.Random;

//PERLIN NOISE 1D, USED FOR GENERATING WORLD SURFACE (MOUNTAINS, TERRAIN)
//http://www.dafishinsea.com/blog/2008/09/06/perlin-noise-1-dimensional/

public class Noise1D {
	
	private int seed;
	private int octaves;
	private float persistance;
	private float frequency;
	
	public Noise1D(int seed, int octaves, float persistance, float frequency) {
		this.seed = seed;
		this.octaves = octaves;
		this.persistance = persistance;
		this.frequency = frequency;
	}
	
	public float generate(int x) {
		if(x < 0) x += 1000000;
		float total = 0.0f;
		float frequency = this.frequency;
		float amplitude = persistance;
		for(int i = 0; i < octaves; i++) {
			total += interpolateNoise(x * frequency) * amplitude;
			frequency *= 2;
			amplitude *= persistance;
		}
		return total;
	}
	
	private long hash(int x, int y) {
		int a = x >= 0 ? 2 * x : -2 * x - 1;
		int b = y >= 0 ? 2 * y : -2 * y - 1;
		return a >= b ? a * a + a + b : a + b * b;
	}
	
	public float getNoise(int x) {
		Random rng = new Random(hash(x, seed));
		int r = rng.nextInt();
		return (float)(r & 0x7fff)/(float)0x7fff - .5f;
	}
	
	public float interpolateNoise(float x) {
		int intX = (int) x;
		float fractX = x - intX;
		float v1 = smoothNoise(intX);
		float v2 = smoothNoise(intX + 1);
		return interpolate(v1, v2, fractX);
	}
	
	private float interpolate(float a, float b, float x) {
		float ft = x * 3.1415927f;
		float f = (float) ((1 - Math.cos(ft)) * .5);
		return  a * (1 - f) + b * f;
	}
	
	private float smoothNoise(int x) {
		return getNoise(x) / 2 + getNoise(x - 1) / 4 + getNoise(x + 1) / 4;
	}

}
