package com.gammarush.engine.gui.event;

//ENUM CLASS FOR EVENT TYPES

public enum EventType {
	INVALID(-1), LEFTCLICK(0), MIDCLICK(1), RIGHTCLICK(2), LEFTRELEASE(3), MIDRELEASE(4), RIGHTRELEASE(5), HOVERENTER(6), HOVEREXIT(7), KEYINPUT(8);
	public int id;
	private EventType(int id) {
		this.id = id;
	}
	
	public static EventType get(int id) {
		for(EventType event : EventType.values()) {
			if(event.id == id) return event;
		}
		return INVALID;
	}
}