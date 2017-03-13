package com.gammarush.engine.entities;

import java.util.ArrayList;

import com.gammarush.engine.Game;
import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.graphics.Sprite;
import com.gammarush.engine.graphics.SpriteSheet;
import com.gammarush.engine.math.vector.Vector2f;
import com.gammarush.engine.particles.ParticleEmitter;
import com.gammarush.engine.physics.AABB;
import com.gammarush.engine.physics.Physics;
import com.gammarush.engine.sound.Sound;
import com.gammarush.engine.tiles.Tile;

//PLAYER CLASS THAT USER CONTROLS

public class Player {
	
	private Game game;
	public Vector2f position;
	public int width = Tile.width;
	public int height = (int) (Tile.height * 1.5);
	
	private Physics physics;
	public Vector2f velocity;
	public float speed = 4;
	public int direction = 2;
	
	public boolean moving = false;
	public boolean falling = false;
	public boolean climbing = false;
	
	public int lives = 3;
	public int cooldown = 0;
	
	private SpriteSheet spritesheet = new SpriteSheet("/entities/player.png");
	private ArrayList<Sprite> sprites = new ArrayList<Sprite>();
	private int index = 0;
	private int frame = 0;
	private int max = 8;
	
	public ParticleEmitter emitter;
	
	public AABB focusArea;
	public Vector2f focusAreaVelocity = new Vector2f();
	
	public Player(Vector2f position, Game game) {
		this.game = game;
		this.position = position;
		
		physics = new Physics(width, width, game);
		velocity = new Vector2f();
		
		focusArea = new AABB(position.x, position.y, Tile.width, Tile.height);
		
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
				sprites.add(new Sprite(spritesheet, j * 16, i * 24, 16, 24).scale(width, height));
			}
		}
	}
	
	public void update() {
		//GRAB KEY ARRAY FROM LISTENER CLASS
		boolean[] keys = game.listener.keys;
		
		if(keys[82] && !(game.complete || game.failed)) {
			//RESTART GAME
			game.paused = true;
			game.failed = true;
			game.restartConfirmation();
		}
		
		//TEST FOR GAMEOVER
		if(lives <= 0) {
			game.failed = true;
			game.gameover();
		}
		
		//CHECK FOR COMPLETION OF A LEVEL AND ADD POINTS TO SCORE
		if(position.y / Tile.height > game.world.levels[1] && game.level == 1) {
			Sound.play("/sounds/level.wav", -15f);
			game.level++;
			game.score += 100;
		}
		if(position.y / Tile.height > game.world.levels[2] && game.level == 2) {
			Sound.play("/sounds/level.wav", -15f);
			game.level++;
			game.score += 200;
		}
		
		//JUMP IF SPACE IS PRESSED
		if(keys[32] && !game.failed) {
			if(!falling) {
				Sound.play("/sounds/jump.wav", -25f);
				velocity = physics.jump(velocity, 14);
				falling = true;
			}
		}
		
		//IF STANDING ON NON SOLID TILE, FALL
		if(game.world.getTile((int) Math.floor(position.x / Tile.width), (int) Math.floor((position.y + height - 1) / Tile.height)) == 0 && !falling) {
			falling = true;
		}
		
		//IF FALLING, ADD VELOCITY DOWNWARD
		if(falling) {
			velocity = physics.gravity(velocity);
		}
		
		//IF E IS PRESSED, SPAWN LADDER ABOVE PLAYER IF POSSIBLE
		if(keys[69] && ((game.ladders.size() == 0) || (game.ladders.size() > 0 && game.ladders.get(0).height >= Tile.height)) && !(game.complete || game.failed)) {
			int wx = (int) Math.floor(position.x / Tile.width);
			int wy = (int) Math.floor(position.y / Tile.height);
			int lx = Integer.MIN_VALUE, ly = Integer.MIN_VALUE;
			for(int i = wy; i >= Math.max(wy - 8, 0); i--) {
				Tile tile = Tile.tiles.get(game.world.getTile(wx, i));
				Tile leftTile = Tile.tiles.get(game.world.getTile(wx - 1, i));
				Tile rightTile = Tile.tiles.get(game.world.getTile(wx + 1, i));
				if(!tile.solid) {
					if(leftTile.solid) {
						lx = wx * Tile.width;
						ly = i * Tile.height;
					}
					if(rightTile.solid) {
						lx = (wx + 1) * Tile.height - 8;
						ly = i * Tile.height;
					}
				}
				else break;
			}
			if(lx != Integer.MIN_VALUE && ly != Integer.MIN_VALUE) {
				Sound.play("/sounds/rope.wav", -25f);
				game.ladders.clear();
				game.ladders.add(new Ladder(new Vector2f(lx, ly), game));
			}
		}
		
		//IF ON BOAT, MOVE POSITION TO MATCH BOATS POSITION
		climbing = false;
		AABB box = new AABB(position, width, height);
		for(int i = 0; i < game.boats.size(); i++) {
			Boat e = game.boats.get(i);
			if(Physics.getCollision(box, new AABB(e.position.add(0, 3), e.width, e.height - 4))) {
				position = position.add(e.velocity);
			}
		}
		
		//CLIMB LADDER IF COLLIDING WITH ONE
		for(int i = 0; i < game.ladders.size(); i++) {
			Ladder e = game.ladders.get(i);
			if(Physics.getCollision(box, new AABB(e.position/*.add(0, -Tile.width)*/, e.width, e.height/* + Tile.width*/))) {
				//ALIGN PLAYER WITH LADDER FOR EASIER CLIMBING
				if(keys[87] || keys[38]) {
					float step = 1f;
					float x = (float) (Math.floor(e.position.x  / Tile.width) * Tile.width);
					if(position.x < x) position.x += step;
					if(position.x > x) position.x -= step;
					
					//STOP BOUNCING VISUAL GLITCH ON ROPE
					if(position.y < e.position.y - Tile.width + 4) position.y = e.position.y - Tile.width + 4;
				}
				climbing = true;
			}
		}
		
		//IF PLAYER COLLIDES WITH ITEM, ADD HEALTH TO PLAYER AND SPAWN PARTICLES
		for(int i = 0; i < game.items.size(); i++) {
			Item e = game.items.get(i);
			if(Physics.getCollision(box, new AABB(e.position, e.width, e.height))) {
				Sound.play("/sounds/health.wav", -25f);
				lives++;
				if(lives > 5) lives = 5;
				game.emitters.add(new ParticleEmitter(e.position.add(e.width / 2, e.height / 2), 1f, 10, new int[]{0xff0000, 0xb20000, 0xcc0000}, game));
				game.emitters.get(game.emitters.size() - 1).emit(45);
				game.items.remove(i);
				
				game.score += 10;
			}
		}
		
		//UPDATE DAMAGE COOLDOWN
		if(cooldown > 0) cooldown--;
		//TEST FOR ENEMY COLLISION
		for(int i = 0; i < game.enemies.size(); i++) {
			Enemy e = game.enemies.get(i);
			//TEST PADDING OF 4 ON ENEMY COLLISION
			if(Physics.getCollision(box, new AABB(e.position.add(4, 4), e.width - 4, e.height - 4))) {
				//PLAYER HIT ENEMY
				if(position.y < e.position.y && velocity.y > 0) {
					//IF NO COOLDOWN
					if(e.cooldown <= 0) {
						//SUBTRACT LIVES FROM ENEMY AND RESET COOLDOWN
						e.lives--;
						e.cooldown = 60;
						//IF ENEMY OUT LIVES
						if(e.lives <= 0) {
							//YOU WIN, START END ANIMATIONS
							if(e instanceof Boss) {
								Sound.play("/sounds/level.wav", -15f);
								
								game.score += 100;
								speed /= 4;
								game.complete = true;
								game.enemies.clear();
								game.boats.clear();
								game.boats.add(new Boat(new Vector2f(), new Vector2f(), game));
								
								game.gameover();
							}
							//30% CHANCE FOR HEALTH ITEM
							if(Math.random() < .3) game.items.add(new Item(e.position, game));
							//SPAWN PARTICLES
							game.emitters.add(new ParticleEmitter(e.position.add(e.width / 2, e.height / 2), 1f, 20, new int[]{0xffffff, 0xaaaaaa, 0x777777}, game));
							game.emitters.get(game.emitters.size() - 1).emit(45);
							//REMOVE DEAD ENEMY
							if(!game.complete) game.enemies.remove(i);
							
							if(e instanceof Enemy) game.score += 10;
							if(e instanceof Spider) game.score += 25;
						}
					}
				}
				//ENEMY HIT PLAYER
				else {
					if(cooldown <= 0 && e.cooldown <= 0 && lives > 0) {
						Sound.play("/sounds/hurt.wav", -25f);
						lives--;
						cooldown = 60;
					}
				}
			}
		}
		
		//IF PLAYER IS IN LAVA, DAMAGE PLAYER
		if(game.world.getWater((int)(position.x / Tile.width), (int)(position.y / Tile.height)) != 0 && cooldown <= 0 && !game.complete && lives > 0) {
			Sound.play("/sounds/hurt.wav", -25f);
			lives--;
			cooldown = 120;
		}
		
		Vector2f initial = new Vector2f(velocity);
		
		//MOVE PLAYER WHEN W, A, AND D KEYS ARE PRESSED
		if(!game.world.animationComplete && !game.failed) {
			//W: MOVE PLAYER UP
			if((keys[87] || keys[38]) && climbing) {
				velocity.y = -speed;
				direction = 0;
			}
			//A: MOVE PLAYER LEFT
			if(keys[65] || keys[37]) {
				velocity.x = -speed;
				direction = 3;
			}
			//D: MOVE PLAYER RIGHT
			if(keys[68] || keys[39]) {
				velocity.x = speed;
				direction = 1;
			}
			
			//IF MOVING, UPDATE SPRITE ANIMATION
			if(velocity.magnitude() > 0) moving = true;
			else moving = false;
		}
		
		//ADD VELOCITY TO POSITION, THEN CHECK AND FIX COLLISION
		position = position.add(velocity);
		Vector2f translation = physics.collision(position);
		position = position.add(translation);
		
		//STAY IN THE MAP
		if(position.x < 11 * Tile.width) position.x = 11 * Tile.width;
		if(position.x > game.world.width * Tile.width - 12 * Tile.width) position.x = game.world.width * Tile.width - 12 * Tile.width;
		
		//SET VELOCITY BACK TO INITIAL BEFORE PLAYER INPUT
		velocity = initial;
		
		if(translation.y < 0) {
			velocity.y = 0;
			falling = false;
		}
		
		
		
		//UPDATE FOCUS AREA FOR CAMERA
		float shiftX = 0;
		if(position.x < focusArea.getMin().x) {
			shiftX = position.x - focusArea.getMin().x;
		}
		else if(position.x + width > focusArea.getMax().x) {
			shiftX = position.x + width - focusArea.getMax().x;
		}
		focusArea.x += shiftX;
		
		float shiftY = 0;
		if(position.y < focusArea.getMin().y) {
			shiftY = position.y - focusArea.getMin().y;
		}
		else if(position.y + width > focusArea.getMax().y) {
			shiftY = position.y + width - focusArea.getMax().y;
		}
		focusArea.y += shiftY;
		
		focusAreaVelocity = new Vector2f(shiftX, shiftY);
		
		
		//UPDATE SPRITE ANIMATION INDEX
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
		//RENDER PLAYER SPRITE WITH CORRECT ANIMATION INDEX
		//IF DAMAGED, BLINK SPRITE
		if(cooldown % 3 == 0) sprites.get(index + direction * 4).render((int) (position.x - renderer.position.x), (int) (position.y - renderer.position.y - (height - width)), renderer);
	}

}
