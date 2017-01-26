package com.gammarush.engine.gui.animation;

import com.gammarush.engine.gui.UIContainer;
import com.gammarush.engine.math.vector.Vector2f;

//GUI COMPONENT WILL DECREASE WIDTH UNTIL NOT VISIBLE

public class UIAnimationSlideClose extends UIAnimation {
	
	private AnimationType type;
	private Vector2f step;
	
	public UIAnimationSlideClose(UIContainer container, int time, AnimationType type) {
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
			width -= step.x;
			height -= step.y;
			if(type == AnimationType.DOWN) position.y += step.y;
			if(type == AnimationType.RIGHT) position.x += step.x;
			frame++;
		}
	}
	
}
