package com.gammarush.engine.physics;

import java.util.ArrayList;
import java.util.List;

import com.gammarush.engine.Game;
import com.gammarush.engine.entities.Boat;
import com.gammarush.engine.math.vector.Vector2f;
import com.gammarush.engine.math.vector.Vector2i;
import com.gammarush.engine.tiles.Tile;

//PHYSICS COMPONENT FOR ENTITIES
//CAN DETECT AND FIX COLLISION AND CALCULATE GRAVITY/VELOCITY OF ENTITY

public class Physics {
	
	private Game game;
	private int width;
	private int height;

	public Physics(int width, int height, Game game) {
		this.game = game;
		this.width = width;
		this.height = height;
	}
	
	public Vector2f collision(Vector2f position) {
		int range = 2;
		Vector2f mtv = new Vector2f();
		
		//CREATE AXIS ALIGNED BOUNDING BOX FOR ENTITY
		AABB box = new AABB(position, width, height);
		
		//GET ALL SOLID TILES IN RANGE OF ENTITY
		List<Vector2i> tiles = game.world.getSolidTiles((int) ((position.x + width / 2) / Tile.width), (int) ((position.y + height / 2) / Tile.height), range);
		
		//ORGANIZE TILE AABB INTO SIMPLIFIED GROUPS BIGGER THAN INDIVIDUAL TILES
		List<AABB> groups = new ArrayList<AABB>();
		for(int i = 0; i < tiles.size(); i++) {
			Vector2i tile = tiles.get(i);
			boolean success = false;
			for(int j = 0; j < groups.size(); j++) {
				AABB group = groups.get(j);
				if(tile.x == group.x && tile.y == group.y + group.height) {
					group.height += Tile.height;
					success = true;
				}
			}
			if(!success) {
				AABB group = new AABB(tile.x, tile.y, Tile.width, Tile.height);
				groups.add(group);
			}
		}
		
		//ADD ALL BOAT HITBOXES TO GROUPS ARRAY
		for(int i = 0; i < game.boats.size(); i++) {
			Boat e = game.boats.get(i);
			groups.add(new AABB(e.position.add(0, 4), e.width, e.height - 4));
		}
		
		//ITERATE THRU HITBOX GROUPS
		for(int i = 0; i < groups.size(); i++) {
			AABB group_box = groups.get(i);
			//QUICK TEST FOR COLLISION OF HITBOXES
			if(getCollision(box, group_box)) {
				//FIX THE COLLISION SO THE HITBOXES DONT OVERLAP AND STORE THIS OFFSET IN A VECTOR
				Vector2f v = getTranslationVector(box, group_box);
				if(Math.abs(v.x) > Math.abs(mtv.x)) mtv.x = v.x;
				if(Math.abs(v.y) > Math.abs(mtv.y)) mtv.y = v.y;
			}
		}
		
		//RETURN OFFSET VECTOR (MINIMUM TRANSLATION VECTOR)
		return mtv;
	}
	
	//ADD TO DOWNWARD VELOCITY TO SIMULATE GRAVITY
	public Vector2i gravity(Vector2i velocity) {
		if(velocity.y < Tile.height / 2) velocity.y += 1;
		return velocity;
	}
	
	public Vector2f gravity(Vector2f velocity) {
		if(velocity.y < Tile.height / 2) velocity.y += 1f;
		return velocity;
	}
	
	public Vector2i gravity(Vector2i velocity, int power) {
		if(velocity.y < Tile.height / 2) velocity.y += power;
		return velocity;
	}
	
	public Vector2f gravity(Vector2f velocity, float power) {
		if(velocity.y < Tile.height / 2) velocity.y += power;
		return velocity;
	}
	
	//ADD TO UPWARD VELOCITY TO SIMULATE A JUMP OPPOSITE TO GRAVITY
	public Vector2i jump(Vector2i velocity, int power) {
		velocity.y = -power;
		return velocity;
	}
	
	public Vector2f jump(Vector2f velocity, float power) {
		velocity.y = -power;
		return velocity;
	}
	
	//SIMPLE RECTANGLE HITBOX COLLISION TEST, RETURN RESULT
	public static boolean getCollision(AABB a, AABB b) {
		if(a.x >= b.x + b.width || a.y >= b.y + b.height || a.x + a.width <= b.x || a.y + a.height <= b.y) return false;
		return true;
	}
	
	//FIND SHORTEST VECTOR TO FIX OVERLAPPING HITBOXES, RETURN VECTOR
	public static Vector2f getTranslationVector(AABB a, AABB b) {
		Vector2f mtv = new Vector2f();
		Vector2f amin = a.getMin();
		Vector2f amax = a.getMax();
		Vector2f bmin = b.getMin();
		Vector2f bmax = b.getMax();
		
		float left = (bmin.x - amax.x);
        float right = (bmax.x - amin.x);
        float top = (bmin.y - amax.y);
        float bottom = (bmax.y - amin.y);
        
        if(Math.abs(left) < right) mtv.x = left;
        else mtv.x = right;
        if(Math.abs(top) < bottom) mtv.y = top;
        else mtv.y = bottom;
        
        if(Math.abs(mtv.x) < Math.abs(mtv.y)) mtv.y = 0;
        else mtv.x = 0;
        
		return mtv;
	}
	
}
