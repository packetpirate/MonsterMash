package com.ludum.entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Iterator;

import com.ludum.Game;
import com.ludum.entities.spells.SkullBarrier;
import com.ludum.entities.spells.SpellEffect;
import com.ludum.gfx.Screen;
import com.ludum.gfx.Textures;

public class Projectile {
	private Point2D.Double location;
	private double speed;
	private double theta;
	private double damage;
	
	public boolean alive;
	
	public Projectile(Point2D.Double location, double speed, double theta, double damage) {
		this.location = location;
		this.speed = speed;
		this.theta = theta;
		this.damage = damage;
		
		this.alive = true;
	}
	
	public void update(Game game) {
		double dx = Math.cos(theta) * speed;
		double dy = Math.sin(theta) * speed;
		
		if(alive) {
			location.x += dx;
			location.y += dy;
			
			// Check for a collision with the player.
			double a = (game.player.location.x - location.x);
			double b = (game.player.location.y - location.y);
			double dist = Math.sqrt((a * a) + (b * b));
			if(dist <= 20) {
				alive = false;
				
				if(SkullBarrier.skulls > 0) {
					SkullBarrier.skulls--;
					
					// Find and delete a skull effect.
					synchronized(game.currentLevel.spellEffects) {
						Iterator<SpellEffect> it = game.currentLevel.spellEffects.iterator();
						while(it.hasNext()) {
							SpellEffect sp = it.next();
							
							if(sp.alive && (sp.spell.getName().equals(SkullBarrier.NAME))) {
								sp.alive = false;
								sp.light.killLight();
								it.remove();
								break;
							}
						}
					}
				} else {
					game.player.takeDamage(damage);
				}
			}
			
			// Check to see if it has gone out of bounds.
			if(!Screen.inBounds((int)location.x, (int)location.y)) {
				alive = false;
			}
		}
	}
	
	public void render(Graphics2D g2d) {
		if(alive) {
			if(Textures.ARROW.img != null) {
				int x = (int)(location.x - (Textures.ARROW.img.getWidth() / 2));
				int y = (int)(location.y - (Textures.ARROW.img.getHeight() / 2));
				AffineTransform saved = g2d.getTransform();
				g2d.setTransform(AffineTransform.getRotateInstance((theta + (Math.PI / 2)), location.x, location.y));
				g2d.drawImage(Textures.ARROW.img, x, y, null);
				
				g2d.setTransform(saved);
			} else {
				g2d.setColor(Color.YELLOW);
				g2d.fillOval((int)(location.x - 2), (int)(location.y - 2), 4, 4);
			}
		}
	}
}
