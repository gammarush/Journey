package com.gammarush.engine.tiles;

import java.util.ArrayList;
import java.util.HashMap;

import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.graphics.Sprite;
import com.gammarush.engine.graphics.SpriteSheet;

//TILE CLASS FOR ORGANIZATION AND RENDERING FAST

public class Tile {
	
	//STATIC TILE SIZE
	public static final int width = 32;
	public static final int height = 32;
	
	//TILE CACHE SO TILES ARE ONLY CREATED ON STARTUP
	public static HashMap<Integer, Tile> tiles = new HashMap<Integer, Tile>();
	
	private SpriteSheet blendsheet;
	public ArrayList<Sprite> blendmaps = new ArrayList<Sprite>();
	
	public int id;
	public boolean solid;
	public int connect;
	public Sprite sprite;
	
	public Tile(int id, boolean solid, int connect, Sprite sprite) {
		this.id = id;
		this.solid = solid;
		this.connect = connect;
		this.sprite = sprite.scale(width, height);
		
		//PUT NEW TILE IN CACHE
		tiles.put(this.id, this);
		
		//INIT DEFAULT BLEND MAPS
		blendsheet = new SpriteSheet("/tiles/blend.png");
		for(int i = 0; i < 8; i++) blendmaps.add(new Sprite(blendsheet, i * 32, 0, 32, 32));
	}
	
	public void update() {
		//FUTURE METHOD FOR UPDATING ANIMATED TILES
	}
	
	//RENDER TILE SPRITE WITHOUT BLEND MAPS
	public void render(int x, int y, Renderer renderer) {
		sprite.render(x, y, renderer);
	}
	
	//RENDER TILE SPRITE WITH BLEND MAPS
	public void render(int x, int y, Tile tile, ArrayList<Sprite> blendmaps, Renderer renderer) {
		sprite.blend(tile.sprite, blendmaps).render(x, y, renderer);
	}
}
