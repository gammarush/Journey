package com.gammarush.engine.listener;

import com.gammarush.engine.gui.UIComponent;
import com.gammarush.engine.gui.UIContainer;
import com.gammarush.engine.gui.event.EventType;
import com.gammarush.engine.math.vector.Vector2f;
import com.gammarush.engine.Game;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

//HANDLES ALL KEY AND MOUSE INPUTS, ROUTES THEM TO GAME LOGIC OR GUI

public class Listener {
	
	public Game game;
	
	public Vector2f hover = new Vector2f();
	public boolean keys[] = new boolean[255];
	public int scrollDelta = 0;
	
	public Listener(Game game) {
		this.game = game;
		game.frame.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				int code = e.getKeyCode();
				if(code == 222) code = '\'';
				if(e.isShiftDown()) {
					if(code == '1') code = '!';
					else if(code == '2') code = '@';
					else if(code == '3') code = '#';
					else if(code == '4') code = '$';
					else if(code == '5') code = '%';
					else if(code == '6') code = '^';
					else if(code == '7') code = '&';
					else if(code == '8') code = '*';
					else if(code == '9') code = '(';
					else if(code == '0') code = ')';
					else if(code == '-') code = '_';
					else if(code == '=') code = '+';
					else if(code == '[') code = '{';
					else if(code == ']') code = '}';
					else if(code == '\\') code = '|';
					else if(code == ';') code = ':';
					else if(code == '\'') code = '"';
					else if(code == ',') code = '<';
					else if(code == '.') code = '>';
					else if(code == '/') code = '?';
				}
				//IF GUI IS NOT RELEVANT, SEND TO GAME LOGIC KEY ARRAY
				if(!keyInput(code)) {
					keys[code] = true;
				}
				//IF BACKSPACE OR ESC, PAUSE GAME
				if((code == 8 || code == 27) && !game.paused) game.pause();
			}
			@Override
			public void keyReleased(KeyEvent e) {
				int code = e.getKeyCode();
				keys[code] = false;
			}
		});
		
		game.frame.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				try {
					int button = e.getButton();
					//CONVERT TO SCREEN COORDINATES
					int x = (int)(e.getX() / ((double) game.frame.getBounds().width / (double) game.renderer.width));
					int y = (int)(e.getY() / ((double) game.frame.getBounds().height / (double) game.renderer.height));
					if(button == 1) leftClick(x, y);
					if(button == 2) midClick(x, y);
					if(button == 3) rightClick(x, y);
				}
				catch (NullPointerException e1) {
					//IGNORE, GAME HASNT LOADED YET
				}
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				try {
					int button = e.getButton();
					//CONVERT TO SCREEN COORDINATES
					int x = (int)(e.getX() / ((double) game.frame.getBounds().width / (double) game.renderer.width));
					int y = (int)(e.getY() / ((double) game.frame.getBounds().height / (double) game.renderer.height));
					if(button == 1) leftRelease(x, y);
					if(button == 2) midRelease(x, y);
					if(button == 3) rightRelease(x, y);
				}
				catch (NullPointerException e1) {
					//IGNORE, GAME HASNT LOADED YET
				}
			}
		});
			
		game.frame.addMouseMotionListener(new MouseMotionAdapter() {	
			@Override
			public void mouseDragged(MouseEvent e) {
				try {
					//CONVERT TO SCREEN COORDINATES
					int x = (int)(e.getX() / ((double) game.frame.getBounds().width / (double) game.renderer.width));
					int y = (int)(e.getY() / ((double) game.frame.getBounds().height / (double) game.renderer.height));
					hover(x, y);
				}
				catch (NullPointerException e1) {
					//IGNORE, GAME HASNT LOADED YET
				}
			}
			@Override
			public void mouseMoved(MouseEvent e) {
				try {
					//CONVERT TO SCREEN COORDINATES
					int x = (int)(e.getX() / ((double) game.frame.getBounds().width / (double) game.renderer.width));
					int y = (int)(e.getY() / ((double) game.frame.getBounds().height / (double) game.renderer.height));
					hover(x, y);
				}
				catch (NullPointerException e1) {
					//IGNORE, GAME HASNT LOADED YET
				}
			}
		});
		
		game.frame.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				scrollDelta = e.getWheelRotation();
			}
		});
	}
	
	public boolean keyInput(int key) {
		boolean result = false;
		//TEST FOR RELEVANT GUI
		for(UIContainer cont : game.gui.containers) {
			if(cont.visible) {
				for(UIComponent comp : cont.components) {
					if(comp.focus && comp.editable) {
						comp.activate(EventType.KEYINPUT, key);
						result = true;
					}
				}
			}
		}
		return result;
	}
	
	public void leftClick(int x, int y) {
		//TEST FOR RELEVANT GUI
		UIContainer container = null;
		for(UIContainer cont : game.gui.containers) {
			if(cont.visible && cont.getCollision(x, y) && (container == null || container.index < cont.index)) {
				container = cont;
			}
		}
		if(container != null) {
			for(UIComponent comp : container.components) {
				if(comp.getCollision(x, y) && comp.clickable) {
				if(!comp.focus && comp.focusable) comp.focus = true;
					comp.click = true;
					comp.activate(EventType.LEFTCLICK);
				}
				else if(comp.focus && comp.focusable) comp.focus = false;
			}
		}
		//game.world.setTile(0, (int) Math.floor((x + game.renderer.position.x) / Tile.width), (int) Math.floor((y + game.renderer.position.y) / Tile.height));
		//game.clouds.add(new Cloud(new Vector2f(x, y).add(new Vector2f(game.renderer.position)), game));
		//game.world.setWater(0, (int) Math.floor((x + game.renderer.position.x) / Tile.width), (int) Math.floor((y + game.renderer.position.y) / Tile.height));
	}
	
	public void midClick(int x, int y) {
		//TEST FOR RELEVANT GUI
		for(UIContainer cont : game.gui.containers) {
			if(cont.visible && cont.getCollision(x, y)) {
				for(UIComponent comp : cont.components) {
					if(comp.getCollision(x, y) && comp.clickable) {
						comp.click = true;
						comp.activate(EventType.MIDCLICK);
					}
				}
			}
		}
		//game.renderer.screenshot();
	}
	
	public void rightClick(int x, int y) {
		//TEST FOR RELEVANT GUI
		for(UIContainer cont : game.gui.containers) {
			if(cont.visible && cont.getCollision(x, y)) {
				for(UIComponent comp : cont.components) {
					if(comp.getCollision(x, y) && comp.clickable) {
						comp.click = true;
						comp.activate(EventType.RIGHTCLICK);
					}
				}
			}
		}
		//game.world.setTile(3, (int) Math.floor((x + game.renderer.position.x) / Tile.width), (int) Math.floor((y + game.renderer.position.y) / Tile.height));
		//game.world.setTile(3, (int) Math.floor((x + game.renderer.position.x) / Tile.width), (int) Math.floor((y + game.renderer.position.y) / Tile.height));
		//game.enemies.add(new Enemy(new Vector2f(x + game.renderer.position.x, y + game.renderer.position.y), game));
	}
	
	public void leftRelease(int x, int y) {
		//TEST FOR RELEVANT GUI
		for(UIContainer cont : game.gui.containers) {
			if(cont.visible) {
				for(UIComponent comp : cont.components) {
					if(comp.click) {
						comp.click = false;
						comp.activate(EventType.LEFTRELEASE);
					}
				}
			}
		}
	}
	
	public void midRelease(int x, int y) {
		//TEST FOR RELEVANT GUI
		for(UIContainer cont : game.gui.containers) {
			if(cont.visible) {
				for(UIComponent comp : cont.components) {
					if(comp.click) {
						comp.click = false;
						comp.activate(EventType.MIDRELEASE);
					}
				}
			}
		}
	}
	
	public void rightRelease(int x, int y) {
		//TEST FOR RELEVANT GUI
		for(UIContainer cont : game.gui.containers) {
			if(cont.visible) {
				for(UIComponent comp : cont.components) {
					if(comp.click) {
						comp.click = false;
						comp.activate(EventType.RIGHTRELEASE);
					}
				}
			}
		}
	}
	
	public void hover(int x, int y) {
		hover.x = x;
		hover.y = y;
		//TEST FOR RELEVANT GUI
		UIContainer container = null;
		for(UIContainer cont : game.gui.containers) {
			if(cont.visible && cont.getCollision(x, y) && (container == null || container.index < cont.index)) {
				container = cont;
			}
		}
		if(container != null) {
			for(UIComponent comp : container.components) {
				if(comp.getCollision(x, y)) {
					if(!comp.hover) {
						comp.hover = true;
						comp.activate(EventType.HOVERENTER);
					}
				} else {
					if(comp.hover) {
						comp.hover = false;
						comp.activate(EventType.HOVEREXIT);
					}
				}
			}
		}
	}
	
}
