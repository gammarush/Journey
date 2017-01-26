package com.gammarush.engine.graphics;

import java.util.ArrayList;

//FONT CLASS USES A SPRITESHEET FULL OF CHARACTERS AND IS ABLE TO RENDER THEM TO SCREEN

public class Font {
	
	public SpriteSheet spritesheet;
	
	public ArrayList<Sprite> characters = new ArrayList<Sprite>();
	
	public Font(String path) {
		this.spritesheet = new SpriteSheet(path);
		int x = 0;
		for(int i = 0; i < 36; i++) {
			int width = 3;
			if(i == 23 || i == 26) width = 4;
			if(i == 22 || i == 32) width = 5;
			characters.add(new Sprite(spritesheet, x, 0, width, 5));
			x += width;
		}
	}
	
	//METHOD FOR DRAWING STRING TO SCREEN
	public void drawString(String string, int x, int y, int size, Renderer renderer) {
		//ITERATE THRU STRING
		for(int i = 0; i < string.length(); i++) {
			//CONVERT CHARACTERS TO KEYCODES
			int code = (int) string.charAt(i);
			if(code == 32) {
				x += (3.5f * size);
				continue;
			}
			int offset = 48;
			if(code >= 65) offset = 55;
			//FIND SPRITE TO GO WITH KEYCODE
			Sprite sprite = null;
			try {
				sprite = characters.get(code - offset);
			}
			catch(IndexOutOfBoundsException e) {
				return;
			}
			//DRAW SPRITE
			sprite.scale(sprite.width * size, sprite.height * size).render(x, y, renderer);
			//MOVE X POSITION FOR NEXT CHARACTER
			x += (int) Math.max(((sprite.width + .5f) * size), sprite.width + 1);
		}
	}
	
	//SAME METHOD AS ABOVE JUST WITH OPTIONAL TRANSPARENCY
	public void drawString(String string, int x, int y, int size, float alpha, Renderer renderer) {
		for(int i = 0; i < string.length(); i++) {
			int code = (int) string.charAt(i);
			if(code == 32) {
				x += (3.5f * size);
				continue;
			}
			int offset = 48;
			if(code >= 65) offset = 55;
			Sprite sprite = characters.get(code - offset);
			sprite.scale(sprite.width * size, sprite.height * size).render(x, y, alpha, renderer);
			x += (int) Math.max(((sprite.width + .5f) * size), sprite.width + 1);
		}
	}
	
}
