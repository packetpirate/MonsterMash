package com.ludum.entities.enemies;

import java.awt.geom.Point2D;

import com.ludum.entities.EnemyFactory;

public enum EnemyType {
	PEASANT(),
	ARCHER(),
	SOLDIER(),
	CLERIC();
	
	public static Enemy getEnemy(EnemyType type, EnemyFactory origin, Point2D.Double location) {
		if(type == EnemyType.PEASANT) return new Peasant(origin, location);
		if(type == EnemyType.ARCHER) return new Archer(origin, location);
		if(type == EnemyType.SOLDIER) return new Soldier(origin, location);
		if(type == EnemyType.CLERIC) return new Cleric(origin, location);
			
		return null;
	}
}
