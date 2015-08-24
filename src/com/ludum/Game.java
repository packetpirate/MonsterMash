package com.ludum;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.ludum.entities.EnemyFactory;
import com.ludum.entities.LightFactory;
import com.ludum.entities.LightType;
import com.ludum.entities.Player;
import com.ludum.entities.enemies.Enemy;
import com.ludum.entities.factories.Barracks;
import com.ludum.entities.factories.Farm;
import com.ludum.entities.items.Grave;
import com.ludum.entities.spells.SpellEffect;
import com.ludum.gfx.Screen;

public class Game {
	public static final String TITLE = "Monster Mash";
	public static final int WIDTH = 640;
	public static final int HEIGHT = (WIDTH * 3) / 4;
	
	public static GameTime time;
	public static GameState state;
	
	public Screen screen;
	public LightFactory lightFactory;
	public List<EnemyFactory> factories;
	public List<Enemy> enemies;
	public List<SpellEffect> spellEffects;
	public List<Grave> graves;
	public List<Message> messages;
	
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
				
				if(Game.state == GameState.GAME_STARTED) {
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
				
				if(Game.state == GameState.GAME_STARTED) {
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
		
		lightFactory = new LightFactory();
		factories = Collections.synchronizedList(new ArrayList<>());
		enemies = Collections.synchronizedList(new ArrayList<>());
		spellEffects = Collections.synchronizedList(new ArrayList<>());
		graves = Collections.synchronizedList(new ArrayList<>());
		messages = Collections.synchronizedList(new ArrayList<>());
		
		// Add a peasant farm.
		Farm farm = new Farm(new Point2D.Double((Game.WIDTH - 128), 128));
		farm.addLight(lightFactory.createLight(farm.getSpawnLocation(), LightType.TORCH));
		factories.add(farm);
		
		// Add a barracks.
		Barracks barracks = new Barracks(new Point2D.Double((Game.WIDTH - 128), (Game.HEIGHT - 128)));
		barracks.addLight(lightFactory.createLight(barracks.getSpawnLocation(), LightType.TORCH));
		factories.add(barracks);
		
		player = new Player(this);
		
		running = false;
		paused = false;
		reset = false;
	}	
	
	public void update() {
		if(Game.state == GameState.MENU) {
			screen.menu.update(this);
			if(reset) reset = false;
		} else if(Game.state == GameState.GAME_STARTED) {
			if(reset) reset = false;
			if(!isPaused()) {
				Game.time.update();
				
				// Update player information.
				player.update(this);
				if(player.isAlive()) {
					// Check for collisions with enemies.
					synchronized(enemies) {
						if(!enemies.isEmpty()) {
							Iterator<Enemy> it = enemies.iterator();
							while(it.hasNext()) {
								Enemy e = it.next();
								
								double a = (e.location.x - player.location.x);
								double b = (e.location.y - player.location.y);
								double dist = Math.sqrt((a * a) + (b * b));
								if(e.isAlive() && player.canTakeDamage() && (dist <= 10)) {
									player.takeDamage(e.getDamage());
									continue;
								}
							}
						}
					}
					
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
				
				// Handle spell effects.
				for(SpellEffect effect : spellEffects) {
					effect.update(this);
				}
				
				// Remove spell effects that are out of bounds.
				synchronized(spellEffects) {
					if(!spellEffects.isEmpty()) {
						Iterator<SpellEffect> it = spellEffects.iterator();
						while(it.hasNext()) {
							SpellEffect effect = it.next();
							if((effect.location.x < 0) || (effect.location.x > Game.WIDTH) ||
							   (effect.location.y < 0) || (effect.location.y > Game.HEIGHT) ||
							   (!effect.alive)) {
								effect.light.killLight();
								it.remove();
								continue;
							}
						}
					}
				}
				
				synchronized(factories) {
					if(!factories.isEmpty()) {
						Iterator<EnemyFactory> it = factories.iterator();
						while(it.hasNext()) {
							EnemyFactory factory = it.next();
							
							if(!factory.isAlive()) {
								factory.getLight().killLight();
							}
							
							if(factory.canSpawn()) {
								synchronized(enemies) {
									enemies.add(factory.spawnEnemy());
								}
							}
						}
					}
				}
				
				// Update enemy information.
				synchronized(enemies) {
					if(!enemies.isEmpty()) {
						Iterator<Enemy> it = enemies.iterator();
						while(it.hasNext()) {
							Enemy e = it.next();
							
							e.update(this);
							if(!e.isAlive()) {
								player.addExperience(this, e.getExperience());
								e.origin.enemyDeath();
								graves.add(new Grave(new Point2D.Double(e.location.x, e.location.y)));
								it.remove();
								continue;
							}
						}
					}
				}
				
				// ...update... graves?
				synchronized(graves) {
					if(!graves.isEmpty()) {
						Iterator<Grave> it = graves.iterator();
						while(it.hasNext()) {
							Grave g = it.next();
							
							g.update(this);
							if(!g.isAlive()) {
								it.remove();
								continue;
							}
						}
					}
				}
				
				// Delete expired messages.
				synchronized(messages) {
					if(!messages.isEmpty()) {
						Iterator<Message> it = messages.iterator();
						while(it.hasNext()) {
							Message msg = it.next();
							
							msg.update(this);
							if(!msg.isActive()) {
								it.remove();
								continue;
							}
						}
					}
				}
				
				// Handle deleting dead lights.
				lightFactory.destroyLights();
			} else {
				Game.time.increaseOffset();
			}
		} else if(Game.state == GameState.GAME_OVER) {
			screen.gameOver.update(this);
			if(!reset) {
				lightFactory.reset();
				for(EnemyFactory factory : factories) {
					factory.reset();
				}
				factories.clear();
				for(Enemy e : enemies) {
					e.reset();
				}
				enemies.clear();
				for(SpellEffect spe : spellEffects) {
					spe.reset();
				}
				spellEffects.clear();
				graves.clear();
				messages.clear();
				
				// Add a peasant farm.
				Farm farm = new Farm(new Point2D.Double((Game.WIDTH - 128), 128));
				farm.addLight(lightFactory.createLight(farm.getSpawnLocation(), LightType.TORCH));
				factories.add(farm);
				
				// Add a barracks.
				Barracks barracks = new Barracks(new Point2D.Double((Game.WIDTH - 128), (Game.HEIGHT - 128)));
				barracks.addLight(lightFactory.createLight(barracks.getSpawnLocation(), LightType.TORCH));
				factories.add(barracks);
				
				player.resetPlayer(this);
				reset = true;
				paused = false;
			}
		}
	}
	
	public void render() {
		screen.repaint();
	}
	
	public void registerSpellEffect(SpellEffect effect) {
		spellEffects.add(effect);
	}
}
