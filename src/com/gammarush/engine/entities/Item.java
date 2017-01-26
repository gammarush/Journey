package com.gammarush.engine.entities;

import com.gammarush.engine.Game;
import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.graphics.Sprite;
import com.gammarush.engine.graphics.SpriteSheet;
import com.gammarush.engine.math.vector.Vector2f;
import com.gammarush.engine.physics.Physics;
import com.gammarush.engine.tiles.Tile;

//USED FOR HEARTS THAT ENEMIES DROP

public class Item {
	
	private Game game;
	public Vector2f position;
	public int width = Tile.width / 2;
	public int height = Tile.height / 2;
	
	private Vector2f velocity;
	private Physics physics;
	private boolean falling = false;
	
	private Sprite sprite = new Sprite(new SpriteSheet("/gui/heart.png"));
	
	private float time;
	private int offset;
	
	public Item(Vector2f position, Game game) {
		this.game = game;
		this.position = position;
		physics = new Physics(width, height, game);
		velocity = new Vector2f();
	}
	
	public void update() {
		//CALCULATE POSITION TO GIVE FLOATING UP AND DOWN APPEARANCE
		time += .1;
		if(time >= 6.28) time = 0;
		offset = (int) (Math.abs(Math.sin(time) * 4)) - 8;
		
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
		}
	}
	
	public void render(Renderer renderer) {
		//RENDER ITEM
		sprite.render((int) (position.x - renderer.position.x), (int) (position.y - renderer.position.y) + offset, renderer);
	}

}
