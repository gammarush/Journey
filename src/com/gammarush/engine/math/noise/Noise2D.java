package com.gammarush.engine.math.noise;

import java.util.Random;

//PERLIN NOISE 2D, USED FOR GENERATING CAVES IN WORLD

public class Noise2D {
	
	private int seed;
	private int octaves;
	private float persistance;
	private float frequency;

	public Noise2D(int seed, int octaves, float persistance, float frequency) {
		this.seed = seed;
		this.octaves = octaves;
		this.persistance = persistance;
		this.frequency = frequency;
	}
	
	public float generate(float x, float y) {
		float total = 0.0f;
		float frequency = this.frequency;
		float amplitude = persistance;
		for(int i = 0; i < octaves; i++) {
			total += interpolateNoise(x * frequency, y * frequency) * amplitude;
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
	
	private float getNoise(int x, int y) {
		Random rng = new Random(hash((int) hash(x, y), seed));
		int r = rng.nextInt();
		return (float)(r & 0x7fff)/(float)0x7fff - .5f;
	}
	
	private float interpolateNoise(float x, float y) {
		int intX, intY;
		float fractX, fractY, v1, v2, v3, v4, i1, i2;
		intX = (int) x;
		fractX = x - intX;
		intY = (int) y;
		fractY = y - intY;
		v1 = smoothNoise(intX, intY);
		v2 = smoothNoise(intX + 1, intY);
		v3 = smoothNoise(intX, intY + 1);
		v4 = smoothNoise(intX + 1, intY + 1);
		i1 = interpolate(v1, v2, fractX);
		i2 = interpolate(v3, v4, fractX);
		return interpolate(i1, i2, fractY);
	}
	
	private float interpolate(float a, float b, float x) {
		float ft = x * 3.1415927f;
		float f = (float) ((1 - Math.cos(ft)) * .5);
		return  a * (1 - f) + b * f;
	}
	
	private float smoothNoise(int x, int y) {
		float corners = (getNoise(x-1, y-1) + getNoise(x+1, y-1) + getNoise(x-1, y+1) + getNoise(x+1, y+1) ) / 16;
		float sides   = (getNoise(x-1, y) + getNoise(x+1, y) + getNoise(x, y-1) + getNoise(x, y+1) ) / 8;
		float center = getNoise(x, y) / 4;
		return corners + sides + center;
	}

}
