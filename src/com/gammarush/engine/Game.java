package com.gammarush.engine;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import com.gammarush.engine.entities.Boat;
import com.gammarush.engine.entities.Cloud;
import com.gammarush.engine.entities.Enemy;
import com.gammarush.engine.entities.FallingTile;
import com.gammarush.engine.entities.Item;
import com.gammarush.engine.entities.Ladder;
import com.gammarush.engine.entities.Player;
import com.gammarush.engine.entities.Projectile;
import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.graphics.Sprite;
import com.gammarush.engine.graphics.SpriteSheet;
import com.gammarush.engine.gui.Score;
import com.gammarush.engine.gui.UIManager;
import com.gammarush.engine.listener.Listener;
import com.gammarush.engine.particles.ParticleEmitter;
import com.gammarush.engine.sound.Sound;
import com.gammarush.engine.tiles.Tile;
import com.gammarush.engine.world.World;

//MAIN CLASS OF PROJECT, IMPLEMENTS RUNNABLE FOR GAME THREAD MANAGEMENT

public class Game implements Runnable {
	
	public JFrame frame;
	private Thread thread;
	private boolean running = false;
	
	//GAME STATES
	public boolean paused = true;
	public boolean complete = false;
	public boolean failed = false;
	
	public int score = 0;
	public int level = 1;
	
	public int fps = 0;
	public int ups = 0;
	private int width = 720;
	private int height = width / 16 * 9;
	private int scale = 3;
	
	//CONVERTS IMAGE TO INTEGER ARRAY FOR PIXELS
	public BufferedImage screen = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt) screen.getRaster().getDataBuffer()).getData();
	
	//HANDLES ALL KEYBOARD AND MOUSE INPUTS
	public Listener listener;
	
	//HANDLES ALL DRAWING IN GAME
	public Renderer renderer;
	
	//MANAGES ALL GUI
	public UIManager gui;
	
	//WORLD GAME IS SET IN
	public World world;
	
	//ALL ENTITY AND OBJECT ARRAYS
	public Player player;
	public ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	public ArrayList<Item> items = new ArrayList<Item>();
	public ArrayList<FallingTile> fallingTiles = new ArrayList<FallingTile>();
	public ArrayList<Boat> boats = new ArrayList<Boat>();
	public ArrayList<Ladder> ladders = new ArrayList<Ladder>();
	public ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
	public ArrayList<ParticleEmitter> emitters = new ArrayList<ParticleEmitter>();
	public ArrayList<Cloud> clouds = new ArrayList<Cloud>();
	
	public ArrayList<Score> highscores = new ArrayList<Score>();
	
	public Game() {
		//INIT JFRAME (GAME WINDOW)
		frame = new JFrame("Journey");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(width * scale, height * scale);
		frame.setIconImage(new ImageIcon(Game.class.getResource("/gui/icon.png")).getImage());
		frame.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(new ImageIcon(Game.class.getResource("/gui/cursor.png")).getImage(), new Point(0,0), "cursor"));
		frame.setResizable(true);
		frame.setFocusable(true);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		
		//INIT LISTENER, RENDERER, AND GUI MANAGER
		listener = new Listener(this);
		renderer = new Renderer(width, height, this);
		gui = new UIManager(this);
		
		//LOOP BACKGROUND MUSIC
		Sound.loop("/sounds/music1.wav", -25f);
		
		//SET FULLSCREEN
		renderer.setFullScreen(true);
	}
	
	//START GAME THREAD
	public synchronized void start() {
		init();
		running = true;
		thread = new Thread(this);
		thread.start();
	}
	
	//STOP GAME THREAD
	public synchronized void stop() {
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	//PAUSE GAME AND SHOW GAME MENU
	public void pause() {
		if(!gui.gameover.visible && !gui.restartConfirmation.visible) {
			paused = true;
			
			gui.main.visible = true;
			
			gui.health.visible = false;
			gui.arrow.visible = false;
			gui.jump.visible = false;
			gui.rope.visible = false;
			gui.restart.visible = false;
		}
	}
	
	//UNPAUSE GAME
	public void play() {
		paused = false;
		
		gui.main.visible = false;
		
		gui.health.visible = true;
		gui.arrow.visible = true;
		gui.jump.visible = true;
		gui.rope.visible = true;
		gui.restart.visible = true;
	}

	//RUN METHOD FOR GAME THREAD
	//TARGET UPDATE PER SECOND RATE IS 60 UPS
	//UNLIMITED FPS
	public void run() {
		int UPS_CAP = 60;
		int FPS_CAP = 60;
		boolean FPS_CAP_ON = false;
		int updates = 0;
		int frames = 0;

		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		final double us = 1000000000.0 / UPS_CAP;
		final double fs = 1000000000.0 / FPS_CAP;
		double udelta = 0;
		double fdelta = 0;
		while (running) {
			long now = System.nanoTime();
			udelta += (now - lastTime) / us;
			if (FPS_CAP_ON)
				fdelta += (now - lastTime) / fs;
			lastTime = now;
			while (udelta >= 1) {
				update();
				updates++;
				udelta--;
			}
			if (FPS_CAP_ON) {
				while (fdelta >= 1) {
					render();
					frames++;
					fdelta--;
				}
			} else {
				render();
				frames++;
			}

			if (System.currentTimeMillis() - timer > 1000) {
				timer += (System.currentTimeMillis() - timer);
				fps = frames;
				ups = updates;
				updates = 0;
				frames = 0;
				
				//frame.setTitle("Journey [FPS: " + fps + "]");
				//System.out.println(fps);
			}
		}
		stop();
	}
	
	//MASTER UPDATE METHOD
	public void update() {
		//IF NOT PAUSED, DO GAME LOGIC
		if(!paused) {
			world.update();
			
			player.update();
			
			for(int i = 0; i < enemies.size(); i++) {
				enemies.get(i).update();
			}
			
			for(int i = 0; i < items.size(); i++) {
				items.get(i).update();
			}
			
			for(int i = 0; i < fallingTiles.size(); i++) {
				fallingTiles.get(i).update();
			}
			
			for(int i = 0; i < boats.size(); i++) {
				boats.get(i).update();
			}
			
			for(int i = 0; i < ladders.size(); i++) {
				ladders.get(i).update();
			}
			
			for(int i = 0; i < projectiles.size(); i++) {
				projectiles.get(i).update();
			}
			
			for(int i = 0; i < emitters.size(); i++) {
				emitters.get(i).update();
			}
			
			for(int i = 0; i < clouds.size(); i++) {
				clouds.get(i).update();
			}
		}
		//UPDATE GUI EVEN IF PAUSED
		gui.update();
		renderer.update();
	}

	//MASTER RENDER METHOD
	public void render() {
		BufferStrategy bs = frame.getBufferStrategy();
		if (bs == null) {
			frame.createBufferStrategy(2);
			return;
		}

		//CLEAR SCREEN
		renderer.clear();
		
		//CALL RENDERER TO RENDER NEW FRAME
		renderer.render();
		
		//CONVERT PIXEL ARRAY AND DRAW NEW FRAME TO JFRAME
		for(int i = 0; i < pixels.length; i++) {
			pixels[i] = renderer.pixels[i];
		}

		Graphics g = bs.getDrawGraphics();
		g.drawImage(screen, 0, 0, frame.getWidth(), frame.getHeight(), null);
		g.dispose();
		bs.show();
	}
	
	//INIT ALL TILES, WORLD, AND PLAYER ON STARTUP
	public void init() {
		new Tile(0, false, 1, new Sprite(new SpriteSheet("/tiles/tile0.png")));
		new Tile(1, true, 1, new Sprite(new SpriteSheet("/tiles/tile1.png")));
		new Tile(2, true, 2, new Sprite(new SpriteSheet("/tiles/tile2.png")));
		new Tile(3, true, 2, new Sprite(new SpriteSheet("/tiles/tile3.png")));
		new Tile(4, true, 2, new Sprite(new SpriteSheet("/tiles/tile3g.png")));
		new Tile(5, true, 2, new Sprite(new SpriteSheet("/tiles/tile4.png")));
		new Tile(6, true, 2, new Sprite(new SpriteSheet("/tiles/tile5.png")));
		
		world = new World(256, 256, this);
		world.generate(World.getRandomSeed());
		player = new Player(world.getStartPosition().mult(Tile.width, Tile.height), this);
		//player = new Player(new Vector2f(world.width / 2 * Tile.width, world.levels[2] * Tile.height), this);
	}
	
	//CALLED AFTER LEADERBOARD IS SHOWN ON GAMEOVER TO RESET GAME
	public void gamestart() {
		//RESET EVERYTHING ON NEW GAME
		complete = false;
		failed = false;
		score = 0;
		level = 1;
		
		gui.health.visible = true;
		gui.arrow.visible = true;
		gui.jump.visible = true;
		gui.rope.visible = true;
		gui.restart.visible = true;
		gui.gameover.visible = false;
		
		pause();
		
		world = new World(256, 256, this);
		world.generate(World.getRandomSeed());
		player = new Player(world.getStartPosition().mult(Tile.width, Tile.height), this);
	}
	
	//CALLED ON PLAYER DEATH OR GAME COMPLETION (GAME OVER)
	public void gameover() {
		//MANAGE GAMEOVER GUI
		gui.gameover.visible = true;
		gui.health.visible = false;
		gui.arrow.visible = false;
		gui.jump.visible = false;
		gui.rope.visible = false;
		gui.restart.visible = false;
		gui.restartConfirmation.visible = false;
	}
	
	public void restartConfirmation() {
		//MANAGE RESTART GUI
		gui.restartConfirmation.visible = true;
		gui.health.visible = false;
		gui.arrow.visible = false;
		gui.jump.visible = false;
		gui.rope.visible = false;
		gui.restart.visible = false;
	}

	public static void main(String[] args) {
		//START GAME THREAD
		Game game = new Game();
		game.start();
	}

}
