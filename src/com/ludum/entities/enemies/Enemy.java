package com.ludum.entities.enemies;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.ludum.Game;
import com.ludum.entities.EnemyFactory;
import com.ludum.entities.Projectile;
import com.ludum.entities.minions.Minion;

public class Enemy {
	public EnemyFactory origin;
	
	protected String name;
	protected double health;
	protected int experience;
	protected double damage;
	public double getDamage() { return damage; }
	public Point2D.Double location;
	public List<Projectile> projectiles;
	
	public Enemy(EnemyFactory origin, String name, double health, int experience, double damage, Point2D.Double location) { // number one :)
		this.origin = origin;
		this.name = name;
		this.health = health;
		this.experience = experience;
		this.damage = damage;
		this.location = new Point2D.Double(location.x, location.y);
		this.projectiles = Collections.synchronizedList(new ArrayList<>());
	}
	
	public void reset() {
		projectiles.clear();
	}
	
	public void update(Game game) {
		synchronized(game.player.getMinions()) {
			if(!game.player.getMinions().isEmpty()) {
				Iterator<Minion> it = game.player.getMinions().iterator();
				while(it.hasNext()) {
					Minion m = it.next();
					
					double a = (m.location.x - location.x);
					double b = (m.location.y - location.y);
					double dist = Math.sqrt((a * a) + (b * b));
					if(m.isAlive() && m.canTakeDamage() && (dist <= 20)) {
						m.takeDamage(damage);
					}
				}
			}
		}
		
		synchronized(projectiles) {
			if(!projectiles.isEmpty()) {
				Iterator<Projectile> it = projectiles.iterator();
				while(it.hasNext()) {
					Projectile p = it.next();
					
					p.update(game);
					if(!p.alive) {
						it.remove();
						continue;
					}
				}
			}
		}
	}
	
	public void render(Graphics2D g2d) {
		synchronized(projectiles) {
			if(!projectiles.isEmpty()) {
				Iterator<Projectile> it = projectiles.iterator();
				while(it.hasNext()) {
					Projectile p = it.next();
					
					if(p.alive) {
						p.render(g2d);
						continue;
					}
				}
			}
		}
	}
	
	public boolean isAlive() {
		return (health > 0);
	}
	
	public int getExperience() {
		return experience;
	}
	
	public void takeDamage(double amnt) {
		this.health -= amnt;
	}
}