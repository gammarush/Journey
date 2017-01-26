package com.gammarush.engine.particles;

import java.util.ArrayList;
import java.util.List;

import com.gammarush.engine.Game;
import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.math.vector.Vector2f;

//SPAWNER CLASS FOR PARTICLES, CONTROLS HOW MUCH, HOW FAST, AND HOW OFTEN PARTICLES ARE SPAWNED

public class ParticleEmitter {
	
	private Game game;
	
	public Vector2f position;
	
	private boolean running = false;
	public int limit = 256;
	
	public float speed;
	public int max;
	public int[] colors;
	
	private boolean directional = false;
	public float direction;
	public float radius;
	
	private int currentLife = 0;
	private int maxLife = -1;
	
	public List<Particle> particles = new ArrayList<Particle>();
	public List<Particle> list = new ArrayList<Particle>();
	
	//CIRCLULAR EMITTER
	public ParticleEmitter(Vector2f position, float speed, int max, int[] colors, Game game) {
		this.game = game;
		this.position = position;
		this.speed = speed;
		this.max = max;
		this.colors = colors;
	}
	
	//EMITS PARTICLES IN ONE DIRECTION COVERING A SPECIFIED ANGLE
	public ParticleEmitter(Vector2f position, float speed, int max, int[] colors, float direction, float radius, Game game) {
		this.game = game;
		this.position = position;
		this.speed = speed;
		this.max = max;
		this.colors = colors;
		this.direction = direction;
		this.radius = radius;
		
		directional = true;
	}
	
	public void update() {
		//IF RUNNING, SPAWN PARTICLES
		if(running) {
			if(maxLife != -1) {
				currentLife++;
				if(currentLife >= maxLife) {
					game.emitters.remove(this);
					return;
				}
			}
			if(particles.size() < limit) {
				float angle = (float) Math.random();
				if(directional) {
					float min = direction - radius;
					float max = direction + radius;
					angle = (max - min) * angle + min;
				}
				else {
					angle *= 360;
				}
				
				int color = colors[(int) (Math.random() * colors.length)];
				particles.add(new Particle(new Vector2f(position), speed, angle, max, color, this));
			}
		}
		
		//UPDATE ALL PARTICLES
		for(Particle p : particles) {
			p.update();
		}
		
		//REMOVE ALL PARTICLES ON KILL LIST
		for(Particle p : list) {
			particles.remove(p);
		}
		
		//CLEAR KILL LIST
		list.clear();
	}
	
	public void render(Renderer renderer) {
		//RENDER ALL PARTICLES
		for(Particle p : particles) {
			p.render(renderer);
		}
	}
	
	public void emit() {
		running = true;
	}
	
	public void emit(int time) {
		//TURN ON EMITTER FOR SPECIFIED TIME
		currentLife = 0;
		maxLife = time;
		running = true;
	}
	
	public void kill() {
		running = false;
	}
}
