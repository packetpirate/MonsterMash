package com.ludum.entities;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import com.ludum.Game;
import com.ludum.entities.spells.Fireball;
import com.ludum.entities.spells.EldritchBolt;
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
		return (Game.time.getElapsedMillis() >= (lastDamageTaken + INVINCIBILITY_TIME));
	}
	public void takeDamage(double amnt) { 
		health -= amnt;
		if(health < 0) health = 0;
		lastDamageTaken = Game.time.getElapsedMillis();
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
	
	private int level;
	public int getLevel() { return level; }
	private int experience;
	public int getExperience() { return experience; }
	private int experienceToLevel;
	public int getExperienceToLevel() { return experienceToLevel; }
	public void addExperience(int exp) {
		experience += exp;
		if(experience >= experienceToLevel) {
			int carryOver = experienceToLevel % experience;
			experience = carryOver;
			level++;
		}
	}
	
	private List<Spell> spells;
	public List<Spell> getSpells() { return spells; }
	private int selectedSpell;
	
	public Point2D.Double location;
	public Light light;
	
	public Player(Game game) {
		health = MAX_HEALTH;
		mana = 50;
		level = 1;
		experience = 0;
		experienceToLevel = 300;
		
		spells = new ArrayList<>();
		selectedSpell = 0;
		spells.add(new EldritchBolt());
		spells.add(new Fireball());
		
		location = new Point2D.Double((Game.WIDTH / 2), (Game.HEIGHT / 2));
		light = LightType.createLight(location, LightType.PLAYER);
		game.lightFactory.lights.add(light);
	}
	
	public void update() {
		mana += MANA_REGEN_RATE;
		if(mana >= MAX_MANA) mana = MAX_MANA;
	}
	
	public void move(double dx, double dy) {
		location.x += dx;
		location.y += dy;
		light.location.x = location.x;
		light.location.y = location.y;
	}
	
	public void moveTo(Point2D.Double target) {
		location.x = target.x;
		location.y = target.y;
	}
	
	public Spell getCurrentSpell() {
		return spells.get(selectedSpell);
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
