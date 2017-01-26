package com.gammarush.engine.gui;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import com.gammarush.engine.gui.UIComponent;
import com.gammarush.engine.gui.animation.UIAnimation;
import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.graphics.Sprite;
import com.gammarush.engine.math.vector.Vector2f;

//HOLDS ALL GUI COMPONENTS, ORGANIZES THEM

public class UIContainer {
	
	public Vector2f position;
	public int width;
	public int height;
	public float alpha = 1f;
	public int index = 0;
	public boolean visible = true;
	public boolean ready = true;
	public Sprite sprite;
	
	public UIAnimation open;
	public UIAnimation close;
	
	public ArrayList<UIComponent> components = new ArrayList<UIComponent>();
	
	public UIContainer(Vector2f position, int width, int height) {
		this.position = position;
		this.width = width;
		this.height = height;
		sprite = new Sprite(0xff00ff, width, height);
		setOpenAnimation(new UIAnimation(this));
		setCloseAnimation(new UIAnimation(this));
	}
	
	public UIContainer(Vector2f position, int width, int height, int color) {
		this.position = position;
		this.width = width;
		this.height = height;
		sprite = new Sprite(color, width, height);
		setOpenAnimation(new UIAnimation(this));
		setCloseAnimation(new UIAnimation(this));
	}
	
	public UIContainer(Vector2f position, int width, int height, int color, float alpha) {
		this.position = position;
		this.width = width;
		this.height = height;
		sprite = new Sprite(color, width, height);
		this.alpha = alpha;
		setOpenAnimation(new UIAnimation(this));
		setCloseAnimation(new UIAnimation(this));
	}
	
	public void update() {
		if(open.running) open.update();
		if(close.running) close.update();
		if(open.complete) {
			open.complete = false;
		}
		if(close.complete) {
			visible = false;
			close.complete = false;
		}
	}
	
	public void render(Renderer renderer) {
		if(open.running) sprite.scale(open.width, open.height).render((int) open.position.x, (int) open.position.y, open.alpha, renderer);
		else if(close.running) sprite.scale(close.width, close.height).render((int) close.position.x, (int) close.position.y, close.alpha, renderer);
		else {
			sprite.render((int) position.x, (int) position.y, alpha, renderer);
			try {
				for(UIComponent c : components) {
					c.render(renderer);
				}
			}
			catch (ConcurrentModificationException e) {
				
			}
		}
	}
	
	public void add(UIComponent component) {
		component.container = this;
		components.add(component);
	}
	
	public void setOpenAnimation(UIAnimation animation) {
		this.open = animation;
	}
	
	public void setCloseAnimation(UIAnimation animation) {
		this.close = animation;
	}
	
	public boolean getCollision(float x, float y) {
		if(x >= position.x + width || y >= position.y + height || x <= position.x || y <= position.y) return false;
		return true;
	}

}