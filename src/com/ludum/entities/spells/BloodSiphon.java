package com.ludum.entities.spells;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Iterator;

import com.ludum.Game;
import com.ludum.entities.LightType;
import com.ludum.entities.Status;
import com.ludum.entities.enemies.Enemy;
import com.ludum.gfx.Textures;

public class BloodSiphon extends Spell {
	public static final String NAME = "Blood Siphon";
	
	@Override
	public String getName() { return BloodSiphon.NAME; }
	
	public BloodSiphon() {
		super(10000, 50, 0);
	}
	
	@Override
	public void renderIcon(Graphics2D g2d, Point2D.Double position) {
		if(Textures.BLOOD_SIPHON_ICON.img != null) {
			g2d.drawImage(Textures.BLOOD_SIPHON_ICON.img, (int)position.x, (int)position.y, null);
		}
	}
	
	@Override
	public void cast(Game game) {
		if(game.player.currentMana() >= manaCost) {
			game.player.useMana(manaCost);
			game.currentLevel.spellEffects.add(new SpellEffect(this, new Point2D.Double(game.player.location.x, game.player.location.y), new Point2D.Double(game.screen.mousePos.x, game.screen.mousePos.y)) {
				{ // Begin pseudo-constructor.
					light = LightType.createLight(location, LightType.BLOOD_SIPHON);
					game.currentLevel.lightFactory.lights.add(light);
				} // End pseudo-constructor.
				
				@Override
				public void update(Game game) {
					// Handle collisions with enemies.
					synchronized(game.currentLevel.enemies) {
						if(alive && !game.currentLevel.enemies.isEmpty()) {
							Iterator<Enemy> it = game.currentLevel.enemies.iterator();
							while(it.hasNext()) {
								Enemy e = it.next();
								
								double a = e.location.x - location.x;
								double b = e.location.y - location.y;
								double dist = Math.sqrt((a * a) + (b * b));
								if(alive && e.isAlive() && (dist <= 16)) {
									alive = false;
									light.killLight();
									e.addStatus(new Status("siphon", 3000));
								}
							}
						}
					}
					
					double dx = Math.cos(theta) * 10;
					double dy = Math.sin(theta) * 10;
					
					location.x += dx;
					location.y += dy;
					light.location.x = location.x;
					light.location.y = location.y;
				}
				
				@Override
				public void render(Graphics2D g2d) {
					if(Textures.BLOOD_SIPHON.img != null) {
						AffineTransform saved = g2d.getTransform();
						
						int x = (int)(location.x - (Textures.BLOOD_SIPHON.img.getWidth() / 2));
						int y = (int)(location.y - (Textures.BLOOD_SIPHON.img.getHeight() / 2));
						AffineTransform rotate = AffineTransform.getRotateInstance((theta + (Math.PI / 2)), location.x, location.y);
						g2d.setTransform(rotate);
						g2d.drawImage(Textures.BLOOD_SIPHON.img, x, y, null);
						
						g2d.setTransform(saved);
					} else {
						g2d.setColor(new Color(0xBD2121));
						g2d.fillOval((int)(location.x - 2), (int)(location.y - 2), 4, 4);
					}
				}
			});
			
			lastCast = Game.time.getElapsedMillis();
		}
	}
}
