package com.ludum.entities;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.ludum.Game;
import com.ludum.Message;
import com.ludum.entities.minions.Minion;
import com.ludum.entities.spells.EldritchBolt;
import com.ludum.entities.spells.Fireball;
import com.ludum.entities.spells.LightningBolt;
import com.ludum.entities.spells.Spell;
import com.ludum.entities.spells.summons.SummonSkeleton;
import com.ludum.entities.spells.summons.SummonWraith;
import com.ludum.entities.spells.summons.SummonZombie;
import com.ludum.gfx.Textures;

public class Player {
	private final long INVINCIBILITY_TIME = 1000;
	private final double MANA_REGEN_RATE = 0.5; // set back to 0.5 when not testing
	
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
	public void addExperience(Game game, int exp) {
		experience += exp;
		if(experience >= experienceToLevel) {
			// Level up!
			int carryOver = experience - experienceToLevel;
			experience = carryOver;
			level++;
			game.messages.add(new Message("Level Up!", new Point2D.Double(location.x, (location.y - 32)), 3000) {
				@Override
				public void update(Game game) {
					this.center.x = game.player.location.x;
					this.center.y = game.player.location.y - 32;
				}
			});
			
			// Add to maximum health and mana.
			Player.MAX_HEALTH += 20;
			health = Player.MAX_HEALTH;
			Player.MAX_MANA += 20;
			mana = Player.MAX_MANA;
			
			// Determine new experience required to level.
			experienceToLevel = level * 150;
			
			// Get level up reward!
			switch(level) {
				case 2: {
					spells.get("Summon Zombie").activate();
					spellCount++;
					break;
				}
				case 3: {
					spells.get("Fireball").activate();
					spellCount++;
					break;
				}
				case 5: {
					spells.get("Lightning Bolt").activate();
					spellCount++;
					break;
				}
				case 7: {
					spells.get("Summon Skeleton").activate();
					spellCount++;
					break;
				}
				case 10: {
					spells.get("Summon Wraith").activate();
					spellCount++;
					break;
				}
				default:
					break;
			}
			
			selectSpell(0);
		}
	}
	
	private int spellCount;
	public int getSpellCount() { return spellCount; }
	private Map<String, Spell> spells;
	public Map<String, Spell> getSpells() { return spells; }
	private int selectedSpell;
	public int getSelectedSpell() { return selectedSpell; }
	public Spell getCurrentSpell() {
		List<String> spellNames = new ArrayList<>();
		synchronized(spells) {
			Iterator it = spells.entrySet().iterator();
			while(it.hasNext()) {
				Map.Entry<String, Spell> pair = (Map.Entry<String, Spell>) it.next();
				if(pair.getValue().isActive()) {
					spellNames.add(pair.getKey());
				}
			}
		}
		return spells.get(spellNames.get(selectedSpell));
	}
	public void selectSpell(int slot) {
		if(slot < spellCount) selectedSpell = slot;
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
		experienceToLevel = 150;
		
		spells = new LinkedHashMap<>();
		selectedSpell = 0;
		spells.put(EldritchBolt.NAME, new EldritchBolt());
		spells.put(Fireball.NAME, new Fireball());
		spells.put(LightningBolt.NAME, new LightningBolt());
		spells.put(SummonZombie.NAME, new SummonZombie());
		spells.put(SummonSkeleton.NAME, new SummonSkeleton());
		spells.put(SummonWraith.NAME, new SummonWraith());
		spells.get(EldritchBolt.NAME).activate();
		spellCount = 1;
		
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
		experienceToLevel = 150;
		
		selectedSpell = 0;
		Iterator it = spells.entrySet().iterator();
		while(it.hasNext()) {
			Map.Entry<String, Spell> pair = (Map.Entry<String, Spell>) it.next();
			if(!pair.getKey().equals(EldritchBolt.NAME)) {
				pair.getValue().deactivate();
			}
		}
		spellCount = 1;
		
		summonCap = Player.MAX_SUMMONS;
		minions.clear();
		
		location = new Point2D.Double((Game.WIDTH / 2), (Game.HEIGHT / 2));
		light = game.lightFactory.createLight(location, LightType.PLAYER);
	}
	
	public void update(Game game) {
		mana += MANA_REGEN_RATE;
		if(mana >= MAX_MANA) mana = MAX_MANA;
		
		synchronized(spells) {
			Iterator it = spells.entrySet().iterator();
			while(it.hasNext()) {
				Map.Entry<String, Spell> pair = (Map.Entry<String, Spell>) it.next();
				pair.getValue().update(game);
			}
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
