package com.gammarush.engine.entities;

import com.gammarush.engine.Game;
import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.graphics.Sprite;
import com.gammarush.engine.math.vector.Vector2f;
import com.gammarush.engine.particles.ParticleEmitter;
import com.gammarush.engine.physics.Physics;
import com.gammarush.engine.tiles.Tile;

//CREATE MINI TILES THAT USE PHYSICS, USED FOR VOLCANO ERUPTION ENDING

public class FallingTile {
	
	private Game game;
	public Vector2f position;
	public int width = Tile.width - 8;
	public int height = Tile.height - 8;
	
	private Vector2f velocity;
	private Physics physics;
	private boolean falling = false;
	
	private Sprite sprite;
	
	public FallingTile(Vector2f position, Tile tile, Game game) {
		this.game = game;
		this.position = position;
		physics = new Physics(width, height, game);
		velocity = new Vector2f();
		sprite = tile.sprite.scale(width, height);
	}
	
	public void update() {
		//IF STANDING ON NON SOLID TILE, FALL
		if(game.world.getTile((int) Math.floor(position.x / Tile.width), (int) Math.floor((position.y + height - 1) / Tile.height)) == 0 && !falling) {
			falling = true;
		}
		
		//IF FALLING, ADD VELOCITY DOWNWARD
		if(falling) {
			velocity = physics.gravity(velocity);
		}
		
		//CALCULATE NEW POSITION
		position = position.add(velocity);
		Vector2f translation = physics.collision(position);
		position = position.add(translation);
		
		//IF ENEMY HITS SOLID TILE, STOP FALLING
		if(translation.y < 0) {
			velocity.y = 0;
			falling = false;
			game.emitters.add(new ParticleEmitter(position.add(width / 2, height / 2), 1f, 20, new int[]{0xaaaaaa, 0x888888, 0x555555}, game));
			game.emitters.get(game.emitters.size() - 1).emit(45);
			game.fallingTiles.remove(this);
		}
	}
	
	public void render(Renderer renderer) {
		//RENDER FALLING TILE
		sprite.render((int) (position.x - renderer.position.x), (int) (position.y - renderer.position.y), renderer);
	}

}
