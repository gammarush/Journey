package com.gammarush.engine.entities;

import java.util.ArrayList;

import com.gammarush.engine.Game;
import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.graphics.Sprite;
import com.gammarush.engine.graphics.SpriteSheet;
import com.gammarush.engine.math.vector.Vector2f;
import com.gammarush.engine.physics.Physics;
import com.gammarush.engine.tiles.Tile;

//BASIC ENEMY CLASS, TEMPLATE FOR BOSS AND SPIDER CLASS

public class Enemy {
	
	public Game game;
	public Vector2f position;
	public int width = Tile.width;
	public int height = (int) (Tile.height * 1.5);
	
	private Physics physics;
	public Vector2f velocity;
	public float speed = 2;
	public int direction = 1;
	
	public boolean moving = true;
	public boolean falling = false;
	
	public int lives = 1;
	public int cooldown = 0;
	
	public Vector2f waypoint;
	
	private SpriteSheet spritesheet = new SpriteSheet("/entities/enemy.png");
	private ArrayList<Sprite> sprites = new ArrayList<Sprite>();
	private int index = 0;
	private int frame = 0;
	private int max = 8;
	
	public Enemy(Vector2f position, Game game) {
		this.game = game;
		this.position = position;
		
		physics = new Physics(width, width, game);
		velocity = new Vector2f();
		
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
				sprites.add(new Sprite(spritesheet, j * 16, i * 24, 16, 24).scale(width, height));
			}
		}
	}
	
	public void update() {
		//UPDATE COOLDOWN TIMERS FOR DAMAGE
		if(cooldown > 0) cooldown--;
		
		//IF STANDING ON NON SOLID TILE, FALL
		if(game.world.getTile((int) Math.floor(position.x / Tile.width), (int) Math.floor((position.y + height - 1) / Tile.height)) == 0 && !falling) {
			falling = true;
		}
		
		//IF FALLING, ADD VELOCITY DOWNWARD
		if(falling) {
			velocity = physics.gravity(velocity);
		}
		
		//IF ENEMY IS IN LAVA, DAMAGE ENEMY
		if(game.world.getWater((int)(position.x / Tile.width), (int)(position.y / Tile.height)) != 0 && cooldown <= 0 && lives > 0) {
			lives--;
			cooldown = 120;
		}
		
		Vector2f initial = new Vector2f(velocity);
		
		//CALCULATE NEW TARGET WAYPOINT POSITION (PLAYER)
		Vector2f waypoint = position;
		if(Math.abs(position.x - game.player.position.x) < 12 * Tile.width && Math.abs(position.y - game.player.position.y) < 8 * Tile.height) waypoint = game.player.position;
		
		//FOLLOW NEW WAYPOINT
		if(position.x < waypoint.x) {
			velocity.x = speed;
			direction = 1;
		}
		if(position.x > waypoint.x) {
			velocity.x = -speed;
			direction = 3;
		}
		
		//IF VELOCITY EXISTS, ANIMATE SPRITE
		if(velocity.magnitude() > 0) moving = true;
		else moving = false;
		
		//CALCULATE NEW POSITION
		position = position.add(velocity);
		Vector2f translation = physics.collision(position);
		position = position.add(translation);
		
		velocity = initial;
		
		//IF ENEMY HITS SOLID TILE, STOP FALLING
		if(translation.y < 0) {
			velocity.y = 0;
			falling = false;
		}
		
		//JUMP UP TO REACH HIGHER TILE IF POSSIBLE
		if(translation.x != 0) {
			if(!falling) {
				velocity = physics.jump(velocity, 12);
				falling = true;
			}
		}
		
		//IF MOVING, CALCULATE INDEX OF SPRITE ANIMATION
		if(moving) {
			if(frame < max) {
                frame += 1;
            } else {
                frame = 0;
                if(index < 3) {
                    index += 1;
                } else {
                    index = 0;
                }
            }
		} else {
			frame = 0;
            index = 0;
		}
	}
	
	public void render(Renderer renderer) {
		//RENDER ENEMY SPRITE WITH CORRECT ANIMATION INDEX
		//IF DAMAGED, BLINK SPRITE
		if(cooldown % 3 == 0) sprites.get(index + direction * 4).render((int) (position.x - renderer.position.x), (int) (position.y - renderer.position.y - (height - width)), renderer);
	}

}