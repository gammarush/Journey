package com.gammarush.engine.entities;

import com.gammarush.engine.Game;
import com.gammarush.engine.graphics.Graphic2D;
import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.graphics.Sprite;
import com.gammarush.engine.math.vector.Vector2f;
import com.gammarush.engine.physics.AABB;
import com.gammarush.engine.physics.Physics;
import com.gammarush.engine.tiles.Tile;

//PROJECTILE THAT DAMAGES PLAYER ON HIT, USED BY BOSS ENEMY

public class Projectile {
	
	public Game game;
	public Vector2f position;
	public int width = Tile.width / 4;
	public int height = Tile.height / 4;
	
	public Vector2f velocity;
	public int age = 0;
	public int max = 60;
	public Sprite sprite;
	
	public Projectile(Vector2f position, Vector2f direction, float speed, Game game) {
		this.game = game;
		this.position = position;
		velocity = direction.mult(speed);
	}
	
	public void update() {
		//CALCULATE NEW POSITION
		position = position.add(velocity);
		
		//CHECK COLLISION WITH PLAYER AND DAMAGE PLAYER
		AABB box = new AABB(position, width, height);
		if(Physics.getCollision(box, new AABB(game.player.position, game.player.width, game.player.height))) {
			game.projectiles.remove(this);
			game.player.lives--;
		}
		
		//REMOVE PROJECTILE AFTER 1 SECOND
		if(age >= max) game.projectiles.remove(this);
		age++;
	}
	
	public void render(Renderer renderer) {
		//DRAW CIRCLE TO REPRESENT PROJECTILE
		Graphic2D.drawCircle((int) (position.x - renderer.position.x), (int) (position.y - renderer.position.y), width / 2, 0x000000, renderer);
	}
	
}
