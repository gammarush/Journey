package com.gammarush.engine.gui.animation;

import com.gammarush.engine.gui.UIContainer;

//GUI COMPONENT WILL DECREASE TRANSPARENCY UNTIL FULLY VISIBLE

public class UIAnimationFadeOpen extends UIAnimation {
	
	private float step;
	
	public UIAnimationFadeOpen(UIContainer container, int time) {
		super(container, time);
		step = container.alpha / time;
	}
	
	public void update() {
		if(!running) return;
		if(frame >= max) stop();
		else {
			alpha += step;
			frame++;
		}
	}
	
	public void start() {
		running = true;
		container.visible = true;
		alpha = 0.0f;
	}

}
