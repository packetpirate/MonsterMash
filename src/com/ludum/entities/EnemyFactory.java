package com.ludum.entities;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.ludum.Game;
import com.ludum.entities.enemies.Enemy;
import com.ludum.entities.enemies.EnemyType;

public class EnemyFactory {
	private Random r;
	
	protected String name;
	protected double health;
	protected double maxHealth;
	protected long lastSpawn;
	protected long spawnRate;
	protected int enemiesSpawned;
	public void enemyDeath() { enemiesSpawned--; }
	protected int spawnCap;
	protected Dimension size;
	protected Point2D.Double position;
	public Point2D.Double getPosition() { return position; }
	protected Point2D.Double spawn;
	public Point2D.Double getSpawnLocation() { return spawn; }
	protected List<EnemyType> enemyTypes;
	protected Light light;
	public Light getLight() { return light; }
	public void addLight(Light light) { this.light = light; }
	
	public EnemyFactory(String name, double health, long spawnRate, int spawnCap, Dimension size, Point2D.Double position, Point2D.Double spawn) {
		this.r = new Random();
		
		this.name = name;
		this.health = health;
		this.maxHealth = health;
		this.lastSpawn = Game.time.getElapsedMillis();
		this.spawnRate = spawnRate;
		this.enemiesSpawned = 0;
		this.spawnCap = spawnCap;
		this.size = size;
		this.position = position;
		this.spawn = spawn;
		this.enemyTypes = new ArrayList<>();
		this.light = null;
	}
	
	public void render(Graphics2D g2d) {
		
	}
	
	public boolean isAlive() {
		return (health > 0);
	}
	
	public void takeDamage(double amnt) {
		health -= amnt;
	}
	
	public boolean collision(Point2D.Double target) {
		return ((target.x >= (position.x - (size.width / 2))) && 
				(target.x <= (position.x + (size.width / 2))) && 
				(target.y >= (position.y - (size.height / 2))) && 
				(target.y <= (position.y + (size.height / 2))));
	}
	
	public void addEnemyType(EnemyType type) {
		enemyTypes.add(type);
	}
	
	public boolean canSpawn() {
		return (isAlive() && (enemiesSpawned < spawnCap) && (Game.time.getElapsedMillis() >= (lastSpawn + spawnRate)));
	}
	
	public Enemy spawnEnemy() {
		// Return a random enemy type from the available types in the list.
		if(!enemyTypes.isEmpty()) {
			lastSpawn = Game.time.getElapsedMillis();
			enemiesSpawned++;
			return EnemyType.getEnemy(enemyTypes.get(r.nextInt(enemyTypes.size())), this, spawn);
		}
		
		return null;
	}
}
