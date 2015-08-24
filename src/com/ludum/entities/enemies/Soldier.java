package com.ludum.entities.enemies;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import com.ludum.Game;
import com.ludum.entities.EnemyFactory;
import com.ludum.gfx.Textures;

public class Soldier extends Enemy{
	public static final int SPEED = 2;
	
	public Soldier(EnemyFactory origin, Point2D.Double spawnLocation) {
		super(origin, "Soldier", 150, 50, 15, spawnLocation);
	}
	
	@Override
	public void update(Game game) {
		super.update(game);
		
		if(isAlive()) {
			double theta = Math.atan2((game.player.location.y - location.y), (game.player.location.x - location.x));
			double dx = Math.cos(theta) * Soldier.SPEED;
			double dy = Math.sin(theta) * Soldier.SPEED;
	
			location.x += dx;
			location.y += dy;
		}
	}
	
	@Override
	public void render(Graphics2D g2d, Game game) {
		if(Textures.SOLDIER.img != null) {
			int x = (int)(location.x - (Textures.SOLDIER.img.getWidth() / 2));
			int y = (int)(location.y - (Textures.SOLDIER.img.getHeight() / 2));

			// Determine which way the soldier should face.
			if(game.player.location.x >= location.x) {
				// Face to the right. (flip the image)
				g2d.drawImage(Textures.SOLDIER.img, 
							  (x + Textures.SOLDIER.img.getWidth()), y,
							  -Textures.SOLDIER.img.getWidth(),
							  Textures.SOLDIER.img.getHeight(), null);
			} else {
				// Face to the left. (just draw normally)
				g2d.drawImage(Textures.SOLDIER.img, x, y, null);
			}
		} else {
			g2d.setColor(Color.BLUE);
			g2d.fillOval((int)(location.x - 10), (int)(location.y - 10), 20, 20);
		}
	}
}
