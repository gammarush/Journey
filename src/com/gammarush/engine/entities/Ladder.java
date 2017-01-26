package com.gammarush.engine.entities;

import com.gammarush.engine.Game;
import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.graphics.Sprite;
import com.gammarush.engine.graphics.SpriteSheet;
import com.gammarush.engine.math.vector.Vector2f;
import com.gammarush.engine.tiles.Tile;

//USED BY PLAYER TO CLIMB OUT OF HOLES MORE THAN 2 TILES DEEP, PRESS E TO ACTIVATE

public class Ladder {
	
	private Game game;
	public Vector2f position;
	public int width = Tile.width / 4;
	public int height = Tile.height / 4;
	
	private Sprite sprite = new Sprite(new SpriteSheet("/entities/ladder.png"));
	
	public Ladder(Vector2f position, Game game) {
		this.game = game;
		this.position = position;
	}
	
	public void update() {
		//LADDER GROWS DOWNWARD UNTIL IT HITS SOLID TILE
		int wx = (int) Math.floor(position.x / Tile.width);
		int wy = (int) Math.floor((position.y + height) / Tile.height);
		Tile tile = Tile.tiles.get(game.world.getTile(wx, wy));
		if(!tile.solid) height += 4;
	}
	
	public void render(Renderer renderer) {
		//RENDER LADDER SPRITE ACCORDING TO HEIGHT
		sprite.render((int) (position.x - renderer.position.x), (int) (position.y - renderer.position.y), height, renderer);
	}

}
