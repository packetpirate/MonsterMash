package com.ludum.entities.factories;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import com.ludum.entities.EnemyFactory;
import com.ludum.entities.enemies.EnemyType;

public class Barracks extends EnemyFactory {
	public Barracks(Point2D.Double position) {
		super("Barracks", 1000, 4000, 5, position, 
				new Point2D.Double((position.x - 20), (position.y + 30)));
		
		addEnemyType(EnemyType.ARCHER);
		addEnemyType(EnemyType.SOLDIER);
		addEnemyType(EnemyType.CLERIC);
	}
	
	@Override
	public void render(Graphics2D g2d) {
		g2d.setColor(Color.GRAY);
		g2d.fillRect((int)(position.x - 50), (int)(position.y - 50), 100, 100);
		
		g2d.setColor(Color.BLACK);
		g2d.fillRect((int)(spawn.x - 20), (int)(spawn.y - 20), 40, 40);
		
		// Draw a "health" bar.
		int hW = (int)((health / maxHealth) * 82);
		Dimension size = new Dimension(100, 100);
		g2d.fillRect((int)(position.x - ((size.width / 2) - 5)), (int)(position.y - ((size.width / 2) - 5)), 90, 20);
		g2d.setColor(Color.RED);
		g2d.fillRect((int)(position.x - ((size.width / 2) - 9)), (int)(position.y - ((size.width / 2) - 9)), hW, 12);
	}
}
