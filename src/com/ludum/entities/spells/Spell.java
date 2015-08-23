package com.ludum.entities.spells;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import com.ludum.Game;

public class Spell {
	protected String name;
	public String getName() { return name; }
	
	protected long lastCast;
	protected long cooldown;
	protected double manaCost;
	protected double damage;
	
	public Spell(String name, long cooldown, int manaCost, double damage) {
		this.name = name;
		this.lastCast = Game.time.getElapsedMillis() - cooldown;
		this.cooldown = cooldown;
		this.manaCost = manaCost;
		this.damage = damage;
	}
	
	public void update(Game game) {
		
	}
	
	public void renderIcon(Graphics2D g2d, Point2D.Double position) {
		// Override this.
	}
	
	public boolean canCast() {
		return (Game.time.getElapsedMillis() >= (lastCast + cooldown));
	}
	
	public void cast(Game game) {
		
	}
}
