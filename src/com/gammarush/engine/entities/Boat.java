package com.gammarush.engine.entities;

import com.gammarush.engine.Game;
import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.graphics.Sprite;
import com.gammarush.engine.graphics.SpriteSheet;
import com.gammarush.engine.math.vector.Vector2f;
import com.gammarush.engine.particles.ParticleEmitter;
import com.gammarush.engine.tiles.Tile;

//FLOATING ROCKS AT THE BOTTOM OF THE CAVE IN LAVA RIVER, PLAYER CAN JUMP AND MOVE ON TOP OF THEM AS THEY FLOAT TOWARDS BOSS FIGHT

public class Boat {
	
	private Game game;
	public Vector2f position;
	public int width = (int) (Tile.width * 1.5);
	public int height = Tile.height;
	
	public Vector2f velocity;
	
	private Sprite sprite = new Sprite(new SpriteSheet("/entities/boat.png"));
	
	public Boat(Vector2f position, Vector2f velocity, Game game) {
		this.game = game;
		this.position = position;
		this.velocity = velocity;
		//IF BOAT IS ON RIGHT SIDE OF WORLD, FLIP DIRECTION
		if(this.position.x / Tile.width > this.game.world.width / 2) {
			this.velocity = this.velocity.mult(-1);
		}
	}
	
	public void update() {
		//CALCULATE NEW POSITION
		position = position.add(velocity);
		//IF BOAT REACHES MIDDLE OF WORLD, DELETE AND SPAWN PARTICLES
		if(velocity.x > 0 && position.x / Tile.width > game.world.width / 2 - 6.5) {
			game.emitters.add(new ParticleEmitter(position.add(width / 2, height / 2), 1f, 80, new int[]{0x666666, 0x444444, 0x222222, 0xff0000}, game));
			game.emitters.get(game.emitters.size() - 1).emit(30);
			game.boats.remove(this);
			game.boats.add(new Boat(new Vector2f(0, (game.world.levels[2] + 7) * Tile.height - 8), new Vector2f(1f, 0), game));
		}
		//IF BOAT REACHES MIDDLE OF WORLD, DELETE AND SPAWN PARTICLES
		if(velocity.x < 0 && position.x / Tile.width < game.world.width / 2 + 6) {
			game.emitters.add(new ParticleEmitter(position.add(width / 2, height / 2), 1f, 80, new int[]{0x666666, 0x444444, 0x222222, 0xff0000}, game));
			game.emitters.get(game.emitters.size() - 1).emit(30);
			game.boats.remove(this);
			game.boats.add(new Boat(new Vector2f(game.world.width * Tile.width, (game.world.levels[2] + 7) * Tile.height - 8), new Vector2f(1f, 0), game));
		}
	}
	
	public void render(Renderer renderer) {
		//RENDER BOAT SPRITE
		sprite.render((int) (position.x - renderer.position.x), (int) (position.y - renderer.position.y), renderer);
	}

}
