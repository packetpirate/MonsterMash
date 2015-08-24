package com.ludum.entities.spells;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import com.ludum.Game;

public class Spell {
	public String getName() { return ""; }
	protected long lastCast;
	protected long cooldown;
	protected double manaCost;
	protected double damage;
	protected boolean activated;
	public void activate() { activated = true; }
	public void deactivate() { activated = false; }
	public boolean isActive() { return activated; }
	
	public Spell(long cooldown, int manaCost, double damage) {
		this.lastCast = Game.time.getElapsedMillis() - cooldown;
		this.cooldown = cooldown;
		this.manaCost = manaCost;
		this.damage = damage;
		this.activated = false;
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
