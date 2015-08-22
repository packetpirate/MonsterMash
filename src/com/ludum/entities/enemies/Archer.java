package com.ludum.entities.enemies;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import com.ludum.Game;
import com.ludum.entities.EnemyFactory;
import com.ludum.entities.Projectile;

public class Archer extends Enemy {
	public static final int SPEED = 1;
	private static final double MIN_DISTANCE = 100;
	private static final double FIRING_RANGE = 200;
	private static final long FIRING_RATE = 2000;
	
	private long lastFire;
	private boolean canFire() {
		return (isAlive() && (Game.time.getElapsedMillis() >= (lastFire + Archer.FIRING_RATE)));
	}
	
	public Archer(EnemyFactory origin, Point2D.Double spawnLocation) {
		super(origin, "Archer", 80, 40, 3, spawnLocation);
		
		lastFire = Game.time.getElapsedMillis();
	}
	
	@Override
	public void update(Game game) {
		super.update(game);
		
		// Moves within firing range of player and shoots.
		double theta = Math.atan2((game.player.location.y - location.y), (game.player.location.x - location.x));
		double dx = Math.cos(theta) * Archer.SPEED;
		double dy = Math.sin(theta) * Archer.SPEED;
		
		double a = (location.x - game.player.location.x);
		double b = (location.y - game.player.location.y);
		double dist = Math.sqrt((a * a) + (b * b));
		
		if(dist > Archer.MIN_DISTANCE) {
			location.x += dx;
			location.y += dy;
		}
		
		if((dist <= Archer.FIRING_RANGE) && canFire()) {
			double ang = Math.atan2((game.player.location.y - location.y), (game.player.location.x - location.x));
			projectiles.add(new Projectile(new Point2D.Double(location.x, location.y), 5, ang, 5));
			lastFire = Game.time.getElapsedMillis();
		}
	}
	
	@Override
	public void render(Graphics2D g2d) {
		super.render(g2d);
		
		g2d.setColor(Color.YELLOW);
		g2d.fillOval((int)(location.x - 10), (int)(location.y - 10), 20, 20);
	}
}
