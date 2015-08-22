package com.ludum.entities.spells;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import com.ludum.Game;

public class SpellEffect {
	public Point2D.Double location;
	public boolean alive;
	public double theta;
	
	public SpellEffect(Point2D.Double location, Point2D.Double target) {
		this.location = location;
		this.alive = true;
		this.theta = Math.atan2((target.y - location.y), (target.x - location.x));
	}
	
	public void update(Game game) {
		
	}
	
	public void render(Graphics2D g2d) {
		
	}
}
