package com.ludum;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.ludum.entities.EnemyFactory;
import com.ludum.entities.LightFactory;
import com.ludum.entities.LightType;
import com.ludum.entities.enemies.Enemy;
import com.ludum.entities.factories.Barracks;
import com.ludum.entities.factories.Chapel;
import com.ludum.entities.factories.Farm;
import com.ludum.entities.items.Grave;
import com.ludum.entities.minions.Minion;
import com.ludum.entities.spells.LightningBolt;
import com.ludum.entities.spells.SpellEffect;
import com.ludum.gfx.Screen;
import com.ludum.gfx.Textures;

public class Level {
	public LightFactory lightFactory;
	public List<EnemyFactory> factories;
	public List<Enemy> enemies;
	public List<SpellEffect> spellEffects;
	public List<Grave> graves;
	public List<Message> messages;
	
	public boolean isLevelClear() {
		return (factories.isEmpty() && enemies.isEmpty());
	}
	
	public Level() {
		lightFactory = new LightFactory();
		factories = Collections.synchronizedList(new ArrayList<>());
		enemies = Collections.synchronizedList(new ArrayList<>());
		spellEffects = Collections.synchronizedList(new ArrayList<>());
		graves = Collections.synchronizedList(new ArrayList<>());
		messages = Collections.synchronizedList(new ArrayList<>());
	}
	
	public void update(Game game) {
		// Handle spell effects.
		for(SpellEffect effect : spellEffects) {
			effect.update(game);
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
						it.remove();
						continue;
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
					
					e.update(game);
					if(!e.isAlive()) {
						game.player.addExperience(game, e.getExperience());
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
					
					g.update(game);
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
					
					msg.update(game);
					if(!msg.isActive()) {
						it.remove();
						continue;
					}
				}
			}
		}
		
		// Handle deleting dead lights.
		lightFactory.destroyLights();
		
		// If the level has been cleared, show a message.
		if(isLevelClear()) {
			if(Game.state != GameState.LEVEL_CLEAR) {
				messages.add(new Message("LEVEL CLEAR", 
										 new Point2D.Double((Game.WIDTH / 2), (Game.HEIGHT / 2)), 
										 -1));
				Game.state = GameState.LEVEL_CLEAR;
			}
		}
	}
	
	public void render(Graphics2D g2d, Game game) {
		if(Textures.GRASS.img != null) {
			BufferedImage grass = Textures.GRASS.img;
			int xR = (Game.WIDTH / grass.getWidth());
			int yR = (Game.HEIGHT / grass.getHeight());
			for(int row = 0; row < yR; row++) {
				for(int col = 0; col < xR; col++) {
					g2d.drawImage(grass, (col * grass.getWidth()), (row * grass.getHeight()), null);
				}
			}
		} else {
			g2d.setColor(new Color(0x336600));
			g2d.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
		}
		
		synchronized(spellEffects) {
			for(SpellEffect effect : spellEffects) {
				if(effect.alive) effect.render(g2d);
			}
		}
		
		// TODO: Figure out a better way to handle this.
		// Specific: Draw lightning bolt strikes.
		if(LightningBolt.strikes.size() > 1) {
			g2d.setColor(new Color(0xE6FFFF));
			g2d.setStroke(new BasicStroke(3.0f));
			for(int i = 0; i < (LightningBolt.strikes.size() - 1); i++) {
				Point2D.Double strike1 = LightningBolt.strikes.get(i);
				Point2D.Double strike2 = LightningBolt.strikes.get(i + 1);
				g2d.drawLine((int)strike1.x, (int)strike1.y, (int)strike2.x, (int)strike2.y);
			}
		}
		// End drawing lightning bolt strikes.
		
		synchronized(factories) {
			for(EnemyFactory factory : factories) {
				if(factory.isAlive()) factory.render(g2d);
			}
		}
		
		synchronized(graves) {
			for(Grave gr : graves) {
				if(gr.isAlive()) gr.render(g2d);
			}
		}
		
		synchronized(enemies) {
			for(Enemy enemy : enemies) {
				if(enemy.isAlive()) enemy.render(g2d, game);
			}
		}
		
		synchronized(game.player.getMinions()) {
			for(Minion m : game.player.getMinions()) {
				if(m.isAlive()) m.render(g2d);
			}
		}
		
		game.player.render(g2d);
		
		// Draw lights in the light factory.
		BufferedImage overlay = new BufferedImage(Game.WIDTH, Game.HEIGHT, BufferedImage.TYPE_INT_ARGB);
		Graphics2D og2d = overlay.createGraphics();
		og2d.setColor(new Color(0.0f, 0.0f, 0.0f, 0.85f));
		og2d.clearRect(0, 0, Game.WIDTH, Game.HEIGHT);
		lightFactory.render(overlay);
		
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.9f));
		g2d.drawImage(overlay, 0, 0, null);
		
		synchronized(messages) {
			for(Message msg : messages) {
				msg.render(g2d);
			}
		}
	}
	
	public void reset(Game game) {
		lightFactory.reset();
		
		spellEffects.clear();
		graves.clear();
		messages.clear();
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
	}
	
	public static Level generateLevel(Game game) {
		Level level = new Level();
		Random r = new Random();
		
		// Decide what enemy factories to populate the level with.
		{ // Spawn 1-2 Farms
			int farms = r.nextInt(1) + 1;
			for(int i = 0; i < farms; i++) {
				Farm farm = new Farm(Level.validFactoryPosition(r, level, Textures.FARM.img.getWidth()));
				farm.addLight(level.lightFactory.createLight(farm.getSpawnLocation(), LightType.TORCH));
				level.factories.add(farm);
			}
		}
		
		if(game.player != null) {
			if(game.player.getLevel() >= 5) {
				// Spawn a barracks.
				Barracks barracks = new Barracks(Level.validFactoryPosition(r, level, Textures.BARRACKS.img.getWidth()));
				barracks.addLight(level.lightFactory.createLight(barracks.getSpawnLocation(), LightType.TORCH));
				level.factories.add(barracks);
			}
			if(game.player.getLevel() >= 8) {
				// Spawn a chapel.
				Chapel chapel = new Chapel(Level.validFactoryPosition(r, level, Textures.CHAPEL.img.getWidth()));
				chapel.addLight(level.lightFactory.createLight(chapel.getSpawnLocation(), LightType.TORCH));
				level.factories.add(chapel);
			}
		}
		
		return level;
	}
	
	public static Point2D.Double validFactoryPosition(Random r, Level level, double factorySize) {
		boolean validPos = false;
		double minX = factorySize;
		double maxX = Game.WIDTH - factorySize;
		double minY = factorySize + 20;
		double maxY = Game.HEIGHT - factorySize;
		Point2D.Double position = new Point2D.Double();
		while(!validPos) {
			position.x = r.nextInt((int)(maxX - minX)) + minX;
			position.y = r.nextInt((int)(maxY - minY)) + minY;
			if(!level.factories.isEmpty()) {
				for(EnemyFactory factory : level.factories) {
					if(Screen.dist(position, factory.getPosition()) > factorySize) {
						validPos = true;
					}
				}
			} else validPos = true;
		}
		return position;
	}
}
