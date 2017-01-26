package com.gammarush.engine.gui;

import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.graphics.Sprite;
import com.gammarush.engine.math.vector.Vector2f;

//IMAGE COMPONENT

public class UIImage extends UIComponent {
	
	public Sprite sprite;
	
	public UIImage(Vector2f position, int width, int height, Sprite sprite) {
		super(position, width, height);
		this.sprite = sprite.scale(width, height);
	}
	
	public void render(Renderer renderer) {
		if(visible) sprite.scale(width, height).render((int) (position.x + container.position.x), (int) (position.y + container.position.y), renderer);
	}

}
