package com.gammarush.engine.gui.animation;

import com.gammarush.engine.gui.UIContainer;
import com.gammarush.engine.math.vector.Vector2f;

//GUI COMPONENT WILL INCREASE WIDTH UNTIL FULLY VISIBLE

public class UIAnimationSlideOpen extends UIAnimation {
	
	private AnimationType type;
	private Vector2f step;
	
	public UIAnimationSlideOpen(UIContainer container, int time, AnimationType type) {
		super(container, time);
		this.type = type;
		step = new Vector2f();
		if(type == AnimationType.UP || type == AnimationType.DOWN) step.y = container.height / time;
		if(type == AnimationType.LEFT || type == AnimationType.RIGHT) step.x = container.width / time;
	}
	
	public void update() {
		if(!running) return;
		if(frame >= max) stop();
		else {
			width += step.x;
			height += step.y;
			if(type == AnimationType.UP) position.y -= step.y;
			if(type == AnimationType.LEFT) position.x -= step.x;
			frame++;
		}
	}
	
	public void start() {
		running = true;
		container.visible = true;
		if(type == AnimationType.UP) position.y += height;
		if(type == AnimationType.LEFT) position.x += width;
		if(type == AnimationType.LEFT || type == AnimationType.RIGHT) width = 0;
		if(type == AnimationType.UP || type == AnimationType.DOWN) height = 0;
	}

}
