package com.ludum.entities.factories;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import com.ludum.entities.EnemyFactory;
import com.ludum.entities.enemies.EnemyType;
import com.ludum.gfx.Textures;

public class Farm extends EnemyFactory {
	public Farm(Point2D.Double position) {
		super("Farm", 1000, 2000, 15, position, 
				new Point2D.Double((position.x + 30), (position.y + 45)));
		
		addEnemyType(EnemyType.PEASANT);
	}
	
	@Override
	public void render(Graphics2D g2d) {
		Dimension size = new Dimension(Textures.FARM.img.getWidth(),
									   Textures.FARM.img.getHeight());
		int hW = (int)((health / maxHealth) * (size.width - 4));
		
		if(Textures.FARM.img != null) {
			int x = (int)(position.x - (size.width / 2));
			int y = (int)(position.y - (size.height / 2));
			g2d.drawImage(Textures.FARM.img, x, y, null);
		}
		
		// Draw a "health" bar.
		g2d.setColor(Color.BLACK);
		g2d.fillRect((int)(position.x - (size.width / 2)), 
					 (int)(position.y - (size.height / 2) - 16), 
					 size.width, 16);
		g2d.setColor(Color.RED);
		g2d.fillRect((int)(position.x - (size.width / 2) + 2), 
					 (int)(position.y - (size.height / 2) - 14), 
					 hW, 12);
	}
}
