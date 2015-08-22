package com.ludum.entities.enemies;

import java.awt.geom.Point2D;

public enum EnemyType {
	PEASANT();
	
	public static Enemy getEnemy(EnemyType type, Point2D.Double location) {
		if(type == EnemyType.PEASANT) return new Peasant(location); 
			
		return null;
	}
}
