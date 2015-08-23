package com.ludum.entities.enemies;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import com.ludum.Game;
import com.ludum.entities.EnemyFactory;

public class Soldier extends Enemy{
	public static final int SPEED = 2;
	
	public Soldier(EnemyFactory origin, Point2D.Double spawnLocation) {
		super(origin, "Soldier", 150, 50, 3, spawnLocation);
	}
	
	@Override
	public void update(Game game) {
		super.update(game);
		
		if(isAlive()) {
			// Moves within firing range of player and shoots.
			double theta = Math.atan2((game.player.location.y - location.y), (game.player.location.x - location.x));
			double dx = Math.cos(theta) * Archer.SPEED;
			double dy = Math.sin(theta) * Archer.SPEED;
	
			location.x += dx;
			location.y += dy;
		}
	}
	
	@Override
	public void render(Graphics2D g2d) {
		super.render(g2d);
		
		g2d.setColor(Color.BLUE);
		g2d.fillOval((int)(location.x - 10), (int)(location.y - 10), 20, 20);
	}
}
