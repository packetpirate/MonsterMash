package com.ludum.entities.minions;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.Random;

import com.ludum.Game;
import com.ludum.entities.Status;
import com.ludum.entities.enemies.Enemy;
import com.ludum.entities.spells.summons.SummonSkeleton;
import com.ludum.gfx.Screen;

public class Minion {
	private static final long INVINCIBILITY_TIME = 1000;
	
	public Point2D.Double location;
	
	private String name;
	public String getName() { return name; }
	private double health;
	public boolean isAlive() {
		return (health > 0);
	}
	public boolean canTakeDamage() {
		return (isAlive() && (Game.time.getElapsedMillis() >= (lastDamage + Minion.INVINCIBILITY_TIME)));
	}
	public void takeDamage(double amnt) {
		health -= amnt;
		lastDamage = Game.time.getElapsedMillis();
	}
	
	private double damage;
	private long lastDamage;
	public double getDamage() { return damage; }
	
	private double speed;
	private int summonCost;
	public int getSummmonCost() { return summonCost; }
	
	public Minion(String name, Point2D.Double location, double health, double damage, double speed, int summonCost) {
		this.name = name;
		this.location = location;
		this.health = health;
		this.damage = damage;
		this.lastDamage = Game.time.getElapsedMillis();
		this.speed = speed;
		this.summonCost = summonCost;
	}
	
	public void update(Game game) {
		synchronized(game.currentLevel.enemies) {
			if(!game.currentLevel.enemies.isEmpty()) {
				Enemy target = game.currentLevel.enemies.get(0);
				
				Iterator<Enemy> it = game.currentLevel.enemies.iterator();
				while(it.hasNext()) {
					Enemy e = it.next();
					
					double dist = Screen.dist(location, e.location);
					if(dist < Screen.dist(location, target.location)) target = e;
					
					if(name.equals("Skeleton") && e.getName().equals("Peasant") && 
					   !e.hasStatus("fear") && (dist <= SummonSkeleton.FEAR_RADIUS)) {
						Random r = new Random();
						if((r.nextInt(100) + 1) <= SummonSkeleton.FEAR_CHANCE) e.addStatus(new Status("fear", 3000));
					}
					
					if(e.isAlive() && (dist <= 20)) {
						e.takeDamage(damage);
					}
				}
				
				// Lock on to the nearest enemy.
				double theta = Math.atan2((target.location.y - location.y), 
										  (target.location.x - location.x));
				double dx = Math.cos(theta) * speed;
				double dy = Math.sin(theta) * speed;
				
				location.x += dx;
				location.y += dy;
			} else {
				// Make them follow the player if there are no enemies on screen.
				double dist = Screen.dist(location, game.player.location);
				if(dist > 100) {
					double theta = Math.atan2((game.player.location.y - location.y), 
											  (game.player.location.x - location.x));
					double dx = Math.cos(theta) * speed;
					double dy = Math.sin(theta) * speed;
					
					location.x += dx;
					location.y += dy;
				}
			}
		}
	}
	
	public void render(Graphics2D g2d) {
		g2d.setColor(Color.WHITE);
		g2d.fillOval((int)(location.x - 10), (int)(location.y - 10), 20, 20);
	}
}
