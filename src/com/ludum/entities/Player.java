package com.ludum.entities;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ludum.Game;
import com.ludum.entities.minions.Minion;
import com.ludum.entities.spells.EldritchBolt;
import com.ludum.entities.spells.Fireball;
import com.ludum.entities.spells.LightningBolt;
import com.ludum.entities.spells.Spell;
import com.ludum.entities.spells.summons.SummonSkeleton;
import com.ludum.entities.spells.summons.SummonZombie;
import com.ludum.gfx.Textures;

public class Player {
	private final long INVINCIBILITY_TIME = 100;
	private final double MANA_REGEN_RATE = 0.5;
	
	public static double MAX_HEALTH = 100;
	public static double MAX_MANA = 100;
	public static int MAX_SUMMONS = 15;
	
	private double health;
	private long lastDamageTaken;
	public double currentHealth() { return health; }
	public boolean isAlive() {
		return (health > 0);
	}
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
			int carryOver = experience - experienceToLevel;
			experience = carryOver;
			level++;
		}
	}
	
	private List<Spell> spells;
	public List<Spell> getSpells() { return spells; }
	private int selectedSpell;
	public int getSelectedSpell() { return selectedSpell; }
	public Spell getCurrentSpell() {
		return spells.get(selectedSpell);
	}
	public void selectSpell(int slot) {
		if(slot < spells.size()) selectedSpell = slot;
	}
	public void castSpell(Game game) {
		getCurrentSpell().cast(game);
	}
	
	private int summonCap;
	public int getSummonPoints() { return summonCap; }
	private List<Minion> minions;
	public List<Minion> getMinions() { return minions; }
	public void summon(Minion minion) {
		if(summonCap >= minion.getSummmonCost()) {
			minions.add(minion);
			summonCap -= minion.getSummmonCost();
		}
	}
	
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
		spells.add(new LightningBolt());
		spells.add(new SummonZombie());
		spells.add(new SummonSkeleton());
		
		summonCap = Player.MAX_SUMMONS;
		minions = new ArrayList<>();
		
		location = new Point2D.Double((Game.WIDTH / 2), (Game.HEIGHT / 2));
		light = LightType.createLight(location, LightType.PLAYER);
		game.lightFactory.lights.add(light);
	}
	
	public void resetPlayer(Game game) {
		health = MAX_HEALTH;
		mana = 50;
		level = 1;
		experience = 0;
		experienceToLevel = 300;
		
		selectedSpell = 0;
		
		summonCap = Player.MAX_SUMMONS;
		minions.clear();
		
		location = new Point2D.Double((Game.WIDTH / 2), (Game.HEIGHT / 2));
		light = game.lightFactory.createLight(location, LightType.PLAYER);
	}
	
	public void update(Game game) {
		mana += MANA_REGEN_RATE;
		if(mana >= MAX_MANA) mana = MAX_MANA;
		
		for(Spell sp : spells) {
			sp.update(game);
		}
		
		synchronized(minions) {
			if(!minions.isEmpty()) {
				Iterator<Minion> it = minions.iterator();
				while(it.hasNext()) {
					Minion m = it.next();
					
					m.update(game);
					if(!m.isAlive()) {
						summonCap += m.getSummmonCost();
						it.remove();
						continue;
					}
				}
			}
		}
	}
	
	public void render(Graphics2D g2d) {
		if(Textures.NECROMANCER.img != null) {
			int x = (int)(location.x - (Textures.NECROMANCER.img.getWidth() / 2));
			int y = (int)(location.y - (Textures.NECROMANCER.img.getHeight() / 2));
			g2d.drawImage(Textures.NECROMANCER.img, x, y, null);
		}
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
}
