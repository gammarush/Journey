package com.gammarush.engine.gui;

import com.gammarush.engine.gui.event.EventType;
import com.gammarush.engine.gui.event.UIEventHandler;
import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.math.vector.Vector2f;

//BASE GUI COMPONENT OTHER COMPONENTS EXTEND OFF OF

public class UIComponent {
	
	public Vector2f position;
	public int width;
	public int height;
	
	public UIContainer container;
	private UIEventHandler eventhandler;
	
	public boolean click = false;
	public boolean focus = false;
	public boolean hover = false;
	
	public boolean clickable = true;
	public boolean focusable = false;
	public boolean editable = false;
	public boolean resizable = false;
	
	public boolean visible = true;
	
	public UIComponent(Vector2f position, int width, int height) {
		this.position = position;
		this.width = width;
		this.height = height;
		setEventHandler(new UIEventHandler() {
			@Override
			public void leftClick() {
			}
			@Override
			public void midClick() {
			}
			@Override
			public void rightClick() {
			}
			@Override
			public void leftRelease() {
			}
			@Override
			public void midRelease() {
			}
			@Override
			public void rightRelease() {
			}
			@Override
			public void hoverEnter() {
			}
			@Override
			public void hoverExit() {
			}
			@Override
			public void keyInput(int key) {
			}
		});
	}
	
	public void render(Renderer renderer) {
		
	}
	
	public void activate(EventType type) {
		eventhandler.activate(type.id);
	}
	
	public void activate(int type) {
		eventhandler.activate(type);
	}
	
	public void activate(EventType type, int key) {
		eventhandler.keyInput(key);
	}
	
	public void setEventHandler(UIEventHandler eventhandler) {
		this.eventhandler = eventhandler;
	}
	
	public boolean getCollision(float x, float y) {
		if(x >= position.x + container.position.x + width || y >= position.y + container.position.y + height || x <= position.x + container.position.x || y <= position.y + container.position.y) return false;
		return true;
	}

}