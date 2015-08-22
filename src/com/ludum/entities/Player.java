package com.ludum.entities;

import java.awt.geom.Point2D;

import com.ludum.Game;
import com.ludum.entities.spells.Fireball;
import com.ludum.entities.spells.Spell;

public class Player {
	private final long INVINCIBILITY_TIME = 100;
	private final double MANA_REGEN_RATE = 0.5;
	
	public static double MAX_HEALTH = 100;
	public static double MAX_MANA = 100;
	
	private double health;
	private long lastDamageTaken;
	public double currentHealth() { return health; }
	public boolean canTakeDamage() {
		return (Game.gameTime.getElapsedMillis() >= (lastDamageTaken + INVINCIBILITY_TIME));
	}
	public void takeDamage(double amnt) { 
		health -= amnt;
		if(health < 0) health = 0;
		lastDamageTaken = Game.gameTime.getElapsedMillis();
	}
	public void heal(double amnt) {
		health += amnt;
		if(health > MAX_HEALTH) health = MAX_HEALTH;
	}
	
	private double mana;
	public double currentMana() { return mana; }
	public void useMana(double amnt) { mana -= amnt; }
	public void restoreMana(double amnt) {
		mana += amnt;
		if(mana > MAX_MANA) mana = MAX_MANA;
	}
	
	private Spell [] spells;
	private int selectedSpell;
	
	public Point2D.Double location;
	
	public Player(Game game) {
		health = MAX_HEALTH;
		mana = 50;
		
		spells = new Spell[10];
		for(int i = 0; i < 10; i++) {
			spells[i] = new Spell("null", 0, 0, 0);
		}
		selectedSpell = 0;
		
		spells[0] = new Fireball();
		
		location = new Point2D.Double();
	}
	
	public void update() {
		mana += MANA_REGEN_RATE;
		if(mana >= MAX_MANA) mana = MAX_MANA;
	}
	
	public void move(double dx, double dy) {
		location.x += dx;
		location.y += dy;
	}
	
	public void moveTo(Point2D.Double target) {
		location.x = target.x;
		location.y = target.y;
	}
	
	public Spell getCurrentSpell() {
		return spells[selectedSpell];
	}
	
	public void selectSpell(int slot) {
		selectedSpell = slot;
	}
	
	public void castSpell(Game game) {
		getCurrentSpell().cast(game);
	}
	
	public boolean isAlive() {
		return (health > 0);
	}
}
