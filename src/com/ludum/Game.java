package com.ludum;

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
import com.ludum.entities.factories.Farm;
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
	
	public Player player;
	
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
		
		lightFactory = new LightFactory();
		factories = Collections.synchronizedList(new ArrayList<>());
		enemies = Collections.synchronizedList(new ArrayList<>());
		spellEffects = Collections.synchronizedList(new ArrayList<>());
		
		// TESTING
		
		// Add a peasant farm.
		Farm farm = new Farm(new Point2D.Double((Game.WIDTH - 50), 50));
		lightFactory.createLight(farm.getSpawnLocation(), LightType.TORCH);
		factories.add(farm);
		
		// TESTING
		
		player = new Player(this);
		player.moveTo(new Point2D.Double((Game.WIDTH / 2), (Game.HEIGHT / 2)));
		
		paused = false;
	}	
	
	public void update() {
		if(!isPaused()) {
			Game.time.update();
			
			// Update player information.
			player.update();
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
				
				if(screen.keys[0]) {
					player.move(0, -5);
				}
				if(screen.keys[1]) {
					player.move(-5, 0);
				}
				if(screen.keys[2]) {
					player.move(0, 5);
				}
				if(screen.keys[3]) {
					player.move(5, 0);
				}
				
				if(screen.mouseDown) {
					if(player.getCurrentSpell().canCast()) player.castSpell(this);
				}
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
							it.remove();
							continue;
						}
					}
				}
			}
			
			// Handle spawning enemies from factories.
			for(EnemyFactory factory : factories) {
				if(factory.canSpawn()) {
					synchronized(enemies) {
						enemies.add(factory.spawnEnemy());
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
							it.remove();
							continue;
						}
					}
				}
			}
		} else {
			Game.time.increaseOffset();
		}
	}
	
	public void render() {
		screen.repaint();
	}
	
	public void registerSpellEffect(SpellEffect effect) {
		spellEffects.add(effect);
	}
}
