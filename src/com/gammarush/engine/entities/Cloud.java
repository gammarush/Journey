package com.gammarush.engine.entities;

import com.gammarush.engine.Game;
import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.graphics.Sprite;
import com.gammarush.engine.graphics.SpriteSheet;
import com.gammarush.engine.math.vector.Vector2f;
import com.gammarush.engine.tiles.Tile;

//MOVING CLOUDS TO ADD TO END GAME SCENERY

public class Cloud {
	
	private Game game;
	public Vector2f position;
	public int width = Tile.width * 3;
	public int height = Tile.height * 2;
	
	public Vector2f velocity;
	
	private Sprite sprite;
	private SpriteSheet spritesheet = new SpriteSheet("/entities/cloud.png");
	
	public Cloud(Vector2f position, Game game) {
		this.game = game;
		this.position = position;
		this.velocity = new Vector2f((float) (Math.floor(Math.random() * 2f) * 2f - 1f) / 2f, 0);
		int random = (int) (Math.random() * 2);
		//CHOOSE ONE OF TWO CLOUD SPRITES
		sprite = new Sprite(spritesheet, random * 96, 0, 96, 64);
	}
	
	public void update() {
		//UPDATE CLOUD POSITION, REMOVE IF OFF SCREEN
		position = position.add(velocity);
		if(position.x + width < game.renderer.position.x || position.x > game.renderer.position.x + game.renderer.width) {
			game.clouds.add(new Cloud(position.add(0, (int) (Math.random() * 512 - 256)), game));
			game.clouds.remove(this);
		}
	}
	
	public void render(Renderer renderer) {
		//RENDER CLOUD SPRITE
		sprite.render((int) (position.x - renderer.position.x), (int) (position.y - renderer.position.y), 0.8f, renderer);
	}
	
}
