package com.gammarush.engine.physics;

import com.gammarush.engine.math.vector.Vector2f;

//AXIS ALIGNED BOUNDING BOX (HITBOX), USED FOR COLLISION OF ENTITIES, BASICALLY A RECTANGLE

public class AABB {
	
	public float x;
	public float y;
	public float width;
	public float height;
	
	public AABB(Vector2f pos, float width, float height) {
		this.x = pos.x;
		this.y = pos.y;
		this.width = width;
		this.height = height;
	}

	public AABB(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public Vector2f getMin() {
		return new Vector2f(x, y);
	}
	
	public Vector2f getMax() {
		return new Vector2f(x + width, y + height);
	}

}
