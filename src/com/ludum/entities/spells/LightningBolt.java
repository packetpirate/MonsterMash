package com.ludum.entities.spells;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ludum.Game;
import com.ludum.entities.EnemyFactory;
import com.ludum.entities.LightType;
import com.ludum.entities.enemies.Enemy;

public class LightningBolt extends Spell {
	private static final int MAX_JUMPS = 3;
	private static final int JUMP_RANGE = 300;
	
	public static List<Point2D.Double> strikes;
	public static long strikeTime;
	
	public LightningBolt() {
		super("Lightning Bolt", 3000, 80, 60);
		
		LightningBolt.strikes = new ArrayList<>();
		LightningBolt.strikeTime = 0;
	}
	
	@Override
	public void update() {
		synchronized(LightningBolt.strikes) {
			if(!LightningBolt.strikes.isEmpty() && (Game.time.getElapsedMillis() >= (LightningBolt.strikeTime + 1000))) {
				LightningBolt.strikes.clear();
			}
		}
	}
	
	@Override
	public void cast(Game game) {
		if(game.player.currentMana() >= manaCost) {
			game.player.useMana(manaCost);
			game.registerSpellEffect(new SpellEffect(this, new Point2D.Double(game.player.location.x, game.player.location.y), new Point2D.Double(game.screen.mousePos.x, game.screen.mousePos.y)) {
				{ // Begin pseudo-constructor.
					light = LightType.createLight(location, LightType.LIGHTNING_BOLT);
					game.lightFactory.lights.add(light);
					
					setFlag("struck", false);
				} // End pseudo-constructor.
				
				@Override
				public void reset() {
					strikes.clear();
					strikeTime = 0;
					
					setFlag("struck", false);
				}
				
				@Override
				public void update(Game game) {
					double dx = Math.cos(theta) * 5;
					double dy = Math.sin(theta) * 5;
					
					location.x += dx;
					location.y += dy;
					light.location.x = location.x;
					light.location.y = location.y;
					
					// Handle collisions with enemies.
					synchronized(game.enemies) {
						if(alive && !game.enemies.isEmpty()) {
							int jumps = 0;
							Iterator<Enemy> it = game.enemies.iterator();
							while(it.hasNext()) {
								Enemy e = it.next();
								
								double a = e.location.x - location.x;
								double b = e.location.y - location.y;
								double dist = Math.sqrt((a * a) + (b * b));
								if(alive && (getFlag("struck") == Boolean.FALSE) && e.isAlive() && (dist <= 10)) {
									alive = false;
									setFlag("struck", Boolean.TRUE);
									light.killLight();
									LightningBolt.strikes.add(new Point2D.Double(e.location.x, e.location.y));
									LightningBolt.strikeTime = Game.time.getElapsedMillis();
									game.lightFactory.createLight(new Point2D.Double(e.location.x, e.location.y), LightType.LIGHTNING_STRIKE);
									e.takeDamage(spell.damage);
								} else if((getFlag("struck") == Boolean.TRUE) && e.isAlive() && (dist <= LightningBolt.JUMP_RANGE) && (jumps < LightningBolt.MAX_JUMPS)) {
									e.takeDamage(spell.damage);
									LightningBolt.strikes.add(new Point2D.Double(e.location.x, e.location.y));
									game.lightFactory.createLight(new Point2D.Double(e.location.x, e.location.y), LightType.LIGHTNING_STRIKE);
									jumps++;
								}
							}
						}
					}
					
					// Handle collisions with factories.
					synchronized(game.factories) {
						if(alive && !game.factories.isEmpty()) {
							Iterator<EnemyFactory> it = game.factories.iterator();
							while(it.hasNext()) {
								EnemyFactory ef = it.next();
								
								if(alive && ef.isAlive() && ef.collision(location)) {
									alive = false;
									light.killLight();
									ef.takeDamage(spell.damage);
								}
							}
						}
					}
				}
				
				@Override
				public void render(Graphics2D g2d) {
					if(getFlag("struck") == Boolean.FALSE) {
						g2d.setColor(Color.WHITE);
						g2d.fillOval((int)(location.x - 5), (int)(location.y - 5), 10, 10);
					}
				}
			});
			
			lastCast = Game.time.getElapsedMillis();
		}
	}
}
