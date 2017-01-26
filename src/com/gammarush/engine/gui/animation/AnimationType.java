package com.gammarush.engine.gui.animation;

//ENUM CLASS FOR GUI ANIMATION TYPES

public enum AnimationType {
	INVALID(-1), UP(0), DOWN(1), LEFT(2), RIGHT(3);
	public int id;
	private AnimationType(int id) {
		this.id = id;
	}
	
	public static AnimationType get(int id) {
		for(AnimationType animation : AnimationType.values()) {
			if(animation.id == id) return animation;
		}
		return INVALID;
	}
}
