package com.ludum.entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import com.ludum.Game;
import com.ludum.gfx.Screen;

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
				game.player.takeDamage(damage);
			}
			
			// Check to see if it has gone out of bounds.
			if(!Screen.inBounds((int)location.x, (int)location.y)) {
				alive = false;
			}
		}
	}
	
	public void render(Graphics2D g2d) {
		if(alive) {
			g2d.setColor(Color.YELLOW);
			g2d.fillOval((int)(location.x - 2), (int)(location.y - 2), 4, 4);
		}
	}
}
