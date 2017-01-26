package com.gammarush.engine.graphics;

import java.util.ArrayList;

import com.gammarush.engine.math.Mathf;

//TAKES SPRITESHEET AND IS ABLE TO SCALE, BLEND, ROTATE, AND RENDER THAT PIXEL DATA

public class Sprite {
	
	private int x;
	private int y;
	private int xo = 0;
	private int yo = 0;
	public int width;
	public int height;
	public int[] pixels;
	private SpriteSheet sheet;
	
	public Sprite(SpriteSheet sheet, int x, int y, int width, int height) {
		this.x = Math.min(x, sheet.width - 1);
		this.y = Math.min(y,  sheet.height - 1);
		this.width = Math.min(width, sheet.width - this.x);
		this.height = Math.min(height, sheet.height - this.y);
		pixels = new int[this.width * this.height];
		this.sheet = sheet;
		load();
	}
	
	public Sprite(SpriteSheet sheet, int width, int height) {
		this.x = 0;
		this.y = 0;
		this.width = Math.min(width, sheet.width);
		this.height = Math.min(height, sheet.height);
		pixels = new int[this.width * this.height];
		this.sheet = sheet;
		load();
	}
	
	public Sprite(SpriteSheet sheet) {
		this.x = 0;
		this.y = 0;
		this.width = sheet.width;
		this.height = sheet.height;
		pixels = new int[width * height];
		this.sheet = sheet;
		load();
	}
	
	public Sprite(int color, int width, int height) {
		pixels = new int[width * height];
		for(int i = 0; i < width * height; i++) {
			pixels[i] = color;
		}
		this.width = width;
		this.height = height;
	}
	
	public Sprite(int[] pixels, int width, int height) {
		this.pixels = pixels;
		this.width = width;
		this.height = height;
	}
	
	public Sprite(int[] pixels, int width, int height, int xo, int yo) {
		this.pixels = pixels;
		this.width = width;
		this.height = height;
		this.xo = xo;
		this.yo = yo;
	}
	
	//COPY PIXEL ARRAY FROM SPRITESHEET TO SPRITE OBJECT
	private void load() {
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				pixels[x + y * width] = sheet.pixels[(this.x + x) + (this.y + y) * sheet.width];
			}
		}
	}
	
	//BASIC RENDER METHOD FOR SPRITE
	public void render(int x, int y, Renderer renderer) {
		x -= xo;
		y -= yo;
		for(int ya = 0; ya < height; ya++) {
			int yb = ya + y;
			if(yb < 0 || yb >= renderer.height) continue;
			for(int xa = 0; xa < width; xa++) {
				int xb = xa + x;
				if(xb < 0 || xb >= renderer.width) continue;
				int color = pixels[xa + ya * width];
				if(color != 0xffff00ff && color != 0xff00ff) Graphic2D.drawPixel(xb, yb, color, renderer);
			}
		}
	}
	
	//REPEATS SPRITE VERTICALLY BASED ON GIVEN HEIGHT, USED TO RENDER LADDER IN GAME
	public void render(int x, int y, int height, Renderer renderer) {
		x -= xo;
		y -= yo;
		for(int ya = 0; ya < height; ya++) {
			int yb = ya + y;
			if(yb < 0 || yb >= renderer.height) continue;
			for(int xa = 0; xa < width; xa++) {
				int xb = xa + x;
				if(xb < 0 || xb >= renderer.width) continue;
				int yc = ya;
				while(yc >= this.height) yc -= this.height;
				int color = pixels[xa + yc * width];
				if(color != 0xffff00ff && color != 0xff00ff) Graphic2D.drawPixel(xb, yb, color, renderer);
			}
		}
	}
	
	//RENDER SPRITE WITH TRANSPARENCY
	public void render(int x, int y, float alpha, Renderer renderer) {
		if(alpha >= 1f) {
			render(x, y, renderer);
			return;
		}
		if(alpha <= 0f) {
			return;
		}
		x -= xo;
		y -= yo;
		for(int ya = 0; ya < height; ya++) {
			int yb = ya + y;
			if(yb < 0 || yb >= renderer.height) continue;
			for(int xa = 0; xa < width; xa++) {
				int xb = xa + x;
				if(xb < 0 || xb >= renderer.width) continue;
				int color = pixels[xa + ya * width];
				if(color != 0xffff00ff && color != 0xff00ff) Graphic2D.drawPixel(xb, yb, color, alpha, renderer);
			}
		}
	}
	
	//SCALE SPRITE TO GIVEN WIDTH AND HEIGHT
	public Sprite scale(int width, int height) {
		width = Math.max(width, 1);
		height = Math.max(height, 1);
		int[] new_pixels = new int[width * height];
		int xr = (int)((this.width << 16) / width) + 1;
		int yr = (int)((this.height << 16) / height) + 1;
		int x2, y2;
		for(int i = 0; i < height; i++) {
	        for(int j = 0; j < width; j++) {
	            x2 = ((j * xr) >> 16) ;
	            y2 = ((i * yr) >> 16) ;
	            new_pixels[(i * width) + j] = pixels[(y2 * this.width) + x2] ;
	        }                
	    }   
		Sprite new_sprite = new Sprite(new_pixels, width, height);
		return new_sprite;
	}
	
	//FLIP SPRITE PIXELS ACROSS Y AXIS
	public Sprite flip() {
		int[] new_pixels = new int[width * height];
		for(int i = 0; i < height; i++) {
	        for(int j = 0; j < width; j++) {
	        	new_pixels[i * width + j] = pixels[i * width + (width - 1 - j)];
	        }
		}
		return new Sprite(new_pixels, width, height);
	}
	
	//ROTATE SPRITE AROUND GIVEN POINT
	public Sprite rotate(float angle, int xp, int yp, boolean fixed) {
		angle = (float) Math.toRadians(angle);
		int[] new_pixels;
		int new_width;
		int new_height;
		int new_xo;
		int new_yo;
		if(fixed) {
			new_width = width;
			new_height = height;
			new_pixels = new int[width * height];
			new_xo = 0;
			new_yo = 0;
		} else {
			new_width = (int)(Mathf.getDistance(xp, yp, width, height) * 2 + 1);
			if(new_width % 2 != 0) new_width += 1;
			new_height = new_width;
			new_pixels = new int[new_width * new_height];
			new_xo = new_width / 2 - xp;
			new_yo = new_height / 2 - yp;
		}
		double r11 = Math.cos(angle);
		double r12 = -Math.sin(angle);
		double r21 = Math.sin(angle);
		double r22 = Math.cos(angle);
		int xd = (int) (((xp * r11) + (yp * r12)) - xp);
		int yd = (int) (((yp * r21) + (yp * r22)) - yp);
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				int xx = (int) (x * r11 + y * r12) - xd + new_xo;
				int yy = (int) (x * r21 + y * r22) - yd + new_yo;
				if(xx < 0 || xx >= new_width || yy < 0 || yy >= new_height) continue;
				new_pixels[xx + yy * new_width] = pixels[x + y * width];
			}
		}
		for(int i=0; i<new_pixels.length; i++) {
			if(i > 0 && i <= new_pixels.length - 2) {
				if(new_pixels[i] == 0 && new_pixels[i-1] != 0 && new_pixels[i+1] != 0) {
					new_pixels[i] = new_pixels[i-1] - (new_pixels[i-1] - new_pixels[i+1]);
				}
			}
		}
		for(int i=0; i<new_pixels.length; i++) {
			if(new_pixels[i] == 0) {
				new_pixels[i] = 0xffff00ff;
			}
		}
		Sprite new_sprite = new Sprite(new_pixels, new_width, new_height, new_xo, new_yo);
		return new_sprite;
	}
	
	//BLEND TWO SPRITES TOGETHER USING BLENDMAP AS A MASK
	public Sprite blend(Sprite sprite, ArrayList<Sprite> blendmaps) {
		int[] new_pixels = new int[width * height];
		for(int i = 0; i < pixels.length; i++) {
			
			int color1 = pixels[i];
			int r1 = (color1 >> 16) & 0xff;
			int g1 = (color1 >> 8) & 0xff;
			int b1 = (color1) & 0xff;
			
			int color2 = sprite.pixels[i];
			int r2 = (color2 >> 16) & 0xff;
			int g2 = (color2 >> 8) & 0xff;
			int b2 = (color2) & 0xff;
			
			float percent = 0;
			for(Sprite b : blendmaps) {
				percent += ((b.pixels[i] >> 16) & 0xff) / 255f;
			}
			percent = Math.min(percent, 1f);
			
			int r = (int) (r1 + percent * (r2 - r1));
			int g = (int) (g1 + percent * (g2 - g1));
			int b = (int) (b1 + percent * (b2 - b1));
			
			int color = ((r & 0x0ff) << 16) | ((g & 0x0ff) << 8) | (b & 0x0ff);
			new_pixels[i] = color;
		}
		return new Sprite(new_pixels, width, height, 0, 0);
	}
	
	//REPLACE TARGET COLOR IN SPRITE AS NEW COLOR
	public Sprite replace(int target, int color) {
		target += 0xff000000;
		for(int i = 0; i < pixels.length; i++) {
			int pixel = pixels[i];
			if(pixel == target) pixels[i] = color;
		}
		return new Sprite(pixels, width, height, 0, 0);
	}
}