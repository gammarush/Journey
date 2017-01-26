package com.gammarush.engine.particles;

import com.gammarush.engine.graphics.Graphic2D;
import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.math.vector.Vector2f;

//SINGLE PARTICLE THAT SPAWNS FROM A PARTICLE EMITTER IN THE WORLD

public class Particle {
	
	public ParticleEmitter emitter;
	
	public Vector2f position;
	public Vector2f velocity;
	
	public int width;
	public int height;
	
	public int age;
	public int max = 60;
	
	public float angle;
	public float speed;
	public int color = 0xff00ff;
	
	public Particle(Vector2f position, float speed, float angle, int max, int color, ParticleEmitter emitter) {
		this.emitter = emitter;
		
		this.position = position;
		
		this.width = 2;
		this.height = 2;
		
		this.speed = speed;
		this.angle = angle;
		this.max = max;
		this.color = color;
		
		//CALCULATE STATIC VELOCITY OF PARTICLE
		velocity = new Vector2f((float) (Math.cos(Math.toRadians(this.angle))) * this.speed, (float) (Math.sin(Math.toRadians(this.angle))) * this.speed);
	}
	
	public void update() {
		//CALCULATE NEW POSITION AND AGE OF PARTICLE
		if(age > max) kill();
		age++;
		position = position.add(velocity);
	}
	
	public void render(Renderer renderer) {
		//RENDER RECTANGLE AT PARTICLE POSITION
		Graphic2D.drawRect((int) position.x - renderer.position.x, (int) position.y - renderer.position.y, width, height, color, renderer);
	}
	
	public void kill() {
		//DELETE THIS PARTICLE
		emitter.list.add(this);
	}
}
