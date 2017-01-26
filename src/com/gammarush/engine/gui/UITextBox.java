package com.gammarush.engine.gui;

import com.gammarush.engine.gui.event.UIEventHandler;
import com.gammarush.engine.graphics.Graphic2D;
import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.math.vector.Vector2f;

//TEXTBOX COMPONENT
//STRING CAN BE EDITED WHEN FOCUSED

public class UITextBox extends UIComponent {
	
	public String string;
	public int scale;
	
	private UIEventHandler editEventHandler = new UIEventHandler() {
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
			int backspace = 8, enter = 10, shift = 16, space = 32;
			if(key == backspace) string = string.substring(0, Math.max(0, string.length() - 1));
			else if(string.length() < 6 && key != shift && key != space) string += (char) key;
			if(string.length() >= 6 || (string.length() >= 3 && key == enter)) focus = false;
		}
	};
	
	public UITextBox(String string, int scale, Vector2f position) {
		super(position, (int) (string.length() * 3.2f * scale), 5 * scale);
		this.string = string;
		this.scale = scale;
	}
	
	public UITextBox(String string, int scale, boolean editable, Vector2f position) {
		super(position, (int) (string.length() * 3.2f * scale), 5 * scale);
		this.string = string;
		this.scale = scale;
		if(editable) {
			//this.focusable = true;
			this.editable = true;
			setEventHandler(editEventHandler);
		}
	}
	
	public void render(Renderer renderer) {
		Graphic2D.drawString(string, (int) (position.x + container.position.x), (int) (position.y + container.position.y), scale, renderer);
	}
	
	public void setText(String string) {
		this.string = string;
		width = (int) (string.length() * 3.2f * scale);
	}
	
	public void setScale(int scale) {
		this.scale = scale;
		width = (int) (string.length() * 3.2f * scale);
		height = 5 * scale;
	}

}
