package com.ludum.entities.spells;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Iterator;

import com.ludum.Game;
import com.ludum.entities.EnemyFactory;
import com.ludum.entities.LightType;
import com.ludum.entities.enemies.Enemy;
import com.ludum.gfx.Textures;

public class Fireball extends Spell {
	public static final String NAME = "Fireball";
	private static final int BLAST_RADIUS = 100;
	
	@Override
	public String getName() { return Fireball.NAME; }
	
	public Fireball() {
		super(1500, 25, 50);
	}
	
	@Override
	public void renderIcon(Graphics2D g2d, Point2D.Double position) {
		if(Textures.FIREBALL_ICON.img != null) {
			g2d.drawImage(Textures.FIREBALL_ICON.img, (int)position.x, (int)position.y, null);
		}
	}
	
	@Override
	public void cast(Game game) {
		if(game.player.currentMana() >= manaCost) {
			game.player.useMana(manaCost);
			game.currentLevel.spellEffects.add(new SpellEffect(this, new Point2D.Double(game.player.location.x, game.player.location.y), new Point2D.Double(game.screen.mousePos.x, game.screen.mousePos.y)) {
				{ // Begin pseudo-constructor.
					light = LightType.createLight(location, LightType.FIREBALL);
					game.currentLevel.lightFactory.lights.add(light);
					
					setFlag("exploded", false);
				} // End pseudo-constructor.
				
				@Override
				public void reset() {
					setFlag("exploded", false);
				}
				
				@Override
				public void update(Game game) {
					// Do fireball update stuff here.
					double dx = Math.cos(theta) * 5;
					double dy = Math.sin(theta) * 5;
					
					location.x += dx;
					location.y += dy;
					light.location.x = location.x;
					light.location.y = location.y;
					
					// Handle collisions with enemies.
					synchronized(game.currentLevel.enemies) {
						if(alive && !game.currentLevel.enemies.isEmpty()) {
							Iterator<Enemy> it = game.currentLevel.enemies.iterator();
							while(it.hasNext()) {
								Enemy e = it.next();
								
								double a = e.location.x - location.x;
								double b = e.location.y - location.y;
								double dist = Math.sqrt((a * a) + (b * b));
								if(alive && (getFlag("exploded") == Boolean.FALSE) && e.isAlive() && (dist <= 10)) {
									alive = false;
									setFlag("exploded", Boolean.TRUE);
									light.killLight();
									game.currentLevel.lightFactory.createLight(new Point2D.Double(location.x, location.y), LightType.EXPLOSION);
									e.takeDamage(spell.damage);
								} else if((getFlag("exploded") == Boolean.TRUE) && e.isAlive() && (dist <= Fireball.BLAST_RADIUS)) {
									e.takeDamage(spell.damage / 2);
								}
							}
						}
					}
					
					// Handle collisions with factories.
					synchronized(game.currentLevel.factories) {
						if(alive && !game.currentLevel.factories.isEmpty()) {
							Iterator<EnemyFactory> it = game.currentLevel.factories.iterator();
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
					if(Textures.FIREBALL.img != null) {
						AffineTransform saved = g2d.getTransform();
						
						int x = (int)(location.x - (Textures.FIREBALL.img.getWidth() / 2));
						int y = (int)(location.y - (Textures.FIREBALL.img.getHeight() / 2));
						AffineTransform rotate = AffineTransform.getRotateInstance((theta + (Math.PI / 2)), location.x, location.y);
						g2d.setTransform(rotate);
						g2d.drawImage(Textures.FIREBALL.img, x, y, null);
						
						g2d.setTransform(saved);
					} else {
						g2d.setColor(Color.ORANGE);
						g2d.fillOval((int)(location.x - 5), (int)(location.y - 5), 10, 10);
					}
				}
			});
			
			lastCast = Game.time.getElapsedMillis();
		}
	}
}
