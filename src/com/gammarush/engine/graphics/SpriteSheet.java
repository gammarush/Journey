package com.gammarush.engine.graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

//LOADS IMAGES FROM RES FOLDER AND WILL BE PASSED TO THE SPRITE CLASS

public class SpriteSheet {
	
	private String path;
	public int[] pixels;
	public int width;
	public int height;
	
	public SpriteSheet(String path) {
		this.path = path;
		load();
	}
	
	//CONVERT IMAGE TO RAW PIXEL DATA
	private void load() {
		try {
			BufferedImage image = ImageIO.read(SpriteSheet.class.getResource(path));
			width = image.getWidth();
			height = image.getHeight();
			pixels = new int[width * height];
			image.getRGB(0, 0, width, height, pixels, 0, width);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
