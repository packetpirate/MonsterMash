package com.ludum.entities.spells;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.Iterator;

import com.ludum.Game;
import com.ludum.entities.EnemyFactory;
import com.ludum.entities.Light;
import com.ludum.entities.enemies.Enemy;

public class SpellEffect {
	public Spell spell;
	public Point2D.Double location;
	public Light light;
	public boolean alive;
	public double theta;
	
	public SpellEffect(Spell spell, Point2D.Double location, Point2D.Double target) {
		this.spell = spell;
		this.location = location;
		this.light = null;
		this.alive = true;
		this.theta = Math.atan2((target.y - location.y), (target.x - location.x));
	}
	
	public void update(Game game) {
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
						e.takeDamage(spell.damage);
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
						ef.takeDamage(spell.damage);
					}
				}
			}
		}
	}
	
	public void render(Graphics2D g2d) {
		
	}
	
	public boolean hasLight() {
		return (light != null);
	}
}
