package com.gammarush.engine.graphics;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.gammarush.engine.Game;
import com.gammarush.engine.math.vector.Vector2i;

//CLASS IN CHARGE OF ALL RENDERING IN THE GAME

public class Renderer {
	
	public Game game;
	public Vector2i position;
	public int width;
	public int height;
	public int[] pixels;
	
	public Font font;
	
	//GRAPHICS PERFORMANCE OPTIONS
	public boolean useTransparency = true;
	public boolean useBlendMaps = true;
	
	public Renderer(int width, int height, Game game) {
		this.game = game;
		this.position = new Vector2i();
		this.width = width;
		this.height = height;
		pixels = new int[width * height];
		//LOAD FONT FROM SPRITESHEET
		font = new Font("/fonts/font.png");
	}
	
	//CLEAR SCREEN TO A SKY BLUE COLOR
	public void clear() {
		for(int i = 0; i < pixels.length; i++) {
			pixels[i] = 0x79c9f9;
		}
	}
	
	public void render() {
		//SET CAMERA POSITION TO AN OFFSET OF PLAYER POSITION
		position.x = (int) (game.player.position.x - width / 2 + game.player.width / 2);
		position.y = (int) (game.player.position.y - height / 2 + game.player.height / 2);
		
		//RENDER WORLD TILES FIRST
		game.world.render(this);
		
		//RENDER ALL BOATS NEXT
		for(int i = 0; i < game.boats.size(); i++) {
			game.boats.get(i).render(this);
		}
		
		//RENDER ALL LADDERS NEXT
		for(int i = 0; i < game.ladders.size(); i++) {
			game.ladders.get(i).render(this);
		}
		
		//RENDER PLAYER NEXT
		game.player.render(this);
		
		//RENDER ALL ENEMIES (BOSS, ENEMY, AND SPIDER) NEXT
		for(int i = 0; i < game.enemies.size(); i++) {
			game.enemies.get(i).render(this);
		}
		
		//RENDER ALL ITEMS NEXT
		for(int i = 0; i < game.items.size(); i++) {
			game.items.get(i).render(this);
		}
		
		//RENDER ALL FALLING TILES NEXT
		for(int i = 0; i < game.fallingTiles.size(); i++) {
			game.fallingTiles.get(i).render(this);
		}
		
		//RENDER ALL PROJECTILES NEXT
		for(int i = 0; i < game.projectiles.size(); i++) {
			game.projectiles.get(i).render(this);
		}
		
		//RENDER ALL PARTICLES NEXT
		for(int i = 0; i < game.emitters.size(); i++) {
			game.emitters.get(i).render(this);
		}
		
		//RENDER LAVA NEXT
		game.world.renderWater(this);
		
		//RENDER END GAME CLOUDS NEXT
		for(int i = 0; i < game.clouds.size(); i++) {
			game.clouds.get(i).render(this);
		}
		
		//RENDER GUI ON TOP OF EVERYTHING
		game.gui.render(this);
		
		//game.world.sprite.render(8, 80, this);
	}
	
	//SCREENSHOT GAME AND STORE IN SCREENSHOTS FOLDER
	public void screenshot() {
		BufferedImage img = game.screen;
		int i = 0;
		while(new File("res/screenshots/screenshot" + i + ".png").exists()) {
			i++;
			if(i >= 100) return;
		}
		File file = new File("res/screenshots/screenshot" + i + ".png");
		try {
			ImageIO.write(img, "PNG", file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("SCREENSHOT SAVED");
	}
}
