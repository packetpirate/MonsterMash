package com.ludum.entities.factories;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import com.ludum.entities.EnemyFactory;
import com.ludum.entities.enemies.EnemyType;
import com.ludum.gfx.Textures;

public class Chapel extends EnemyFactory {
	public Chapel(Point2D.Double position) {
		super("Chapel", 1000, 10000, 1, position, 
				new Point2D.Double(position.x, (position.y + 44)));
		
		addEnemyType(EnemyType.CLERIC);
	}
	
	@Override
	public void render(Graphics2D g2d) {
		Dimension size = new Dimension(Textures.CHAPEL.img.getWidth(),
									   Textures.CHAPEL.img.getHeight());
		int hW = (int)((health / maxHealth) * (size.width - 4));
		
		if(Textures.CHAPEL.img != null) {
			int x = (int)(position.x - (size.width / 2));
			int y = (int)(position.y - (size.height / 2));
			g2d.drawImage(Textures.CHAPEL.img, x, y, null);
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
