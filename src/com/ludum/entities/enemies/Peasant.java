package com.ludum.entities.enemies;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import com.ludum.Game;
import com.ludum.entities.EnemyFactory;

// Most basic enemy unit.
public class Peasant extends Enemy {
	public static final int SPEED = 2;
	
	public Peasant(EnemyFactory origin, Point2D.Double spawnLocation) {
		super(origin, "Peasant", 50, 10, 2, spawnLocation);
	}
	
	@Override
	public void update(Game game) {
		// Basic enemy. Just travels toward the player.
		double theta = Math.atan2((game.player.location.y - location.y), (game.player.location.x - location.x));
		double dx = Math.cos(theta) * Peasant.SPEED;
		double dy = Math.sin(theta) * Peasant.SPEED;
		
		location.x += dx;
		location.y += dy;
	}
	
	@Override
	public void render(Graphics2D g2d) {
		g2d.setColor(Color.GRAY);
		g2d.fillOval((int)(location.x - 10), (int)(location.y - 10), 20, 20);
	}
}