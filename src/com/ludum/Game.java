package com.ludum;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;

import com.ludum.entities.Player;
import com.ludum.gfx.Screen;

public class Game {
	public static final String TITLE = "Monster Mash";
	public static final int WIDTH = 1024;
	public static final int HEIGHT = 576;
	
	public static GameTime time;
	public static GameState state;
	
	public Screen screen;
	public Level currentLevel;
	
	public Player player;
	
	public boolean running;
	public boolean reset;
	private boolean paused;
	public boolean isPaused() { return paused; }
	public void togglePause() {
		if(paused) paused = false;
		else paused = true;
	}
	
	public Game() {
		time = new GameTime();
		state = GameState.MENU;
		
		screen = new Screen(this);
		screen.setFocusable(true);
		
		screen.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent m) {
				if(Game.state == GameState.MENU) {
					screen.menu.dispatchClick(new Point2D.Double(screen.mousePos.x, screen.mousePos.y));
				} else if(Game.state == GameState.LEVEL_CLEAR) {
					screen.hud.dispatchClick(new Point2D.Double(screen.mousePos.x, screen.mousePos.y));
				} else if(Game.state == GameState.GAME_OVER) {
					screen.gameOver.dispatchClick(new Point2D.Double(screen.mousePos.x, screen.mousePos.y));
				}
			}
			
			@Override
			public void mousePressed(MouseEvent m) {
				screen.mouseDown = true;
			}
			
			@Override
			public void mouseReleased(MouseEvent m) {
				screen.mouseDown = false;
			}
		});
		
		screen.addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent m) {
				screen.mousePos = m.getPoint();
			}
			
			@Override
			public void mouseDragged(MouseEvent m) {
				screen.mousePos = m.getPoint();
			}
		});
		
		screen.addMouseWheelListener(new MouseAdapter() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent mw) {
				if((Game.state == GameState.GAME_STARTED) && !paused) {
					// Only do this if the player has more than one spell.
					if(player.getSpellCount() > 1) {
						int direction = mw.getWheelRotation();
						if(direction < 0) { // Mouse scrolled up.
							// Move spell selection to the right.
							if((player.getSelectedSpell() + 1) >= player.getSpellCount()) {
								player.selectSpell(0);
							} else player.selectSpell(player.getSelectedSpell() + 1);
						} else {			// Mouse scrolled down.
							// Move spell selection to the left.
							if((player.getSelectedSpell() - 1) < 0) {
								player.selectSpell(player.getSpellCount() - 1);
							} else player.selectSpell(player.getSelectedSpell() - 1);
						}
					}
				}
			}
		});
		
		screen.keys = new boolean[16];
		for(int i = 0; i < 16; i++) {
			screen.keys[i] = false;
		}
		screen.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent k) {
				int key = k.getKeyCode();
				
				if(key == KeyEvent.VK_ESCAPE) {
					if(!screen.keys[0]) running = false;
					screen.keys[0] = true;
				}
				
				if((Game.state == GameState.GAME_STARTED) || (Game.state == GameState.LEVEL_CLEAR)) {
					if(key == KeyEvent.VK_W) screen.keys[1] = true;
					if(key == KeyEvent.VK_A) screen.keys[2] = true;
					if(key == KeyEvent.VK_S) screen.keys[3] = true;
					if(key == KeyEvent.VK_D) screen.keys[4] = true;
					
					if(key == KeyEvent.VK_P) {
						if(!screen.keys[5]) togglePause();
						screen.keys[5] = true;
					}

					if(key == KeyEvent.VK_1) {
						if(!screen.keys[6] && (player.getSpellCount() >= 1)) player.selectSpell(0);
						screen.keys[6] = true;
					}
					if(key == KeyEvent.VK_2) {
						if(!screen.keys[7] && (player.getSpellCount() >= 2)) player.selectSpell(1);
						screen.keys[7] = true;
					}
					if(key == KeyEvent.VK_3) {
						if(!screen.keys[8] && (player.getSpellCount() >= 3)) player.selectSpell(2);
						screen.keys[8] = true;
					}
					if(key == KeyEvent.VK_4) {
						if(!screen.keys[9] && (player.getSpellCount() >= 4)) player.selectSpell(3);
						screen.keys[9] = true;
					}
					if(key == KeyEvent.VK_5) {
						if(!screen.keys[10] && (player.getSpellCount() >= 5)) player.selectSpell(4);
						screen.keys[10] = true;
					}
					if(key == KeyEvent.VK_6) {
						if(!screen.keys[11] && (player.getSpellCount() >= 6)) player.selectSpell(5);
						screen.keys[11] = true;
					}
					if(key == KeyEvent.VK_7) {
						if(!screen.keys[12] && (player.getSpellCount() >= 7)) player.selectSpell(6);
						screen.keys[12] = true;
					}
					if(key == KeyEvent.VK_8) {
						if(!screen.keys[13] && (player.getSpellCount() >= 8)) player.selectSpell(7);
						screen.keys[13] = true;
					}
					if(key == KeyEvent.VK_9) {
						if(!screen.keys[14] && (player.getSpellCount() >= 9)) player.selectSpell(8);
						screen.keys[14] = true;
					}
					if(key == KeyEvent.VK_0) {
						if(!screen.keys[15] && (player.getSpellCount() >= 10)) player.selectSpell(9);
						screen.keys[15] = true;
					}
				}
			}
			
			@Override
			public void keyReleased(KeyEvent k) {
				int key = k.getKeyCode();
				
				if(key == KeyEvent.VK_ESCAPE) screen.keys[0] = false;
				
				if((Game.state == GameState.GAME_STARTED) || (Game.state == GameState.LEVEL_CLEAR)) {
					if(key == KeyEvent.VK_W) screen.keys[1] = false;
					if(key == KeyEvent.VK_A) screen.keys[2] = false;
					if(key == KeyEvent.VK_S) screen.keys[3] = false;
					if(key == KeyEvent.VK_D) screen.keys[4] = false;
					
					if(key == KeyEvent.VK_P) screen.keys[5] = false;
					
					if(key == KeyEvent.VK_1) screen.keys[6] = false;
					if(key == KeyEvent.VK_2) screen.keys[7] = false;
					if(key == KeyEvent.VK_3) screen.keys[8] = false;
					if(key == KeyEvent.VK_4) screen.keys[9] = false;
					if(key == KeyEvent.VK_5) screen.keys[10] = false;
					if(key == KeyEvent.VK_6) screen.keys[11] = false;
					if(key == KeyEvent.VK_7) screen.keys[12] = false;
					if(key == KeyEvent.VK_8) screen.keys[13] = false;
					if(key == KeyEvent.VK_9) screen.keys[14] = false;
					if(key == KeyEvent.VK_0) screen.keys[15] = false;
				}
			}
		});
		
		player = new Player(this);
		currentLevel = Level.generateLevel(this);
		player.resetLight(this);
		
		running = false;
		paused = false;
		reset = false;
	}	
	
	public void update() {
		if(Game.state == GameState.MENU) {
			screen.menu.update(this);
			if(reset) reset = false;
		} else if((Game.state == GameState.GAME_STARTED) || (Game.state == GameState.LEVEL_CLEAR)) {
			if(reset) reset = false;
			if(!isPaused()) {
				Game.time.update();
				
				// Update player information.
				player.update(this);
				if(player.isAlive()) {
					if(screen.keys[1]) {
						player.move(0, -5);
					}
					if(screen.keys[2]) {
						player.move(-5, 0);
					}
					if(screen.keys[3]) {
						player.move(0, 5);
					}
					if(screen.keys[4]) {
						player.move(5, 0);
					}
					
					if(screen.mouseDown) {
						if(player.getCurrentSpell().canCast()) player.castSpell(this);
					}
				}
				
				if(!player.isAlive()) {
					Game.state = GameState.GAME_OVER;
				}
				
				currentLevel.update(this);
			} else {
				Game.time.increaseOffset();
			}
		} else if(Game.state == GameState.LEVEL_TRANSITION) {
			// Generate a new level.
			currentLevel = Level.generateLevel(this);
			player.levelTransition(this);
			Game.state = GameState.GAME_STARTED;
		} else if(Game.state == GameState.GAME_OVER) {
			screen.gameOver.update(this);
			if(!reset) {
				player.resetPlayer(this);
				currentLevel = Level.generateLevel(this);
				reset = true;
				paused = false;
			}
		}
	}
	
	public void render() {
		screen.repaint();
	}
}
