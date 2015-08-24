package com.ludum.entities.enemies;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.ludum.Game;
import com.ludum.entities.EnemyFactory;
import com.ludum.entities.Projectile;
import com.ludum.entities.Status;
import com.ludum.entities.minions.Minion;
import com.ludum.gfx.Screen;

public class Enemy {
	public EnemyFactory origin;
	
	protected String name;
	public String getName() { return name; }
	protected double health;
	protected int experience;
	protected double damage;
	public double getDamage() { return damage; }
	public Point2D.Double location;
	protected Set<Status> statuses;
	public void addStatus(Status status) { statuses.add(status); }
	public void removeStatus(String name) { 
		for(Status s : statuses) {
			if(s.name == name) statuses.remove(s);
		}
	}
	public boolean hasStatus(String name) {
		for(Status s : statuses) {
			if(s.name == name) return true;
		}
		return false;
	}
	public Status getStatus(String name) {
		for(Status s : statuses) {
			if(s.name == name) return s;
		}
		return null;
	}
	public List<Projectile> projectiles;
	
	public Enemy(EnemyFactory origin, String name, double health, int experience, double damage, Point2D.Double location) { // number one :)
		this.origin = origin;
		this.name = name;
		this.health = health;
		this.experience = experience;
		this.damage = damage;
		this.location = new Point2D.Double(location.x, location.y);
		this.statuses = new HashSet<>();
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
					
					double dist = Screen.dist(location, m.location);
					if(m.isAlive() && m.canTakeDamage() && (dist <= 20)) {
						m.takeDamage(damage);
					}
				}
			}
		}
	}
	
	public void render(Graphics2D g2d, Game game) {
		
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