package com.gammarush.engine.world;

import java.util.ArrayList;

import com.gammarush.engine.Game;
import com.gammarush.engine.entities.Boat;
import com.gammarush.engine.entities.Boss;
import com.gammarush.engine.entities.Cloud;
import com.gammarush.engine.entities.Enemy;
import com.gammarush.engine.entities.FallingTile;
import com.gammarush.engine.entities.Spider;
import com.gammarush.engine.graphics.Graphic2D;
import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.graphics.Sprite;
import com.gammarush.engine.math.noise.Noise2D;
import com.gammarush.engine.math.vector.Vector2f;
import com.gammarush.engine.math.vector.Vector2i;
import com.gammarush.engine.tiles.Tile;

//WORLD CLASS, RUNS VOLCANO ERUPTION ANIMATION, GENERATES CAVES, UPDATES LAVA FLOWS

public class World {
	
	public Game game;
	public int width;
	public int height;
	public int[] array;
	
	//LAVA DATA
	public int[] waterArray;
	private int[] waterVertices = new int[] {0, 0, 0};
	private float waterAngle = 0;
	private int waterAmp = 3;
	private int waterColor = 0xff0000;
	private float waterAlpha = 0.5f;
	
	public int time = 0;
	
	//SEPARATION OF 3 IN GAME LEVELS
	public int[] levels = new int[] {0, 160, 220};
	
	//ANIMATION DATA
	private float animationLevel;
	private float animationRate = .1f;
	public boolean animationComplete = false;
	
	public Sprite sprite;
	
	public World(int width, int height, Game game) {
		this.game = game;
		this.width = width;
		this.height = height;
		this.array = new int[width * height];
		this.waterArray = new int[width * height];
		
		animationLevel = height - 1;
	}
	
	public void update() {
		//WATER PHYSICS AND SPREADING
		if(time >= 30) {
			time = 0;
			
			int[] tempWaterArray = new int[waterArray.length];
			for(int i = 0;  i < waterArray.length; i++) {
				tempWaterArray[i] = waterArray[i];
			}
			
			int buffer = 32;
			int fx = (int) Math.max(Math.floor(game.renderer.position.x / Tile.width) - buffer, 0);
			int lx = Math.min(fx + (game.renderer.width / Tile.width) + 2 + buffer * 2, width);
			int fy = (int) Math.max(Math.floor(game.renderer.position.y / Tile.height - buffer), 0);
			int ly = Math.min(fy + (game.renderer.height / Tile.height) + 2 + buffer * 2, height);
			for(int y = fy; y < ly; y++) {
				for(int x = fx; x < lx; x++) {
					int water = getWater(x, y);
					int topWater = getWater(x, y - 1);
					int bottomWater = getWater(x, y + 1);
					int leftWater = getWater(x - 1, y);
					int rightWater = getWater(x + 1, y);
					Tile tile = Tile.tiles.get(getTile(x, y));
					Tile bottomLeft = Tile.tiles.get(getTile(x - 1, y + 1));
					Tile bottomRight = Tile.tiles.get(getTile(x + 1, y + 1));
					if(!tile.solid) {
						if(leftWater > 1 && bottomLeft.solid) {
							leftWater = Math.min(leftWater, 8);
							if(water < leftWater) tempWaterArray[x + y * width] = leftWater - 1;
						}
						if(rightWater > 1 && bottomRight.solid) {
							water = tempWaterArray[x + y * width];
							rightWater = Math.min(rightWater, 8);
							if(water < rightWater) tempWaterArray[x + y * width] = rightWater - 1;
						}
						//SPREAD DOWN
						if(water < 8 && topWater > 0) tempWaterArray[x + y * width] = 8;
						//REMOVE DOWNSTREAM WATER IF TILE ABOVE IS AIR
						if(water == 8 && topWater == 0) tempWaterArray[x + y * width] = Math.max(water - 2, 0);
						//REMOVE OTHER NON DOWNSTREAM BLOCKS IF NO CONNECTION TO SOURCE
						if(water < 8 && topWater == 0 && bottomWater == 8 && water > leftWater && water > rightWater) tempWaterArray[x + y * width] = Math.max(water - 2, 0);
						//if(water < 8 && water > leftWater && water > rightWater) tempWaterArray[x + y * width] = water - 1;
					}
				}
			}
			
			for(int i = 0;  i < waterArray.length; i++) {
				waterArray[i] = tempWaterArray[i];
			}
		}
		else time++;
		
		//WATER VISUALS
		waterAngle += .08;
		if(waterAngle > 6.28) waterAngle = 0;
		
		//UPDATE LAVA WAVE ANIMATION
		for(int i = 0; i < waterVertices.length; i++) {
			waterVertices[i] = (int) ((Math.sin(waterAngle - (i * 3.14 / waterVertices.length))) * waterAmp);
		}
		
		//UPDATE IF GAME IS COMPLETED
		if(game.complete && !animationComplete) {
			if(animationLevel < height * .75 && animationRate < .2f) animationRate = .2f;
			if(animationLevel < height * .5 && animationRate < .4f) animationRate = .4f;
			
			waterAmp = 4;
			int fx = width / 2 - 7;
			int lx = width / 2 + 8;
			animationLevel -= animationRate;
			for(int x = fx; x < lx; x++) {
				if(animationLevel >= 0) {
					if(Math.random() < .5f) {
						Tile tile = Tile.tiles.get(getTile(x, (int) animationLevel - 1));
						if(tile.id != 0) {
							if(Math.random() < .1) game.fallingTiles.add(new FallingTile(new Vector2f(x * Tile.width, (animationLevel - 1) * Tile.height + (float) (Math.random() * 48 - 24)), tile, game));
							setTile(0, x, (int) animationLevel - 1);
						}
					}
					Tile tile = Tile.tiles.get(getTile(x, (int) animationLevel));
					if(tile.id != 0) {
						if(Math.random() < .1) game.fallingTiles.add(new FallingTile(new Vector2f(x * Tile.width, animationLevel * Tile.height + (float) (Math.random() * 48 - 24)), tile, game));
						setTile(0, x, (int) animationLevel);
					}
				}
				if(animationLevel >= -4) {
					float waterAnimationLevel = (float) Math.floor((animationLevel + 4) * 8) / 8;
					int waterAnimationId = (int) ((1 - (waterAnimationLevel - Math.floor(waterAnimationLevel))) * 8);
					setWater(waterAnimationId, x, (int) waterAnimationLevel);
					setWater(9, x, (int) waterAnimationLevel + 1);
					game.boats.get(0).position = new Vector2f(game.player.position.x - 8, waterAnimationLevel * Tile.height - 4);
				}
				else if(!animationComplete) {
					animationComplete = true;
					game.player.direction = 2;
					game.boats.get(0).velocity = new Vector2f(0, -8f);
					for(int i = 0; i < 40; i++) {
						game.clouds.add(new Cloud(new Vector2f((int) ((game.player.position.x - game.renderer.width / 2) + Math.random() * game.renderer.width), (int) (-32 * Tile.height + Math.random() * game.renderer.height * 2)), game));
					}
				}
			}
			//UPDATE VOLCANO SIDES
			if(animationLevel >= 0) {
				setTile(5, width / 2 - 8, (int) animationLevel);
				setTile(5, width / 2 + 8, (int) animationLevel);
				setTile(5, width / 2 - 9, (int) animationLevel + 2);
				setTile(5, width / 2 + 9, (int) animationLevel + 2);
				setTile(5, width / 2 - 10, (int) animationLevel + 5);
				setTile(5, width / 2 + 10, (int) animationLevel + 5);
				setTile(5, width / 2 - 11, (int) animationLevel + 8);
				setTile(5, width / 2 + 11, (int) animationLevel + 8);
				setTile(5, width / 2 - 12, (int) animationLevel + 9);
				setTile(5, width / 2 + 12, (int) animationLevel + 9);
				setTile(5, width / 2 - 13, (int) animationLevel + 10);
				setTile(5, width / 2 + 13, (int) animationLevel + 10);
			}
		}
		
		//MOVE BOAT PLAYER IS STANDING ON
		if(animationComplete) {
			Boat boat = game.boats.get(0);
			if(boat.velocity.y != 0 && boat.position.y <= -16 * Tile.height) {
				boat.velocity = new Vector2f();
			}
		}
	}
	
	public void render(Renderer renderer) {
		//DETERMINE WHAT TILES TO RENDER
		int fx = (int) Math.max(Math.floor(renderer.position.x / Tile.width), 0);
		int lx = Math.min(fx + (renderer.width / Tile.width) + 2, width);
		int fy = (int) Math.max(Math.floor(renderer.position.y / Tile.height), 0);
		int ly = Math.min(fy + (renderer.height / Tile.height) + 2, height);
		for(int y = fy; y < ly; y++) {
			for(int x = fx; x < lx; x++) {
				int id = getTile(x, y);
				Tile tile = Tile.tiles.get(id);
				int tx = x * Tile.width;
				int ty = y * Tile.height;
				//CALCULATE WHICH BLENDMAPS TO USE
				if(renderer.useBlendMaps && tile.connect == 1) {
					Tile tile1 = Tile.tiles.get(getTile(x - 1, y));
					Tile tile2 = Tile.tiles.get(getTile(x, y - 1));
					Tile tile3 = Tile.tiles.get(getTile(x + 1, y));
					Tile tile4 = Tile.tiles.get(getTile(x, y + 1));
					Tile tile5 = Tile.tiles.get(getTile(x - 1, y - 1));
					Tile tile6 = Tile.tiles.get(getTile(x + 1, y - 1));
					Tile tile7 = Tile.tiles.get(getTile(x + 1, y + 1));
					Tile tile8 = Tile.tiles.get(getTile(x - 1, y + 1));
					Tile tilef = null;
					ArrayList<Sprite> blendmaps = new ArrayList<Sprite>();
					if(tile1.connect == 2) {
						tilef = tile1;
						blendmaps.add(tile.blendmaps.get(0));
					}
					if(tile2.connect == 2) {
						tilef = tile2;
						blendmaps.add(tile.blendmaps.get(1));
					}
					if(tile3.connect == 2) {
						tilef = tile3;
						blendmaps.add(tile.blendmaps.get(2));
					}
					if(tile4.connect == 2) {
						tilef = tile4;
						blendmaps.add(tile.blendmaps.get(3));
					}
					if(tile5.connect == 2) {
						tilef = tile5;
						blendmaps.add(tile.blendmaps.get(4));
					}
					if(tile6.connect == 2) {
						tilef = tile6;
						blendmaps.add(tile.blendmaps.get(5));
					}
					if(tile7.connect == 2) {
						tilef = tile7;
						blendmaps.add(tile.blendmaps.get(6));
					}
					if(tile8.connect == 2) {
						tilef = tile8;
						blendmaps.add(tile.blendmaps.get(7));
					}
					if(tilef != null) {
						//RENDER TILE WITH BLENDMAPS
						tile.render(tx - renderer.position.x, ty - renderer.position.y, tilef, blendmaps, renderer);
					}
					else {
						//RENDER TILE WITHOUT BLENDMAPS
						tile.render(tx - renderer.position.x, ty - renderer.position.y, renderer);
					}
				}
				else {
					//RENDER TILE WITHOUT BLENDMAPS
					tile.render(tx - renderer.position.x, ty - renderer.position.y, renderer);
				}
			}
		}
	}
	
	public void renderWater(Renderer renderer) {
		//DETERMINE WHAT WATER TILES TO RENDER
		int fx = (int) Math.max(Math.floor(renderer.position.x / Tile.width), 0);
		int lx = Math.min(fx + (renderer.width / Tile.width) + 2, width);
		int fy = (int) Math.max(Math.floor(renderer.position.y / Tile.height), 0);
		int ly = Math.min(fy + (renderer.height / Tile.height) + 2, height);
		for(int y = fy; y < ly; y++) {
			for(int x = fx; x < lx; x++) {
				int level = getWater(x, y);
				//int id = level;
				if(level > 8) level = 8;
				if(level > 0) {
					int wx = x * Tile.width;
					int wy = y * Tile.height + (8 - level) * 4;
					int wh = Tile.height - (8 - level) * 4;
					int wx1 = (int) (wx - renderer.position.x);
					int wy1 = (int) (wy - renderer.position.y);
					if(getWater(x, y - 1) == 0) {
						for(int i = 0; i < waterVertices.length; i++) {
							int y1 = waterVertices[i];
							int y2 = i < waterVertices.length - 1 ? waterVertices[i + 1] : waterVertices[0];
							int lo = 0;
							int ro = 0;
							int ao = Tile.height / 16;
							if(i == 0) {
								if(getWater(x - 1, y) < level) lo = ao;
								if(getWater(x - 1, y) > level) lo = -ao;
							}
							if(i == waterVertices.length - 1) {
								if(getWater(x + 1, y) < level) ro = ao;
								if(getWater(x + 1, y) > level) ro = -ao;
							}
							//RENDER LAVA TILE
							Graphic2D.drawQuad(wx1 + i * Tile.width / waterVertices.length, wy1 + y1 + lo,
									wx1 + (i + 1) * Tile.width / waterVertices.length, wy1 + y2 + ro,
									wx1 + (i + 1) * Tile.width / waterVertices.length, wy1 + wh,
									wx1 + i * Tile.width / waterVertices.length, wy1 + wh,
									waterColor, waterAlpha, renderer);
						}
					}
					else {
						Graphic2D.drawRect((int) (wx - renderer.position.x), (int) (y * Tile.height - renderer.position.y), Tile.width, Tile.height, waterColor, waterAlpha, renderer);
					}
					//Graphic2D.drawRect((int) (wx - renderer.position.x), (int) (wy - renderer.position.y), Tile.width, wh, 0xadd8e6, .5f, renderer);
					//Graphic2D.drawString(id + "", (int) (wx - renderer.position.x), (int) (wy - renderer.position.y), 1, renderer);
				}
			}
		}
	}
	
	//GETTERS AND SETTERS FOR WORLD TILES AND LAVA TILES
	public int getTile(int x, int y) {
		if(x < 0 || y < 0 || x >= width || y >= height) return 0;
		return array[x + y * width];
	}
	
	public int getWater(int x, int y) {
		if(x < 0 || y < 0 || x >= width || y >= height) return 0;
		return waterArray[x + y * width];
	}
	
	public void setTile(int id, int x, int y) {
		if(x < 0 || y < 0 || x >= width || y >= height) return;
		array[x + y * width] = id;
		waterArray[x + y * width] = 0;
	}
	
	public void setWater(int id, int x, int y) {
		if(x < 0 || y < 0 || x >= width || y >= height) return;
		waterArray[x + y * width] = id;
	}
	
	//GET SOLID TILES, USED FOR ENTITY SPAWNING CHECK, AND PHYSICS
	public ArrayList<Vector2i> getSolidTiles(int x, int y, int radius) {
		ArrayList<Vector2i> tiles = new ArrayList<Vector2i>();
		int x1 = x - radius;
		int y1 = y - radius;
		int x2 = x + radius;
		int y2 = y + radius;
		for(int i = x1; i < x2; i++) {
			for(int j = y1; j < y2; j++) {
				int id = getTile(i, j);
				Tile tile = Tile.tiles.get(id);
				if(tile.solid) {
					tiles.add(new Vector2i(i * Tile.width, j * Tile.height));
				}
			}
		}
		return tiles;
	}
	
	//GET SPAWNABLE TILES, USED FOR ENTITY SPAWNING CHECK
	public ArrayList<Vector2i> getSpawnTiles(int x, int y, int radius) {
		ArrayList<Vector2i> tiles = new ArrayList<Vector2i>();
		int x1 = x - radius;
		int y1 = y - radius;
		int x2 = x + radius;
		int y2 = y + radius;
		for(int i = x1; i < x2; i++) {
			for(int j = y1; j < y2; j++) {
				Tile tile = Tile.tiles.get(getTile(i, j));
				Tile bottom = Tile.tiles.get(getTile(i, j + 1));
				if(!tile.solid && bottom.solid) {
					tiles.add(new Vector2i(i * Tile.width, j * Tile.height));
				}
			}
		}
		return tiles;
	}
	
	//GENERATE WORLD AND CAVES BASED ON SEED
	//USES PERLIN 2D NOISE WITH A ABS(x) TO GIVE THE APPEARANCE OF CAVES
	//SPAWN ENEMIES AND BOATS AFTER
	public void generate(int seed) {
		System.out.println("WORLD GENERATED WITH SEED: " + seed);
		
		game.enemies.clear();
		game.items.clear();
		game.fallingTiles.clear();
		game.boats.clear();
		game.ladders.clear();
		game.projectiles.clear();
		game.emitters.clear();
		game.clouds.clear();
		
		for(int i = 0; i < width * height; i++) {
			array[i] = 0;
			waterArray[i] = 0;
		}
		Noise2D noise = new Noise2D(seed, 5, 1f, 1/64f);
		
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				float value = Math.abs(noise.generate(x, y));
				if(value < .15) {
					array[x + y * width] = 0;
				}
				else {
					if(y == 0) array[x + y * width] = 1;
					//DIRT
					else if(y < 10) {
						if(y > 8) array[x + y * width] = Math.random() < .5 ? 2 : 3;
						else array[x + y * width] = 2;
					}
					//STONE
					else if (y < levels[1]) {
						array[x + y * width] = Math.random() < .98 ? 3 : 4;
					}
					//HOT STONE
					else {
						if(y == levels[1]) array[x + y * width] = Math.random() < .5 ? 3 : 5;
						else array[x + y * width] = 5;
					}
				}
				if(y > levels[2]) {
					if(getTile(x, y) != 0 && y == levels[2] + 1) array[x + y * width] = Math.random() < .5 ? 0 : 5;
					if(y >= levels[2] + 2 && y < levels[2] + 10) array[x + y * width] = 0;
					if(y >= levels[2] + 7 && y < levels[2] + 10 && (x >= width / 2 + 7 || x <= width / 2 - 7)) setWater(9, x, y);
					if(y >= levels[2] + 10) array[x + y * width] = 5;
					if(y == levels[2] + 9) array[x + y * width] = Math.random() < .5 ? 0 : 5;
					
					if(y >= levels[2] + 9 && y < levels[2] + 20 && x < width / 2 + 8 && x > width / 2 - 8) {
						array[x + y * width] = 0;
						if(y == levels[2] + 16 && x < width / 2 + 6 && x > width / 2 - 6) array[x + y * width] = 5;
						if(y == levels[2] + 10 && (x == width / 2 + 3 || x == width / 2 - 3)) array[x +  y * width] = 5;
					}
				}
			}
		}
		
		//ADD END BOSS
		game.enemies.add(new Boss(new Vector2f(width / 2 * Tile.width, (levels[2] + 12) * Tile.width), game));
		
		//ADD BOATS(LAVA ROCKS) TO BOSS LEVEL
		for(int i = 0; i < 100; i++) {
			game.boats.add(new Boat(new Vector2f(i * 3f * Tile.width, (levels[2] + 7) * Tile.height - 8), new Vector2f(1f, 0), game));
		}
		
		//ADD COMMON ENEMIES
		ArrayList<Vector2i> tiles = getSpawnTiles(width / 2, height / 2, width / 2);
		for(int i = 0; i < tiles.size(); i++) {
			Vector2i spawn = tiles.get((int) (Math.random() * tiles.size()));
			if(spawn.y < levels[2] * Tile.height) {
				float random = (float) Math.random();
				if(random < .05) game.enemies.add(new Enemy(new Vector2f(spawn), game));
				else if(random < .1) game.enemies.add(new Spider(new Vector2f(spawn), game));
			}
		}
		
		//GENERATE WORLD SPRITE(TESTING ONLY)
		int[] pixels = new int[width * height];
		for(int i = 0; i < width * height; i++) {
			pixels[i] = (int) ((array[i] / 3f) * 0xffffff);
		}
		sprite = new Sprite(pixels, width, height);
	}
	
}