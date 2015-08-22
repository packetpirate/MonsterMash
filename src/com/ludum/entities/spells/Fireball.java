package com.ludum.entities.spells;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.Iterator;

import com.ludum.Game;
import com.ludum.entities.EnemyFactory;
import com.ludum.entities.LightType;
import com.ludum.entities.enemies.Enemy;

public class Fireball extends Spell {
	public Fireball() {
		super("Fireball", 500, 10, 50);
	}
	
	@Override
	public void cast(Game game) {
		if(game.player.currentMana() >= manaCost) {
			game.player.useMana(manaCost);
			game.registerSpellEffect(new SpellEffect(new Point2D.Double(game.player.location.x, game.player.location.y), new Point2D.Double(game.screen.mousePos.x, game.screen.mousePos.y)) {
				{ // Begin pseudo-constructor.
					light = LightType.createLight(location, LightType.FIREBALL);
					game.lightFactory.lights.add(light);
				} // End pseudo-constructor.
				
				@Override
				public void update(Game game) {
					// Do fireball update stuff here.
					double dx = Math.cos(theta) * 10;
					double dy = Math.sin(theta) * 10;
					
					location.x += dx;
					location.y += dy;
					light.location.x = location.x;
					light.location.y = location.y;
					
					// Handle collisions with enemies.
					synchronized(game.enemies) {
						if(alive && !game.enemies.isEmpty()) {
							Iterator<Enemy> it = game.enemies.iterator();
							while(it.hasNext()) {
								Enemy e = it.next();
								
								double a = e.location.x - location.x;
								double b = e.location.y - location.y;
								double dist = Math.sqrt((a * a) + (b * b));
								if(alive && e.isAlive() && (dist <= 10)) {
									alive = false;
									light.alive = false;
									e.takeDamage(damage);
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
									light.alive = false;
									ef.takeDamage(damage);
								}
							}
						}
					}
				}
				
				@Override
				public void render(Graphics2D g2d) {
					g2d.setColor(Color.ORANGE);
					g2d.fillOval((int)(location.x - 5), (int)(location.y - 5), 10, 10);
				}
			});
			
			lastCast = Game.time.getElapsedMillis();
		}
	}
}
