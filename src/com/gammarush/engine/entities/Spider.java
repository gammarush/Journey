package com.gammarush.engine.entities;

import java.util.ArrayList;

import com.gammarush.engine.Game;
import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.graphics.Sprite;
import com.gammarush.engine.graphics.SpriteSheet;
import com.gammarush.engine.math.vector.Vector2f;
import com.gammarush.engine.physics.Physics;
import com.gammarush.engine.tiles.Tile;

//SPIDER ENEMY, 2 TILES WIDE AND 1 TILE HIGH, VARIATION OF BASIC ENEMY

public class Spider extends Enemy {

	public int width = Tile.width * 2;
	public int height = Tile.height;
	
	private Physics physics;
	
	private SpriteSheet spritesheet = new SpriteSheet("/entities/spider.png");
	private ArrayList<Sprite> sprites = new ArrayList<Sprite>();
	private int index = 0;
	private int frame = 0;
	private int max = 4;
	
	public Spider(Vector2f position, Game game) {
		super(position, game);
		
		physics = new Physics(width, width, game);
		
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
				sprites.add(new Sprite(spritesheet, j * 32, i * 16, 32, 16).scale(width, height));
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
				velocity = physics.jump(velocity, 16);
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
		//RENDER SPIDER SPRITE WITH CORRECT ANIMATION INDEX
		//IF DAMAGED, BLINK SPRITE
		if(cooldown % 3 == 0) sprites.get(index + direction * 4).render((int) (position.x - renderer.position.x), (int) (position.y - renderer.position.y - (height - width)), renderer);
	}

}
