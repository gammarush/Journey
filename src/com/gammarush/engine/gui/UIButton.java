package com.gammarush.engine.gui;

import com.gammarush.engine.graphics.Graphic2D;
import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.graphics.Sprite;
import com.gammarush.engine.math.vector.Vector2f;

//BUTTON COMPONENT
//CAN BE CLICKED AND HOVERED OVER

public class UIButton extends UIComponent {
	
	public String string = "";
	public int scale = 0;
	public Sprite sprite;

	public UIButton(Vector2f position, int width, int height) {
		super(position, width, height);
		sprite = new Sprite(0xffffff, width, height);
	}
	
	public UIButton(Vector2f position, int width, int height, int color) {
		super(position, width, height);
		sprite = new Sprite(color, width, height);
	}
	
	public UIButton(Vector2f position, int width, int height, Sprite sprite) {
		super(position, width, height);
		this.sprite = sprite.scale(width, height);
	}
	
	public void render(Renderer renderer) {
		sprite.render((int) (position.x + container.position.x), (int) (position.y + container.position.y), renderer);
		float string_width = string.length() * 3.2f;
		float string_height = 5.0f;
		int scale = (int) (width / (string_width * 2.0f));
		if(this.scale != 0) scale = this.scale;
		string_width *= scale;
		string_height *= scale;
		Graphic2D.drawString(string, (int) (position.x + container.position.x + width / 2 - string_width / 2), (int) (position.y + container.position.y + height / 2 - string_height / 2), scale, renderer);
	}

}