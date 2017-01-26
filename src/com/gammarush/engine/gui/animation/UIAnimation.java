package com.gammarush.engine.gui.animation;

import com.gammarush.engine.gui.UIContainer;
import com.gammarush.engine.math.vector.Vector2f;

//PARENT CLASS FOR GUI ANIMATIONS

public class UIAnimation {
	
	public UIContainer container;
	public Vector2f position;
	public int width;
	public int height;
	public float alpha;
	
	public boolean running = false;
	public boolean complete = false;
	public int frame;
	public int max;
	
	public UIAnimation(UIContainer container) {
		this.container = container;
		position = new Vector2f(container.position);
		width = container.width;
		height = container.height;
		alpha = container.alpha;
		max = 0;
		frame = 0;
	}
	
	public UIAnimation(UIContainer container, int time) {
		this.container = container;
		position = new Vector2f(container.position);
		width = container.width;
		height = container.height;
		alpha = container.alpha;
		max = time;
		frame = 0;
	}
	
	public void update() {
		
	}
	
	//START ANIMATION
	public void start() {
		running = true;
		container.visible = true;
	}
	
	//STOP ANIMATION
	public void stop() {
		running = false;
		complete = true;
		frame = 0;
		position = new Vector2f(container.position);
		width = container.width;
		height = container.height;
		alpha = container.alpha;
	}
	
}