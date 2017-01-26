package com.gammarush.engine.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;

import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.graphics.Sprite;
import com.gammarush.engine.graphics.SpriteSheet;
import com.gammarush.engine.gui.UIContainer;
import com.gammarush.engine.gui.animation.UIAnimationFadeClose;
import com.gammarush.engine.gui.animation.UIAnimationFadeOpen;
import com.gammarush.engine.gui.event.EventType;
import com.gammarush.engine.gui.event.UIEventHandler;
import com.gammarush.engine.math.vector.Vector2f;
import com.gammarush.engine.Game;

//HOLDS ALL GUI CONTAINERS AND EVENT HANDLERS

public class UIManager {
	
	public Game game;
	
	public ArrayList<UIContainer> containers = new ArrayList<UIContainer>();
	
	//MAIN MENU CONTAINERS
	public UIContainer main;
	public UIContainer controls;
	public UIContainer options;
	
	//GENERAL GAMEOVER COMPONENTS CONTAINER
	public UIContainer gameover;
	
	//NAME TEXTBOX ON GAMEOVER SCREEN
	public UIContainer name;
	
	//LEADERBOARD ON GAMEOVER SCREEN
	public UIContainer table;
	private UIImage tableImage = new UIImage(new Vector2f(), 256, 198, new Sprite(new SpriteSheet("/gui/table.png")));
	public int gameoverStage = 0;
	
	//FADES SCREEN TO BLACK WHEN RELOADING WORLD
	public UIContainer cover;
	
	//SCORE AND LEVEL
	public UIContainer profile;
	
	//HEARTS IN THE MIDDLE
	public UIContainer health;
	
	//CONTROL ICONS IN BOTTOM LEFT
	public UIContainer arrow;
	public UIContainer jump;
	public UIContainer rope;
	
	//KEYBOARD GUI CONTROL INDICES
	public int mainSelectedIndex = 1;
	public int optionsSelectedIndex = 2;
	
	public UIManager(Game game) {
		this.game = game;
		init();
	}
	
	public void update() {
		for(UIContainer c : containers) {
			if(c.visible) c.update();
		}
		
		//SCORE
		((UITextBox)profile.components.get(0)).string = "SCORE " + game.score;
		//LEVEL
		((UITextBox)profile.components.get(1)).string = "LEVEL " + game.level;
		
		for(int i = 5; i < health.components.size(); i++) {
			if(i - 5 < game.player.lives) health.components.get(i).visible = true;
			else health.components.get(i).visible = false;
		}
		
		//KEYBOARD MAIN MENU GUI
		if(main.visible && !controls.visible && !options.visible) {
			if(game.listener.keys[38]) {
				main.components.get(mainSelectedIndex).activate(EventType.HOVEREXIT);
				mainSelectedIndex--;
				if(mainSelectedIndex < 1) mainSelectedIndex = 4;
				main.components.get(mainSelectedIndex).activate(EventType.HOVERENTER);
				game.listener.keys[38] = false;
			}
			if(game.listener.keys[40]) {
				main.components.get(mainSelectedIndex).activate(EventType.HOVEREXIT);
				mainSelectedIndex++;
				if(mainSelectedIndex > 4) mainSelectedIndex = 1;
				main.components.get(mainSelectedIndex).activate(EventType.HOVERENTER);
				game.listener.keys[40] = false;
			}
			if(game.listener.keys[10] || game.listener.keys[32]) {
				main.components.get(mainSelectedIndex).activate(EventType.LEFTRELEASE);
				game.listener.keys[10] = false;
				game.listener.keys[32] = false;
			}
		}
		if(controls.visible) {
			if(game.listener.keys[8] || game.listener.keys[27]) controls.components.get(1).activate(EventType.LEFTRELEASE);
		}
		if(options.visible) {
			if(game.listener.keys[38]) {
				options.components.get(optionsSelectedIndex).activate(EventType.HOVEREXIT);
				optionsSelectedIndex--;
				if(optionsSelectedIndex < 2) optionsSelectedIndex = 3;
				options.components.get(optionsSelectedIndex).activate(EventType.HOVERENTER);
				game.listener.keys[38] = false;
			}
			if(game.listener.keys[40]) {
				options.components.get(optionsSelectedIndex).activate(EventType.HOVEREXIT);
				optionsSelectedIndex++;
				if(optionsSelectedIndex > 3) optionsSelectedIndex = 2;
				options.components.get(optionsSelectedIndex).activate(EventType.HOVERENTER);
				game.listener.keys[40] = false;
			}
			if(game.listener.keys[10] || game.listener.keys[32]) {
				options.components.get(optionsSelectedIndex).activate(EventType.LEFTRELEASE);
				game.listener.keys[10] = false;
				game.listener.keys[32] = false;
			}
			if(game.listener.keys[8] || game.listener.keys[27]) options.components.get(1).activate(EventType.LEFTRELEASE);
		}
		
		//GAMEOVER GUI
		if(gameover.visible) {
			//ALLOW BACKSPACE ON ENTER NAME TEXTBOX AND AVOID WRONG FOCUSING
			if(gameoverStage == 3) {
				if(!name.components.get(0).focus && game.listener.keys[8]) {
					name.components.get(0).focus = true;
					((UITextBox)name.components.get(0)).string = ((UITextBox)name.components.get(0)).string.substring(0, Math.max(0, ((UITextBox)name.components.get(0)).string.length() - 1));
				}
				if(((UITextBox)name.components.get(0)).string.length() >= 3) ((UIButton)gameover.components.get(1)).string = "PRESS ENTER";
				else ((UIButton)gameover.components.get(1)).string = "ENTER NAME";
			}
			
			//FADE OUT WHEN RESTARTING GAME
			if(gameoverStage == 8) {
				cover.alpha += .01f;
				if(cover.alpha >= 1f) {
					cover.alpha = 1f;
					game.gamestart();
					gameoverStage++;
				}
			}
			
			//TEST FOR ENTER KEY ON GAMEOVER BUTTON
			if(game.listener.keys[10]) {
				gameover.components.get(1).activate(EventType.LEFTRELEASE);
			}
			else {
				gameover.components.get(1).activate(EventType.LEFTCLICK);
			}
		}
		
		//FADE IN START OF GAME
		if(gameoverStage == 9) {
			cover.alpha -= .01f;
			if(cover.alpha <= 0f) {
				cover.alpha = 0f;
				cover.visible = false;
				gameoverStage = 0;
			}
		}
		
	}
	
	public void render(Renderer renderer) {
		for(UIContainer c : containers) {
			if(c.visible) c.render(renderer);
		}
	}

	public void init() {
		main = new UIContainer(new Vector2f(), game.renderer.width, game.renderer.height, 0x000000, .7f);
		main.index = 1;
		UIImage title = new UIImage(new Vector2f(game.renderer.width / 2 - 128, 32), 256, 64, new Sprite(new SpriteSheet("/gui/title.png")));
		UIButton playButton = new UIButton(new Vector2f(game.renderer.width / 2 - 128, 128), 256, 48, 0x777777);
		playButton.string = "PLAY";
		playButton.scale = 4;
		UIButton controlsButton = new UIButton(new Vector2f(game.renderer.width / 2 - 128, 192), 256, 48, 0x777777);
		controlsButton.string = "ABOUT";
		controlsButton.scale = 4;
		UIButton optionsButton = new UIButton(new Vector2f(game.renderer.width / 2 - 128, 256), 256, 48, 0x777777);
		optionsButton.string = "OPTIONS";
		optionsButton.scale = 4;
		UIButton quitButton = new UIButton(new Vector2f(game.renderer.width / 2 - 128, 320), 256, 48, 0x777777);
		quitButton.string = "QUIT";
		quitButton.scale = 4;
		
		playButton.setEventHandler(new UIEventHandler() {
			@Override
			public void leftClick() {
			}
			@Override
			public void midClick() {
			}
			@Override
			public void rightClick() {
			}
			@Override
			public void leftRelease() {
				game.play();
			}
			@Override
			public void midRelease() {
			}
			@Override
			public void rightRelease() {
			}
			@Override
			public void hoverEnter() {
				playButton.sprite = new Sprite(0x999999, playButton.width, playButton.height);
			}
			@Override
			public void hoverExit() {
				playButton.sprite = new Sprite(0x777777, playButton.width, playButton.height);
			}
			@Override
			public void keyInput(int key) {
			}
		});
		
		controlsButton.setEventHandler(new UIEventHandler() {
			@Override
			public void leftClick() {
			}
			@Override
			public void midClick() {
			}
			@Override
			public void rightClick() {
			}
			@Override
			public void leftRelease() {
				controls.open.start();
			}
			@Override
			public void midRelease() {
			}
			@Override
			public void rightRelease() {
			}
			@Override
			public void hoverEnter() {
				controlsButton.sprite = new Sprite(0x999999, controlsButton.width, controlsButton.height);
			}
			@Override
			public void hoverExit() {
				controlsButton.sprite = new Sprite(0x777777, controlsButton.width, controlsButton.height);
			}
			@Override
			public void keyInput(int key) {
			}
		});
		
		optionsButton.setEventHandler(new UIEventHandler() {
			@Override
			public void leftClick() {
			}
			@Override
			public void midClick() {
			}
			@Override
			public void rightClick() {
			}
			@Override
			public void leftRelease() {
				options.open.start();
			}
			@Override
			public void midRelease() {
			}
			@Override
			public void rightRelease() {
			}
			@Override
			public void hoverEnter() {
				optionsButton.sprite = new Sprite(0x999999, optionsButton.width, optionsButton.height);
			}
			@Override
			public void hoverExit() {
				optionsButton.sprite = new Sprite(0x777777, optionsButton.width, optionsButton.height);
			}
			@Override
			public void keyInput(int key) {
			}
		});
		
		quitButton.setEventHandler(new UIEventHandler() {
			@Override
			public void leftClick() {
			}
			@Override
			public void midClick() {
			}
			@Override
			public void rightClick() {
			}
			@Override
			public void leftRelease() {
				System.exit(-1);
			}
			@Override
			public void midRelease() {
			}
			@Override
			public void rightRelease() {
			}
			@Override
			public void hoverEnter() {
				quitButton.sprite = new Sprite(0x999999, quitButton.width, quitButton.height);
			}
			@Override
			public void hoverExit() {
				quitButton.sprite = new Sprite(0x777777, quitButton.width, quitButton.height);
			}
			@Override
			public void keyInput(int key) {
			}
		});
		
		main.add(title);
		main.add(playButton);
		main.add(controlsButton);
		main.add(optionsButton);
		main.add(quitButton);
		
		controls = new UIContainer(new Vector2f(game.renderer.width / 2 - 128, 32), 256, 352, 0xaaaaaa);
		controls.index = 2;
		controls.visible = false;
		controls.setOpenAnimation(new UIAnimationFadeOpen(controls, 4));
		controls.setCloseAnimation(new UIAnimationFadeClose(controls, 8));
		
		UITextBox controlsTextBox = new UITextBox("ABOUT", 3, new Vector2f(4, 12));
		UIButton controlsExitButton = new UIButton(new Vector2f(controls.width - 36, 4), 32, 32, new Sprite(new SpriteSheet("/gui/cancel.png")));
		UITextBox controlsTextBox1 = new UITextBox("YOURE A PROFESSOR EXPLORING CAVES", 2, new Vector2f(4, 40));
		UITextBox controlsTextBox2 = new UITextBox("SAID TO REACH THE CENTER OF THE", 2, new Vector2f(4, 54));
		UITextBox controlsTextBox3 = new UITextBox("EARTH", 2, new Vector2f(4, 68));
		UITextBox controlsTextBox4 = new UITextBox("DODGE GOONS AND SPIDERS AS YOU", 2, new Vector2f(4, 94));
		UITextBox controlsTextBox5 = new UITextBox("MAKE YOUR WAY TO THE CORE", 2, new Vector2f(4, 108));
		UITextBox controlsTextBox6 = new UITextBox("UNCOVER WHATS AT THE CENTER AND", 2, new Vector2f(4, 134));
		UITextBox controlsTextBox7 = new UITextBox("MAKE IT OUT ALIVE", 2, new Vector2f(4, 148));
		UITextBox controlsTextBox8 = new UITextBox("MADE BY GAMMARUSH PRODUCTIONS", 2, new Vector2f(4, 332));
		
		controlsExitButton.setEventHandler(new UIEventHandler() {
			@Override
			public void leftClick() {
			}
			@Override
			public void midClick() {
			}
			@Override
			public void rightClick() {
			}
			@Override
			public void leftRelease() {
				controls.close.start();
			}
			@Override
			public void midRelease() {
			}
			@Override
			public void rightRelease() {
			}
			@Override
			public void hoverEnter() {
			}
			@Override
			public void hoverExit() {
			}
			@Override
			public void keyInput(int key) {
			}
		});
		
		controls.add(controlsTextBox);
		controls.add(controlsExitButton);
		controls.add(controlsTextBox1);
		controls.add(controlsTextBox2);
		controls.add(controlsTextBox3);
		controls.add(controlsTextBox4);
		controls.add(controlsTextBox5);
		controls.add(controlsTextBox6);
		controls.add(controlsTextBox7);
		controls.add(controlsTextBox8);
		
		options = new UIContainer(new Vector2f(game.renderer.width / 2 - 128, 32), 256, 352, 0xaaaaaa);
		options.index = 2;
		options.visible = false;
		options.setOpenAnimation(new UIAnimationFadeOpen(options, 4));
		options.setCloseAnimation(new UIAnimationFadeClose(options, 8));
		
		UITextBox optionsTextBox = new UITextBox("OPTIONS", 3, new Vector2f(4, 12));
		UIButton optionsExitButton = new UIButton(new Vector2f(options.width - 36, 4), 32, 32, new Sprite(new SpriteSheet("/gui/cancel.png")));
		UIButton transparencyButton = new UIButton(new Vector2f(4, 40), 248, 32, 0x008000);
		transparencyButton.string = "TOGGLE TRANSPARENCY";
		UIButton blendMapsButton = new UIButton(new Vector2f(4, 76), 248, 32, 0x008000);
		blendMapsButton.string = "TOGGLE BLEND MAPS";
		
		optionsExitButton.setEventHandler(new UIEventHandler() {
			@Override
			public void leftClick() {
			}
			@Override
			public void midClick() {
			}
			@Override
			public void rightClick() {
			}
			@Override
			public void leftRelease() {
				options.close.start();
			}
			@Override
			public void midRelease() {
			}
			@Override
			public void rightRelease() {
			}
			@Override
			public void hoverEnter() {
			}
			@Override
			public void hoverExit() {
			}
			@Override
			public void keyInput(int key) {
			}
		});
		
		transparencyButton.setEventHandler(new UIEventHandler() {
			@Override
			public void leftClick() {
			}
			@Override
			public void midClick() {
			}
			@Override
			public void rightClick() {
			}
			@Override
			public void leftRelease() {
				if(game.renderer.useTransparency) {
					game.renderer.useTransparency = false;
					transparencyButton.sprite = new Sprite(0xff3232, transparencyButton.width, transparencyButton.height);
				}
				else {
					game.renderer.useTransparency = true;
					transparencyButton.sprite = new Sprite(0x329932, transparencyButton.width, transparencyButton.height);
				}
			}
			@Override
			public void midRelease() {
			}
			@Override
			public void rightRelease() {
			}
			@Override
			public void hoverEnter() {
				if(game.renderer.useTransparency) transparencyButton.sprite = new Sprite(0x329932, transparencyButton.width, transparencyButton.height);
				else transparencyButton.sprite = new Sprite(0xff3232, blendMapsButton.width, transparencyButton.height);
			}
			@Override
			public void hoverExit() {
				if(game.renderer.useTransparency) transparencyButton.sprite = new Sprite(0x008000, transparencyButton.width, transparencyButton.height);
				else transparencyButton.sprite = new Sprite(0xff0000, transparencyButton.width, transparencyButton.height);
			}
			@Override
			public void keyInput(int key) {
			}
		});
		
		blendMapsButton.setEventHandler(new UIEventHandler() {
			@Override
			public void leftClick() {
			}
			@Override
			public void midClick() {
			}
			@Override
			public void rightClick() {
			}
			@Override
			public void leftRelease() {
				if(game.renderer.useBlendMaps) {
					game.renderer.useBlendMaps = false;
					blendMapsButton.sprite = new Sprite(0xff3232, blendMapsButton.width, blendMapsButton.height);
				}
				else {
					game.renderer.useBlendMaps = true;
					blendMapsButton.sprite = new Sprite(0x329932, blendMapsButton.width, blendMapsButton.height);
				}
			}
			@Override
			public void midRelease() {
			}
			@Override
			public void rightRelease() {
			}
			@Override
			public void hoverEnter() {
				if(game.renderer.useBlendMaps) blendMapsButton.sprite = new Sprite(0x329932, blendMapsButton.width, blendMapsButton.height);
				else blendMapsButton.sprite = new Sprite(0xff3232, blendMapsButton.width, blendMapsButton.height);
			}
			@Override
			public void hoverExit() {
				if(game.renderer.useBlendMaps) blendMapsButton.sprite = new Sprite(0x008000, blendMapsButton.width, blendMapsButton.height);
				else blendMapsButton.sprite = new Sprite(0xff0000, blendMapsButton.width, blendMapsButton.height);
			}
			@Override
			public void keyInput(int key) {
			}
		});
		
		options.add(optionsTextBox);
		options.add(optionsExitButton);
		options.add(transparencyButton);
		options.add(blendMapsButton);
		
		profile = new UIContainer(new Vector2f(0, 24), game.renderer.width, 64);
		UITextBox profileScore = new UITextBox("SCORE 0", 3, new Vector2f(24, 8));
		UITextBox profileLevel = new UITextBox("LEVEL 3", 3, new Vector2f(game.renderer.width - 96, 8));
		profile.add(profileScore);
		profile.add(profileLevel);
		
		health = new UIContainer(new Vector2f(game.renderer.width / 2 - 80, 24), 160, 32);
		Sprite heart = new Sprite(new SpriteSheet("/gui/heart.png"));
		Sprite emptyHeart = new Sprite(new SpriteSheet("/gui/emptyheart.png"));
		health.add(new UIImage(new Vector2f(8, 8), 16, 16, emptyHeart));
		health.add(new UIImage(new Vector2f(40, 8), 16, 16, emptyHeart));
		health.add(new UIImage(new Vector2f(72, 8), 16, 16, emptyHeart));
		health.add(new UIImage(new Vector2f(104, 8), 16, 16, emptyHeart));
		health.add(new UIImage(new Vector2f(136, 8), 16, 16, emptyHeart));
		health.add(new UIImage(new Vector2f(8, 8), 16, 16, heart));
		health.add(new UIImage(new Vector2f(40, 8), 16, 16, heart));
		health.add(new UIImage(new Vector2f(72, 8), 16, 16, heart));
		health.add(new UIImage(new Vector2f(104, 8), 16, 16, heart));
		health.add(new UIImage(new Vector2f(136, 8), 16, 16, heart));
		
		arrow = new UIContainer(new Vector2f(24, game.renderer.height - 56), 32, 32);
		arrow.add(new UIImage(new Vector2f(), 32, 32, new Sprite(new SpriteSheet("/gui/arrow.png"))));
		
		jump = new UIContainer(new Vector2f(60, game.renderer.height - 56), 32, 32);
		jump.add(new UIImage(new Vector2f(), 32, 32, new Sprite(new SpriteSheet("/gui/jump.png"))));
		
		rope = new UIContainer(new Vector2f(96, game.renderer.height - 56), 32, 32);
		rope.add(new UIImage(new Vector2f(), 32, 32, new Sprite(new SpriteSheet("/gui/rope.png"))));
		
		gameover = new UIContainer(new Vector2f(game.renderer.width / 2 - 240, 24), 480, 360);
		gameover.visible = false;
		
		UIButton gameoverTextButton = new UIButton(new Vector2f(gameover.width / 2 - 160, 0), 320, 64, 0xff00ff);
		gameoverTextButton.string = "GAME OVER ";
		gameoverTextButton.scale = 7;
		UIButton gameoverButton = new UIButton(new Vector2f(gameover.width / 2 - 128, gameover.height - 52), 256, 48, 0x777777);
		gameoverButton.string = "PRESS ENTER";
		gameoverButton.scale = 3;

		gameoverButton.setEventHandler(new UIEventHandler() {
			@Override
			public void leftClick() {
				if(gameoverStage == 0 || gameoverStage == 2 || gameoverStage == 4 || gameoverStage == 6) {
					gameoverStage++;
				}
			}
			@Override
			public void midClick() {
			}
			@Override
			public void rightClick() {
			}
			@Override
			public void leftRelease() {
				if(gameoverStage == 1) {
					name.visible = true;
					name.components.get(0).focus = true;
					gameoverStage++;
				}
				else if(gameoverStage == 3) {
					name.visible = false;
					table.visible = true;
					
					game.highscores.add(new Score(((UITextBox)name.components.get(0)).string, game.score));
					
					try {
						Collections.sort(game.highscores, new Comparator<Score>() {
					        @Override
					        public int compare(Score s1, Score s2) {
								if(s1.score < s2.score) return 1;
								else if(s1.score > s2.score) return -1;
								return 0;
					        }
					    });
					}
					catch(ConcurrentModificationException e) {
					}
					
					table.components.clear();
					table.components.add(tableImage);
					
					table.add(new UITextBox("   NAME", 3, new Vector2f(16, 8)));
					table.add(new UITextBox("SCORE", 3, new Vector2f(table.width / 2 + 32, 8)));
					
					for(int i = 0; i < 5; i++) {
						String name = "";
						String score = "";
						if(i < game.highscores.size()) {
							name = game.highscores.get(i).name;
							score = game.highscores.get(i).score + "";
						}
						UITextBox tableName = new UITextBox((i + 1) + "  " + name, 3, new Vector2f(16, i * 32 + 36));
						UITextBox tableScore = new UITextBox(score, 3, new Vector2f(table.width / 2 + 32, i * 32 + 36));
						table.add(tableName);
						table.add(tableScore);
					}
					
					((UITextBox)name.components.get(0)).string = "";
					
					gameoverStage++;
				}
				else if(gameoverStage == 5) {
					table.visible = false;
					((UIButton)gameover.components.get(1)).string = "PRESS ENTER";
					gameoverStage++;
				}
				else if(gameoverStage == 7) {
					cover.visible = true;
					gameoverStage++;
					//game.gamestart();
				}
			}
			@Override
			public void midRelease() {
			}
			@Override
			public void rightRelease() {
			}
			@Override
			public void hoverEnter() {
				gameoverButton.sprite = new Sprite(0x999999, playButton.width, playButton.height);
			}
			@Override
			public void hoverExit() {
				gameoverButton.sprite = new Sprite(0x777777, playButton.width, playButton.height);
			}
			@Override
			public void keyInput(int key) {
			}
		});
		
		gameover.add(gameoverTextButton);
		gameover.add(gameoverButton);
		
		name = new UIContainer(new Vector2f(game.renderer.width / 2 - 128, game.renderer.height / 2 - 16), 256, 32, 0x999999);
		name.visible = false;
		UITextBox nameTextBox = new UITextBox("", 4, true, new Vector2f(name.width / 2 - 32, name.height / 2 - 12));
		nameTextBox.focus = true;
		name.add(nameTextBox);
		
		table = new UIContainer(new Vector2f(game.renderer.width / 2 - 128, game.renderer.height / 2 - 98), 256, 198);
		table.visible = false;
		table.add(tableImage);
		
		cover = new UIContainer(new Vector2f(), game.renderer.width, game.renderer.height, 0x000000, 0f);
		cover.visible = false;
		
		containers.add(profile);
		containers.add(health);
		containers.add(arrow);
		containers.add(jump);
		containers.add(rope);
		containers.add(main);
		containers.add(controls);
		containers.add(options);
		containers.add(gameover);
		containers.add(name);
		containers.add(table);
		containers.add(cover);
	}
	
}