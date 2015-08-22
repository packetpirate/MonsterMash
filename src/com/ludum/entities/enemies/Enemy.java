package com.ludum.entities.enemies;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import com.ludum.Game;

public class Enemy {
	protected String name;
	protected double health;
	protected int experience;
	protected double damage;
	public double getDamage() { return damage; }
	public Point2D.Double location;
	
	public Enemy(String name, double health, int experience, double damage, Point2D.Double location) { // number one :)
		this.name = name;
		this.health = health;
		this.experience = experience;
		this.damage = damage;
		this.location = new Point2D.Double(location.x, location.y);
	}
	
	public void update(Game game) {
		
	}
	
	public void render(Graphics2D g2d) {
		
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